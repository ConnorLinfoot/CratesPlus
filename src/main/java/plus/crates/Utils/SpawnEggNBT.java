package plus.crates.Utils;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.SpawnEgg;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Represents a spawn egg that can be used to spawn mobs. Only for 1.9+, from https://gist.github.com/tastybento/6053eeb68c1c19d33540
 * Updated to use reflection instead of direct NMS.
 *
 * @author tastybento & Connor Linfoot
 */
public class SpawnEggNBT {
    private EntityType type;

    public SpawnEggNBT(EntityType type) {
        this.type = type;
    }

    public EntityType getSpawnedType() {
        return type;
    }

    public void setSpawnedType(EntityType type) {
        if (type.isAlive()) {
            this.type = type;
        }
    }

    public ItemStack toItemStack(int amount, boolean is_1_11) {
        ItemStack item = new ItemStack(Material.MONSTER_EGG, amount);
        try {
            Class craftItemStack = ReflectionUtil.getCBClass("inventory.CraftItemStack");
            Method asNMSCopyMethod = craftItemStack.getDeclaredMethod("asNMSCopy", ItemStack.class);
            Object nmsItemStack = asNMSCopyMethod.invoke(null, item);
            Class nmsItemStackClass = ReflectionUtil.getNMSClass("ItemStack");
            Object nbtTagCompound = nmsItemStackClass.getDeclaredMethod("getTag").invoke(nmsItemStack);
            Class nbtTagCompoundClass = ReflectionUtil.getNMSClass("NBTTagCompound");
            if (nbtTagCompound == null) {
                nbtTagCompound = nbtTagCompoundClass.getConstructor().newInstance();
            }
            Object id = nbtTagCompoundClass.getConstructor().newInstance();
            Method setStringMethod = nbtTagCompoundClass.getDeclaredMethod("setString", String.class, String.class);

            if (is_1_11) {
                setStringMethod.invoke(id, "id", "minecraft:" + type.getName());
            } else {
                setStringMethod.invoke(id, "id", type.getName());
            }

            Method setMethod = nbtTagCompoundClass.getDeclaredMethod("set", String.class, ReflectionUtil.getNMSClass("NBTBase"));
            setMethod.invoke(nbtTagCompound, "EntityTag", id);
            Method setTagMethod = nmsItemStackClass.getDeclaredMethod("setTag", nbtTagCompoundClass);
            setTagMethod.invoke(nmsItemStack, nbtTagCompound);
            Method asBukkitCopy = craftItemStack.getDeclaredMethod("asBukkitCopy", nmsItemStackClass);
            return (ItemStack) asBukkitCopy.invoke(null, nmsItemStack);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static SpawnEggNBT fromItemStack(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() != Material.MONSTER_EGG)
            return null;
        try {
            Class craftItemStack = ReflectionUtil.getCBClass("inventory.CraftItemStack");
            Method asNMSCopyMethod = craftItemStack.getDeclaredMethod("asNMSCopy", ItemStack.class);
            Object nmsItemStack = asNMSCopyMethod.invoke(null, itemStack);
            Class nmsItemStackClass = ReflectionUtil.getNMSClass("ItemStack");
            Object nbtTagCompound = nmsItemStackClass.getDeclaredMethod("getTag").invoke(nmsItemStack);
            Class nbtTagCompoundClass = ReflectionUtil.getNMSClass("NBTTagCompound");
            if (nbtTagCompound != null) {
                Method getCompoundMethod = nbtTagCompoundClass.getDeclaredMethod("getCompound", String.class);
                Object entityTagCompount = getCompoundMethod.invoke(nbtTagCompound, "EntityTag");
                Method getStringMethod = nbtTagCompoundClass.getDeclaredMethod("getString", String.class);
                String type = (String) getStringMethod.invoke(entityTagCompount, "id");
                type = type.replaceFirst("minecraft:", "");
                switch (type) {
                    case "CAVESPIDER":
                        type = "CAVE_SPIDER";
                        break;
                }
                EntityType entityType = EntityType.fromName(type);
                if (entityType == null || !entityType.isAlive())
                    return null;
                return new SpawnEggNBT(entityType);
            } else if (itemStack.getData() instanceof SpawnEgg) {
                return new SpawnEggNBT(((SpawnEgg) itemStack.getData()).getSpawnedType());
            }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}