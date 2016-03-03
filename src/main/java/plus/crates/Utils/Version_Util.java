package plus.crates.Utils;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Version_Util {

    public ItemStack getItemInPlayersHand(Player player) {
        return player.getItemInHand();
    }

    public ItemStack getItemInPlayersOffHand(Player player) {
        return null;
    }

    public void removeItemInOffHand(Player player) {

    }

}
