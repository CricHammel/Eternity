package de.cric_hammel.eternity.infinity.items;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class CustomTieredItem extends CustomItem {

	private final ItemStack tierOne;
	private final ItemStack tierTwo;
	private final ItemStack tierThree;

	public CustomTieredItem(Material m, String name, String lore) {
		super(m, name, lore);
		tierOne = super.getItem();
		setTier(tierOne, 1);
		tierTwo = super.getItem();
		setTier(tierTwo, 2);
		tierThree = super.getItem();
		setTier(tierThree, 3);
		changeTierOne();
		changeTierTwo();
		changeTierThree();
	}

	private void setTier(ItemStack item, int tier) {

		try {
			item.getItemMeta().getLore().set(2, "Tier " + tier);
		} catch (Exception e) {
			return;
		}
	}

	public abstract void changeTierOne();

	public abstract void changeTierTwo();

	public abstract void changeTierThree();

	public boolean hasTierInHand(Player p, int tier) {

		try {
			ItemStack item = p.getInventory().getItemInMainHand();
			List<String> loreList = item.getItemMeta().getLore();

			if (super.hasInHand(p) && loreList.get(2).contains(Integer.toString(tier))) {
				return true;
			}

			return false;
		} catch (Exception e) {
			return false;
		}
	}

	public ItemStack getTierOne() {
		return tierOne;
	}

	public ItemStack getTierTwo() {
		return tierTwo;
	}

	public ItemStack getTierThree() {
		return tierThree;
	}
}
