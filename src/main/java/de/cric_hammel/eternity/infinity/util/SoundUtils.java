package de.cric_hammel.eternity.infinity.util;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SoundUtils {

	public static void playToAll(Player p, Sound s, float volume, float pitch) {
		p.getLocation().getWorld().playSound(p, s, volume, pitch);
	}

	public static void playToAll(Location l, Sound s, float volume, float pitch) {
		l.getWorld().playSound(l, s, volume, pitch);
	}

	public static void play(Player p, Sound s, float volume, float pitch) {
		p.playSound(p, s, volume, pitch);
	}
}
