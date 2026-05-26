package de.cric_hammel.eternity.infinity.items.thanos;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import de.cric_hammel.eternity.Main;
import de.cric_hammel.eternity.infinity.items.CustomItem;
import de.cric_hammel.eternity.infinity.util.AttributeUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class ElectronCompressedChitauriDagger extends CustomItem {

	private static ElectronCompressedChitauriDagger instance;

	public static ElectronCompressedChitauriDagger getInstance() {
		if (null == instance) {
			synchronized (ElectronCompressedChitauriDagger.class) {
				if (null == instance) {
					instance = new ElectronCompressedChitauriDagger();
				}
			}
		}
		
		return instance;
	}
	
	private ElectronCompressedChitauriDagger() {
		super(Material.END_ROD, Component.text("Electron-Compressed Chitauri Dagger", NamedTextColor.BLUE), Component.text("Holds an unlimited charge"));
	}

	@Override
	public ItemStack getItem() {
		ItemStack dagger = super.getItem();
		AttributeUtils.add(dagger, Attribute.ATTACK_DAMAGE, 15, Operation.ADD_NUMBER, EquipmentSlotGroup.HAND);
		AttributeUtils.add(dagger, Attribute.ATTACK_SPEED, 2.5, Operation.ADD_NUMBER, EquipmentSlotGroup.HAND);
		dagger.addUnsafeEnchantment(Enchantment.SHARPNESS, 5);
		return dagger;
	}

	public static class Listeners implements Listener {
		
		private static final Set<Entity> ZAPPED = new HashSet<>();

		@EventHandler
		public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
			if (!(event.getDamager() instanceof LivingEntity) || !(event.getEntity() instanceof LivingEntity)) {
				return;
			}

			LivingEntity e = (LivingEntity) event.getEntity();
			LivingEntity damager = (LivingEntity) event.getDamager();

			if (!ElectronCompressedChitauriDagger.getInstance().hasInHand(damager) || ZAPPED.contains(e)) {
				return;
			}

			e.setFreezeTicks(3*20);
			e.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 3*20, 3, false));
			ZAPPED.add(e);
			new BukkitRunnable() {

				int i = 0;

				@Override
				public void run() {
					if (i >= 3) {
						ZAPPED.remove(e);
						cancel();
						return;
					}

					e.damage(15);
					i++;
				}

			}.runTaskTimer(Main.getPlugin(), 0, 20);
		}
	}
}
