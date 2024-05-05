package de.cric_hammel.eternity.infinity.util;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

public class DeadEntityStorage {

	private EntityType type;
	private ItemStack[] equipment;
	private Location location;
	private String customName;

	public DeadEntityStorage(LivingEntity e) {
		type = e.getType();
		equipment = e.getEquipment().getArmorContents();
		location = e.getLocation();
		customName = e.getCustomName();
	}

	public void resurrect() {
		LivingEntity e = (LivingEntity) location.getWorld().spawnEntity(location, type, false);
		e.getEquipment().setArmorContents(equipment);
		e.setCustomName(customName);
	}

	public Location getLocation() {
		return location;
	}
}
