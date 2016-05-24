package plus.crates.Opener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import plus.crates.Crate;
import plus.crates.CratesPlus;
import plus.crates.Winning;

import java.util.ArrayList;

public abstract class Opener {
	protected Plugin plugin;
	protected String name;
	protected Player player;
	protected Crate crate;
	protected Location blockLocation;
	protected boolean async = false;

	public Opener(Plugin plugin, String name) {
		this(plugin, name, false);
	}

	public Opener(Plugin plugin, String name, boolean async) {
		this.plugin = plugin;
		this.name = name.replaceAll(" ", "_").replaceAll("-", "_");
		this.async = async;
	}

	public void startOpening(Player player, Crate crate, Location blockLocation) {
		this.player = player;
		this.crate = crate;
		this.blockLocation = blockLocation;
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				doTask();
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

	public String getName() {
		return name;
	}

	public Player getPlayer() {
		return player;
	}

	public Crate getCrate() {
		return crate;
	}

	public Location getBlockLocation() {
		return blockLocation;
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
	public Winning getWinning() {
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

	protected abstract void doTask();

}
