package com.connorlinfoot.cratesplus.Events;

import com.connorlinfoot.cratesplus.Crate;
import com.connorlinfoot.cratesplus.CratesPlus;
import com.connorlinfoot.cratesplus.Handlers.CrateHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class CratePreviewEvent extends Event {
    private Player player;
    private String crateType;
    private Crate crate;
    private boolean canceled = false;

    public CratePreviewEvent(Player player, String crateType) {
        this.player = player;
        this.crateType = crateType;
        this.crate = CratesPlus.crates.get(crateType.toLowerCase());
    }

    public void doEvent() {
        List<String> items = crate.getItems();
        Integer size;
        if (items.size() <= 9) {
            size = 9;
        } else if (items.size() <= 18) {
            size = 18;
        } else if (items.size() <= 27) {
            size = 27;
        } else if (items.size() <= 36) {
            size = 36;
        } else if (items.size() <= 45) {
            size = 45;
        } else {
            size = 54;
        }
        Inventory inventory = Bukkit.createInventory(null, size, CratesPlus.crates.get(crateType.toLowerCase()).getColor() + crateType + " Possible Wins:");
        for (String i : items) {
            inventory.addItem(CrateHandler.stringToItemstack(i, player, false));
        }
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

}