package de.cric_hammel.eternity;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import de.cric_hammel.eternity.infinity.commands.GetGauntletCommand;
import de.cric_hammel.eternity.infinity.commands.GetKreeArmorCommand;
import de.cric_hammel.eternity.infinity.commands.GetStonesCommand;
import de.cric_hammel.eternity.infinity.commands.SpawnKreeCommand;
import de.cric_hammel.eternity.infinity.items.gauntlet.Gauntlet;
import de.cric_hammel.eternity.infinity.items.kree.KreeArmor;
import de.cric_hammel.eternity.infinity.items.stones.InfinityStoneListener;
import de.cric_hammel.eternity.infinity.items.stones.MindStone;
import de.cric_hammel.eternity.infinity.items.stones.PowerStone;
import de.cric_hammel.eternity.infinity.items.stones.RealityStone;
import de.cric_hammel.eternity.infinity.items.stones.SoulStone;
import de.cric_hammel.eternity.infinity.items.stones.SpaceStone;
import de.cric_hammel.eternity.infinity.items.stones.TimeStone;
import de.cric_hammel.eternity.infinity.mobs.kree.KreeGeneral;
import de.cric_hammel.eternity.infinity.mobs.kree.KreeSoldier;

public class Main extends JavaPlugin {

	private static Main plugin;
	public static Map<String, String> defaultMessages = new HashMap<String, String>();
	public static final String LORE_ID = ChatColor.MAGIC + "eternity";

	public void onEnable() {
		plugin = this;

		defaultMessages.put("notPlayer", "This command can only be executed by players!");
		defaultMessages.put("noPermission", "You don't have the permission to execute this command!");
		defaultMessages.put("wrongArgs", "Wrong arguments! Correct usage: ");

		getCommand("getgauntlet").setExecutor(new GetGauntletCommand());
		getCommand("getstones").setExecutor(new GetStonesCommand());
		getCommand("getstones").setTabCompleter(new GetStonesCommand());
		getCommand("getkreearmor").setExecutor(new GetKreeArmorCommand());
		getCommand("spawnkree").setExecutor(new SpawnKreeCommand());

		PluginManager pluginManager = Bukkit.getPluginManager();
		pluginManager.registerEvents(new Gauntlet(), plugin);
		pluginManager.registerEvents(new InfinityStoneListener(), plugin);
		pluginManager.registerEvents(new PowerStone(), plugin);
		pluginManager.registerEvents(new SpaceStone(), plugin);
		pluginManager.registerEvents(new RealityStone(), plugin);
		pluginManager.registerEvents(new SoulStone(), plugin);
		pluginManager.registerEvents(new MindStone(), plugin);
		pluginManager.registerEvents(new TimeStone(), plugin);
		pluginManager.registerEvents(new KreeArmor(), plugin);
		pluginManager.registerEvents(new KreeSoldier(), plugin);
		pluginManager.registerEvents(new KreeGeneral(), plugin);

	}

	public static Main getPlugin() {
		return plugin;
	}
}
