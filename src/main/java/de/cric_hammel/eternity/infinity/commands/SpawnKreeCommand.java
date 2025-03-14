package de.cric_hammel.eternity.infinity.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.cric_hammel.eternity.Main;
import de.cric_hammel.eternity.infinity.mobs.kree.KreeGeneral;
import de.cric_hammel.eternity.infinity.mobs.kree.KreeGuard;
import de.cric_hammel.eternity.infinity.mobs.kree.KreeSoldier;
import de.cric_hammel.eternity.infinity.mobs.kree.Xylop;

public class SpawnKreeCommand implements CommandExecutor {

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
			p.sendMessage(Main.defaultMessages.get("wrongArgs") + "/spawnkree [0|1|2|3]");
			return false;
		}

		if (args[0].equals("0")) {
			KreeSoldier.getInstance().spawn(p.getLocation());
		} else if (args[0].equals("1")) {
			KreeGeneral.getInstance().spawn(p.getLocation());
		} else if (args[0].equals("2")) {
			KreeGuard.getInstance().spawn(p.getLocation());
		} else if (args[0].equals("3")) {
			Xylop.getInstance().spawn(p.getLocation());
		}

		return true;
	}
}
