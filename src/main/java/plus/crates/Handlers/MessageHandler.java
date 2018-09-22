package plus.crates.Handlers;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import plus.crates.Crates.Crate;
import plus.crates.Crates.Winning;
import plus.crates.CratesPlus;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class MessageHandler {
    private static CratesPlus cratesPlus;
    private static YamlConfiguration config;
    private static File file;
    private static HashMap<String, String> messages = new HashMap<>();
    public static boolean testMessages = false;

    public static void loadMessageConfiguration(CratesPlus cratesPlus, YamlConfiguration config, File file) {
        MessageHandler.cratesPlus = cratesPlus;
        MessageHandler.config = config;
        MessageHandler.file = file;

        handleConversion();

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleConversion() {
        if (cratesPlus == null || config == null || file == null) {
            return;
        }

        if (!config.isSet("Messages Version")) {
            config.set("Messages Version", 2);

            HashMap<String, String> oldKeys = new HashMap<>();
            oldKeys.put("Command No Permission", "&cYou do not have the correct permission to run this command");
            oldKeys.put("Crate No Permission", "&cYou do not have the correct permission to use this crate");
            oldKeys.put("Crate Open Without Key", "&cYou must be holding a %crate% &ckey to open this crate");
            oldKeys.put("Key Given", "&aYou have been given a %crate% &acrate key");
            oldKeys.put("Broadcast", "&d%displayname% &dopened a %crate% &dcrate");
            oldKeys.put("Cant Place", "&cYou can not place crate keys");
            oldKeys.put("Cant Drop", "&cYou can not drop crate keys");
            oldKeys.put("Chance Message", "&d%percentage%% Chance");
            oldKeys.put("Inventory Full Claim", "&aYou're inventory is full, you can claim your keys later using /crate");
            oldKeys.put("Claim Join", "&aYou currently have keys waiting to be claimed, use /crate to claim");
            oldKeys.put("Possible Wins Title", "Possible Wins:");

            oldKeys.forEach((key, value) -> {
                if (config.isSet(key)) {
                    config.set(value, config.getString(key));
                    config.set(key, null);
                }
            });

            cratesPlus.getConfig().set("Prefix", config.getString("Prefix", "&7[&bCratesPlus&7]"));
            config.set("Prefix", null);
            cratesPlus.getConfig().set("Chance Message Gap", config.getBoolean("Chance Message Gap", true));
            config.set("Chance Message Gap", null);
            cratesPlus.saveConfig();

            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String getMessageFromConfig(String message) {
        if (testMessages) {
            return "TRANSLATED(" + message + ChatColor.RESET + ") ";
        }
        if (messages.containsKey(message))
            return message;
        // TODO Insert into config reeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee
        return message;
    }

    public static String convertPlaceholders(String message, Player player, Crate crate, Winning winning) {
        message = ChatColor.translateAlternateColorCodes('&', message);
        if (player != null)
            message = message.replaceAll("%name%", player.getName()).replaceAll("%displayname%", player.getDisplayName()).replaceAll("%uuid%", player.getUniqueId().toString());

        if (crate != null)
            message = message.replaceAll("%crate%", crate.getName(true) + ChatColor.RESET);

        if (winning != null) {
            String name;
            if (winning.getWinningItemStack().hasItemMeta() && winning.getWinningItemStack().getItemMeta().hasDisplayName()) {
                name = winning.getWinningItemStack().getItemMeta().getDisplayName();
            } else {
                name = winning.getWinningItemStack().getType().name();
                name = name.substring(0, 1).toUpperCase() + name.substring(1);
            }
            message = message.replaceAll("%prize%", name + ChatColor.RESET).replaceAll("%winning%", name + ChatColor.RESET).replaceAll("%percentage%", String.valueOf(winning.getPercentage()));
        }
        return message;
    }

    public static String getMessage(String message, Player player, Crate crate, Winning winning) {
        message = getMessageFromConfig(message);
        return ChatColor.translateAlternateColorCodes('&', convertPlaceholders(message, player, crate, winning));
    }

    public static void sendMessage(Player player, String message, Crate crate, Winning winning) {
        player.sendMessage(cratesPlus.getPluginPrefix() + getMessage(message, player, crate, winning));
    }

}
