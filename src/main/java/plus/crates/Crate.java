package plus.crates;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Crate {
	private String name;
	private String slug;
	private ChatColor color = ChatColor.WHITE;
	private Material block = Material.CHEST;
	private boolean firework = false;
	private boolean broadcast = false;
	private boolean preview = true;
	private double knockback = 0.0;
	private ArrayList<Winning> winnings = new ArrayList<Winning>();
	private ArrayList<Integer> percentages = new ArrayList<Integer>();
	private int totalPercentage = 0;
	private Key key;
	private HashMap<String, Location> locations = new HashMap<String, Location>();

	public Crate(String name) {
		this.name = name;
		this.slug = name.toLowerCase();

		if (CratesPlus.getPlugin().getConfig().isSet("Crates." + name + ".Color"))
			this.color = ChatColor.valueOf(CratesPlus.getPlugin().getConfig().getString("Crates." + name + ".Color").toUpperCase());
		if (CratesPlus.getPlugin().getConfig().isSet("Crates." + name + ".Block"))
			this.block = Material.valueOf(CratesPlus.getPlugin().getConfig().getString("Crates." + name + ".Block").toUpperCase());
		if (CratesPlus.getPlugin().getConfig().isSet("Crates." + name + ".Firework"))
			this.firework = CratesPlus.getPlugin().getConfig().getBoolean("Crates." + name + ".Firework");
		if (CratesPlus.getPlugin().getConfig().isSet("Crates." + name + ".Broadcast"))
			this.broadcast = CratesPlus.getPlugin().getConfig().getBoolean("Crates." + name + ".Broadcast");
		if (CratesPlus.getPlugin().getConfig().isSet("Crates." + name + ".Preview"))
			this.preview = CratesPlus.getPlugin().getConfig().getBoolean("Crates." + name + ".Preview");
		if (CratesPlus.getPlugin().getConfig().isSet("Crates." + name + ".Knockback"))
			this.knockback = CratesPlus.getPlugin().getConfig().getDouble("Crates." + name + ".Knockback");

		if (!CratesPlus.getPlugin().getConfig().isSet("Crates." + name + ".Key") || !CratesPlus.getPlugin().getConfig().isSet("Crates." + name + ".Key.Item") || !CratesPlus.getPlugin().getConfig().isSet("Crates." + name + ".Key.Name") || !CratesPlus.getPlugin().getConfig().isSet("Crates." + name + ".Key.Enchanted"))
			return;

		this.key = new Key(Material.valueOf(CratesPlus.getPlugin().getConfig().getString("Crates." + name + ".Key.Item")), CratesPlus.getPlugin().getConfig().getString("Crates." + name + ".Key.Name").replaceAll("%type%", getName(true)), CratesPlus.getPlugin().getConfig().getBoolean("Crates." + name + ".Key.Enchanted"));

		if (!CratesPlus.getPlugin().getConfig().isSet("Crates." + name + ".Winnings"))
			return;

		for (String id : CratesPlus.getPlugin().getConfig().getConfigurationSection("Crates." + name + ".Winnings").getKeys(false)) {
			String path = "Crates." + name + ".Winnings." + id;
			Winning winning = new Winning(path);
			if (totalPercentage + winning.getPercentage() > 100 || !winning.isValid()) {
				if (totalPercentage + winning.getPercentage() > 100)
					Bukkit.getLogger().warning("Your percentages must NOT add up to more than 100%");
				break;
			}
			totalPercentage = totalPercentage + winning.getPercentage();
			winnings.add(winning);
			if (winning.getPercentage() > 0) {
				for (int i = 0; i < winning.getPercentage(); i++) {
					percentages.add(winnings.size() - 1);
				}
			}
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

	public boolean isFirework() {
		return this.firework;
	}

	public boolean isBroadcast() {
		return this.broadcast;
	}

	public boolean isPreview() {
		return preview;
	}

	public double getKnockback() {
		return this.knockback;
	}

	public void reloadWinnings() {
		CratesPlus.getPlugin().reloadConfig();
		winnings.clear();
		for (String id : CratesPlus.getPlugin().getConfig().getConfigurationSection("Crates." + name + ".Winnings").getKeys(false)) {
			String path = "Crates." + name + ".Winnings." + id;
			Winning winning = new Winning(path);
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

	public int getTotalPercentage() {
		return totalPercentage;
	}

	public ArrayList<Integer> getPercentages() {
		return percentages;
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
		CratesPlus.getPlugin().getConfig().set(path, color);
		CratesPlus.getPlugin().saveConfig();
		CratesPlus.reloadPlugin();
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

}
