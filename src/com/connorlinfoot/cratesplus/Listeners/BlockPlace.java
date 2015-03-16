package com.connorlinfoot.cratesplus.Listeners;

import com.connorlinfoot.cratesplus.CratesPlus;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class BlockPlace implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();
        if (item.hasItemMeta() && item.getItemMeta().getDisplayName() != null && item.getItemMeta().getDisplayName().contains("Crate Key!")) {
            event.getPlayer().sendMessage(CratesPlus.pluginPrefix + ChatColor.RED + "You cant place crate keys!");
            event.setCancelled(true);
            return;
        }

        if (item.hasItemMeta() && item.getItemMeta().getDisplayName() != null && item.getItemMeta().getDisplayName().contains("Crate!")) {
            Location location = event.getBlock().getLocation();
            location.setY(location.getBlockY() - 1);
            Entity entity = location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
            ArmorStand armorStand = (ArmorStand) entity;
            armorStand.setVisible(false);
            armorStand.setGravity(false);
            armorStand.setCustomNameVisible(true);
            armorStand.setCustomName(item.getItemMeta().getDisplayName().replace(" Crate!", ""));
        }
    }

}
