package plus.crates.Opener;

import plus.crates.CratesPlus;

public class NoGUIOpener extends Opener {

	public NoGUIOpener(CratesPlus cratesPlus) {
		super(cratesPlus, "NoGUI");
	}

	@Override
	public void doSetup() {

	}

	@Override
	public void doOpen() {
		getWinning().runWin(getPlayer());
		finish();
	}

	@Override
	public void doReopen() {

	}

}
