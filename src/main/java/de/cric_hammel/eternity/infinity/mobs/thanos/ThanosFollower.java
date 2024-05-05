package de.cric_hammel.eternity.infinity.mobs.thanos;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.loot.LootTable;
import org.bukkit.metadata.FixedMetadataValue;

import de.cric_hammel.eternity.Main;
import de.cric_hammel.eternity.infinity.mobs.CustomMob;

public abstract class ThanosFollower extends CustomMob {

	public static final String META_KEY = "eternity_thanos";

	public ThanosFollower(EntityType type, String name, LootTable loot) {
		super(type, name, loot);
	}

	@Override
	public Mob spawn(Location loc) {
		Mob m = super.spawn(loc);
		m.setMetadata(META_KEY, new FixedMetadataValue(Main.getPlugin(), 1));
		return m;
	}
}
