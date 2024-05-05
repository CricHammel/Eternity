package de.cric_hammel.eternity.infinity.util;

import org.bukkit.event.block.Action;

public class ActionUtils {

	public static boolean isRightclick(Action a) {
		if (a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK) {
			return true;
		}
		return false;
	}

	public static boolean isLeftclick(Action a) {
		if (a == Action.LEFT_CLICK_AIR || a == Action.LEFT_CLICK_BLOCK) {
			return true;
		}
		return false;
	}
}
