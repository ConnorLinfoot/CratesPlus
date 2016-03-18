package plus.crates.Events;

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
import plus.crates.Crate;
import plus.crates.CratesPlus;
import plus.crates.Handlers.CrateHandler;
import plus.crates.Handlers.MessageHandler;
import plus.crates.Winning;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CrateOpenEvent extends Event {
	private Player player;
	private String crateName;
	private Crate crate;
	private boolean canceled = false;
	private Inventory winGUI;
	private BukkitTask task;
	private Integer timer = 0;
	private Integer currentItem = 0;
	private Winning winning = null;

	public CrateOpenEvent(Player player, String crateName) {
		this.player = player;
		this.crateName = crateName;
		this.crate = CratesPlus.getConfigHandler().getCrates().get(crateName.toLowerCase());
	}

	public void doEvent() {
		if (CratesPlus.getConfigHandler().isDoGui()) {
			doBasicGUI();
		} else {
			if (crate.getTotalPercentage() > 0) {
				double maxPercentage = 0.0D;
				for (Winning winning1 : crate.getWinnings()) {
					if (winning1.getPercentage() > maxPercentage)
						maxPercentage = winning1.getPercentage();
				}

				double randDouble = CrateHandler.randDouble(0, maxPercentage);
				ArrayList<Winning> winnings = new ArrayList<Winning>();
				for (Winning winning1 : crate.getWinnings()) {
					if (randDouble < winning1.getPercentage())
						winnings.add(winning1);
				}

				if (winnings.size() > 1) {
					winning = winnings.get(CrateHandler.randInt(0, winnings.size() - 1));
				} else {
					winning = winnings.get(0);
				}
			} else {
				winning = crate.getWinnings().get(CrateHandler.randInt(0, crate.getWinnings().size() - 1));
			}
			winning.runWin(getPlayer());

			/** Do broadcast */
			if (crate.isBroadcast()) {
				Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "-------------------------------------------------");
				Bukkit.broadcastMessage(CratesPlus.getPluginPrefix() + MessageHandler.getMessage(CratesPlus.getPlugin(), "Broadcast", player, crate, winning));
				Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "-------------------------------------------------");
			}

			/** Spawn firework */
			if (crate.isFirework()) {
				CrateHandler.spawnFirework(player.getLocation());
			}
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

	private void doBasicGUI() {
		Random random = new Random();
		int max = crate.getWinnings().size() - 1;
		int min = 0;
		currentItem = random.nextInt((max - min) + 1) + min; // Oh look, it's actually a random win now... xD
		winGUI = Bukkit.createInventory(null, 45, CratesPlus.getConfigHandler().getCrates().get(crateName.toLowerCase()).getColor() + crateName + " Win");
		CrateHandler.addOpening(player.getUniqueId(), winGUI);
		player.openInventory(winGUI);
		int maxTime = CratesPlus.getConfigHandler().getCrateGUITime();
		final int maxTimeTicks = maxTime * 10;
		task = Bukkit.getScheduler().runTaskTimerAsynchronously(CratesPlus.getPlugin(), new Runnable() {
			public void run() {
				if (!player.isOnline()) { // TODO, Try and handle DC for players?
					task.cancel();
					return;
				}
				Integer i = 0;
				while (i < 45) {
					if (i == 22) {
						i++;
						if (crate.getWinnings().size() == currentItem)
							currentItem = 0;
						final Winning winning;
						if (timer == maxTimeTicks) {
							if (crate.getTotalPercentage() > 0) {

								double maxPercentage = 0.0D;
								for (Winning winning1 : crate.getWinnings()) {
									if (winning1.getPercentage() > maxPercentage)
										maxPercentage = winning1.getPercentage();
								}

								double randDouble = CrateHandler.randDouble(0, maxPercentage);
								ArrayList<Winning> winnings = new ArrayList<Winning>();
								for (Winning winning1 : crate.getWinnings()) {
									if (randDouble < winning1.getPercentage())
										winnings.add(winning1);
								}

								if (winnings.size() > 1) {
									winning = winnings.get(CrateHandler.randInt(0, winnings.size() - 1));
								} else {
									winning = winnings.get(0);
								}
							} else {
								winning = crate.getWinnings().get(CrateHandler.randInt(0, crate.getWinnings().size() - 1));
							}
						} else {
							winning = crate.getWinnings().get(currentItem);
						}

						final ItemStack currentItemStack = winning.getPreviewItemStack();
						if (timer == maxTimeTicks) {

							Bukkit.getScheduler().runTask(CratesPlus.getPlugin(), new Runnable() {
								@Override
								public void run() {
									winning.runWin(player);
									/** Do broadcast */
									if (crate.isBroadcast()) {
										Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "-------------------------------------------------");
										Bukkit.broadcastMessage(CratesPlus.getPluginPrefix() + MessageHandler.getMessage(CratesPlus.getPlugin(), "Broadcast", player, crate, winning));
										Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "-------------------------------------------------");
									}

									/** Spawn firework */
									if (crate.isFirework()) {
										CrateHandler.spawnFirework(player.getLocation());
									}
								}
							});
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
						Sound sound;
						try {
							sound = Sound.valueOf("NOTE_PIANO");
						} catch (Exception e) {
							try {
								sound = Sound.valueOf("BLOCK_NOTE_HARP");
							} catch (Exception ee) {
								return; // This should never happen!
							}
						}
						final Sound finalSound = sound;
						Bukkit.getScheduler().runTask(CratesPlus.getPlugin(), new Runnable() {
							@Override
							public void run() {
								if (player.getOpenInventory().getTitle() != null && player.getOpenInventory().getTitle().contains(" Win"))
									player.playSound(player.getLocation(), finalSound, (float) 0.2, 2);
							}
						});
						itemMeta.setDisplayName(ChatColor.RESET + "Rolling...");
					}
					itemStack.setItemMeta(itemMeta);
					winGUI.setItem(i, itemStack);
					i++;
				}
				if (timer == maxTimeTicks) {
					CrateHandler.removeOpening(player.getUniqueId());
					task.cancel();
					return;
				}
				timer++;
			}
		}, 0L, 2L);


	}

}