package de.cric_hammel.eternity.infinity.items.stones;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import de.cric_hammel.eternity.infinity.items.CustomItem;

public class InfinityStoneListener implements Listener{
	
	@EventHandler
	public void useInfinityStone(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		if (CustomItem.hasAnyInHand(p)) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void useInfinityStoneEntity(PlayerInteractEntityEvent event) {
		Player p = event.getPlayer();
		if (CustomItem.hasAnyInHand(p)) {
			event.setCancelled(true);
		}
	}
}
