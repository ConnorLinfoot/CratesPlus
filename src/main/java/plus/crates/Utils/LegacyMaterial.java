package plus.crates.Utils;

import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import plus.crates.CratesPlus;

public enum LegacyMaterial {
    WOOL("LEGACY_WOOL"),
    EMPTY_MAP("MAP"),
    STAINED_GLASS_PANE("LEGACY_STAINED_GLASS_PANE"),
    MONSTER_EGG("LEGACY_MONSTER_EGG"),
    REDSTONE_TORCH_ON("LEGACY_REDSTONE_TORCH_ON");
    private static boolean is113;

    static {
        is113 = JavaPlugin.getPlugin(CratesPlus.class).versionCompare(JavaPlugin.getPlugin(CratesPlus.class).getBukkitVersion(), "1.13") > -1;
    }

    private final String oldName;
    private final String newName;

    LegacyMaterial(String newName) {
        this.oldName = name();
        this.newName = newName;
    }

    public Material getMaterial() {
        if (is113) {
            return Material.valueOf(newName);
        } else {
            return Material.valueOf(oldName);
        }
    }

}
