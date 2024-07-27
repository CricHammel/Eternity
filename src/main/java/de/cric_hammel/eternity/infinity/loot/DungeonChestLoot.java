package de.cric_hammel.eternity.infinity.loot;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.io.FileUtils;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.cric_hammel.eternity.Main;
import de.cric_hammel.eternity.infinity.items.misc.InfiniCoin;
import de.cric_hammel.eternity.infinity.items.misc.teleport.TeleportCapsule;
import de.cric_hammel.eternity.infinity.items.misc.teleport.TwelveTeraVoltBattery;

public class DungeonChestLoot implements CustomLootTable {

	private File lootFile;

	public DungeonChestLoot(String fileName) {
		// TODO: Overhaul file system (Copy whole folder in Main etc.)
		lootFile = new File(Main.getLootPath(), fileName);

		if (!lootFile.exists()) {

			try {
				FileUtils.copyInputStreamToFile(Main.getPlugin().getResource(fileName), lootFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void generateLoot(Inventory inventory, Random random) {
		ItemStack[] loot = new ItemStack[27];
		YamlConfiguration config = YamlConfiguration.loadConfiguration(lootFile);
		ConfigurationSection lootSection = config.getConfigurationSection("Loot");
		int minCount = config.getInt("minSlots");
		int maxCount = config.getInt("maxSlots");
		int count = random.nextInt(maxCount - minCount + 1) + minCount;
		List<Integer> slots = IntStream.range(0, 27).boxed().collect(Collectors.toList());
		Collections.shuffle(slots, random);

		for (int i = 0; i < slots.size() && i < count; i++) {
			int slot = slots.get(i);
			loot[slot] = selectRandomLootItem(lootSection, random);
		}

		inventory.setContents(loot);
	}

	private static ItemStack selectRandomLootItem(ConfigurationSection section, Random random) {
		Map<String, Map<String, Object>> lootTable = convertToMapOfMaps(section);
	    int totalWeight = lootTable.values()
	                               .stream()
	                               .mapToInt(item -> (int) item.get("weight"))
	                               .sum();
	    int selectedWeight = random.nextInt(totalWeight);
	    int currentWeight = 0;

	    for (Map.Entry<String, Map<String, Object>> entry : lootTable.entrySet()) {
	        int weight = (int) entry.getValue().get("weight");

	        if (selectedWeight >= currentWeight && selectedWeight < currentWeight + weight) {
	        	int minCount = (int) entry.getValue().get("minCount");
	        	int maxCount = (int) entry.getValue().get("maxCount");
	        	int count = random.nextInt(minCount, maxCount + 1);
	            String itemString = entry.getKey();

	            if (itemString.equals("ENCHANTED_BOOK_PROT")) {
					ItemStack book = new ItemStack(Material.ENCHANTED_BOOK, count);
					book.addUnsafeEnchantment(Enchantment.PROTECTION, 5);
					return book;
				} else if (itemString.equals("COIN")) {
	            	ItemStack coin = InfiniCoin.getInstance().getItem();
	            	coin.setAmount(count);
	            	return coin;
	            } else if (itemString.equals("CAPSULE")) {
	            	ItemStack capsule = TeleportCapsule.getInstance().getItem();
	            	capsule.setAmount(count);
	            	return capsule;
	            } else if (itemString.equals("BATTERY")) {
	            	ItemStack battery = TwelveTeraVoltBattery.getInstance().getItem();
	            	battery.setAmount(count);
	            	return battery;
	            }

				Material m;

				try {
					m = Material.valueOf(itemString);
				} catch (Exception e) {
					m = Material.AIR;
				}

				return new ItemStack(m, count);
	        }
	        currentWeight += weight;
	    }

	    return null;
	}

	private static Map<String, Map<String, Object>> convertToMapOfMaps(ConfigurationSection section) {
        Map<String, Map<String, Object>> resultMap = new HashMap<>();

        if (section != null) {
            for (String key : section.getKeys(false)) {
                ConfigurationSection subsection = section.getConfigurationSection(key);
                Map<String, Object> subMap = new HashMap<>();

                if (subsection != null) {
                    for (String subKey : subsection.getKeys(false)) {
                        subMap.put(subKey, subsection.get(subKey));
                    }
                }

                resultMap.put(key, subMap);
            }
        }

        return resultMap;
    }

}
