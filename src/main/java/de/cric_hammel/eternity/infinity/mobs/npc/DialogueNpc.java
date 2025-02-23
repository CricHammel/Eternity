package de.cric_hammel.eternity.infinity.mobs.npc;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import de.cric_hammel.eternity.Main;

public class DialogueNpc {

	private static final String META_KEY_NPC = "eternity_dialoguenpc";

	private final Dialogue d;

	public DialogueNpc(EntityType type, String name, Location loc, Dialogue d) {
		d.name = name;
		this.d = d;
		LivingEntity e = (LivingEntity) loc.getWorld().spawnEntity(loc, type, false);
		e.setCustomName(name);
		e.setCustomNameVisible(true);
		e.setInvulnerable(true);
		e.setPersistent(true);
		e.setAI(false);
		e.setCanPickupItems(false);
		e.setCollidable(false);
		e.setRemoveWhenFarAway(false);
		e.setMetadata(META_KEY_NPC, new FixedMetadataValue(Main.getPlugin(), this));
	}
	
	public static boolean isNpc(LivingEntity e) {
		return e.hasMetadata(META_KEY_NPC);
	}

	public static class Dialogue {

		private String name;
		private ChatColor color;
		private int delaySec;
		private List<String> dialogue;

		public Dialogue(ChatColor color, int delaySec) {
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

					p.sendMessage("[" + name + "] " + color + dialogue.get(count));
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

			if (!e.hasMetadata(META_KEY_NPC)) {
				return;
			}

			DialogueNpc npc = (DialogueNpc) e.getMetadata(META_KEY_NPC).get(0).value();
			npc.d.talk(event.getPlayer());
			event.setCancelled(true);
		}
	}
}
