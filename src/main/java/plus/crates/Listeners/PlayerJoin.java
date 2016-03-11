package plus.crates.Listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import plus.crates.CratesPlus;

public class PlayerJoin implements Listener {

	@EventHandler(ignoreCancelled = true)
	public void onPlayerJoin(PlayerJoinEvent event) {
		if (CratesPlus.updateAvailable && event.getPlayer().hasPermission("cratesplus.updates")) {
			event.getPlayer().sendMessage(CratesPlus.updateMessage);
		}
		if (CratesPlus.configBackup != null && event.getPlayer().hasPermission("cratesplus.admin")) {
			event.getPlayer().sendMessage(CratesPlus.getPluginPrefix() + ChatColor.GREEN + "Your config has been updated. Your old config was backed up to " + CratesPlus.configBackup);
			CratesPlus.configBackup = null;
		}
	}

}
