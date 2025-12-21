package de.cric_hammel.eternity.infinity.mobs.kree;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.PiglinBrute;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.cric_hammel.eternity.infinity.items.kree.KreeArmor;
import de.cric_hammel.eternity.infinity.mobs.CustomMob;

public class KreeGeneral extends Kree {

	private static KreeGeneral instance;
	
	private static KreeArmor armor = KreeArmor.getInstance();

	public static KreeGeneral getInstance() {
		if (null == instance) {
			synchronized (KreeGeneral.class) {
				if (null == instance) {
					instance = new KreeGeneral();
				}
			}
		}
		
		return instance;
	}
	
	private KreeGeneral() {
		super(EntityType.PIGLIN_BRUTE, "General");
	}

	@Override
	public Mob spawn(Location loc) {
		PiglinBrute mob = (PiglinBrute) super.spawn(loc);
		ItemStack[] armorStack = armor.getTier(2);
		CustomMob.setArmor(mob, armorStack, 0.005f);
		ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
		sword.addEnchantment(Enchantment.SHARPNESS, 5);
		sword.addEnchantment(Enchantment.KNOCKBACK, 1);
		CustomMob.setMainHand(mob, sword, 0);
		mob.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(120);
		mob.setHealth(60);
		mob.setImmuneToZombification(true);
		return mob;
	}

	public static class Listeners implements Listener {

		@EventHandler
		public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
			KreeGeneral kree = KreeGeneral.getInstance();
			Entity damager = event.getDamager();

			if (!kree.isMob(damager)) {
				return;
			}

			for (Entity e : damager.getNearbyEntities(5, 5, 5)) {

				if (kree.isKree(e)) {
					((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 5 * 20, 1));
				}
			}
		}
	}
}
