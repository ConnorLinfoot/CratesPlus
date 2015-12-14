package com.connorlinfoot.cratesplus.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryClose implements Listener {

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (true) return; // This whole file shouldn't be needed anymore... we'll see.
        if (event.getInventory().getTitle() != null && !event.getInventory().getTitle().contains(" Wins") && !event.getInventory().getTitle().contains("Edit ") && event.getInventory().getTitle().contains(" Win")) {
            if (event.getInventory().getItem(0).hasItemMeta() && event.getInventory().getItem(0).getItemMeta().hasDisplayName() && event.getInventory().getItem(0).getItemMeta().getDisplayName().contains("Winner")) {
                if (event.getInventory().getItem(22).getItemMeta().hasLore() && event.getInventory().getItem(22).getItemMeta().getLore().toString().contains("Crate Command"))
                    return;
                if (!event.getInventory().getItem(22).hasItemMeta() || !event.getInventory().getItem(22).getItemMeta().hasLore() || !event.getInventory().getItem(22).getItemMeta().getLore().toString().contains("Crates Command"))
                    event.getPlayer().getInventory().addItem(event.getInventory().getItem(22));
            }
        }
    }

}
