package de.cric_hammel.eternity.infinity.items.gauntlet;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import de.cric_hammel.eternity.Main;
import de.cric_hammel.eternity.infinity.items.CustomItem;
import de.cric_hammel.eternity.infinity.items.stones.StoneType;
import de.cric_hammel.eternity.infinity.util.ActionUtils;
import de.cric_hammel.eternity.infinity.util.AttributeUtils;
import de.cric_hammel.eternity.infinity.util.SoundUtils;

public class Gauntlet extends CustomItem {
	
	private static Gauntlet instance;

	private static final String METADATA_KEY_FREEZE = "eternity_gauntlet_freeze";
	private static final String METADATA_KEY_COOLDOWN_LEFT = "eternity_gauntlet_cooldown_left";
	private static final String METADATA_KEY_COOLDOWN_RIGHT = "eternity_gauntlet_cooldown_right";

	private static final int cooldownLeftclick = 120;
	private static final int cooldownRightclick = 60;

	public static Gauntlet getInstance() {
		if (null == instance) {
			synchronized (Gauntlet.class) {
				if (null == instance) {
					instance = new Gauntlet();
				}
			}
		}
		
		return instance;
	}
	
	private Gauntlet() {
		super(Material.TORCHFLOWER, ChatColor.GOLD + "Infinity Gauntlet",
				"Designed to channel the power of all six Infinity Stones");
	}

	@Override
	public ItemStack getItem() {
		ItemStack gauntlet = super.getItem();
		AttributeUtils.add(gauntlet, Attribute.GENERIC_MAX_HEALTH, 2 * 20, Operation.ADD_NUMBER, EquipmentSlot.OFF_HAND);
		AttributeUtils.add(gauntlet, Attribute.GENERIC_ATTACK_DAMAGE, 10, Operation.ADD_NUMBER, EquipmentSlot.OFF_HAND);
		AttributeUtils.add(gauntlet, Attribute.GENERIC_ARMOR_TOUGHNESS, 10, Operation.ADD_NUMBER, EquipmentSlot.OFF_HAND);
		return gauntlet;
	}
	
	public static class Listeners implements Listener {
		
		@EventHandler
		public void useGauntlet(PlayerInteractEvent event) {
			Gauntlet g = Gauntlet.getInstance();
			Player p = event.getPlayer();
			
			if (!hasGauntletInOffHand(p)) {
				return;
			}
			
			Action a = event.getAction();
			
			if (ActionUtils.isRightclick(a) && !g.hasCooldown(p, METADATA_KEY_COOLDOWN_RIGHT)) {
				snap(p);
			} else if (ActionUtils.isLeftclick(a) && !g.hasCooldown(p, METADATA_KEY_COOLDOWN_LEFT)) {
				balance(p);
			}
			
			event.setCancelled(true);
		}
		
		private void snap(Player p) {
			boolean remove = true;
			
			for (Entity e : p.getNearbyEntities(200, 200, 200)) {
				
				if (!(e instanceof Monster)) {
					continue;
				}
				
				if (remove) {
					e.remove();
					remove = false;
					p.getWorld().spawnParticle(Particle.SMOKE_NORMAL, e.getLocation(), 50, 0.25, 0.5, 0.25, 0);
				} else {
					remove = true;
				}
			}
			
			SoundUtils.playToAll(p, Sound.PARTICLE_SOUL_ESCAPE, 10f, 0.5f);
			Gauntlet.getInstance().applyCooldown(p, METADATA_KEY_COOLDOWN_RIGHT, cooldownRightclick);
		}
		
		private void balance(Player p) {
			World w = p.getWorld();
			w.setTime(1000);
			w.setThundering(false);
			w.setStorm(false);
			p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
			p.setFoodLevel(20);
			p.setSaturation(20);
			
			for (PotionEffect effect : p.getActivePotionEffects()) {
				
				if (NegativeEffects.contains(effect.getType().getKey().getKey())) {
					p.removePotionEffect(effect.getType());
				}
				
			}
			
			freezeNearbyEntities(p);
			
			ItemStack[] contents = p.getInventory().getContents();
			
			for (ItemStack i : contents) {
				
				if (i == null || !i.hasItemMeta()) {
					continue;
				}
				
				ItemMeta iMeta = i.getItemMeta();
				
				if (!(iMeta instanceof Damageable)) {
					continue;
				}
				
				Damageable iMetaD = (Damageable) iMeta;
				iMetaD.setDamage(0);
				i.setItemMeta(iMetaD);
			}
			
			p.getInventory().setContents(contents);
			SoundUtils.playToAll(p, Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 1.5f);
			p.getWorld().spawnParticle(Particle.HEART, p.getLocation(), 50, 0.75, 0.5, 0.75);
			Gauntlet.getInstance().applyCooldown(p, METADATA_KEY_COOLDOWN_LEFT, cooldownLeftclick);
		}
		
		private void freezeNearbyEntities(Player p) {
			
			for (final Entity e : p.getNearbyEntities(20, 20, 20)) {
				
				if (e instanceof Player) {
					e.setMetadata(METADATA_KEY_FREEZE, new FixedMetadataValue(Main.getPlugin(), 1));
					
					new BukkitRunnable() {
						
						@Override
						public void run() {
							e.removeMetadata(METADATA_KEY_FREEZE, Main.getPlugin());
						}
						
					}.runTaskLaterAsynchronously(Main.getPlugin(), 15 * 20);
				} else if (e instanceof LivingEntity) {
					final LivingEntity l = (LivingEntity) e;
					l.setAI(false);
					
					new BukkitRunnable() {
						
						@Override
						public void run() {
							l.setAI(true);
						}
						
					}.runTaskLaterAsynchronously(Main.getPlugin(), 15 * 20);
				}
			}
		}
		
		private boolean hasGauntletInOffHand(Player p) {
			ItemStack gauntlet = p.getInventory().getItemInOffHand();
			if (gauntlet != null && gauntlet.hasItemMeta() && gauntlet.getItemMeta().hasLore()
					&& gauntlet.getItemMeta().getLore().get(0).equals(Gauntlet.getInstance().getLore()) && !StoneType.hasAnyInHand(p)) {
				return true;
			}
			return false;
		}
		
		@EventHandler
		public void onPlayerMove(PlayerMoveEvent event) {
			Player p = event.getPlayer();
			
			if (p.hasMetadata(METADATA_KEY_FREEZE)) {
				event.setCancelled(true);
			}
		}
		
		@EventHandler
		public void moveGauntlet(InventoryClickEvent event) {
			Gauntlet g = Gauntlet.getInstance();
			Inventory inv = event.getClickedInventory();
			
			if (inv == event.getWhoClicked().getInventory()) {
				if (event.getClick().isShiftClick()) {
					ItemStack current = event.getCurrentItem();
					
					if (g.isItem(current)) {
						event.setCancelled(true);
					}
				}
			} else {
				ItemStack cursor = event.getCursor();
				
				if (g.isItem(cursor)) {
					event.setCancelled(true);
				}
			}
		}
		
		@EventHandler
		public void dragGauntlet(InventoryDragEvent event) {
			ItemStack drag = event.getOldCursor();
			Inventory inv = event.getInventory();
			
			if (!Gauntlet.getInstance().isItem(drag)) {
				return;
			}
			
			int invSize = inv.getSize();
			
			for (int i : event.getRawSlots()) {
				
				if (i < invSize) {
					event.setCancelled(true);
					break;
				}
			}
		}
		
		@EventHandler
		public void dropGauntlet(PlayerDropItemEvent event) {
			
			if (Gauntlet.getInstance().isItem(event.getItemDrop().getItemStack())) {
				event.setCancelled(true);
			}
		}
		
		@EventHandler
		public void pickupGauntlet(InventoryPickupItemEvent event) {
			
			if (event.getInventory().getType() == InventoryType.HOPPER && Gauntlet.getInstance().isItem(event.getItem().getItemStack())) {
				event.setCancelled(true);
			}
		}
		
		private enum NegativeEffects {
			
			CONFUSION, HARM, HUNGER, POISON, SLOW_DIGGING, SLOW, WEAKNESS, WITHER;
			
			public static boolean contains(String name) {
				try {
					valueOf(name);
					return true;
				} catch (Exception e) {
					return false;
				}
			}
		}
	}
}
