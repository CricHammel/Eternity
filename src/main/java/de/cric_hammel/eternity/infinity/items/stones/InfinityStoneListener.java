package de.cric_hammel.eternity.infinity.items.stones;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InfinityStoneListener implements Listener {

	@EventHandler
	public void useInfinityStone(PlayerInteractEvent event) {
		Player p = event.getPlayer();

		if (StoneType.hasAnyInHand(p)) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void useInfinityStoneEntity(PlayerInteractEntityEvent event) {
		Player p = event.getPlayer();

		if (StoneType.hasAnyInHand(p)) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void moveInfinityStone(InventoryClickEvent event) {
		Inventory inv = event.getClickedInventory();

		if (StoneUploader.invs.contains(inv)) {
			return;
		}

		if (inv == event.getWhoClicked().getInventory()) {

			if (event.getClick().isShiftClick()) {
				ItemStack current = event.getCurrentItem();

				if (StoneType.whichStone(current) != null) {
					event.setCancelled(true);
				}
			}
		} else {
			ItemStack cursor = event.getCursor();

			if (StoneType.whichStone(cursor) != null) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void dragInfinityStone(InventoryDragEvent event) {
		ItemStack drag = event.getOldCursor();
		Inventory inv = event.getInventory();

		if (StoneType.whichStone(drag) == null || StoneUploader.invs.contains(inv)) {
			return;
		}

		int invSize = inv.getSize();

		for (int i : event.getRawSlots()) {

			if (i < invSize) {
				event.setCancelled(true);
				break;
			}
		}
	}

	@EventHandler
	public void dropInfinityStone(PlayerDropItemEvent event) {

		if (StoneType.whichStone(event.getItemDrop().getItemStack()) != null) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void pickupInfinityStone(InventoryPickupItemEvent event) {

		if (event.getInventory().getType() == InventoryType.HOPPER && StoneType.whichStone(event.getItem().getItemStack()) != null) {
			event.setCancelled(true);
		}
	}
}
