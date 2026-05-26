package de.cric_hammel.eternity.infinity.mobs.kree;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;

import de.cric_hammel.eternity.infinity.mobs.DungeonMob;

public abstract class Kree extends DungeonMob {

	private static final String KREE_PREFIX = "Kree ";

	public Kree(EntityType type, String name) {
		super(type, Component.text(KREE_PREFIX + name, NamedTextColor.RED));
	}

	public boolean isKree(Entity e) {

		if (!(e.customName() instanceof TextComponent tc)) {
			return false;
		}

		if (tc.content().startsWith(KREE_PREFIX)) {
			return true;
		}

		return false;
	}
}
