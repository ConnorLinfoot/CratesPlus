package plus.crates.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import plus.crates.CratesPlus;

import java.util.HashMap;

public class InventoryInteract implements Listener {
	private CratesPlus cratesPlus;

	public InventoryInteract(CratesPlus cratesPlus) {
		this.cratesPlus = cratesPlus;
	}

	@EventHandler(ignoreCancelled = true)
	public void onInventoryClick(InventoryClickEvent event) {
		if (event.getInventory() == null || (event.getInventory().getTitle() != null && event.getInventory().getTitle().contains("Edit ")))
			return;
		if (event.getInventory().getTitle() != null && event.getInventory().getTitle().contains("Possible Wins:")) {
			event.setCancelled(true);
		} else if (event.getInventory().getTitle() != null && event.getInventory().getTitle().contains("Claim Crate Keys")) {
			event.setCancelled(true);
			if (event.getCurrentItem() != null) {
				ItemStack itemStack = event.getCurrentItem();
				if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName() && itemStack.getItemMeta().hasLore()) {
					// We assume it's a key
					HashMap<String, Integer> keys = cratesPlus.getCrateHandler().getPendingKey(event.getWhoClicked().getUniqueId());
					Object[] keyNames = keys.keySet().toArray();
					if (event.getSlot() >= keyNames.length)
						return;
					String keyName = (String) keyNames[event.getSlot()];
					if (keyName != null) {
						cratesPlus.getCrateHandler().claimKey(event.getWhoClicked().getUniqueId(), keyName);
						((Player) event.getWhoClicked()).performCommand("crate claim");
					}
				}
			}
		} else if (event.getInventory().getTitle() != null && event.getInventory().getTitle().contains(" Win") && !event.getInventory().getTitle().contains("Edit ")) {
			if (event.getInventory().getType() != null && event.getInventory().getType() == InventoryType.CHEST && event.getSlot() != 22 || (event.getCurrentItem() != null)) {
				event.setCancelled(true);
				event.getWhoClicked().closeInventory();
			}
		}
	}

}
