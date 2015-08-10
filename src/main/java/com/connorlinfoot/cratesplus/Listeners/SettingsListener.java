package com.connorlinfoot.cratesplus.Listeners;

import com.connorlinfoot.cratesplus.Crate;
import com.connorlinfoot.cratesplus.CratesPlus;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SettingsListener implements Listener {

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player)) return;
        if (event.getInventory().getTitle() != null && event.getInventory().getTitle().contains("Crate Winnings")) {
            String crateName = ChatColor.stripColor(event.getInventory().getTitle().replaceAll("Edit ", "").replaceAll(" Crate Winnings", ""));
            Crate crate = CratesPlus.crates.get(crateName.toLowerCase());
            if (crate == null) {
                return;
            }

            CratesPlus.getPlugin().getConfig().set("Crates." + crateName + ".Winnings", null);
            CratesPlus.getPlugin().saveConfig();
            for (ItemStack itemStack : event.getInventory().getContents()) {
                if (itemStack == null)
                    continue;
                int id = getFreeID(crateName, 1);

                String type = "ITEM";
                String itemtype = itemStack.getType().toString().toUpperCase();
                Byte itemData = itemStack.getData().getData();
                String name = "NONE";
                if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName())
                    name = itemStack.getItemMeta().getDisplayName();
                Integer amount = itemStack.getAmount();
                List<String> enchantments = new ArrayList<String>();
                if (itemStack.getEnchantments() != null && !itemStack.getEnchantments().isEmpty()) {
                    for (Map.Entry<Enchantment, Integer> entry : itemStack.getEnchantments().entrySet()) {
                        Enchantment enchantment = entry.getKey();
                        Integer level = entry.getValue();
                        enchantments.add(enchantment.getName().toUpperCase() + "-" + level);
                    }
                }

                // Save to config and creating winning instance
                FileConfiguration config = CratesPlus.getPlugin().getConfig();
                String path = "Crates." + crateName + ".Winnings." + id;
                config.set(path + ".Type", type);
                config.set(path + ".Item Type", itemtype);
                config.set(path + ".Item Data", itemData);
                config.set(path + ".Name", name);
                config.set(path + ".Amount", amount);
                config.set(path + ".Enchantments", enchantments);
            }

            CratesPlus.getPlugin().saveConfig();
            crate.reloadWinnings();
            event.getPlayer().sendMessage(CratesPlus.pluginPrefix + ChatColor.GREEN + "Crate winnings updated");
        }
    }

    private static int getFreeID(String crate, int check) {
        if (CratesPlus.getPlugin().getConfig().isSet("Crates." + crate + ".Winnings." + check))
            return getFreeID(crate, check + 1);
        return check;
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
