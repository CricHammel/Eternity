package de.cric_hammel.eternity.util;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import de.cric_hammel.eternity.Main;

public class CustomItem {
	
	public static boolean hasInHand(Player p, String lore, Material m) {
		ItemStack item = p.getInventory().getItemInMainHand();
		if (item != null && item.getItemMeta().hasLore() && item.getItemMeta().getLore().get(0).equals(lore)
				&& item.getType() == m) {
			return true;
		}
		return false;
	}
	
	public static boolean hasInInv(Player p, String lore, Material m) {
		for (ItemStack item : p.getInventory().getContents()) {
			if (item != null && item.getItemMeta().hasLore() && item.getItemMeta().getLore().get(0).equals(lore)
					&& item.getType() == m) {
				return true;
			}
		}
		return false;
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
		stoneMeta.setLore(loreList);
		stone.setItemMeta(stoneMeta);
		return stone;
	}
}
