package plus.crates.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import plus.crates.Crate;
import plus.crates.CratesPlus;
import plus.crates.Utils.Hologram;

import java.util.HashMap;
import java.util.Map;

public class HologramListeners implements Listener {

	@EventHandler
	public void onChunkLoad(ChunkLoadEvent event) {
		for (Entity entity : event.getChunk().getEntities()) {
			if (entity instanceof Player)
				loadHolograms((Player) entity, entity.getLocation());
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		loadHolograms(event.getPlayer(), event.getPlayer().getLocation(), 40L);
	}

	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		loadHolograms(event.getPlayer(), event.getPlayer().getLocation());
	}

	@EventHandler
	public void onWorldChange(PlayerChangedWorldEvent event) {
		loadHolograms(event.getPlayer(), event.getPlayer().getLocation());
	}

	@EventHandler
	public void onRespawn(PlayerRespawnEvent event) {
		loadHolograms(event.getPlayer(), event.getPlayer().getLocation());
	}

	private void loadHolograms(final Player player, Location location) {
		loadHolograms(player, location, 0L);
	}

	private void loadHolograms(final Player player, Location location, long delay) {
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
		Bukkit.getScheduler().runTaskLater(CratesPlus.getPlugin(), runnable, delay); // Delay this because if it's too quick the packets don't always get sent? May improve this later on!
	}

}
