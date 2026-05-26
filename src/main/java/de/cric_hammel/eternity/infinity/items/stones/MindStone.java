package de.cric_hammel.eternity.infinity.items.stones;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import de.cric_hammel.eternity.Main;
import de.cric_hammel.eternity.infinity.util.SoundUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;

public class MindStone implements Listener {

	private static final String INVENTORY_TITLE_TEXT = "Inventory of ";
	private static final Set<Player> POSSESSING = new HashSet<>();
	private static final Set<Damageable> POSSESSED = new HashSet<>();

	@EventHandler
	public void useMindStoneEntity(PlayerInteractEntityEvent event) {

		if (!(event.getRightClicked() instanceof Player)) {
			return;
		}

		Player p = event.getPlayer();

		if (!StoneType.MIND.hasStoneInHand(p) || StoneType.MIND.hasCooldown(p)) {
			return;
		}

		Player victim = (Player) event.getRightClicked();
		Inventory inv = Bukkit.createInventory(p, 9 * 5, Component.text(INVENTORY_TITLE_TEXT + victim.getName(), NamedTextColor.YELLOW));
		inv.setContents(victim.getInventory().getContents());
		p.openInventory(inv);
		StoneType.MIND.applyCooldownRightclick(p);
	}

	@EventHandler
	public void useMindStoneEntityHit(EntityDamageByEntityEvent event) {

		if (!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof Damageable)) {
			return;
		}

		final Player p = (Player) event.getDamager();

		if (!StoneType.MIND.hasStoneInHand(p) || StoneType.MIND.hasCooldown(p)) {
			return;
		}

		final Damageable d = (Damageable) event.getEntity();

		if (POSSESSED.contains(p) || POSSESSING.contains(d)) {
			return;
		}

		if (!POSSESSING.contains(p) && !POSSESSED.contains(d)) {
			possess(p, d);
		} else if (POSSESSING.contains(p) && POSSESSED.contains(d)) {
			unpossess(p, d);
		}
	}

	private void possess(final Player p, final Damageable d) {
		POSSESSING.add(p);
		POSSESSED.add(d);
		p.setInvisible(true);
		p.teleport(d);

		new BukkitRunnable() {

			@Override
			public void run() {

				if (!POSSESSING.contains(p) && !POSSESSED.contains(d)) {
					cancel();
				}

				if (d.isDead() || !p.isOnline()) {
					POSSESSING.remove(p);
					POSSESSED.remove(d);
				}

				d.teleport(p);
			}

		}.runTaskTimer(Main.getPlugin(), 0, 1);

		SoundUtils.playToAll(p, Sound.BLOCK_RESPAWN_ANCHOR_CHARGE, 1f, 2f);

		p.spawnParticle(Particle.PORTAL, d.getLocation(), 300, 0, 0, 0);
	}

	private void unpossess(final Player p, final Damageable d) {
		POSSESSING.remove(p);
		POSSESSED.remove(d);
		p.setInvisible(false);

		SoundUtils.playToAll(p, Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, 1f, 2f);
		p.spawnParticle(Particle.REVERSE_PORTAL, d.getLocation(), 300, 0, 0, 0);
		StoneType.MIND.applyCooldownLeftclick(p);
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {

		if (event.getView().title() instanceof TextComponent tc && tc.content().contains(INVENTORY_TITLE_TEXT)) {
			event.setCancelled(true);
		}
	}
}
