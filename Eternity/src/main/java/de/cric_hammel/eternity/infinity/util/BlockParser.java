package de.cric_hammel.eternity.infinity.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.util.io.BukkitObjectInputStream;

import de.cric_hammel.eternity.Main;

public class BlockParser {

	private String fileName;
	
	private final Map<Material, BlockAction> actions = new HashMap<Material, BlockAction>();
	
	public BlockParser(String fileName) {
		this.fileName = fileName;
	}
	
	@SuppressWarnings("unchecked")
	public void parse(World w) {
		File file = new File(Main.getPlugin().getDataFolder(), fileName);
		List<Map<String, Object>> blocks;
		
		try {
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
			BukkitObjectInputStream bois = new BukkitObjectInputStream(ois);
			blocks = (List<Map<String, Object>>) bois.readObject();
			bois.close();
		
			for (Map<String, Object> block : blocks) {
				Location loc = new Location(w, Double.parseDouble(block.get("x").toString()), Double.parseDouble(block.get("y").toString()), Double.parseDouble(block.get("z").toString()));
				BlockData data = Bukkit.getServer().createBlockData(block.get("data").toString());
				Material m = data.getMaterial();
				BlockAction action = actions.get(m);
				
				if (action != null) {
					action.execute(loc.add(0.5, 0, 0.5), data);
					continue;
				}
				
				loc.getBlock().setBlockData(data);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return;
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
	
	public interface BlockAction {
		void execute(Location loc, BlockData data);
	}
}
