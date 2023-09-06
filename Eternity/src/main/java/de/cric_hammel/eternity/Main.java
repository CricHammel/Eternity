package de.cric_hammel.eternity;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import de.cric_hammel.eternity.commands.GetStonesCommand;
import de.cric_hammel.eternity.stones.InfinityStone;
import de.cric_hammel.eternity.stones.MindStone;
import de.cric_hammel.eternity.stones.PowerStone;
import de.cric_hammel.eternity.stones.RealityStone;
import de.cric_hammel.eternity.stones.SoulStone;
import de.cric_hammel.eternity.stones.SpaceStone;
import de.cric_hammel.eternity.stones.TimeStone;

public class Main extends JavaPlugin{

	private static Main plugin;
	public static Map<String, String> defaultMessages = new HashMap<String, String>();
	
	public void onEnable() {
		plugin = this;
		
		defaultMessages.put("notPlayer", "This command can only be executed by players!");
		defaultMessages.put("noPermission", "You don't have the permission to execute this command!");
		defaultMessages.put("wrongArgs", "Wrong arguments! Correct usage: ");
		
		getCommand("getstones").setExecutor(new GetStonesCommand());
		getCommand("getstones").setTabCompleter(new GetStonesCommand());
		
		PluginManager pluginManager = Bukkit.getPluginManager();
		pluginManager.registerEvents(new InfinityStone(), plugin);
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
}
