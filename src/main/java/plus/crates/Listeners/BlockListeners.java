package plus.crates.Listeners;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import plus.crates.Crate;
import plus.crates.CratesPlus;
import plus.crates.Handlers.MessageHandler;
import plus.crates.Key;

import java.util.Map;

public class BlockListeners implements Listener {

	@EventHandler(ignoreCancelled = true)
	public void onItemDrop(PlayerDropItemEvent event) {
		if (!CratesPlus.getPlugin().getConfig().getBoolean("Disable Key Dropping"))
			return;
		String title;
		ItemStack item = event.getItemDrop().getItemStack();

		for (Map.Entry<String, Crate> crate : CratesPlus.getConfigHandler().getCrates().entrySet()) {
			Key key = crate.getValue().getKey();
			if (key == null)
				continue;
			title = key.getName();

			if (item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().contains(title)) {
				event.getPlayer().sendMessage(CratesPlus.getPluginPrefix() + MessageHandler.getMessage(CratesPlus.getPlugin(), "Cant Drop", event.getPlayer(), crate.getValue(), null));
				event.setCancelled(true);
				return;
			}
		}

	}

	@EventHandler(ignoreCancelled = true)
	public void onBlockPlace(BlockPlaceEvent event) {
		String title;
		Player player = event.getPlayer();
		ItemStack item = CratesPlus.version_util.getItemInPlayersHand(player);
		ItemStack itemOff = CratesPlus.version_util.getItemInPlayersOffHand(player);

		for (Map.Entry<String, Crate> crate : CratesPlus.getConfigHandler().getCrates().entrySet()) {
			Key key = crate.getValue().getKey();
			if (key == null)
				continue;
			title = key.getName();

			if (itemOff != null && itemOff.hasItemMeta() && itemOff.getItemMeta().hasDisplayName() && itemOff.getItemMeta().getDisplayName() != null && itemOff.getItemMeta().getDisplayName().contains(title)) {
				item = itemOff;
			}

			if (item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().contains(title)) {
				event.getPlayer().sendMessage(CratesPlus.getPluginPrefix() + MessageHandler.getMessage(CratesPlus.getPlugin(), "Cant Place", event.getPlayer(), crate.getValue(), null));
				event.setCancelled(true);
				return;
			}
		}

		if (item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().contains("Crate!")) {
			final String crateType = item.getItemMeta().getDisplayName().replaceAll(" Crate!", "");
			final Crate crate = CratesPlus.getConfigHandler().getCrates().get(ChatColor.stripColor(crateType).toLowerCase());
			Location location = event.getBlock().getLocation();
			crate.addLocation(location.getBlockX() + "-" + location.getBlockY() + "-" + location.getBlockZ(), location);
			crate.addToConfig(location);
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
					return CratesPlus.getPlugin();
				}

				@Override
				public void invalidate() {

				}
			});

			Location location1 = location.getBlock().getLocation().add(0.5, 0.5, 0.5);
			crate.loadHolograms(location.getBlock().getLocation(), location1);
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event) {
		if (event.getBlock().getMetadata("CrateType") == null || event.getBlock().getMetadata("CrateType").isEmpty()) {
			onBlockBreakLegacy(event);
			return;
		}
		String crateType = event.getBlock().getMetadata("CrateType").get(0).asString();
		Crate crate = CratesPlus.getConfigHandler().getCrates().get(crateType.toLowerCase());
		Location location = event.getBlock().getLocation();

		if (event.getPlayer().isSneaking() && (CratesPlus.getPlugin().getConfig().getBoolean("Crate Protection") && !event.getPlayer().hasPermission("cratesplus.admin"))) {
			event.getPlayer().sendMessage(CratesPlus.getPluginPrefix() + ChatColor.RED + "You do not have permission to remove this crate");
			event.setCancelled(true);
			return;
		} else if (!event.getPlayer().isSneaking()) {
			event.getPlayer().sendMessage(CratesPlus.getPluginPrefix() + ChatColor.RED + "Sneak to break crates");
			event.setCancelled(true);
			return;
		}
		location.getBlock().removeMetadata("CrateType", CratesPlus.getPlugin());
		crate.removeFromConfig(location);
		crate.removeHolograms(location.getBlock().getLocation());
	}

	public void onBlockBreakLegacy(BlockBreakEvent event) { // This is to support legacy breaks
		if (event.getBlock().getState() instanceof Chest) {
			Chest chest = (Chest) event.getBlock().getState();
			if (chest.getInventory().getTitle() != null && chest.getInventory().getTitle().contains("Crate!")) {
				Location location = chest.getLocation();

				if (event.getPlayer().isSneaking() && (CratesPlus.getPlugin().getConfig().getBoolean("Crate Protection") && !event.getPlayer().hasPermission("cratesplus.admin"))) {
					event.getPlayer().sendMessage(CratesPlus.getPluginPrefix() + ChatColor.RED + "You do not have permission to remove this crate");
					event.setCancelled(true);
					return;
				} else if (!event.getPlayer().isSneaking()) {
					event.getPlayer().sendMessage(CratesPlus.getPluginPrefix() + ChatColor.RED + "Sneak to break crates");
					event.setCancelled(true);
					return;
				}
				for (Entity entity : location.getWorld().getEntities()) {
					if (entity.isDead() || entity.getType() != EntityType.ARMOR_STAND) continue;
					String name = chest.getInventory().getTitle().replace(" Crate!", "");
					if (name != null && entity.getLocation().getBlockX() == chest.getX() && entity.getLocation().getBlockZ() == chest.getZ()) {
						entity.remove();
					}
				}
			}
		}
	}

}
