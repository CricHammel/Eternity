package de.cric_hammel.eternity.infinity.items.stones;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import de.cric_hammel.eternity.Main;

public class RealityStone implements Listener {

	private static final String METADATA_KEY = "eternity_scaffold";

	@EventHandler
	public void useRealityStone(PlayerInteractEvent event) {
		final Player p = event.getPlayer();
		if (StoneType.REALITY.hasStoneInHand(p)) {
			World w = p.getWorld();
			Action a = event.getAction();
			if (p.getLocation().getPitch() == -90d) {
				if (a == Action.RIGHT_CLICK_AIR) {
					w.setTime(w.getTime() + 900);
					w.spawnParticle(Particle.END_ROD, p.getLocation().add(0, 3.5, 0), 50, 0.4, 0.25, 0.4, 0.01);
					w.playSound(p.getLocation(), Sound.ENTITY_ENDER_EYE_DEATH, 1, 1);
				} else if (a == Action.LEFT_CLICK_AIR) {
					if (w.isClearWeather()) {
						w.setStorm(true);
					} else {
						w.setStorm(false);
						w.setThundering(false);
					}
					w.spawnParticle(Particle.VILLAGER_ANGRY, p.getLocation().add(0, 3.5, 0), 20, 0.5, 0, 0.5, 1);
					w.playSound(p.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1, 1);
				}
			} else {
				if (a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK) {
					p.setMetadata(METADATA_KEY, new FixedMetadataValue(Main.getPlugin(), true));
					p.playSound(p.getLocation(), Sound.BLOCK_END_PORTAL_FRAME_FILL, 1, 2);
				} else if (a == Action.LEFT_CLICK_AIR || a == Action.LEFT_CLICK_BLOCK) {
					p.removeMetadata(METADATA_KEY, Main.getPlugin());
					p.playSound(p.getLocation(), Sound.BLOCK_END_PORTAL_FRAME_FILL, 1, 0);
				}
			}
		}
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Location from = event.getFrom();
		Location to = event.getTo();

		if (to.getBlockX() == from.getBlockX() && to.getBlockY() == from.getBlockY()
				&& to.getBlockZ() == from.getBlockZ()) {
			return;
		}

		final Player p = event.getPlayer();

		if (p.hasMetadata(METADATA_KEY)) {
			final Block underPlayer;
			if (event.getPlayer().isSneaking()) {
				underPlayer = to.getBlock().getRelative(BlockFace.DOWN).getRelative(BlockFace.DOWN);
			} else {
				underPlayer = to.getBlock().getRelative(BlockFace.DOWN);
			}

//			Material previousType = underPlayer.getType();
			final BlockState state;
			if (!underPlayer.hasMetadata(METADATA_KEY)) {
				state = underPlayer.getState();
				underPlayer.setMetadata(METADATA_KEY, new FixedMetadataValue(Main.getPlugin(), state));
			} else {
				state = (BlockState) underPlayer.getMetadata(METADATA_KEY).get(0).value();
			}

			underPlayer.setType(Material.RED_STAINED_GLASS);
			p.playSound(underPlayer.getLocation(), Sound.BLOCK_GLASS_PLACE, 1, 2);
			new BukkitRunnable() {
				@Override
				public void run() {
					if (underPlayer.hasMetadata(METADATA_KEY)) {
						state.update(true);
						underPlayer.removeMetadata(METADATA_KEY, Main.getPlugin());
						p.playSound(underPlayer.getLocation(), Sound.BLOCK_GLASS_BREAK, 1, 2);
					}
				}
			}.runTaskLater(Main.getPlugin(), 20 * 2);
		}
	}
}
