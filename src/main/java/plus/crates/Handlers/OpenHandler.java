package plus.crates.Handlers;

import plus.crates.Crates.Crate;
import plus.crates.CratesPlus;
import plus.crates.Opener.BasicGUIOpener;
import plus.crates.Opener.NoGUIOpener;
import plus.crates.Opener.Opener;

import java.util.HashMap;

/**
 * Public handler for CratesPlus to be able to modify the way crates open.
 */
public class OpenHandler {
	private CratesPlus cratesPlus;
	private HashMap<String, Opener> registered = new HashMap<>();
	private String defaultOpener;

	public OpenHandler(CratesPlus cratesPlus) {
		this.cratesPlus = cratesPlus;
		registerDefaults();
	}

	private void registerDefaults() {
		registerOpener(new BasicGUIOpener(cratesPlus));
		registerOpener(new NoGUIOpener(cratesPlus));
		defaultOpener = cratesPlus.getConfigHandler().getDefaultOpener();
	}

	public void registerOpener(Opener opener) {
		if (registered.containsKey(opener.getName())) {
			getCratesPlus().getLogger().warning("An opener with the name \"" + opener.getName() + "\" already exists and will not be registered");
			return;
		}
		try {
			opener.doSetup();
			registered.put(opener.getName(), opener);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getCratesPlusVersion() {
		return getCratesPlus().getDescription().getVersion();
	}

	public Opener getOpener(Crate crate) {
		//TODO
//		if (registered.containsKey(crate.getOpener()))
//			return registered.get(crate.getOpener());
		return getDefaultOpener(crate);
	}

	public Opener getDefaultOpener(Crate crate) {
		if (registered.containsKey(defaultOpener)) {
			Opener opener = registered.get(defaultOpener);
			if (opener.doesSupport(crate))
				return opener;
		}
		return registered.get("NoGUI");
	}

	public void setDefaultOpener(String defaultOpener) {
		this.defaultOpener = defaultOpener;
		cratesPlus.getConfig().set("Default Opener", defaultOpener);
		cratesPlus.saveConfig();
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
