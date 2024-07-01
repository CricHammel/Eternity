package de.cric_hammel.eternity.infinity.items.misc.teleport;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.cric_hammel.eternity.infinity.items.CustomItem;
import de.cric_hammel.eternity.infinity.util.ActionUtils;
import de.cric_hammel.eternity.infinity.util.SoundUtils;

public class TeleportRailgun extends CustomItem {
	
	private static final String COOLDOWN_KEY = "eternity_railgun_cooldown";
	private static final String CHARGES_PREFIX = "Capsules: ";
	private static final int TELEPORT_BLOCKS = 15;
	private static final double TELEPORT_STEP = 0.2;

	public TeleportRailgun() {
		super(Material.GOLDEN_HOE, ChatColor.GOLD + "Teleport Railgun", "Accelerates Teleport Capsules");
	}
	
	@Override
	public ItemStack getItem() {
		ItemStack gun = super.getItem();
		ItemMeta gunMeta = gun.getItemMeta();
		gunMeta.setUnbreakable(true);
		List<String> lore = gunMeta.getLore();
		lore.add(CHARGES_PREFIX + 0);
		gunMeta.setLore(lore);
		gun.setItemMeta(gunMeta);
		return gun;
	}
	
	private int getCharges(ItemStack item) {
		if (!super.isItem(item)) {
			throw new IllegalArgumentException("This is not a Teleport Railgun");
		}
		
		List<String> lore = item.getItemMeta().getLore();
		String charges = lore.get(2);
		return Integer.parseInt(charges.substring(CHARGES_PREFIX.length()));
	}
	
	private void setCharges(ItemStack item, int charges) {
		if (!super.isItem(item)) {
			throw new IllegalArgumentException("This is not a Teleport Railgun");
		}
		
		ItemMeta itemMeta = item.getItemMeta();
		List<String> lore = itemMeta.getLore();
		lore.set(2, CHARGES_PREFIX + charges);
		itemMeta.setLore(lore);
		item.setItemMeta(itemMeta);
	}
	
	private void teleport(Player p) {
		World currentWorld = p.getWorld();
		Location currentLocation = p.getEyeLocation();
		Entity armorStand1 = currentWorld.spawnEntity(currentLocation, EntityType.ARMOR_STAND);
		ArmorStand armorStand = (ArmorStand) armorStand1;
		armorStand.setInvisible(true);
		float yaw = p.getLocation().getYaw();
		float pitch = p.getLocation().getPitch();
		armorStand.setGravity(false);
		armorStand.setInvulnerable(true);
		armorStand.setRotation(yaw, pitch);

		int breakCounter = 0;
		
		while (true) {
			breakCounter++;
			armorStand.teleport(armorStand.getLocation().add(armorStand.getLocation().getDirection().multiply(TELEPORT_STEP)));
			
			if (!armorStand.getLocation().getBlock().isPassable() || breakCounter >= (TELEPORT_BLOCKS / TELEPORT_STEP)) {
				armorStand.teleport(armorStand.getLocation().add(armorStand.getLocation().getDirection().multiply(-1)));
				p.teleport(armorStand.getLocation());
				p.setFallDistance(0);
				p.getLocation().getDirection().zero();
				p.playEffect(EntityEffect.TELEPORT_ENDER);
				SoundUtils.playToAll(p, Sound.ENTITY_ENDERMAN_TELEPORT, 10, 1);
				armorStand.remove();
				break;
			}
		}
	}

	public static class Listeners implements Listener {
		
		@EventHandler
		public void onInventoryClick(InventoryClickEvent event) {
			TeleportRailgun gun = new TeleportRailgun();
			HumanEntity h = event.getWhoClicked();
			
			if (!(h instanceof Player) || event.getClick() == ClickType.CREATIVE) {
				return;
			}
	
			Player p = (Player) h;
			
			ItemStack current = event.getCurrentItem();
			ItemStack cursor = event.getCursor();
			
			if (current == null || cursor == null) {
				return;
			}
			
			if (!gun.isItem(current) || !(new TeleportCapsule().isItem(cursor))) {
				return;
			}
			
			event.setCancelled(true);
			
			gun.setCharges(current, gun.getCharges(current) + cursor.getAmount());
			
			p.setItemOnCursor(null);
		}
	
		@EventHandler
		public void onInteract(PlayerInteractEvent event) {
			TeleportRailgun gun = new TeleportRailgun();
			ItemStack item = event.getItem();
			Player p = event.getPlayer();
			
			if (!gun.isItem(item) || !ActionUtils.isRightclick(event.getAction()) || event.getHand() != EquipmentSlot.HAND) {
				return;
			}
			
			event.setCancelled(true);
			
			int charges = gun.getCharges(item);
			
			if (charges <= 0 || gun.hasCooldown(p, COOLDOWN_KEY)) {
				SoundUtils.play(p, Sound.ENTITY_ENDERMITE_AMBIENT, 10, 1);
				return;
			}
			
			gun.teleport(p);
			
			gun.setCharges(item, charges - 1);
			gun.applyCooldown(p, COOLDOWN_KEY, 2);
		}
	}
}
