package com.connorlinfoot.cratesplus.Listeners;

import com.connorlinfoot.cratesplus.Crate;
import com.connorlinfoot.cratesplus.CratesPlus;
import com.connorlinfoot.cratesplus.Handlers.MessageHandler;
import com.connorlinfoot.cratesplus.Key;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Chest;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.Map;

public class BlockListeners implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();
        String title;

        for (Map.Entry<String, Crate> crate : CratesPlus.crates.entrySet()) {
            Key key = crate.getValue().getKey();
            title = key.getName();

            if (item.hasItemMeta() && item.getItemMeta().getDisplayName() != null && item.getItemMeta().getDisplayName().contains(title)) {
                event.getPlayer().sendMessage(CratesPlus.pluginPrefix + MessageHandler.getMessage(CratesPlus.getPlugin(), "Cant Place", event.getPlayer(), ChatColor.stripColor(item.getItemMeta().getDisplayName().replaceAll(title, ""))));
                event.setCancelled(true);
                return;
            }
        }

        if (item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().contains("Crate!")) {
            final String crateType = item.getItemMeta().getDisplayName().replaceAll(" Crate!", "");
            // BlockMeta to be used for some stuff in the future!
            event.getBlock().setMetadata("CrateType", new MetadataValue() {
                @Override
                public Object value() {
                    return null;
                }

                @Override
                public int asInt() {
                    return 0;
                }

                @Override
                public float asFloat() {
                    return 0;
                }

                @Override
                public double asDouble() {
                    return 0;
                }

                @Override
                public long asLong() {
                    return 0;
                }

                @Override
                public short asShort() {
                    return 0;
                }

                @Override
                public byte asByte() {
                    return 0;
                }

                @Override
                public boolean asBoolean() {
                    return false;
                }

                @Override
                public String asString() {
                    return crateType;
                }

                @Override
                public Plugin getOwningPlugin() {
                    return CratesPlus.getPlugin();
                }

                @Override
                public void invalidate() {

                }
            });
            Location location = event.getBlock().getLocation();
            location.setY(location.getBlockY() - 1);
            location.setX(location.getBlockX() + 0.5);
            location.setZ(location.getBlockZ() + 0.5);

            // Do holograms
            if (CratesPlus.holograms == null || CratesPlus.holograms.isEmpty())
                return;

            String line1;
            String line2;
            String line3;
            String line4;
            ArmorStand armorStand;
            switch (CratesPlus.holograms.size()) {
                case 1:
                    line1 = (String) CratesPlus.holograms.get(0);
                    line1 = line1.replaceAll("%crate%", crateType);
                    armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
                    armorStand.setVisible(false);
                    armorStand.setGravity(false);
                    armorStand.setCustomNameVisible(true);
                    armorStand.setCustomName(line1);
                    break;
                case 2:
                    line1 = (String) CratesPlus.holograms.get(0);
                    line1 = line1.replaceAll("%crate%", crateType);
                    line2 = (String) CratesPlus.holograms.get(1);
                    line2 = line2.replaceAll("%crate%", crateType);

                    armorStand = (ArmorStand) location.getWorld().spawnEntity(location.add(0, 0.2, 0), EntityType.ARMOR_STAND);
                    armorStand.setVisible(false);
                    armorStand.setGravity(false);
                    armorStand.setCustomNameVisible(true);
                    armorStand.setCustomName(line1);

                    armorStand = (ArmorStand) location.getWorld().spawnEntity(location.add(0, -0.2, 0), EntityType.ARMOR_STAND);
                    armorStand.setVisible(false);
                    armorStand.setGravity(false);
                    armorStand.setCustomNameVisible(true);
                    armorStand.setCustomName(line2);
                    break;
                case 3:
                    line1 = (String) CratesPlus.holograms.get(0);
                    line1 = line1.replaceAll("%crate%", crateType);
                    line2 = (String) CratesPlus.holograms.get(1);
                    line2 = line2.replaceAll("%crate%", crateType);
                    line3 = (String) CratesPlus.holograms.get(2);
                    line3 = line3.replaceAll("%crate%", crateType);

                    armorStand = (ArmorStand) location.getWorld().spawnEntity(location.add(0, 0.4, 0), EntityType.ARMOR_STAND);
                    armorStand.setVisible(false);
                    armorStand.setGravity(false);
                    armorStand.setCustomNameVisible(true);
                    armorStand.setCustomName(line1);

                    armorStand = (ArmorStand) location.getWorld().spawnEntity(location.add(0, -0.2, 0), EntityType.ARMOR_STAND);
                    armorStand.setVisible(false);
                    armorStand.setGravity(false);
                    armorStand.setCustomNameVisible(true);
                    armorStand.setCustomName(line2);

                    armorStand = (ArmorStand) location.getWorld().spawnEntity(location.add(0, -0.2, 0), EntityType.ARMOR_STAND);
                    armorStand.setVisible(false);
                    armorStand.setGravity(false);
                    armorStand.setCustomNameVisible(true);
                    armorStand.setCustomName(line3);
                    break;
                default:
                    line1 = (String) CratesPlus.holograms.get(0);
                    line1 = line1.replaceAll("%crate%", crateType);
                    line2 = (String) CratesPlus.holograms.get(1);
                    line2 = line2.replaceAll("%crate%", crateType);
                    line3 = (String) CratesPlus.holograms.get(2);
                    line3 = line3.replaceAll("%crate%", crateType);
                    line4 = (String) CratesPlus.holograms.get(3);
                    line4 = line4.replaceAll("%crate%", crateType);

                    armorStand = (ArmorStand) location.getWorld().spawnEntity(location.add(0, 0.6, 0), EntityType.ARMOR_STAND);
                    armorStand.setVisible(false);
                    armorStand.setGravity(false);
                    armorStand.setCustomNameVisible(true);
                    armorStand.setCustomName(line1);

                    armorStand = (ArmorStand) location.getWorld().spawnEntity(location.add(0, -0.2, 0), EntityType.ARMOR_STAND);
                    armorStand.setVisible(false);
                    armorStand.setGravity(false);
                    armorStand.setCustomNameVisible(true);
                    armorStand.setCustomName(line2);

                    armorStand = (ArmorStand) location.getWorld().spawnEntity(location.add(0, -0.2, 0), EntityType.ARMOR_STAND);
                    armorStand.setVisible(false);
                    armorStand.setGravity(false);
                    armorStand.setCustomNameVisible(true);
                    armorStand.setCustomName(line3);

                    armorStand = (ArmorStand) location.getWorld().spawnEntity(location.add(0, -0.2, 0), EntityType.ARMOR_STAND);
                    armorStand.setVisible(false);
                    armorStand.setGravity(false);
                    armorStand.setCustomNameVisible(true);
                    armorStand.setCustomName(line4);
                    break;
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().getState() instanceof Chest) {
            Chest chest = (Chest) event.getBlock().getState();
            if (chest.getInventory().getTitle() != null && chest.getInventory().getTitle().contains("Crate!")) {
                Location location = chest.getLocation();

                if (event.getPlayer().isSneaking() && (CratesPlus.getPlugin().getConfig().getBoolean("Crate Protection") && !event.getPlayer().hasPermission("cratesplus.admin"))) {
                    event.getPlayer().sendMessage(CratesPlus.pluginPrefix + ChatColor.RED + "You do not have permission to remove this crate");
                    event.setCancelled(true);
                    return;
                } else if (!event.getPlayer().isSneaking()) {
                    event.getPlayer().sendMessage(CratesPlus.pluginPrefix + ChatColor.RED + "Sneak to break crates");
                    event.setCancelled(true);
                    return;
                }
                for (Entity entity : location.getWorld().getEntities()) {
                    if (entity.isDead() || entity.getType() != EntityType.ARMOR_STAND) continue;
                    String name = chest.getInventory().getTitle().replace(" Crate!", "");
                    if (name != null && entity.getLocation().getBlockX() == chest.getX() && entity.getLocation().getBlockZ() == chest.getZ()) {
                        entity.remove();
                    }
                }
            }
        }
    }

}
