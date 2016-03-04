package plus.crates.Events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import plus.crates.Crate;
import plus.crates.CratesPlus;
import plus.crates.Winning;

import java.util.List;

public class CratePreviewEvent extends Event {
	private Player player;
	private String crateName;
	private Crate crate;
	private boolean canceled = false;

	public CratePreviewEvent(Player player, String crateName) {
		this.player = player;
		this.crateName = crateName;
		this.crate = CratesPlus.crates.get(crateName.toLowerCase());
	}

	public void doEvent() {
		if (!crate.isPreview())
			return; // Preview is disabled
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
		Inventory inventory = Bukkit.createInventory(null, size, crate.getName(true) + " Possible Wins:");
		for (Winning winning : items) {
			ItemStack itemStack = winning.getPreviewItemStack();
			if (itemStack == null)
				continue;
			inventory.addItem(itemStack);
		}
		player.openInventory(inventory);
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

}