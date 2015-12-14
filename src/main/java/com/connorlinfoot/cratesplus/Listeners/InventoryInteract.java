package com.connorlinfoot.cratesplus.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

public class InventoryInteract implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory() == null)
            return;
        if (event.getInventory().getTitle() != null && event.getInventory().getTitle().contains("Possible Wins:")) {
            event.setCancelled(true);
        } else if (event.getInventory().getTitle() != null && event.getInventory().getTitle().contains(" Win") && !event.getInventory().getTitle().contains("Edit ")) {
            if (event.getInventory().getType() != null && event.getInventory().getType() == InventoryType.CHEST && event.getSlot() != 22 || (event.getCurrentItem() != null)) {
                event.setCancelled(true);
                event.getWhoClicked().closeInventory();
            }
        }
    }

}
