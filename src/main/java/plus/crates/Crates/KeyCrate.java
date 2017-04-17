package plus.crates.Crates;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import plus.crates.CratesPlus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class KeyCrate extends Crate {
	protected Key key;
	protected HashMap<String, Location> locations = new HashMap<>();
	protected boolean preview = true;
	protected boolean hidePercentages = false;
	protected double knockback = 0.0;

	public KeyCrate(CratesPlus cratesPlus, String name) {
		super(cratesPlus, name);
	}

	protected void loadCrate() {
		super.loadCrate();
		if (cratesPlus.getConfig().isSet("Crates." + name + ".Preview"))
			this.preview = cratesPlus.getConfig().getBoolean("Crates." + name + ".Preview");
		if (cratesPlus.getConfig().isSet("Crates." + name + ".Knockback"))
			this.knockback = cratesPlus.getConfig().getDouble("Crates." + name + ".Knockback");
		if (cratesPlus.getConfig().isSet("Crates." + name + ".Hide Percentages"))
			this.hidePercentages = cratesPlus.getConfig().getBoolean("Crates." + name + ".Hide Percentages");

		if (!cratesPlus.getConfig().isSet("Crates." + name + ".Key") || !cratesPlus.getConfig().isSet("Crates." + name + ".Key.Item") || !cratesPlus.getConfig().isSet("Crates." + name + ".Key.Name") || !cratesPlus.getConfig().isSet("Crates." + name + ".Key.Enchanted"))
			return;

		this.key = new Key(name, Material.valueOf(cratesPlus.getConfig().getString("Crates." + name + ".Key.Item")), cratesPlus.getConfig().getString("Crates." + name + ".Key.Name").replaceAll("%type%", getName(true)), cratesPlus.getConfig().getBoolean("Crates." + name + ".Key.Enchanted"), cratesPlus);
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public HashMap<String, Location> getLocations() {
		return locations;
	}

	public void addLocation(String string, Location location) {
		locations.put(string, location);
	}

	public Location getLocation(String key) {
		return locations.get(key);
	}

	public Location removeLocation(String key) {
		return locations.remove(key);
	}

	public void loadHolograms(Location location) {
		// Do holograms
		if (cratesPlus.getConfigHandler().getHolograms(this.slug) == null || cratesPlus.getConfigHandler().getHolograms(this.slug).isEmpty())
			return;

		ArrayList<String> list = new ArrayList<>();
		for (String string : cratesPlus.getConfigHandler().getHolograms(this.slug))
			list.add(cratesPlus.getMessageHandler().doPlaceholders(string, null, this, null));
		cratesPlus.getVersion_util().createHologram(location, list, this);
	}

	public void removeHolograms(Location location) {
		cratesPlus.getVersion_util().removeHologram(location);
	}

	public void removeFromConfig(Location location) {
		List<String> locations = new ArrayList<>();
		if (cratesPlus.getDataConfig().isSet("Crate Locations." + this.getName(false).toLowerCase()))
			locations = cratesPlus.getDataConfig().getStringList("Crate Locations." + this.getName(false).toLowerCase());
		if (locations.contains(location.getWorld().getName() + "|" + location.getBlockX() + "|" + location.getBlockY() + "|" + location.getBlockZ()))
			locations.remove(location.getWorld().getName() + "|" + location.getBlockX() + "|" + location.getBlockY() + "|" + location.getBlockZ());
		cratesPlus.getDataConfig().set("Crate Locations." + this.getName(false).toLowerCase(), locations);
		try {
			cratesPlus.getDataConfig().save(cratesPlus.getDataFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addToConfig(Location location) {
		List<String> locations = new ArrayList<>();
		if (cratesPlus.getDataConfig().isSet("Crate Locations." + this.getName(false).toLowerCase()))
			locations = cratesPlus.getDataConfig().getStringList("Crate Locations." + this.getName(false).toLowerCase());
		locations.add(location.getWorld().getName() + "|" + location.getBlockX() + "|" + location.getBlockY() + "|" + location.getBlockZ());
		cratesPlus.getDataConfig().set("Crate Locations." + this.getName(false).toLowerCase(), locations);
		try {
			cratesPlus.getDataConfig().save(cratesPlus.getDataFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void give(OfflinePlayer offlinePlayer, Integer amount) {
		cratesPlus.getCrateHandler().giveCrateKey(offlinePlayer, getName(false), amount);
	}

	public double getKnockback() {
		return knockback;
	}
}
