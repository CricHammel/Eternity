package de.cric_hammel.eternity.infinity.items.kree;

import org.bukkit.Material;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.cric_hammel.eternity.infinity.items.CustomItem;
import de.cric_hammel.eternity.infinity.items.thanos.ElectronCompressedChitauriDagger;

public class XylopMeat extends CustomItem {

	private static XylopMeat instance;
	
	public static XylopMeat getInstance() {
		if (null == instance) {
			synchronized (ElectronCompressedChitauriDagger.class) {
				if (null == instance) {
					instance = new XylopMeat();
				}
			}
		}
		
		return instance;
	}
	
	private XylopMeat() {
		super(Material.ROTTEN_FLESH, Component.text("Xylop Meat", NamedTextColor.GOLD), Component.text("Needs at least 2 stomaches to be digested"));
	}
	
	public static class Listeners implements Listener {
		
		@EventHandler
		public void onPlayerEat(PlayerItemConsumeEvent event) {
			
			if (!XylopMeat.getInstance().isItem(event.getItem())) {
				return;
			}
			
			Player p = event.getPlayer();
			
			p.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, 10*20, 0));
			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 10*20, 1));
		}
	}
}
