package de.cric_hammel.eternity.infinity.items.stones;

import java.util.HashMap;

import org.bukkit.EntityEffect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import de.cric_hammel.eternity.Main;
import de.cric_hammel.eternity.infinity.util.ActionUtils;
import de.cric_hammel.eternity.infinity.util.SoundUtils;

public class SpaceStone implements Listener {

	private final HashMap<Player, GameMode> lastGameMode = new HashMap<Player, GameMode>();

	@EventHandler
	public void useSpaceStone(PlayerInteractEvent event) {
		final Player p = event.getPlayer();

		if (!StoneType.SPACE.hasStoneInHand(p)) {
			return;
		}

		Action a = event.getAction();

		if (ActionUtils.isRightclick(a) && !StoneType.SPACE.hasCooldownRightclick(p)) {
			teleport(p);
		} else if (ActionUtils.isLeftclick(a) && !StoneType.SPACE.hasCooldownLeftclick(p)
				&& p.getGameMode() != GameMode.SPECTATOR) {
			lastGameMode.put(p, p.getGameMode());
			p.setGameMode(GameMode.SPECTATOR);
			SoundUtils.playToAll(p, Sound.BLOCK_BEACON_ACTIVATE, 1f, 1f);
			p.playEffect(EntityEffect.TELEPORT_ENDER);

			new BukkitRunnable() {

				@Override
				public void run() {
					p.setGameMode(lastGameMode.get(p));
					lastGameMode.remove(p);
					SoundUtils.playToAll(p, Sound.BLOCK_BEACON_DEACTIVATE, 1f, 1f);
					p.playEffect(EntityEffect.TELEPORT_ENDER);
				}

			}.runTaskLater(Main.getPlugin(), 5 * 20);

			StoneType.SPACE.applyCooldownLeftclick(p);
		}
	}

	private void teleport(final Player p) {
		World currentWorld = p.getWorld();
		Location currentLocation = p.getLocation();
		Entity armorStand1 = currentWorld.spawnEntity(currentLocation, EntityType.ARMOR_STAND);
		ArmorStand armorStand = (ArmorStand) armorStand1;
		armorStand.setInvisible(true);
		float yaw = p.getLocation().getYaw();
		float pitch = p.getLocation().getPitch();
		armorStand.setGravity(false);
		armorStand.setInvulnerable(true);
		armorStand.setRotation(yaw, pitch);

		boolean inBlock = false;
		int breakCounter = 0;

		while (!inBlock) {
			breakCounter++;

			if (breakCounter >= 1000) {
				armorStand.remove();
				break;
			}

			armorStand.teleport(armorStand.getLocation().add(armorStand.getLocation().getDirection()));

			if (!armorStand.getLocation().getBlock().isPassable()) {
				inBlock = true;
				armorStand.teleport(armorStand.getLocation().add(armorStand.getLocation().getDirection().multiply(-1)));
				p.teleport(armorStand);
				p.setFallDistance(0);
				p.getLocation().getDirection().zero();
				SoundUtils.playToAll(p, Sound.ENTITY_ENDERMAN_TELEPORT, 2f, 1f);
				p.playEffect(EntityEffect.TELEPORT_ENDER);
				armorStand.remove();
				StoneType.SPACE.applyCooldownRightclick(p);
			}
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player p = event.getPlayer();

		if (lastGameMode.containsKey(p)) {
			p.setGameMode(lastGameMode.get(p));
		}
	}
}
