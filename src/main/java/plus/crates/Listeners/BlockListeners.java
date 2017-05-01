package plus.crates.Listeners;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import plus.crates.Crates.Crate;
import plus.crates.Crates.Key;
import plus.crates.Crates.KeyCrate;
import plus.crates.Crates.SupplyCrate;
import plus.crates.CratesPlus;

import java.util.List;
import java.util.Map;

public class BlockListeners implements Listener {
	private CratesPlus cratesPlus;

	public BlockListeners(CratesPlus cratesPlus) {
		this.cratesPlus = cratesPlus;
	}

	@EventHandler(ignoreCancelled = true)
	public void onItemDrop(PlayerDropItemEvent event) {
		if (!cratesPlus.getConfigHandler().isDisableKeySwapping())
			return;
		String title;
		ItemStack item = event.getItemDrop().getItemStack();

		for (Map.Entry<String, Crate> crate : cratesPlus.getConfigHandler().getCrates().entrySet()) {
			if (!(crate.getValue() instanceof KeyCrate)) {
				continue;
			}
			KeyCrate keyCrate = (KeyCrate) crate.getValue();
			Key key = keyCrate.getKey();
			if (key == null)
				continue;
			title = key.getName();

			if (item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().contains(title)) {
				event.getPlayer().sendMessage(cratesPlus.getPluginPrefix() + cratesPlus.getMessageHandler().getMessage("Cant Drop", event.getPlayer(), crate.getValue(), null));
				event.setCancelled(true);
				return;
			}
		}

	}

	// meh idc where I put my listeners ;)
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		if (!cratesPlus.getConfigHandler().isDisableKeySwapping())
			return;
		String title;
		List<ItemStack> items = event.getDrops();
		for (ItemStack item : items) {
			for (Map.Entry<String, Crate> crate : cratesPlus.getConfigHandler().getCrates().entrySet()) {
				if (!(crate.getValue() instanceof KeyCrate)) {
					continue;
				}
				KeyCrate keyCrate = (KeyCrate) crate.getValue();
				Key key = keyCrate.getKey();
				if (key == null)
					continue;
				title = key.getName();

				if (item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().contains(title)) {
					event.getDrops().remove(item);
					cratesPlus.getCrateHandler().giveCrateKey(event.getEntity(), crate.getValue().getName(), item.getAmount(), false, true);
					return;
				}
			}
		}
	}

	@EventHandler
	public void onInventoryMove(InventoryMoveItemEvent event) {
		if (!cratesPlus.getConfigHandler().isDisableKeySwapping())
			return;
		String title;
		ItemStack item = event.getItem();

		for (Map.Entry<String, Crate> crate : cratesPlus.getConfigHandler().getCrates().entrySet()) {
			if (!(crate.getValue() instanceof KeyCrate)) {
				continue;
			}
			KeyCrate keyCrate = (KeyCrate) crate.getValue();
			Key key = keyCrate.getKey();
			if (key == null)
				continue;
			title = key.getName();

			if (item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().contains(title)) {
				// Send message?
				event.setCancelled(true);
				return;
			}
		}
	}

	@EventHandler
	public void onInventoryPickup(InventoryPickupItemEvent event) {
		if (!cratesPlus.getConfigHandler().isDisableKeySwapping())
			return;
		if (event.getItem().getItemStack() != null) {
			String title;
			ItemStack item = event.getItem().getItemStack();
			for (Map.Entry<String, Crate> crate : cratesPlus.getConfigHandler().getCrates().entrySet()) {
				if (!(crate.getValue() instanceof KeyCrate)) {
					continue;
				}
				KeyCrate keyCrate = (KeyCrate) crate.getValue();
				Key key = keyCrate.getKey();
				if (key == null)
					continue;
				title = key.getName();

				if (item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().contains(title)) {
					// Send message?
					event.setCancelled(true);
					return;
				}
			}
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (!cratesPlus.getConfigHandler().isDisableKeySwapping())
			return;
		if (!event.getInventory().getType().toString().contains("PLAYER") && event.getCurrentItem() != null) {
			String title;
			ItemStack item = event.getCurrentItem();
			for (Map.Entry<String, Crate> crate : cratesPlus.getConfigHandler().getCrates().entrySet()) {
				if (!(crate.getValue() instanceof KeyCrate)) {
					continue;
				}
				KeyCrate keyCrate = (KeyCrate) crate.getValue();
				Key key = keyCrate.getKey();
				if (key == null)
					continue;
				title = key.getName();

				if (item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().contains(title)) {
					// Send message?
					event.setCancelled(true);
					return;
				}
			}
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onBlockPlace(BlockPlaceEvent event) {
		String title;
		Player player = event.getPlayer();
		ItemStack item = cratesPlus.getVersion_util().getItemInPlayersHand(player);
		ItemStack itemOff = cratesPlus.getVersion_util().getItemInPlayersOffHand(player);

		for (Map.Entry<String, Crate> crate : cratesPlus.getConfigHandler().getCrates().entrySet()) {
			if (!(crate.getValue() instanceof KeyCrate)) {
				continue;
			}
			KeyCrate keyCrate = (KeyCrate) crate.getValue();
			Key key = keyCrate.getKey();
			if (key == null)
				continue;
			title = key.getName();

			if (itemOff != null && itemOff.hasItemMeta() && itemOff.getItemMeta().hasDisplayName() && itemOff.getItemMeta().getDisplayName() != null && itemOff.getItemMeta().getDisplayName().contains(title)) {
				item = itemOff;
			}

			if (item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().contains(title)) {
				event.getPlayer().sendMessage(cratesPlus.getPluginPrefix() + cratesPlus.getMessageHandler().getMessage("Cant Place", event.getPlayer(), crate.getValue(), null));
				event.setCancelled(true);
				return;
			}
		}

		if (item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().contains("Crate")) {
			final String crateType = item.getItemMeta().getDisplayName().replaceAll(" Crate", "");
			final Crate crate = cratesPlus.getConfigHandler().getCrates().get(ChatColor.stripColor(crateType).toLowerCase());

			if (crate instanceof SupplyCrate) {
				// Handle supply crate
				SupplyCrate supplyCrate = (SupplyCrate) crate;
				if (!event.isCancelled()) {
					supplyCrate.handleWin(player, event.getBlockPlaced());
				}
			} else if (crate instanceof KeyCrate) {
				KeyCrate keyCrate = (KeyCrate) crate;
				Location location = event.getBlock().getLocation();
				keyCrate.addLocation(location.getBlockX() + "-" + location.getBlockY() + "-" + location.getBlockZ(), location);
				keyCrate.addToConfig(location);
				// BlockMeta to be used for some stuff in the future!
				event.getBlock().setMetadata("CrateType", new MetadataValue() {
					@Override
					public Object value() {
						return crate.getName(false);
					}

					@Override
					public int asInt() {
						return 0;
					}

					@Override
					public float asFloat() {
						return 0;
					}

					@Override
					public double asDouble() {
						return 0;
					}

					@Override
					public long asLong() {
						return 0;
					}

					@Override
					public short asShort() {
						return 0;
					}

					@Override
					public byte asByte() {
						return 0;
					}

					@Override
					public boolean asBoolean() {
						return false;
					}

					@Override
					public String asString() {
						return value().toString();
					}

					@Override
					public Plugin getOwningPlugin() {
						return cratesPlus;
					}

					@Override
					public void invalidate() {

					}
				});

				Location location1 = location.getBlock().getLocation().add(0.5, 0.5, 0.5);
				keyCrate.loadHolograms(location1);
			}
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event) {
		if (event.getBlock().getMetadata("CrateType") == null || event.getBlock().getMetadata("CrateType").isEmpty()) {
			return;
		}
		String crateType = event.getBlock().getMetadata("CrateType").get(0).asString();
		Crate crate = cratesPlus.getConfigHandler().getCrates().get(crateType.toLowerCase());
		if (crate == null) // TODO Better handling of crates removed from the config
			return;
		if (!(crate instanceof KeyCrate)) {
			return;
		}
		KeyCrate keyCrate = (KeyCrate) crate;
		Location location = event.getBlock().getLocation();

		if (event.getPlayer().isSneaking() && (cratesPlus.getConfig().getBoolean("Crate Protection") && !event.getPlayer().hasPermission("cratesplus.admin"))) {
			event.getPlayer().sendMessage(cratesPlus.getPluginPrefix() + ChatColor.RED + "You do not have permission to remove this crate");
			event.setCancelled(true);
			return;
		} else if (!event.getPlayer().isSneaking()) {
			event.getPlayer().sendMessage(cratesPlus.getPluginPrefix() + ChatColor.RED + "Sneak to break crates");
			event.setCancelled(true);
			return;
		}
		location.getBlock().removeMetadata("CrateType", cratesPlus);
		keyCrate.removeFromConfig(location);
		keyCrate.removeHolograms(location.getBlock().getLocation());
	}

}
