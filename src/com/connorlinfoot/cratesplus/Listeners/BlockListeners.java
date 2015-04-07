package com.connorlinfoot.cratesplus.Listeners;

import com.connorlinfoot.cratesplus.CratesPlus;
import com.connorlinfoot.cratesplus.Handlers.MessageHandler;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Chest;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class BlockListeners implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();
        String title = CratesPlus.getPlugin().getConfig().getString("Crate Keys.Name").replaceAll("%type%", "");
        if (item.hasItemMeta() && item.getItemMeta().getDisplayName() != null && item.getItemMeta().getDisplayName().contains(title)) {
            event.getPlayer().sendMessage(CratesPlus.pluginPrefix + MessageHandler.getMessage(CratesPlus.getPlugin(), "Cant Place", event.getPlayer(), ChatColor.stripColor(item.getItemMeta().getDisplayName().replaceAll(title, ""))));
            event.setCancelled(true);
            return;
        }

        if (item.hasItemMeta() && item.getItemMeta().getDisplayName() != null && item.getItemMeta().getDisplayName().contains("Crate!")) {
            final String crateType = item.getItemMeta().getDisplayName().replaceAll(" Crate!", "");
            // BlockMeta to be used for some stuff in the future!
            event.getBlock().setMetadata("CrateType", new MetadataValue() {
                @Override
                public Object value() {
                    return null;
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
                    return crateType;
                }

                @Override
                public Plugin getOwningPlugin() {
                    return CratesPlus.getPlugin();
                }

                @Override
                public void invalidate() {

                }
            });
            Location location = event.getBlock().getLocation();
            location.setY(location.getBlockY() - 1);
            location.setX(location.getBlockX() + 0.5);
            location.setZ(location.getBlockZ() + 0.5);
            Entity entity = location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
            ArmorStand armorStand = (ArmorStand) entity;
            armorStand.setVisible(false);
            armorStand.setGravity(false);
            armorStand.setCustomNameVisible(true);
            armorStand.setCustomName(crateType);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().getState() instanceof Chest) {
            Chest chest = (Chest) event.getBlock().getState();
            if (chest.getInventory().getTitle() != null && chest.getInventory().getTitle().contains("Crate!")) {
                Location location = chest.getLocation();
                if (CratesPlus.getPlugin().getConfig().getBoolean("Crate Protection") && !event.getPlayer().hasPermission("cratesplus.admin")) {
                    event.getPlayer().sendMessage(ChatColor.RED + "You do not have permission to remove this crate");
                    event.setCancelled(true);
                    return;
                }
                for (Entity entity : location.getWorld().getEntities()) {
                    if (entity.isDead() || entity.getType() != EntityType.ARMOR_STAND) continue;
                    String name = chest.getInventory().getTitle().replace(" Crate!", "");
                    if (name != null && name.equals(entity.getCustomName()) && entity.getLocation().getBlockX() == chest.getX() && entity.getLocation().getBlockZ() == chest.getZ() && entity.getLocation().getBlockY() + 1 == chest.getY()) {
                        entity.remove();
                        break;
                    }
                }
            }
        }
    }

}
