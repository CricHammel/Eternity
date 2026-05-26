package de.cric_hammel.eternity.infinity.items.misc;

import org.bukkit.Material;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

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
		super(Material.GOLD_INGOT, Component.text("InfiniCoin", NamedTextColor.GOLD), Component.text("Buy the most mystical items"));
	}
}
