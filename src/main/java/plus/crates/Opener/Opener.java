package plus.crates.Opener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import plus.crates.Crate;
import plus.crates.CratesPlus;
import plus.crates.Winning;

import java.io.File;
import java.io.IOException;
import java.util.List;

public abstract class Opener {
	protected Plugin plugin;
	protected String name;
	protected boolean async = false;

	public Opener(Plugin plugin, String name) {
		this(plugin, name, false);
	}

	public Opener(Plugin plugin, String name, boolean async) {
		this.plugin = plugin;
		this.name = name.replaceAll(" ", "_").replaceAll("-", "_");
		this.async = async;
	}

	public void startOpening(final Player player, final Crate crate, final Location blockLocation) {
		CratesPlus.getOpenHandler().getCratesPlus().getCrateHandler().addOpening(player.getUniqueId(), this);
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				doOpen(player, crate, blockLocation);
			}
		};
		if (isAsync()) {
			// Start the opening as a async task
			Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
		} else {
			// Run as a non async task
			Bukkit.getScheduler().runTask(plugin, runnable);
		}
	}

	public Plugin getPlugin() {
		return plugin;
	}

	public String getName() {
		return name;
	}

	public boolean isAsync() {
		return async;
	}

	public Winning getWinning(Crate crate) {
		Winning winning;
		if (crate.getTotalPercentage() > 0) {
			List<Winning> winnings = crate.getWinnings();
			// Compute the total weight of all items together
			double totalWeight = 0.0d;
			for (Winning winning1 : winnings) {
				totalWeight += winning1.getPercentage();
			}

			// Now choose a random item
			int randomIndex = -1;
			double random = Math.random() * totalWeight;
			for (int i = 0; i < winnings.size(); ++i) {
				random -= winnings.get(i).getPercentage();
				if (random <= 0.0d) {
					randomIndex = i;
					break;
				}
			}
			winning = winnings.get(randomIndex);
		} else {
			winning = crate.getWinnings().get(CratesPlus.getOpenHandler().getCratesPlus().getCrateHandler().randInt(0, crate.getWinnings().size() - 1));
		}
		return winning;
	}

	public File getOpenerConfigFile() {
		File openersDir = new File(JavaPlugin.getPlugin(CratesPlus.class).getDataFolder(), "openers");
		if (!openersDir.exists())
			if (!openersDir.mkdirs())
				return null;
		File configurationFile = new File(openersDir, getName() + ".yml");
		if (!configurationFile.exists())
			try {
				if (!configurationFile.createNewFile())
					return null;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		return configurationFile;
	}

	public FileConfiguration getOpenerConfig() {
		File file = getOpenerConfigFile();
		if (file == null)
			return null;
		return YamlConfiguration.loadConfiguration(file);
	}

	protected void finish(final Player player) {
		Bukkit.getScheduler().runTask(getPlugin(), new Runnable() {
			@Override
			public void run() {
				CratesPlus.getOpenHandler().getCratesPlus().getCrateHandler().removeOpening(player.getUniqueId());
			}
		});
	}

	public abstract void doSetup();

	public abstract void doOpen(Player player, Crate crate, Location blockLocation);

	public abstract void doReopen(Player player, Crate crate, Location blockLocation);

}
