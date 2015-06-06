package com.connorlinfoot.cratesplus.Handlers;

import com.connorlinfoot.cratesplus.Crate;
import com.connorlinfoot.cratesplus.CratesPlus;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class CrateHandler {

    public static int randInt(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    private static Color getColor(int i) {
        Color c;
        switch (i) {
            case 1:
                c = Color.AQUA;
                break;
            case 2:
                c = Color.BLACK;
                break;
            case 3:
                c = Color.BLUE;
                break;
            case 4:
                c = Color.FUCHSIA;
                break;
            case 5:
                c = Color.GRAY;
                break;
            case 6:
                c = Color.GREEN;
                break;
            case 7:
                c = Color.LIME;
                break;
            case 8:
                c = Color.MAROON;
                break;
            case 9:
                c = Color.NAVY;
                break;
            case 10:
                c = Color.OLIVE;
                break;
            case 11:
                c = Color.ORANGE;
                break;
            case 12:
                c = Color.PURPLE;
                break;
            case 13:
                c = Color.RED;
                break;
            case 14:
                c = Color.SILVER;
                break;
            case 15:
                c = Color.TEAL;
                break;
            case 16:
                c = Color.WHITE;
                break;
            case 17:
                c = Color.YELLOW;
                break;
            default:
                c = Color.AQUA;
                break;
        }
        return c;
    }

    public static void spawnFirework(Location location) {
        Firework fw = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();
        Random r = new Random();
        int rt = r.nextInt(4) + 1;
        FireworkEffect.Type type = FireworkEffect.Type.BALL;
        if (rt == 1) type = FireworkEffect.Type.BALL;
        if (rt == 2) type = FireworkEffect.Type.BALL_LARGE;
        if (rt == 3) type = FireworkEffect.Type.BURST;
        if (rt == 4) type = FireworkEffect.Type.CREEPER;
        if (rt == 5) type = FireworkEffect.Type.STAR;
        int r1i = r.nextInt(17) + 1;
        int r2i = r.nextInt(17) + 1;
        Color c1 = getColor(r1i);
        Color c2 = getColor(r2i);
        FireworkEffect effect = FireworkEffect.builder().flicker(r.nextBoolean()).withColor(c1).withFade(c2).with(type).trail(r.nextBoolean()).build();
        fwm.addEffect(effect);
        int rp = r.nextInt(2) + 1;
        fwm.setPower(rp);
        fw.setFireworkMeta(fwm);
    }

    public static void giveCrateKey(Player player) {
        Set<String> crates = CratesPlus.getPlugin().getConfig().getConfigurationSection("Crates").getKeys(false);
        Integer random = randInt(0, crates.size() - 1);
        String crateType = "";
        Integer i = 0;
        for (String crate : crates) {
            if (i.equals(random)) {
                crateType = crate;
                break;
            }
            i++;
        }
        giveCrateKey(player, crateType);
    }

    public static void giveCrateKey(Player player, String crateType) {
        if (player == null || !player.isOnline()) return;
        if (crateType == null) {
            giveCrateKey(player);
            return;
        }
        Crate crate = CratesPlus.crates.get(crateType.toLowerCase());

        ItemStack key = new ItemStack(Material.getMaterial(CratesPlus.getPlugin().getConfig().getString("Crate Keys.Item").toUpperCase()));
        List<String> enchantments = CratesPlus.getPlugin().getConfig().getStringList("Crate Keys.Enchantments");
        if (enchantments.size() > 0) {
            for (String e : enchantments) {
                String[] args = e.split("-");
                if (args.length == 1) {
                    try {
                        key.addUnsafeEnchantment(Enchantment.getByName(e), 1);
                    } catch (Exception ee) {
                        ee.printStackTrace();
                    }
                } else {
                    try {
                        key.addUnsafeEnchantment(Enchantment.getByName(args[0]), Integer.parseInt(args[1]));
                    } catch (Exception ee) {
                        ee.printStackTrace();
                    }
                }
            }
        }
        ItemMeta keyMeta = key.getItemMeta();
        String title = CratesPlus.getPlugin().getConfig().getString("Crate Keys.Name").replaceAll("%type%", crate.getName(true));
        keyMeta.setDisplayName(title);
        List<String> lore = new ArrayList<String>();
        lore.add(ChatColor.GRAY + "Right-Click on a \"" + crate.getName(true) + ChatColor.GRAY + "\" crate");
        lore.add(ChatColor.GRAY + "to win an item!");
        lore.add("");
        keyMeta.setLore(lore);
        key.setItemMeta(keyMeta);
        player.getInventory().addItem(key);
        player.sendMessage(CratesPlus.pluginPrefix + MessageHandler.getMessage(CratesPlus.getPlugin(), "Key Given", player, crateType));
    }

    public static void giveCrate(Player player, String crateType) {
        if (player == null || !player.isOnline()) return;
        // This is the chest crate for staff to be placed!

        Crate crate = CratesPlus.crates.get(crateType.toLowerCase());

        ItemStack crateItem = new ItemStack(Material.CHEST);
        ItemMeta crateMeta = crateItem.getItemMeta();
        crateMeta.setDisplayName(crate.getName(true) + " Crate!");
        List<String> lore = new ArrayList<String>();
        lore.add(ChatColor.GRAY + "Place this crate somewhere!");
        lore.add("");
        crateMeta.setLore(lore);
        crateItem.setItemMeta(crateMeta);
        player.getInventory().addItem(crateItem);
        player.sendMessage(CratesPlus.pluginPrefix + ChatColor.GREEN + "You have been given a " + CratesPlus.crates.get(crateType.toLowerCase()).getColor() + crateType + ChatColor.GREEN + " crate!");
    }

    @Deprecated
    public static ItemStack stringToItemstackOld(String i) {
        String[] args = i.split(":", -1);
        if (args.length >= 2 && args[0].equalsIgnoreCase("command")) {
            /** Commands */
            String command = args[1];
            String title = "Command: /" + command;
            if (args.length == 3) {
                title = args[2];
            }
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
            ItemStack itemStack = new ItemStack(Material.EMPTY_MAP);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(ChatColor.RESET + title);
            itemStack.setItemMeta(itemMeta);
            return itemStack;
        } else if (args.length == 1) {
            /** Item without any amounts or enchantments */
            String[] args1 = args[0].split("-");
            ItemStack itemStack;
            if (args1.length == 1) {
                itemStack = new ItemStack(Material.getMaterial(args1[0].toUpperCase()));
            } else {
                itemStack = new ItemStack(Material.getMaterial(args1[0].toUpperCase()), 1, Byte.parseByte(args1[1]));
            }
            return itemStack;
        } else if (args.length == 2) {
            return new ItemStack(Material.getMaterial(args[0].toUpperCase()), Integer.parseInt(args[1]));
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
            return itemStack;
        }
        return null;
    }

    public static ItemStack stringToItemstack(String i, Player player, boolean isWin) {
        String[] args = i.split(":", -1);
        if (args.length >= 2 && args[0].equalsIgnoreCase("command")) {
            /** Commands */
            String command = args[1];
            String title = "Command: /" + command;
            if (args.length == 3) {
                title = args[2];
            }
            command = command.replaceAll("%name%", player.getName());
            title = title.replaceAll("%name%", player.getName());
            if (isWin)
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
            ItemStack itemStack = new ItemStack(Material.EMPTY_MAP);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(ChatColor.RESET + title);
            itemStack.setItemMeta(itemMeta);
            return itemStack;
        } else if (args.length == 1) {
            /** Item without any amounts, custom name or enchantments */
            String[] args1 = args[0].split("-");
            ItemStack itemStack;
            if (args1.length == 1) {
                itemStack = new ItemStack(Material.getMaterial(args1[0].toUpperCase()));
            } else {
                itemStack = new ItemStack(Material.getMaterial(args1[0].toUpperCase()), 1, Byte.parseByte(args1[1]));
            }
            return itemStack;
        } else if (args.length == 2) {
            String[] args1 = args[0].split("-");
            if (args1.length == 1) {
                return new ItemStack(Material.getMaterial(args1[0].toUpperCase()), Integer.parseInt(args[1]));
            } else {
                return new ItemStack(Material.getMaterial(args1[0].toUpperCase()), Integer.parseInt(args[1]), Byte.parseByte(args1[1]));
            }
        } else if (args.length == 3) {
            if (args[2].equalsIgnoreCase("NONE")) {
                String[] args1 = args[0].split("-");
                if (args1.length == 1) {
                    return new ItemStack(Material.getMaterial(args1[0].toUpperCase()), Integer.parseInt(args[1]));
                } else {
                    return new ItemStack(Material.getMaterial(args1[0].toUpperCase()), Integer.parseInt(args[1]), Byte.parseByte(args1[1]));
                }
            } else {
                String[] args1 = args[0].split("-");
                ItemStack itemStack;
                if (args1.length == 1) {
                    itemStack = new ItemStack(Material.getMaterial(args1[0].toUpperCase()), Integer.parseInt(args[1]));
                } else {
                    itemStack = new ItemStack(Material.getMaterial(args1[0].toUpperCase()), Integer.parseInt(args[1]), Byte.parseByte(args1[1]));
                }
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', args[2]));
                itemStack.setItemMeta(itemMeta);
                return itemStack;
            }
        } else if (args.length == 4) {
            String[] enchantments = args[3].split("\\|", -1);
            String[] args1 = args[0].split("-");
            ItemStack itemStack;
            if (args1.length == 1) {
                itemStack = new ItemStack(Material.getMaterial(args1[0].toUpperCase()), Integer.parseInt(args[1]));
            } else {
                itemStack = new ItemStack(Material.getMaterial(args1[0].toUpperCase()), Integer.parseInt(args[1]), Byte.parseByte(args1[1]));
            }
            for (String e : enchantments) {
                args1 = e.split("-", -1);
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
            if (!args[2].equalsIgnoreCase("NONE")) {
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', args[2]));
                itemStack.setItemMeta(itemMeta);
            }
            return itemStack;
        }
        return null;
    }

    public static String itemstackToString(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() == Material.AIR)
            return null;

        String finalString = "";
        finalString = finalString + itemStack.getType().toString();
        if (itemStack.getData().getData() != 0) {
            finalString = finalString + "-" + itemStack.getData().getData();
        }
        finalString = finalString + ":" + itemStack.getAmount();

        if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName()) {
            finalString = finalString + ":" + itemStack.getItemMeta().getDisplayName();
        } else {
            finalString = finalString + ":NONE";
        }

        int i = 0;
        for (Map.Entry<Enchantment, Integer> entry : itemStack.getEnchantments().entrySet()) {
            Enchantment enchantment = entry.getKey();
            Integer level = entry.getValue();
            if (i == 0) {
                finalString = finalString + ":";
            } else {
                finalString = finalString + "|";
            }
            if (level > 1) {
                finalString = finalString + enchantment.getName().toUpperCase() + "-" + level;
            } else {
                finalString = finalString + enchantment.getName().toUpperCase();
            }
            i++;
        }

        return finalString;
    }

}
