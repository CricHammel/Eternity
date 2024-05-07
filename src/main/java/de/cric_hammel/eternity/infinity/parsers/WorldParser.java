package de.cric_hammel.eternity.infinity.parsers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import de.cric_hammel.eternity.Main;

public class WorldParser extends BlockParser {
	
    private boolean isParsing = false;
    private boolean interrupt = false;

    private final Map<Material, BlockAction> actions = new HashMap<>();

    public WorldParser(String fileName, World world) {
    	super(fileName, world);
    }

    @Override
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
            	if (interrupt) {
            		isParsing = false;
            		interrupt = false;
            		logger.info("[Eternity] Interrupted parsing for '" + world.getName() + "'");
            		cancel();
            		return;
            	}
            	
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
    
    public void addAction(Material m, BlockAction action) {
        actions.put(m, action);
    }

    public void addAction(Tag<Material> t, BlockAction action) {
        for (Material m : t.getValues()) {
            actions.put(m, action);
        }
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
    
    public void interrupt() {
    	if (isParsing) {
    		interrupt = true;
    	}
    }

    public interface BlockAction {
        void execute(Location loc, BlockData data);
    }
}
