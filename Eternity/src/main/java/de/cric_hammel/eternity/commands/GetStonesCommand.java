package de.cric_hammel.eternity.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.StringUtil;

import de.cric_hammel.eternity.Main;

public class GetStonesCommand implements CommandExecutor, TabExecutor{

	private static final String[] ARGUMENTS = {"power", "space", "reality", "soul", "mind", "time"};
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (player.hasPermission("eternity.getstones")) {
				if (args.length == 1) {
					Material type;
					String name;
					
					switch (args[0]) {
					case "power":
						type = Material.PURPLE_DYE;
						name = ChatColor.LIGHT_PURPLE + "Power Stone";
						break;
					case "space":
						type = Material.BLUE_DYE;
						name = ChatColor.BLUE + "Space Stone";
						break;
					case "reality":
						type = Material.RED_DYE;
						name = ChatColor.DARK_RED + "Reality Stone";
						break;
					case "soul":
						type = Material.ORANGE_DYE;
						name = ChatColor.RED + "Soul Stone";
						break;
					case "mind":
						type = Material.YELLOW_DYE;
						name = ChatColor.YELLOW + "Mind Stone";
						break;
					case "time":
						type = Material.LIME_DYE;
						name = ChatColor.GREEN + "Time Stone";
						break;
					default:
						player.sendMessage(Main.defaultMessages.get("wrongArgs") + "/getstones [power|space|reality|soul|mind|time]");
						return false;
					}
					
					ItemStack stone = new ItemStack(type);
					ItemMeta stoneMeta = stone.getItemMeta();
					stoneMeta.setDisplayName(name);
					ArrayList<String> lore = new ArrayList<String>();
					lore.add(Main.STONES_LORE);
					stoneMeta.setLore(lore);
					stone.setItemMeta(stoneMeta);
					stone.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1);
					player.getInventory().addItem(stone);
				} else {
					player.sendMessage(Main.defaultMessages.get("wrongArgs") + "/getstones [power|space|reality|soul|mind|time]");
				}
			} else {
				player.sendMessage(Main.defaultMessages.get("noPermission"));
			}
		} else {
			sender.sendMessage(Main.defaultMessages.get("notPlayer"));
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 1) {
			ArrayList<String> completions = new ArrayList<String>();
			StringUtil.copyPartialMatches(args[0], Arrays.asList(ARGUMENTS), completions);
			return completions;
		}
		return null;
	}
	
}
