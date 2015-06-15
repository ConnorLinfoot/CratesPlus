package com.connorlinfoot.cratesplus.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryClose implements Listener {

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory().getTitle() != null && !event.getInventory().getTitle().contains(" Wins") && event.getInventory().getTitle().contains(" Win")) {
            if (event.getInventory().getItem(0).hasItemMeta() && event.getInventory().getItem(0).getItemMeta().hasDisplayName() && event.getInventory().getItem(0).getItemMeta().getDisplayName().contains("Winner")) {
                if (event.getInventory().getItem(13).getItemMeta().hasLore() && event.getInventory().getItem(13).getItemMeta().getLore().toString().contains("Crate Command"))
                    return;
                event.getPlayer().getInventory().addItem(event.getInventory().getItem(13));
            }
        }
    }

}
