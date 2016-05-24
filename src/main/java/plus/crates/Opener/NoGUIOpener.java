package plus.crates.Opener;

import plus.crates.CratesPlus;

public class NoGUIOpener extends Opener {

	public NoGUIOpener(CratesPlus cratesPlus) {
		super(cratesPlus, "NoGUI");
	}

	@Override
	public void doTask() {
		getWinning().runWin(getPlayer());
	}

}
