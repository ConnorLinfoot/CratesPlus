package com.connorlinfoot.cratesplus;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Winning {
    private boolean valid = false;
    private boolean command = false;
    private int percentage = 0;
    private ItemStack previewItemStack;
    private ItemStack winningItemStack;
    private List<String> commands = new ArrayList<String>();
    private List<String> lore = new ArrayList<String>();

    public Winning(String path) {
        FileConfiguration config = CratesPlus.getPlugin().getConfig();
        if (!config.isSet(path))
            return;

        if (!config.isSet(path + ".Type"))
            return;
        String type = config.getString(path + ".Type");
        ItemStack itemStack;
        if (type.equalsIgnoreCase("item") || type.equalsIgnoreCase("block")) {
            Material itemType = null;
            if (config.isSet(path + ".Item Type"))
                itemType = Material.getMaterial(config.getString(path + ".Item Type"));
            else if (config.isSet(path + ".Block Type"))
                itemType = Material.getMaterial(config.getString(path + ".Block Type"));

            if (itemType == null)
                return;

            Integer itemData = 0;
            if (config.isSet(path + ".Item Data"))
                itemData = config.getInt(path + ".Item Data");

            if (config.isSet(path + ".Percentage"))
                percentage = config.getInt(path + ".Percentage");

            Integer amount = 1;
            if (config.isSet(path + ".Amount"))
                amount = config.getInt(path + ".Amount");
            itemStack = new ItemStack(itemType, amount, Short.parseShort(String.valueOf(itemData)));
        } else if (type.equalsIgnoreCase("command")) {
            command = true;
            if (!config.isSet(path + ".Commands") || config.getStringList(path + ".Commands").size() == 0)
                return;
            commands = config.getStringList(path + ".Commands");

            Material itemType = Material.PAPER;
            if (config.isSet(path + ".Item Type"))
                itemType = Material.getMaterial(config.getString(path + ".Item Type"));

            if (itemType == null)
                return;

            Integer itemData = 0;
            if (config.isSet(path + ".Item Data"))
                itemData = config.getInt(path + ".Item Data");

            if (config.isSet(path + ".Percentage"))
                percentage = config.getInt(path + ".Percentage");

            Integer amount = 1;
            if (config.isSet(path + ".Amount"))
                amount = config.getInt(path + ".Amount");
            itemStack = new ItemStack(itemType, amount, Short.parseShort(String.valueOf(itemData)));
        } else {
            return;
        }
        ItemStack winningItemStack = itemStack.clone();
        ItemStack previewItemStack = itemStack.clone();

        boolean showAmountInTitle = false;
        int originalAmount = 0;
        if (previewItemStack.getAmount() > previewItemStack.getMaxStackSize()) { // Stop multiple stacks for the same item!
            originalAmount = previewItemStack.getAmount();
            showAmountInTitle = true;
            previewItemStack.setAmount(previewItemStack.getMaxStackSize());
        }

        ItemMeta previewItemStackItemMeta = previewItemStack.getItemMeta();
        String displayName = "";
        if (config.isSet(path + ".Name") && !config.getString(path + ".Name").equals("NONE"))
            displayName = ChatColor.translateAlternateColorCodes('&', config.getString(path + ".Name"));
        if (showAmountInTitle)
            displayName = displayName + " x" + originalAmount;
        if (!displayName.equals(""))
            previewItemStackItemMeta.setDisplayName(displayName);
        previewItemStack.setItemMeta(previewItemStackItemMeta);

        if (config.isSet(path + ".Enchantments")) {
            List<?> enchtantments = config.getList(path + ".Enchantments");
            for (Object object : enchtantments) {
                String enchantment = (String) object;
                String[] args = enchantment.split("-");
                try {
                    Integer level = 1;
                    if (args.length > 1)
                        level = Integer.valueOf(args[1]);
                    previewItemStack.addUnsafeEnchantment(Enchantment.getByName(args[0].toUpperCase()), level);
                } catch (Exception ignored) {
                }
            }
        }

        if (config.isSet(path + ".Lore")) {
            List<?> lines = config.getList(path + ".Lore");
            for (Object object : lines) {
                String line = (String) object;
                this.lore.add(ChatColor.translateAlternateColorCodes('&', line));
            }
        }


        ItemMeta winningItemStackItemMeta = winningItemStack.getItemMeta();
        displayName = "";
        if (config.isSet(path + ".Name") && !config.getString(path + ".Name").equals("NONE"))
            displayName = ChatColor.translateAlternateColorCodes('&', config.getString(path + ".Name"));
        if (!displayName.equals(""))
            winningItemStackItemMeta.setDisplayName(displayName);
        winningItemStack.setItemMeta(winningItemStackItemMeta);

        if (config.isSet(path + ".Enchantments")) {
            List<?> enchtantments = config.getList(path + ".Enchantments");
            for (Object object : enchtantments) {
                String enchantment = (String) object;
                String[] args = enchantment.split("-");
                try {
                    Integer level = 1;
                    if (args.length > 1)
                        level = Integer.valueOf(args[1]);
                    winningItemStack.addUnsafeEnchantment(Enchantment.getByName(args[0].toUpperCase()), level);
                } catch (Exception ignored) {
                }
            }
        }

        if (config.isSet(path + ".Lore")) {
            List<?> lines = config.getList(path + ".Lore");
            for (Object object : lines) {
                String line = (String) object;
                this.lore.add(ChatColor.translateAlternateColorCodes('&', line));
            }
        }


        previewItemStackItemMeta = previewItemStack.getItemMeta();
        List<String> lore = this.lore;
        if (percentage > 0) {
            // Percentage
            lore.add(ChatColor.LIGHT_PURPLE + "");
            lore.add("" + ChatColor.LIGHT_PURPLE + percentage + "% Chance");
            lore.add(ChatColor.LIGHT_PURPLE + "");
        }
        previewItemStackItemMeta.setLore(lore);
        previewItemStack.setItemMeta(previewItemStackItemMeta);

        // Done :D
        valid = true;
        this.previewItemStack = previewItemStack;
        this.winningItemStack = winningItemStack;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public ItemStack getPreviewItemStack() {
        return previewItemStack;
    }

    public ItemStack getWinningItemStack() {
        return winningItemStack;
    }

    public void runWin(Player player) {
        if (isCommand() && commands.size() > 0) {
            for (String command : commands) {
                command = command.replaceAll("%name%", player.getName());
                command = command.replaceAll("%uuid%", player.getUniqueId().toString());
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
            }
        } else if (!isCommand()) {
            player.getInventory().addItem(getWinningItemStack());
        }
    }

    public boolean isCommand() {
        return command;
    }

    public int getPercentage() {
        return percentage;
    }

    public List<String> getCommands() {
        return commands;
    }
}
