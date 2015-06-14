package com.connorlinfoot.cratesplus.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryClose implements Listener {

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory().getTitle() != null && event.getInventory().getTitle().contains(" Win!") && event.getInventory().getItem(13) != null) {
            event.getPlayer().getInventory().addItem(event.getInventory().getItem(13));
        }
    }

}
