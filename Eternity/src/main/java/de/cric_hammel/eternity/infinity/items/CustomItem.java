package de.cric_hammel.eternity.infinity.items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import de.cric_hammel.eternity.Main;

public abstract class CustomItem {

	private final Material m;
	private final String name;
	private final String lore;
	private final ItemStack item;

	public CustomItem(Material m, String name, String lore) {
		this.m = m;
		this.name = name;
		this.lore = lore;
		item = createItem(m, name, lore);
	}

	private ItemStack createItem(Material m, String name, String lore) {
		ItemStack item = new ItemStack(m);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(name);
		ArrayList<String> loreList = new ArrayList<>();
		loreList.add(lore);
		loreList.add(Main.LORE_ID);
		itemMeta.setLore(loreList);
		item.setItemMeta(itemMeta);
		item.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1);
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

			try {
				List<String> loreList = item.getItemMeta().getLore();

				if (loreList.get(1).equals(Main.LORE_ID) && loreList.get(0).equals(lore) && item.getType() == m) {
					return true;
				}

			} catch (Exception e) {
				continue;
			}
		}

		return false;
	}

	public boolean isItem(ItemStack item) {
		try {
			List<String> loreList = item.getItemMeta().getLore();

			if (loreList.get(1).equals(Main.LORE_ID) && loreList.get(0).equals(lore) && item.getType() == m) {
				return true;
			}

			return false;

		} catch (Exception e) {
			return false;
		}
	}

	public void applyCooldown(Player p, String metaKey, int cooldown) {

		if (!p.hasMetadata(metaKey)) {
			p.setMetadata(metaKey,
					new FixedMetadataValue(Main.getPlugin(), System.currentTimeMillis() + cooldown * 1000));
			p.setCooldown(m, cooldown * 20);
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


	public String getName() {
		return name;
	}

	public String getLore() {
		return lore;
	}

	public ItemStack getItem() {
		return item.clone();
	}
}
