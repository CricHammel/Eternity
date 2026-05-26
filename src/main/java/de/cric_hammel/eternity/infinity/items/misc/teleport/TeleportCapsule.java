package de.cric_hammel.eternity.infinity.items.misc.teleport;

import org.bukkit.Material;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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
		super(Material.SUNFLOWER, Component.text("Teleport Capsule", NamedTextColor.GOLD), Component.text("Click on Teleport Railgun to load"));
	}
	
	public static class Listeners implements Listener {
		
		@EventHandler
		public void onPlayerInteract(PlayerInteractEvent event) {
			if (!TeleportCapsule.getInstance().isItem(event.getItem()) || !ActionUtils.isRightclick(event.getAction())) {
				return;
			}
			
			event.setCancelled(true);
		}
	}
}
