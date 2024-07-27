package de.cric_hammel.eternity.infinity.mobs.kree;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Piglin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import de.cric_hammel.eternity.Main;
import de.cric_hammel.eternity.infinity.items.kree.KreeArmor;
import de.cric_hammel.eternity.infinity.mobs.CustomMob;
import de.cric_hammel.eternity.infinity.worlds.dungeons.DungeonFactory;

public class KreeSoldier extends Kree {

	private static KreeSoldier instance;
	
	private static final String METADATA_KEY_TARGET = "eternity_kree_target";
	private static KreeArmor armor = KreeArmor.getInstance();

	public static KreeSoldier getInstance() {
		if (null == instance) {
			synchronized (KreeSoldier.class) {
				if (null == instance) {
					instance = new KreeSoldier();
				}
			}
		}
		
		return instance;
	}
	
	private KreeSoldier() {
		super(EntityType.PIGLIN, "Soldier", null);
	}

	@Override
	public Mob spawn(Location loc) {
		Piglin mob = (Piglin) super.spawn(loc);
		ItemStack[] armorStack = armor.getTier(1);
		CustomMob.setArmor(mob, armorStack, 0.0125f);
		ItemStack sword = new ItemStack(Material.IRON_SWORD);
		sword.addEnchantment(Enchantment.SHARPNESS, 5);
		CustomMob.setMainHand(mob, sword, 0);
		mob.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(60);
		mob.setHealth(30);
		mob.setImmuneToZombification(true);
		return mob;
	}

	public static class Listeners implements Listener {

		@EventHandler
		public void onEntityPickupItem(EntityPickupItemEvent event) {

			if ((new KreeSoldier()).isMob(event.getEntity())) {
				event.setCancelled(true);
			}
		}

		@EventHandler
		public void onEntityTarget(EntityTargetEvent event) {
			Entity e = event.getEntity();
			Entity target = event.getTarget();

			if (!(new KreeSoldier()).isMob(e) || !(target instanceof Player)) {
				return;
			}

			Player p = (Player) event.getTarget();

			if (!p.hasMetadata(METADATA_KEY_TARGET)) {
				event.setCancelled(true);
			}
		}

		@EventHandler
		public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
			KreeSoldier kree = new KreeSoldier();
			Entity damager = event.getDamager();
			Entity entity = event.getEntity();

			if (damager instanceof Player && kree.isKree(entity)) {
				aggro((Player) damager);
			}

			if (kree.isKree(damager) && entity instanceof Player) {
				aggro((Player) entity);
			}
		}

		@EventHandler
		public void onInventoryOpen(InventoryOpenEvent event) {

			if (!(event.getPlayer() instanceof Player) || !(event.getInventory().getHolder() instanceof Chest)) {
				return;
			}

			Player p = (Player) event.getPlayer();
			aggro(p);
		}

		private void aggro(final Player p) {

			if (DungeonFactory.getCurrentDungeon(p) == null) {
				return;
			}
			
			if (!p.hasMetadata(METADATA_KEY_TARGET)) {
				p.setMetadata(METADATA_KEY_TARGET, new FixedMetadataValue(Main.getPlugin(), 1));

				new BukkitRunnable() {

					@Override
					public void run() {
						if (p.hasMetadata(METADATA_KEY_TARGET)) {
							p.removeMetadata(METADATA_KEY_TARGET, Main.getPlugin());
						}
					}

				}.runTaskLater(Main.getPlugin(), 120 * 20);
			}
		}
	}
}
