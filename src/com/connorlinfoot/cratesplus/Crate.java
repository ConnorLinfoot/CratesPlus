package com.connorlinfoot.cratesplus;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class Crate {
    private String name;
    private ChatColor color;
    private Material block = Material.CHEST;
    private boolean firework = false;
    private boolean broadcast = false;
    private double knockback = 0.0;
    private ArrayList<ItemStack> items = new ArrayList<ItemStack>();

    public Crate(String name) {

    }

}
