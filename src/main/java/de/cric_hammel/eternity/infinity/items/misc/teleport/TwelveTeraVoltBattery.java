package de.cric_hammel.eternity.infinity.items.misc.teleport;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import de.cric_hammel.eternity.infinity.items.CustomItem;
import de.cric_hammel.eternity.infinity.util.ActionUtils;
import net.md_5.bungee.api.ChatColor;

public class TwelveTeraVoltBattery extends CustomItem {

	public TwelveTeraVoltBattery() {
		super(Material.SEA_PICKLE, ChatColor.AQUA + "12-Teravolt Battery", "Powers flashlights and/or teleporters");
	}
	
	public static class Listeners implements Listener {

		@EventHandler
		public void onPlayerInteract(PlayerInteractEvent event) {
			if (!(new TwelveTeraVoltBattery()).isItem(event.getItem()) || !ActionUtils.isRightclick(event.getAction())) {
				return;
			}
			
			event.setCancelled(true);
		}
	}
}
