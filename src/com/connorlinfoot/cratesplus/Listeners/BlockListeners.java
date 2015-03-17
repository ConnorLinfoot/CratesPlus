package com.connorlinfoot.cratesplus.Listeners;

import com.connorlinfoot.cratesplus.CratesPlus;
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

public class BlockListeners implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        final ItemStack item = event.getItemInHand();
        if (item.hasItemMeta() && item.getItemMeta().getDisplayName() != null && item.getItemMeta().getDisplayName().contains("Crate Key!")) {
            event.getPlayer().sendMessage(CratesPlus.pluginPrefix + ChatColor.RED + "You cant place crate keys!");
            event.setCancelled(true);
            return;
        }

        if (item.hasItemMeta() && item.getItemMeta().getDisplayName() != null && item.getItemMeta().getDisplayName().contains("Crate!")) {
            final Location location = event.getBlock().getLocation();
            location.setY(location.getBlockY() - 1);
            location.setX(location.getBlockX() + 0.5);
            location.setZ(location.getBlockZ() + 0.5);
            Entity entity = location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
            ArmorStand armorStand = (ArmorStand) entity;
            armorStand.setVisible(false);
            armorStand.setGravity(false);
            armorStand.setCustomNameVisible(true);
            armorStand.setCustomName(item.getItemMeta().getDisplayName().replace(" Crate!", ""));
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().getState() instanceof Chest) {
            Chest chest = (Chest) event.getBlock().getState();
            if (chest.getInventory().getTitle() != null && chest.getInventory().getTitle().contains("Crate!")) {
                Location location = chest.getLocation();
                for (Entity entity : location.getWorld().getEntities()) {
                    if (entity.isDead()) continue;
                    if (entity.getLocation().getBlockX() == chest.getX() && entity.getLocation().getBlockZ() == chest.getZ()) {
                        entity.remove();
                    }
                }
            }
        }
    }

}
