package de.cric_hammel.eternity.infinity.items.gauntlet;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import de.cric_hammel.eternity.Main;
import de.cric_hammel.eternity.infinity.items.CustomItem;

public class Gauntlet implements Listener{
	
	public static final String LORE = "Designed to channel the power of all six Infinity Stones";
	private static final String METADATA_KEY_FREEZE = "eternity_gauntlet_freeze";
	private static final String METADATA_KEY_COOLDOWN_LEFT = "eternity_gauntlet_cooldown_left";
	private static final String METADATA_KEY_COOLDOWN_RIGHT = "eternity_gauntlet_cooldown_right";
	
	private static final int cooldownLeftclick = 120;
	private static final int cooldownRightclick = 60;
	private static final Material m = Material.TORCHFLOWER;
	
	@EventHandler
	public void useGauntlet(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		if (hasGauntletInOffHand(p)) {
			Action a = event.getAction();
			if ((a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK) && !CustomItem.hasCooldown(p, METADATA_KEY_COOLDOWN_RIGHT, m)) {
				boolean remove = true;
				for (Entity e : p.getNearbyEntities(200, 200, 200)) {
					if (e instanceof Monster) {
						if (remove) {
							e.remove();
							remove = false;
							p.getWorld().spawnParticle(Particle.SMOKE_NORMAL, e.getLocation(), 50, 0.25, 0.5, 0.25, 0);
						} else {
							remove = true;
						}
					}
				}
				p.playSound(p.getLocation(), Sound.PARTICLE_SOUL_ESCAPE, 10, 0.5f);
				CustomItem.applyCooldown(p, METADATA_KEY_COOLDOWN_RIGHT, cooldownRightclick, m);
			} else if ((a == Action.LEFT_CLICK_AIR || a == Action.LEFT_CLICK_BLOCK) && !CustomItem.hasCooldown(p, METADATA_KEY_COOLDOWN_LEFT, m)) {
				World w = p.getWorld();
				w.setTime(1000);
				w.setThundering(false);
				w.setStorm(false);
				p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
				p.setFoodLevel(20);
				p.setSaturation(20);
				for (PotionEffect effect : p.getActivePotionEffects()) {
					if (NegativeEffects.contains(effect.getType().getName())) {
						p.removePotionEffect(effect.getType());
					}
				}
				for (final Entity e : p.getNearbyEntities(20, 20, 20)) {
					if (e instanceof Player) {
						e.setMetadata(METADATA_KEY_FREEZE, new FixedMetadataValue(Main.getPlugin(), 1));
						new BukkitRunnable() {
							
							@Override
							public void run() {
								e.removeMetadata(METADATA_KEY_FREEZE, Main.getPlugin());
							}
						}.runTaskLaterAsynchronously(Main.getPlugin(), 15*20);
					} else if (e instanceof LivingEntity) {
						final LivingEntity l = (LivingEntity) e;
						l.setAI(false);
						new BukkitRunnable() {
							
							@Override
							public void run() {
								l.setAI(true);
							}
						}.runTaskLaterAsynchronously(Main.getPlugin(), 15*20);
					}
				}
				ItemStack[] contents = p.getInventory().getContents();
				for (ItemStack i : contents) {
					if (i != null && i.hasItemMeta()) {
						ItemMeta iMeta = i.getItemMeta();
						if (iMeta instanceof Damageable) {
							Damageable iMetaD = (Damageable) iMeta;
							iMetaD.setDamage(0);
							i.setItemMeta(iMetaD);
						}
					}
				}
				p.getInventory().setContents(contents);
				p.playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1, 1.5f);
				p.getWorld().spawnParticle(Particle.HEART, p.getLocation(), 50, 0.75, 0.5, 0.75);
				CustomItem.applyCooldown(p, METADATA_KEY_COOLDOWN_LEFT, cooldownLeftclick, m);
			}
			event.setCancelled(true);
		}
	}
	
	private boolean hasGauntletInOffHand(Player p) {
		ItemStack gauntlet = p.getInventory().getItemInOffHand();
		if (gauntlet != null && gauntlet.hasItemMeta() && gauntlet.getItemMeta().hasLore() && gauntlet.getItemMeta().getLore().get(0).equals(LORE) && !CustomItem.hasAnyInHand(p)) {
			return true;
		}
		return false;
	}
	
	private enum NegativeEffects {
        CONFUSION, HARM, HUNGER, POISON, SLOW_DIGGING, SLOW, WEAKNESS, WITHER;
		
		public static boolean contains(String name) {
			try {
				valueOf(name);
				return true;
			} catch (Exception e) {
				return false;
			}
		}
    }
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player p = event.getPlayer();
		if (p.hasMetadata(METADATA_KEY_FREEZE)) {
			event.setCancelled(true);
		}
	}
	
	public static ItemStack getItem() {
		ItemStack gauntlet = CustomItem.getItem(m, ChatColor.GOLD + "Infinity Gauntlet", LORE);
		ItemMeta gauntletMeta = gauntlet.getItemMeta();
		gauntletMeta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, new AttributeModifier(UUID.randomUUID(), "generic.max_health", 2*20, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.OFF_HAND));
		gauntletMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(), "generic.attack_damage", 10, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.OFF_HAND));
		gauntletMeta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(), "generic.armor_toughness", 10, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.OFF_HAND));
		gauntlet.setItemMeta(gauntletMeta);
		gauntlet.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1);
		return gauntlet;
	}
}
