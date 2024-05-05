package de.cric_hammel.eternity.infinity.worlds.dungeons;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import de.cric_hammel.eternity.infinity.items.stones.StoneType;
import de.cric_hammel.eternity.infinity.loot.DungeonChestLoot;
import de.cric_hammel.eternity.infinity.mobs.kree.KreeGeneral;
import de.cric_hammel.eternity.infinity.mobs.kree.KreeGuard;
import de.cric_hammel.eternity.infinity.mobs.kree.KreeSoldier;
import de.cric_hammel.eternity.infinity.parsers.WorldParser;

public class DungeonFactory {

	private static Map<Player, Dungeon> dungeons = new HashMap<>();

	public static Dungeon createFromType(Player p, StoneType type) {

		switch (type) {
		case POWER:
			return createPowerDungeon(p);

		default:
			return null;
		}
	}

	public static Dungeon createPowerDungeon(Player p) {
		Dungeon current = getCurrentDungeon(p);

		if (current != null) {
			return current;
		}

		Dungeon dungeon = new Dungeon(p, StoneType.POWER, new DungeonChestLoot("power_chest.yml"), Material.CRACKED_STONE_BRICKS, "power_dungeon.txt");
		WorldParser parser = dungeon.getParser();
		parser.addAction(Material.MAGENTA_GLAZED_TERRACOTTA, (loc, data) -> new KreeSoldier().spawn(loc));
		parser.addAction(Material.PINK_GLAZED_TERRACOTTA, (loc, data) -> new KreeGeneral().spawn(loc));
		parser.addAction(Material.LIGHT_BLUE_GLAZED_TERRACOTTA, (loc, data) -> new KreeGuard().spawn(loc));
		dungeons.put(p, dungeon);
		dungeon.create();
		return dungeon;
	}

	public static void closeDungeon(Player p) {
		Dungeon dungeon = getCurrentDungeon(p);

		if (dungeon == null) {
			return;
		}

		dungeon.delete();
		dungeons.remove(p);
	}

	public static void closeAll() {

		for (Map.Entry<Player, Dungeon> entry : dungeons.entrySet()) {
			Dungeon dungeon = entry.getValue();
			dungeon.delete();
		}

		dungeons.clear();
	}

	public static Dungeon getCurrentDungeon(Player p) {
		return dungeons.get(p);
	}

	public static Map<Player, Dungeon> getDungeons() {
		return dungeons;
	}
}
