package de.cric_hammel.eternity.infinity.worlds;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import de.cric_hammel.eternity.infinity.items.CustomItem;
import de.cric_hammel.eternity.infinity.items.keys.PowerDungeonKey;
import de.cric_hammel.eternity.infinity.items.kree.KreeArmor;
import de.cric_hammel.eternity.infinity.items.misc.InterdimensionalShears;
import de.cric_hammel.eternity.infinity.items.misc.PocketAnvil;
import de.cric_hammel.eternity.infinity.items.misc.teleport.TeleportCapsule;
import de.cric_hammel.eternity.infinity.items.misc.teleport.TeleportRailgun;
import de.cric_hammel.eternity.infinity.items.misc.teleport.TwelveTeraVoltBattery;
import de.cric_hammel.eternity.infinity.items.stones.StoneType;
import de.cric_hammel.eternity.infinity.mobs.npc.DialogueNpc;
import de.cric_hammel.eternity.infinity.mobs.npc.DialogueNpc.Dialogue;
import de.cric_hammel.eternity.infinity.mobs.npc.ShopNpc;
import de.cric_hammel.eternity.infinity.mobs.npc.ShopNpc.ShopItem;
import de.cric_hammel.eternity.infinity.worlds.dungeons.DungeonFactory;

public class Lobby extends MultiplayerWorld {

	private static Lobby instance;
	
	private final Map<Block, StoneType> doors = new HashMap<>();

	public static Lobby getInstance() {
		if (null == instance) {
			synchronized (Lobby.class) {
				if (null == instance) {
					instance = new Lobby();
				}
			}
		}
		
		return instance;
	}
	
	private Lobby() {
		super("lobby.txt", "Lobby");
		parser.addAction(Material.WALL_TORCH, (loc, data) -> {
			setDoor(loc, (Directional) data, StoneType.POWER);
		});
		parser.addAction(Material.SOUL_WALL_TORCH, (loc, data) -> {
			setDoor(loc, (Directional) data, StoneType.SPACE);
		});
		parser.addAction(Material.REDSTONE_WALL_TORCH, (loc, data) -> {
			setDoor(loc, (Directional) data, StoneType.REALITY);
		});
		parser.addAction(Material.OAK_WALL_SIGN, (loc, data) -> {
			setDoor(loc, (Directional) data, StoneType.MIND);
		});
		parser.addAction(Material.OAK_WALL_HANGING_SIGN, (loc, data) -> {
			setDoor(loc, (Directional) data, StoneType.SOUL);
		});
		parser.addAction(Material.TRIPWIRE_HOOK, (loc, data) -> {
			setDoor(loc, (Directional) data, StoneType.TIME);
		});
		parser.addAction(Material.MAGENTA_GLAZED_TERRACOTTA, (loc, data) -> {
			ArrayList<ShopItem> items = new ArrayList<>();
			items.add(new ShopItem(ChatColor.RED + "Upgrade Kree Helmet", Material.CHAINMAIL_HELMET, new ArrayList<>(Arrays.asList("5 Tier 1 to Tier 2")), 1, (p) -> {
				return upgradeKreeArmor(p, 1, 3);
			}));
			items.add(new ShopItem(ChatColor.RED + "Upgrade Kree Chestplate", Material.CHAINMAIL_CHESTPLATE, new ArrayList<>(Arrays.asList("5 Tier 1 to Tier 2")), 1, (p) -> {
				return upgradeKreeArmor(p, 1, 2);
			}));
			items.add(new ShopItem(ChatColor.RED + "Upgrade Kree Leggings", Material.CHAINMAIL_LEGGINGS, new ArrayList<>(Arrays.asList("5 Tier 1 to Tier 2")), 1, (p) -> {
				return upgradeKreeArmor(p, 1, 1);
			}));
			items.add(new ShopItem(ChatColor.RED + "Upgrade Kree Boots", Material.CHAINMAIL_BOOTS, new ArrayList<>(Arrays.asList("5 Tier 1 to Tier 2")), 1, (p) -> {
				return upgradeKreeArmor(p, 1, 0);
			}));
			items.add(new ShopItem(ChatColor.RED + "Upgrade Kree Helmet", Material.CHAINMAIL_HELMET, new ArrayList<>(Arrays.asList("5 Tier 2 to Tier 3")), 6, (p) -> {
				return upgradeKreeArmor(p, 2, 3);
			}));
			items.add(new ShopItem(ChatColor.RED + "Upgrade Kree Chestplate", Material.CHAINMAIL_CHESTPLATE, new ArrayList<>(Arrays.asList("5 Tier 2 to Tier 3")), 6, (p) -> {
				return upgradeKreeArmor(p, 2, 2);
			}));
			items.add(new ShopItem(ChatColor.RED + "Upgrade Kree Leggings", Material.CHAINMAIL_LEGGINGS, new ArrayList<>(Arrays.asList("5 Tier 2 to Tier 3")), 6, (p) -> {
				return upgradeKreeArmor(p, 2, 1);
			}));
			items.add(new ShopItem(ChatColor.RED + "Upgrade Kree Boots", Material.CHAINMAIL_BOOTS, new ArrayList<>(Arrays.asList("5 Tier 2 to Tier 3")), 6, (p) -> {
				return upgradeKreeArmor(p, 2, 0);
			}));
			items.add(new ShopItem(ChatColor.RED + "Buy Pocket Anvil", Material.ANVIL, new ArrayList<String>(Arrays.asList("1 Anvil")), 1, (p) -> {
				Inventory inv = p.getInventory();
				ItemStack anvil = new ItemStack(Material.ANVIL);
				
				if (!inv.containsAtLeast(anvil, 1)) {
					return false;
				}
				
				inv.removeItem(anvil);
				inv.addItem(PocketAnvil.getInstance().getItem());
				return true;
			}));
			loc.setYaw(90f);
			new ShopNpc(EntityType.PIGLIN, ChatColor.RED + "Kree Blacksmith", loc, items);
		});
		parser.addAction(Material.PINK_GLAZED_TERRACOTTA, (loc, data) -> {
			ArrayList<ShopItem> items = new ArrayList<>();
			items.add(new ShopItem(ChatColor.GOLD + "Buy Dungeon Key", Material.NAME_TAG, new ArrayList<String>(Arrays.asList("8 Phantom Membrane", "8 Iron Blocks", "1 Emerald Block")), 0, (p) -> {
				return buyDungeonKey(p, StoneType.POWER, new ItemStack(Material.PHANTOM_MEMBRANE, 8), new ItemStack(Material.IRON_BLOCK, 8), new ItemStack(Material.EMERALD_BLOCK, 1));
			}));
			items.add(new ShopItem(ChatColor.GOLD + "Buy Interdimensional Shears", Material.SHEARS, new ArrayList<String>(), 1, (p) -> {
				return p.getInventory().addItem(InterdimensionalShears.getInstance().getItem()).isEmpty();
			}));
			items.add(new ShopItem(ChatColor.GOLD + "Buy Teleport Railgun", Material.GOLDEN_HOE, new ArrayList<String>(Arrays.asList("1 Golden Hoe", "1 12-Teravolt Battery")), 10, (p) -> {
				ItemStack battery = TwelveTeraVoltBattery.getInstance().getItem();
				PlayerInventory inv = p.getInventory();
				
				if (!inv.containsAtLeast(battery, 1)) {
					return false;
				}
				
				inv.removeItem(battery);
				ItemStack gun = TeleportRailgun.getInstance().getItem();
				inv.addItem(gun);
				return true;
			}));
			items.add(new ShopItem(ChatColor.GOLD + "Buy 3 Teleport Capsule", Material.SUNFLOWER, new ArrayList<String>(), 1, (p) -> {
				ItemStack capsule = TeleportCapsule.getInstance().getItem();
				capsule.setAmount(3);
				return p.getInventory().addItem(capsule).isEmpty();
			}));
			loc.setYaw(180f);
			new ShopNpc(EntityType.PIGLIN, ChatColor.RED + "Kree Merchant", loc, items);
		});
		parser.addAction(Material.PURPLE_GLAZED_TERRACOTTA, (loc, data) -> {
			DialogueNpc.Dialogue dialogue = new Dialogue(ChatColor.RED, 3);
			dialogue.add("I");
			dialogue.add("am");
			dialogue.add("warrior!");
			loc.setYaw(135f);
			new DialogueNpc(EntityType.PIGLIN_BRUTE, ChatColor.RED + "Kree Warrior", loc, dialogue);
		});
		parser.addAction(Material.BLUE_GLAZED_TERRACOTTA, (loc, data) -> {
			DialogueNpc.Dialogue dialogue = new Dialogue(ChatColor.RED, 3);
			dialogue.add("I");
			dialogue.add("am");
			dialogue.add("wise!");
			loc.setYaw(180f);
			new DialogueNpc(EntityType.ZOMBIFIED_PIGLIN, ChatColor.RED + "Kree Wise", loc, dialogue);
		});
		super.create();
	}

	private void setDoor(Location loc, Directional dData, StoneType type) {
		Block relative = loc.getBlock().getRelative(dData.getFacing().getOppositeFace());
		doors.put(relative, type);
	}

	private boolean upgradeKreeArmor(Player p, int tier, int piece) {
		KreeArmor kreeArmor = KreeArmor.getInstance();
		ItemStack[] armor = kreeArmor.getTier(tier);
		Inventory inv = p.getInventory();
		ItemStack pieceItem = armor[piece];
		int count = 0;
		List<Integer> slots = new ArrayList<>();

		for (int i = 0; i < inv.getSize(); i++) {
			ItemStack item = inv.getItem(i);

			if (item != null && item.getType() == pieceItem.getType() && item.hasItemMeta() && item.getItemMeta().hasLore() && item.getItemMeta().getLore().equals(pieceItem.getItemMeta().getLore())) {
				count++;
				slots.add(i);
			}
		}

		if (count < 5) {
			return false;
		}

		for (int i = 0; i < 5; i++) {
			inv.setItem(slots.get(i), null);
		}

		inv.addItem(kreeArmor.getTier(tier + 1)[piece]);
		return true;
	}

	private boolean buyDungeonKey(Player p, StoneType type, ItemStack... price) {
		Inventory inv = p.getInventory();

		for (ItemStack item : price) {
			if (!inv.containsAtLeast(item, item.getAmount())) {
				return false;
			}
		}

		// TODO: Add missing dungeon keys
		inv.removeItem(price);
		inv.addItem(PowerDungeonKey.getInstance().getItem());
		return true;
	}

	@Override
	public void delete() {
		ThanosFight.getInstance().stop(false);
		super.delete();
	}

	@Override
	public boolean teleport(Player p) {
		boolean inWorld = super.teleport(p);

		if (super.getWorld().getPlayers().isEmpty()) {
			ThanosFight.getInstance().stop(false);
		}

		return inWorld;
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player p = event.getPlayer();
		Block b = event.getClickedBlock();
		ItemStack item = event.getItem();

		if (b == null || b.getType() != Material.BEDROCK || !doors.containsKey(b)) {
			return;
		}

		CustomItem key;
		StoneType type = doors.get(b);

		// TODO: Add missing dungeons and keys
		switch (type) {
			case POWER:
				key = PowerDungeonKey.getInstance();
				break;
			case SPACE:
				key = null;
				break;
			case REALITY:
				key = null;
				break;
			case MIND:
				key = null;
				break;
			case SOUL:
				key = null;
				break;
			case TIME:
				key = null;
				break;
			default:
				key = null;
				break;
		}

		if (!key.isItem(item)) {
			return;
		}

		p.getInventory().removeItem(key.getItem());
		DungeonFactory.createFromType(p, type);
	}
}
