package de.cric_hammel.eternity.infinity.mobs;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.loot.LootTable;

public abstract class CustomMob {
	
	private final EntityType type;
	private final String name;
	private final LootTable lootTable;
	
	public CustomMob(EntityType type, String name, LootTable lootTable) {
		this.type = type;
		this.name = name;
		this.lootTable = lootTable;
	}
	
	public Mob spawn(Location loc) {
		Mob m = (Mob) loc.getWorld().spawnEntity(loc, type, false);
		m.setCustomName(name);
		if (lootTable != null) {
			m.setLootTable(lootTable);
		}
		return m;
	}
	
	public boolean isMob(Entity e) {
		if (e.getType() == type && e.getCustomName().equals(name)) {
			return true;
		}
		return false;
	}
}
