package plus.crates.Handlers;

import org.bukkit.configuration.file.FileConfiguration;
import plus.crates.Configs.*;
import plus.crates.Crates.*;
import plus.crates.CratesPlus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConfigHandler {
    private final CratesPlus cratesPlus;
    private Integer defaultCooldown = 5;
    private Integer crateGUITime = 10;
    private Integer claimMessageDelay = 0;
    private List<String> defaultHologramText;
    private HashMap<String, List<String>> holograms = new HashMap<>();
    private HashMap<String, Crate> crates = new HashMap<>();
    private boolean disableKeySwapping = false;
    private boolean debugMode = false;
    private List<ConfigVersion> configVersions = new ArrayList<>();

    public ConfigHandler(FileConfiguration config, CratesPlus cratesPlus) {
        this.cratesPlus = cratesPlus;

        // Register config versions
        configVersions.add(new Version2(cratesPlus));
        configVersions.add(new Version3(cratesPlus));
        configVersions.add(new Version4(cratesPlus));
        configVersions.add(new Version5(cratesPlus));
        configVersions.add(new Version6(cratesPlus));
        configVersions.add(new Version7(cratesPlus));

        // Actually update configs
        updateConfigs();

        // Load configuration
        if (config.isSet("Cooldown")) {
            config.set("Default Cooldown", config.getInt("Cooldown"));
            config.set("Cooldown", null);
            cratesPlus.saveConfig();
        }

        if (config.isSet("Debug Mode")) {
            debugMode = config.getBoolean("Debug Mode", false);
        }

        if (config.isSet("Disable Key Dropping")) {
            config.set("Disable Key Swapping", config.getBoolean("Disable Key Dropping"));
            config.set("Disable Key Dropping", null);
            cratesPlus.saveConfig();
        }

        if (config.isSet("Claim Message Delay"))
            claimMessageDelay = config.getInt("Claim Message Delay", 0);

        if (config.isSet("Disable Key Swapping"))
            disableKeySwapping = config.getBoolean("Disable Key Swapping");

        if (config.isSet("Default Cooldown"))
            setDefaultCooldown(config.getInt("Default Cooldown"));

        // Register Crates
        if (config.isSet("Crates")) {
            for (String crate : config.getConfigurationSection("Crates").getKeys(false)) {
                registerCrate(cratesPlus, config, crate);
            }
        }

        // Crate GUI
        if (config.isSet("Use GUI")) {
            if (config.getBoolean("Use GUI")) {
                config.set("Default Opener", "BasicGUI");
            } else {
                config.set("Default Opener", "NoGUI");
            }
            config.set("Use GUI", null);
            cratesPlus.saveConfig();
        }

        // TODO Load openers here?

        // Crate GUI Time, this is now moved into the BasicGUI opener
        if (config.isSet("GUI Time")) {
            crateGUITime = config.getInt("GUI Time");
            config.set("GUI Time", null);
            cratesPlus.saveConfig();
        }

        // Crate Hologram
        defaultHologramText = config.getStringList("Default Hologram Text");

        for (String crateLowerName : crates.keySet()) {
            Crate crate = crates.get(crateLowerName);
            List<String> crateSpecificHologram = config.getStringList("Crates." + crate.getName() + ".Hologram Text");
            holograms.put(crate.getName().toLowerCase(), (config.isSet("Crates." + crate.getName() + ".Hologram Text")) ? crateSpecificHologram : defaultHologramText);
        }
    }

    private void updateConfigs() {
        for (ConfigVersion configVersion : configVersions) {
            configVersion.shouldUpdate(true);
        }
    }

    public Integer getDefaultCooldown() {
        return defaultCooldown;
    }

    public void setDefaultCooldown(int defaultCooldown) {
        this.defaultCooldown = defaultCooldown;
    }

    public void setCrates(HashMap<String, Crate> crates) {
        this.crates = crates;
    }

    public void addCrate(String name, Crate crate) {
        this.crates.put(name, crate);
    }

    public Crate getCrate(String name) {
        if (this.crates.containsKey(name))
            return this.crates.get(name);
        return null;
    }

    public HashMap<String, Crate> getCrates() {
        return this.crates;
    }

    public List<String> getHolograms(String crateType) {
        return this.holograms.get(crateType.toLowerCase());
    }

    @Deprecated
    public Integer getCrateGUITime() {
        return crateGUITime;
    }

    public boolean isDisableKeySwapping() {
        return disableKeySwapping;
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public Integer getClaimMessageDelay() {
        return claimMessageDelay;
    }

    public void registerCrate(CratesPlus cratesPlus, FileConfiguration config, String crateName) {
        if (config.isSet("Crates." + crateName)) {
            String type = config.getString("Crates." + crateName + ".Type", "");
            switch (type.toLowerCase().replaceAll(" ", "")) {
                case "keycrate":
                case "key":
                    addCrate(crateName.toLowerCase(), new KeyCrate(this, crateName));
                    break;
                /*case "virtual":
                case "virtualcrate":
                    addCrate(crateName.toLowerCase(), new VirtualCrate(this, crateName));
                    break;*/
                case "supplycrate":
                case "supply":
                    addCrate(crateName.toLowerCase(), new SupplyCrate(this, crateName));
                    break;
                case "dropcrate":
                case "drop":
                    addCrate(crateName.toLowerCase(), new DropCrate(this, crateName));
                    break;
                case "mystery":
                case "mysterycrate":
                case "mysterybox":
                    addCrate(crateName.toLowerCase(), new MysteryCrate(this, crateName));
                    break;
                default:
                    cratesPlus.getLogger().warning("Invalid \"Type\" set for crate \"" + crateName + "\"");
                    break;
            }
        }
    }

    public CratesPlus getCratesPlus() {
        return cratesPlus;
    }

}
