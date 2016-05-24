package plus.crates.Utils;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import plus.crates.CratesPlus;

public class Version_1_9 extends Version_Util {

	public Version_1_9(CratesPlus cratesPlus) {
		super(cratesPlus);
	}

	public ItemStack getItemInPlayersHand(Player player) {
		return player.getInventory().getItemInMainHand();
	}

	public ItemStack getItemInPlayersOffHand(Player player) {
		return player.getInventory().getItemInOffHand();
	}

	public void removeItemInOffHand(Player player) {
		player.getInventory().setItemInOffHand(null);
	}

}
