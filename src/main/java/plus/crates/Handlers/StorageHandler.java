package plus.crates.Handlers;

import org.bukkit.configuration.file.YamlConfiguration;
import plus.crates.CratesPlus;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * // TODO - Perhaps rewrite to support other plugins being able to extend and create custom storage handlers?
 */
public class StorageHandler {
    private CratesPlus cratesPlus;
    private StorageType storageType;
    private File flatFile;
    private YamlConfiguration flatConfig;

    public enum StorageType {
        FLAT, SQLITE, MYSQL
    }

    public StorageHandler(CratesPlus cratesPlus, StorageType storageType) {
        this.cratesPlus = cratesPlus;
        this.storageType = storageType;
        setupStorage();
    }

    private void setupStorage() {
        // Configure the flat file no matter what, we'll still use this for "Crate Locations" and maybe other data that is per instance
        flatFile = new File(cratesPlus.getDataFolder(), "data.yml");
        flatConfig = YamlConfiguration.loadConfiguration(flatFile);
        try {
            flatConfig.save(flatFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        updateDataFile();

        switch (getStorageType()) {
            case SQLITE:
                break;
            case MYSQL:
                break;
        }
    }

    private void updateDataFile() {
        if (!flatConfig.isSet("Data Version") || flatConfig.getInt("Data Version") == 1) {
            flatConfig.set("Data Version", 2);
            if (flatConfig.isSet("Crate Locations"))
                flatConfig.set("Crate Locations", null);
            try {
                flatConfig.save(flatFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public StorageType getStorageType() {
        return storageType;
    }

    public File getFlatFile() {
        return flatFile;
    }

    public YamlConfiguration getFlatConfig() {
        return flatConfig;
    }

    public void saveFlat() {
        try {
            flatConfig.save(flatFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object getPlayerData(UUID uuid, String key) {
        switch (getStorageType()) {
            case FLAT:
                return flatConfig.get("Player." + uuid.toString() + "." + key, null);
        }
        return null;
    }

    public void setPlayerData(UUID uuid, String key, String value) {

    }

    public void incPlayerData(UUID uuid, String key, Integer value) {
        switch (getStorageType()) {
            case FLAT:
                Integer current = flatConfig.getInt("Player." + uuid.toString() + "." + key, 0);
                if (value > 0) {
                    current += value;
                } else if (value < 0) {
                    current -= value;
                }
                flatConfig.set("Player." + uuid.toString() + "." + key, current);
                saveFlat();
                break;
        }
    }

}
