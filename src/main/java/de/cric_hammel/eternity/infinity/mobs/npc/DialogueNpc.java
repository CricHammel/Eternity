package de.cric_hammel.eternity.infinity.mobs.npc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.scheduler.BukkitRunnable;

import de.cric_hammel.eternity.Main;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class DialogueNpc {

	private static final Map<LivingEntity, DialogueNpc> NPCS = new HashMap<>();

	private final Dialogue d;

	public DialogueNpc(EntityType type, Component name, Location loc, Dialogue d) {
		d.name = name;
		this.d = d;
		LivingEntity e = (LivingEntity) loc.getWorld().spawnEntity(loc, type, false);
		e.customName(name);
		e.setCustomNameVisible(true);
		e.setInvulnerable(true);
		e.setPersistent(true);
		e.setAI(false);
		e.setCanPickupItems(false);
		e.setCollidable(false);
		e.setRemoveWhenFarAway(false);
		NPCS.put(e, this);
	}

	public static boolean isNpc(Entity e) {
		return NPCS.containsKey(e);
	}

	public static class Dialogue {

		private Component name;
		private TextColor color;
		private int delaySec;
		private List<String> dialogue;

		public Dialogue(TextColor color, int delaySec) {
			this.color = color;
			this.delaySec= delaySec;
			dialogue = new ArrayList<>();
		}

		public void add(String text) {
			dialogue.add(text);
		}

		public void talk(Player p) {
			new BukkitRunnable() {
				int count = 0;

				@Override
				public void run() {
					if (count >= dialogue.size() || !p.isOnline()) {
						cancel();
						return;
					}

					p.sendMessage(Component.text("[").append(name).append(Component.text("] "))
							.append(Component.text(dialogue.get(count), color)));
					count++;
				}
			}.runTaskTimer(Main.getPlugin(), 0, delaySec * 20);
		}
	}

	public static class Listeners implements Listener {

		@EventHandler
		public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {

			if (event.getHand() != EquipmentSlot.HAND) {
				return;
			}

			Entity e = event.getRightClicked();

			if (!NPCS.containsKey(e)) {
				return;
			}

			DialogueNpc npc = NPCS.get(e);
			npc.d.talk(event.getPlayer());
			event.setCancelled(true);
		}
	}
}
