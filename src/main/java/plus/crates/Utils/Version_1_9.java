package plus.crates.Utils;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import plus.crates.CratesPlus;

import java.util.List;

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

    public ItemStack getSpawnEgg(EntityType entityType, Integer amount) {
        ItemStack egg = new ItemStack(LegacyMaterial.MONSTER_EGG.getMaterial(), amount);
        if (entityType != null) {
            SpawnEggNBT spawnEgg = new SpawnEggNBT(entityType);
            egg = spawnEgg.toItemStack(amount, cratesPlus.versionCompare(cratesPlus.getBukkitVersion(), "1.11") > -1);
        }
        return egg;
    }

    public EntityType getEntityTypeFromItemStack(ItemStack itemStack) {
        SpawnEggNBT spawnEggNBT = SpawnEggNBT.fromItemStack(itemStack);
        return spawnEggNBT.getSpawnedType();
    }

    public ItemMeta handleItemFlags(ItemMeta itemMeta, List<String> flags) {
        if (flags.size() > 0) {
            for (String flag : flags) {
                try {
                    ItemFlag itemFlag = ItemFlag.valueOf(flag.toUpperCase());
                    if (itemFlag != null) {
                        itemMeta.addItemFlags(itemFlag);
                    }
                } catch (Exception ignored) {

                }
            }
        }
        return itemMeta;
    }

}
