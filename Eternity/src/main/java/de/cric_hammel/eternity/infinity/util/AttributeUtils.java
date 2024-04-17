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
		add(armor[3], a, value, o, EquipmentSlot.HEAD);
		add(armor[2], a, value, o, EquipmentSlot.CHEST);
		add(armor[1], a, value, o, EquipmentSlot.LEGS);
		add(armor[0], a, value, o, EquipmentSlot.FEET);
	}
}
