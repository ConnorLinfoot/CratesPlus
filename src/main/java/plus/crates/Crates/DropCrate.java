package plus.crates.Crates;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import plus.crates.CratesPlus;
import plus.crates.Handlers.ConfigHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Drop it like it's hot!
 */
public class DropCrate extends SupplyCrate implements Listener {
    private Random rand = new Random();
    private List<Location> drops = new ArrayList<>();
    private List<String> worlds = new ArrayList<>();
    private Integer minSpawnInterval = 60 * 60; // 1 Hour
    private Integer maxSpawnInterval = 120 * 60; // 2 Hours
    private Integer radiusClosestToPlayer = 300; // will spawn 300 blocks close to a player TODO Make this and any others configurable!
    private Integer despawnTimer = 30 * 60; // will despawn after 30 minutes if nobody has found it
    private Integer minPlayers = 2;

    public DropCrate(ConfigHandler configHandler, String name) {
        super(configHandler, name);
        loadCrateMore();
        startTimer();
        getCratesPlus().getServer().getPluginManager().registerEvents(this, getCratesPlus());
    }

    private void loadCrateMore() {
        CratesPlus cratesPlus = getCratesPlus();
        if (cratesPlus.getConfig().isSet("Crates." + name + ".Worlds"))
            this.worlds = cratesPlus.getConfig().getStringList("Crates." + name + ".Worlds");

        if (cratesPlus.getConfig().isSet("Crates." + name + ".Min Spawn Interval"))
            this.minSpawnInterval = cratesPlus.getConfig().getInt("Crates." + name + ".Min Spawn Interval", this.minSpawnInterval);

        if (cratesPlus.getConfig().isSet("Crates." + name + ".Max Spawn Interval"))
            this.maxSpawnInterval = cratesPlus.getConfig().getInt("Crates." + name + ".Max Spawn Interval", this.maxSpawnInterval);

        if (cratesPlus.getConfig().isSet("Crates." + name + ".Min Players"))
            this.minPlayers = cratesPlus.getConfig().getInt("Crates." + name + ".Min Players", this.minPlayers);

        if (cratesPlus.getConfig().isSet("Crates." + name + ".Despawn Timer"))
            this.despawnTimer = cratesPlus.getConfig().getInt("Crates." + name + ".Despawn Timer", this.despawnTimer);
    }

    private void startTimer() {
        Integer timer = randInt(minSpawnInterval, maxSpawnInterval);
        // TODO Use a debug option to show this?
        getCratesPlus().getLogger().info("Will attempt to drop crate \"" + getName() + "\" in " + timer + " seconds!");
        // TODO Should we validate the config first? I feel like we should...
        Bukkit.getScheduler().runTaskLater(getCratesPlus(), this::spawnCrate, 20L * timer);
    }

    private void spawnCrate() {
        startTimer(); // Start timer for the next drop
        if (Bukkit.getOnlinePlayers().size() < minPlayers) {
            return; // Not enough players online :(
        }

        List<String> worlds = new ArrayList<>();
        for (String worldName : this.worlds) {
            if (Bukkit.getWorld(worldName) == null)
                continue;
            if (Bukkit.getWorld(worldName).getPlayers().size() < minPlayers)
                continue; // No players in world
            worlds.add(worldName);
        }

        if (worlds.isEmpty()) {
            return; // No world with players found :(
        }

        World world = Bukkit.getWorld(worlds.get(randInt(0, worlds.size() - 1)));
        Player player = world.getPlayers().get(randInt(0, world.getPlayers().size() - 1)); // Get random player to spawn crate near
        Location location = player.getLocation().clone();
        double randomX = randInt((int) location.getX() - radiusClosestToPlayer, (int) location.getX() + radiusClosestToPlayer);
        double randomZ = randInt((int) location.getZ() - radiusClosestToPlayer, (int) location.getZ() + radiusClosestToPlayer);
        location.setX(randomX);
        location.setZ(randomZ);
        location = world.getHighestBlockAt(location).getLocation().clone().add(0, 0, 0);
        if (location.getBlock().getType().equals(Material.AIR)) {
            location.getBlock().setType(getBlock());
            // TODO idk how to handle the below, is it even needed with 1.13? So reflection if thats the case...
//            location.getBlock().setData((byte) getBlockData());
            System.out.println("Crate dropped at " + location.toString());
            drops.add(location);
            // TODO Broadcast, populate and what not

            if (despawnTimer > 0) {
                // TODO Despawn
                Location finalLocation = location;
                Bukkit.getScheduler().runTaskLater(getCratesPlus(), () -> {
                    if (drops.contains(finalLocation)) {
                        drops.remove(finalLocation);
                        finalLocation.getBlock().setType(Material.AIR);
                    }
                }, despawnTimer * 20);
            }
        }
    }

    private int randInt(int min, int max) {
        return rand.nextInt((max - min) + 1) + min;
    }

    private void openCrate(Player player, Location location) {
        drops.remove(location);
        handleWin(player, location.getBlock());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() != null && event.getClickedBlock().getType() == getBlock() && drops.contains(event.getClickedBlock().getLocation())) {
            openCrate(event.getPlayer(), event.getClickedBlock().getLocation());
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        drops.forEach(location -> location.getBlock().setType(Material.AIR));
    }
}
