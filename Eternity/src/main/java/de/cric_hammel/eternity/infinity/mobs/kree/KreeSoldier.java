package de.cric_hammel.eternity.infinity.mobs.kree;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Chest;
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

public class KreeSoldier extends Kree implements Listener {

	private static final String METADATA_KEY_TARGET = "eternity_kree_target";
	
	public KreeSoldier() {
		super(EntityType.PIGLIN, "Soldier", null);
	}
	
	@Override
	public Mob spawn(Location loc) {
		Piglin mob = (Piglin) super.spawn(loc);
		ItemStack[] armor = new KreeArmor().getTierOne();
		CustomMob.setArmor(mob, armor, 0.05f);
		CustomMob.setMainHand(mob, new ItemStack(Material.IRON_SWORD), 0);
		mob.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(30);
		mob.setHealth(30);
		mob.setImmuneToZombification(true);
		return mob;
	}
	
	@EventHandler
	public void onEntityPickupItem(EntityPickupItemEvent event) {
		
		if (super.isMob(event.getEntity())) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onEntityTarget(EntityTargetEvent event) {
		Entity e = event.getEntity();
		
		if (!super.isMob(e) || !(event.getTarget() instanceof Player)) {
			return;
		}
		
		Player p = (Player) event.getTarget();
		
		if (!p.hasMetadata(METADATA_KEY_TARGET)) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		Entity damager = event.getDamager();
		Entity entity = event.getEntity();
		
		if (damager instanceof Player && super.isKree(entity)) {
			aggro((Player) damager);
		}
		
		if (super.isKree(damager) && entity instanceof Player) {
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
	
	public void aggro(final Player p) {
		
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
