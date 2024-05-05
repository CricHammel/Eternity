package de.cric_hammel.eternity.infinity.items;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
			ItemMeta meta = item.getItemMeta();
			List<String> lore = meta.getLore();
			lore.add("Tier " + tier);
			meta.setLore(lore);
			item.setItemMeta(meta);
		} catch (Exception e) {
			return;
		}
	}

	public abstract void changeTierOne();

	public abstract void changeTierTwo();

	public abstract void changeTierThree();

	public boolean hasTierInHand(Player p, int tier) {
		ItemStack item = p.getInventory().getItemInMainHand();
		
		if (item == null || !item.hasItemMeta()) {
			return false;
		}
		
		ItemMeta meta = item.getItemMeta();
		
		if (!meta.hasLore()) {
			return false;
		}
		
		List<String> loreList = meta.getLore();
		
		if (loreList.size() < 2) {
			return false;
		}

		if (super.hasInHand(p) && loreList.get(2).contains(Integer.toString(tier))) {
			return true;
		}

		return false;
	}

	public ItemStack getTier(int tier) {
		switch (tier) {
			case 1:
				return tierOne;
			case 2:
				return tierTwo;
			case 3:
				return tierThree;
			default:
				return null;
		}
	}
}
