package plus.crates.Handlers;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import plus.crates.Crate;
import plus.crates.CratesPlus;
import plus.crates.Key;
import plus.crates.Opener.Opener;

import java.util.*;

public class CrateHandler {
	private CratesPlus cratesPlus;
	private Random rand = new Random();
	private HashMap<UUID, Opener> openings = new HashMap<>();
	private HashMap<UUID, HashMap<String, Integer>> pendingKeys = new HashMap<>();

	public CrateHandler(CratesPlus cratesPlus) {
		this.cratesPlus = cratesPlus;
	}

	public int randInt(int min, int max) {
		return rand.nextInt((max - min) + 1) + min;
	}

	public double randDouble(double min, double max) {
		double range = max - min;
		double scaled = rand.nextDouble() * range;
		return scaled + min; // == (rand.nextDouble() * (max-min)) + min;
	}

	private Color getColor(int i) {
		Color c;
		switch (i) {
			case 1:
				c = Color.AQUA;
				break;
			case 2:
				c = Color.BLACK;
				break;
			case 3:
				c = Color.BLUE;
				break;
			case 4:
				c = Color.FUCHSIA;
				break;
			case 5:
				c = Color.GRAY;
				break;
			case 6:
				c = Color.GREEN;
				break;
			case 7:
				c = Color.LIME;
				break;
			case 8:
				c = Color.MAROON;
				break;
			case 9:
				c = Color.NAVY;
				break;
			case 10:
				c = Color.OLIVE;
				break;
			case 11:
				c = Color.ORANGE;
				break;
			case 12:
				c = Color.PURPLE;
				break;
			case 13:
				c = Color.RED;
				break;
			case 14:
				c = Color.SILVER;
				break;
			case 15:
				c = Color.TEAL;
				break;
			case 16:
				c = Color.WHITE;
				break;
			case 17:
				c = Color.YELLOW;
				break;
			default:
				c = Color.AQUA;
				break;
		}
		return c;
	}

	public void spawnFirework(Location location) {
		Firework fw = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
		FireworkMeta fwm = fw.getFireworkMeta();
		Random r = new Random();
		int rt = r.nextInt(4) + 1;
		FireworkEffect.Type type = FireworkEffect.Type.BALL;
		if (rt == 1) type = FireworkEffect.Type.BALL;
		if (rt == 2) type = FireworkEffect.Type.BALL_LARGE;
		if (rt == 3) type = FireworkEffect.Type.BURST;
		if (rt == 4) type = FireworkEffect.Type.CREEPER;
		if (rt == 5) type = FireworkEffect.Type.STAR;
		int r1i = r.nextInt(17) + 1;
		int r2i = r.nextInt(17) + 1;
		Color c1 = getColor(r1i);
		Color c2 = getColor(r2i);
		FireworkEffect effect = FireworkEffect.builder().flicker(r.nextBoolean()).withColor(c1).withFade(c2).with(type).trail(r.nextBoolean()).build();
		fwm.addEffect(effect);
		int rp = r.nextInt(2) + 1;
		fwm.setPower(rp);
		fw.setFireworkMeta(fwm);
	}

	public void giveCrateKey(Player player) {
		Set<String> crates = cratesPlus.getConfig().getConfigurationSection("Crates").getKeys(false);
		Integer random = randInt(0, crates.size() - 1);
		String crateType = "";
		Integer i = 0;
		for (String crate : crates) {
			if (i.equals(random)) {
				crateType = crate;
				break;
			}
			i++;
		}
		giveCrateKey(player, crateType);
	}

	public void giveCrateKey(Player player, String crateType) {
		giveCrateKey(player, crateType, 1);
	}

	public void giveCrateKey(Player player, String crateType, Integer amount) {
		if (player == null || !player.isOnline()) return;
		if (crateType == null) {
			giveCrateKey(player);
			return;
		}
		Crate crate = cratesPlus.getConfigHandler().getCrates().get(crateType.toLowerCase());

		//If the crate does not exist, tell the user
		//This also prevents nullpointerexceptions beeing thrown
		if (crate == null) {
			player.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.RED + "Crate type: '" + crateType + "' does not exist");
			return;
		}

		Key key = crate.getKey();

		//Show error message about the config file missing a Crates.<crateType>.Key field
		if (key == null) {
			player.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.RED + "Could not get key for crate: '" + crateType + "'");
			player.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.RED + "Make sure your config file is correct");
			return;
		}

		//if (player.getInventory().firstEmpty() == -1) {
		//	// Add key to claim
		//	HashMap<String, Integer> keys = new HashMap<String, Integer>();
		//	if( CrateHandler.hasPendingKeys(player.getUniqueId()))
		//		keys = CrateHandler.getPendingKey(player.getUniqueId());
		//	if( keys.containsKey(crateType) )
		//		amount = amount + keys.get(crateType);
		//	keys.put(crateType, amount);
		//	CrateHandler.pendingKeys.put(player.getUniqueId(), keys);
		//	player.sendMessage(ChatColor.GREEN + "Do /crate claim");
		//	return;
		//}
		ItemStack keyItem = key.getKeyItem(amount);
		player.getInventory().addItem(keyItem);
		player.sendMessage(cratesPlus.getPluginPrefix() + cratesPlus.getMessageHandler().getMessage("Key Given", player, crate, null));
	}

	@Deprecated
	public void giveCrate(Player player, String crateType) {
		Crate crate = cratesPlus.getConfigHandler().getCrates().get(crateType.toLowerCase());
		if (crate == null)
			return;
		giveCrate(player, crate);
	}

	public void giveCrate(Player player, Crate crate) {
		if (player == null || !player.isOnline() || crate == null) return;

		ItemStack crateItem = new ItemStack(crate.getBlock());
		ItemMeta crateMeta = crateItem.getItemMeta();
		crateMeta.setDisplayName(crate.getName(true) + " Crate!");
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.GRAY + "Place this crate somewhere!");
		lore.add("");
		crateMeta.setLore(lore);
		crateItem.setItemMeta(crateMeta);
		player.getInventory().addItem(crateItem);
		player.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.GREEN + "You have been given a " + crate.getName(true) + ChatColor.GREEN + " crate");
	}

	@Deprecated
	public ItemStack stringToItemstackOld(String i) {
		String[] args = i.split(":", -1);
		if (args.length >= 2 && args[0].equalsIgnoreCase("command")) {
			/** Commands */
			String command = args[1];
			String title = "Command: /" + command;
			if (args.length == 3) {
				title = args[2];
			}
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
			ItemStack itemStack = new ItemStack(Material.EMPTY_MAP);
			ItemMeta itemMeta = itemStack.getItemMeta();
			itemMeta.setDisplayName(ChatColor.RESET + title);
			itemStack.setItemMeta(itemMeta);
			return itemStack;
		} else if (args.length == 1) {
			/** Item without any amounts or enchantments */
			String[] args1 = args[0].split("-");
			ItemStack itemStack;
			if (args1.length == 1) {
				itemStack = new ItemStack(Material.getMaterial(args1[0].toUpperCase()));
			} else {
				itemStack = new ItemStack(Material.getMaterial(args1[0].toUpperCase()), 1, Byte.parseByte(args1[1]));
			}
			return itemStack;
		} else if (args.length == 2) {
			return new ItemStack(Material.getMaterial(args[0].toUpperCase()), Integer.parseInt(args[1]));
		} else if (args.length == 3) {
			String[] enchantments = args[2].split("\\|", -1);
			ItemStack itemStack = new ItemStack(Material.getMaterial(args[0]), Integer.parseInt(args[1]));
			for (String e : enchantments) {
				String[] args1 = e.split("-", -1);
				if (args1.length == 1) {
					try {
						itemStack.addUnsafeEnchantment(Enchantment.getByName(args1[0]), 1);
					} catch (Exception ignored) {
					}
				} else if (args1.length == 2) {
					try {
						itemStack.addUnsafeEnchantment(Enchantment.getByName(args1[0]), Integer.parseInt(args1[1]));
					} catch (Exception ignored) {
					}
				}
			}
			return itemStack;
		}
		return null;
	}

	public ItemStack stringToItemstack(String i, Player player, boolean isWin) {
		try {
			String[] args = i.split(":", -1);
			if (args.length >= 2 && args[0].equalsIgnoreCase("command")) {
				String name = "Command";
				String commands;
				if (args.length >= 3 && !args[1].equalsIgnoreCase("NONE")) {
					name = ChatColor.translateAlternateColorCodes('&', args[1]);
					commands = args[2];
				} else {
					commands = args[1];
				}

				if (isWin) {
					/** Do Commands */
					String[] args1 = commands.split("\\|");
					for (String command : args1) {
						player.sendMessage(command);
						command = command.replaceAll("%name%", player.getName());
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
					}
				}

				ItemStack itemStack = new ItemStack(Material.EMPTY_MAP);
				ItemMeta itemMeta = itemStack.getItemMeta();
				List<String> lore = new ArrayList<String>();
				lore.add(ChatColor.DARK_GRAY + "Crate Command");
				itemMeta.setLore(lore);
				itemMeta.setDisplayName(ChatColor.RESET + name);
				itemStack.setItemMeta(itemMeta);
				return itemStack;
			} else if (args.length == 1) {
				/** Item without any amounts, custom name or enchantments */
				String[] args1 = args[0].split("-");
				ItemStack itemStack;
				if (args1.length == 1) {
					itemStack = new ItemStack(Material.getMaterial(args1[0].toUpperCase()));
				} else {
					itemStack = new ItemStack(Material.getMaterial(args1[0].toUpperCase()), 1, Byte.parseByte(args1[1]));
				}
				return itemStack;
			} else if (args.length == 2) {
				String[] args1 = args[0].split("-");
				if (args1.length == 1) {
					return new ItemStack(Material.getMaterial(args1[0].toUpperCase()), Integer.parseInt(args[1]));
				} else {
					return new ItemStack(Material.getMaterial(args1[0].toUpperCase()), Integer.parseInt(args[1]), Byte.parseByte(args1[1]));
				}
			} else if (args.length == 3) {
				if (args[2].equalsIgnoreCase("NONE")) {
					String[] args1 = args[0].split("-");
					if (args1.length == 1) {
						return new ItemStack(Material.getMaterial(args1[0].toUpperCase()), Integer.parseInt(args[1]));
					} else {
						return new ItemStack(Material.getMaterial(args1[0].toUpperCase()), Integer.parseInt(args[1]), Byte.parseByte(args1[1]));
					}
				} else {
					String[] args1 = args[0].split("-");
					ItemStack itemStack;
					if (args1.length == 1) {
						itemStack = new ItemStack(Material.getMaterial(args1[0].toUpperCase()), Integer.parseInt(args[1]));
					} else {
						itemStack = new ItemStack(Material.getMaterial(args1[0].toUpperCase()), Integer.parseInt(args[1]), Byte.parseByte(args1[1]));
					}
					ItemMeta itemMeta = itemStack.getItemMeta();
					itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', args[2]));
					itemStack.setItemMeta(itemMeta);
					return itemStack;
				}
			} else if (args.length == 4) {
				String[] enchantments = args[3].split("\\|", -1);
				String[] args1 = args[0].split("-");
				ItemStack itemStack;
				if (args1.length == 1) {
					itemStack = new ItemStack(Material.getMaterial(args1[0].toUpperCase()), Integer.parseInt(args[1]));
				} else {
					itemStack = new ItemStack(Material.getMaterial(args1[0].toUpperCase()), Integer.parseInt(args[1]), Byte.parseByte(args1[1]));
				}
				for (String e : enchantments) {
					args1 = e.split("-", -1);
					if (args1.length == 1) {
						try {
							itemStack.addUnsafeEnchantment(Enchantment.getByName(args1[0]), 1);
						} catch (Exception ignored) {
						}
					} else if (args1.length == 2) {
						try {
							itemStack.addUnsafeEnchantment(Enchantment.getByName(args1[0]), Integer.parseInt(args1[1]));
						} catch (Exception ignored) {
						}
					}
				}
				if (!args[2].equalsIgnoreCase("NONE")) {
					ItemMeta itemMeta = itemStack.getItemMeta();
					itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', args[2]));
					itemStack.setItemMeta(itemMeta);
				}
				return itemStack;
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	public String itemstackToString(ItemStack itemStack) {
		if (itemStack == null || itemStack.getType() == Material.AIR)
			return null;

		String finalString = "";
		finalString = finalString + itemStack.getType().toString();
		if (itemStack.getData().getData() != 0) {
			finalString = finalString + "-" + itemStack.getData().getData();
		}
		finalString = finalString + ":" + itemStack.getAmount();

		if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName()) {
			finalString = finalString + ":" + itemStack.getItemMeta().getDisplayName();
		} else {
			finalString = finalString + ":NONE";
		}

		int i = 0;
		for (Map.Entry<Enchantment, Integer> entry : itemStack.getEnchantments().entrySet()) {
			Enchantment enchantment = entry.getKey();
			Integer level = entry.getValue();
			if (i == 0) {
				finalString = finalString + ":";
			} else {
				finalString = finalString + "|";
			}
			if (level > 1) {
				finalString = finalString + enchantment.getName().toUpperCase() + "-" + level;
			} else {
				finalString = finalString + enchantment.getName().toUpperCase();
			}
			i++;
		}

		return finalString;
	}

	public HashMap<UUID, Opener> getOpenings() {
		return openings;
	}

	public boolean hasOpening(UUID uuid) {
		return getOpening(uuid) != null;
	}

	public Opener getOpening(UUID uuid) {
		if (openings.containsKey(uuid))
			return openings.get(uuid);
		return null;
	}

	public void addOpening(UUID uuid, Opener opener) {
		openings.put(uuid, opener);
	}

	public void removeOpening(UUID uuid) {
		if (hasOpening(uuid))
			openings.remove(uuid);
	}

	public HashMap<UUID, HashMap<String, Integer>> getPendingKeys() {
		return pendingKeys;
	}

	public HashMap<String, Integer> getPendingKey(UUID uuid) {
		if (!hasPendingKeys(uuid))
			return null;
		return pendingKeys.get(uuid);
	}

	public boolean hasPendingKeys(UUID uuid) {
		return pendingKeys.containsKey(uuid);
	}

}
