package de.cric_hammel.eternity.infinity.dungeons;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import de.cric_hammel.eternity.infinity.mobs.kree.KreeGeneral;
import de.cric_hammel.eternity.infinity.mobs.kree.KreeGuard;
import de.cric_hammel.eternity.infinity.mobs.kree.KreeSoldier;

public class PowerDungeon extends Dungeon implements Listener {
	
	private static final Material trap = Material.GILDED_BLACKSTONE;

	public PowerDungeon() {
		super("power_dungeon.txt", null, Material.CRACKED_STONE_BRICKS);
		parser.addAction(Material.MAGENTA_GLAZED_TERRACOTTA, (loc, data) -> new KreeSoldier().spawn(loc));
		parser.addAction(Material.PINK_GLAZED_TERRACOTTA, (loc, data) -> new KreeGeneral().spawn(loc));
		parser.addAction(Material.LIGHT_BLUE_GLAZED_TERRACOTTA, (loc, data) -> new KreeGuard().spawn(loc));
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		
		if (!super.isInDungeon(event.getPlayer())) {
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
}
