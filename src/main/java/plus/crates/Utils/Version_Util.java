package plus.crates.Utils;

import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.micrlink.individualholograms.IndividualHolograms;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import plus.crates.Crate;
import plus.crates.CratesPlus;

import java.util.ArrayList;
import java.util.HashMap;

public class Version_Util {
	private HashMap<String, com.gmail.filoghost.holographicdisplays.api.Hologram> holograms = new HashMap<>();
	private CratesPlus cratesPlus;

	public Version_Util(CratesPlus cratesPlus) {
		this.cratesPlus = cratesPlus;
	}

	public ItemStack getItemInPlayersHand(Player player) {
		return player.getItemInHand();
	}

	public ItemStack getItemInPlayersOffHand(Player player) {
		return null;
	}

	public void removeItemInOffHand(Player player) {

	}

	public void createHologram(Location location, ArrayList<String> lines, Crate crate) {
		if (cratesPlus.useIndividualHolograms()) {
			IndividualHolograms.get().getHologramManager().createNewHologram("" + location.getWorld().getName() + "|" + location.getBlockX() + "|" + location.getBlockY() + "|" + location.getBlockZ(), location.clone().add(0, -1, 0), lines);
		} else if (cratesPlus.useHolographicDisplays()) {
			com.gmail.filoghost.holographicdisplays.api.Hologram hologram = HologramsAPI.createHologram(cratesPlus, location.clone().add(0, 1.25, 0));
			for (String line : lines) {
				hologram.appendTextLine(line);
			}
			holograms.put("" + location.getWorld().getName() + "|" + location.getBlockX() + "|" + location.getBlockY() + "|" + location.getBlockZ(), hologram);
		} else {
			if (cratesPlus.getBukkitVersion().equals("1.7") || cratesPlus.getBukkitVersion().startsWith("1.7.")) {
				// Warning that 1.7 needs holographic displays for holograms
			} else {
				Hologram hologram = new Hologram(location, lines);
				crate.addHologram(location.getBlock().getLocation(), hologram);
				hologram.displayAll();
			}
		}
	}

	public void removeHologram(Location location) {
		if (cratesPlus.useIndividualHolograms()) {
			IndividualHolograms.get().getHologramManager().removeHologram("" + location.getWorld().getName() + "|" + location.getBlockX() + "|" + location.getBlockY() + "|" + location.getBlockZ());
		} else if (cratesPlus.useHolographicDisplays()) {
			if (holograms.containsKey("" + location.getWorld().getName() + "|" + location.getBlockX() + "|" + location.getBlockY() + "|" + location.getBlockZ())) {
				holograms.get("" + location.getWorld().getName() + "|" + location.getBlockX() + "|" + location.getBlockY() + "|" + location.getBlockZ()).delete();
				holograms.remove("" + location.getWorld().getName() + "|" + location.getBlockX() + "|" + location.getBlockY() + "|" + location.getBlockZ());
			}
		}
	}

}
