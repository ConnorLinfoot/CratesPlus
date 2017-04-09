package plus.crates.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import plus.crates.Utils.SignInputHandler;

public class PlayerInputEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	private Player player;
	public String[] lines;

	public PlayerInputEvent(Player player, String[] lines) {
		this.player = player;
		this.lines = lines;
		SignInputHandler.ejectNetty(player);
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public Player getPlayer() {
		return player;
	}

	public String[] getLines() {
		return lines;
	}

}