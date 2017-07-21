package plus.crates.Handlers.Holograms;

import com.micrlink.individualholograms.IndividualHolograms;
import org.bukkit.Location;
import plus.crates.Crates.Crate;

import java.util.ArrayList;

public class IndividualHologramsHologram implements Hologram {

    public void create(Location location, Crate crate, ArrayList<String> lines) {
        IndividualHolograms.get().getHologramManager().createNewHologram("" + location.getWorld().getName() + "|" + location.getBlockX() + "|" + location.getBlockY() + "|" + location.getBlockZ(), location.clone().add(0, -1, 0), lines);
    }

    public void remove(Location location, Crate crate) {
        IndividualHolograms.get().getHologramManager().removeHologram("" + location.getWorld().getName() + "|" + location.getBlockX() + "|" + location.getBlockY() + "|" + location.getBlockZ());
    }

}
