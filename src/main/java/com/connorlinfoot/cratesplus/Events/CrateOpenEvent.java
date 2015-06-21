package com.connorlinfoot.cratesplus.Events;

import com.connorlinfoot.cratesplus.Crate;
import com.connorlinfoot.cratesplus.CratesPlus;
import com.connorlinfoot.cratesplus.Handlers.CrateHandler;
import com.connorlinfoot.cratesplus.Handlers.MessageHandler;
import com.connorlinfoot.cratesplus.Winning;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
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
    private Winning winning = null;

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
        doBetaGUI();
    }

    private Winning getValidWin(List<Winning> winnings) {
        return winnings.get(CrateHandler.randInt(0, winnings.size() - 1));
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

        winGUI = Bukkit.createInventory(null, 45, CratesPlus.crates.get(crateType.toLowerCase()).getColor() + crateType + " Win");
        player.openInventory(winGUI);
        task = Bukkit.getScheduler().runTaskTimerAsynchronously(CratesPlus.getPlugin(), new Runnable() {
            public void run() {
                if (player.getOpenInventory().getTitle() == null || !player.getOpenInventory().getTitle().contains(" Win")) {
                    player.openInventory(winGUI);
                }
                Integer i = 0;
                while (i < 45) {
                    if (i == 22) {
                        i++;
                        if (crate.getWinnings().size() == currentItem)
                            currentItem = 0;
                        Winning winning = crate.getWinnings().get(currentItem);

                        ItemStack currentItemStack = winning.getItemStack();
                        if (timer == 100)
                            winning.runWin();
                        if (currentItemStack != null) {
                            winGUI.setItem(22, currentItemStack);
                        }

                        currentItem++;
                        continue;
                    }
                    ItemStack itemStack = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) CrateHandler.randInt(0, 15));
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    if (timer == 100) {
                        itemMeta.setDisplayName(ChatColor.RESET + "Winner!");
                    } else {
                        player.playSound(player.getLocation(), Sound.NOTE_PIANO, (float) 0.2, 2);
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
        }, 0L, 3L);


    }

}