package plus.crates.Opener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import plus.crates.Crate;
import plus.crates.CratesPlus;
import plus.crates.Winning;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public abstract class Opener {
	protected Plugin plugin;
	protected String name;
	protected boolean async = false;
	protected boolean running = false;

	public Opener(Plugin plugin, String name) {
		this(plugin, name, false);
	}

	public Opener(Plugin plugin, String name, boolean async) {
		this.plugin = plugin;
		this.name = name.replaceAll(" ", "_").replaceAll("-", "_");
		this.async = async;
	}

	public void startOpening(final Player player, final Crate crate, final Location blockLocation) {
		this.running = true;
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

	/**
	 * @return boolean - Whether or not the task should run asynchronously
	 */
	public boolean isAsync() {
		return async;
	}

	/**
	 * @return Winning - A random Winning object that you can then award to a player.
	 */
	public Winning getWinning(Crate crate) {
		Winning winning;
		if (crate.getTotalPercentage() > 0) {
			double maxPercentage = 0.0D;
			for (Winning winning1 : crate.getWinnings()) {
				if (winning1.getPercentage() > maxPercentage)
					maxPercentage = winning1.getPercentage();
			}

			double randDouble = CratesPlus.getOpenHandler().getCratesPlus().getCrateHandler().randDouble(0, maxPercentage);
			ArrayList<Winning> winnings = new ArrayList<>();
			for (Winning winning1 : crate.getWinnings()) {
				if (randDouble < winning1.getPercentage())
					winnings.add(winning1);
			}

			if (winnings.size() > 1) {
				winning = winnings.get(CratesPlus.getOpenHandler().getCratesPlus().getCrateHandler().randInt(0, winnings.size() - 1));
			} else {
				winning = winnings.get(0);
			}
		} else {
			winning = crate.getWinnings().get(CratesPlus.getOpenHandler().getCratesPlus().getCrateHandler().randInt(0, crate.getWinnings().size() - 1));
		}
		return winning;
	}

	public File getOpenerConfigFile() {
		File openersDir = new File(CratesPlus.getOpenHandler().getCratesPlus().getDataFolder(), "openers");
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
				setRunning(false);
			}
		});
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public abstract void doSetup();

	protected abstract void doOpen(Player player, Crate crate, Location blockLocation);

	public abstract void doReopen(Player player, Crate crate, Location blockLocation);

}
