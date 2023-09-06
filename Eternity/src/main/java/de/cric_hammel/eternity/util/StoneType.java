package de.cric_hammel.eternity.util;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import de.cric_hammel.eternity.Main;

public enum StoneType {

	POWER(30, 1, Material.PURPLE_DYE, ChatColor.LIGHT_PURPLE + "Power Stone"),
	SPACE(60, 2, Material.BLUE_DYE, ChatColor.BLUE + "Space Stone"),
	REALITY(0, 0, Material.RED_DYE, ChatColor.DARK_RED + "Reality Stone"),
	SOUL(3, 60, Material.ORANGE_DYE, ChatColor.RED + "Soul Stone"),
	MIND(30, 1, Material.YELLOW_DYE, ChatColor.YELLOW + "Mind Stone"),
	TIME(2, 10, Material.LIME_DYE, ChatColor.GREEN + "Time Stone");

	private static final String METADATA_KEY_LEFT = "eternity_cooldown_left_";
	private static final String METADATA_KEY_RIGHT = "eternity_cooldown_right_";
	private static final String LORE = "One of the six powerful Infinity Stones";

	private final int cooldownLeftclick;
	private final int cooldownRightclick;
	public final Material m;
	private final String itemName;

	private StoneType(int cooldownLeftClick, int cooldownRightClick, Material m, String itemName) {
		this.cooldownLeftclick = cooldownLeftClick;
		this.cooldownRightclick = cooldownRightClick;
		this.m = m;
		this.itemName = itemName;
	}

	public boolean hasStoneInHand(Player p) {
		ItemStack stone = p.getInventory().getItemInMainHand();
		if (stone != null && stone.getItemMeta().hasLore() && stone.getItemMeta().getLore().get(0).equals(LORE)
				&& stone.getType() == m) {
			return true;
		}
		return false;
	}

	public boolean hasStoneInInv(Player p) {
		for (ItemStack stone : p.getInventory().getContents()) {
			if (stone != null && stone.getItemMeta().hasLore() && stone.getItemMeta().getLore().get(0).equals(LORE)
					&& stone.getType() == m) {
				return true;
			}
		}
		return false;
	}

	public static boolean hasAnyStoneInHand(Player p) {
		ItemStack stone = p.getInventory().getItemInMainHand();
		if (stone != null && stone.getItemMeta().hasLore() && stone.getItemMeta().getLore().get(0).equals(LORE)) {
			return true;
		}
		return false;
	}

	public void applyCooldownLeftclick(Player p) {
		String metaKey = METADATA_KEY_LEFT + this.toString();
		if (!p.hasMetadata(metaKey)) {
			p.setMetadata(metaKey,
					new FixedMetadataValue(Main.getPlugin(), System.currentTimeMillis() + cooldownLeftclick * 1000));
			p.setCooldown(m, cooldownLeftclick * 20);
		}
	}

	public void applyCooldownRightclick(Player p) {
		String metaKey = METADATA_KEY_RIGHT + this.toString();
		if (!p.hasMetadata(metaKey)) {
			p.setMetadata(metaKey,
					new FixedMetadataValue(Main.getPlugin(), System.currentTimeMillis() + cooldownRightclick * 1000));
			p.setCooldown(m, cooldownRightclick * 20);
		}
	}

	public boolean hasCooldownLeftclick(Player p) {
		String metaKey = METADATA_KEY_LEFT + this.toString();
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
		return false;
	}

	public boolean hasCooldownRightclick(Player p) {
		String metaKey = METADATA_KEY_RIGHT + this.toString();
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
