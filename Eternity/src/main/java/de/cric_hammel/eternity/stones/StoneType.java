package de.cric_hammel.eternity.stones;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.cric_hammel.eternity.util.CustomItem;

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
		return CustomItem.hasInHand(p, LORE, m);
	}

	public boolean hasStoneInInv(Player p) {
		return CustomItem.hasInInv(p, LORE, m);
	}

	public static boolean hasAnyStoneInHand(Player p) {
		ItemStack stone = p.getInventory().getItemInMainHand();
		if (stone != null && stone.hasItemMeta() && stone.getItemMeta().hasLore() && stone.getItemMeta().getLore().get(0).equals(LORE)) {
			return true;
		}
		return false;
	}

	public void applyCooldownLeftclick(Player p) {
		CustomItem.applyCooldown(p, METADATA_KEY_LEFT + this.toString(), cooldownLeftclick, m);
	}

	public void applyCooldownRightclick(Player p) {
		CustomItem.applyCooldown(p, METADATA_KEY_RIGHT + this.toString(), cooldownRightclick, m);
	}

	public boolean hasCooldownLeftclick(Player p) {
		return CustomItem.hasCooldown(p, METADATA_KEY_LEFT + this.toString(), m);
	}

	public boolean hasCooldownRightclick(Player p) {
		return CustomItem.hasCooldown(p, METADATA_KEY_RIGHT + this.toString(), m);
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
		ItemStack stone = CustomItem.getItem(m, itemName, LORE);
		stone.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1);
		return stone;
	}
}
