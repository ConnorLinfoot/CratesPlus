package plus.crates.Opener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;
import plus.crates.CratesPlus;
import plus.crates.Winning;

import java.util.Random;

public class BasicGUIOpener extends Opener {
	private CratesPlus cratesPlus;
	private Inventory winGUI;
	private BukkitTask task;
	private Integer timer = 0;
	private Integer currentItem = 0;

	public BasicGUIOpener(CratesPlus cratesPlus) {
		super(cratesPlus, "BasicGUI");
		this.cratesPlus = cratesPlus;
	}

	@Override
	public void doSetup() {

	}

	@Override
	public void doOpen() {
		Random random = new Random();
		int max = crate.getWinnings().size() - 1;
		int min = 0;
		currentItem = random.nextInt((max - min) + 1) + min; // Oh look, it's actually a random win now... xD
		winGUI = Bukkit.createInventory(null, 45, crate.getColor() + crate.getName() + " Win");
		player.openInventory(winGUI);
		int maxTime = cratesPlus.getConfigHandler().getCrateGUITime();
		final int maxTimeTicks = maxTime * 10;
		task = Bukkit.getScheduler().runTaskTimerAsynchronously(cratesPlus, new Runnable() {
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
							winning = getWinning();
						} else {
							winning = crate.getWinnings().get(currentItem);
						}

						final ItemStack currentItemStack = winning.getPreviewItemStack();
						if (timer == maxTimeTicks) {
							winning.runWin(player);
						}
						winGUI.setItem(22, currentItemStack);

						currentItem++;
						continue;
					}
					ItemStack itemStack = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) cratesPlus.getCrateHandler().randInt(0, 15));
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
						Bukkit.getScheduler().runTask(cratesPlus, new Runnable() {
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
					finish();
					task.cancel();
					return;
				}
				timer++;
			}
		}, 0L, 2L);
	}

	@Override
	public void doReopen() {
		getPlayer().openInventory(winGUI);
	}

}
