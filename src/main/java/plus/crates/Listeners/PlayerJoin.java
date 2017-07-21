package plus.crates.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import plus.crates.CratesPlus;

public class PlayerJoin implements Listener {
    private CratesPlus cratesPlus;

    public PlayerJoin(CratesPlus cratesPlus) {
        this.cratesPlus = cratesPlus;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskLater(cratesPlus, new Runnable() {
            @Override
            public void run() {
                if (cratesPlus.isUpdateAvailable() && event.getPlayer().hasPermission("cratesplus.updates")) {
                    event.getPlayer().sendMessage(cratesPlus.getUpdateMessage());
                }
                if (cratesPlus.getConfigBackup() != null && event.getPlayer().hasPermission("cratesplus.admin")) {
                    event.getPlayer().sendMessage(cratesPlus.getPluginPrefix() + ChatColor.GREEN + "Your config has been updated. Your old config was backed up to " + cratesPlus.getConfigBackup());
                    cratesPlus.setConfigBackup(null);
                }
                if (cratesPlus.getCrateHandler().hasPendingKeys(event.getPlayer().getUniqueId())) {
                    if (cratesPlus.getConfigHandler().getClaimMessageDelay() > -1) {
                        Bukkit.getScheduler().runTaskLater(cratesPlus, new Runnable() {
                            @Override
                            public void run() {
                                event.getPlayer().sendMessage(cratesPlus.getMessageHandler().getMessage("Claim Join", event.getPlayer(), null, null));
                            }
                        }, cratesPlus.getConfigHandler().getClaimMessageDelay() > 0 ? (20 * cratesPlus.getConfigHandler().getClaimMessageDelay()) : 0);
                    }
                }
            }
        }, 1L);
    }

}
