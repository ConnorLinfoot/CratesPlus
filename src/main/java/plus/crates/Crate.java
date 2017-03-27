package plus.crates;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import plus.crates.Handlers.ConfigHandler;
import plus.crates.Utils.Hologram;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Crate {
	private CratesPlus cratesPlus;
	private String name;
	private String slug;
	private ChatColor color = ChatColor.WHITE;
	private Material block = Material.CHEST;
	private Integer blockData = 0;
	private boolean firework = false;
	private boolean broadcast = false;
	private boolean preview = true;
	private boolean hidePercentages = false;
	private double knockback = 0.0;
	private ArrayList<Winning> winnings = new ArrayList<>();
	private double totalPercentage = 0;
	private Key key;
	private HashMap<String, Location> locations = new HashMap<>();
	private String permission = null;
	private HashMap<Location, Hologram> holograms = new HashMap<>();
	private String opener = null;
	private Integer cooldown = null;

	public Crate(String name, CratesPlus cratesPlus, ConfigHandler configHandler) {
		this.cratesPlus = cratesPlus;
		this.name = name;
		this.slug = name.toLowerCase();

		if (cratesPlus.getConfig().isSet("Crates." + name + ".Color"))
			this.color = ChatColor.valueOf(cratesPlus.getConfig().getString("Crates." + name + ".Color").toUpperCase());
		if (cratesPlus.getConfig().isSet("Crates." + name + ".Block"))
			this.block = Material.valueOf(cratesPlus.getConfig().getString("Crates." + name + ".Block").toUpperCase());
		if (cratesPlus.getConfig().isSet("Crates." + name + ".Block Data"))
			this.blockData = Integer.valueOf(cratesPlus.getConfig().getString("Crates." + name + ".Block Data"));
		if (cratesPlus.getConfig().isSet("Crates." + name + ".Firework"))
			this.firework = cratesPlus.getConfig().getBoolean("Crates." + name + ".Firework");
		if (cratesPlus.getConfig().isSet("Crates." + name + ".Broadcast"))
			this.broadcast = cratesPlus.getConfig().getBoolean("Crates." + name + ".Broadcast");
		if (cratesPlus.getConfig().isSet("Crates." + name + ".Preview"))
			this.preview = cratesPlus.getConfig().getBoolean("Crates." + name + ".Preview");
		if (cratesPlus.getConfig().isSet("Crates." + name + ".Knockback"))
			this.knockback = cratesPlus.getConfig().getDouble("Crates." + name + ".Knockback");
		if (cratesPlus.getConfig().isSet("Crates." + name + ".Permission"))
			this.permission = cratesPlus.getConfig().getString("Crates." + name + ".Permission");
		if (cratesPlus.getConfig().isSet("Crates." + name + ".Hide Percentages"))
			this.hidePercentages = cratesPlus.getConfig().getBoolean("Crates." + name + ".Hide Percentages");
		if (cratesPlus.getConfig().isSet("Crates." + name + ".Opener"))
			this.opener = cratesPlus.getConfig().getString("Crates." + name + ".Opener");
		if (cratesPlus.getConfig().isSet("Crates." + name + ".Cooldown"))
			this.cooldown = cratesPlus.getConfig().getInt("Crates." + name + ".Cooldown");

		if (!cratesPlus.getConfig().isSet("Crates." + name + ".Key") || !cratesPlus.getConfig().isSet("Crates." + name + ".Key.Item") || !cratesPlus.getConfig().isSet("Crates." + name + ".Key.Name") || !cratesPlus.getConfig().isSet("Crates." + name + ".Key.Enchanted"))
			return;

		this.key = new Key(name, Material.valueOf(cratesPlus.getConfig().getString("Crates." + name + ".Key.Item")), cratesPlus.getConfig().getString("Crates." + name + ".Key.Name").replaceAll("%type%", getName(true)), cratesPlus.getConfig().getBoolean("Crates." + name + ".Key.Enchanted"), cratesPlus);

		if (!cratesPlus.getConfig().isSet("Crates." + name + ".Winnings"))
			return;

		for (String id : cratesPlus.getConfig().getConfigurationSection("Crates." + name + ".Winnings").getKeys(false)) {
			String path = "Crates." + name + ".Winnings." + id;
			Winning winning = new Winning(this, path, cratesPlus, configHandler);
			if (totalPercentage + winning.getPercentage() > 100 || !winning.isValid()) {
				if (totalPercentage + winning.getPercentage() > 100)
					Bukkit.getLogger().warning("Your percentages must NOT add up to more than 100%");
				break;
			}
			totalPercentage = totalPercentage + winning.getPercentage();
			winnings.add(winning);
		}
	}

	public String getName() {
		return getName(false);
	}

	public String getName(boolean includecolor) {
		if (includecolor) return getColor() + this.name;
		return this.name;
	}

	public String getSlug() {
		return slug;
	}

	public ChatColor getColor() {
		return this.color;
	}

	public Material getBlock() {
		return this.block;
	}

	public short getBlockData() {
		return (short) (int) this.blockData;
	}

	public boolean isFirework() {
		return this.firework;
	}

	public boolean isBroadcast() {
		return this.broadcast;
	}

	public boolean isPreview() {
		return preview;
	}

	public boolean isHidePercentages() {
		return hidePercentages;
	}

	public double getKnockback() {
		return this.knockback;
	}

	public void reloadWinnings() {
		cratesPlus.reloadConfig();
		winnings.clear();
		for (String id : cratesPlus.getConfig().getConfigurationSection("Crates." + name + ".Winnings").getKeys(false)) {
			String path = "Crates." + name + ".Winnings." + id;
			Winning winning = new Winning(this, path, cratesPlus, cratesPlus.getConfigHandler());
			if (winning.isValid())
				winnings.add(winning);
		}
	}

	public List<Winning> getWinnings() {
		return winnings;
	}

	public void clearWinnings() {
		winnings.clear();
	}

	public void addWinning(Winning winning) {
		winnings.add(winning);
	}

	public double getTotalPercentage() {
		return totalPercentage;
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public void setColor(String color) {
		this.color = ChatColor.valueOf(color);
		String path = "Crates." + name + ".Color";
		cratesPlus.getConfig().set(path, color);
		cratesPlus.saveConfig();
		cratesPlus.reloadPlugin();
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

	public String getPermission() {
		return permission;
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

	public void loadHolograms(Location location) {
		// Do holograms
		if (cratesPlus.getConfigHandler().getHolograms(this.slug) == null || cratesPlus.getConfigHandler().getHolograms(this.slug).isEmpty())
			return;

		ArrayList<String> list = new ArrayList<>();
		for (String string : cratesPlus.getConfigHandler().getHolograms(this.slug))
			list.add(cratesPlus.getMessageHandler().doPlaceholders(string, null, this, null));
		cratesPlus.getVersion_util().createHologram(location, list, this);
	}

	public HashMap<Location, Hologram> getHolograms() {
		return holograms;
	}

	public void addHologram(Location location, Hologram hologram) {
		holograms.put(location, hologram);
	}

	public void removeHolograms(Location location) {
		cratesPlus.getVersion_util().removeHologram(location);
		if (holograms.containsKey(location)) {
			holograms.get(location).destroyAll();
			holograms.remove(location);
		}
	}

	public String getOpener() {
		return opener;
	}

	public Integer getCooldown() {
		if (cooldown == null || cooldown < 0)
			return cratesPlus.getConfigHandler().getDefaultCooldown();
		return cooldown;
	}

	public void setOpener(String opener) {
		this.opener = opener;
		cratesPlus.getConfig().set("Crates." + getName(false) + ".Opener", opener);
		cratesPlus.saveConfig();
	}

	public boolean containsCommandItem() {
		for (Winning winning : getWinnings()) {
			if (winning.isCommand())
				return true;
		}
		return false;
	}

}
