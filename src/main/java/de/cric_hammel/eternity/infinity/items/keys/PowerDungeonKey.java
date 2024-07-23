package de.cric_hammel.eternity.infinity.items.keys;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import de.cric_hammel.eternity.infinity.items.CustomItem;

public class PowerDungeonKey extends CustomItem {

	private static PowerDungeonKey instance;

	public static PowerDungeonKey getInstance() {
		if (null == instance) {
			synchronized (PowerDungeonKey.class) {
				if (null == instance) {
					instance = new PowerDungeonKey();
				}
			}
		}
		
		return instance;
	}
	
	private PowerDungeonKey() {
		super(Material.NAME_TAG, ChatColor.GOLD + "Power Dungeon Key", "Allows you to access the Power Dungeon");
	}
}
