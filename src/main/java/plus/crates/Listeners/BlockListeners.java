package plus.crates.Listeners;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
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

		for (Map.Entry<String, Crate> crate : CratesPlus.crates.entrySet()) {
			Key key = crate.getValue().getKey();
			if (key == null)
				continue;
			title = key.getName();

			if (item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().contains(title)) {
				event.getPlayer().sendMessage(CratesPlus.pluginPrefix + MessageHandler.getMessage(CratesPlus.getPlugin(), "Cant Drop", event.getPlayer(), crate.getValue(), null));
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

		for (Map.Entry<String, Crate> crate : CratesPlus.crates.entrySet()) {
			Key key = crate.getValue().getKey();
			if (key == null)
				continue;
			title = key.getName();

			if (itemOff != null && itemOff.hasItemMeta() && itemOff.getItemMeta().hasDisplayName() && itemOff.getItemMeta().getDisplayName() != null && itemOff.getItemMeta().getDisplayName().contains(title)) {
				item = itemOff;
			}

			if (item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().contains(title)) {
				event.getPlayer().sendMessage(CratesPlus.pluginPrefix + MessageHandler.getMessage(CratesPlus.getPlugin(), "Cant Place", event.getPlayer(), crate.getValue(), null));
				event.setCancelled(true);
				return;
			}
		}

		if (item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().contains("Crate!")) {
			final String crateType = item.getItemMeta().getDisplayName().replaceAll(" Crate!", "");
			final Crate crate = CratesPlus.crates.get(ChatColor.stripColor(crateType).toLowerCase());
			Location location = event.getBlock().getLocation();
			crate.addLocation(location.getBlockX() + "-" + location.getBlockY() + "-" + location.getBlockZ(), location);
			CratesPlus.addCrateBlockToConfig(crate, location);
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
			location.setY(location.getBlockY() - 1);
			location.setX(location.getBlockX() + 0.5);
			location.setZ(location.getBlockZ() + 0.5);

			// Do holograms
			if (CratesPlus.holograms == null || CratesPlus.holograms.isEmpty())
				return;

			String line1;
			String line2;
			String line3;
			String line4;
			ArmorStand armorStand;
			switch (CratesPlus.holograms.size()) {
				case 1:
					line1 = (String) CratesPlus.holograms.get(0);
					line1 = MessageHandler.doPlaceholders(line1, null, crate, null);
					armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
					armorStand.setVisible(false);
					armorStand.setGravity(false);
					armorStand.setCustomNameVisible(true);
					armorStand.setCustomName(line1);
					break;
				case 2:
					line1 = (String) CratesPlus.holograms.get(0);
					line1 = MessageHandler.doPlaceholders(line1, null, crate, null);
					line2 = (String) CratesPlus.holograms.get(1);
					line2 = line2.replaceAll("%crate%", crateType);
					line2 = MessageHandler.doPlaceholders(line2, null, crate, null);

					armorStand = (ArmorStand) location.getWorld().spawnEntity(location.add(0, 0.2, 0), EntityType.ARMOR_STAND);
					armorStand.setVisible(false);
					armorStand.setGravity(false);
					armorStand.setCustomNameVisible(true);
					armorStand.setCustomName(line1);

					armorStand = (ArmorStand) location.getWorld().spawnEntity(location.add(0, -0.2, 0), EntityType.ARMOR_STAND);
					armorStand.setVisible(false);
					armorStand.setGravity(false);
					armorStand.setCustomNameVisible(true);
					armorStand.setCustomName(line2);
					break;
				case 3:
					line1 = (String) CratesPlus.holograms.get(0);
					line1 = MessageHandler.doPlaceholders(line1, null, crate, null);
					line2 = (String) CratesPlus.holograms.get(1);
					line2 = MessageHandler.doPlaceholders(line2, null, crate, null);
					line3 = (String) CratesPlus.holograms.get(2);
					line3 = MessageHandler.doPlaceholders(line3, null, crate, null);

					armorStand = (ArmorStand) location.getWorld().spawnEntity(location.add(0, 0.4, 0), EntityType.ARMOR_STAND);
					armorStand.setVisible(false);
					armorStand.setGravity(false);
					armorStand.setCustomNameVisible(true);
					armorStand.setCustomName(line1);

					armorStand = (ArmorStand) location.getWorld().spawnEntity(location.add(0, -0.2, 0), EntityType.ARMOR_STAND);
					armorStand.setVisible(false);
					armorStand.setGravity(false);
					armorStand.setCustomNameVisible(true);
					armorStand.setCustomName(line2);

					armorStand = (ArmorStand) location.getWorld().spawnEntity(location.add(0, -0.2, 0), EntityType.ARMOR_STAND);
					armorStand.setVisible(false);
					armorStand.setGravity(false);
					armorStand.setCustomNameVisible(true);
					armorStand.setCustomName(line3);
					break;
				default:
					line1 = (String) CratesPlus.holograms.get(0);
					line1 = MessageHandler.doPlaceholders(line1, null, crate, null);
					line2 = (String) CratesPlus.holograms.get(1);
					line2 = MessageHandler.doPlaceholders(line2, null, crate, null);
					line3 = (String) CratesPlus.holograms.get(2);
					line3 = MessageHandler.doPlaceholders(line3, null, crate, null);
					line4 = (String) CratesPlus.holograms.get(3);
					line4 = MessageHandler.doPlaceholders(line4, null, crate, null);

					armorStand = (ArmorStand) location.getWorld().spawnEntity(location.add(0, 0.6, 0), EntityType.ARMOR_STAND);
					armorStand.setVisible(false);
					armorStand.setGravity(false);
					armorStand.setCustomNameVisible(true);
					armorStand.setCustomName(line1);

					armorStand = (ArmorStand) location.getWorld().spawnEntity(location.add(0, -0.2, 0), EntityType.ARMOR_STAND);
					armorStand.setVisible(false);
					armorStand.setGravity(false);
					armorStand.setCustomNameVisible(true);
					armorStand.setCustomName(line2);

					armorStand = (ArmorStand) location.getWorld().spawnEntity(location.add(0, -0.2, 0), EntityType.ARMOR_STAND);
					armorStand.setVisible(false);
					armorStand.setGravity(false);
					armorStand.setCustomNameVisible(true);
					armorStand.setCustomName(line3);

					armorStand = (ArmorStand) location.getWorld().spawnEntity(location.add(0, -0.2, 0), EntityType.ARMOR_STAND);
					armorStand.setVisible(false);
					armorStand.setGravity(false);
					armorStand.setCustomNameVisible(true);
					armorStand.setCustomName(line4);
					break;
			}
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event) {
		if (event.getBlock().getMetadata("CrateType") == null || event.getBlock().getMetadata("CrateType").isEmpty())
			return;
		String crateType = event.getBlock().getMetadata("CrateType").get(0).asString();
		Crate crate = CratesPlus.crates.get(crateType.toLowerCase());
		Location location = event.getBlock().getLocation();

		if (event.getPlayer().isSneaking() && (CratesPlus.getPlugin().getConfig().getBoolean("Crate Protection") && !event.getPlayer().hasPermission("cratesplus.admin"))) {
			event.getPlayer().sendMessage(CratesPlus.pluginPrefix + ChatColor.RED + "You do not have permission to remove this crate");
			event.setCancelled(true);
			return;
		} else if (!event.getPlayer().isSneaking()) {
			event.getPlayer().sendMessage(CratesPlus.pluginPrefix + ChatColor.RED + "Sneak to break crates");
			event.setCancelled(true);
			return;
		}
		for (Entity entity : location.getWorld().getEntities()) {
			if (entity.isDead() || entity.getType() != EntityType.ARMOR_STAND) continue;
			if (entity.getLocation().getBlockX() == event.getBlock().getX() && entity.getLocation().getBlockZ() == event.getBlock().getZ()) {
				entity.remove();
			}
		}
		if (crate.getLocations().containsKey(location.getBlockX() + "-" + location.getBlockY() + "-" + location.getBlockZ()))
			crate.removeLocation(location.getBlockX() + "-" + location.getBlockY() + "-" + location.getBlockZ());
	}

}
