package plus.crates.Events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import plus.crates.Crates.Crate;
import plus.crates.Crates.VirtualCrate;
import plus.crates.CratesPlus;

public class CrateOpenEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private CratesPlus cratesPlus;
    private Player player;
    private Crate crate;
    private Location blockLocation;

    public CrateOpenEvent(Player player, Crate crate, Location blockLocation, CratesPlus cratesPlus) {
        this.cratesPlus = cratesPlus;
        this.player = player;
        this.blockLocation = blockLocation;
        this.crate = crate;
    }

    public void doEvent() {
        if (getCrate() instanceof VirtualCrate) {
            ((VirtualCrate) getCrate()).openGUI(getPlayer());
        } else {
            CratesPlus.getOpenHandler().getOpener(getCrate()).startOpening(getPlayer(), getCrate(), getBlockLocation());
        }
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