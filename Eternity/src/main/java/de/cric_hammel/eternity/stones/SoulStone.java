package de.cric_hammel.eternity.stones;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import de.cric_hammel.eternity.util.StoneType;

public class SoulStone implements Listener {

	private HashMap<Player, Location> lastLoc = new HashMap<Player, Location>();

	@EventHandler
	public void useSoulStone(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		Action a = event.getAction();
		if (StoneType.SOUL.hasStoneInHand(p) && (a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK)
				&& !StoneType.SOUL.hasCooldownRightclick(p)) {
			World soulWorld = Bukkit.getWorld(p.getName());
			if (soulWorld == null) {
				WorldCreator soulWorldCreator = new WorldCreator(p.getName());
				soulWorldCreator.environment(Environment.NORMAL);
				soulWorldCreator.generateStructures(false);
				soulWorldCreator.type(WorldType.FLAT);
				soulWorldCreator.generatorSettings(
						"{\"structures\": {\"structures\": {}}, \"layers\": [{\"block\": \"bedrock\", \"height\": 1}, {\"block\": \"water\", \"height\": 1}], \"biome\":\"the_void\"}");

				soulWorld = soulWorldCreator.createWorld();
				soulWorld.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
				soulWorld.setTime(12500);
			}
			if (p.getWorld() == soulWorld) {
				if (lastLoc.containsKey(p)) {
					p.teleport(lastLoc.get(p));
					lastLoc.remove(p);
				} else {
					p.teleport(Bukkit.getWorld(getLevelname()).getSpawnLocation());
				}
				StoneType.SOUL.applyCooldownRightclick(p);
			} else {
				lastLoc.put(p, p.getLocation());
				p.teleport(soulWorld.getSpawnLocation());
			}
			p.playSound(p.getLocation(), Sound.AMBIENT_CAVE, 1, 2);
		}
	}

	@EventHandler
	public void useSoulStoneEntityHit(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player && event.getEntity() instanceof Damageable) {
			Player p = (Player) event.getDamager();
			Damageable d = (Damageable) event.getEntity();
			if (StoneType.SOUL.hasStoneInHand(p) && !StoneType.SOUL.hasCooldownLeftclick(p)) {
				double maxHealth = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
				double currentHealth = p.getHealth();
				if ((currentHealth + 4) <= maxHealth) {
					d.damage(4);
					p.setHealth(currentHealth + 4);
				} else {
					d.damage(maxHealth - currentHealth);
					p.setHealth(maxHealth);
				}
				p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_SCREAM, 1, 1.5f);
				p.getWorld().spawnParticle(Particle.HEART, d.getLocation(), 2, 0.5, 0.5, 0.5);
				StoneType.SOUL.applyCooldownLeftclick(p);
			}
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player p = event.getPlayer();
		if (p.getWorld().getName().equals(p.getName())) {
			if (lastLoc.containsKey(p)) {
				p.teleport(lastLoc.get(p));
				lastLoc.remove(p);
			} else {
				p.teleport(Bukkit.getWorld(getLevelname()).getSpawnLocation());
			}
		}
	}

	private String getLevelname() {
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(new File("server.properties")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop.getProperty("level-name");
	}
}
