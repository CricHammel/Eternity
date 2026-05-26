package de.cric_hammel.eternity.infinity.util;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import net.kyori.adventure.text.Component;

public class DeadEntityStorage {

	private EntityType type;
	private ItemStack[] equipment;
	private Location location;
	private Component customName;

	public DeadEntityStorage(LivingEntity e) {
		type = e.getType();
		equipment = e.getEquipment().getArmorContents();
		location = e.getLocation();
		customName = e.customName();
	}

	public void resurrect() {
		LivingEntity e = (LivingEntity) location.getWorld().spawnEntity(location, type, false);
		e.getEquipment().setArmorContents(equipment);
		e.customName(customName);
	}

	public Location getLocation() {
		return location;
	}
}
