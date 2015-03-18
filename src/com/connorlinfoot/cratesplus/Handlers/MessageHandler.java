package com.connorlinfoot.cratesplus.Handlers;

import com.connorlinfoot.cratesplus.CrateType;
import com.connorlinfoot.cratesplus.CratesPlus;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MessageHandler {

    public static String getMessage(CratesPlus cratesPlus, String messageName, Player player, CrateType crateType) {
        if (!cratesPlus.getConfig().isSet("Messages." + messageName))
            return null;
        String message = cratesPlus.getConfig().getString("Messages." + messageName);
        message = doPlaceholders(message, player, crateType);
        message = ChatColor.translateAlternateColorCodes('&', message);
        return message;
    }

    private static String doPlaceholders(String message, Player player, CrateType crateType) {
        String name = player.getName();
        String displayName = player.getDisplayName();
        String uuid = player.getUniqueId().toString();
        String crate = crateType.getCode(true);
        return message.replaceAll("%name%", name).replaceAll("%displayname%", displayName).replaceAll("%uuid%", uuid).replaceAll("%crate%", crate);
    }

}
