package de.cric_hammel.eternity.infinity.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.cric_hammel.eternity.Main;
import de.cric_hammel.eternity.infinity.items.kree.KreeArmor;

public class GetKreeArmorCommand implements CommandExecutor {

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

		KreeArmor armor = KreeArmor.getInstance();
		ItemStack[] tierOne = armor.getTier(1);
		p.getInventory().addItem(tierOne);
		ItemStack[] tierTwo = armor.getTier(2);
		p.getInventory().addItem(tierTwo);
		ItemStack[] tierThree = armor.getTier(3);
		p.getInventory().addItem(tierThree);
		return true;
	}
}
