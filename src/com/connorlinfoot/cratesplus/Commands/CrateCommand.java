package com.connorlinfoot.cratesplus.Commands;

import com.connorlinfoot.cratesplus.CrateHandler;
import com.connorlinfoot.cratesplus.CrateType;
import org.bukkit.Bukkit;
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

        if (args.length == 1) {
            Player player = Bukkit.getPlayer(args[0]);
            if (player == null) return false;

            CrateHandler.giveCrate(player, CrateType.ULTRA);
        }
        return true;
    }

}
