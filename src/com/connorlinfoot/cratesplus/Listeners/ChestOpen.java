package com.connorlinfoot.cratesplus.Listeners;

import com.connorlinfoot.cratesplus.CrateType;
import com.connorlinfoot.cratesplus.CratesPlus;
import com.connorlinfoot.cratesplus.Handlers.CrateHandler;
import com.connorlinfoot.cratesplus.Handlers.MessageHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
            String title = CratesPlus.getPlugin().getConfig().getString("Crate Keys.Name").replaceAll("%type%", crateType.getCode(true));
            if (item.hasItemMeta() && item.getItemMeta().getDisplayName() != null && item.getItemMeta().getDisplayName().contains(title)) {
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
                    Bukkit.broadcastMessage(CratesPlus.pluginPrefix + MessageHandler.getMessage(CratesPlus.getPlugin(), "Broadcast", player, crateType));
                    Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "-------------------------------------------------");
                }

                List<String> items = CratesPlus.getPlugin().getConfig().getStringList("Crate Items." + crateType.getCode());
                boolean useGui = CratesPlus.getPlugin().getConfig().getBoolean("Crate Open GUI");
                Inventory inventory = Bukkit.createInventory(null, 27, crateType.getCode(true) + " Win!");

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
                    title = "Command: /" + command;
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
            } else {
                if (CratesPlus.getPlugin().getConfig().getBoolean("Crate Previews")) {
                    List<String> items = CratesPlus.getPlugin().getConfig().getStringList("Crate Items." + crateType.getCode());
                    Inventory inventory = Bukkit.createInventory(null, (items.size() + 8) / 9 * 9, crateType.getCode(true) + " Possible Wins:");
                    for (String i : items) {
                        String[] args = i.split(":", -1);
                        if (args.length >= 2 && args[0].equalsIgnoreCase("command")) {
                            /** Commands */
                            String command = args[1];
                            title = "Command: /" + command;
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
                } else {
                    player.sendMessage(CratesPlus.pluginPrefix + MessageHandler.getMessage(CratesPlus.getPlugin(), "Crate Open Without Key", player, crateType));
                    double knock = CratesPlus.getPlugin().getConfig().getDouble("Crate Knockback." + crateType.getCode());
                    if (knock != 0) {
                        player.setVelocity(player.getLocation().getDirection().multiply(-knock));
                    }
                }
                event.setCancelled(true);
            }
        }
    }

}
