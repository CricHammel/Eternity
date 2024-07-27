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
import de.cric_hammel.eternity.infinity.util.ActionUtils;
import de.cric_hammel.eternity.infinity.util.SoundUtils;

public class RealityStone implements Listener {

	private static final String METADATA_KEY = "eternity_scaffold";

	@EventHandler
	public void useRealityStone(PlayerInteractEvent event) {
		final Player p = event.getPlayer();

		if (!StoneType.REALITY.hasStoneInHand(p)) {
			return;
		}

		World w = p.getWorld();
		Action a = event.getAction();

		if (p.getLocation().getPitch() == -90d) {

			if (ActionUtils.isRightclick(a)) {
				w.setTime(w.getTime() + 900);
				SoundUtils.playToAll(p, Sound.ENTITY_ENDER_EYE_DEATH, 1f, 1f);
				w.spawnParticle(Particle.END_ROD, p.getLocation().add(0, 3.5, 0), 50, 0.4, 0.25, 0.4, 0.01);
			} else if (ActionUtils.isLeftclick(a)) {

				if (w.isClearWeather()) {
					w.setStorm(true);
				} else {
					w.setStorm(false);
					w.setThundering(false);
				}

				SoundUtils.playToAll(p, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1f, 1f);
				w.spawnParticle(Particle.ANGRY_VILLAGER, p.getLocation().add(0, 3.5, 0), 20, 0.5, 0, 0.5, 1);
			}

		} else {

			if (ActionUtils.isRightclick(a)) {
				p.setMetadata(METADATA_KEY, new FixedMetadataValue(Main.getPlugin(), true));
				SoundUtils.playToAll(p, Sound.BLOCK_END_PORTAL_FRAME_FILL, 1f, 2f);
			} else if (ActionUtils.isLeftclick(a)) {
				p.removeMetadata(METADATA_KEY, Main.getPlugin());
				SoundUtils.playToAll(p, Sound.BLOCK_END_PORTAL_FRAME_FILL, 1f, 0f);
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

		if (!p.hasMetadata(METADATA_KEY)) {
			return;
		}

		final Block underPlayer;

		if (event.getPlayer().isSneaking()) {
			underPlayer = to.getBlock().getRelative(BlockFace.DOWN).getRelative(BlockFace.DOWN);
		} else {
			underPlayer = to.getBlock().getRelative(BlockFace.DOWN);
		}

		final BlockState state;

		if (!underPlayer.hasMetadata(METADATA_KEY)) {
			state = underPlayer.getState();
			underPlayer.setMetadata(METADATA_KEY, new FixedMetadataValue(Main.getPlugin(), state));
		} else {
			state = (BlockState) underPlayer.getMetadata(METADATA_KEY).get(0).value();
		}

		underPlayer.setType(Material.RED_STAINED_GLASS);
		SoundUtils.playToAll(underPlayer.getLocation(), Sound.BLOCK_GLASS_PLACE, 1, 2f);

		new BukkitRunnable() {

			@Override
			public void run() {

				if (underPlayer.hasMetadata(METADATA_KEY)) {
					state.update(true);
					underPlayer.removeMetadata(METADATA_KEY, Main.getPlugin());
					SoundUtils.playToAll(underPlayer.getLocation(), Sound.BLOCK_GLASS_BREAK, 1f, 2f);
				}
			}

		}.runTaskLater(Main.getPlugin(), 20 * 2);
	}
}
