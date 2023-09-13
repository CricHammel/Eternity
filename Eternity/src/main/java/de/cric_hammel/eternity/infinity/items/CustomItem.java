package de.cric_hammel.eternity.infinity.items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import de.cric_hammel.eternity.Main;

public class CustomItem {
	
	private static final String LORE_ID = ChatColor.MAGIC + "eternity";
	
	public static boolean hasInHand(Player p, String lore, Material m) {
		try {
			ItemStack item = p.getInventory().getItemInMainHand();
			List<String> loreList = item.getItemMeta().getLore();
			if (loreList.get(1).equals(LORE_ID) && loreList.get(0).equals(lore) && item.getType() == m) {
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static boolean hasInInv(Player p, String lore, Material m) {
		for (ItemStack item : p.getInventory().getContents()) {
			try {
				List<String> loreList = item.getItemMeta().getLore();
				if (loreList.get(1).equals(LORE_ID) && loreList.get(0).equals(lore) && item.getType() == m) {
					return true;
				}
			} catch (Exception e) {
				
			}
		}
		return false;
	}
	
	public static boolean hasAnyInHand(Player p) {
		try {
			ItemStack item = p.getInventory().getItemInMainHand();
			List<String> loreList = item.getItemMeta().getLore();
			if (loreList.get(1).equals(LORE_ID)) {
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static void applyCooldown(Player p, String metaKey, int cooldown, Material m) {
		if (!p.hasMetadata(metaKey)) {
			p.setMetadata(metaKey,
					new FixedMetadataValue(Main.getPlugin(), System.currentTimeMillis() + cooldown * 1000));
			p.setCooldown(m, cooldown * 20);
		}
	}
	
	public static boolean hasCooldown(Player p, String metaKey, Material m) {
		if (p.hasMetadata(metaKey)) {
			long time = (long) p.getMetadata(metaKey).get(0).value();
			if (System.currentTimeMillis() > time) {
				p.removeMetadata(metaKey, Main.getPlugin());
				p.setCooldown(m, 0);
				return false;
			}
			p.setCooldown(m, Math.round(time - System.currentTimeMillis()) / 50);
			return true;
		}
		return false;	}
	
	public static ItemStack getItem(Material m, String name, String lore) {
		ItemStack stone = new ItemStack(m);
		ItemMeta stoneMeta = stone.getItemMeta();
		stoneMeta.setDisplayName(name);
		ArrayList<String> loreList = new ArrayList<String>();
		loreList.add(lore);
		loreList.add(LORE_ID);
		stoneMeta.setLore(loreList);
		stone.setItemMeta(stoneMeta);
		return stone;
	}
}
