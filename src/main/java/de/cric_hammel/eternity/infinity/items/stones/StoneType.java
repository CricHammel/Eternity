package de.cric_hammel.eternity.infinity.items.stones;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.cric_hammel.eternity.Main;
import de.cric_hammel.eternity.infinity.items.CustomItem;
import de.cric_hammel.eternity.infinity.items.gauntlet.Gauntlet;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public enum StoneType {

	POWER(30, 1, Material.PURPLE_DYE, Component.text("Power Stone", NamedTextColor.LIGHT_PURPLE)),
	SPACE(60, 2, Material.BLUE_DYE, Component.text("Space Stone", NamedTextColor.BLUE)),
	REALITY(0, 0, Material.RED_DYE, Component.text("Reality Stone", NamedTextColor.DARK_RED)),
	SOUL(3, 60, Material.ORANGE_DYE, Component.text("Soul Stone", NamedTextColor.RED)),
	MIND(30, 1, Material.YELLOW_DYE, Component.text("Mind Stone", NamedTextColor.YELLOW)),
	TIME(2, 10, Material.LIME_DYE, Component.text("Time Stone", NamedTextColor.GREEN));

	private final int cooldownLeftclick;
	private final int cooldownRightclick;
	public final Material m;
	private final InfinityStone infinityStone;

	private StoneType(int cooldownLeftClick, int cooldownRightClick, Material m, Component itemName) {
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
		infinityStone.applyCooldown(p, cooldownLeftclick);
	}

	public void applyCooldownRightclick(Player p) {
		infinityStone.applyCooldown(p, cooldownRightclick);
	}

	public boolean hasCooldown(Player p) {
		return infinityStone.hasCooldown(p);
	}

	public boolean canGetStone(Player p) {

		if (hasAnyInInv(p) || StoneUploader.getStoneContainer(p)[StoneUploader.Data.fromType(this).getId()] || Gauntlet.getInstance().hasInInv(p)) {
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
		ItemStack item = p.getInventory().getItemInMainHand();

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

		for (StoneType type : StoneType.values()) {

			if (loreList.get(1).equals(Main.LORE_ID) && loreList.get(0).equals(type.infinityStone.getLore())) {
				return true;
			}
		}

		return false;
	}

	public static boolean hasAnyInInv(Player p) {

		for (ItemStack item : p.getInventory().getContents()) {

			if (item == null || !item.hasItemMeta()) {
				continue;
			}

			ItemMeta meta = item.getItemMeta();

			if (!meta.hasLore()) {
				continue;
			}

			List<Component> loreList = meta.lore();

			if (loreList.size() < 2) {
				continue;
			}

			for (StoneType type : StoneType.values()) {

				if (loreList.get(1).equals(Main.LORE_ID) && loreList.get(0).equals(type.infinityStone.getLore())) {
					return true;
				}
			}
		}

		return false;
	}

	public static StoneType whichStone(ItemStack item) {

		if (item == null || !item.hasItemMeta()) {
			return null;
		}

		ItemMeta meta = item.getItemMeta();

		if (!meta.hasLore()) {
			return null;
		}

		List<Component> loreList = meta.lore();

		if (loreList.size() < 2) {
			return null;
		}

		for (StoneType type : StoneType.values()) {

			if (loreList.get(1).equals(Main.LORE_ID) && loreList.get(0).equals(type.infinityStone.getLore()) && item.getType() == type.m) {
				return type;
			}
		}

		return null;
	}

	public ItemStack getItem() {
		return infinityStone.getItem();
	}

	private class InfinityStone extends CustomItem {

		public InfinityStone(Material m, Component name) {
			super(m, name, Component.text("One of the six powerful Infinity Stones"));
		}

	}
}
