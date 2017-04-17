package plus.crates.Crates;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import plus.crates.CratesPlus;
import plus.crates.Opener.Opener;

import java.util.ArrayList;

public abstract class Crate {
	protected CratesPlus cratesPlus;
	protected String name;
	protected String slug;
	protected ChatColor color = ChatColor.WHITE;
	protected Material block = Material.CHEST;
	protected String permission = null;
	protected ArrayList<Winning> winnings = new ArrayList<>();
	protected double totalPercentage = 0;
	protected boolean firework = false;
	protected boolean broadcast = false;
	protected String opener = null;
	protected Integer cooldown = null;

	public Crate(CratesPlus cratesPlus, String name) {
		this.cratesPlus = cratesPlus;
		this.name = name;
		this.slug = name.toLowerCase();
		loadCrate();
	}

	public double getTotalPercentage() {
		return totalPercentage;
	}

	protected void loadCrate() {
		if (cratesPlus.getConfig().isSet("Crates." + name + ".Color"))
			this.color = ChatColor.valueOf(cratesPlus.getConfig().getString("Crates." + name + ".Color").toUpperCase());
		if (cratesPlus.getConfig().isSet("Crates." + name + ".Block"))
			this.block = Material.valueOf(cratesPlus.getConfig().getString("Crates." + name + ".Block").toUpperCase());
		if (cratesPlus.getConfig().isSet("Crates." + name + ".Permission"))
			this.permission = cratesPlus.getConfig().getString("Crates." + name + ".Permission");
		if (cratesPlus.getConfig().isSet("Crates." + name + ".Firework"))
			this.firework = cratesPlus.getConfig().getBoolean("Crates." + name + ".Firework");
		if (cratesPlus.getConfig().isSet("Crates." + name + ".Broadcast"))
			this.broadcast = cratesPlus.getConfig().getBoolean("Crates." + name + ".Broadcast");
		if (cratesPlus.getConfig().isSet("Crates." + name + ".Opener"))
			this.opener = cratesPlus.getConfig().getString("Crates." + name + ".Opener");
		if (cratesPlus.getConfig().isSet("Crates." + name + ".Cooldown"))
			this.cooldown = cratesPlus.getConfig().getInt("Crates." + name + ".Cooldown");

		if (!cratesPlus.getConfig().isSet("Crates." + name + ".Winnings"))
			return;

		for (String id : cratesPlus.getConfig().getConfigurationSection("Crates." + name + ".Winnings").getKeys(false)) {
			String path = "Crates." + name + ".Winnings." + id;
			Winning winning = new Winning(this, path, cratesPlus, null);
			if (totalPercentage + winning.getPercentage() > 100 || !winning.isValid()) {
				if (totalPercentage + winning.getPercentage() > 100)
					Bukkit.getLogger().warning("Your percentages must NOT add up to more than 100%");
				break;
			}
			totalPercentage = totalPercentage + winning.getPercentage();
			winnings.add(winning);
		}
	}

	public CratesPlus getCratesPlus() {
		return cratesPlus;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getName(boolean includecolor) {
		if (includecolor) return getColor() + this.name;
		return this.name;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public String getSlug() {
		return slug;
	}

	public void setColor(ChatColor color) {
		this.color = color;
	}

	public ChatColor getColor() {
		return color;
	}

	public void setBlock(Material block) {
		this.block = block;
	}

	public Material getBlock() {
		return block;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public String getPermission() {
		return permission;
	}

	public boolean isFirework() {
		return firework;
	}

	public boolean isBroadcast() {
		return broadcast;
	}

	public ArrayList<Winning> getWinnings() {
		return winnings;
	}

	public boolean containsCommandItem() {
		for (Winning winning : getWinnings()) {
			if (winning.isCommand())
				return true;
		}
		return false;
	}

	public boolean supportsOpener(Opener opener) {
		return opener.doesSupport(this);
	}

	public void giveAll(Integer amount) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			give(player, amount);
		}
	}

	public void give(OfflinePlayer offlinePlayer, Integer amount) {

	}

}
