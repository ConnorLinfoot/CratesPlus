package plus.crates.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import plus.crates.Crate;
import plus.crates.CratesPlus;
import plus.crates.Opener.Opener;
import plus.crates.Utils.ReflectionUtil;
import plus.crates.Utils.SignInputHandler;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class CrateCommand implements CommandExecutor {
	private CratesPlus cratesPlus;

	public CrateCommand(CratesPlus cratesPlus) {
		this.cratesPlus = cratesPlus;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String string, String[] args) {

		if (sender instanceof Player && !sender.hasPermission("cratesplus.admin")) {
			//if (args.length == 0 || (args.length > 0 && args[0].equalsIgnoreCase("claim"))) {
			//	// Assume player and show "claim" GUI
			//	doClaim((Player) sender);
			//	return true;
			//}
			sender.sendMessage(cratesPlus.getPluginPrefix() + cratesPlus.getMessageHandler().getMessage("Command No Permission", (Player) sender, null, null));
			return false;
		}

		//if (args.length >= 1 && args[0].equalsIgnoreCase("claim")) {
		//	if (!(sender instanceof Player)) {
		//		sender.sendMessage(CratesPlus.getPluginPrefix() + ChatColor.RED + "This command must be ran as a player");
		//		return false;
		//	}
		//	doClaim((Player) sender);
		//	return true;
		//}

		if (args.length >= 1) {
			switch (args[0].toLowerCase()) {
				default:
					sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.RED + "Unknown arg");
					break;
				case "opener":
				case "openers":
					if (args.length > 1) {
						if (args.length < 3) {
							sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.RED + "Correct usage: /" + string + " " + args[0] + " <crate> <opener>");
						} else {
							if (args[1].equalsIgnoreCase("default")) {
								if (CratesPlus.getOpenHandler().openerExist(args[2])) {
									CratesPlus.getOpenHandler().setDefaultOpener(args[2]);
									sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.GREEN + "Set default opener to " + args[2]);
								} else {
									sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.RED + "No opener is registered with that name");
								}
							} else {
								if (CratesPlus.getOpenHandler().openerExist(args[2])) {
									if (cratesPlus.getConfigHandler().getCrate(args[1]) == null) {
										sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.RED + "No crate exists with that name");
									} else {
										cratesPlus.getConfigHandler().getCrate(args[1]).setOpener(args[2]);
										sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.GREEN + "Set opener to " + args[2]);
									}
								} else {
									sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.RED + "No opener is registered with that name");
								}
							}
						}

					} else {
						sender.sendMessage(ChatColor.GOLD + "Registered Openers:");
						sender.sendMessage(ChatColor.AQUA + "Name" + ChatColor.GRAY + " | " + ChatColor.YELLOW + "Plugin");
						sender.sendMessage(ChatColor.AQUA + "");
						for (Map.Entry<String, Opener> map : CratesPlus.getOpenHandler().getRegistered().entrySet()) {
							sender.sendMessage(ChatColor.AQUA + map.getKey() + ChatColor.GRAY + " | " + ChatColor.YELLOW + map.getValue().getPlugin().getDescription().getName());
						}
					}
					break;
				case "reload":
					cratesPlus.reloadPlugin();
					sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.GREEN + "CratesPlus configuration was reloaded - This feature is not fully supported and may not work correctly");
					break;
				case "settings":
					if (!(sender instanceof Player)) {
						sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.RED + "This command must be ran as a player");
						return false;
					}
					cratesPlus.getSettingsHandler().openSettings((Player) sender);
					break;
				case "create":
				case "createbeta":
					if (args[0].equalsIgnoreCase("createbeta") && false) { // TODO Bring back in 4.2 ;)
						// Lets try and open a sign to do the name! :D
						Player player = (Player) sender;
						try {
							Constructor signConstructor = ReflectionUtil.getNMSClass("PacketPlayOutOpenSignEditor").getConstructor(ReflectionUtil.getNMSClass("BlockPosition"));
							Object packet = signConstructor.newInstance(getBlockPosition(player));
							SignInputHandler.injectNetty(player);
							sendPacket(player, packet);
						} catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
							e.printStackTrace();
						}

						return true;
					}
					if (args.length < 2) {
						sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.RED + "Correct Usage: /crate create <name>");
						return false;
					}

					String name = args[1];
					FileConfiguration config = cratesPlus.getConfig();
					if (config.isSet("Crates." + name)) {
						sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.RED + name + " crate already exists");
						return false;
					}

					// Setup example item
					config.set("Crates." + name + ".Winnings.1.Type", "ITEM");
					config.set("Crates." + name + ".Winnings.1.Item Type", "IRON_SWORD");
					config.set("Crates." + name + ".Winnings.1.Item Data", 0);
					config.set("Crates." + name + ".Winnings.1.Percentage", 0);
					config.set("Crates." + name + ".Winnings.1.Name", "&6&lExample Sword");
					config.set("Crates." + name + ".Winnings.1.Amount", 1);

					// Setup key with defaults
					config.set("Crates." + name + ".Key.Item", "TRIPWIRE_HOOK");
					config.set("Crates." + name + ".Key.Name", "%type% Crate Key");
					config.set("Crates." + name + ".Key.Enchanted", true);

					config.set("Crates." + name + ".Knockback", 0.0);
					config.set("Crates." + name + ".Broadcast", false);
					config.set("Crates." + name + ".Firework", false);
					config.set("Crates." + name + ".Preview", true);
					config.set("Crates." + name + ".Block", "CHEST");
					config.set("Crates." + name + ".Color", "WHITE");
					cratesPlus.saveConfig();
					cratesPlus.reloadConfig();

					cratesPlus.getConfigHandler().getCrates().put(name.toLowerCase(), new Crate(name, cratesPlus));
					cratesPlus.getSettingsHandler().setupCratesInventory();

					sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.GREEN + name + " crate has been created");
					break;
				case "rename":
					if (args.length < 3) {
						sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.RED + "Correct Usage: /crate rename <old name> <new name>");
						return false;
					}

					String oldName = args[1];
					String newName = args[2];

					if (!cratesPlus.getConfigHandler().getCrates().containsKey(oldName.toLowerCase())) {
						sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.RED + oldName + " crate was not found");
						return false;
					}
					Crate crate = cratesPlus.getConfigHandler().getCrates().get(oldName.toLowerCase());

					config = cratesPlus.getConfig();
					if (config.isSet("Crates." + newName)) {
						sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.RED + newName + " crate already exists");
						return false;
					}

					for (String id : cratesPlus.getConfig().getConfigurationSection("Crates." + crate.getName(false) + ".Winnings").getKeys(false)) {
						String path = "Crates." + crate.getName(false) + ".Winnings." + id;
						String newPath = "Crates." + newName + ".Winnings." + id;

						if (config.isSet(path + ".Type"))
							config.set(newPath + ".Type", config.getString(path + ".Type"));
						if (config.isSet(path + ".Item Type"))
							config.set(newPath + ".Item Type", config.getString(path + ".Item Type"));
						if (config.isSet(path + ".Item Data"))
							config.set(newPath + ".Item Data", config.getInt(path + ".Item Data"));
						if (config.isSet(path + ".Percentage"))
							config.set(newPath + ".Percentage", config.getDouble(path + ".Percentage"));
						if (config.isSet(path + ".Name"))
							config.set(newPath + ".Name", config.getString(path + ".Name"));
						if (config.isSet(path + ".Amount"))
							config.set(newPath + ".Amount", config.getInt(path + ".Amount"));
						if (config.isSet(path + ".Enchantments"))
							config.set(newPath + ".Enchantments", config.getList(path + ".Enchantments"));
						if (config.isSet(path + ".Commands"))
							config.set(newPath + ".Commands", config.getList(path + ".Commands"));
					}

					config.set("Crates." + newName + ".Knockback", config.getDouble("Crates." + crate.getName(false) + ".Knockback"));
					if (config.isSet("Crates." + crate.getName(false) + ".Block"))
						config.set("Crates." + newName + ".Block", config.getString("Crates." + crate.getName(false) + ".Block"));
					if (config.isSet("Crates." + crate.getName(false) + ".Color"))
						config.set("Crates." + newName + ".Color", config.getString("Crates." + crate.getName(false) + ".Color"));
					if (config.isSet("Crates." + crate.getName(false) + ".Knockback"))
						config.set("Crates." + newName + ".Knockback", config.getDouble("Crates." + crate.getName(false) + ".Knockback"));
					if (config.isSet("Crates." + crate.getName(false) + ".Broadcast"))
						config.set("Crates." + newName + ".Broadcast", config.getBoolean("Crates." + crate.getName(false) + ".Broadcast"));
					if (config.isSet("Crates." + crate.getName(false) + ".Firework"))
						config.set("Crates." + newName + ".Firework", config.getBoolean("Crates." + crate.getName(false) + ".Firework"));
					if (config.isSet("Crates." + crate.getName(false) + ".Preview"))
						config.set("Crates." + newName + ".Preview", config.getBoolean("Crates." + crate.getName(false) + ".Preview"));

					config.set("Crates." + crate.getName(false), null);
					cratesPlus.saveConfig();
					cratesPlus.reloadConfig();

					cratesPlus.getConfigHandler().getCrates().remove(oldName.toLowerCase());
					cratesPlus.getConfigHandler().getCrates().put(newName.toLowerCase(), new Crate(newName, cratesPlus));
					cratesPlus.getSettingsHandler().setupCratesInventory();

					sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.GREEN + oldName + " has been renamed to " + newName);
					break;
				case "delete":
					if (args.length < 2) {
						sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.RED + "Correct Usage: /crate delete <name>");
						return false;
					}

					name = args[1];
					config = cratesPlus.getConfig();
					if (!config.isSet("Crates." + name)) {
						sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.RED + name + " crate doesn't exist");
						return false;
					}

					config.set("Crates." + name, null);
					cratesPlus.saveConfig();
					cratesPlus.reloadConfig();
					cratesPlus.getConfigHandler().getCrates().remove(name.toLowerCase());
					cratesPlus.getSettingsHandler().setupCratesInventory();

					sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.GREEN + name + " crate has been deleted");
					break;
				case "key":
					if (args.length < 2) {
						sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.RED + "Correct Usage: /crate key <player/all> [type] [amount]");
						return false;
					}

					Integer amount = 1;
					if (args.length > 3) {
						try {
							amount = Integer.parseInt(args[3]);
						} catch (Exception ignored) {
							sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.RED + "Invalid amount");
							return false;
						}
					}

					Player player = null;
					if (!args[1].equalsIgnoreCase("all")) {
						player = Bukkit.getPlayer(args[1]);
						if (player == null) {
							sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.RED + "The player " + args[1] + " was not found");
							return false;
						}
					}

					String crateType = null;
					if (args.length >= 3) {
						crateType = args[2];
					}

					if (crateType != null) {

						if (cratesPlus.getConfigHandler().getCrates().get(crateType.toLowerCase()) == null) {
							sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.RED + "Crate not found");
							return false;
						}

						if (player == null) {
							for (Player p : Bukkit.getOnlinePlayers()) {
								cratesPlus.getCrateHandler().giveCrateKey(p, crateType, amount);
							}
						} else {
							cratesPlus.getCrateHandler().giveCrateKey(player, crateType, amount);
						}
					} else {
						if (player == null) {
							for (Player p : Bukkit.getOnlinePlayers()) {
								cratesPlus.getCrateHandler().giveCrateKey(p);
							}
						} else {
							cratesPlus.getCrateHandler().giveCrateKey(player);
						}
					}

					if (player == null) {
						sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.GREEN + "Given all players a crate key");
					} else {
						sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.GREEN + "Given " + player.getDisplayName() + ChatColor.RESET + ChatColor.GREEN + " a crate key");
					}
					break;
				case "crate":
					if (args.length == 1) {
						sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.RED + "Correct Usage: /crate crate <type> [player]");
						return false;
					}

					if (args.length == 3) {
						player = Bukkit.getPlayer(args[2]);
					} else if (sender instanceof Player) {
						player = (Player) sender;
					} else {
						sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.RED + "Correct Usage: /crate crate <type> [player]");
						return false;
					}

					if (player == null) {
						sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.RED + "The player " + args[2] + " was not found");
						return false;
					}

					try {
						crateType = args[1];
					} catch (IllegalArgumentException e) {
						sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.RED + "Please specify a valid crate type");
						return false;
					}

					if (cratesPlus.getConfigHandler().getCrates().get(crateType.toLowerCase()) == null) {
						sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.RED + "Crate not found");
						return false;
					}

					cratesPlus.getCrateHandler().giveCrate(player, crateType);

					sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.GREEN + "Given " + player.getDisplayName() + ChatColor.RESET + ChatColor.GREEN + " a crate");
					break;
			}
		} else {

			// Help Messages
			sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.AQUA + "----- CratePlus v" + cratesPlus.getDescription().getVersion() + " Help -----");
			sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.AQUA + "/crate reload " + ChatColor.YELLOW + "- Reload configuration for CratesPlus (Experimental)");
			sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.AQUA + "/crate settings " + ChatColor.YELLOW + "- Edit settings of CratesPlus and crate winnings");
			sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.AQUA + "/crate create <name> " + ChatColor.YELLOW + "- Create a new crate");
			sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.AQUA + "/crate rename <old name> <new name> " + ChatColor.YELLOW + "- Rename a new crate");
			sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.AQUA + "/crate delete <name> " + ChatColor.YELLOW + "- Delete a crate");
			sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.AQUA + "/crate key <player/all> [type] [amount] " + ChatColor.YELLOW + "- Give player a random crate key");
			sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.AQUA + "/crate crate <type> [player] " + ChatColor.YELLOW + "- Give player a crate to be placed");
			sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.AQUA + "/crate opener <crate/default> <opener> " + ChatColor.YELLOW + "- Change the opener for a crate (or the default)");

		}

		return true;
	}

	private void doClaim(Player player) {
		if (!cratesPlus.getCrateHandler().hasPendingKeys(player.getUniqueId())) {
			// TODO Send message
			return;
		}
		Integer size = cratesPlus.getCrateHandler().getPendingKey(player.getUniqueId()).size();
		if (size < 9)
			size = 9;
		else if (size <= 18)
			size = 18;
		else if (size <= 27)
			size = 27;
		else if (size <= 36)
			size = 36;
		else if (size <= 45)
			size = 45;
		else
			size = 54;
		Inventory inventory = Bukkit.createInventory(null, size, "Claim Crate Keys");
		Integer i = 0;
		for (Map.Entry<String, Integer> map : cratesPlus.getCrateHandler().getPendingKey(player.getUniqueId()).entrySet()) {
			String crateName = map.getKey();
			Integer amount = map.getValue();
			Crate crate = cratesPlus.getConfigHandler().getCrates().get(crateName.toLowerCase());
			if (crate == null)
				return; // Crate must have been removed?
			ItemStack keyItem = crate.getKey().getKeyItem(amount);
			inventory.setItem(i, keyItem);
			i++;
		}
		player.openInventory(inventory);
	}

	public Object getBlockPosition(Player player) {
		try {
			Object handle = player.getClass().getMethod("getHandle").invoke(player);
			Constructor constructor = ReflectionUtil.getNMSClass("BlockPosition").getConstructor(ReflectionUtil.getNMSClass("Entity"));
			return constructor.newInstance(handle);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void sendPacket(Player player, Object packet) {
		try {
			Object handle = player.getClass().getMethod("getHandle").invoke(player);
			Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
			playerConnection.getClass().getMethod("sendPacket", ReflectionUtil.getNMSClass("Packet")).invoke(playerConnection, packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
