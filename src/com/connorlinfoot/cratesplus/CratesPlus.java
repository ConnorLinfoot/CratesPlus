package com.connorlinfoot.cratesplus;

import com.connorlinfoot.cratesplus.Commands.CrateCommand;
import com.connorlinfoot.cratesplus.Listeners.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;

public class CratesPlus extends JavaPlugin implements Listener {
    private static CratesPlus instance;
    public static HashMap<String, Crate> crates = new HashMap<String, Crate>();
    public static boolean updateAvailable = false;
    public static String updateMessage = "";
    public static String pluginPrefix = ChatColor.GRAY + "[" + ChatColor.AQUA + "CratesPlus" + ChatColor.GRAY + "] " + ChatColor.RESET;

    public void onEnable() {
        instance = this;
        getConfig().options().copyDefaults(true);
        saveConfig();
        Server server = getServer();
        final ConsoleCommandSender console = server.getConsoleSender();
        if (getConfig().isSet("Crate Knockback") || (getConfig().isSet("Config Version") && getConfig().getInt("Config Version") < 2)) {
            convertConfig(console);
        }

        // Do Prefix
        pluginPrefix = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Messages.Prefix")) + " " + ChatColor.RESET;

        // Register Crates
        for (String crate : getConfig().getConfigurationSection("Crates").getKeys(false)) {
            crates.put(crate, new Crate(crate));
        }

        // Register /crate command
        Bukkit.getPluginCommand("crate").setExecutor(new CrateCommand());

        // Register Events
        Bukkit.getPluginManager().registerEvents(new BlockListeners(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoin(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryInteract(), this);
        if (getConfig().getBoolean("Use Interact")) {
            Bukkit.getPluginManager().registerEvents(new ChestInteract(), this);
        } else {
            Bukkit.getPluginManager().registerEvents(new ChestOpen(), this);
        }

        getServer().getScheduler().runTaskLaterAsynchronously(this, new Runnable() {
            public void run() {
                checkUpdate(console, getConfig().getBoolean("Update Checks"));
            }
        }, 10L);

        console.sendMessage("");
        console.sendMessage(ChatColor.BLUE + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
        console.sendMessage("");
        console.sendMessage(ChatColor.AQUA + getDescription().getName());
        console.sendMessage(ChatColor.AQUA + "Version " + getDescription().getVersion());
        console.sendMessage("");
        console.sendMessage(ChatColor.BLUE + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
        console.sendMessage("");
    }

    private void convertConfig(ConsoleCommandSender console) {
        console.sendMessage(pluginPrefix + ChatColor.GREEN + "Converting config to version 2...");

        // Convert crate items
        if (getConfig().isSet("Crate Items.Common")) {
            List<String> oldCommonItems = getConfig().getStringList("Crate Items.Common");
            getConfig().set("Crates.Common.Items", oldCommonItems);
        }
        if (getConfig().isSet("Crate Items.Rare")) {
            List<String> oldRareItems = getConfig().getStringList("Crate Items.Rare");
            getConfig().set("Crates.Rare.Items", oldRareItems);
        }
        if (getConfig().isSet("Crate Items.Ultra")) {
            List<String> oldUltraItems = getConfig().getStringList("Crate Items.Ultra");
            getConfig().set("Crates.Ultra.Items", oldUltraItems);
        }

        // Convert knockback settings
        if (getConfig().isSet("Crate Knockback.Common")) {
            double oldCommonKnockback = getConfig().getDouble("Crate Knockback.Common");
            getConfig().set("Crates.Common.Knockback", oldCommonKnockback);
        }
        if (getConfig().isSet("Crate Knockback.Rare")) {
            double oldRareKnockback = getConfig().getDouble("Crate Knockback.Rare");
            getConfig().set("Crates.Rare.Knockback", oldRareKnockback);
        }
        if (getConfig().isSet("Crate Knockback.Ultra")) {
            double oldUltraKnockback = getConfig().getDouble("Crate Knockback.Ultra");
            getConfig().set("Crates.Ultra.Knockback", oldUltraKnockback);
        }

        // Convert broadcast settings
        if (getConfig().isSet("Broadcast On Crate Open.Common")) {
            boolean oldCommonBroadcast = getConfig().getBoolean("Broadcast On Crate Open.Common");
            getConfig().set("Crates.Common.Broadcast", oldCommonBroadcast);
        }
        if (getConfig().isSet("Broadcast On Crate Open.Rare")) {
            boolean oldRareBroadcast = getConfig().getBoolean("Broadcast On Crate Open.Rare");
            getConfig().set("Crates.Rare.Broadcast", oldRareBroadcast);
        }
        if (getConfig().isSet("Broadcast On Crate Open.Ultra")) {
            boolean oldUltraBroadcast = getConfig().getBoolean("Broadcast On Crate Open.Ultra");
            getConfig().set("Crates.Ultra.Broadcast", oldUltraBroadcast);
        }

        // Convert firework settings
        if (getConfig().isSet("Firework On Crate Open.Common")) {
            boolean oldCommonFirework = getConfig().getBoolean("Firework On Crate Open.Common");
            getConfig().set("Crates.Common.Firework", oldCommonFirework);
        }
        if (getConfig().isSet("Firework On Crate Open.Rare")) {
            boolean oldRareFirework = getConfig().getBoolean("Firework On Crate Open.Rare");
            getConfig().set("Crates.Rare.Firework", oldRareFirework);
        }
        if (getConfig().isSet("Firework On Crate Open.Ultra")) {
            boolean oldUltraFirework = getConfig().getBoolean("Firework On Crate Open.Ultra");
            getConfig().set("Crates.Ultra.Firework", oldUltraFirework);
        }

        // Clear all old config
        if (getConfig().isSet("Crate Items"))
            getConfig().set("Crate Items", null);
        if (getConfig().isSet("Crate Knockback"))
            getConfig().set("Crate Knockback", null);
        if (getConfig().isSet("Broadcast On Crate Open"))
            getConfig().set("Broadcast On Crate Open", null);
        if (getConfig().isSet("Firework On Crate Open"))
            getConfig().set("Firework On Crate Open", null);

        // Set config version
        getConfig().set("Config Version", 2);

        // Save config
        saveConfig();

        console.sendMessage(pluginPrefix + ChatColor.GREEN + "Conversion of config has completed.");
    }

    public static CratesPlus getPlugin() {
        return instance;
    }

    private void checkUpdate(final ConsoleCommandSender console, final boolean enabled) {
        final Updater updater = new Updater(this, 5018, !enabled);
        final Updater.UpdateResult result = updater.getResult();
        switch (result) {
            default:
                break;
            case BAD_RESOURCEID:
            case FAIL_NOVERSION:
            case FAIL_SPIGOT:
                updateAvailable = false;
                updateMessage = pluginPrefix + "Failed to check for updates. Will try again later.";
                getServer().getScheduler().runTaskLaterAsynchronously(this, new Runnable() {
                    public void run() {
                        checkUpdate(console, enabled);
                    }
                }, 60 * (60 * 20L)); // Checks again an hour later
                break;
            case NO_UPDATE:
                updateAvailable = false;
                updateMessage = pluginPrefix + "No update was found, you are running the latest version. Will check again later.";
                getServer().getScheduler().runTaskLaterAsynchronously(this, new Runnable() {
                    public void run() {
                        checkUpdate(console, enabled);
                    }
                }, 60 * (60 * 20L)); // Checks again an hour later
                break;
            case DISABLED:
                updateAvailable = false;
                updateMessage = pluginPrefix + "You currently have update checks disabled";
                break;
            case UPDATE_AVAILABLE:
                updateAvailable = true;
                updateMessage = pluginPrefix + "An update for CratesPlus is available, new version is " + updater.getVersion() + ". Your installed version is " + getDescription().getVersion() + ".\nPlease update to the latest version :)";
                break;
            case MAJOR_UPDATE_AVALIABLE:
                updateAvailable = true;
                updateMessage = pluginPrefix + "A major update for CratesPlus is available, new version is " + updater.getVersion() + ". Your installed version is " + getDescription().getVersion() + ".\nPlease update to the latest version :)";
                break;
        }
        console.sendMessage(updateMessage);
    }
}
