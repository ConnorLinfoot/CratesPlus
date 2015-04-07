package com.connorlinfoot.cratesplus.Listeners;

import com.connorlinfoot.cratesplus.CratesPlus;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (CratesPlus.updateAvailable && event.getPlayer().hasPermission("cratesplus.updates")) {
            event.getPlayer().sendMessage(CratesPlus.updateMessage);
        }
    }

}
