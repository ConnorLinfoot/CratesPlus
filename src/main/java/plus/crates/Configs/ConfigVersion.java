package plus.crates.Configs;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import plus.crates.CratesPlus;

public abstract class ConfigVersion {
    private final CratesPlus cratesPlus;
    private final Integer version;

    public ConfigVersion(CratesPlus cratesPlus, Integer version) {
        this.cratesPlus = cratesPlus;
        this.version = version;
    }

    public CratesPlus getCratesPlus() {
        return cratesPlus;
    }

    public Integer getVersion() {
        return version;
    }

    public boolean shouldUpdate() {
        return shouldUpdate(false);
    }

    public boolean shouldUpdate(boolean actuallyUpdate) {
        if (!getConfig().isSet("Config Version") || getConfig().getInt("Config Version") >= getVersion()) {
            return false;
        }

        if (actuallyUpdate) {
            runUpdate();
        }
        return true;
    }

    private String backupConfig() {
        return getCratesPlus().uploadFile("config.yml");
    }

    protected void runUpdate() {
        String configBackup = backupConfig();
        ConsoleCommandSender console = Bukkit.getConsoleSender();

        console.sendMessage(getCratesPlus().getPluginPrefix() + ChatColor.GREEN + "Converting config to version " + getVersion() + "...");

        update();
        getConfig().set("Config Version", getVersion());
        save();

        console.sendMessage(getCratesPlus().getPluginPrefix() + ChatColor.GREEN + "Conversion of config has completed.");
        if (configBackup != null && !configBackup.equalsIgnoreCase("")) {
            getCratesPlus().setConfigBackup(configBackup);
            console.sendMessage(getCratesPlus().getPluginPrefix() + ChatColor.GREEN + "Your old config was backed up to " + configBackup);
        }
    }

    protected abstract void update();

    public void directMap(String from, String to) {
        directMap(from, to, true);
    }

    public void directMap(String from, String to, boolean delete) {
        if (getConfig().isSet(from)) {
            getConfig().set(to, getConfig().get(from));
            if (delete)
                delete(from);
        }
    }

    public void delete(String path) {
        if (getConfig().isSet(path))
            getConfig().set(path, null);
    }

    public void save() {
        cratesPlus.saveConfig();
    }

    protected FileConfiguration getConfig() {
        return getCratesPlus().getConfig();
    }
}
