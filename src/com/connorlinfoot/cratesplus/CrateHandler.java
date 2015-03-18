package com.connorlinfoot.cratesplus;

import org.bukkit.*;
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
        Color c = null;
        if (i == 1) {
            c = Color.AQUA;
        }
        if (i == 2) {
            c = Color.BLACK;
        }
        if (i == 3) {
            c = Color.BLUE;
        }
        if (i == 4) {
            c = Color.FUCHSIA;
        }
        if (i == 5) {
            c = Color.GRAY;
        }
        if (i == 6) {
            c = Color.GREEN;
        }
        if (i == 7) {
            c = Color.LIME;
        }
        if (i == 8) {
            c = Color.MAROON;
        }
        if (i == 9) {
            c = Color.NAVY;
        }
        if (i == 10) {
            c = Color.OLIVE;
        }
        if (i == 11) {
            c = Color.ORANGE;
        }
        if (i == 12) {
            c = Color.PURPLE;
        }
        if (i == 13) {
            c = Color.RED;
        }
        if (i == 14) {
            c = Color.SILVER;
        }
        if (i == 15) {
            c = Color.TEAL;
        }
        if (i == 16) {
            c = Color.WHITE;
        }
        if (i == 17) {
            c = Color.YELLOW;
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
        // Not really good at a way to do this, hoping this works. Any ideas on improvements send them! :)
        if (number >= CratesPlus.getPlugin().getConfig().getInt("Crate Chances.Common")) {
            giveCrateKey(player, CrateType.COMMON);
        } else if (number >= CratesPlus.getPlugin().getConfig().getInt("Crate Chances.Rare")) {
            giveCrateKey(player, CrateType.RARE);
        } else if (number >= CratesPlus.getPlugin().getConfig().getInt("Crate Chances.Ultra")) {
            giveCrateKey(player, CrateType.ULTRA);
        } else {
            giveCrateKey(player, CrateType.COMMON);
        }
    }

    public static void giveCrateKey(Player player, CrateType crateType) {
        if (player == null || !player.isOnline()) return;

        ItemStack key = new ItemStack(Material.TRIPWIRE_HOOK);
        ItemMeta keyMeta = key.getItemMeta();
        keyMeta.setDisplayName(crateType.getCode(true) + " Crate Key!");
        List<String> lore = new ArrayList<String>();
        lore.add(ChatColor.DARK_GRAY + "Right-Click on a \"" + crateType.getCode(true) + ChatColor.DARK_GRAY + "\" crate");
        lore.add(ChatColor.DARK_GRAY + "to win an item!");
        lore.add("");
        keyMeta.setLore(lore);
        key.setItemMeta(keyMeta);
        player.getInventory().addItem(key);
        player.sendMessage(CratesPlus.pluginPrefix + ChatColor.GREEN + "You have been given a " + crateType.getCode(true) + ChatColor.GREEN + " crate key!");
    }

    public static void giveCrate(Player player, CrateType crateType) {
        if (player == null || !player.isOnline()) return;
        // This is the chest crate for staff to be placed!

        ItemStack crate = new ItemStack(Material.CHEST);
        ItemMeta crateMeta = crate.getItemMeta();
        crateMeta.setDisplayName(crateType.getCode(true) + " Crate!");
        List<String> lore = new ArrayList<String>();
        lore.add(ChatColor.DARK_GRAY + "Place this crate somewhere!");
        lore.add("");
        crateMeta.setLore(lore);
        crate.setItemMeta(crateMeta);
        player.getInventory().addItem(crate);
        player.sendMessage(CratesPlus.pluginPrefix + ChatColor.GREEN + "You have been given a " + crateType.getCode(true) + ChatColor.GREEN + " crate!");
    }

}

