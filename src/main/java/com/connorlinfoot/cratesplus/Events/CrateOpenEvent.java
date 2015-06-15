package com.connorlinfoot.cratesplus.Events;

import com.connorlinfoot.cratesplus.Crate;
import com.connorlinfoot.cratesplus.CratesPlus;
import com.connorlinfoot.cratesplus.Handlers.CrateHandler;
import com.connorlinfoot.cratesplus.Handlers.MessageHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;

public class CrateOpenEvent extends Event {
    private Player player;
    private String crateType;
    private Crate crate;
    private boolean canceled = false;
    private int tries = 0;
    private Inventory winGUI;
    private BukkitTask task;
    private Integer timer = 0;
    private Integer currentItem = 0;

    public CrateOpenEvent(Player player, String crateType) {
        this.player = player;
        this.crateType = crateType;
        this.crate = CratesPlus.crates.get(crateType.toLowerCase());
    }

    public void doEvent() {
        /** Spawn firework */
        if (crate.isFirework()) {
            CrateHandler.spawnFirework(player.getLocation());
        }

        /** Do broadcast */
        if (crate.isBroadcast()) {
            Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "-------------------------------------------------");
            Bukkit.broadcastMessage(CratesPlus.pluginPrefix + MessageHandler.getMessage(CratesPlus.getPlugin(), "Broadcast", player, crateType));
            Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "-------------------------------------------------");
        }

        if (CratesPlus.betaGUI) {
            doBetaGUI();
        } else {
            List<String> items = crate.getItems();

            Inventory inventory = Bukkit.createInventory(null, 27, CratesPlus.crates.get(crateType.toLowerCase()).getColor() + crateType + " Win");

            Integer ii = 0;
            while (ii < 10) {
                inventory.setItem(ii, new ItemStack(Material.STAINED_GLASS_PANE));
                ii++;
            }

            ii = 17;
            while (ii < 27) {
                inventory.setItem(ii, new ItemStack(Material.STAINED_GLASS_PANE));
                ii++;
            }

            ItemStack win = getValidWin(items);
            if (win == null) {
                player.sendMessage(ChatColor.RED + "No valid win was found");
                return;
            }
            inventory.setItem(13, win);
            player.openInventory(inventory);
        }
    }

    private ItemStack getValidWin(List<String> items) {
        if (tries == 5)
            return null;
        String i = items.get(CrateHandler.randInt(0, items.size() - 1));
        ItemStack win = CrateHandler.stringToItemstack(i, player, true);
        if (win == null) {
            win = getValidWin(items);
            tries++;
        }
        return win;
    }

    @Override
    public HandlerList getHandlers() {
        return null;
    }

    public boolean isCanceled() {
        return this.canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setCrateType(String crateType) {
        this.crateType = crateType;
    }

    public String getCrateType() {
        return this.crateType;
    }

    public Crate getCrate() {
        return this.crate;
    }

    private void doBetaGUI() {
        /** Time for some cool GUI's, hopefully */

        winGUI = Bukkit.createInventory(null, 27, CratesPlus.crates.get(crateType.toLowerCase()).getColor() + crateType + " Win");
        player.openInventory(winGUI);
        task = Bukkit.getScheduler().runTaskTimerAsynchronously(CratesPlus.getPlugin(), new Runnable() {
            public void run() {
                if (player.getOpenInventory().getTitle() == null || !player.getOpenInventory().getTitle().contains(" Win")) {
                    player.openInventory(winGUI);
                }
                Integer i = 0;
                while (i < 27) {
                    if (i == 13) {
                        i++;
                        if (crate.getItems().size() == currentItem)
                            currentItem = 0;

                        ItemStack currentItemStack = CrateHandler.stringToItemstack(crate.getItems().get(currentItem), player, timer == 100);
                        if (currentItemStack != null) {
                            winGUI.setItem(13, currentItemStack);
                        }

                        currentItem++;
                        continue;
                    }
                    ItemStack itemStack = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) CrateHandler.randInt(0, 15));
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    if (timer == 100) {
                        itemMeta.setDisplayName(ChatColor.RESET + "Winner!");
                    } else {
                        itemMeta.setDisplayName(ChatColor.RESET + "Rolling...");
                    }
                    itemStack.setItemMeta(itemMeta);
                    winGUI.setItem(i, itemStack);
                    i++;
                }
                if (timer == 100) {
                    task.cancel();
                    return;
                }
                timer = timer + 2;
            }
        }, 0L, 2L);


    }

}