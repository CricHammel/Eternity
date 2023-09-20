package de.cric_hammel.eternity.infinity.util;

import java.util.UUID;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AttributeUtils {

	public static void add(ItemStack item, Attribute a, double value, Operation o, EquipmentSlot e) {
		ItemMeta meta = item.getItemMeta();
		meta.addAttributeModifier(a, new AttributeModifier(UUID.randomUUID(), a.toString(), value, o, e));
		item.setItemMeta(meta);
	}
	
	public static void addToArmor(ItemStack[] armor, Attribute a, double value, Operation o) {
		ItemMeta helmetMeta = armor[3].getItemMeta();
		helmetMeta.addAttributeModifier(a, new AttributeModifier(UUID.randomUUID(), a.toString(), value, o, EquipmentSlot.HEAD));
		armor[3].setItemMeta(helmetMeta);
		
		ItemMeta chestplateMeta = armor[2].getItemMeta();
		chestplateMeta.addAttributeModifier(a, new AttributeModifier(UUID.randomUUID(), a.toString(), value, o, EquipmentSlot.CHEST));
		armor[2].setItemMeta(chestplateMeta);
		
		ItemMeta leggingsMeta = armor[1].getItemMeta();
		leggingsMeta.addAttributeModifier(a, new AttributeModifier(UUID.randomUUID(), a.toString(), value, o, EquipmentSlot.LEGS));
		armor[1].setItemMeta(leggingsMeta);
		
		ItemMeta bootsMeta = armor[0].getItemMeta();
		bootsMeta.addAttributeModifier(a, new AttributeModifier(UUID.randomUUID(), a.toString(), value, o, EquipmentSlot.FEET));
		armor[0].setItemMeta(bootsMeta);
	}
}
