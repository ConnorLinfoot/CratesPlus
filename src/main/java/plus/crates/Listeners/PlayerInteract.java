package plus.crates.Listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
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

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class PlayerInteract implements Listener {
    private CratesPlus cratesPlus;
    private HashMap<String, Long> lastOpended = new HashMap<String, Long>();

    public PlayerInteract(CratesPlus cratesPlus) {
        this.cratesPlus = cratesPlus;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getClickedBlock() == null || event.getClickedBlock().getType() == Material.AIR)
            return;
        ItemStack item = cratesPlus.getVersion_util().getItemInPlayersHand(player);
        ItemStack itemOff = cratesPlus.getVersion_util().getItemInPlayersOffHand(player);

        String crateType;
        if (event.getClickedBlock().getMetadata("CrateType").isEmpty()) {
            // Try to use the old method of getting the crate!
            if (event.getClickedBlock().getType() != Material.CHEST)
                return;
            Chest chest = (Chest) event.getClickedBlock().getState();
            String title = chest.getCustomName();
            if (title != null && title.contains(" Crate!"))
                return;
            crateType = ChatColor.stripColor(title.replaceAll(" Crate!", ""));
        } else {
            crateType = event.getClickedBlock().getMetadata("CrateType").get(0).asString();
        }

        if (!cratesPlus.getConfig().isSet("Crates." + crateType)) {
            return;
        }

        Crate crate = cratesPlus.getConfigHandler().getCrates().get(crateType.toLowerCase());

        if (crate == null) {
            return; // Not sure if we should do some warning here? TODO
        }

        if (crate.getPermission() != null && !player.hasPermission(crate.getPermission())) {
            event.setCancelled(true);
            player.sendMessage(cratesPlus.getPluginPrefix() + cratesPlus.getMessageHandler().getMessage("Crate No Permission", player, crate, null));
            return;
        }
        String title = ChatColor.stripColor(crate.getKey().getName());
        String lore = crate.getKey().getLore().toString();
        if (event.getAction().toString().contains("LEFT")) {
            if (event.getPlayer().isSneaking())
                return;
            /** Do preview */
            CratePreviewEvent cratePreviewEvent = new CratePreviewEvent(player, crateType, cratesPlus);
            if (!cratePreviewEvent.isCanceled())
                cratePreviewEvent.doEvent();
        } else {
            /** Opening of Crate **/
            boolean usingOffHand = false;
            if (itemOff != null && itemOff.hasItemMeta() && !itemOff.getType().equals(Material.AIR) && itemOff.getItemMeta().getDisplayName() != null && itemOff.getItemMeta().getDisplayName().equals(title)) {
                item = itemOff;
                usingOffHand = true;
            }

            if (cratesPlus.getCrateHandler().hasOpening(player.getUniqueId())) { /** If already opening crate, show GUI for said crate **/
                cratesPlus.getCrateHandler().getOpening(player.getUniqueId()).doReopen(player, crate, event.getClickedBlock().getLocation());
                event.setCancelled(true);
                return;
            }

            /** Checks if holding valid key **/
            if (item != null && item.hasItemMeta() && !item.getType().equals(Material.AIR) && item.getItemMeta().getDisplayName() != null && ChatColor.stripColor(item.getItemMeta().getDisplayName()).equals(title) && item.getItemMeta().hasLore() && item.getItemMeta().getLore().toString().equals(lore)) {
                event.setCancelled(true);

                if (player.getInventory().firstEmpty() == -1) {
                    player.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.RED + "You can't open a Crate while your inventory is full");
                    return;
                }

                if (crate.getCooldown() > 0 && lastOpended.containsKey(player.getUniqueId().toString()) && lastOpended.get(player.getUniqueId().toString()) + crate.getCooldown() > TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())) {
                    long whenCooldownEnds = lastOpended.get(player.getUniqueId().toString()) + cratesPlus.getConfigHandler().getDefaultCooldown();
                    long remaining = whenCooldownEnds - TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
                    player.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.RED + "You must wait another " + remaining + " seconds before opening another crate");
                    return;
                }

                lastOpended.put(player.getUniqueId().toString(), TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())); // Store time in seconds of when the player opened the crate

                if (item.getAmount() > 1) {
                    item.setAmount(item.getAmount() - 1);
                } else {
                    if (usingOffHand) {
                        cratesPlus.getVersion_util().removeItemInOffHand(player);
                    } else {
                        player.setItemInHand(null);
                    }
                }

                CrateOpenEvent crateOpenEvent = new CrateOpenEvent(player, crateType, event.getClickedBlock().getLocation(), cratesPlus);
                crateOpenEvent.doEvent();
            } else {
                player.sendMessage(cratesPlus.getPluginPrefix() + cratesPlus.getMessageHandler().getMessage("Crate Open Without Key", player, crate, null));
                if (crate.getKnockback() != 0) {
                    player.setVelocity(player.getLocation().getDirection().multiply(-crate.getKnockback()));
                }
                event.setCancelled(true);
            }
        }
    }

}
