package de.cric_hammel.eternity.infinity.items;

import java.util.List;

import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class CustomTieredArmor extends CustomArmor {

	private final ItemStack[] tierOne;
	private final ItemStack[] tierTwo;
	private final ItemStack[] tierThree;

	public CustomTieredArmor(ArmorType type, String name, String lore) {
		super(type, name, lore);
		tierOne = super.getArmor();
		setTier(tierOne, 1);
		tierTwo = super.getArmor();
		setTier(tierTwo, 2);
		tierThree = super.getArmor();
		setTier(tierThree, 3);
		changeTierOne();
		changeTierTwo();
		changeTierThree();
	}

	private void setTier(ItemStack[] armor, int tier) {

		try {

			for (ItemStack item : armor) {
				ItemMeta meta = item.getItemMeta();
				List<String> lore = meta.getLore();
				lore.add("Tier " + tier);
				meta.setLore(lore);
				item.setItemMeta(meta);
			}

		} catch (Exception e) {
			return;
		}
	}

	public abstract void changeTierOne();

	public abstract void changeTierTwo();

	public abstract void changeTierThree();

	public boolean isWearingTier(LivingEntity e, int tier) {

		if (!super.isWearing(e)) {
			return false;
		}

		ItemStack[] entityArmor = e.getEquipment().getArmorContents();

		for (ItemStack item : entityArmor) {
			List<String> loreList = item.getItemMeta().getLore();

			if (!loreList.get(2).contains(Integer.toString(tier))) {
				return false;
			}
		}

		return true;
	}

	public ItemStack[] getTierOne() {
		return tierOne;
	}

	public ItemStack[] getTierTwo() {
		return tierTwo;
	}

	public ItemStack[] getTierThree() {
		return tierThree;
	}
}
