package com.connorlinfoot.cratesplus.Events;

import com.connorlinfoot.cratesplus.Crate;
import com.connorlinfoot.cratesplus.CratesPlus;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class CratePreviewEvent extends Event {
    private Player player;
    private String crateType;
    private Crate crate;
    private boolean canceled = false;

    public CratePreviewEvent(Player player, String crateType) {
        this.player = player;
        this.crateType = crateType;
        this.crate = CratesPlus.crates.get(crateType);
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
        Inventory inventory = Bukkit.createInventory(null, size, CratesPlus.crates.get(crateType).getColor() + crateType + " Possible Wins:");
        for (String i : items) {
            String[] args = i.split(":", -1);
            if (args.length >= 2 && args[0].equalsIgnoreCase("command")) {
                /** Commands */
                String command = args[1];
                String title = "Command: /" + command;
                if (args.length == 3) {
                    title = args[2];
                }
                ItemStack itemStack = new ItemStack(Material.EMPTY_MAP);
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName(ChatColor.RESET + title);
                itemStack.setItemMeta(itemMeta);
                inventory.addItem(itemStack);
            } else if (args.length == 1) {
                /** Item without any amounts or enchantments */
                String[] args1 = args[0].split("-");
                ItemStack itemStack;
                if (args1.length == 1) {
                    itemStack = new ItemStack(Material.getMaterial(args1[0].toUpperCase()));
                } else {
                    itemStack = new ItemStack(Material.getMaterial(args1[0].toUpperCase()), 1, Byte.parseByte(args1[1]));
                }
                inventory.addItem(itemStack);
            } else if (args.length == 2) {
                ItemStack itemStack = new ItemStack(Material.getMaterial(args[0].toUpperCase()), Integer.parseInt(args[1]));
                inventory.addItem(itemStack);
            } else if (args.length == 3) {
                String[] enchantments = args[2].split("\\|", -1);
                ItemStack itemStack = new ItemStack(Material.getMaterial(args[0]), Integer.parseInt(args[1]));
                for (String e : enchantments) {
                    String[] args1 = e.split("-", -1);
                    if (args1.length == 1) {
                        try {
                            itemStack.addUnsafeEnchantment(Enchantment.getByName(args1[0]), 1);
                        } catch (Exception ignored) {
                        }
                    } else if (args1.length == 2) {
                        try {
                            itemStack.addUnsafeEnchantment(Enchantment.getByName(args1[0]), Integer.parseInt(args1[1]));
                        } catch (Exception ignored) {
                        }
                    }
                }
                inventory.addItem(itemStack);
            }
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