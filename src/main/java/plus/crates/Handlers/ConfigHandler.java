package plus.crates.Handlers;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigHandler {
	private int cooldown = 5;

	public ConfigHandler(FileConfiguration config) {
		// Load configuration
		if (config.isSet("Cooldown"))
			setCooldown(config.getInt("Cooldown"));
	}

	public int getCooldown() {
		return cooldown;
	}

	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}

}
