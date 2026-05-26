package de.cric_hammel.eternity.infinity.items.misc.teleport;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import de.cric_hammel.eternity.infinity.items.CustomItem;
import de.cric_hammel.eternity.infinity.util.ActionUtils;

public class TwelveTeraVoltBattery extends CustomItem {

	private static TwelveTeraVoltBattery instance;

	public static TwelveTeraVoltBattery getInstance() {
		if (null == instance) {
			synchronized (TwelveTeraVoltBattery.class) {
				if (null == instance) {
					instance = new TwelveTeraVoltBattery();
				}
			}
		}
		
		return instance;
	}
	
	private TwelveTeraVoltBattery() {
		super(Material.SEA_PICKLE, Component.text("12-Teravolt Battery", NamedTextColor.AQUA), Component.text("Powers flashlights and/or teleporters"));
	}
	
	public static class Listeners implements Listener {

		@EventHandler
		public void onPlayerInteract(PlayerInteractEvent event) {
			if (!TwelveTeraVoltBattery.getInstance().isItem(event.getItem()) || !ActionUtils.isRightclick(event.getAction())) {
				return;
			}
			
			event.setCancelled(true);
		}
	}
}
