package plus.crates.Configs;

import plus.crates.CratesPlus;

public class Version7 extends ConfigVersion {

    public Version7(CratesPlus cratesPlus) {
        super(cratesPlus, 7);
    }

    @Override
    protected void update() {
        if (getConfig().isSet("Crates")) {
            for (String crate : getConfig().getConfigurationSection("Crates").getKeys(false)) {
                if (!getConfig().isSet("Crates." + crate + ".Type")) {
                    getConfig().set("Crates." + crate + ".Type", "KeyCrate");
                }
            }
        }

        delete("More Info Hologram");
        delete("Enable GUI Beta Animation");
    }

}
