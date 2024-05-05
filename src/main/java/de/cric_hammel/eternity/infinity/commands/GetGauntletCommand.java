package de.cric_hammel.eternity.infinity.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.cric_hammel.eternity.Main;
import de.cric_hammel.eternity.infinity.items.gauntlet.Gauntlet;

public class GetGauntletCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (!(sender instanceof Player)) {
			sender.sendMessage(Main.defaultMessages.get("notPlayer"));
			return false;
		}

		Player p = (Player) sender;

		if (!p.hasPermission("eternity.getgauntlet")) {
			p.sendMessage(Main.defaultMessages.get("noPermission"));
			return false;
		}

		if (args.length != 0) {
			p.sendMessage(Main.defaultMessages.get("wrongArgs") + "/getgauntlet");
			return false;
		}

		ItemStack gauntlet = new Gauntlet().getItem();
		p.getInventory().addItem(gauntlet);
		return true;
	}

}
