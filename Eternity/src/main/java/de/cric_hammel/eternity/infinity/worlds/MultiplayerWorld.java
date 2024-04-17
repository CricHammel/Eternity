package de.cric_hammel.eternity.infinity.worlds;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.scheduler.BukkitRunnable;

import de.cric_hammel.eternity.Main;
import de.cric_hammel.eternity.infinity.util.BlockParser;

public abstract class MultiplayerWorld implements Listener {

	private World world;
	private String name;

	protected final BlockParser parser;

	protected static final Map<Player, Location> lastLoc = new HashMap<>();

	public MultiplayerWorld(String fileName, String name) {
		this.name = "Eternity_" + name;
		world = Bukkit.getWorld(this.name);
		parser = new BlockParser(fileName, world);
		parser.addAction(Material.SPAWNER, (loc, data) -> loc.getWorld().setSpawnLocation(loc));
	}

	protected void create() {
		if (world == null) {
			WorldCreator wc = new WorldCreator(name);
			wc.environment(Environment.NORMAL);
			wc.generateStructures(false);
			wc.type(WorldType.FLAT);
			wc.generatorSettings("{\"structures\": {\"structures\": {}}, \"layers\": [], \"biome\":\"the_void\"}");
			world = wc.createWorld();
			world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
			world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
			world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
			world.setTime(18000);
			parser.setWorld(world);
		}
	}

	public void delete() {
		for (Player p : world.getPlayers()) {
			teleport(p);
		}

		Bukkit.unloadWorld(world, false);
		removeDirectory(world.getWorldFolder());
	}

	private boolean removeDirectory(File directoryToBeDeleted) {
	    File[] contents = directoryToBeDeleted.listFiles();

	    if (contents != null) {

	        for (File file : contents) {
	            removeDirectory(file);
	        }
	    }

	    return directoryToBeDeleted.delete();
	}

	public boolean isInWorld(Player p) {
		return p.getWorld() == world;
	}

	public boolean teleport(Player p) {
		if (isInWorld(p)) {
			if (lastLoc.containsKey(p)) {
				p.teleport(lastLoc.get(p));
			}
			return false;
		} else {
			lastLoc.put(p, p.getLocation());
			parser.tryTeleport(p, -90);
			return true;
		}
	}

	public World getWorld() {
		return world;
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		
		if (isInWorld(event.getPlayer())) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {

		if (isInWorld(event.getPlayer())) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onServerLoad(ServerLoadEvent event) {
		new BukkitRunnable() {

			@Override
			public void run() {
				parser.parse();
			}
		}.runTask(Main.getPlugin());
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player p = event.getPlayer();

		if (isInWorld(p)) {
			teleport(p);
		}
	}
}
