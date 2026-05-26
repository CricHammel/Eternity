package de.cric_hammel.eternity.infinity.items.keys;

import org.bukkit.Material;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

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
		super(Material.NAME_TAG, Component.text("Power Dungeon Key", NamedTextColor.GOLD), Component.text("Allows you to access the Power Dungeon"));
	}
}
