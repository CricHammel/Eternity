package de.cric_hammel.eternity.infinity.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.io.BukkitObjectInputStream;

import de.cric_hammel.eternity.Main;

public class BlockParser {

    private String fileName;
    private World world;

    private boolean isParsing = false;

    private final Map<Material, BlockAction> actions = new HashMap<>();

    public BlockParser(String fileName, World world) {
        this.fileName = fileName;
        this.world = world;
    }

    public void parse() {
        if (isParsing) {
            return;
        }

        isParsing = true;
        Logger logger = Bukkit.getLogger();
        logger.info("[Eternity] Started parsing '" + fileName + "' into '" + world.getName() + "'");
        List<Map<String, Object>> blocks = readData();

        if (blocks == null) {
        	logger.info("[Eternity] Something went wrong while parsing '" + fileName + "' into '" + world.getName() + "'");
        	isParsing = false;
        	return;
        }
        
        int totalBlocks = blocks.size();
        int batchSize = 500;
        int batches = (totalBlocks + batchSize - 1) / batchSize;

        new BukkitRunnable() {

        	int processedBlocks = 0;

            @Override
            public void run() {
                int currentBatch = processedBlocks;
                processedBlocks++;

                for (int i = currentBatch * batchSize; i < Math.min((currentBatch + 1) * batchSize, totalBlocks); i++) {
                    Map<String, Object> block = blocks.get(i);
                    Location loc = new Location(world, Double.parseDouble(block.get("x").toString()),
                            Double.parseDouble(block.get("y").toString()),
                            Double.parseDouble(block.get("z").toString()));
                    BlockData data = Bukkit.getServer().createBlockData(block.get("data").toString());
                    Material m = data.getMaterial();
                    BlockAction action = actions.get(m);

                    if (action != null) {
                        action.execute(loc.add(0.5, 0, 0.5), data);
                        continue;
                    }

                    loc.getBlock().setBlockData(data);
                }

                if (processedBlocks >= batches) {
                    isParsing = false;
                    logger.info("[Eternity] Finished parsing, '" + world.getName() + "' is ready!");
                    cancel();
                }
            }
        }.runTaskTimer(Main.getPlugin(), 1, 1);
    }
    
    public Set<FallingBlock> parseAsFallingBlocks(Location startLoc) {
    	List<Map<String, Object>> blocks = readData();
    	Set<FallingBlock> fallingBlocks = new HashSet<FallingBlock>();
    	
    	for (Map<String, Object> block : blocks) {
    		Location loc = new Location(world, Double.parseDouble(block.get("x").toString()),
                    Double.parseDouble(block.get("y").toString()),
                    Double.parseDouble(block.get("z").toString()));
            BlockData data = Bukkit.getServer().createBlockData(block.get("data").toString());
            Location spawnLoc = startLoc.clone().add(loc);
            FallingBlock fallingBlock = world.spawnFallingBlock(spawnLoc, data);
            fallingBlock.setCancelDrop(true);
            fallingBlock.setHurtEntities(false);
            fallingBlocks.add(fallingBlock);
		}
    	
    	return fallingBlocks;
    }

    @SuppressWarnings("unchecked")
	private List<Map<String, Object>> readData() {
    	try {
	    	File file = new File(Main.getPlugin().getDataFolder(), fileName);
	        List<Map<String, Object>> blocks;
	        FileInputStream fis = new FileInputStream(file);
	        ObjectInputStream ois = new ObjectInputStream(fis);
	        BukkitObjectInputStream bois = new BukkitObjectInputStream(ois);
	        blocks = (List<Map<String, Object>>) bois.readObject();
	        bois.close();
	        return blocks;
    	} catch (Exception e) {
    		e.printStackTrace();
    		return null;
    	}
    }
    
    public void addAction(Material m, BlockAction action) {
        actions.put(m, action);
    }

    public void addAction(Tag<Material> t, BlockAction action) {
        for (Material m : t.getValues()) {
            actions.put(m, action);
        }
    }

    public void setWorld(World world) {
    	this.world = world;
    }

    public void tryTeleport(Player p, float yaw) {
    	new BukkitRunnable() {

    		boolean firstCheck = true;

			@Override
			public void run() {
				if (!isParsing) {
					Location loc = world.getSpawnLocation();
			    	loc.setYaw(yaw);
		    		p.teleport(loc);
		    		cancel();
		    		return;
		    	}

				if (firstCheck) {
					p.sendMessage(ChatColor.GREEN + "The world is currently being generated, you will be teleported soon!");
					firstCheck = false;
				}
			}
		}.runTaskTimer(Main.getPlugin(), 0, 1);
    }

    public interface BlockAction {
        void execute(Location loc, BlockData data);
    }
}
