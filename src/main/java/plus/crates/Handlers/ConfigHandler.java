package plus.crates.Handlers;

import org.bukkit.configuration.file.FileConfiguration;
import plus.crates.Crate;
import plus.crates.CratesPlus;

import java.util.HashMap;
import java.util.List;

public class ConfigHandler {
	private int cooldown = 5;
	private HashMap<String, Crate> crates = new HashMap<>();
	private List<String> defaultHologramText;
	private HashMap<String, List<String>> holograms = new HashMap<>();
	private String defaultOpener = "NoGUI";
	private int crateGUITime = 10;

	public ConfigHandler(FileConfiguration config, CratesPlus cratesPlus) {
		// Load configuration
		if (config.isSet("Cooldown"))
			setCooldown(config.getInt("Cooldown"));

		// Register Crates
		if (config.isSet("Crates")) {
			for (String crate : config.getConfigurationSection("Crates").getKeys(false)) {
				addCrate(crate.toLowerCase(), new Crate(crate, cratesPlus));
			}
		}

		// Crate GUI
		if (config.isSet("Use GUI")) {
			config.set("Default Opener", "BasicGUI");
			config.set("Use GUI", null);
			cratesPlus.saveConfig();
		}

		// Default Opener
		if (config.isSet("Default Opener"))
			defaultOpener = config.getString("Default Opener");

		// Crate GUI Time // TODO Move into BasicGUI opener config instead
		if (config.isSet("GUI Time"))
			crateGUITime = config.getInt("GUI Time");

		// Crate Holograms
		defaultHologramText = config.getStringList("Default Hologram Text");

		for (String crate : crates.keySet()) {
			List<String> crateSpecificHologram = config.getStringList("Crates." + crate + ".Hologram Text");
			addHologram(crate.toLowerCase(), (config.isSet("Crates." + crate + ".Hologram Text")) ? crateSpecificHologram : defaultHologramText);
		}
	}

	public int getCooldown() {
		return cooldown;
	}

	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
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

	public HashMap<String, List<String>> getHolograms() {
		return this.holograms;
	}

	public List<String> getHologramsForCrate(String crateType) {
		return this.holograms.get(crateType.toLowerCase());
	}

	public void addHologram(String name, List<String> hologramLines) {
		this.holograms.put(name, hologramLines);
	}

	public int getCrateGUITime() {
		return crateGUITime;
	}

	public String getDefaultOpener() {
		return defaultOpener;
	}

}
