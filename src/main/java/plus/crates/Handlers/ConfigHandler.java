package plus.crates.Handlers;

import org.bukkit.configuration.file.FileConfiguration;
import plus.crates.Crate;

import java.util.HashMap;
import java.util.List;

public class ConfigHandler {
	private int cooldown = 5;
	private HashMap<String, Crate> crates = new HashMap<String, Crate>();
	public List<String> holograms;
	public boolean doGui = true;
	public int crateGUITime = 10;

	public ConfigHandler(FileConfiguration config) {
		// Load configuration
		if (config.isSet("Cooldown"))
			setCooldown(config.getInt("Cooldown"));

		// Register Crates
		if (config.isSet("Crates")) {
			for (String crate : config.getConfigurationSection("Crates").getKeys(false)) {
				addCrate(crate.toLowerCase(), new Crate(crate));
			}
		}

		// Crate GUI
		if (config.isSet("Use GUI"))
			doGui = config.getBoolean("Use GUI");

		// Crate GUI Time
		if (config.isSet("GUI Time"))
			crateGUITime = config.getInt("GUI Time");

		// Crate Holograms
		holograms = config.getStringList("Hologram Text");
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

	public List<String> getHolograms() {
		return holograms;
	}

	public void setHolograms(List<String> holograms) {
		this.holograms = holograms;
	}

	public boolean isDoGui() {
		return doGui;
	}

	public void setDoGui(boolean doGui) {
		this.doGui = doGui;
	}

	public int getCrateGUITime() {
		return crateGUITime;
	}

	public void setCrateGUITime(int crateGUITime) {
		this.crateGUITime = crateGUITime;
	}

}
