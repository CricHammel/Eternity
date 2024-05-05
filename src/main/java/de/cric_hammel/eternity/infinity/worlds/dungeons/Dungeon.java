package de.cric_hammel.eternity.infinity.worlds.dungeons;

import java.io.File;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import de.cric_hammel.eternity.Main;
import de.cric_hammel.eternity.infinity.items.keys.DungeonKeyCore;
import de.cric_hammel.eternity.infinity.items.stones.StoneType;
import de.cric_hammel.eternity.infinity.loot.CustomLootTable;
import de.cric_hammel.eternity.infinity.mobs.kree.KreeGuard;
import de.cric_hammel.eternity.infinity.parsers.WorldParser;
import de.cric_hammel.eternity.infinity.parsers.WorldParser.BlockAction;
import de.cric_hammel.eternity.infinity.util.ActionUtils;

/**
 * Represents a custom dungeon, bound to a player.
 * Dungeons should be created via the {@link DungeonFactory}
 */
public class Dungeon implements Listener {

	private Material mineable;
	private WorldParser parser;
	private CustomLootTable loot;
	private Player p;
	private StoneType type;
	private World world;
	private Location lastLoc;

	protected Dungeon(Player p, StoneType type, CustomLootTable loot, Material mineable, String fileName) {
		this.p = p;
		this.type = type;
		this.loot = loot;
		this.mineable = mineable;
		parser = new WorldParser(fileName, null);
		parser.addAction(Material.SPAWNER, (loc, data) -> loc.getWorld().setSpawnLocation(loc));
		parser.addAction(Material.CHEST, (loc, data) -> {
			Block b = loc.getBlock();
			b.setBlockData(data);
			Chest c = (Chest) b.getState();
			c.update(true);
		});
		// TODO: Implement custom banners
//		parser.addAction(Tag.BANNERS, new BlockAction() {
//
//			@Override
//			public void execute(Location loc, BlockData data) {
//				Block b = loc.getBlock();
//				b.setBlockData(data);
//				Banner banner = (Banner) b.getState();
//				createBanner(banner);
//				banner.update(true);
//			}
//		});
		parser.addAction(Material.ORANGE_CONCRETE, (loc, data) -> loc.getBlock().setType(Material.LAVA));
	}

	/**
	 * Actually creates the world.
	 * The separation from the contructor is to allow the {@link DungeonFactory} to inject more {@link BlockAction}s.
	 */
	protected void create() {
		WorldCreator wc = new WorldCreator("Dungeon of " + p.getName());
		wc.environment(Environment.NORMAL);
		wc.generateStructures(false);
		wc.type(WorldType.FLAT);
		wc.generatorSettings("{\"structures\": {\"structures\": {}}, \"layers\": [], \"biome\":\"the_void\"}");
		world = wc.createWorld();
		world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
		world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
		world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
		world.setTime(18000);
		parser.setWorld(world);
		parser.parse();
		lastLoc = p.getLocation();
		parser.tryTeleport(p, 90);
	}

	/**
	 * Deletes the associated world of the dungeon, kicking the player
	 */
	protected void delete() {

		if (world.getPlayers().contains(p)) {
			p.teleport(lastLoc);
		}

		Bukkit.unloadWorld(world, false);
		removeDirectory(world.getWorldFolder());
	}

	private boolean removeDirectory(File directoryToBeDeleted) {
	    File[] contents = directoryToBeDeleted.listFiles();

	    if (contents != null) {

	        for (File file : contents) {
	            removeDirectory(file);
	        }
	    }

	    return directoryToBeDeleted.delete();
	}

	public WorldParser getParser() {
		return parser;
	}

	public static class Listeners implements Listener {

		private static final Material trap = Material.GILDED_BLACKSTONE;
		private static final String META_KILL_KEY = "eternity_powerdungeon_killed_guard";
		private static final String META_KEY_OPENED = "eternity_opened";

		@EventHandler
		public void onPlayerQuitAll(PlayerQuitEvent event) {
			Player p = event.getPlayer();

			if (DungeonFactory.getCurrentDungeon(p) != null) {
				DungeonFactory.closeDungeon(p);
			}
		}

		@EventHandler
		public void onEntityDamageAll(EntityDamageEvent event) {

			if (!(event.getEntity() instanceof Player)) {
				return;
			}

			Player p = (Player) event.getEntity();

			if (DungeonFactory.getCurrentDungeon(p) == null || event.getFinalDamage() < p.getHealth()) {
				return;
			}

			event.setCancelled(true);
			p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
			DungeonFactory.closeDungeon(p);
		}

		@EventHandler
		public void onBlockBreakAll(BlockBreakEvent event) {
			Player p = event.getPlayer();
			Material m = event.getBlock().getType();
			Dungeon dungeon = DungeonFactory.getCurrentDungeon(p);

			if (dungeon != null && m != dungeon.mineable) {
				event.setCancelled(true);
			}
		}

		@EventHandler
		public void onBlockPlaceAll(BlockPlaceEvent event) {

			if (DungeonFactory.getCurrentDungeon(event.getPlayer()) != null) {
				event.setCancelled(true);
			}
		}

		@EventHandler
		public void onPlayerBucketFillAll(PlayerBucketFillEvent event) {

			if (DungeonFactory.getCurrentDungeon(event.getPlayer()) != null) {
				event.setCancelled(true);
			}
		}

		@EventHandler
		public void onPlayerBucketEmptyAll(PlayerBucketEmptyEvent event) {

			if (DungeonFactory.getCurrentDungeon(event.getPlayer()) != null) {
				event.setCancelled(true);
			}
		}

		@EventHandler
		public void onPlayerInteractAllChests(PlayerInteractEvent event) {
			Dungeon dungeon = DungeonFactory.getCurrentDungeon(event.getPlayer());

			if (dungeon == null || event.getAction() != Action.RIGHT_CLICK_BLOCK || !(event.getClickedBlock().getState() instanceof Chest)) {
				return;
			}

			Chest chest = (Chest) event.getClickedBlock().getState();

			if (chest.hasMetadata(META_KEY_OPENED)) {
				return;
			}

			dungeon.loot.generateLoot(chest.getInventory(), new Random());
			chest.setMetadata(META_KEY_OPENED, new FixedMetadataValue(Main.getPlugin(), 1));
		}
		
		@EventHandler
		public void onPlayerInteractAllPearls(PlayerInteractEvent event) {
			Player p = event.getPlayer();
			Dungeon dungeon = DungeonFactory.getCurrentDungeon(p);
			
			if (dungeon == null || !ActionUtils.isRightclick(event.getAction())) {
				return;
			}
			
			if (event.getMaterial() == Material.ENDER_PEARL) {
				event.setCancelled(true);
			}
		}

		@EventHandler
		public void onBlockSpreadAll(BlockSpreadEvent event) {

			if (event.getSource().getType() != Material.FIRE) {
				return;
			}

			for (Map.Entry<Player, Dungeon> entry : DungeonFactory.getDungeons().entrySet()) {
				Dungeon dungeon = entry.getValue();

				if (dungeon.world.equals(event.getSource().getWorld())) {
					event.setCancelled(true);
				}
			}
		}

		@EventHandler
		public void onPlayerMovePower(PlayerMoveEvent event) {
			Dungeon dungeon = DungeonFactory.getCurrentDungeon(event.getPlayer());

			if (dungeon == null || dungeon.type != StoneType.POWER) {
				return;
			}

			Location from = event.getFrom();
			Location to = event.getTo();

			if (to.getBlockX() == from.getBlockX() && to.getBlockY() == from.getBlockY() && to.getBlockZ() == from.getBlockZ()) {
				return;
			}

			Block underPlayer = to.getBlock().getRelative(BlockFace.DOWN);

			if (underPlayer.getType() != trap) {
				return;
			}

			Location spawn = to.clone();
			spawn.setX(to.getBlockX() + 0.5);
			spawn.setZ(to.getBlockZ() + 0.5);
			AreaEffectCloud cloud = (AreaEffectCloud) to.getWorld().spawnEntity(spawn, EntityType.AREA_EFFECT_CLOUD);
			cloud.setRadius(1.5f);
			cloud.setBasePotionData(new PotionData(PotionType.INSTANT_DAMAGE));
			cloud.setColor(Color.RED);
			cloud.setDuration(3*20);
			cloud.setRadiusPerTick(0);
			cloud.setReapplicationDelay(10);
		}

		@EventHandler
		public void onEntityDeathPower(EntityDeathEvent event) {
			LivingEntity e = event.getEntity();

			if (!(e.getLastDamageCause().getCause() == DamageCause.ENTITY_ATTACK)) {
				return;
			}

			Player p = e.getKiller();
			Dungeon dungeon = DungeonFactory.getCurrentDungeon(p);

			if (p == null || dungeon == null || dungeon.type != StoneType.POWER || !(new KreeGuard().isMob(e))) {
				return;
			}

			if (p.hasMetadata(META_KILL_KEY)) {
				World w = dungeon.world;
				// TODO: Insert final location of stone drop
				Location loc = new Location(w, 0, 0, 0);

				if (StoneType.POWER.canGetStone(p)) {
					w.dropItem(loc, StoneType.POWER.getItem());
				} else {
					w.dropItem(loc, new DungeonKeyCore().getItem());
				}

				p.removeMetadata(META_KILL_KEY, Main.getPlugin());
			} else {
				p.setMetadata(META_KILL_KEY, new FixedMetadataValue(Main.getPlugin(), 1));
			}
		}
	}
}
