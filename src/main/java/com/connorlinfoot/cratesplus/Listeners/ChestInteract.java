package com.connorlinfoot.cratesplus.Listeners;

import com.connorlinfoot.cratesplus.Crate;
import com.connorlinfoot.cratesplus.CratesPlus;
import com.connorlinfoot.cratesplus.Events.CrateOpenEvent;
import com.connorlinfoot.cratesplus.Events.CratePreviewEvent;
import com.connorlinfoot.cratesplus.Handlers.MessageHandler;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ChestInteract implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onChestInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        if (event.getClickedBlock() == null || event.getClickedBlock().getType() != Material.CHEST)
            return;
        Chest chest = (Chest) event.getClickedBlock().getState();
        Inventory chestInventory = chest.getInventory();
        ItemStack item = player.getItemInHand();
        if (chestInventory.getTitle() != null && chestInventory.getTitle().contains(" Crate!")) {
            String crateType = ChatColor.stripColor(chestInventory.getTitle().replaceAll(" Crate!", ""));
            if (!CratesPlus.getPlugin().getConfig().isSet("Crates." + crateType)) {
                return;
            }
            Crate crate = new Crate(crateType);
            String title = CratesPlus.getPlugin().getConfig().getString("Crate Keys.Name").replaceAll("%type%", CratesPlus.crates.get(crateType).getColor() + crateType);
            if (item.hasItemMeta() && item.getItemMeta().getDisplayName() != null && item.getItemMeta().getDisplayName().contains(title)) {
                event.setCancelled(true);
                if (item.getAmount() > 1) {
                    item.setAmount(item.getAmount() - 1);
                } else {
                    player.getInventory().remove(item);
                }
                CrateOpenEvent crateOpenEvent = new CrateOpenEvent(player, crateType);
                if (!crateOpenEvent.isCanceled())
                    crateOpenEvent.doEvent();
            } else {
                if (CratesPlus.getPlugin().getConfig().getBoolean("Crate Previews")) {
                    CratePreviewEvent cratePreviewEvent = new CratePreviewEvent(player, crateType);
                    if (!cratePreviewEvent.isCanceled())
                        cratePreviewEvent.doEvent();
                } else {
                    player.sendMessage(CratesPlus.pluginPrefix + MessageHandler.getMessage(CratesPlus.getPlugin(), "Crate Open Without Key", player, crateType));
                    if (crate.getKnockback() != 0) {
                        player.setVelocity(player.getLocation().getDirection().multiply(-crate.getKnockback()));
                    }
                }
                event.setCancelled(true);
            }
        }
    }

}
