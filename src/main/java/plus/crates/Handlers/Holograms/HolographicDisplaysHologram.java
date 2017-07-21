package plus.crates.Handlers.Holograms;

import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import org.bukkit.Location;
import plus.crates.Crates.Crate;

import java.util.ArrayList;
import java.util.HashMap;

public class HolographicDisplaysHologram implements Hologram {
    private HashMap<String, com.gmail.filoghost.holographicdisplays.api.Hologram> holograms = new HashMap<>();

    public void create(Location location, Crate crate, ArrayList<String> lines) {
        com.gmail.filoghost.holographicdisplays.api.Hologram hologram = HologramsAPI.createHologram(crate.getCratesPlus(), location.clone().add(0, 1.25, 0));
        for (String line : lines) {
            hologram.appendTextLine(line);
        }
        holograms.put("" + location.getWorld().getName() + "|" + location.getBlockX() + "|" + location.getBlockY() + "|" + location.getBlockZ(), hologram);
    }

    public void remove(Location location, Crate crate) {
        if (holograms.containsKey("" + location.getWorld().getName() + "|" + location.getBlockX() + "|" + location.getBlockY() + "|" + location.getBlockZ())) {
            holograms.get("" + location.getWorld().getName() + "|" + location.getBlockX() + "|" + location.getBlockY() + "|" + location.getBlockZ()).delete();
            holograms.remove("" + location.getWorld().getName() + "|" + location.getBlockX() + "|" + location.getBlockY() + "|" + location.getBlockZ());
        }
    }

}
