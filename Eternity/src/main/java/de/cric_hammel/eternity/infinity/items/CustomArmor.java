package de.cric_hammel.eternity.infinity.items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.cric_hammel.eternity.Main;

public abstract class CustomArmor {
	
	private final String lore;
	private final ItemStack[] armor = new ItemStack[4];
	
	public CustomArmor(ArmorType type, String name, String lore) {
		this.lore = lore;
		
		armor[0] = createItem(type.helmetType, name + " Helmet");
		armor[1]= createItem(type.chestplateType, name + " Chestplate");
		armor[2] = createItem(type.leggingsType, name + " Leggings");
		armor[3] = createItem(type.bootsType, name + " Boots");
	}
	
	private ItemStack createItem(Material m, String name) {
		ItemStack item = new ItemStack(m);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(name);
		ArrayList<String> loreList = new ArrayList<String>();
		loreList.add(lore);
		loreList.add(Main.LORE_ID);
		itemMeta.setLore(loreList);
		item.setItemMeta(itemMeta);
		return item;
	}
	
	public boolean isWearing(Player p) {
		try {
			ItemStack[] playerArmor = p.getInventory().getArmorContents();
			for (ItemStack item : playerArmor) {
				List<String> loreList = item.getItemMeta().getLore();
				if (!loreList.get(1).equals(Main.LORE_ID) || !loreList.get(0).equals(lore)) {
					return false;
				}
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public ItemStack[] getArmor() {
		return armor.clone();
	}

	public String getLore() {
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