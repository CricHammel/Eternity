package de.cric_hammel.eternity.infinity.loot;

import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import de.cric_hammel.eternity.infinity.items.kree.XylopMeat;

public class XylopLoot implements CustomEntityLootTable {

	@Override
	public void generateDrops(List<ItemStack> drops, Random random) {
		drops.clear();
		
		if (random.nextBoolean()) {
			drops.add(XylopMeat.getInstance().getItem());
		}
		
		drops.add(new ItemStack(Material.PORKCHOP, random.nextInt(1, 3 + 1)));
		
		int carrotChance = random.nextInt(0, 99 + 1);
		
		if (carrotChance < 20) {
			drops.add(new ItemStack(Material.CARROT));
		}
		
		int mushroomChance = random.nextInt(0, 99 + 1);
		
		if (mushroomChance < 20) {
			drops.add(new ItemStack(Material.BROWN_MUSHROOM));
		}
	}
}
