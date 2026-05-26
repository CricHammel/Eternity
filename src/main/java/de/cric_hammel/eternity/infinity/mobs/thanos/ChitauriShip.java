package de.cric_hammel.eternity.infinity.mobs.thanos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import de.cric_hammel.eternity.Main;
import de.cric_hammel.eternity.infinity.util.SoundUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class ChitauriShip extends ThanosFollower {

	private static ChitauriShip instance;
	
	private static final Map<Mob, List<Mob>> PASSENGERS = new HashMap<>();
	private static final Map<Mob, Set<BukkitTask>> TASKS = new HashMap<>();

	public static ChitauriShip getInstance() {
		if (null == instance) {
			synchronized (ChitauriShip.class) {
				if (null == instance) {
					instance = new ChitauriShip();
				}
			}
		}
		
		return instance;
	}
	
	private ChitauriShip() {
		super(EntityType.GHAST, Component.text("Chitauri-Ship", NamedTextColor.GOLD));
	}

	public Mob spawnWithPassengers(Location loc, int amount) {
		Mob m = super.spawn(loc);
		m.getAttribute(Attribute.MAX_HEALTH).setBaseValue(50);
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
		PASSENGERS.put(m, passengers);

		scheduledTasks.add(new BukkitRunnable() {
			int count = 0;
			Chitauri c = Chitauri.getInstance();

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
		TASKS.put(m, scheduledTasks);
		return m;
	}

	public void remove(Mob m) {
		if (!isMob(m)) {
			return;
		}

		List<Mob> passengers = PASSENGERS.get(m);
		passengers.forEach(chi -> chi.remove());
		PASSENGERS.remove(m);
		Set<BukkitTask> scheduledTasks = TASKS.get(m);
		scheduledTasks.forEach(task -> task.cancel());
		TASKS.remove(m);
		m.remove();
	}

	public static class Listeners implements Listener {
		
		@EventHandler
		public void onEntityDeath(EntityDeathEvent event) {
			LivingEntity e = event.getEntity();

			if (!ChitauriShip.getInstance().isMob(e) || !PASSENGERS.containsKey(e)) {
				return;
			}

			Chitauri c = Chitauri.getInstance();
			List<Mob> passengers = PASSENGERS.get(e);
			passengers.forEach((m) -> c.disable(m));
			PASSENGERS.remove(e);
			Set<BukkitTask> scheduledTasks = TASKS.get(e);
			scheduledTasks.forEach(task -> task.cancel());
			TASKS.remove(e);
			SoundUtils.playToAll(e.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1f, 1f);
		}
	}
}
