package de.cric_hammel.eternity.infinity.mobs;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

public abstract class CustomMob {

	private final EntityType type;
	private final String name;

	public CustomMob(EntityType type, String name) {
		this.type = type;
		this.name = name;
	}

	public Mob spawn(Location loc) {
		Mob m = (Mob) loc.getWorld().spawnEntity(loc, type, false);
		m.setCustomName(name);
		m.setPersistent(true);
		m.setRemoveWhenFarAway(false);
		m.setLootTable(null);
		return m;
	}

	public boolean isMob(Entity e) {
		
		if (e != null && e.getType() == type && e.getCustomName() != null && e.getCustomName().equals(name)) {
			return true;
		}

		return false;
	}

	public static void setArmor(Mob m, ItemStack[] armor, float dropChance) {
		EntityEquipment e = m.getEquipment();
		e.setArmorContents(armor);
		e.setHelmetDropChance(dropChance);
		e.setChestplateDropChance(dropChance);
		e.setLeggingsDropChance(dropChance);
		e.setBootsDropChance(dropChance);
	}

	public static void setMainHand(Mob m, ItemStack item, float dropChance) {
		EntityEquipment e = m.getEquipment();
		e.setItemInMainHand(item);
		e.setItemInMainHandDropChance(dropChance);
	}
}
