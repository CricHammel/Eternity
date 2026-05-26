package de.cric_hammel.eternity.infinity.items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

public abstract class CustomTieredArmor extends CustomArmor {

	private final ItemStack[] tierOne;
	private final ItemStack[] tierTwo;
	private final ItemStack[] tierThree;

	public CustomTieredArmor(ArmorType type, Component name, Component lore) {
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
				List<Component> lore = new ArrayList<>(meta.lore());
				lore.add(Component.text("Tier " + tier));
				meta.lore(lore);
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
			List<Component> loreList = item.getItemMeta().lore();

			if (!(loreList.get(2) instanceof TextComponent tc)
					|| !tc.content().contains(Integer.toString(tier))) {
				return false;
			}
		}

		return true;
	}

	public ItemStack[] getTier(int tier) {
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
