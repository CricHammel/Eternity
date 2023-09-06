package de.cric_hammel.eternity.util;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.cric_hammel.eternity.Main;

public enum StoneType {

	POWER(Material.PURPLE_DYE, ChatColor.LIGHT_PURPLE + "Power Stone"),
	SPACE(Material.BLUE_DYE, ChatColor.BLUE + "Space Stone"),
	REALITY(Material.RED_DYE, ChatColor.DARK_RED + "Reality Stone"),
	SOUL(Material.ORANGE_DYE, ChatColor.RED + "Soul Stone"),
	MIND(Material.YELLOW_DYE, ChatColor.YELLOW + "Mind Stone"),
	TIME(Material.LIME_DYE, ChatColor.GREEN + "Time Stone");
	
	private static final String LORE = "One of the six powerful Infinity Stones";
	
	public final Material m;
	private final String itemName;
	
	private StoneType(Material m, String itemName) {
		this.m = m;
		this.itemName = itemName;
	}
	
	public boolean hasStoneInHand(Player p) {
		ItemStack stone = p.getInventory().getItemInMainHand();
		if (stone != null && stone.getItemMeta().hasLore() && stone.getItemMeta().getLore().get(0).equals(LORE) && stone.getType() == m) {
			return true;
		}
		return false;
	}
	
	public boolean hasStoneInInv(Player p) {
		for (ItemStack stone : p.getInventory().getContents()) {
			if (stone != null && stone.getItemMeta().hasLore() && stone.getItemMeta().getLore().get(0).equals(LORE) && stone.getType() == m) {
				return true;
			}
		}
		return false;
	}
	
	public static StoneType getValue(String v) {
		for (StoneType type : StoneType.values()) {
			if (type.name().equalsIgnoreCase(v)) {
				return type;
			}
		}
		return null;
	}
	
	public ItemStack getItem() {
		ItemStack stone = new ItemStack(m);
		ItemMeta stoneMeta = stone.getItemMeta();
		stoneMeta.setDisplayName(itemName);
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(LORE);
		stoneMeta.setLore(lore);
		stone.setItemMeta(stoneMeta);
		stone.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1);
		return stone;
	}
}
