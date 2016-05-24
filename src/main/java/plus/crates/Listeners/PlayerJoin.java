package plus.crates.Listeners;

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
		if (cratesPlus.isUpdateAvailable() && event.getPlayer().hasPermission("cratesplus.updates")) {
			event.getPlayer().sendMessage(cratesPlus.getUpdateMessage());
		}
		if (cratesPlus.getConfigBackup() != null && event.getPlayer().hasPermission("cratesplus.admin")) {
			event.getPlayer().sendMessage(cratesPlus.getPluginPrefix() + ChatColor.GREEN + "Your config has been updated. Your old config was backed up to " + cratesPlus.getConfigBackup());
			cratesPlus.setConfigBackup(null);
		}
		//if(CrateHandler.hasPendingKeys(event.getPlayer().getUniqueId())) {
		//	// TODO Send Message
		//}
	}

}
