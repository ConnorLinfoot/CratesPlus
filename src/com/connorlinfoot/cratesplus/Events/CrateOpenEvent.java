package com.connorlinfoot.cratesplus.Events;

import com.connorlinfoot.cratesplus.CratesPlus;
import com.connorlinfoot.cratesplus.Handlers.CrateHandler;
import com.connorlinfoot.cratesplus.Handlers.MessageHandler;
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

public class CrateOpenEvent extends Event {
    private Player player;
    private String crateType;
    private boolean canceled = false;

    public CrateOpenEvent(Player player, String crateType) {
        this.player = player;
        this.crateType = crateType;
    }

    public void doEvent() {
        // Spawn firework
        if (CratesPlus.getPlugin().getConfig().getBoolean("Firework On Crate Open." + crateType)) {
            CrateHandler.spawnFirework(player.getLocation());
        }

        if (CratesPlus.getPlugin().getConfig().getBoolean("Broadcast On Crate Open." + crateType)) {
            Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "-------------------------------------------------");
            Bukkit.broadcastMessage(CratesPlus.pluginPrefix + MessageHandler.getMessage(CratesPlus.getPlugin(), "Broadcast", player, crateType));
            Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "-------------------------------------------------");
        }

        List<String> items = CratesPlus.getPlugin().getConfig().getStringList("Crate Items." + crateType);
        boolean useGui = CratesPlus.getPlugin().getConfig().getBoolean("Crate Open GUI");
        Inventory inventory = Bukkit.createInventory(null, 27, CratesPlus.crates.get(crateType) + crateType + " Win!");

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
        String[] args = i.split(":", -1);
        if (args.length >= 2 && args[0].equalsIgnoreCase("command")) {
            /** Commands */
            String command = args[1];
            String title = "Command: /" + command;
            if (args.length == 3) {
                title = args[2];
            }
            command = command.replaceAll("%name%", player.getName());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
            if (useGui) {
                ItemStack itemStack = new ItemStack(Material.EMPTY_MAP);
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName(ChatColor.RESET + title);
                itemStack.setItemMeta(itemMeta);
                inventory.setItem(13, itemStack);
            }
        } else if (args.length == 1) {
            /** Item without any amounts or enchantments */
            String[] args1 = args[0].split("-");
            ItemStack itemStack;
            if (args1.length == 1) {
                itemStack = new ItemStack(Material.getMaterial(args1[0].toUpperCase()));
            } else {
                itemStack = new ItemStack(Material.getMaterial(args1[0].toUpperCase()), 1, Byte.parseByte(args1[1]));
            }

            if (useGui) {
                inventory.setItem(13, itemStack);
            } else {
                player.getInventory().addItem(itemStack);
            }
        } else if (args.length == 2) {
            if (useGui) {
                inventory.setItem(13, new ItemStack(Material.getMaterial(args[0].toUpperCase()), Integer.parseInt(args[1])));
            } else {
                player.getInventory().addItem(new ItemStack(Material.getMaterial(args[0].toUpperCase()), Integer.parseInt(args[1])));
            }
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
            if (useGui) {
                inventory.setItem(13, itemStack);
            } else {
                player.getInventory().addItem(itemStack);
            }
        }
        if (useGui) {
            player.openInventory(inventory);
        } else {
            player.updateInventory();
        }
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