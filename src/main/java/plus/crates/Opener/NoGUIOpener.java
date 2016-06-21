package plus.crates.Opener;

import org.bukkit.Location;
import org.bukkit.Sound;
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
		Sound sound;
		try {
			sound = Sound.valueOf("CHEST_OPEN");
		} catch (Exception e) {
			try {
				sound = Sound.valueOf("BLOCK_CHEST_OPEN");
			} catch (Exception ee) {
				return; // This should never happen!
			}
		}
		player.playSound(player.getLocation(), sound, (float) 0.5, 1);
		getWinning(crate).runWin(player);
		finish(player);
	}

	@Override
	public void doReopen(Player player, Crate crate, Location location) {

	}

}
