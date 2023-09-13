package de.cric_hammel.eternity.infinity.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.cric_hammel.eternity.Main;
import de.cric_hammel.eternity.infinity.items.gauntlet.Gauntlet;

public class GetGauntletCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (p.hasPermission("eternity.getgauntlet")) {
				if (args.length == 0) {
					ItemStack gauntlet = Gauntlet.getItem();
					p.getInventory().addItem(gauntlet);
					return true;
				} else {
					p.sendMessage(
							Main.defaultMessages.get("wrongArgs") + "/getgauntlet");
				}
			} else {
				p.sendMessage(Main.defaultMessages.get("noPermission"));
			}
		} else {
			sender.sendMessage(Main.defaultMessages.get("notPlayer"));
		}
		return false;
	}

}
