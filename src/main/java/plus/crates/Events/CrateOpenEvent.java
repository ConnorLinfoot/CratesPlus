package plus.crates.Events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitTask;
import plus.crates.Crate;
import plus.crates.CratesPlus;
import plus.crates.Winning;

public class CrateOpenEvent extends Event {
	private CratesPlus cratesPlus;
	private Player player;
	private String crateName;
	private Crate crate;
	private boolean canceled = false;
	private Inventory winGUI;
	private BukkitTask task;
	private Integer timer = 0;
	private Integer currentItem = 0;
	private Winning winning = null;
	private Location blockLocation;

	public CrateOpenEvent(Player player, String crateName, Location blockLocation, CratesPlus cratesPlus) {
		this.cratesPlus = cratesPlus;
		this.player = player;
		this.crateName = crateName;
		this.blockLocation = blockLocation;
		this.crate = cratesPlus.getConfigHandler().getCrates().get(crateName.toLowerCase());
	}

	public void doEvent() {
		CratesPlus.getOpenHandler().getEnabledOpener().startOpening(player, crate, blockLocation);
	}

	@Override
	public HandlerList getHandlers() {
		return null;
	}

	public boolean isCanceled() {
		return this.canceled;
	}

	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}

	public Player getPlayer() {
		return this.player;
	}

	public void setCrateName(String crateName) {
		this.crateName = crateName;
	}

	public String getCrateName() {
		return this.crateName;
	}

	public Crate getCrate() {
		return this.crate;
	}

	public Location getBlockLocation() {
		return blockLocation;
	}

}