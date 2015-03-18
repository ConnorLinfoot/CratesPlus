package com.connorlinfoot.cratesplus.Listeners;

import com.connorlinfoot.cratesplus.CrateHandler;
import com.connorlinfoot.cratesplus.CrateType;
import com.connorlinfoot.cratesplus.CratesPlus;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ChestOpen implements Listener {

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        final Player player = (Player) event.getPlayer();
        ItemStack item = player.getItemInHand();
        if (event.getInventory().getTitle().contains(" Crate!")) {
            CrateType crateType = CrateType.COMMON;
            if (event.getInventory().getTitle().contains("Rare")) {
                crateType = CrateType.RARE;
            } else if (event.getInventory().getTitle().contains("Ultra")) {
                crateType = CrateType.ULTRA;
            }
            if (item.hasItemMeta() && item.getItemMeta().getDisplayName() != null && item.getItemMeta().getDisplayName().contains(crateType.getCode(true) + " Crate Key!")) {
                event.setCancelled(true);
                if (item.getAmount() > 1) {
                    item.setAmount(item.getAmount() - 1);
                } else {
                    player.getInventory().remove(item);
                }

                // Spawn firework
                if (CratesPlus.getPlugin().getConfig().getBoolean("Firework On Crate Open." + crateType.getCode())) {
                    CrateHandler.spawnFirework(player.getLocation());
                }

                if (CratesPlus.getPlugin().getConfig().getBoolean("Broadcast On Crate Open." + crateType.getCode())) {
                    Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "-------------------------------------------------");
                    Bukkit.broadcastMessage(CratesPlus.pluginPrefix + ChatColor.LIGHT_PURPLE + player.getDisplayName() + ChatColor.LIGHT_PURPLE + " opened a " + crateType.getCode(true) + ChatColor.LIGHT_PURPLE + " crate!");
                    Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "-------------------------------------------------");
                }

                List<String> items = CratesPlus.getPlugin().getConfig().getStringList("Crate Items." + crateType.getCode());
                String i = items.get(CrateHandler.randInt(0, items.size() - 1));
                String[] args = i.split(":", -1);
                if (args.length == 2 && args[0].equalsIgnoreCase("command")) {
                    String command = args[1];
                    command = command.replaceAll("%name%", player.getName());
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                } else if (args.length == 1) {
                    player.getInventory().addItem(new ItemStack(Material.getMaterial(args[0].toUpperCase())));
                } else if (args.length == 2) {
                    player.getInventory().addItem(new ItemStack(Material.getMaterial(args[0].toUpperCase()), Integer.parseInt(args[1])));
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
                    player.getInventory().addItem(itemStack);
                }
                player.updateInventory();
            } else {
                player.sendMessage(CratesPlus.pluginPrefix + ChatColor.RED + "You must be holding a " + crateType.getCode(true) + ChatColor.RED + " key to open this crate");
                event.setCancelled(true);
                double knock = CratesPlus.getPlugin().getConfig().getDouble("Crate Knockback." + crateType.getCode());
                if (knock != 0) {
                    player.setVelocity(player.getLocation().getDirection().multiply(-knock));
                }
            }
        }
    }

}
