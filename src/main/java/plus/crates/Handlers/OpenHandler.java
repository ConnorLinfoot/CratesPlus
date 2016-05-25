package plus.crates.Handlers;

import plus.crates.Crate;
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
		BasicGUIOpener basicGUIOpener = new BasicGUIOpener(cratesPlus);
		NoGUIOpener noGUIOpener = new NoGUIOpener(cratesPlus);
		registerOpener(basicGUIOpener);
		registerOpener(noGUIOpener);
		defaultOpener = cratesPlus.getConfigHandler().getDefaultOpener();
	}

	public void registerOpener(Opener opener) {
		registered.put(opener.getName(), opener);
		opener.doSetup();
	}

	public Opener getOpener(Crate crate) {
		if (registered.containsKey(crate.getOpener()))
			return registered.get(crate.getName());
		return getDefaultOpener();
	}

	public Opener getDefaultOpener() {
		if (registered.containsKey(defaultOpener))
			return registered.get(defaultOpener);
		return registered.get("NoGUI");
	}

	public void setDefaultOpener(String defaultOpener) {
		this.defaultOpener = defaultOpener;
	}

	public CratesPlus getCratesPlus() {
		return cratesPlus;
	}

	public boolean openerExist(String name) {
		return registered.containsKey(name);
	}

	public HashMap<String, Opener> getRegistered() {
		return registered;
	}

}
