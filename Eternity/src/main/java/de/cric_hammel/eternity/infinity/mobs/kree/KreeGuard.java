package de.cric_hammel.eternity.infinity.mobs.kree;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Mob;
import org.bukkit.inventory.ItemStack;

import de.cric_hammel.eternity.infinity.items.kree.KreeArmor;
import de.cric_hammel.eternity.infinity.mobs.CustomMob;

public class KreeGuard extends Kree {

	public KreeGuard() {
		super(EntityType.IRON_GOLEM, "Guard", null);
	}
	
	@Override
	public Mob spawn(Location loc) {
		IronGolem golem = (IronGolem) super.spawn(loc);
		ItemStack[] armor = new KreeArmor().getTierThree();
		CustomMob.setArmor(golem, armor, 0.005f);
		ItemStack sword = new ItemStack(Material.NETHERITE_SWORD);
		sword.addEnchantment(Enchantment.DAMAGE_ALL, 5);
		sword.addEnchantment(Enchantment.KNOCKBACK, 2);
		CustomMob.setMainHand(golem, sword, 0);
		golem.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(120);
		golem.setHealth(120);
		return golem;
	}
}
