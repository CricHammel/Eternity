package de.cric_hammel.eternity.infinity.items.thanos;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import de.cric_hammel.eternity.Main;
import de.cric_hammel.eternity.infinity.items.CustomItem;
import de.cric_hammel.eternity.infinity.util.AttributeUtils;

public class ElectronCompressedChitauriDagger extends CustomItem implements Listener {

	private static final String META_KEY = "eternity_zap";

	public ElectronCompressedChitauriDagger() {
		super(Material.END_ROD, ChatColor.BLUE + "Electron-Compressed Chitauri Dagger", "Holds an unlimited charge");
	}

	@Override
	public ItemStack getItem() {
		ItemStack dagger = super.getItem();
		AttributeUtils.add(dagger, Attribute.GENERIC_ATTACK_DAMAGE, 15, Operation.ADD_NUMBER, EquipmentSlot.HAND);
		AttributeUtils.add(dagger, Attribute.GENERIC_ATTACK_SPEED, 2.5, Operation.ADD_NUMBER, EquipmentSlot.HAND);
		dagger.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 5);
		return dagger;
	}

	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if (!(event.getDamager() instanceof LivingEntity) || !(event.getEntity() instanceof LivingEntity)) {
			return;
		}

		LivingEntity e = (LivingEntity) event.getEntity();
		LivingEntity damager = (LivingEntity) event.getDamager();

		if (!super.hasInHand(damager) || e.hasMetadata(META_KEY)) {
			return;
		}

		e.setFreezeTicks(3*20);
		e.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 3*20, 3, false));
		e.setMetadata(META_KEY, new FixedMetadataValue(Main.getPlugin(), true));
		new BukkitRunnable() {

			int i = 0;

			@Override
			public void run() {
				if (i >= 3) {
					e.removeMetadata(META_KEY, Main.getPlugin());
					cancel();
					return;
				}

				e.damage(15);
				i++;
			}

		}.runTaskTimer(Main.getPlugin(), 0, 20);
	}
}
