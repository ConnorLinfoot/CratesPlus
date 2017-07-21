package plus.crates.Utils;

import org.bukkit.enchantments.Enchantment;

public class EnchantmentUtil {

    public static Enchantment getEnchantmentFromNiceName(String name) {
        Enchantment enchantment = null;
        try {
            enchantment = Enchantment.getByName(name);
            enchantment = Enchantment.getById(Integer.parseInt(name));
        } catch (Exception ignored) {
        }

        if (enchantment != null)
            return enchantment;

        if (name.toLowerCase().equals("sharpness"))
            enchantment = Enchantment.DAMAGE_ALL;
        else if (name.toLowerCase().equals("unbreaking"))
            enchantment = Enchantment.DURABILITY;
        else if (name.toLowerCase().equals("efficiency"))
            enchantment = Enchantment.DIG_SPEED;
        else if (name.toLowerCase().equals("protection"))
            enchantment = Enchantment.PROTECTION_ENVIRONMENTAL;
        else if (name.toLowerCase().equals("power"))
            enchantment = Enchantment.ARROW_DAMAGE;
        else if (name.toLowerCase().equals("punch"))
            enchantment = Enchantment.ARROW_KNOCKBACK;
        else if (name.toLowerCase().equals("infinite"))
            enchantment = Enchantment.ARROW_INFINITE;

        return enchantment;
    }

}
