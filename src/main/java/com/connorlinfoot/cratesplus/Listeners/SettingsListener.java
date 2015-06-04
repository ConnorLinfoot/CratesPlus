package com.connorlinfoot.cratesplus.Listeners;

import com.connorlinfoot.cratesplus.CratesPlus;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class SettingsListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        ItemStack itemStack = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();

        if (event.getInventory().getTitle() == null)
            return;


        if (event.getInventory().getTitle().contains("CratesPlus Settings")) {

            if (itemStack == null || itemStack.getType() == Material.AIR || !event.getCurrentItem().hasItemMeta() || !event.getCurrentItem().getItemMeta().hasDisplayName()) {
                event.setCancelled(true);
                return;
            }

            if (event.getCurrentItem().getItemMeta().getDisplayName().contains("Edit Crates")) {
                player.closeInventory();
                CratesPlus.settingsHandler.openCrates(player);
                return;
            }

            if (event.getCurrentItem().getItemMeta().getDisplayName().contains(ChatColor.RED + "")) {
                event.setCancelled(true);
                player.closeInventory();
                player.sendMessage(ChatColor.RED + "Coming Soon");
            }

        } else if (event.getInventory().getTitle().contains("Crates")) {
            if (itemStack == null || itemStack.getType() == Material.AIR || !event.getCurrentItem().hasItemMeta() || !event.getCurrentItem().getItemMeta().hasDisplayName()) {
                event.setCancelled(true);
                return;
            }

            if (event.getCurrentItem().getType() == Material.CHEST) {
                player.closeInventory();
                CratesPlus.settingsHandler.openCrate(player, ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()));
            }
        }

    }

}
