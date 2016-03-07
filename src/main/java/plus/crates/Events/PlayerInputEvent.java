package plus.crates.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import plus.crates.Utils.SignInputHandler;

public class PlayerInputEvent extends PlayerEvent {
	public static HandlerList handlerList = new HandlerList();

	public PlayerInputEvent(Player p, String[] lines) {
		super(p);
		//This is were your code goes
		for (String line : lines) {
			System.out.println(line);
		}
		SignInputHandler.ejectNetty(player);
	}

	@Override
	public HandlerList getHandlers() {
		return handlerList;
	}
}