package plus.crates.Listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import plus.crates.Crate;
import plus.crates.CratesPlus;
import plus.crates.Events.CrateOpenEvent;
import plus.crates.Events.CratePreviewEvent;
import plus.crates.Handlers.MessageHandler;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class PlayerInteract implements Listener {
	HashMap<String, Long> lastOpended = new HashMap<String, Long>();

	@EventHandler(ignoreCancelled = true)
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (event.getClickedBlock() == null || event.getClickedBlock().getType() == Material.AIR)
			return;
		ItemStack item = CratesPlus.version_util.getItemInPlayersHand(player);
		ItemStack itemOff = CratesPlus.version_util.getItemInPlayersOffHand(player);

		String crateType;
		if (event.getClickedBlock().getMetadata("CrateType") == null || event.getClickedBlock().getMetadata("CrateType").isEmpty()) {
			// Try to use the old method of getting the crate!
			if (event.getClickedBlock().getType() != Material.CHEST)
				return;
			Chest chest = (Chest) event.getClickedBlock().getState();
			Inventory chestInventory = chest.getInventory();
			if (chestInventory.getTitle() == null || !chestInventory.getTitle().contains(" Crate!"))
				return;
			crateType = ChatColor.stripColor(chestInventory.getTitle().replaceAll(" Crate!", ""));
		} else {
			crateType = event.getClickedBlock().getMetadata("CrateType").get(0).asString();
		}

		if (!CratesPlus.getPlugin().getConfig().isSet("Crates." + crateType)) {
			return;
		}

		Crate crate = CratesPlus.crates.get(crateType.toLowerCase());

		if (crate.getPermission() != null && !player.hasPermission(crate.getPermission())) {
			event.setCancelled(true);
			player.sendMessage(CratesPlus.pluginPrefix + MessageHandler.getMessage(CratesPlus.getPlugin(), "Crate No Permission", player, crate, null));
			return;
		}
		String title = crate.getKey().getName();
		if (event.getAction().toString().contains("LEFT")) {
			if (event.getPlayer().isSneaking())
				return;
			/** Do preview */
			CratePreviewEvent cratePreviewEvent = new CratePreviewEvent(player, crateType);
			if (!cratePreviewEvent.isCanceled())
				cratePreviewEvent.doEvent();
		} else {
			boolean usingOffHand = false;
			if (itemOff != null && itemOff.hasItemMeta() && itemOff.getItemMeta().getDisplayName() != null && itemOff.getItemMeta().getDisplayName().equals(title)) {
				item = itemOff;
				usingOffHand = true;
			}

			if (item != null && item.hasItemMeta() && item.getItemMeta().getDisplayName() != null && item.getItemMeta().getDisplayName().equals(title)) {
				event.setCancelled(true);

				if (player.getInventory().firstEmpty() == -1) {
					player.sendMessage(CratesPlus.pluginPrefix + ChatColor.RED + "You can't open a Crate while your inventory is full");
					return;
				}

				lastOpended.put(player.getUniqueId().toString(), TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())); // Store time in seconds of when the player opended the crate

				if (item.getAmount() > 1) {
					item.setAmount(item.getAmount() - 1);
				} else {
					if (usingOffHand) {
						CratesPlus.version_util.removeItemInOffHand(player);
					} else {
						player.setItemInHand(null);
					}
				}

				CrateOpenEvent crateOpenEvent = new CrateOpenEvent(player, crateType);
				if (!crateOpenEvent.isCanceled()) {
					Sound sound;
					try {
						sound = Sound.valueOf("CHEST_OPEN");
					} catch (Exception e) {
						try {
							sound = Sound.valueOf("BLOCK_CHEST_OPEN");
						} catch (Exception ee) {
							return; // This should never happen!
						}
					}
					player.getLocation().getWorld().playSound(player.getLocation(), sound, 10, 1);
					crateOpenEvent.doEvent();
				}
			} else {
				player.sendMessage(CratesPlus.pluginPrefix + MessageHandler.getMessage(CratesPlus.getPlugin(), "Crate Open Without Key", player, crate, null));
				if (crate.getKnockback() != 0) {
					player.setVelocity(player.getLocation().getDirection().multiply(-crate.getKnockback()));
				}
				event.setCancelled(true);
			}
		}
	}

}
