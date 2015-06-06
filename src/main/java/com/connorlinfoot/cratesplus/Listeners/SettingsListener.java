package com.connorlinfoot.cratesplus.Listeners;

import com.connorlinfoot.cratesplus.Crate;
import com.connorlinfoot.cratesplus.CratesPlus;
import com.connorlinfoot.cratesplus.Handlers.CrateHandler;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SettingsListener implements Listener {

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player)) return;
        if (event.getInventory().getTitle() != null && event.getInventory().getTitle().contains("Crate Winnings")) {
            String crateName = ChatColor.stripColor(event.getInventory().getTitle().replaceAll("Edit ", "").replaceAll(" Crate Winnings", ""));
            List<String> items = new ArrayList<String>();
            for (ItemStack itemStack : event.getInventory().getContents()) {
                String itemString = CrateHandler.itemstackToString(itemStack);
                if (itemString != null) items.add(itemString);
            }
            Crate crate = CratesPlus.crates.get(crateName.toLowerCase());
            if (crate == null) {
                return;
            }
            CratesPlus.getPlugin().getConfig().set("Crates." + crate.getName(false) + ".Items", items);
            CratesPlus.getPlugin().saveConfig();
            crate.reloadItems();
            ((Player) event.getPlayer()).sendMessage(CratesPlus.pluginPrefix + ChatColor.GREEN + "Crate winnings updated");
        }
    }

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

            if (event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Edit Crates")) {
                event.setCancelled(true);
                player.closeInventory();
                CratesPlus.settingsHandler.openCrates(player);
                return;
            }

            if (event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Reload Config")) {
                event.setCancelled(true);
                player.closeInventory();
                CratesPlus.reloadPlugin();
                player.sendMessage(CratesPlus.pluginPrefix + ChatColor.GREEN + "CratesPlus configuration was reloaded - This feature is not fully tested and may not work correctly");
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

        } else if (event.getInventory().getTitle().contains(" Crate Winnings")) {
            return;
        } else if (event.getInventory().getTitle().contains("Edit ")) {
            if (itemStack == null || itemStack.getType() == Material.AIR || !event.getCurrentItem().hasItemMeta() || !event.getCurrentItem().getItemMeta().hasDisplayName()) {
                event.setCancelled(true);
                return;
            }

            if (event.getCurrentItem().getItemMeta().getDisplayName().contains("Edit Crate Winnings")) {
                event.setCancelled(true);
                player.closeInventory();
                String name = ChatColor.stripColor(event.getInventory().getTitle().replaceAll("Edit ", "").replaceAll(" Crate", ""));
                CratesPlus.settingsHandler.openCrateWinnings(player, name);
            } else if (event.getCurrentItem().getItemMeta().getDisplayName().contains("Delete")) {
                event.setCancelled(true);
                player.closeInventory();
                String name = ChatColor.stripColor(event.getInventory().getTitle().replaceAll("Edit ", "").replaceAll(" Crate", ""));
                CratesPlus.getPlugin().getConfig().set("Crates." + name, null);
                CratesPlus.getPlugin().saveConfig();
                CratesPlus.getPlugin().reloadConfig();
                CratesPlus.crates.remove(name.toLowerCase());
                CratesPlus.settingsHandler.setupCratesInventory();
                player.sendMessage(CratesPlus.pluginPrefix + ChatColor.GREEN + name + " crate has been deleted");
            } else if (event.getCurrentItem().getItemMeta().getDisplayName().contains("Rename Crate")) {
                event.setCancelled(true);
            }

        }

    }

}
