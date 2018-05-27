package plus.crates.Configs;

import plus.crates.CratesPlus;

public class Version5 extends ConfigVersion {

    public Version5(CratesPlus cratesPlus) {
        super(cratesPlus, 5);
    }

    @Override
    protected void update() {
        for (String name : getConfig().getConfigurationSection("Crates").getKeys(false)) {
            getConfig().set("Crates." + name + ".Key.Item", getConfig().getString("Crate Keys.Item"));
            getConfig().set("Crates." + name + ".Key.Name", getConfig().getString("Crate Keys.Name"));
            getConfig().set("Crates." + name + ".Key.Enchanted", getConfig().getBoolean("Crate Keys.Enchanted"));
        }

        delete("Crate Keys");
    }

}
