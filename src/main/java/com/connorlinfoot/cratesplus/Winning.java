package com.connorlinfoot.cratesplus;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class Winning {
    private boolean valid = false;
    private ItemStack itemStack;
    private List<String> commands;

    public Winning(String path) {
        FileConfiguration config = CratesPlus.getPlugin().getConfig();
        if (!config.isSet(path))
            return;

        if (!config.isSet(path + ".Type"))
            return;
        String type = config.getString(path + ".Type");
        ItemStack itemStack = null;
        if (type.equalsIgnoreCase("item")) {
            if (!config.isSet(path + ".Item Type"))
                return;
            Material itemType = Material.getMaterial(config.getString(path + ".Item Type"));
            if (itemType == null)
                return;

            Integer itemData = 0;
            if (config.isSet(path + ".Item Data"))
                itemData = config.getInt(path + ".Item Data");

            Integer amount = 1;
            if (config.isSet(path + ".Amount"))
                amount = config.getInt(path + ".Amount");
            itemStack = new ItemStack(itemType, amount, Short.parseShort(String.valueOf(itemData)));
        } else if (type.equalsIgnoreCase("command")) {
            // TODO
        } else {
            return;
        }
        if (itemStack == null)
            return;

        ItemMeta itemMeta = itemStack.getItemMeta();
        if (config.isSet(path + ".Name"))
            itemMeta.setDisplayName(config.getString(path + ".Name"));
        itemStack.setItemMeta(itemMeta);

        if (config.isSet(path + ".Enchantments")) {
            List<?> enchtantments = config.getList(path + ".Enchantments");
            for (Object object : enchtantments) {
                String enchantment = (String) object;
                String[] args = enchantment.split("-");
                try {
                    Integer level = 1;
                    if (args.length > 1)
                        level = Integer.valueOf(args[1]);
                    itemStack.addUnsafeEnchantment(Enchantment.getByName(args[0].toUpperCase()), level);
                } catch (Exception ignored) {
                }
            }
        }


        // Done :D
        valid = true;
        this.itemStack = itemStack;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void runWin() {
    }

}
