package plus.crates.Configs;

import plus.crates.CratesPlus;

public class Version6 extends ConfigVersion {

    public Version6(CratesPlus cratesPlus) {
        super(cratesPlus, 6);
    }

    @Override
    protected void update() {
        directMap("Hologram Text", "Default Hologram Text");
    }

}
