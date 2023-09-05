package de.cric_hammel.eternity.stones;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import de.cric_hammel.eternity.Main;
import de.cric_hammel.eternity.util.DeadEntityStorage;

public class TimeStone implements Listener{
	
	private static final String METADATA_KEY = "eternity_time";
	private HashMap<UUID, LinkedList<DeadEntityStorage>> killedEntities = new HashMap<UUID, LinkedList<DeadEntityStorage>>();
	private HashMap<UUID, LinkedList<Location>> lastLocations = new HashMap<UUID, LinkedList<Location>>();
	
	@EventHandler
	public void useTimeStone(PlayerInteractEvent event) {
		final Player p = event.getPlayer();
		if (Main.hasStoneInHand(p, 5)) {
			Action a = event.getAction();
			if (a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK) {
				LinkedList<Location> locations = lastLocations.get(p.getUniqueId());
				if (locations != null && !locations.isEmpty()) {
					int removed = 0;
					while (locations.size() > 1 && removed < 29) {
						locations.removeLast();
						removed++;
					}
					p.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, p.getLocation(), 50, 0.25, 0.5, 0.25);
					p.playSound(p.getLocation(), Sound.ENTITY_PUFFER_FISH_BLOW_OUT, 1, 0);
					p.teleport(locations.getLast());
					p.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, p.getLocation(), 50, 0.25, 0.5, 0.25);
					p.playSound(p.getLocation(), Sound.ENTITY_PUFFER_FISH_BLOW_UP, 1, 0);
				}
			} else if ((a == Action.LEFT_CLICK_AIR || a == Action.LEFT_CLICK_BLOCK)) {
				LinkedList<DeadEntityStorage> list = killedEntities.get(p.getUniqueId());
				if (list != null && !list.isEmpty()) {
					DeadEntityStorage s = list.get(list.size() - 1);
					s.resurrect();
					list.remove(s);
					p.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, s.getLocation(), 50, 0.25, 0.5, 0.25);
					p.playSound(s.getLocation(), Sound.ENTITY_WARDEN_SONIC_CHARGE, 1, 1.5f);
				}
			}	
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerKillEntity(EntityDeathEvent event) {
		LivingEntity e = event.getEntity();
		Player p = e.getKiller();
		if (p != null && Main.hasStoneInInv(p, 5) && !(e instanceof Player)) {
			if (!killedEntities.containsKey(p.getUniqueId())) {
				killedEntities.put(p.getUniqueId(), new LinkedList<DeadEntityStorage>());
			}
			killedEntities.get(p.getUniqueId()).add(new DeadEntityStorage(e));
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		if (Main.hasStoneInInv(p, 5)) {
			startTimer(p);
		}
	}
	
	@EventHandler
	public void onPlayerPickupTimeStone(EntityPickupItemEvent event) {
		if (event.getEntity() instanceof Player) {
			final Player p = (Player) event.getEntity();
			new BukkitRunnable() {
				
				@Override
				public void run() {
					if (Main.hasStoneInInv(p, 5)) {
						startTimer(p);
					}
				}
			}.runTaskLater(Main.getPlugin(), 1);
		}
	}
	
	private void startTimer(final Player p) {
		if (p.hasMetadata(METADATA_KEY)) {
			return;
		}
		
		p.setMetadata(METADATA_KEY, new FixedMetadataValue(Main.getPlugin(), true));
		new BukkitRunnable() {
			
			@Override
			public void run() {
				if (!lastLocations.containsKey(p.getUniqueId())) {
					lastLocations.put(p.getUniqueId(), new LinkedList<Location>());
				}
				LinkedList<Location> locations = lastLocations.get(p.getUniqueId());
				if (!p.isOnline() || !Main.hasStoneInInv(p, 5)) {
					p.removeMetadata(METADATA_KEY, Main.getPlugin());
					lastLocations.remove(p.getUniqueId());
					cancel();
				}
				locations.add(p.getLocation());
				while (locations.size() > 30) {
					locations.remove();
				}
			}
		}.runTaskTimerAsynchronously(Main.getPlugin(), 20, 20);
	}
}
