package de.cric_hammel.eternity.infinity.loot;

import java.util.Random;

import org.bukkit.inventory.Inventory;

public interface CustomChestLootTable {
	public void generateLoot(Inventory inventory, Random random);
}
