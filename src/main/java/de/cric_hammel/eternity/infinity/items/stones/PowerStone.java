package de.cric_hammel.eternity.infinity.items.stones;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import de.cric_hammel.eternity.Main;
import de.cric_hammel.eternity.infinity.util.ActionUtils;
import de.cric_hammel.eternity.infinity.util.SoundUtils;

public class PowerStone implements Listener {

	@EventHandler
	public void usePowerStoneEntity(PlayerInteractEntityEvent event) {
		Player p = event.getPlayer();

		if (!StoneType.POWER.hasStoneInHand(p) || StoneType.POWER.hasCooldownRightclick(p)) {
			return;
		}

		Entity e = event.getRightClicked();

		if (e instanceof Player) {
			((Player) e).damage(19, p);
		} else if (e instanceof Damageable) {
			((Damageable) e).damage(1000, p);
		} else {
			return;
		}

		World w = p.getWorld();
		SoundUtils.playToAll(p, Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, 1f, 1f);
		w.spawnParticle(Particle.WITCH, e.getLocation(), 50, 0.1, 0.1, 0.1, 0.001);
		StoneType.POWER.applyCooldownRightclick(p);
	}

	@EventHandler
	public void usePowerStone(PlayerInteractEvent event) {
		final Player p = event.getPlayer();

		if (StoneType.POWER.hasStoneInHand(p)) {
			Action a = event.getAction();

			if (a == Action.RIGHT_CLICK_BLOCK && !StoneType.POWER.hasCooldownRightclick(p)) {
				Block b = event.getClickedBlock();
				b.breakNaturally();
				World w = p.getWorld();
				SoundUtils.playToAll(event.getClickedBlock().getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, 1f, 1f);
				w.spawnParticle(Particle.WITCH, event.getClickedBlock().getLocation(), 50, 0.1, 0.1, 0.1, 0.001);
				StoneType.POWER.applyCooldownRightclick(p);
			} else if (ActionUtils.isLeftclick(a) && !StoneType.POWER.hasCooldownLeftclick(p)) {

				new BukkitRunnable() {

					int count = 0;

					@Override
					public void run() {
						Location l = p.getLocation();

						for (double r = 0.5; r < 5; r += 0.5) {
							for (double phi = 0; phi <= Math.PI; phi += Math.PI / 10) {
								for (double theta = 0; theta <= 2 * Math.PI; theta += Math.PI / 20) {
									double x = r * Math.cos(theta) * Math.sin(phi);
									double y = r * Math.cos(phi) + 1.5;
									double z = r * Math.sin(theta) * Math.sin(phi);
									l.add(x, y, z);
									p.getWorld().spawnParticle(Particle.WITCH, l, 1, 0F, 0F, 0F, 0);
									l.subtract(x, y, z);
								}
							}
						}

						for (Entity e : p.getNearbyEntities(5, 5, 5)) {

							if (e instanceof Damageable) {
								Damageable d = (Damageable) e;
								d.damage(4, p);
							}
						}

						SoundUtils.playToAll(p, Sound.ENTITY_ELDER_GUARDIAN_AMBIENT, 1f, 1f);

						if (count >= 20) {
							cancel();
						}

						count++;
					}

				}.runTaskTimer(Main.getPlugin(), 0, 10);

				StoneType.POWER.applyCooldownLeftclick(p);
			}
		}
	}
}
