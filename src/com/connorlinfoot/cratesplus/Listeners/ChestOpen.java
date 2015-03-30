package com.connorlinfoot.cratesplus.Listeners;

import com.connorlinfoot.cratesplus.CratesPlus;
import com.connorlinfoot.cratesplus.Events.CrateOpenEvent;
import com.connorlinfoot.cratesplus.Events.CratePreviewEvent;
import com.connorlinfoot.cratesplus.Handlers.MessageHandler;
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
        if (event.getInventory().getTitle().contains(" Crate!")) {
            String crateType = "Common";
            // TODO get crates from config and check etc
            String title = CratesPlus.getPlugin().getConfig().getString("Crate Keys.Name").replaceAll("%type%", CratesPlus.crates.get(crateType) + crateType);
            if (item.hasItemMeta() && item.getItemMeta().getDisplayName() != null && item.getItemMeta().getDisplayName().contains(title)) {
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
                    double knock = CratesPlus.getPlugin().getConfig().getDouble("Crate Knockback." + crateType);
                    if (knock != 0) {
                        player.setVelocity(player.getLocation().getDirection().multiply(-knock));
                    }
                }
                event.setCancelled(true);
            }
        }
    }

}
