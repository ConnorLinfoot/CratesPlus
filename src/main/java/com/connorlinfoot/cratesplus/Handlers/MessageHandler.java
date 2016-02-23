package com.connorlinfoot.cratesplus.Handlers;

import com.connorlinfoot.cratesplus.Crate;
import com.connorlinfoot.cratesplus.CratesPlus;
import com.connorlinfoot.cratesplus.Winning;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MessageHandler {

    public static String getMessage(CratesPlus cratesPlus, String messageName, Player player, Crate crate, Winning winning) {
        if (!cratesPlus.getConfig().isSet("Messages." + messageName))
            return null;
        String message = cratesPlus.getConfig().getString("Messages." + messageName);
        message = doPlaceholders(message, player, crate, winning);
        message = ChatColor.translateAlternateColorCodes('&', message);
        return message;
    }

    public static String doPlaceholders(String message, Player player, Crate crate, Winning winning) {
        message = ChatColor.translateAlternateColorCodes('&', message);
        if (player != null)
            message = message.replaceAll("%name%", player.getName()).replaceAll("%displayname%", player.getDisplayName()).replaceAll("%uuid%", player.getUniqueId().toString());
        if (crate != null)
            message = message.replaceAll("%crate%", crate.getName(true) + ChatColor.RESET);
        if (winning != null)
            message = message.replaceAll("%prize%", winning.getWinningItemStack().getItemMeta().getDisplayName() + ChatColor.RESET);
        return message;
    }

}
