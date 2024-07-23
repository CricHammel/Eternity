package de.cric_hammel.eternity.infinity.items.keys;

import org.bukkit.ChatColor;
import org.bukkit.Material;

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
		super(Material.HEART_OF_THE_SEA, ChatColor.GOLD + "Dungeon Key Core", "Allows you to trade for the higher Dungeon Keys");
	}
}
