package de.cric_hammel.eternity.infinity.dungeons;

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
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.loot.LootTable;

import de.cric_hammel.eternity.infinity.util.BlockParser;
import de.cric_hammel.eternity.infinity.util.BlockParser.BlockAction;

public abstract class Dungeon implements Listener {
	
	protected final BlockParser parser;
	private final Material mineable;
	
	private static final Map<Player, World> worlds = new HashMap<Player, World>();
	private static final Map<Player, Location> lastLoc = new HashMap<Player, Location>();
	
	public Dungeon(String fileName, LootTable loot, Material mineable) {
		parser = new BlockParser(fileName);
		parser.addAction(Material.SPAWNER, (loc, data) -> loc.getWorld().setSpawnLocation(loc));
		parser.addAction(Material.CHEST, new BlockAction() {
			
			@Override
			public void execute(Location loc, BlockData data) {
				Block b = loc.getBlock();
				b.setBlockData(data);
				Chest c = (Chest) b.getState();
				c.setLootTable(loot);
				c.update();
			}
		});
		
		this.mineable = mineable;
	}
	
	public World create(Player p) {
		
		if (worlds.containsKey(p)) {
			return null;
		}
		
		WorldCreator wc = new WorldCreator("Dungeon of " + p.getName());
		wc.environment(Environment.NORMAL);
		wc.generateStructures(false);
		wc.type(WorldType.FLAT);
		wc.generatorSettings("{\"structures\": {\"structures\": {}}, \"layers\": [], \"biome\":\"the_void\"}");
		World w = wc.createWorld();
		w.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
		w.setGameRule(GameRule.DO_MOB_SPAWNING, false);
		w.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
		parser.parse(w);
		worlds.put(p, w);
		lastLoc.put(p, p.getLocation());
		p.teleport(w.getSpawnLocation());
		return w;
	}
	
	public void delete(Player p) {
		
		if (!worlds.containsKey(p)) {
			return;
		}
		
		World w = worlds.get(p);
		
		if (w.getPlayers().contains(p)) {
			p.teleport(lastLoc.get(p));
			lastLoc.remove(p);
		}
		
		worlds.remove(p);
		Bukkit.unloadWorld(w, true);
		removeDirectory(w.getWorldFolder());
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
	
	public static boolean isInDungeon(Player p) {
		return worlds.containsKey(p);
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player p = event.getPlayer();
		
		if (isInDungeon(p)) {
			delete(p);
		}
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		
		if (!(event.getEntity() instanceof Player)) {
			return;
		}
		
		Player p = (Player) event.getEntity();
		
		if (!isInDungeon(p) || event.getFinalDamage() < p.getHealth()) {
			return;
		}
		
		event.setCancelled(true);
		p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
		delete(p);
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Player p = event.getPlayer();
		Material m = event.getBlock().getType();
		
		if (isInDungeon(p) && m != mineable) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		
		if (isInDungeon(event.getPlayer())) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerBucketFill(PlayerBucketFillEvent event) {
		
		if (isInDungeon(event.getPlayer())) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
		
		if (isInDungeon(event.getPlayer())) {
			event.setCancelled(true);
		}
	}
}
