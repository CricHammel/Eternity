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

public class KreeGeneral extends Kree implements Listener {
	
	public KreeGeneral() {
		super(EntityType.PIGLIN_BRUTE, "General", null);
	}
	
	@Override
	public Mob spawn(Location loc) {
		PiglinBrute mob = (PiglinBrute) super.spawn(loc);
		ItemStack[] armor = new KreeArmor().getTier(2);
		CustomMob.setArmor(mob, armor, 0.005f);
		ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
		sword.addEnchantment(Enchantment.DAMAGE_ALL, 5);
		sword.addEnchantment(Enchantment.KNOCKBACK, 1);
		CustomMob.setMainHand(mob, sword, 0);
		mob.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(120);
		mob.setHealth(60);
		mob.setImmuneToZombification(true);
		return mob;
	}
	
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		Entity damager = event.getDamager();
		
		if (!super.isMob(damager)) {
			return;
		}
		
		for (Entity e : damager.getNearbyEntities(5, 5, 5)) {
			
			if (super.isKree(e)) {
				((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 5 * 20, 1));
			}
		}
	}
}
