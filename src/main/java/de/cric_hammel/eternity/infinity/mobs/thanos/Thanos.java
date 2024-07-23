package de.cric_hammel.eternity.infinity.mobs.thanos;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;

public class Thanos extends ThanosFollower {

	private static Thanos instance;

	public static Thanos getInstance() {
		if (null == instance) {
			synchronized (Thanos.class) {
				if (null == instance) {
					instance = new Thanos();
				}
			}
		}
		
		return instance;
	}
	
	private Thanos() {
		super(EntityType.WARDEN, ChatColor.DARK_PURPLE + "Thanos", null);
	}

	@Override
	public Mob spawn(Location loc) {
		Mob m = super.spawn(loc);
		m.setInvulnerable(true);
		m.setAI(false);
		m.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(1000);
		m.setHealth(1000);
		return m;
	}

	public void remove(Mob m) {
		if (!isMob(m)) {
			return;
		}
		
		m.remove();
	}
}
