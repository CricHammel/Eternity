package de.cric_hammel.eternity.infinity.util;

import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.cric_hammel.eternity.Main;

public class AttributeUtils {

	public static void add(ItemStack item, Attribute a, double value, Operation o, EquipmentSlotGroup e) {
		ItemMeta meta = item.getItemMeta();
		meta.addAttributeModifier(a, new AttributeModifier(new NamespacedKey(Main.getPlugin(), a.toString()), value, o, e));
		item.setItemMeta(meta);
	}

	public static void addToArmor(ItemStack[] armor, Attribute a, double value, Operation o) {
		add(armor[3], a, value, o, EquipmentSlotGroup.HEAD);
		add(armor[2], a, value, o, EquipmentSlotGroup.CHEST);
		add(armor[1], a, value, o, EquipmentSlotGroup.LEGS);
		add(armor[0], a, value, o, EquipmentSlotGroup.FEET);
	}
}
