package de.cric_hammel.eternity.infinity.loot;

import java.util.List;
import java.util.Random;

import org.bukkit.inventory.ItemStack;

public interface CustomEntityLootTable {
	public void generateDrops(List<ItemStack> drops, Random random);
}
