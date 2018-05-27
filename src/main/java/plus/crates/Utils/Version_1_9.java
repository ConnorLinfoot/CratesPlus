package plus.crates.Utils;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import plus.crates.CratesPlus;

public class Version_1_9 extends Version_1_8 {

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

    public ItemStack getSpawnEgg(EntityType entityType, Integer amount) {
        ItemStack egg = new ItemStack(Material.MONSTER_EGG, amount);
        if (entityType != null) {
            SpawnEggNBT spawnEgg = new SpawnEggNBT(entityType);
            egg = spawnEgg.toItemStack(amount, LinfootUtil.versionCompare(cratesPlus.getBukkitVersion(), "1.11") > -1);
        }
        return egg;
    }

    public EntityType getEntityTypeFromItemStack(ItemStack itemStack) {
        SpawnEggNBT spawnEggNBT = SpawnEggNBT.fromItemStack(itemStack);
        return spawnEggNBT.getSpawnedType();
    }

}
