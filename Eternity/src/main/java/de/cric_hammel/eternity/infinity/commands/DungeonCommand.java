package de.cric_hammel.eternity.infinity.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.cric_hammel.eternity.Main;
import de.cric_hammel.eternity.infinity.dungeons.Dungeon;
import de.cric_hammel.eternity.infinity.dungeons.PowerDungeon;

public class DungeonCommand implements CommandExecutor{

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

		if (args.length != 1) {
			p.sendMessage(Main.defaultMessages.get("wrongArgs") + "/dungeon [power]");
			return false;
		}

		if (args[0].equals("power")) {
			new PowerDungeon();
			if (Dungeon.isInDungeon(p)) {
				new PowerDungeon().delete(p);
			} else {
				new PowerDungeon().create(p);
			}
		}
		
		return true;
	}
}
