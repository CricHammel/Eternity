package de.cric_hammel.eternity.infinity.items.misc.teleport;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import de.cric_hammel.eternity.infinity.items.CustomItem;
import de.cric_hammel.eternity.infinity.util.ActionUtils;

public class TeleportCapsule extends CustomItem {

	private static TeleportCapsule instance;

	public static TeleportCapsule getInstance() {
		if (null == instance) {
			synchronized (TeleportCapsule.class) {
				if (null == instance) {
					instance = new TeleportCapsule();
				}
			}
		}
		
		return instance;
	}
	
	private TeleportCapsule() {
		super(Material.SUNFLOWER, ChatColor.GOLD + "Teleport Capsule", "Click on Teleport Railgun to load");
	}
	
	public static class Listeners implements Listener {
		
		@EventHandler
		public void onPlayerInteract(PlayerInteractEvent event) {
			if (!(new TeleportCapsule()).isItem(event.getItem()) || !ActionUtils.isRightclick(event.getAction())) {
				return;
			}
			
			event.setCancelled(true);
		}
	}
}
