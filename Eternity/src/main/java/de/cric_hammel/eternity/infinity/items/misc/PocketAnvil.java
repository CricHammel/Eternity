package de.cric_hammel.eternity.infinity.items.misc;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import de.cric_hammel.eternity.infinity.items.CustomItem;
import de.cric_hammel.eternity.infinity.util.ActionUtils;
import de.cric_hammel.eternity.infinity.util.SoundUtils;

public class PocketAnvil extends CustomItem implements Listener {

	public PocketAnvil() {
		super(Material.ANVIL, ChatColor.RED + "Pocket Anvil", "Lets you open an anvil out of your pocket");
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		
		if (!super.hasInHand(p)) {
			return;
		} else if (!ActionUtils.isRightclick(event.getAction())) {
			event.setCancelled(true);
			return;
		}
		
		ItemStack anvil = event.getItem();
		anvil.setAmount(0);
		SoundUtils.play(p, Sound.BLOCK_ANVIL_DESTROY, 1, 1);
		
		p.openInventory(Bukkit.createInventory(p, InventoryType.ANVIL));
		event.setCancelled(true);
	}
}
