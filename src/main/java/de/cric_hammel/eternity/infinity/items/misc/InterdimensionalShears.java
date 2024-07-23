package de.cric_hammel.eternity.infinity.items.misc;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import de.cric_hammel.eternity.infinity.items.CustomItem;
import de.cric_hammel.eternity.infinity.util.ActionUtils;
import de.cric_hammel.eternity.infinity.util.SoundUtils;
import de.cric_hammel.eternity.infinity.worlds.Lobby;

public class InterdimensionalShears extends CustomItem {

	private static InterdimensionalShears instance;

	public static InterdimensionalShears getInstance() {
		if (null == instance) {
			synchronized (InterdimensionalShears.class) {
				if (null == instance) {
					instance = new InterdimensionalShears();
				}
			}
		}
		
		return instance;
	}
	
	private InterdimensionalShears() {
		super(Material.SHEARS, ChatColor.BLUE + "Interdimensional Shears", "Cut a wormhole into spacetime");
	}

	public static class Listeners implements Listener {

		@EventHandler
		public void onInteract(PlayerInteractEvent event) {
			Player p = event.getPlayer();

			if (!(new InterdimensionalShears()).hasInHand(p) || !ActionUtils.isRightclick(event.getAction())) {
				return;
			}

			ItemStack shears = event.getItem();
			Damageable shearsMeta = (Damageable) shears.getItemMeta();
			int damage = shearsMeta.getDamage() + 17;

			if (damage >= shears.getType().getMaxDurability()) {
				p.getInventory().setItemInMainHand(null);
				SoundUtils.play(p, Sound.ENTITY_ITEM_BREAK, 1f, 1f);
			} else {
				shearsMeta.setDamage(damage);
				shears.setItemMeta(shearsMeta);
				SoundUtils.play(p, Sound.ENTITY_SHULKER_TELEPORT, 1f, 0.5f);
			}

			Lobby.getInstance().teleport(p);
		}
	}
}
