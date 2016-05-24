package plus.crates.Utils;

import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import plus.crates.Crate;
import plus.crates.CratesPlus;

import java.util.ArrayList;

public class Version_Util {
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
		if (cratesPlus.useHolographicDisplays()) {
			com.gmail.filoghost.holographicdisplays.api.Hologram hologram = HologramsAPI.createHologram(cratesPlus, location);
			for (String line : lines) {
				hologram.appendTextLine(line);
			}
		} else {
			if (cratesPlus.getMc_version() == CratesPlus.MC_VERSION.MC_1_7) {
				// Warning that 1.7 needs holographic displays for holograms
			} else {
				Hologram hologram = new Hologram(location, lines);
				crate.addHologram(location.getBlock().getLocation(), hologram);
				hologram.displayAll();
			}
		}
	}

}
