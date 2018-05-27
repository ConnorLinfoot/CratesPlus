package plus.crates.Configs;

import plus.crates.CratesPlus;

import java.util.ArrayList;
import java.util.List;

public class Version3 extends ConfigVersion {

    public Version3(CratesPlus cratesPlus) {
        super(cratesPlus, 3);
    }

    @Override
    protected void update() {
        for (String crate : getConfig().getConfigurationSection("Crates").getKeys(false)) {
            List<?> items = getConfig().getList("Crates." + crate + ".Items");
            List<String> newItems = new ArrayList<>();
            for (Object object : items) {
                String i = object.toString();
                if (i.toUpperCase().contains("COMMAND:")) {
                    newItems.add(i);
                } else {
                    String newi = getCratesPlus().getCrateHandler().itemstackToString(getCratesPlus().getCrateHandler().stringToItemstackOld(i));
                    newItems.add(newi);
                }
            }
            getConfig().set("Crates." + crate + ".Items", newItems);
        }

        delete("`");
        delete("Crate Previews");
        delete("Crate Open GUI");
    }

}
