package de.cric_hammel.eternity.infinity.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.cric_hammel.eternity.Main;
import de.cric_hammel.eternity.infinity.worlds.ThanosFight;

public class BossfightCommand implements CommandExecutor {

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
			p.sendMessage(Main.defaultMessages.get("wrongArgs") + "/bossfight start|stop");
			return false;
		}

		ThanosFight f = ThanosFight.getInstance();

		if (args[0].equals("start")) {
			f.start();
		} else if (args[0].equals("stop")) {
			f.stop(true);
		} else if (args[0].equals("second")) {
			f.triggerSecondWave();
		}

		return true;
	}
}
