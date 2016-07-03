package plus.crates.Utils;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.SpawnEgg;
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

	public ItemStack getSpawnEgg(EntityType entityType, Integer amount) { // TODO - Make this for working with NBT/1.9+
		ItemStack egg = new ItemStack(Material.MONSTER_EGG, amount);
		if (entityType != null) {
			SpawnEgg spawnEgg = new SpawnEgg(entityType);
			egg.setData(spawnEgg);
		}
		return egg;
	}

}
