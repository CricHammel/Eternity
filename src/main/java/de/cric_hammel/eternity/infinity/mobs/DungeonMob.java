package de.cric_hammel.eternity.infinity.mobs;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;

import net.kyori.adventure.text.Component;

public abstract class DungeonMob extends CustomMob {

	public DungeonMob(EntityType type, Component name) {
		super(type, name);
	}

	@Override
	public Mob spawn(Location loc) {
		Mob m = super.spawn(loc);
		m.setAI(false);
		return m;
	}
}
