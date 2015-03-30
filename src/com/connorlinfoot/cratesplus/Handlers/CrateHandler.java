package com.connorlinfoot.cratesplus.Handlers;

import com.connorlinfoot.cratesplus.CratesPlus;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        Integer number = randInt(0, 100);
        // TODO FIX THIS
        // Not really good at a way to do this, hoping this works. Any ideas on improvements send them! :)
//        if (number >= CratesPlus.getPlugin().getConfig().getInt("Crate Chances.Common")) {
//            giveCrateKey(player, CrateType.COMMON);
//        } else if (number >= CratesPlus.getPlugin().getConfig().getInt("Crate Chances.Rare")) {
//            giveCrateKey(player, CrateType.RARE);
//        } else if (number >= CratesPlus.getPlugin().getConfig().getInt("Crate Chances.Ultra")) {
//            giveCrateKey(player, CrateType.ULTRA);
//        } else {
//            giveCrateKey(player, CrateType.COMMON);
//        }
    }

    public static void giveCrateKey(Player player, String crateType) {
        if (player == null || !player.isOnline()) return;

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
        String title = CratesPlus.getPlugin().getConfig().getString("Crate Keys.Name").replaceAll("%type%", CratesPlus.crates.get(crateType) + crateType);
        keyMeta.setDisplayName(title);
        List<String> lore = new ArrayList<String>();
        lore.add(ChatColor.DARK_GRAY + "Right-Click on a \"" + CratesPlus.crates.get(crateType) + crateType + ChatColor.DARK_GRAY + "\" crate");
        lore.add(ChatColor.DARK_GRAY + "to win an item!");
        lore.add("");
        keyMeta.setLore(lore);
        key.setItemMeta(keyMeta);
        player.getInventory().addItem(key);
        player.sendMessage(CratesPlus.pluginPrefix + MessageHandler.getMessage(CratesPlus.getPlugin(), "Key Given", player, crateType));
    }

    public static void giveCrate(Player player, String crateType) {
        if (player == null || !player.isOnline()) return;
        // This is the chest crate for staff to be placed!

        ItemStack crate = new ItemStack(Material.CHEST);
        ItemMeta crateMeta = crate.getItemMeta();
        crateMeta.setDisplayName(CratesPlus.crates.get(crateType) + crateType + " Crate!");
        List<String> lore = new ArrayList<String>();
        lore.add(ChatColor.DARK_GRAY + "Place this crate somewhere!");
        lore.add("");
        crateMeta.setLore(lore);
        crate.setItemMeta(crateMeta);
        player.getInventory().addItem(crate);
        player.sendMessage(CratesPlus.pluginPrefix + ChatColor.GREEN + "You have been given a " + CratesPlus.crates.get(crateType) + crateType + ChatColor.GREEN + " crate!");
    }

}

