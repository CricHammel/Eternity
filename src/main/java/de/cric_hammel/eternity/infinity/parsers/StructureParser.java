package de.cric_hammel.eternity.infinity.parsers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;

public class StructureParser extends BlockParser {
	
	private Location origin;
	private Map<Location, BlockData> previousBlocks = new HashMap<Location, BlockData>();
	private boolean isParsed = false;

	public StructureParser(String fileName, Location origin) {
		super(fileName, origin.getWorld());
		this.origin = origin;
	}
	
	public void parse() {
		
		if (isParsed) {
			return;
		}
		
		List<Map<String, Object>> blocks = readData();
		
		if (blocks == null) {
			return;
		}
		
		for (int i = 0; i < blocks.size(); i++) {
			Map<String, Object> block = blocks.get(i);
            Location loc = origin.clone().add(Double.parseDouble(block.get("x").toString()),
                    Double.parseDouble(block.get("y").toString()),
                    Double.parseDouble(block.get("z").toString()));
            BlockData data = Bukkit.getServer().createBlockData(block.get("data").toString());
            
            previousBlocks.put(loc, loc.getBlock().getBlockData());
            
            loc.getBlock().setBlockData(data);
		}
		
		isParsed = true;
	}
	
	public void unparse() {
		
		if (!isParsed) {
			return;
		}
		
		for (Map.Entry<Location, BlockData> block : previousBlocks.entrySet()) {
			block.getKey().getBlock().setBlockData(block.getValue());
		}
		
		previousBlocks.clear();
		isParsed = false;
	}
}
