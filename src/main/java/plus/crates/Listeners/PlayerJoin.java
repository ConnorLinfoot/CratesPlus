package plus.crates.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import plus.crates.Crate;
import plus.crates.CratesPlus;
import plus.crates.Utils.Hologram;

import java.util.HashMap;
import java.util.Map;

public class PlayerJoin implements Listener {

	@EventHandler(ignoreCancelled = true)
	public void onPlayerJoin(final PlayerJoinEvent event) {
		final Player player = event.getPlayer();
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				for (Map.Entry<String, Crate> crate : CratesPlus.getCrates().entrySet()) {
					HashMap<Location, Hologram> holograms = crate.getValue().getHolograms();
					if (!holograms.isEmpty()) {
						for (Map.Entry<Location, Hologram> hologram : holograms.entrySet())
							hologram.getValue().display(player);
					}
				}
			}
		};
		Bukkit.getScheduler().runTaskLater(CratesPlus.getPlugin(), runnable, 20L); // Delay this because if it's too quick the packets don't always get sent? May improve this later on!

		if (CratesPlus.updateAvailable && event.getPlayer().hasPermission("cratesplus.updates")) {
			event.getPlayer().sendMessage(CratesPlus.updateMessage);
		}
		if (CratesPlus.configBackup != null && event.getPlayer().hasPermission("cratesplus.admin")) {
			event.getPlayer().sendMessage(CratesPlus.getPluginPrefix() + ChatColor.GREEN + "Your config has been updated. Your old config was backed up to " + CratesPlus.configBackup);
			CratesPlus.configBackup = null;
		}
	}

}
