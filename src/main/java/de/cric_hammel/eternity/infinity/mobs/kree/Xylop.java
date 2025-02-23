package de.cric_hammel.eternity.infinity.mobs.kree;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Hoglin;
import org.bukkit.entity.Mob;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import de.cric_hammel.eternity.infinity.loot.XylopLoot;
import de.cric_hammel.eternity.infinity.mobs.DungeonMob;

public class Xylop extends DungeonMob {
	
	private static Xylop instance;
	
	public static Xylop getInstance() {
		if (null == instance) {
			synchronized (KreeSoldier.class) {
				if (null == instance) {
					instance = new Xylop();
				}
			}
		}
		
		return instance;
	}

	private Xylop() {
		super(EntityType.HOGLIN, ChatColor.GOLD + "Xylop");
	}
	
	@Override
	public Mob spawn(Location loc) {
		Hoglin mob = (Hoglin) super.spawn(loc);
		mob.setImmuneToZombification(true);
		return mob;
	}
	
	public static class Listeners implements Listener {
		
		@EventHandler
		public void onEntityDeath(EntityDeathEvent event) {
			
			if (!Xylop.getInstance().isMob(event.getEntity())) {
				return;
			}
			
			new XylopLoot().generateDrops(event.getDrops(), new Random());
		}
	}
}
