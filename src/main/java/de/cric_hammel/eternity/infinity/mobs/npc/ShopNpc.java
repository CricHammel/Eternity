package de.cric_hammel.eternity.infinity.mobs.npc;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import de.cric_hammel.eternity.Main;
import de.cric_hammel.eternity.infinity.items.misc.InfiniCoin;
import de.cric_hammel.eternity.infinity.util.SoundUtils;

public class ShopNpc {

	private static final String META_KEY_NPC = "eternity_shopnpc";
	private static final String META_KEY_PLAYER = "eternity_shopnpc_last";

	private String name;
	private List<ShopItem> items;
	public final Set<Inventory> invs = new HashSet<>();

	public ShopNpc(EntityType type, String name, Location loc, List<ShopItem> items) {
		this.name = name;
		this.items = items;
		LivingEntity e = (LivingEntity) loc.getWorld().spawnEntity(loc, type, false);
		e.setCustomName(name);
		e.setCustomNameVisible(true);
		e.setInvulnerable(true);
		e.setPersistent(true);
		e.setAI(false);
		e.setCanPickupItems(false);
		e.setCollidable(false);
		e.setRemoveWhenFarAway(false);
		e.setMetadata(META_KEY_NPC, new FixedMetadataValue(Main.getPlugin(), this));
	}

	public void openMenu(Player p) {
		int nrOfRows = 4;
		Inventory inv = Bukkit.createInventory(p, 9*nrOfRows, name + "'s Shop");
		ItemStack pane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
		ItemMeta paneMeta = pane.getItemMeta();
		paneMeta.setDisplayName(" ");
		pane.setItemMeta(paneMeta);

		for (int slot = 0; slot < inv.getSize(); slot++) {
			if (slot % 9 == 0 || slot % 9 == 8 || slot / 9 == 0 || slot / 9 - nrOfRows == -1) {
				inv.setItem(slot, pane);
			}
		}

		for (ShopItem shopItem : items) {
			int firstEmpty = inv.firstEmpty();

			if (firstEmpty != -1) {
				inv.setItem(firstEmpty, shopItem.getItem());
			}
		}

		invs.add(inv);
		p.openInventory(inv);
		p.setMetadata(META_KEY_PLAYER, new FixedMetadataValue(Main.getPlugin(), this));
	}
	
	public static boolean isNpc(LivingEntity e) {
		return e.hasMetadata(META_KEY_NPC);
	}

	public static class ShopItem {

		private String name;
		private Material m;
		private List<String> lore;
		private int price;
		private BuyAction action;

		public ShopItem(String name, Material m, List<String> lore , int price, BuyAction action) {
			this.name = name;
			this.m = m;
			this.lore = lore;
			lore.add("Price: " + price + " InfiniCoins");
			this.price = price;
			this.action = action;
		}

		public boolean buy(Player p) {
			PlayerInventory inv = p.getInventory();
			ItemStack coin = InfiniCoin.getInstance().getItem();

			if (!inv.containsAtLeast(coin, price) || !action.execute(p)) {
				return false;
			}

			coin.setAmount(price);

			inv.removeItem(coin);
			return true;
		}

		public ItemStack getItem() {
			ItemStack item = new ItemStack(m);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(name);
			meta.setLore(lore);
			item.setItemMeta(meta);
			item.addUnsafeEnchantment(Enchantment.INFINITY, 1);
			return item;
		}
	}

	public interface BuyAction {
		public boolean execute(Player p);
	}

	public static class Listeners implements Listener {

		@EventHandler
		public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {

			if (event.getHand() != EquipmentSlot.HAND) {
				return;
			}

			Entity e = event.getRightClicked();

			if (!e.hasMetadata(META_KEY_NPC)) {
				return;
			}

			ShopNpc npc = (ShopNpc) e.getMetadata(META_KEY_NPC).get(0).value();
			npc.openMenu(event.getPlayer());
			event.setCancelled(true);
		}

		@EventHandler
		public void onInventoryClick(InventoryClickEvent event) {
			HumanEntity h = event.getWhoClicked();

			if (!(h instanceof Player) || !h.hasMetadata(META_KEY_PLAYER)) {
				return;
			}

			Player p = (Player) h;
			ShopNpc npc = (ShopNpc) p.getMetadata(META_KEY_PLAYER).get(0).value();
			Inventory inv = event.getInventory();

			if (!npc.invs.contains(inv)) {
				return;
			}

			ItemStack current = event.getCurrentItem();
			ItemStack cursor = event.getCursor();
			ShopItem shopItem = findMatchingItem(current, npc);

			if (current != null && shopItem != null && cursor.getType() == Material.AIR) {
				if (shopItem.buy(p)) {
					SoundUtils.play(p, Sound.ENTITY_VILLAGER_YES, 1, 1);
				} else {
					SoundUtils.play(p, Sound.ENTITY_VILLAGER_NO, 1, 1);
				}
			}

			event.setCancelled(true);
		}

		private ShopItem findMatchingItem(ItemStack item, ShopNpc npc) {
			if (item == null) {
				return null;
			}

			for (ShopItem shopItem : npc.items) {
				if (item.isSimilar(shopItem.getItem())) {
					return shopItem;
				}
			}

			return null;
		}

		@EventHandler
		public void onInventoryClose(InventoryCloseEvent event) {
			HumanEntity h = event.getPlayer();

			if (!(h instanceof Player) || !h.hasMetadata(META_KEY_PLAYER)) {
				return;
			}

			Player p = (Player) h;
			ShopNpc npc = (ShopNpc) p.getMetadata(META_KEY_PLAYER).get(0).value();
			Inventory inv = event.getInventory();

			if (!npc.invs.contains(inv)) {
				return;
			}

			p.removeMetadata(META_KEY_PLAYER, Main.getPlugin());
		}
	}
}
