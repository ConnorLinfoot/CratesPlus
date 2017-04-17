package plus.crates.Handlers;

import org.bukkit.configuration.file.FileConfiguration;
import plus.crates.Crates.Crate;
import plus.crates.Crates.KeyCrate;
import plus.crates.CratesPlus;

import java.util.HashMap;
import java.util.List;

public class ConfigHandler {
	private Integer defaultCooldown = 5;
	private Integer crateGUITime = 10;
	private String defaultOpener = "NoGUI";
	private List<String> defaultHologramText;
	private HashMap<String, List<String>> holograms = new HashMap<>();
	private HashMap<String, Crate> crates = new HashMap<>();
	private boolean disableKeySwapping = false;
	private boolean debugMode = false;

	public ConfigHandler(FileConfiguration config, CratesPlus cratesPlus) {
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

		if (config.isSet("Disable Key Swapping"))
			disableKeySwapping = config.getBoolean("Disable Key Swapping");

		if (config.isSet("Default Cooldown"))
			setDefaultCooldown(config.getInt("Default Cooldown"));

		// Register Crates
		if (config.isSet("Crates")) {
			for (String crate : config.getConfigurationSection("Crates").getKeys(false)) {
				String type = config.getString("Crates." + crate + ".Type", "");
				switch (type.toLowerCase()) {
					case "keycrate":
						addCrate(crate.toLowerCase(), new KeyCrate(cratesPlus, crate));
						break;
					default:
						cratesPlus.getLogger().warning("Invalid \"Type\" set for crate \"" + crate + "\"");
						break;
				}
			}
		}

		// CrateOLD GUI
		if (config.isSet("Use GUI")) {
			if (config.getBoolean("Use GUI")) {
				config.set("Default Opener", "BasicGUI");
			} else {
				config.set("Default Opener", "NoGUI");
			}
			config.set("Use GUI", null);
			cratesPlus.saveConfig();
		}

		// Default Opener
		if (config.isSet("Default Opener"))
			defaultOpener = config.getString("Default Opener");

		// CrateOLD GUI Time, this is now moved into the BasicGUI opener
		if (config.isSet("GUI Time")) {
			crateGUITime = config.getInt("GUI Time");
			config.set("GUI Time", null);
			cratesPlus.saveConfig();
		}

		// CrateOLD Holograms
		defaultHologramText = config.getStringList("Default Hologram Text");

		for (String crateLowerName : crates.keySet()) {
			Crate crate = crates.get(crateLowerName);
			List<String> crateSpecificHologram = config.getStringList("Crates." + crate.getName() + ".Hologram Text");
			holograms.put(crate.getName().toLowerCase(), (config.isSet("Crates." + crate.getName() + ".Hologram Text")) ? crateSpecificHologram : defaultHologramText);
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

	public String getDefaultOpener() {
		return defaultOpener;
	}

	public boolean isDisableKeySwapping() {
		return disableKeySwapping;
	}

	public boolean isDebugMode() {
		return debugMode;
	}

}
