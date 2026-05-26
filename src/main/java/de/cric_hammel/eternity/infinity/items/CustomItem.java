package de.cric_hammel.eternity.infinity.items;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import net.kyori.adventure.text.Component;

import de.cric_hammel.eternity.Main;

public abstract class CustomItem {

	private final Material m;
	private final Component name;
	private final Component lore;
	private final ItemStack item;

	public CustomItem(Material m, Component name, Component lore) {
		this.m = m;
		this.name = name;
		this.lore = lore;
		item = createItem(m, name, lore);
	}

	private ItemStack createItem(Material m, Component name, Component lore) {
		ItemStack item = new ItemStack(m);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.displayName(name);
		itemMeta.lore(List.of(lore, Main.LORE_ID));
		item.setItemMeta(itemMeta);
		item.addUnsafeEnchantment(Enchantment.INFINITY, 1);
		return item;
	}

	public boolean hasInHand(Player p) {
		return isItem(p.getInventory().getItemInMainHand());
	}

	public boolean hasInHand(LivingEntity e) {
		return isItem(e.getEquipment().getItemInMainHand());
	}

	public boolean hasInInv(Player p) {

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

			if (loreList.get(1).equals(Main.LORE_ID) && loreList.get(0).equals(lore) && item.getType() == m) {
				return true;
			}
		}

		return false;
	}

	public boolean isItem(ItemStack item) {

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

		if (loreList.get(1).equals(Main.LORE_ID) && loreList.get(0).equals(lore) && item.getType() == m) {
			return true;
		}

		return false;
	}

	public void applyCooldown(Player p, String metaKey, int cooldownSec) {

		if (!p.hasMetadata(metaKey)) {
			p.setMetadata(metaKey,
					new FixedMetadataValue(Main.getPlugin(), System.currentTimeMillis() + cooldownSec * 1000));
			p.setCooldown(m, cooldownSec * 20);
		}
	}

	public boolean hasCooldown(Player p, String metaKey) {

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

	public Material getMaterial() {
		return m;
	}

	public Component getName() {
		return name;
	}

	public Component getLore() {
		return lore;
	}

	public ItemStack getItem() {
		return item.clone();
	}
}
