package de.cric_hammel.eternity;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import de.cric_hammel.eternity.commands.GetStonesCommand;
import de.cric_hammel.eternity.stones.MindStone;
import de.cric_hammel.eternity.stones.PowerStone;
import de.cric_hammel.eternity.stones.RealityStone;
import de.cric_hammel.eternity.stones.SoulStone;
import de.cric_hammel.eternity.stones.SpaceStone;
import de.cric_hammel.eternity.stones.TimeStone;

public class Main extends JavaPlugin{

	private static Main plugin;
	public static Map<String, String> defaultMessages = new HashMap<String, String>();
	public static final String STONES_LORE = "One of the six powerful Infinity Stones";
	
	public void onEnable() {
		plugin = this;
		
		defaultMessages.put("notPlayer", "This command can only be executed by players!");
		defaultMessages.put("noPermission", "You don't have the permission to execute this command!");
		defaultMessages.put("wrongArgs", "Wrong arguments! Correct usage: ");
		
		getCommand("getstones").setExecutor(new GetStonesCommand());
		getCommand("getstones").setTabCompleter(new GetStonesCommand());
		
		PluginManager pluginManager = Bukkit.getPluginManager();
		pluginManager.registerEvents(new PowerStone(), plugin);
		pluginManager.registerEvents(new SpaceStone(), plugin);
		pluginManager.registerEvents(new RealityStone(), plugin);
		pluginManager.registerEvents(new SoulStone(), plugin);
		pluginManager.registerEvents(new MindStone(), plugin);
		pluginManager.registerEvents(new TimeStone(), plugin);
		
	}

	public static Main getPlugin() {
		return plugin;
	}
	
	public static boolean hasStoneInHand(Player p, int type) {
		ItemStack stone = p.getInventory().getItemInMainHand();
		Material m;
		switch (type) {
		case 0:
			m = Material.PURPLE_DYE;
			break;
		case 1:
			m = Material.BLUE_DYE;
			break;
		case 2:
			m = Material.RED_DYE;
			break;
		case 3:
			m = Material.ORANGE_DYE;
			break;
		case 4:
			m = Material.YELLOW_DYE;
			break;
		case 5:
			m = Material.LIME_DYE;
			break;
		default:
			return false;
		}
		if (stone != null && stone.getItemMeta().hasLore() && stone.getItemMeta().getLore().get(0).equals(Main.STONES_LORE) && stone.getType() == m) {
			return true;
		}
		return false;
	}
	
	public static boolean hasStoneInInv(Player p, int type) {
		for (ItemStack stone : p.getInventory().getContents()) {
			Material m;
			switch (type) {
			case 0:
				m = Material.PURPLE_DYE;
				break;
			case 1:
				m = Material.BLUE_DYE;
				break;
			case 2:
				m = Material.RED_DYE;
				break;
			case 3:
				m = Material.ORANGE_DYE;
				break;
			case 4:
				m = Material.YELLOW_DYE;
				break;
			case 5:
				m = Material.LIME_DYE;
				break;
			default:
				return false;
			}
			if (stone != null && stone.getItemMeta().hasLore() && stone.getItemMeta().getLore().get(0).equals(Main.STONES_LORE) && stone.getType() == m) {
				return true;
			}
		}
		return false;
	}
}
