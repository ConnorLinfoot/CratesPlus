package plus.crates.Events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import plus.crates.Crates.KeyCrate;
import plus.crates.Crates.Winning;
import plus.crates.CratesPlus;

import java.util.List;

public class KeyCratePreviewEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private CratesPlus cratesPlus;
    private Player player;
    private KeyCrate crate;
    private boolean canceled = false;

    public KeyCratePreviewEvent(Player player, KeyCrate crate, CratesPlus cratesPlus) {
        this.cratesPlus = cratesPlus;
        this.player = player;
        this.crate = crate;
    }

    public void doEvent() {
        //TODO Add preview disable option back
//		if (!crate.isPreview())
//			return; // Preview is disabled
        List<Winning> items = crate.getWinnings();
        Integer size = 54;
        if (items.size() <= 9) {
            size = 9;
        } else if (items.size() <= 18) {
            size = 18;
        } else if (items.size() <= 27) {
            size = 27;
        } else if (items.size() <= 36) {
            size = 36;
        } else if (items.size() <= 45) {
            size = 45;
        }
        int i = 0;
        Inventory inventory = Bukkit.createInventory(null, size, crate.getName(true) + " " + cratesPlus.getMessagesConfig().getString("Possible Wins Title"));
        for (Winning winning : items) {
            ItemStack itemStack = winning.getPreviewItemStack();
            if (itemStack == null)
                continue;
            if (i > size - 1)
                break;
            inventory.setItem(i, itemStack);
            i++;
        }
        player.openInventory(inventory);
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

    public CratesPlus getCratesPlus() {
        return cratesPlus;
    }

}