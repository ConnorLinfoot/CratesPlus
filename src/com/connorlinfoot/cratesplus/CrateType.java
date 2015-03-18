package com.connorlinfoot.cratesplus;

import org.bukkit.ChatColor;

public enum CrateType {
    COMMON("Common"), RARE("Rare"), ULTRA("Ultra"), UNKNOWN("Unknown");

    private String code;

    private CrateType(String string) {
        code = string;
    }

    public String getCode() {
        return code;
    }

    public String getCode(boolean includeColor) {
        if (includeColor) {
            if (code.equalsIgnoreCase("common")) {
                return ChatColor.GREEN + code + ChatColor.RESET;
            } else if (code.equalsIgnoreCase("rare")) {
                return ChatColor.GOLD + code + ChatColor.RESET;
            } else if (code.equalsIgnoreCase("ultra")) {
                return ChatColor.DARK_PURPLE + code + ChatColor.RESET;
            }
        }
        return code;
    }
}
