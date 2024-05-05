package de.cric_hammel.eternity.infinity.parsers;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.Map;

import org.bukkit.World;
import org.bukkit.util.io.BukkitObjectInputStream;

import de.cric_hammel.eternity.Main;

public abstract class BlockParser {

	protected String fileName;
    protected World world;
    
	public BlockParser(String fileName, World world) {
		this.fileName = fileName;
		this.world = world;
	}
	
	public abstract void parse();
	
	@SuppressWarnings("unchecked")
	protected List<Map<String, Object>> readData() {
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
	
	public void setWorld(World world) {
    	this.world = world;
    }
}
