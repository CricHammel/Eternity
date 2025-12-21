package de.cric_hammel.eternity.infinity.items.kree;

import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.cric_hammel.eternity.infinity.items.CustomArmor;
import de.cric_hammel.eternity.infinity.items.CustomTieredArmor;
import de.cric_hammel.eternity.infinity.util.AttributeUtils;

public class KreeArmor extends CustomTieredArmor {

	private static KreeArmor instance;

	public static KreeArmor getInstance() {
		if (null == instance) {
			synchronized (KreeArmor.class) {
				if (null == instance) {
					instance = new KreeArmor();
				}
			}
		}
		
		return instance;
	}
	
	private KreeArmor() {
		super(CustomArmor.ArmorType.CHAINMAIL, ChatColor.RED + "Kree", "The Armor of the mighty Kree");
	}

	@Override
	public void changeTierOne() {
		ItemStack[] tierOne = super.getTier(1);
		AttributeUtils.addToArmor(tierOne, Attribute.GENERIC_ATTACK_DAMAGE, 0.25, Operation.ADD_SCALAR);
	}

	@Override
	public void changeTierTwo() {
		ItemStack[] tierTwo = super.getTier(2);
		AttributeUtils.addToArmor(tierTwo, Attribute.GENERIC_ATTACK_DAMAGE, 0.5, Operation.ADD_SCALAR);
	}

	@Override
	public void changeTierThree() {
		ItemStack[] tierThree = super.getTier(3);
		AttributeUtils.addToArmor(tierThree, Attribute.GENERIC_ATTACK_DAMAGE, 0.75, Operation.ADD_SCALAR);
	}

	public static class Listeners implements Listener {
		
		@EventHandler
		public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
			Entity d = event.getDamager();
			Entity e = event.getEntity();

			if (!(d instanceof LivingEntity) || !(e instanceof LivingEntity)) {
				return;
			}

			LivingEntity ld = (LivingEntity) d;
			LivingEntity le = (LivingEntity) e;

			if (!(KreeArmor.getInstance()).isWearingTier(ld, 3)) {
				return;
			}

			le.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 3 * 20, 2));
		}
	}
}
