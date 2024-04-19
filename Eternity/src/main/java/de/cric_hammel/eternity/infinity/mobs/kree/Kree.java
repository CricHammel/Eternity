package de.cric_hammel.eternity.infinity.mobs.kree;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.loot.LootTable;

import de.cric_hammel.eternity.infinity.mobs.DungeonMob;

public abstract class Kree extends DungeonMob {

	private static final String KREE_PREFIX = ChatColor.RED + "Kree ";

	public Kree(EntityType type, String name, LootTable lootTable) {
		super(type, KREE_PREFIX + name, lootTable);
	}

	public boolean isKree(Entity e) {
		
		if (e.getCustomName() == null) {
			return false;
		}
		
		if (e.getCustomName().startsWith(KREE_PREFIX)) {
			return true;
		}
		
		return false;
	}
}
