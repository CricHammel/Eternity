package de.cric_hammel.eternity.infinity.worlds.dungeons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.cric_hammel.eternity.infinity.items.kree.XylopMeat;
import de.cric_hammel.eternity.infinity.items.misc.InfiniCoin;
import de.cric_hammel.eternity.infinity.items.stones.StoneType;
import de.cric_hammel.eternity.infinity.loot.DungeonChestLoot;
import de.cric_hammel.eternity.infinity.mobs.kree.KreeGeneral;
import de.cric_hammel.eternity.infinity.mobs.kree.KreeGuard;
import de.cric_hammel.eternity.infinity.mobs.kree.KreeSoldier;
import de.cric_hammel.eternity.infinity.mobs.kree.Xylop;
import de.cric_hammel.eternity.infinity.mobs.npc.ShopNpc;
import de.cric_hammel.eternity.infinity.mobs.npc.ShopNpc.ShopItem;
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
		parser.addAction(Material.MAGENTA_GLAZED_TERRACOTTA, (loc, data) -> KreeSoldier.getInstance().spawn(loc));
		parser.addAction(Material.PINK_GLAZED_TERRACOTTA, (loc, data) -> KreeGeneral.getInstance().spawn(loc));
		parser.addAction(Material.LIGHT_BLUE_GLAZED_TERRACOTTA, (loc, data) -> KreeGuard.getInstance().spawn(loc));
		parser.addAction(Material.GREEN_GLAZED_TERRACOTTA, (loc, data) -> Xylop.getInstance().spawn(loc));
		parser.addAction(Material.BROWN_GLAZED_TERRACOTTA, (loc, data) -> {
			ArrayList<ShopItem> items = new ArrayList<>();
			items.add(new ShopItem(ChatColor.RED + "InfiniCoin", Material.GOLD_INGOT, new ArrayList<>(Arrays.asList("3 Xylop Meat")), 0, (player) -> {
				Inventory inv = player.getInventory();
				ItemStack meat = XylopMeat.getInstance().getItem();
				meat.setAmount(3);
				
				if (!inv.containsAtLeast(meat, 3)) {
					return false;
				}
				
				inv.removeItem(meat);
				inv.addItem(InfiniCoin.getInstance().getItem());
				return true;
			}));
			loc.setYaw(0f);
			new ShopNpc(EntityType.PIGLIN, ChatColor.RED + "Xylop Farmer", loc, items);
			});
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
