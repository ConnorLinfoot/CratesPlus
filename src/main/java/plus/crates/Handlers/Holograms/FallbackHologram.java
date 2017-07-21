package plus.crates.Handlers.Holograms;

import org.bukkit.Location;
import plus.crates.Crates.Crate;

import java.util.ArrayList;

public class FallbackHologram implements Hologram {

    public void create(Location location, Crate crate, ArrayList<String> lines) {
        crate.getCratesPlus().getLogger().warning("Hologram #create was called but no Hologram plugin is loaded!");
    }

    public void remove(Location location, Crate crate) {
        crate.getCratesPlus().getLogger().warning("Hologram #remove was called but no Hologram plugin is loaded!");
    }

}
