package de.cric_hammel.eternity.infinity.items;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.kyori.adventure.text.Component;

import de.cric_hammel.eternity.Main;

public abstract class CustomArmor {

	private final Component lore;
	private final ItemStack[] armor = new ItemStack[4];

	public CustomArmor(ArmorType type, Component name, Component lore) {
		this.lore = lore;

		armor[0] = createItem(type.bootsType, name.append(Component.text(" Boots")));
		armor[1] = createItem(type.leggingsType, name.append(Component.text(" Leggings")));
		armor[2] = createItem(type.chestplateType, name.append(Component.text(" Chestplate")));
		armor[3] = createItem(type.helmetType, name.append(Component.text(" Helmet")));
	}

	private ItemStack createItem(Material m, Component name) {
		ItemStack item = new ItemStack(m);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.displayName(name);
		itemMeta.lore(List.of(lore, Main.LORE_ID));
		item.setItemMeta(itemMeta);
		item.addUnsafeEnchantment(Enchantment.INFINITY, 1);
		return item;
	}

	public boolean isWearing(LivingEntity m) {
		ItemStack[] entityArmor = m.getEquipment().getArmorContents();

		for (ItemStack item : entityArmor) {

			if (item == null || !item.hasItemMeta()) {
				return false;
			}

			ItemMeta meta = item.getItemMeta();

			if (!meta.hasLore()) {
				return false;
			}

			List<Component> loreList = meta.lore();

			if (loreList.size() < 2) {
				return false;
			}

			if (!loreList.get(1).equals(Main.LORE_ID) || !loreList.get(0).equals(lore)) {
				return false;
			}
		}

		return true;
	}

	public ItemStack[] getArmor() {
		ItemStack[] cloned = armor.clone();
		for (int i = 0; i <= 3; i++) {
			cloned[i] = cloned[i].clone();
		}
		return cloned;
	}

	public Component getLore() {
		return lore;
	}

	public enum ArmorType {

		LEATHER(Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS),
		CHAINMAIL(Material.CHAINMAIL_HELMET, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_LEGGINGS, Material.CHAINMAIL_BOOTS),
		IRON(Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS),
		GOLD(Material.GOLDEN_HELMET, Material.GOLDEN_CHESTPLATE, Material.GOLDEN_LEGGINGS, Material.GOLDEN_BOOTS),
		DIAMOND(Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS),
		NETHERITE(Material.NETHERITE_HELMET, Material.NETHERITE_CHESTPLATE, Material.NETHERITE_LEGGINGS, Material.NETHERITE_BOOTS);

		private final Material helmetType;
		private final Material chestplateType;
		private final Material leggingsType;
		private final Material bootsType;

		ArmorType(Material helmetType, Material chestplateType, Material leggingsType, Material bootsType) {
			this.helmetType = helmetType;
			this.chestplateType = chestplateType;
			this.leggingsType = leggingsType;
			this.bootsType = bootsType;
		}
	}
}
