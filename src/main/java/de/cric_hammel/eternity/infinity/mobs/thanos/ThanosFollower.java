package de.cric_hammel.eternity.infinity.mobs.thanos;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;

import de.cric_hammel.eternity.infinity.mobs.CustomMob;
import net.kyori.adventure.text.Component;

public abstract class ThanosFollower extends CustomMob {

	public ThanosFollower(EntityType type, Component name) {
		super(type, name);
	}

	@Override
	public Mob spawn(Location loc) {
		Mob m = super.spawn(loc);
		return m;
	}
}
