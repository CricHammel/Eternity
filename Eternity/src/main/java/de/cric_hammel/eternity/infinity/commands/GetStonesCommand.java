package de.cric_hammel.eternity.infinity.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;

import de.cric_hammel.eternity.Main;
import de.cric_hammel.eternity.infinity.items.stones.StoneType;

public class GetStonesCommand implements TabExecutor {

	private static final String[] ARGUMENTS = { "power", "space", "reality", "soul", "mind", "time" };

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (!(sender instanceof Player)) {
			sender.sendMessage(Main.defaultMessages.get("notPlayer"));
			return false;
		}

		Player p = (Player) sender;

		if (!p.hasPermission("eternity.getstones")) {
			p.sendMessage(Main.defaultMessages.get("noPermission"));
			return false;
		}

		if (args.length != 1) {
			p.sendMessage(Main.defaultMessages.get("wrongArgs") + "/getstones [power|space|reality|soul|mind|time]");
			return false;
		}

		StoneType type = StoneType.getValue(args[0]);

		if (type == null) {
			p.sendMessage(Main.defaultMessages.get("wrongArgs") + "/getstones [power|space|reality|soul|mind|time]");
			return false;
		}

		ItemStack stone = type.getItem();
		p.getInventory().addItem(stone);
		return true;
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
