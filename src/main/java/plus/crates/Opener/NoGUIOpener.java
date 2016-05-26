package plus.crates.Opener;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import plus.crates.Crate;
import plus.crates.CratesPlus;

public class NoGUIOpener extends Opener {

	public NoGUIOpener(CratesPlus cratesPlus) {
		super(cratesPlus, "NoGUI");
	}

	@Override
	public void doSetup() {

	}

	@Override
	public void doOpen(Player player, Crate crate, Location location) {
		getWinning(crate).runWin(player);
		finish(player);
	}

	@Override
	public void doReopen(Player player, Crate crate, Location location) {

	}

}
