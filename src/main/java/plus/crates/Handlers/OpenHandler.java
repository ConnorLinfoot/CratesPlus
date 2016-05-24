package plus.crates.Handlers;

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
	private String enabledOpener;

	public OpenHandler(CratesPlus cratesPlus) {
		this.cratesPlus = cratesPlus;
		registerDefaults();
	}

	private void registerDefaults() {
		BasicGUIOpener basicGUIOpener = new BasicGUIOpener(cratesPlus);
		NoGUIOpener noGUIOpener = new NoGUIOpener(cratesPlus);
		registerOpener(basicGUIOpener);
		registerOpener(noGUIOpener);

		if (cratesPlus.getConfigHandler().isDoGui()) {
			enabledOpener = basicGUIOpener.getName();
		} else {
			enabledOpener = noGUIOpener.getName();
		}
	}

	public void registerOpener(Opener opener) {
		registered.put(opener.getName(), opener);
	}

	public Opener getEnabledOpener() {
		if (registered.containsKey(enabledOpener))
			return registered.get(enabledOpener);
		return null;
	}

	public void setEnabledOpener(String enabledOpener) {
		this.enabledOpener = enabledOpener;
	}

	public CratesPlus getCratesPlus() {
		return cratesPlus;
	}

	public boolean openerExist(String name) {
		return registered.containsKey(name);
	}

}
