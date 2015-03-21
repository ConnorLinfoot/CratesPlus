package com.connorlinfoot.cratesplus;

import com.connorlinfoot.cratesplus.Commands.CrateCommand;
import com.connorlinfoot.cratesplus.Listeners.BlockListeners;
import com.connorlinfoot.cratesplus.Listeners.ChestOpen;
import com.connorlinfoot.cratesplus.Listeners.InventoryInteract;
import com.connorlinfoot.cratesplus.Listeners.PlayerJoin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class CratesPlus extends JavaPlugin implements Listener {
    private static CratesPlus instance;
    public static boolean updateAvailable = false;
    public static String updateMessage = "";
    public static String pluginPrefix = ChatColor.GRAY + "[" + ChatColor.AQUA + "CratesPlus" + ChatColor.GRAY + "] " + ChatColor.RESET;

    public void onEnable() {
        instance = this;
        getConfig().options().copyDefaults(true);
        saveConfig();
        Server server = getServer();
        final ConsoleCommandSender console = server.getConsoleSender();

        console.sendMessage("");
        console.sendMessage(ChatColor.BLUE + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
        console.sendMessage("");
        console.sendMessage(ChatColor.AQUA + getDescription().getName());
        console.sendMessage(ChatColor.AQUA + "Version " + getDescription().getVersion());
        console.sendMessage("");
        console.sendMessage(ChatColor.BLUE + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
        console.sendMessage("");

        // Register /crate command
        Bukkit.getPluginCommand("crate").setExecutor(new CrateCommand());

        // Register Events
        Bukkit.getPluginManager().registerEvents(new BlockListeners(), this);
        Bukkit.getPluginManager().registerEvents(new ChestOpen(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoin(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryInteract(), this);

        getServer().getScheduler().runTaskLaterAsynchronously(this, new Runnable() {
            public void run() {
                checkUpdate(console, getConfig().getBoolean("Update Checks"));
            }
        }, 10L);
    }

    public void onDisable() {
        getLogger().info(getDescription().getName() + " has been disabled!");
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
