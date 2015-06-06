package com.connorlinfoot.cratesplus;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class Crate {
    private String name;
    private String slug;
    private ChatColor color;
    private Material block = Material.CHEST;
    private boolean firework = false;
    private boolean broadcast = false;
    private double knockback = 0.0;
    private List<String> items = new ArrayList<String>();

    public Crate(String name) {
        this.name = name;
        this.slug = name.toLowerCase();
        this.color = ChatColor.valueOf(CratesPlus.getPlugin().getConfig().getString("Crates." + name + ".Color").toUpperCase());
        // TODO: Add custom blocks for crates
//        this.block = Material.valueOf(CratesPlus.getPlugin().getConfig().getString("Crates." + name + ".Block").toUpperCase());
        this.firework = CratesPlus.getPlugin().getConfig().getBoolean("Crates." + name + ".Firework");
        this.broadcast = CratesPlus.getPlugin().getConfig().getBoolean("Crates." + name + ".Broadcast");
        this.knockback = CratesPlus.getPlugin().getConfig().getDouble("Crates." + name + ".Knockback");
        this.items = CratesPlus.getPlugin().getConfig().getStringList("Crates." + name + ".Items");
    }

    public String getName() {
        return getName(false);
    }

    public String getName(boolean includecolor) {
        if (includecolor) return getColor() + this.name;
        return this.name;
    }

    public ChatColor getColor() {
        return this.color;
    }

    public Material getBlock() {
        return this.block;
    }

    public boolean isFirework() {
        return this.firework;
    }

    public boolean isBroadcast() {
        return this.broadcast;
    }

    public double getKnockback() {
        return this.knockback;
    }

    public void reloadItems() {
        CratesPlus.getPlugin().reloadConfig();
        this.items = CratesPlus.getPlugin().getConfig().getStringList("Crates." + name + ".Items");
    }

    public List<String> getItems() {
        return this.items;
    }

}
