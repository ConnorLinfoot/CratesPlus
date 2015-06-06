package com.connorlinfoot.cratesplus.Commands;

import com.connorlinfoot.cratesplus.Crate;
import com.connorlinfoot.cratesplus.CratesPlus;
import com.connorlinfoot.cratesplus.Handlers.CrateHandler;
import com.connorlinfoot.cratesplus.Handlers.MessageHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CrateCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String string, String[] args) {
        if (sender instanceof Player && !sender.hasPermission("cratesplus.admin")) {
            sender.sendMessage(CratesPlus.pluginPrefix + MessageHandler.getMessage(CratesPlus.getPlugin(), "Command No Permission", (Player) sender, "Unknown"));
            return false;
        }

        if (args.length >= 1 && args[0].equalsIgnoreCase("reload")) {
            CratesPlus.reloadPlugin();
            sender.sendMessage(CratesPlus.pluginPrefix + ChatColor.GREEN + "CratesPlus configuration was reloaded - This feature is not fully tested and may not work correctly");
            return true;
        }

        if (args.length >= 1 && args[0].equalsIgnoreCase("settings")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(CratesPlus.pluginPrefix + ChatColor.RED + "This command must be ran as a player");
                return false;
            }
            CratesPlus.settingsHandler.openSettings((Player) sender);
            return true;
        }

        if (args.length >= 1 && args[0].equalsIgnoreCase("create")) {
            if (args.length < 2) {
                sender.sendMessage(CratesPlus.pluginPrefix + ChatColor.RED + "Correct Usage: /crate create <name>");
                return false;
            }

            String name = args[1];
            FileConfiguration config = CratesPlus.getPlugin().getConfig();
            if (config.isSet("Crates." + name)) {
                sender.sendMessage(CratesPlus.pluginPrefix + ChatColor.RED + name + " crate already exists");
                return false;
            }

            List<String> items = new ArrayList<String>();
            items.add("IRON_SWORD:1:DAMAGE_ALL-3");
            config.set("Crates." + name + ".Items", items);
            config.set("Crates." + name + ".Knockback", 0.0);
            config.set("Crates." + name + ".Broadcast", false);
            config.set("Crates." + name + ".Firework", false);
            config.set("Crates." + name + ".Color", "WHITE");
            CratesPlus.getPlugin().saveConfig();
            CratesPlus.getPlugin().reloadConfig();

            CratesPlus.crates.put(name.toLowerCase(), new Crate(name));
            CratesPlus.settingsHandler.setupCratesInventory();

            sender.sendMessage(CratesPlus.pluginPrefix + ChatColor.GREEN + name + " crate has been created");
            return true;
        }

        if (args.length >= 1 && args[0].equalsIgnoreCase("rename")) {
            if (args.length < 3) {
                sender.sendMessage(CratesPlus.pluginPrefix + ChatColor.RED + "Correct Usage: /crate rename <old name> <new name>");
                return false;
            }

            String oldName = args[1];
            String newName = args[2];

            if (!CratesPlus.crates.containsKey(oldName.toLowerCase())) {
                sender.sendMessage(CratesPlus.pluginPrefix + ChatColor.RED + oldName + " crate was not found");
                return false;
            }
            Crate crate = CratesPlus.crates.get(oldName.toLowerCase());

            FileConfiguration config = CratesPlus.getPlugin().getConfig();
            if (config.isSet("Crates." + newName)) {
                sender.sendMessage(CratesPlus.pluginPrefix + ChatColor.RED + newName + " crate already exists");
                return false;
            }

            config.set("Crates." + newName + ".Items", config.getList("Crates." + crate.getName(false) + ".Items"));
            config.set("Crates." + newName + ".Knockback", config.getDouble("Crates." + crate.getName(false) + ".Knockback"));
            config.set("Crates." + newName + ".Broadcast", config.getBoolean("Crates." + crate.getName(false) + ".Broadcast"));
            config.set("Crates." + newName + ".Firework", config.getBoolean("Crates." + crate.getName(false) + ".Firework"));
            config.set("Crates." + newName + ".Color", config.getString("Crates." + crate.getName(false) + ".Color"));
            config.set("Crates." + crate.getName(false), null);
            CratesPlus.getPlugin().saveConfig();
            CratesPlus.getPlugin().reloadConfig();

            CratesPlus.crates.remove(oldName.toLowerCase());
            CratesPlus.crates.put(newName.toLowerCase(), new Crate(newName));
            CratesPlus.settingsHandler.setupCratesInventory();

            sender.sendMessage(CratesPlus.pluginPrefix + ChatColor.GREEN + oldName + " has been renamed to " + newName);
            return true;
        }

        if (args.length >= 1 && args[0].equalsIgnoreCase("delete")) {
            if (args.length < 2) {
                sender.sendMessage(CratesPlus.pluginPrefix + ChatColor.RED + "Correct Usage: /crate delete <name>");
                return false;
            }

            String name = args[1];
            FileConfiguration config = CratesPlus.getPlugin().getConfig();
            if (!config.isSet("Crates." + name)) {
                sender.sendMessage(CratesPlus.pluginPrefix + ChatColor.RED + name + " crate doesn't exist");
                return false;
            }

            config.set("Crates." + name, null);
            CratesPlus.getPlugin().saveConfig();
            CratesPlus.getPlugin().reloadConfig();
            CratesPlus.crates.remove(name.toLowerCase());
            CratesPlus.settingsHandler.setupCratesInventory();

            sender.sendMessage(CratesPlus.pluginPrefix + ChatColor.GREEN + name + " crate has been deleted");
            return true;
        }

        if (args.length >= 1 && args[0].equalsIgnoreCase("key")) {
            if (args.length < 2) {
                sender.sendMessage(CratesPlus.pluginPrefix + ChatColor.RED + "Correct Usage: /crate key <player> [type]");
                return false;
            }

            Player player = null;
            if (!args[1].equalsIgnoreCase("all")) {
                player = Bukkit.getPlayer(args[1]);
                if (player == null) {
                    sender.sendMessage(CratesPlus.pluginPrefix + ChatColor.RED + "The player " + args[1] + " was not found");
                    return false;
                }
            }

            String crateType = null;
            if (args.length >= 3) {
                crateType = args[2];
            }

            if (crateType != null) {

                if (CratesPlus.crates.get(crateType.toLowerCase()) == null) {
                    sender.sendMessage(CratesPlus.pluginPrefix + ChatColor.RED + "Crate not found, crates are case sensitive");
                    return false;
                }

                if (player == null) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        CrateHandler.giveCrateKey(p, crateType);
                    }
                } else {
                    CrateHandler.giveCrateKey(player, crateType);
                }
            } else {
                if (player == null) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        CrateHandler.giveCrateKey(p);
                    }
                } else {
                    CrateHandler.giveCrateKey(player);
                }
            }

            if (player == null) {
                sender.sendMessage(CratesPlus.pluginPrefix + ChatColor.GREEN + "Given all players " + ChatColor.RESET + ChatColor.GREEN + " a crate key");
            } else {
                sender.sendMessage(CratesPlus.pluginPrefix + ChatColor.GREEN + "Given " + player.getDisplayName() + ChatColor.RESET + ChatColor.GREEN + " a crate key");
            }
            return true;
        }

        if (args.length >= 1 && args[0].equalsIgnoreCase("crate")) {
            Player player;
            String crateType;
            if (args.length == 1) {
                sender.sendMessage(CratesPlus.pluginPrefix + ChatColor.RED + "Correct Usage: /crate crate <type> [player]");
                return false;
            }

            if (args.length == 3) {
                player = Bukkit.getPlayer(args[2]);
            } else if (sender instanceof Player) {
                player = (Player) sender;
            } else {
                sender.sendMessage(CratesPlus.pluginPrefix + ChatColor.RED + "Correct Usage: /crate crate <type> [player]");
                return false;
            }

            if (player == null) {
                sender.sendMessage(CratesPlus.pluginPrefix + ChatColor.RED + "The player " + args[1] + " was not found");
                return false;
            }

            try {
                crateType = args[1];
            } catch (IllegalArgumentException e) {
                sender.sendMessage(CratesPlus.pluginPrefix + ChatColor.RED + "Please specify a valid crate type");
                return false;
            }

            if (CratesPlus.crates.get(crateType.toLowerCase()) == null) {
                sender.sendMessage(CratesPlus.pluginPrefix + ChatColor.RED + "Crate not found");
                return false;
            }

            CrateHandler.giveCrate(player, crateType);

            sender.sendMessage(CratesPlus.pluginPrefix + ChatColor.GREEN + "Given " + player.getDisplayName() + ChatColor.RESET + ChatColor.GREEN + " a crate");
            return true;
        }

        if (args.length > 0 && !args[0].equalsIgnoreCase("help")) {
            sender.sendMessage(CratesPlus.pluginPrefix + ChatColor.RED + "Unknown args");
            return false;
        }

        // Help Messages
        sender.sendMessage(CratesPlus.pluginPrefix + ChatColor.AQUA + "----- CratePlus Help -----");
        sender.sendMessage(CratesPlus.pluginPrefix + ChatColor.AQUA + "/crate reload - Reload configuration for CratesPlus (Experimental)");
        sender.sendMessage(CratesPlus.pluginPrefix + ChatColor.AQUA + "/crate settings - Edit settings of CratesPlus and crate winnings");
        sender.sendMessage(CratesPlus.pluginPrefix + ChatColor.AQUA + "/crate create <name> - Create a new crate");
        sender.sendMessage(CratesPlus.pluginPrefix + ChatColor.AQUA + "/crate rename <old name> <new name> - Rename a new crate");
        sender.sendMessage(CratesPlus.pluginPrefix + ChatColor.AQUA + "/crate delete <name> - Delete a crate");
        sender.sendMessage(CratesPlus.pluginPrefix + ChatColor.AQUA + "/crate key <player> [type] - Give player a random crate key");
        sender.sendMessage(CratesPlus.pluginPrefix + ChatColor.AQUA + "/crate crate <type> [player] - Give player a crate to be placed");

        return true;
    }

}
