package com.connorlinfoot.cratesplus.Commands;

import com.connorlinfoot.cratesplus.CrateHandler;
import com.connorlinfoot.cratesplus.CrateType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CrateCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String string, String[] args) {
        // This file is currently not ready for "production" and is for testing!
        if (!sender.isOp()) {
            return false;
        }

//        if (args.length == 1) {
//            Player player = Bukkit.getPlayer(args[0]);
//            if (player == null) return false;
//
//            CrateHandler.giveCrateKey(player);
//        }

        if (args.length == 0 || (args.length >= 1 && args[0].equalsIgnoreCase("help"))) {
            // Help messages
            return true;
        }

        if (args.length >= 1 && args[0].equalsIgnoreCase("key")) {
            if (args.length != 2) {
                return false;
            }

            Player player = Bukkit.getPlayer(args[1]);
            if (player == null) {
                sender.sendMessage(ChatColor.RED + "The player " + args[1] + " was not found");
                return false;
            }

            CrateHandler.giveCrateKey(player);
            sender.sendMessage(ChatColor.GREEN + "Given " + player.getDisplayName() + ChatColor.RESET + ChatColor.GREEN + " a crate key");
            return true;
        }

        if (args.length >= 1 && args[0].equalsIgnoreCase("crate")) {
            Player player;
            CrateType crateType;
            if (args.length == 1) {
                sender.sendMessage(ChatColor.RED + "Correct Usage: /crate crate <type> [player]");
                return false;
            }

            if (args.length == 3) {
                player = Bukkit.getPlayer(args[2]);
            } else if (sender instanceof Player) {
                player = (Player) sender;
            } else {
                sender.sendMessage(ChatColor.RED + "Correct Usage: /crate crate <type> [player]");
                return false;
            }

            if (player == null) {
                sender.sendMessage(ChatColor.RED + "The player " + args[1] + " was not found");
                return false;
            }

            try {
                crateType = CrateType.valueOf(args[1].toUpperCase());
            } catch (IllegalArgumentException e) {
                sender.sendMessage(ChatColor.RED + "Please specify a valid crate type");
                return false;
            }

            CrateHandler.giveCrate(player, crateType);
            sender.sendMessage(ChatColor.GREEN + "Given " + player.getDisplayName() + ChatColor.RESET + ChatColor.GREEN + " a crate");
            return true;
        }




        return true;
    }

}
