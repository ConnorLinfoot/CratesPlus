package plus.crates;

import org.bukkit.Material;

public class Key {
	private Material material;
	private String name;
	private boolean enchanted;

	public Key(Material material, String name, boolean enchanted) {
		if (material == null)
			material = Material.TRIPWIRE_HOOK;
		this.material = material;
		this.name = name;
		this.enchanted = enchanted;
	}

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isEnchanted() {
		return enchanted;
	}

	public void setEnchanted(boolean enchanted) {
		this.enchanted = enchanted;
	}
}
