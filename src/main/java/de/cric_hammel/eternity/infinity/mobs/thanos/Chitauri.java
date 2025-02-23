package de.cric_hammel.eternity.infinity.mobs.thanos;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.cric_hammel.eternity.infinity.items.thanos.ElectronCompressedChitauriDagger;

public class Chitauri extends ThanosFollower {

	private static Chitauri instance;

	public static Chitauri getInstance() {
		if (null == instance) {
			synchronized (Chitauri.class) {
				if (null == instance) {
					instance = new Chitauri();
				}
			}
		}
		
		return instance;
	}
	
	private Chitauri() {
		super(EntityType.HUSK, ChatColor.GOLD + "Chitauri-Soldier");
	}

	@Override
	public Mob spawn(Location loc) {
		Mob m = super.spawn(loc);
		m.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 10*20, 0, false));
		super.setMainHand(m, ElectronCompressedChitauriDagger.getInstance().getItem(), 0.00784f);
		super.setArmor(m, new ItemStack[]{new ItemStack(Material.NETHERITE_BOOTS), new ItemStack(Material.NETHERITE_LEGGINGS), new ItemStack(Material.NETHERITE_CHESTPLATE), new ItemStack(Material.NETHERITE_HELMET)}, 0);
		return m;
	}

	public void disable(Mob m) {

		if (!isMob(m)) {
			return;
		}

		m.setAI(false);
	}
}
