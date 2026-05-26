package de.cric_hammel.eternity.infinity.items.keys;

import org.bukkit.Material;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import de.cric_hammel.eternity.infinity.items.CustomItem;

public class DungeonKeyCore extends CustomItem {

	private static DungeonKeyCore instance;
	
	public static DungeonKeyCore getInstance() {
		if (null == instance) {
			synchronized (DungeonKeyCore.class) {
				if (null == instance) {
					instance = new DungeonKeyCore();
				}
			}
		}
		
		return instance;
	}
	
	private DungeonKeyCore() {
		super(Material.HEART_OF_THE_SEA, Component.text("Dungeon Key Core", NamedTextColor.GOLD), Component.text("Allows you to trade for the higher Dungeon Keys"));
	}
}
