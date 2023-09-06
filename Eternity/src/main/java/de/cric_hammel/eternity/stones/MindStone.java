package de.cric_hammel.eternity.stones;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import de.cric_hammel.eternity.Main;
import de.cric_hammel.eternity.util.StoneType;

public class MindStone implements Listener{
	
	private static final String INVENTORY_TITLE = ChatColor.YELLOW + "Inventory of ";
	private static final String METADATA_KEY_POSSESSES = "eternity_possesses";
	private static final String METADATA_KEY_ISPOSSESSED = "eternity_ispossessed";
	
	@EventHandler
	public void useMindStoneEntity(PlayerInteractEntityEvent event) {
		if (event.getRightClicked() instanceof Player) {
			Player p = event.getPlayer();
			Player victim = (Player) event.getRightClicked();
			if (StoneType.MIND.hasStoneInHand(p) && !StoneType.MIND.hasCooldownRightclick(p)) {
				Inventory inv = Bukkit.createInventory(p, 9*5, INVENTORY_TITLE + victim.getName());
				inv.setContents(victim.getInventory().getContents());
				p.openInventory(inv);
				StoneType.MIND.applyCooldownRightclick(p);
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void useMindStoneEntityHit(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player && event.getEntity() instanceof Damageable) {
			final Player p = (Player) event.getDamager();
			final Damageable d = (Damageable) event.getEntity();
			if (StoneType.MIND.hasStoneInHand(p) && !StoneType.MIND.hasCooldownLeftclick(p)) {
				if (!p.hasMetadata(METADATA_KEY_ISPOSSESSED) && !d.hasMetadata(METADATA_KEY_POSSESSES)) {
					if (!p.hasMetadata(METADATA_KEY_POSSESSES) && !d.hasMetadata(METADATA_KEY_ISPOSSESSED)) {
						p.setMetadata(METADATA_KEY_POSSESSES, new FixedMetadataValue(Main.getPlugin(), 1));
						d.setMetadata(METADATA_KEY_ISPOSSESSED, new FixedMetadataValue(Main.getPlugin(), 1));
						p.setInvisible(true);
						p.teleport(d);
						
						new BukkitRunnable() {

							@Override
							public void run() {
								if (!p.hasMetadata(METADATA_KEY_POSSESSES)
										&& !d.hasMetadata(METADATA_KEY_ISPOSSESSED)) {
									cancel();
								}

								if (d.isDead() || !p.isOnline()) {
									p.removeMetadata(METADATA_KEY_POSSESSES, Main.getPlugin());
									d.removeMetadata(METADATA_KEY_ISPOSSESSED, Main.getPlugin());
								}

								d.teleport(p);
							}
						}.runTaskTimer(Main.getPlugin(), 0, 1);

						for (Player player : Bukkit.getOnlinePlayers()) {
							player.playSound(p.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_CHARGE, 1f, 2f);
						}
						p.spawnParticle(Particle.PORTAL, d.getLocation(), 300, 0, 0, 0);
						event.setCancelled(true);
					} else if (p.hasMetadata(METADATA_KEY_POSSESSES) && d.hasMetadata(METADATA_KEY_ISPOSSESSED)) {
						p.removeMetadata(METADATA_KEY_POSSESSES, Main.getPlugin());
						d.removeMetadata(METADATA_KEY_ISPOSSESSED, Main.getPlugin());
						p.setInvisible(false);
						for (Player player : Bukkit.getOnlinePlayers()) {
							player.playSound(p.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, 1f, 2f);
						}
						p.spawnParticle(Particle.REVERSE_PORTAL, d.getLocation(), 300, 0, 0, 0);
						StoneType.MIND.applyCooldownLeftclick(p);
						event.setCancelled(true);
					}
				}
				
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (event.getView().getTitle().contains(INVENTORY_TITLE)) {
			event.setCancelled(true);
		}
	}
}
