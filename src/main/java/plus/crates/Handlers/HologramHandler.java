package plus.crates.Handlers;

import org.bukkit.Bukkit;
import plus.crates.Handlers.Holograms.FallbackHologram;
import plus.crates.Handlers.Holograms.Hologram;
import plus.crates.Handlers.Holograms.HolographicDisplaysHologram;
import plus.crates.Handlers.Holograms.IndividualHologramsHologram;

public class HologramHandler {
    private HologramPlugin hologramPlugin = HologramPlugin.NONE;

    public enum HologramPlugin {
        NONE,
        HOLOGRAPHIC_DISPLAYS,
        INDIVIDUAL_HOLOGRAMS;
        private Hologram hologram;

        public void init() {
            switch (this) {
                default:
                case NONE:
                    this.hologram = new FallbackHologram();
                    break;
                case HOLOGRAPHIC_DISPLAYS:
                    this.hologram = new HolographicDisplaysHologram();
                    break;
                case INDIVIDUAL_HOLOGRAMS:
                    this.hologram = new IndividualHologramsHologram();
                    break;
            }
        }

        public Hologram getHologram() {
            return hologram;
        }
    }

    public HologramHandler() {
        if (Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays")) {
            this.hologramPlugin = HologramHandler.HologramPlugin.HOLOGRAPHIC_DISPLAYS;
        } else if (Bukkit.getPluginManager().isPluginEnabled("IndividualHolograms")) {
            this.hologramPlugin = HologramHandler.HologramPlugin.INDIVIDUAL_HOLOGRAMS;
        }
        getHologramPlugin().init();
    }

    public HologramPlugin getHologramPlugin() {
        return hologramPlugin;
    }

}
