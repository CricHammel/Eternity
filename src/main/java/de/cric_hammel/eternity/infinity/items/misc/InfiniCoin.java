package de.cric_hammel.eternity.infinity.items.misc;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import de.cric_hammel.eternity.infinity.items.CustomItem;

public class InfiniCoin extends CustomItem {

	private static InfiniCoin instance;

	public static InfiniCoin getInstance() {
		if (null == instance) {
			synchronized (InfiniCoin.class) {
				if (null == instance) {
					instance = new InfiniCoin();
				}
			}
		}
		
		return instance;
	}

	private InfiniCoin() {
		super(Material.GOLD_INGOT, ChatColor.GOLD + "InfiniCoin", "Buy the most mystical items");
	}
}
