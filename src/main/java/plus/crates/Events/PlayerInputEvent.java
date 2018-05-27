package plus.crates.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import plus.crates.Utils.SignInputHandler;

import java.util.ArrayList;

public class PlayerInputEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Player player;
    public ArrayList<String> lines;

    public PlayerInputEvent(Player player, ArrayList<String> lines) {
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

    public ArrayList<String> getLines() {
        return lines;
    }

}