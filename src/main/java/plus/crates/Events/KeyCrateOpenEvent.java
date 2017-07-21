package plus.crates.Events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import plus.crates.Crates.Crate;
import plus.crates.Crates.KeyCrate;
import plus.crates.CratesPlus;

public class KeyCrateOpenEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private CratesPlus cratesPlus;
    private Player player;
    private KeyCrate crate;
    private Location blockLocation;

    public KeyCrateOpenEvent(Player player, KeyCrate crate, Location blockLocation, CratesPlus cratesPlus) {
        this.cratesPlus = cratesPlus;
        this.player = player;
        this.blockLocation = blockLocation;
        this.crate = crate;
    }

    public void doEvent() {
        // TODO
        CratesPlus.getOpenHandler().getOpener(crate).startOpening(player, crate, blockLocation);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
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