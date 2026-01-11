package de.cric_hammel.eternity.infinity.mobs.thanos;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Outrider extends ThanosFollower {

	private static Outrider instance;

	public static Outrider getInstance() {
		if (null == instance) {
			synchronized (Outrider.class) {
				if (null == instance) {
					instance = new Outrider();
				}
			}
		}
		
		return instance;
	}
	
	private Outrider() {
		super(EntityType.SPIDER, ChatColor.GOLD + "Outrider");
	}
	
	@Override
	public Mob spawn(Location loc) {
		Mob m = super.spawn(loc);
		m.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.8);
		m.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(120);
		m.setHealth(120);
		return m;
	}
	
	public static class Listeners implements Listener {
		
		@EventHandler
		public void onEntityDamage(EntityDamageByEntityEvent event) {
			
			if (!Outrider.getInstance().isMob(event.getDamager()) || !(event.getEntity() instanceof LivingEntity)) {
				return;
			}
			
			LivingEntity e = (LivingEntity) event.getEntity();
			
			e.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 60, 0));
		}
	}
}
