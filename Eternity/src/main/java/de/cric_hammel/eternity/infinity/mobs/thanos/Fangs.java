package de.cric_hammel.eternity.infinity.mobs.thanos;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;

import de.cric_hammel.eternity.Main;

public class Fangs {

	private static final BlockFace LEFT = BlockFace.SOUTH;
	private static final BlockFace FRONT = BlockFace.WEST;
	private static final BlockFace RIGHT = BlockFace.NORTH;

	private Random random = new Random();
	private World w;
	private int rightPos;
	private int spawnLength;
	private Location startLoc;
	private int maxLength;

	public Fangs(Location rightLoc, int spawnLength, int maxLength) {
		w = rightLoc.getWorld();
		rightPos = rightLoc.getBlockZ();
		this.spawnLength = spawnLength;
		startLoc = rightLoc;
		this.maxLength = maxLength;
	}

	public void spawnRandom() {
		startLoc.setZ(random.nextInt(spawnLength + 1) + rightPos);
		spawnFang(startLoc);
		new BukkitRunnable() {
			int length = 0;
			Location currentLoc = startLoc.clone();
			double leftProbab = 0.25;
			double frontProbab = 0.5;
			double rightProbab = 0.25;

			@Override
			public void run() {
				if (length >= maxLength || currentLoc.getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR) {
					cancel();
					return;
				}

				BlockFace next = getRandomFace(new double[]{leftProbab, frontProbab, rightProbab});
				currentLoc = currentLoc.getBlock().getRelative(next).getLocation();

				if (next == LEFT || next == RIGHT) {
					currentLoc = currentLoc.getBlock().getRelative(FRONT).getLocation();
				}

				spawnFang(currentLoc);

				if (next == LEFT) {
					leftProbab = 0.3;
					frontProbab = 0.6;
					rightProbab = 0.1;
				} else if (next == RIGHT) {
					leftProbab = 0.1;
					frontProbab = 0.6;
					rightProbab = 0.3;
				}

				length++;
			}
		}.runTaskTimer(Main.getPlugin(), 2, 2);
	}

	private void spawnFang(Location loc) {
		Location center = loc.clone();
		center.add(0.5, 0, -0.5);
		w.spawnEntity(center, EntityType.EVOKER_FANGS);
		center.add(0, 0, 1);
		w.spawnEntity(center, EntityType.EVOKER_FANGS);
		center.add(0, 0, 1);
		w.spawnEntity(center, EntityType.EVOKER_FANGS);
	}

	private BlockFace getRandomFace(double[] probabs) {
		double probab = random.nextDouble();
		double cumulative = 0.0;
		int option = 1;

		for (int i = 0; i <= 3; i++) {
			double current = probabs[i];
			cumulative += current;

			if (probab <= cumulative) {
				option = i;
				break;
			}
		}

		if (option == 0) {
			return LEFT;
		} else if (option == 1) {
			return FRONT;
		} else {
			return RIGHT;
		}
	}
}
