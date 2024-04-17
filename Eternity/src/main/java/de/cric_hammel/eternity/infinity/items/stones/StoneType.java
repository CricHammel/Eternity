package de.cric_hammel.eternity.infinity.items.stones;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.cric_hammel.eternity.Main;
import de.cric_hammel.eternity.infinity.items.CustomItem;
import de.cric_hammel.eternity.infinity.items.gauntlet.Gauntlet;

public enum StoneType {

	POWER(30, 1, Material.PURPLE_DYE, ChatColor.LIGHT_PURPLE + "Power Stone"),
	SPACE(60, 2, Material.BLUE_DYE, ChatColor.BLUE + "Space Stone"),
	REALITY(0, 0, Material.RED_DYE, ChatColor.DARK_RED + "Reality Stone"),
	SOUL(3, 60, Material.ORANGE_DYE, ChatColor.RED + "Soul Stone"),
	MIND(30, 1, Material.YELLOW_DYE, ChatColor.YELLOW + "Mind Stone"),
	TIME(2, 10, Material.LIME_DYE, ChatColor.GREEN + "Time Stone");

	private static final String METADATA_KEY_COOLDOWN_LEFT = "eternity_cooldown_left_";
	private static final String METADATA_KEY_COOLDOWN_RIGHT = "eternity_cooldown_right_";

	private final int cooldownLeftclick;
	private final int cooldownRightclick;
	public final Material m;
	private final InfinityStone infinityStone;

	private StoneType(int cooldownLeftClick, int cooldownRightClick, Material m, String itemName) {
		this.cooldownLeftclick = cooldownLeftClick;
		this.cooldownRightclick = cooldownRightClick;
		this.m = m;
		infinityStone = new InfinityStone(m, itemName);
	}

	public boolean hasStoneInHand(Player p) {
		return infinityStone.hasInHand(p);
	}

	public boolean hasStoneInInv(Player p) {
		return infinityStone.hasInInv(p);
	}

	public void applyCooldownLeftclick(Player p) {
		infinityStone.applyCooldown(p, METADATA_KEY_COOLDOWN_LEFT + this.toString(), cooldownLeftclick);
	}

	public void applyCooldownRightclick(Player p) {
		infinityStone.applyCooldown(p, METADATA_KEY_COOLDOWN_RIGHT + this.toString(), cooldownRightclick);
	}

	public boolean hasCooldownLeftclick(Player p) {
		return infinityStone.hasCooldown(p, METADATA_KEY_COOLDOWN_LEFT + this.toString());
	}

	public boolean hasCooldownRightclick(Player p) {
		return infinityStone.hasCooldown(p, METADATA_KEY_COOLDOWN_RIGHT + this.toString());
	}

	public boolean canGetStone(Player p) {

		if (hasAnyInInv(p) || StoneUploader.getStoneContainer(p)[StoneUploader.Data.fromType(this).getId()] || new Gauntlet().hasInInv(p)) {
			return false;
		}

		return true;
	}

	public static StoneType getValue(String v) {

		for (StoneType type : StoneType.values()) {

			if (type.name().equalsIgnoreCase(v)) {
				return type;
			}
		}

		return null;
	}

	public static boolean hasAnyInHand(Player p) {

		try {
			ItemStack item = p.getInventory().getItemInMainHand();
			List<String> loreList = item.getItemMeta().getLore();

			for (StoneType type : StoneType.values()) {

				if (loreList.get(1).equals(Main.LORE_ID) && loreList.get(0).equals(type.infinityStone.getLore())) {
					return true;
				}
			}

			return false;
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean hasAnyInInv(Player p) {

		for (ItemStack item : p.getInventory().getContents()) {

			try {
				List<String> loreList = item.getItemMeta().getLore();

				for (StoneType type : StoneType.values()) {

					if (loreList.get(1).equals(Main.LORE_ID) && loreList.get(0).equals(type.infinityStone.getLore())) {
						return true;
					}
				}
			} catch (Exception e) {
				continue;
			}
		}

		return false;
	}

	public static StoneType whichStone(ItemStack item) {

		try {
			List<String> loreList = item.getItemMeta().getLore();

			for (StoneType type : StoneType.values()) {

				if (loreList.get(1).equals(Main.LORE_ID) && loreList.get(0).equals(type.infinityStone.getLore()) && item.getType() == type.m) {
					return type;
				}
			}

			return null;
		} catch (Exception e) {
			return null;
		}
	}

	public ItemStack getItem() {
		return infinityStone.getItem();
	}

	private class InfinityStone extends CustomItem {

		public InfinityStone(Material m, String name) {
			super(m, name, "One of the six powerful Infinity Stones");
		}

	}
}
