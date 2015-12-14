package com.connorlinfoot.cratesplus.Handlers;

import com.connorlinfoot.cratesplus.Crate;
import com.connorlinfoot.cratesplus.CratesPlus;
import com.connorlinfoot.cratesplus.Winning;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SettingsHandler {
    private Inventory settings;
    private Inventory crates;

    public SettingsHandler() {
        setupSettingsInventory();
        setupCratesInventory();
    }

    public void setupSettingsInventory() {
        settings = Bukkit.createInventory(null, 9, "CratesPlus Settings");

        ItemStack itemStack;
        ItemMeta itemMeta;
        List<String> lore;


        /** Crates */

        itemStack = new ItemStack(Material.CHEST);
        itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GREEN + "Edit Crates");
        lore = new ArrayList<String>();
        lore.add("");
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        settings.setItem(1, itemStack);


        /** Reload Config */

        itemStack = new ItemStack(Material.BARRIER);
        itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GREEN + "Reload Config");
        lore = new ArrayList<String>();
        lore.add("");
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        settings.setItem(3, itemStack);
    }

    public void setupCratesInventory() {
        crates = Bukkit.createInventory(null, 54, "Crates");

        ItemStack itemStack;
        ItemMeta itemMeta;
        List<String> lore;

        for (Map.Entry<String, Crate> entry : CratesPlus.crates.entrySet()) {
            String name = entry.getKey();
            Crate crate = entry.getValue();

            itemStack = new ItemStack(Material.CHEST);
            itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(crate.getName(true));
            itemStack.setItemMeta(itemMeta);
            crates.addItem(itemStack);
        }
    }

    public void openSettings(final Player player) {
        Bukkit.getScheduler().runTaskLater(CratesPlus.getPlugin(), new Runnable() {
            @Override
            public void run() {
                player.openInventory(settings);
            }
        }, 1L);
    }

    public void openCrates(final Player player) {
        Bukkit.getScheduler().runTaskLater(CratesPlus.getPlugin(), new Runnable() {
            @Override
            public void run() {
                player.openInventory(crates);
            }
        }, 1L);
    }

    public void openCrateWinnings(final Player player, String crateName) {
        Crate crate = CratesPlus.crates.get(crateName.toLowerCase());
        if (crate == null) {
            return; // TODO Error handling here
        }

        final Inventory inventory = Bukkit.createInventory(null, 54, "Edit " + crate.getName(false) + " Crate Winnings");

        for (Winning winning : crate.getWinnings()) {
            inventory.addItem(winning.getPreviewItemStack());
        }

        Bukkit.getScheduler().runTaskLater(CratesPlus.getPlugin(), new Runnable() {
            @Override
            public void run() {
                player.openInventory(inventory);
            }
        }, 1L);

    }

    public void openCrate(final Player player, String crateName) {
        Crate crate = CratesPlus.crates.get(crateName.toLowerCase());
        if (crate == null) {
            return; // TODO Error handling here
        }

        final Inventory inventory = Bukkit.createInventory(null, 9, "Edit " + crate.getName(false) + " Crate");

        ItemStack itemStack;
        ItemMeta itemMeta;
        List<String> lore;


        /** Rename Crate */

        itemStack = new ItemStack(Material.NAME_TAG);
        itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.RED + "Rename Crate");
        lore = new ArrayList<String>();
        lore.add("");
        lore.add(ChatColor.GRAY + "Use /crate rename " + crate.getName(false) + " <new name>");
        lore.add("");
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        inventory.setItem(1, itemStack);


        /** Edit Crate Winnings */

        itemStack = new ItemStack(Material.DIAMOND);
        itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.WHITE + "Edit Crate Winnings");
        lore = new ArrayList<String>();
        lore.add("");
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        inventory.setItem(3, itemStack);


        /** Delete Crate */

        itemStack = new ItemStack(Material.BARRIER);
        itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.WHITE + "Delete Crate");
        lore = new ArrayList<String>();
        lore.add("");
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        inventory.setItem(5, itemStack);

        Bukkit.getScheduler().runTaskLater(CratesPlus.getPlugin(), new Runnable() {
            @Override
            public void run() {
                player.openInventory(inventory);
            }
        }, 1L);

    }

}
