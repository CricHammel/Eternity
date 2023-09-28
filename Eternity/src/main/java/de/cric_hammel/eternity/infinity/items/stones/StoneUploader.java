package de.cric_hammel.eternity.infinity.items.stones;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.scheduler.BukkitRunnable;

import de.cric_hammel.eternity.Main;
import de.cric_hammel.eternity.infinity.items.CustomItem;
import de.cric_hammel.eternity.infinity.util.ActionUtils;
import de.cric_hammel.eternity.infinity.util.BooleanArrayDataType;

public class StoneUploader extends CustomItem implements Listener {

	public static final Set<Inventory> invs = new HashSet<Inventory>();
	private static final NamespacedKey key = new NamespacedKey(Main.getPlugin(), "eternity_upload");
	
	public StoneUploader() {
		super(Material.BEACON, ChatColor.GOLD + "Uploader", "Lets you upload the Infinity Stones!");
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		Action a = event.getAction();
		
		if (!super.hasInHand(p) || !ActionUtils.isRightclick(a)) {
			return;
		}
		
		p.openInventory(buildInv(p));
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		HumanEntity h = event.getWhoClicked();
		
		if (!(h instanceof Player)) {
			return;
		}
		
		Player p = (Player) h;
		Inventory inv = event.getClickedInventory();
		
		if (!invs.contains(inv)) {
			return;
		}
		
		ItemStack current = event.getCurrentItem();
		ItemStack cursor = event.getCursor();
		
		if (current.getType() == Material.GRAY_STAINED_GLASS_PANE || !event.getClick().isLeftClick() || event.getClick().isShiftClick()) {
			event.setCancelled(true);
			return;
		}
		
		StoneType currentType = StoneType.whichStone(current);
		StoneType cursorType = StoneType.whichStone(cursor);
		int clickedSlot = event.getSlot();
		
		if (currentType != null && cursor != null && cursor.getType() == Material.AIR && !StoneType.hasAnyInInv(p)) {
			Data data = Data.fromType(currentType);
			setStoneContainer(p, data.id, false);
			
			new BukkitRunnable() {
				
				@Override
				public void run() {
					inv.setItem(clickedSlot, new ItemStack(data.pane));
					p.updateInventory();
				}
			}.runTask(Main.getPlugin());
			
		} else if (cursorType != null) {
			Data data = Data.fromType(cursorType);
			
			if (current.getType() != data.pane) {
				event.setCancelled(true);
				return;
			}
			
			setStoneContainer(p, data.id, true);
			
			new BukkitRunnable() {
				
				@Override
				public void run() {
					p.setItemOnCursor(null);
					p.updateInventory();
				}
			}.runTask(Main.getPlugin());
		} else {
			event.setCancelled(true);
		}
	}
	
	private Inventory buildInv(Player p) {
		Inventory inv = Bukkit.createInventory(null, 4*9, super.getName());
		boolean[] bools = getStoneContainer(p);
		
		for (Data data : Data.values()) {
			
			if (bools[data.id]) {
				inv.setItem(data.slot, data.type.getItem());
			} else {
				ItemStack pane = new ItemStack(data.pane);
				ItemMeta paneMeta = pane.getItemMeta();
				paneMeta.setDisplayName(" ");
				pane.setItemMeta(paneMeta);
				inv.setItem(data.slot, pane);
			}
		}
		
		for (int i = 0; i < inv.getSize(); i++) {
			
			if (inv.getItem(i) == null) {
				ItemStack pane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
				ItemMeta paneMeta = pane.getItemMeta();
				paneMeta.setDisplayName(" ");
				pane.setItemMeta(paneMeta);
				inv.setItem(i, pane);
			}
		}
		
		invs.add(inv);
		return inv;
	}
	
	public static boolean[] getStoneContainer(Player p) {
		PersistentDataContainer c = p.getPersistentDataContainer();
		
		if (!c.has(key, new BooleanArrayDataType())) {
			c.set(key, new BooleanArrayDataType(), new boolean[6]);
		}
		
		return c.get(key, new BooleanArrayDataType());
	}
	
	private void setStoneContainer(Player p, int id, boolean bool) {
		boolean[] bools = getStoneContainer(p);
		bools[id] = bool;
		p.getPersistentDataContainer().set(key, new BooleanArrayDataType(), bools);
	}

	public enum Data {
		
		POWER(0, 1*9+3, Material.PURPLE_STAINED_GLASS_PANE, StoneType.POWER),
		SPACE(1, 1*9+4, Material.BLUE_STAINED_GLASS_PANE, StoneType.SPACE),
		REALITY(2, 1*9+5, Material.RED_STAINED_GLASS_PANE, StoneType.REALITY),
		SOUL(3, 2*9+3, Material.ORANGE_STAINED_GLASS_PANE, StoneType.SOUL),
		MIND(4, 2*9+4, Material.YELLOW_STAINED_GLASS_PANE, StoneType.MIND),
		TIME(5, 2*9+5, Material.LIME_STAINED_GLASS_PANE, StoneType.TIME);
		
		private final int id;
		private final int slot;
		private final Material pane;
		private final StoneType type;
		
		private Data(int id, int slot, Material pane, StoneType type) {
			this.id = id;
			this.slot = slot;
			this.pane = pane;
			this.type = type;
		}

		public int getId() {
			return id;
		}

		public int getSlot() {
			return slot;
		}

		public Material getPane() {
			return pane;
		}
		
		public StoneType getType() {
			return type;
		}
		
		public static Data fromType(StoneType type) {
			return valueOf(type.name());
		}
	}
}
