package de.cric_hammel.eternity.stones;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class InfinityStone implements Listener{
	
	@EventHandler
	public void useInfinityStone(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		if (StoneType.hasAnyStoneInHand(p)) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void useInfinityStoneEntity(PlayerInteractEntityEvent event) {
		Player p = event.getPlayer();
		if (StoneType.hasAnyStoneInHand(p)) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void useInfinityStoneEntityHit(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player) {
			Player p = (Player) event.getDamager();
			if (StoneType.hasAnyStoneInHand(p)) {
				event.setCancelled(true);
			}
		}
	}
}
