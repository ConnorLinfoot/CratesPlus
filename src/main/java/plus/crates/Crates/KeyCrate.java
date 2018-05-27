package plus.crates.Crates;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import plus.crates.CratesPlus;
import plus.crates.Handlers.ConfigHandler;
import plus.crates.Handlers.MessageHandler;
import plus.crates.Utils.GUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class KeyCrate extends Crate {
    protected Key key;
    protected HashMap<String, Location> locations = new HashMap<>();
    protected boolean preview = true;
    protected double knockback = 0.0;

    public KeyCrate(ConfigHandler configHandler, String name) {
        super(configHandler, name);
        loadCrate();
    }

    protected void loadCrate() {
        CratesPlus cratesPlus = getCratesPlus();
        if (cratesPlus.getConfig().isSet("Crates." + name + ".Preview"))
            this.preview = cratesPlus.getConfig().getBoolean("Crates." + name + ".Preview");
        if (cratesPlus.getConfig().isSet("Crates." + name + ".Knockback"))
            this.knockback = cratesPlus.getConfig().getDouble("Crates." + name + ".Knockback");

        if (!cratesPlus.getConfig().isSet("Crates." + name + ".Key") || !cratesPlus.getConfig().isSet("Crates." + name + ".Key.Item") || !cratesPlus.getConfig().isSet("Crates." + name + ".Key.Name") || !cratesPlus.getConfig().isSet("Crates." + name + ".Key.Enchanted"))
            return;

        this.key = new Key(this, Material.valueOf(cratesPlus.getConfig().getString("Crates." + name + ".Key.Item")), (short) cratesPlus.getConfig().getInt("Crates." + name + ".Key.Data", 0), cratesPlus.getConfig().getString("Crates." + name + ".Key.Name").replaceAll("%type%", getName(true)), cratesPlus.getConfig().getBoolean("Crates." + name + ".Key.Enchanted"), cratesPlus.getConfig().getStringList("Crates." + name + ".Key.Lore"), cratesPlus);
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
        CratesPlus cratesPlus = getCratesPlus();

        // Do holograms
        if (cratesPlus.getConfigHandler().getHolograms(this.slug) == null || cratesPlus.getConfigHandler().getHolograms(this.slug).isEmpty())
            return;

        ArrayList<String> list = new ArrayList<>();
        for (String line : cratesPlus.getConfigHandler().getHolograms(this.slug))
            list.add(MessageHandler.convertPlaceholders(line, null, this, null));
        cratesPlus.getHologramHandler().getHologramPlugin().getHologram().create(location, this, list);
    }

    public void removeHolograms(Location location) {
        getCratesPlus().getHologramHandler().getHologramPlugin().getHologram().remove(location, this);
    }

    public void removeFromConfig(Location location) {
        CratesPlus cratesPlus = getCratesPlus();

        List<String> locations = new ArrayList<>();
        if (cratesPlus.getStorageHandler().getFlatConfig().isSet("Crate Locations." + this.getName(false).toLowerCase()))
            locations = cratesPlus.getStorageHandler().getFlatConfig().getStringList("Crate Locations." + this.getName(false).toLowerCase());
        if (locations.contains(location.getWorld().getName() + "|" + location.getBlockX() + "|" + location.getBlockY() + "|" + location.getBlockZ()))
            locations.remove(location.getWorld().getName() + "|" + location.getBlockX() + "|" + location.getBlockY() + "|" + location.getBlockZ());
        cratesPlus.getStorageHandler().getFlatConfig().set("Crate Locations." + this.getName(false).toLowerCase(), locations);
        cratesPlus.getStorageHandler().saveFlat();
    }

    public void addToConfig(Location location) {
        CratesPlus cratesPlus = getCratesPlus();

        List<String> locations = new ArrayList<>();
        if (cratesPlus.getStorageHandler().getFlatConfig().isSet("Crate Locations." + this.getName(false).toLowerCase()))
            locations = cratesPlus.getStorageHandler().getFlatConfig().getStringList("Crate Locations." + this.getName(false).toLowerCase());
        locations.add(location.getWorld().getName() + "|" + location.getBlockX() + "|" + location.getBlockY() + "|" + location.getBlockZ());
        cratesPlus.getStorageHandler().getFlatConfig().set("Crate Locations." + this.getName(false).toLowerCase(), locations);
        cratesPlus.getStorageHandler().saveFlat();
    }

    public boolean give(OfflinePlayer offlinePlayer, Integer amount) {
        getCratesPlus().getCrateHandler().giveCrateKey(offlinePlayer, getName(false), amount);
        return true;
    }

    public double getKnockback() {
        return knockback;
    }

    public void openPreviewGUI(Player player) {
        List<Winning> winnings = this.getWinnings();
        GUI previewGUI = new GUI(this.getName(true) + " " + MessageHandler.getMessage("Possible Wins:", null, this, null));
        for (Winning winning : winnings) {
            ItemStack itemStack = winning.getPreviewItemStack();
            if (itemStack == null)
                continue;
            previewGUI.addItem(itemStack);
        }
        previewGUI.open(player);
    }

}
