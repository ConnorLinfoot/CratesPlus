package plus.crates.Handlers;

import plus.crates.Crates.Crate;
import plus.crates.CratesPlus;
import plus.crates.Opener.BasicGUIOpener;
import plus.crates.Opener.NoGUIOpener;
import plus.crates.Opener.Opener;
import plus.crates.Opener.SupplyOpener;

import java.util.HashMap;

/**
 * Public handler for CratesPlus to be able to modify the way crates open.
 */
public class OpenHandler {
    private CratesPlus cratesPlus;
    private HashMap<String, Opener> registered = new HashMap<>();
    private HashMap<String, String> defaults = new HashMap<>();

    public OpenHandler(CratesPlus cratesPlus) {
        this.cratesPlus = cratesPlus;
        registerDefaults();
    }

    private void registerDefaults() {
        registerOpener(new BasicGUIOpener(cratesPlus));
        registerOpener(new NoGUIOpener(cratesPlus));
        registerOpener(new SupplyOpener(cratesPlus));

        defaults.put("keycrate", "BasicGUI");
        defaults.put("supplycrate", "Supply");
        defaults.put("dropcrate", "Supply");
        defaults.put("mysterycrate", "BasicGUI");
//        defaultOpener = cratesPlus.getConfigHandler().getDefaultOpener(); // TODO - Load above from config
    }

    public void registerOpener(Opener opener) {
        if (registered.containsKey(opener.getName())) {
            getCratesPlus().getLogger().warning("An opener with the name \"" + opener.getName() + "\" already exists and will not be registered");
            return;
        }
        try {
            opener.doSetup();
            registered.put(opener.getName(), opener);
            if (getCratesPlus().getConfigHandler().isDebugMode())
                getCratesPlus().getLogger().info("[DEBUG] Opener \"" + opener.getName() + "\" has been registered");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getCratesPlusVersion() {
        return getCratesPlus().getDescription().getVersion();
    }

    public Opener getOpener(Crate crate) {
        if (registered.containsKey(crate.getOpener()) && registered.get(crate.getOpener()).doesSupport(crate))
            return registered.get(crate.getOpener());
        return getDefaultOpener(crate);
    }

    public Opener getDefaultOpener(Crate crate) {
        String name = crate.getClass().getName().toLowerCase();
        if (defaults.containsKey(name)) {
            Opener opener = registered.get(defaults.get(name));
            if (opener.doesSupport(crate))
                return opener;
        }
        return registered.get("NoGUI");
    }

    public CratesPlus getCratesPlus() {
        return cratesPlus;
    }

    public boolean openerExist(String name) {
        return registered.containsKey(name);
    }

    public Opener getOpener(String name) {
        return registered.get(name);
    }

    public HashMap<String, Opener> getRegistered() {
        return registered;
    }

}
