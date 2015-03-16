package com.connorlinfoot.cratesplus.Listeners;

import com.connorlinfoot.cratesplus.CrateHandler;
import com.connorlinfoot.cratesplus.CrateType;
import com.connorlinfoot.cratesplus.CratesPlus;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

public class ChestOpen implements Listener {

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        final Player player = (Player) event.getPlayer();
        ItemStack item = player.getItemInHand();
        if( event.getInventory().getTitle().contains(" Crate!") ) {
            CrateType crateType = CrateType.COMMON;
            if( event.getInventory().getTitle().contains("Rare") ) {
                crateType = CrateType.RARE;
            } else if( event.getInventory().getTitle().contains("Ultra") ) {
                crateType = CrateType.ULTRA;
            }
            if (item.hasItemMeta() && item.getItemMeta().getDisplayName().contains(crateType.getCode(true) + " Crate Key!")) {
                event.setCancelled(true);
                if (item.getAmount() != 1) {
                    player.sendMessage(CratesPlus.pluginPrefix + ChatColor.RED + "Please only hold 1 " + crateType.getCode(true) + ChatColor.RED + " key at a time");
                    return;
                }
                player.getInventory().remove(item);
                if( crateType == CrateType.ULTRA ) {
                    CrateHandler.spawnFirework(player.getLocation());
                    Bukkit.broadcastMessage(CratesPlus.pluginPrefix + ChatColor.DARK_PURPLE + "--------------------------------------------");
                    Bukkit.broadcastMessage(CratesPlus.pluginPrefix + ChatColor.LIGHT_PURPLE + player.getDisplayName() + ChatColor.LIGHT_PURPLE + " opened a Ultra crate!");
                    Bukkit.broadcastMessage(CratesPlus.pluginPrefix + ChatColor.DARK_PURPLE + "--------------------------------------------");
                }

            } else {
                player.sendMessage(CratesPlus.pluginPrefix + ChatColor.RED + "You must be holding a " + crateType.getCode(true) + ChatColor.RED + " key to open this crate");
                event.setCancelled(true);
            }
        }
    }

}
