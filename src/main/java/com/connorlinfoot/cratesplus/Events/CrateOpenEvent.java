package com.connorlinfoot.cratesplus.Events;

import com.connorlinfoot.cratesplus.Crate;
import com.connorlinfoot.cratesplus.CratesPlus;
import com.connorlinfoot.cratesplus.Handlers.CrateHandler;
import com.connorlinfoot.cratesplus.Handlers.MessageHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CrateOpenEvent extends Event {
    private Player player;
    private String crateType;
    private Crate crate;
    private boolean canceled = false;

    public CrateOpenEvent(Player player, String crateType) {
        this.player = player;
        this.crateType = crateType;
        this.crate = CratesPlus.crates.get(crateType.toLowerCase());
    }

    public void doEvent() {
        /** Spawn firework */
        if (crate.isFirework()) {
            CrateHandler.spawnFirework(player.getLocation());
        }

        /** Do broadcast */
        if (crate.isBroadcast()) {
            Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "-------------------------------------------------");
            Bukkit.broadcastMessage(CratesPlus.pluginPrefix + MessageHandler.getMessage(CratesPlus.getPlugin(), "Broadcast", player, crateType));
            Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "-------------------------------------------------");
        }

        List<String> items = crate.getItems();
        Inventory inventory = Bukkit.createInventory(null, 27, CratesPlus.crates.get(crateType.toLowerCase()).getColor() + crateType + " Win!");

        Integer ii = 0;
        while (ii < 10) {
            inventory.setItem(ii, new ItemStack(Material.STAINED_GLASS_PANE));
            ii++;
        }

        ii = 17;
        while (ii < 27) {
            inventory.setItem(ii, new ItemStack(Material.STAINED_GLASS_PANE));
            ii++;
        }

        String i = items.get(CrateHandler.randInt(0, items.size() - 1));
        ItemStack win = CrateHandler.stringToItemstack(i, player, true);
        inventory.setItem(13, win);
        player.openInventory(inventory);
    }

    @Override
    public HandlerList getHandlers() {
        return null;
    }

    public boolean isCanceled() {
        return this.canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setCrateType(String crateType) {
        this.crateType = crateType;
    }

    public String getCrateType() {
        return this.crateType;
    }

    public Crate getCrate() {
        return this.crate;
    }

}