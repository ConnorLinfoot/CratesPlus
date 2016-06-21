package plus.crates.Events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import plus.crates.Crate;
import plus.crates.CratesPlus;

public class CrateOpenEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	private CratesPlus cratesPlus;
	private Player player;
	private Crate crate;
	private boolean canceled = false;
	private Location blockLocation;

	public CrateOpenEvent(Player player, String crateName, Location blockLocation, CratesPlus cratesPlus) {
		this.cratesPlus = cratesPlus;
		this.player = player;
		this.blockLocation = blockLocation;
		this.crate = cratesPlus.getConfigHandler().getCrates().get(crateName.toLowerCase());
	}

	public void doEvent() {
		CratesPlus.getOpenHandler().getOpener(crate).startOpening(player, crate, blockLocation);
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
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

	public Crate getCrate() {
		return this.crate;
	}

	public Location getBlockLocation() {
		return blockLocation;
	}

	public CratesPlus getCratesPlus() {
		return cratesPlus;
	}

}