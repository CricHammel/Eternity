package de.cric_hammel.eternity.infinity.mobs;

import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.loot.LootTable;
import org.bukkit.metadata.FixedMetadataValue;

import de.cric_hammel.eternity.Main;

public class DungeonMob extends CustomMob implements Listener {

	public static final String METADATA_KEY_FROZEN = "eternity_dungeon_frozen";

	public DungeonMob(EntityType type, String name, LootTable lootTable) {
		super(type, name, lootTable);
	}

	@Override
	public Mob spawn(Location loc) {
		Mob m = super.spawn(loc);
		AttributeInstance attribute = m.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
		m.setMetadata(METADATA_KEY_FROZEN, new FixedMetadataValue(Main.getPlugin(), attribute.getBaseValue()));
		attribute.setBaseValue(0);
		return m;
	}

	@EventHandler
	public void onEntityTargetFreeze(EntityTargetEvent event) {
		Entity e = event.getEntity();
		Entity target = event.getTarget();

		if (!super.isMob(e) || !(target instanceof Player)) {
			return;
		}

		Player p = (Player) target;

		if (!p.hasLineOfSight(e)) {
			event.setCancelled(true);
			return;
		}

		Mob m = (Mob) e;

		if (!m.hasMetadata(METADATA_KEY_FROZEN)) {
			return;
		}

		AttributeInstance speed = m.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
		speed.setBaseValue(m.getMetadata(METADATA_KEY_FROZEN).get(0).asDouble());
		m.removeMetadata(METADATA_KEY_FROZEN, Main.getPlugin());
	}
}
