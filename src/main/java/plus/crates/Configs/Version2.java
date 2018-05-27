package plus.crates.Configs;

import plus.crates.CratesPlus;

public class Version2 extends ConfigVersion {

    public Version2(CratesPlus cratesPlus) {
        super(cratesPlus, 2);
    }

    public boolean shouldUpdate(boolean actuallyUpdate) {
        if (!getConfig().isSet("Crate Knockback")) { // Custom check as we didn't have "Config Version" for v1
            return false;
        }

        if (actuallyUpdate) {
            runUpdate();
        }
        return true;
    }

    @Override
    protected void update() {
        // Crate Items
        directMap("Crate Items.Common", "Crates.Common.Items");
        directMap("Crate Items.Rare", "Crates.Rare.Items");
        directMap("Crate Items.Ultra", "Crates.Ultra.Items");

        // Crate Knockback
        directMap("Crate Knockback.Common", "Crates.Common.Knockback");
        directMap("Crate Knockback.Rare", "Crates.Rare.Knockback");
        directMap("Crate Knockback.Ultra", "Crates.Ultra.Knockback");

        // Crate Broadcast
        directMap("Broadcast On Crate Open.Common", "Crates.Common.Broadcast");
        directMap("Broadcast On Crate Open.Rare", "Crates.Rare.Broadcast");
        directMap("Broadcast On Crate Open.Ultra", "Crates.Ultra.Broadcast");

        // Crate Fireworks
        directMap("Firework On Crate Open.Common", "Crates.Common.Firework");
        directMap("Firework On Crate Open.Rare", "Crates.Rare.Firework");
        directMap("Firework On Crate Open.Ultra", "Crates.Ultra.Firework");
    }

}
