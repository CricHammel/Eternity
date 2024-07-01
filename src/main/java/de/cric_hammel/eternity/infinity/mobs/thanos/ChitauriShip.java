package de.cric_hammel.eternity.infinity.mobs.thanos;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import de.cric_hammel.eternity.Main;
import de.cric_hammel.eternity.infinity.util.SoundUtils;

public class ChitauriShip extends ThanosFollower {

	private static final String META_KEY_PASSENGERS = "eternity_passengers";
	private static final String META_KEY_TASKS = "eternity_tasks";

	public ChitauriShip() {
		super(EntityType.GHAST, ChatColor.GOLD + "Chitauri-Ship", null);
	}

	public Mob spawnWithPassengers(Location loc, int amount) {
		Mob m = super.spawn(loc);
		m.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(50);
		m.setHealth(50);
		m.setAI(false);
		m.setInvulnerable(true);
		Location mLoc = m.getLocation();
		Set<BukkitTask> scheduledTasks = new HashSet<>();
		scheduledTasks.add(new BukkitRunnable() {

			@Override
			public void run() {
				if (m.isDead()) {
					cancel();
				}

				mLoc.add(mLoc.getDirection().setY(0));
				mLoc.setYaw(mLoc.getYaw() - 2);
				m.teleport(mLoc);
			}
		}.runTaskTimer(Main.getPlugin(), 0, 1));

		List<Mob> passengers = new ArrayList<>();
		m.setMetadata(META_KEY_PASSENGERS, new FixedMetadataValue(Main.getPlugin(), passengers));

		scheduledTasks.add(new BukkitRunnable() {
			int count = 0;
			Chitauri c = new Chitauri();

			@Override
			public void run() {
				if (count >= amount) {
					cancel();
					m.setInvulnerable(false);
					return;
				}

				Location vLoc = mLoc.clone();
				vLoc.setYaw(mLoc.getYaw() - 90);
				Vector v = vLoc.getDirection();
				v.rotateAroundY(1d);
				Mob m = c.spawn(mLoc);
				m.setVelocity(v);
				passengers.add(m);
				count++;
			}
		}.runTaskTimer(Main.getPlugin(), 0, 10));
		m.setMetadata(META_KEY_TASKS, new FixedMetadataValue(Main.getPlugin(), scheduledTasks));
		return m;
	}

	@SuppressWarnings("unchecked")
	public void remove(Mob m) {
		if (!isMob(m)) {
			return;
		}

		List<Mob> passengers = (List<Mob>) m.getMetadata(META_KEY_PASSENGERS).get(0).value();
		passengers.forEach(chi -> chi.remove());
		Set<BukkitTask> scheduledTasks = (Set<BukkitTask>) m.getMetadata(META_KEY_TASKS).get(0).value();
		scheduledTasks.forEach(task -> task.cancel());
		m.remove();
	}

	public static class Listeners implements Listener {
		
		@EventHandler
		public void onEntityDeath(EntityDeathEvent event) {
			LivingEntity e = event.getEntity();

			if (!(new ChitauriShip().isMob(e)) || !e.hasMetadata(META_KEY_PASSENGERS)) {
				return;
			}

			Chitauri c = new Chitauri();
			@SuppressWarnings("unchecked")
			List<Mob> passengers = (List<Mob>) e.getMetadata(META_KEY_PASSENGERS).get(0).value();
			passengers.forEach((m) -> c.disable(m));
			SoundUtils.playToAll(e.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1f, 1f);
		}
	}
}
