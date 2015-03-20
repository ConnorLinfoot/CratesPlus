package com.connorlinfoot.cratesplus.Listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryInteract implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getTitle() != null && event.getInventory().getTitle().contains("Possible Wins:")) {
            event.setCancelled(true);
        } else if (event.getInventory().getTitle() != null && event.getInventory().getTitle().contains(" Win!")) {
            if (event.getSlot() != 13 || (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.EMPTY_MAP)) {
                event.setCancelled(true);
            }
        }
    }

}
