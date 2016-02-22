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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class CrateOpenEvent extends Event {
    private Player player;
    private String crateName;
    private Crate crate;
    private boolean canceled = false;
    private int tries = 0;
    private Inventory winGUI;
    private BukkitTask task;
    private Integer timer = 0;
    private Integer currentItem = 0;
    private Winning winning = null;

    public CrateOpenEvent(Player player, String crateName) {
        this.player = player;
        this.crateName = crateName;
        this.crate = CratesPlus.crates.get(crateName.toLowerCase());
    }

    public void doEvent() {
        /** Spawn firework */
        if (crate.isFirework()) {
            CrateHandler.spawnFirework(player.getLocation());
        }

        if (CratesPlus.doGui) {
            doBetaGUI();
        } else {
            if (crate.getTotalPercentage() > 0) {
                int id = crate.getPercentages().get(CrateHandler.randInt(0, crate.getPercentages().size() - 1));
                winning = crate.getWinnings().get(id);
            } else {
                winning = crate.getWinnings().get(CrateHandler.randInt(0, crate.getWinnings().size() - 1));
            }
            if (!winning.isCommand()) {
                HashMap<Integer, ItemStack> left = getPlayer().getInventory().addItem(winning.getPreviewItemStack());
                for (Map.Entry<Integer, ItemStack> item : left.entrySet()) {
                    getPlayer().getLocation().getWorld().dropItemNaturally(getPlayer().getLocation(), item.getValue());
                }
            }
            winning.runWin(getPlayer());
        }
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

    public void setCrateName(String crateName) {
        this.crateName = crateName;
    }

    public String getCrateName() {
        return this.crateName;
    }

    public Crate getCrate() {
        return this.crate;
    }

    private void doBetaGUI() {
        /** Time for some cool GUI's, hopefully */
        Random random = new Random();
        int max = crate.getWinnings().size() - 1;
        int min = 0;
        currentItem = random.nextInt((max - min) + 1) + min; // Oh look, it's actually a random win now... xD
        winGUI = Bukkit.createInventory(null, 45, CratesPlus.crates.get(crateName.toLowerCase()).getColor() + crateName + " Win");
        player.openInventory(winGUI);
        int maxTime = CratesPlus.crateGUITime;
        final int maxTimeTicks = maxTime * 10;
        task = Bukkit.getScheduler().runTaskTimerAsynchronously(CratesPlus.getPlugin(), new Runnable() {
            public void run() {
                if (player.getOpenInventory().getTitle() == null || !player.getOpenInventory().getTitle().contains(" Win"))
                    player.openInventory(winGUI);
                Integer i = 0;
                while (i < 45) {
                    if (i == 22) {
                        i++;
                        if (crate.getWinnings().size() == currentItem)
                            currentItem = 0;
                        Winning winning;
                        if (timer == maxTimeTicks) {
                            if (crate.getTotalPercentage() > 0) {
                                int id = crate.getPercentages().get(CrateHandler.randInt(0, crate.getPercentages().size() - 1));
                                winning = crate.getWinnings().get(id);
                            } else {
                                winning = crate.getWinnings().get(CrateHandler.randInt(0, crate.getWinnings().size() - 1));
                            }
                        } else {
                            winning = crate.getWinnings().get(currentItem);
                        }

                        ItemStack currentItemStack = winning.getPreviewItemStack();
                        if (timer == maxTimeTicks) {
                            winning.runWin(player);
                            /** Do broadcast */
                            if (crate.isBroadcast()) {
                                Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "-------------------------------------------------");
                                Bukkit.broadcastMessage(CratesPlus.pluginPrefix + MessageHandler.getMessage(CratesPlus.getPlugin(), "Broadcast", player, crate, winning));
                                Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "-------------------------------------------------");
                            }
                        }
                        winGUI.setItem(22, currentItemStack);

                        currentItem++;
                        continue;
                    }
                    ItemStack itemStack = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) CrateHandler.randInt(0, 15));
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    if (timer == maxTimeTicks) {
                        itemMeta.setDisplayName(ChatColor.RESET + "Winner!");
                    } else {
                        player.playSound(player.getLocation(), Sound.NOTE_PIANO, (float) 0.2, 2);
                        itemMeta.setDisplayName(ChatColor.RESET + "Rolling...");
                    }
                    itemStack.setItemMeta(itemMeta);
                    winGUI.setItem(i, itemStack);
                    i++;
                }
                if (timer == maxTimeTicks) {
                    task.cancel();
                    return;
                }
                timer++;
            }
        }, 0L, 2L);


    }

}