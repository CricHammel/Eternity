package de.cric_hammel.eternity.infinity.worlds;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import de.cric_hammel.eternity.Main;
import de.cric_hammel.eternity.infinity.mobs.thanos.ChitauriShip;
import de.cric_hammel.eternity.infinity.mobs.thanos.Fangs;
import de.cric_hammel.eternity.infinity.mobs.thanos.Thanos;
import de.cric_hammel.eternity.infinity.parsers.StructureParser;
import de.cric_hammel.eternity.infinity.util.SoundUtils;

public class ThanosFight implements Listener {

	private static final double FIRST_DAMAGE_CAP = 2d/3d;
	private static final int AMOUNT_SHIPS = 9;
	private static final int PASSENGERS_PER_SHIP = 18;
	private static final String THANOS_PREFIX = ChatColor.DARK_PURPLE + "" +  ChatColor.BOLD + "Thanos";
	private static boolean running = false;
	private static int phase = 0;
	private static int mobsToKill = 0;
	private static Thanos thanos = new Thanos();
	private static Mob thanosMob;
	private static Set<BukkitTask> scheduledTasks = new HashSet<BukkitTask>();
	private static Set<StructureParser> parsers = new HashSet<StructureParser>();
	private static BossBar bossbar;

	private World lobby = Main.getLobby().getWorld();
	private final Location origin = new Location(lobby, 255, 105, 223);

	public void start() {
		if (running) {
			return;
		}

		running = true;
		phase = 0;
		/*
		 * Statue becomes alive
		 * Sound, animation, dialogue
		 * Spawns first army (Chitauri)
		 */

		thanosMob = thanos.spawn(origin);
		updateBossbar();

		BukkitRunnable task2 = new BukkitRunnable() {
			Location spawn = origin.clone().add(-29, 0, 0);
			ChitauriShip ship = new ChitauriShip();
			int count = 0;

			@Override
			public void run() {
				if (count >= AMOUNT_SHIPS) {
					updateBossbar();
					cancel();
					return;
				}

				ship.spawnWithPassengers(spawn, PASSENGERS_PER_SHIP);
				SoundUtils.playToAll(spawn, Sound.ENTITY_BLAZE_AMBIENT, 1f, 1f);
				count++;
			}
		};

		BukkitRunnable task1 = new BukkitRunnable() {
			int i = 0;
			List<String> dialogue = createFirstDialogue();

			@Override
			public void run() {
				if (i >= dialogue.size()) {
					cancel();
					scheduledTasks.add(task2.runTaskTimer(Main.getPlugin(), 0, 20));
					return;
				}

				lobby.getPlayers().forEach(p -> p.sendMessage(dialogue.get(i)));
				SoundUtils.playToAll(origin, Sound.ENTITY_WARDEN_ANGRY, 1f, 1f);
				i++;
			}
		};

		scheduledTasks.add(task1.runTaskTimer(Main.getPlugin(), 0, 3*20));
		mobsToKill = AMOUNT_SHIPS * PASSENGERS_PER_SHIP + AMOUNT_SHIPS;
	}

	private void triggerFirstHitPhase() {
		/*
		 * All Chitauris are killed
		 * Thanos can be hit up to 2/3 of his life
		 */
		phase = 1;
		thanosMob.setInvulnerable(false);
		SoundUtils.playToAll(origin, Sound.BLOCK_LAVA_EXTINGUISH, 10f, 0.5f);
		updateBossbar();
	}

	public void triggerSecondWave() {
		/*
		 * Dialogue
		 * Spawns second army (Outriders)
		 */
		
		BukkitRunnable task2 = new BukkitRunnable() {

			Location fangsLoc = origin.clone().add(52, -27, -29);
			Fangs fangs = new Fangs(fangsLoc, 70, 150);

			@Override
			public void run() {
				fangs.spawnRandom();
			}
		};
		
		BukkitRunnable task1 = new BukkitRunnable() {
			
			List<Location> spawnLocs = new ArrayList<Location>(Arrays.asList(origin.clone().add(59, -27, -44), origin.clone().add(50, -27, -6), origin.clone().add(51, -27, 41)));
			int i = 0;
			
			@Override
			public void run() {
				
				if (i >= spawnLocs.size()) {
					cancel();
					scheduledTasks.add(task2.runTaskTimer(Main.getPlugin(), 0, 2 * 20));
					return;
				}
				
				StructureParser parser = new StructureParser("spaceShip.txt", spawnLocs.get(i));
				parser.parse();
				parsers.add(parser);
				i++;
			}
		};
		
		scheduledTasks.add(task1.runTaskTimer(Main.getPlugin(), 0, 2 * 20));
//		WorldParser parser = new WorldParser("spaceShip.txt", lobby);
		
//		Set<FallingBlock> blocks = Stream.concat(Stream.concat(parser.parseAsFallingBlocks(origin.add(55, 150, -34)).stream(), parser.parseAsFallingBlocks(origin.add(55, 150, 12)).stream()), parser.parseAsFallingBlocks(origin.add(55, 150, 67)).stream()).collect(Collectors.toSet());
//		Set<FallingBlock> blocks = parser.parseAsFallingBlocks(origin.add(55, 100, -34));
		
//		BukkitRunnable task1 = new BukkitRunnable() {
//			
//			int height = 150 * 20;
//			
//			@Override
//			public void run() {
//				if (height <= 0) {
//					
//					for (FallingBlock block : blocks) {
//						block.getLocation().getBlock().setType(block.getBlockData().getMaterial());
//						block.remove();
//					}
//					
//					cancel();
//					scheduledTasks.add(task2.runTaskTimer(Main.getPlugin(), 0, 2 * 20));
//					return;
//				}
//				
//				for (FallingBlock block : blocks) {
//					block.teleport(block.getLocation().add(0, -(1 / 20), 0));
//					block.setTicksLived(1);
//				}
//				
//				height--;
//			}
//		};
//		
//		scheduledTasks.add(task1.runTaskTimer(Main.getPlugin(), 0, 1));
	}

	private void triggerSecondHitPhase() {
		/*
		 * All Outriders are killed
		 * Thanos can be hit up to 1/3 of his life
		 */
	}

	private void triggerRage() {
		/*
		 * Dialogue
		 * Thanos enters rage mode and fights the player
		 */
	}

	private void triggerCompletion() {
		/*
		 * Thanos is dead
		 * Loot drops for everyone
		 * Gauntlet drop only for original player
		 */
	}

	public void stop(boolean failed) {
		/*
		 * If failed
		 * 	Dialogue
		 *
		 * Kill all related entities
		 * Cancel all tasks
		 */

		running = false;
		scheduledTasks.forEach(task -> task.cancel());
		scheduledTasks.clear();
		parsers.forEach(parser -> parser.unparse());
		parsers.clear();
		thanos.remove(thanosMob);
		ChitauriShip ship = new ChitauriShip();
		lobby.getEntities().forEach(e -> {
			if (ship.isMob(e)) {
				ship.remove((Mob) e);
			}

		});
		
		if (bossbar != null) {
			bossbar.removeAll();
		}
	}

	private List<String> createFirstDialogue() {
		List<String> d = new ArrayList<>();
		d.add(formatDialogue("Test"));
		return d;
	}

	private String formatDialogue(String d) {
		return THANOS_PREFIX + ": " + ChatColor.LIGHT_PURPLE + d;
	}
	
	private void updateBossbar() {
		if (bossbar == null) {
			bossbar = Bukkit.createBossBar(THANOS_PREFIX, BarColor.PURPLE, BarStyle.SEGMENTED_6, BarFlag.DARKEN_SKY);
			return;
		}
		
		if (phase == 0) {
			bossbar.setTitle(THANOS_PREFIX + " (Remaining enemies: " + mobsToKill + ")");
		} else if (phase == 1) {
			bossbar.setTitle(THANOS_PREFIX);
		}
		
		bossbar.setProgress(thanosMob.getHealth() / thanosMob.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
		lobby.getPlayers().forEach((p) -> bossbar.addPlayer(p));
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		LivingEntity e = event.getEntity();

		if (!e.getWorld().equals(lobby) || !running || !(e instanceof Mob)) {
			return;
		}

		if (phase == 0) {
			if (mobsToKill <= 1) {
				triggerFirstHitPhase();
				return;
			}

			mobsToKill--;
			updateBossbar();
		}
	}

	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		Entity e = event.getEntity();

		if (!e.getWorld().equals(lobby) || !running || e != thanosMob) {
			return;
		}

		double predictedHealth = thanosMob.getHealth() - event.getFinalDamage();
		double maxHealth = thanosMob.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();

		if (phase == 1) {
			if (predictedHealth/maxHealth <= FIRST_DAMAGE_CAP) {
				event.setCancelled(true);
				double healthCap = FIRST_DAMAGE_CAP * maxHealth;
				thanosMob.setHealth(healthCap);
				triggerSecondWave();
			}
			
			updateBossbar();
		}
	}
}
