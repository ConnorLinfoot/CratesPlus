package com.connorlinfoot.cratesplus;

import com.connorlinfoot.cratesplus.Commands.CrateCommand;
import com.connorlinfoot.cratesplus.Listeners.*;
import com.connorlinfoot.cratesplus.Utils.PasteUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class CratesPlus extends JavaPlugin implements Listener {
    private static CratesPlus instance;
    public static HashMap<String, Crate> crates = new HashMap<String, Crate>();
    public static boolean updateAvailable = false;
    public static String updateMessage = "";
    public static String configBackup = null;
    public static String pluginPrefix = ChatColor.GRAY + "[" + ChatColor.AQUA + "CratesPlus" + ChatColor.GRAY + "] " + ChatColor.RESET;

    public void onEnable() {
        instance = this;
        Server server = getServer();
        final ConsoleCommandSender console = server.getConsoleSender();
        if (getConfig().isSet("Crate Knockback") || (getConfig().isSet("Config Version") && getConfig().getInt("Config Version") < 2)) {
            String oldConfig = backupConfig();
            convertConfig(console, oldConfig);
        }
        getConfig().options().copyDefaults(true);
        saveConfig();

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

        if (!getConfig().getBoolean("Update Checks")) {
            getServer().getScheduler().runTaskLaterAsynchronously(this, new Runnable() {
                public void run() {
                    checkUpdate(console,
                            getConfig().getString("Update Branch"),
                            getConfig().getBoolean("Auto Update"));
                }
            }, 10L);
        }

        console.sendMessage("");
        console.sendMessage(ChatColor.BLUE + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
        console.sendMessage("");
        console.sendMessage(ChatColor.AQUA + getDescription().getName());
        console.sendMessage(ChatColor.AQUA + "Version " + getDescription().getVersion());
        console.sendMessage("");
        console.sendMessage(ChatColor.BLUE + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
        console.sendMessage("");

        if (configBackup != null && Bukkit.getOnlinePlayers().size() > 0) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.hasPermission("cratesplus.admin")) {
                    player.sendMessage(pluginPrefix + ChatColor.GREEN + "Your config has been updated. Your old config was backed up to " + configBackup);
                    configBackup = null;
                }
            }
        }
    }

    private String backupConfig() {
        File file = new File("plugins/CratesPlus/config.yml");
        if (!file.exists())
            return null;
        LineIterator it;
        String lines = "";
        try {
            it = FileUtils.lineIterator(file, "UTF-8");
            try {
                while (it.hasNext()) {
                    String line = it.nextLine();
                    lines = lines + line + "\n";
                }
            } finally {
                it.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return PasteUtils.paste(lines);
    }

    private void convertConfig(ConsoleCommandSender console, String oldConfig) {
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
        if (oldConfig != null && !oldConfig.equalsIgnoreCase("")) {
            configBackup = oldConfig;
            console.sendMessage(pluginPrefix + ChatColor.GREEN + "Your old config was backed up to " + oldConfig);
        }
    }

    public static CratesPlus getPlugin() {
        return instance;
    }

    private void checkUpdate(final ConsoleCommandSender console, final String branch, final boolean install) {
        if (branch.equalsIgnoreCase("nightly")) {
            console.sendMessage(ChatColor.GREEN + "Oh, nightly builds? You're brave...");

        } else if (branch.equalsIgnoreCase("spigot")) {
            console.sendMessage(ChatColor.GREEN + "Checking for updates via Spigot...");
            final Updater updater = new Updater(this, 5018, false);
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
                            checkUpdate(console,
                                    branch,
                                    install);
                        }
                    }, 60 * (60 * 20L)); // Checks again an hour later
                    break;
                case NO_UPDATE:
                    updateAvailable = false;
                    updateMessage = pluginPrefix + "No update was found, you are running the latest version. Will check again later.";
                    getServer().getScheduler().runTaskLaterAsynchronously(this, new Runnable() {
                        public void run() {
                            checkUpdate(console,
                                    branch,
                                    install);
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
}
