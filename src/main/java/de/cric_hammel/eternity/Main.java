package de.cric_hammel.eternity;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import de.cric_hammel.eternity.infinity.commands.BossfightCommand;
import de.cric_hammel.eternity.infinity.commands.GetGauntletCommand;
import de.cric_hammel.eternity.infinity.commands.GetInfiniCoinCommand;
import de.cric_hammel.eternity.infinity.commands.GetKreeArmorCommand;
import de.cric_hammel.eternity.infinity.commands.GetStoneUploaderCommand;
import de.cric_hammel.eternity.infinity.commands.GetStonesCommand;
import de.cric_hammel.eternity.infinity.commands.SpawnChitauriCommand;
import de.cric_hammel.eternity.infinity.commands.SpawnKreeCommand;
import de.cric_hammel.eternity.infinity.commands.SpawnOutriderCommand;
import de.cric_hammel.eternity.infinity.items.gauntlet.Gauntlet;
import de.cric_hammel.eternity.infinity.items.kree.KreeArmor;
import de.cric_hammel.eternity.infinity.items.kree.XylopMeat;
import de.cric_hammel.eternity.infinity.items.misc.InterdimensionalShears;
import de.cric_hammel.eternity.infinity.items.misc.PocketAnvil;
import de.cric_hammel.eternity.infinity.items.misc.teleport.TeleportCapsule;
import de.cric_hammel.eternity.infinity.items.misc.teleport.TeleportRailgun;
import de.cric_hammel.eternity.infinity.items.misc.teleport.TwelveTeraVoltBattery;
import de.cric_hammel.eternity.infinity.items.stones.InfinityStoneListener;
import de.cric_hammel.eternity.infinity.items.stones.MindStone;
import de.cric_hammel.eternity.infinity.items.stones.PowerStone;
import de.cric_hammel.eternity.infinity.items.stones.RealityStone;
import de.cric_hammel.eternity.infinity.items.stones.SoulStone;
import de.cric_hammel.eternity.infinity.items.stones.SpaceStone;
import de.cric_hammel.eternity.infinity.items.stones.StoneUploader;
import de.cric_hammel.eternity.infinity.items.stones.TimeStone;
import de.cric_hammel.eternity.infinity.items.thanos.ElectronCompressedChitauriDagger;
import de.cric_hammel.eternity.infinity.mobs.kree.KreeGeneral;
import de.cric_hammel.eternity.infinity.mobs.kree.KreeGuard;
import de.cric_hammel.eternity.infinity.mobs.kree.KreeSoldier;
import de.cric_hammel.eternity.infinity.mobs.kree.Xylop;
import de.cric_hammel.eternity.infinity.mobs.npc.DialogueNpc;
import de.cric_hammel.eternity.infinity.mobs.npc.ShopNpc;
import de.cric_hammel.eternity.infinity.mobs.thanos.ChitauriShip;
import de.cric_hammel.eternity.infinity.mobs.thanos.Outrider;
import de.cric_hammel.eternity.infinity.worlds.Lobby;
import de.cric_hammel.eternity.infinity.worlds.ThanosFight;
import de.cric_hammel.eternity.infinity.worlds.dungeons.Dungeon;
import de.cric_hammel.eternity.infinity.worlds.dungeons.DungeonFactory;

public class Main extends JavaPlugin {

	private static Main plugin;
	private static World mainWorld;
	public static Map<String, String> defaultMessages = new HashMap<>();
	public static final String LORE_ID = ChatColor.MAGIC + "eternity";

	@Override
	public void onEnable() {
		plugin = this;
		mainWorld = Bukkit.getWorld(getLevelname());

		if (mainWorld == null) {
			getLogger().severe("[Eternity] Could not find 'level-name' in server.properties. This may cause unexpected behaviour!");
		}

		// TODO: Put these into config
		defaultMessages.put("notPlayer", "This command can only be executed by players!");
		defaultMessages.put("noPermission", "You don't have the permission to execute this command!");
		defaultMessages.put("wrongArgs", "Wrong arguments! Correct usage: ");

		// Commands
		getCommand("getgauntlet").setExecutor(new GetGauntletCommand());
		getCommand("getstones").setExecutor(new GetStonesCommand());
		getCommand("getstones").setTabCompleter(new GetStonesCommand());
		getCommand("getkreearmor").setExecutor(new GetKreeArmorCommand());
		getCommand("spawnkree").setExecutor(new SpawnKreeCommand());
		getCommand("getstoneuploader").setExecutor(new GetStoneUploaderCommand());
		getCommand("getinfinicoin").setExecutor(new GetInfiniCoinCommand());
		getCommand("spawnchitauri").setExecutor(new SpawnChitauriCommand());
		getCommand("bossfight").setExecutor(new BossfightCommand());
		getCommand("spawnoutrider").setExecutor(new SpawnOutriderCommand());

		// Register events
		PluginManager pluginManager = Bukkit.getPluginManager();
		pluginManager.registerEvents(new InterdimensionalShears.Listeners(), plugin);
		pluginManager.registerEvents(new PocketAnvil.Listeners(), plugin);
		pluginManager.registerEvents(new TeleportRailgun.Listeners(), plugin);
		pluginManager.registerEvents(new TeleportCapsule.Listeners(), plugin);
		pluginManager.registerEvents(new TwelveTeraVoltBattery.Listeners(), plugin);
		pluginManager.registerEvents(new Gauntlet.Listeners(), plugin);
		pluginManager.registerEvents(new InfinityStoneListener(), plugin);
		pluginManager.registerEvents(new PowerStone(), plugin);
		pluginManager.registerEvents(new SpaceStone(), plugin);
		pluginManager.registerEvents(new RealityStone(), plugin);
		pluginManager.registerEvents(new SoulStone(), plugin);
		pluginManager.registerEvents(new MindStone(), plugin);
		pluginManager.registerEvents(new TimeStone(), plugin);
		pluginManager.registerEvents(new StoneUploader.Listeners(), plugin);
		
		pluginManager.registerEvents(new KreeArmor.Listeners(), plugin);
		pluginManager.registerEvents(new KreeSoldier.Listeners(), plugin);
		pluginManager.registerEvents(new KreeGeneral.Listeners(), plugin);
		pluginManager.registerEvents(new KreeGuard.Listeners(), plugin);
		pluginManager.registerEvents(new Xylop.Listeners(), plugin);
		pluginManager.registerEvents(new XylopMeat.Listeners(), plugin);
		pluginManager.registerEvents(new ShopNpc.Listeners(), plugin);
		pluginManager.registerEvents(new DialogueNpc.Listeners(), plugin);
		pluginManager.registerEvents(new ChitauriShip.Listeners(), plugin);
		pluginManager.registerEvents(new Outrider.Listeners(), plugin);

		pluginManager.registerEvents(Lobby.getInstance(), plugin);
		pluginManager.registerEvents(new Dungeon.Listeners(), plugin);

		pluginManager.registerEvents(new ThanosFight.Listeners(), plugin);
		pluginManager.registerEvents(new ElectronCompressedChitauriDagger.Listeners(), plugin);

		// Add recipes
		Set<NamespacedKey> recipes = new HashSet<>();
		
		NamespacedKey shearsKey = new NamespacedKey(plugin, "eternity_shears");
		ShapedRecipe shears = new ShapedRecipe(shearsKey, InterdimensionalShears.getInstance().getItem());
		shears.shape("CEP", "OST", "NBN");
		shears.setIngredient('C', Material.CHORUS_FRUIT);
		shears.setIngredient('E', Material.END_CRYSTAL);
		shears.setIngredient('P', Material.ENDER_PEARL);
		shears.setIngredient('O', Material.ECHO_SHARD);
		shears.setIngredient('S', Material.SHEARS);
		shears.setIngredient('T', Material.TOTEM_OF_UNDYING);
		shears.setIngredient('B', Material.BEACON);
		shears.setIngredient('N', Material.NETHERITE_INGOT);
		Bukkit.addRecipe(shears);
		recipes.add(shearsKey);
		
		Bukkit.getOnlinePlayers().forEach((p) -> {
			p.discoverRecipes(recipes);
		});
	}

	@Override
	public void onDisable() {
		// Close all dungeons
		DungeonFactory.closeAll();
		Lobby.getInstance().delete();
	}

	public static Main getPlugin() {
		return plugin;
	}

	public static String getDataPath() {
		return plugin.getDataFolder().getPath();
	}

	public static String getLootPath() {
		return getDataPath() + "/loot";
	}

	public static World getMainWorld() {
		return mainWorld;
	}

	private String getLevelname() {
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(new File("server.properties")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop.getProperty("level-name");
	}
}
