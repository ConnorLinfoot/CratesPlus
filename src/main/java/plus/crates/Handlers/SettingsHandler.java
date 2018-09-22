package plus.crates.Handlers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import plus.crates.Crates.Crate;
import plus.crates.Crates.Winning;
import plus.crates.CratesPlus;
import plus.crates.Events.PlayerInputEvent;
import plus.crates.Utils.GUI;
import plus.crates.Utils.LegacyMaterial;
import plus.crates.Utils.ReflectionUtil;
import plus.crates.Utils.SignInputHandler;

import java.lang.reflect.Constructor;
import java.util.*;

public class SettingsHandler implements Listener {
    private HashMap<UUID, String> renaming = new HashMap<>();
    private CratesPlus cratesPlus;
    private GUI settings;
    private GUI crates;
    private HashMap<String, String> lastCrateEditing = new HashMap<>();

    public SettingsHandler(CratesPlus cratesPlus) {
        this.cratesPlus = cratesPlus;
        Bukkit.getPluginManager().registerEvents(this, cratesPlus);
        setupSettingsInventory();
        setupCratesInventory();
    }

    public void setupSettingsInventory() {
        settings = new GUI("CratesPlus Settings");

        ItemStack itemStack;
        ItemMeta itemMeta;
        List<String> lore;

        itemStack = new ItemStack(Material.CHEST);
        itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GREEN + "Edit Crates");
        lore = new ArrayList<>();
        lore.add("");
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        settings.setItem(1, itemStack, new GUI.ClickHandler() {
            @Override
            public void doClick(Player player, GUI gui) {
                GUI.ignoreClosing.add(player.getUniqueId());
                openCrates(player);
            }
        });

        Material material;
        try {
            material = Material.valueOf("BARRIER");
        } catch (Exception i) {
            material = LegacyMaterial.REDSTONE_TORCH_ON.getMaterial();
        }

        itemStack = new ItemStack(material);
        itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GREEN + "Reload Config");
        lore = new ArrayList<>();
        lore.add("");
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        settings.setItem(5, itemStack, new GUI.ClickHandler() {
            @Override
            public void doClick(Player player, GUI gui) {
                player.closeInventory();
                cratesPlus.reloadConfig();
                player.sendMessage(ChatColor.GREEN + "Reloaded config");
            }
        });
    }

    public void setupCratesInventory() {
        crates = new GUI("Crates");

        ItemStack itemStack;
        ItemMeta itemMeta;

        for (Map.Entry<String, Crate> entry : cratesPlus.getConfigHandler().getCrates().entrySet()) {
            Crate crate = entry.getValue();

            itemStack = new ItemStack(Material.CHEST);
            itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(crate.getName(true));
            itemStack.setItemMeta(itemMeta);
            final String crateName = crate.getName();
            crates.addItem(itemStack, new GUI.ClickHandler() {
                @Override
                public void doClick(Player player, GUI gui) {
                    GUI.ignoreClosing.add(player.getUniqueId());
                    openCrate(player, crateName);
                }
            });
        }
    }

    public void openSettings(final Player player) {
        Bukkit.getScheduler().runTaskLater(cratesPlus, () -> settings.open(player), 1L);
    }

    public void openCrates(final Player player) {
        Bukkit.getScheduler().runTaskLater(cratesPlus, () -> crates.open(player), 1L);
    }

    public void openCrateWinnings(final Player player, String crateName) {
        Crate crate = cratesPlus.getConfigHandler().getCrates().get(crateName.toLowerCase());
        if (crate == null) {
            player.sendMessage(ChatColor.RED + "Unable to find " + crateName + " crate");
            return;
        }

        if (crate.containsCommandItem()) {
            player.sendMessage(ChatColor.RED + "You can not currently edit a crate in the GUI which has command items");
            player.closeInventory();
            return;
        }

        final GUI gui = new GUI("Edit " + crate.getName(false) + " Crate Winnings");

        for (Winning winning : crate.getWinnings()) {
            gui.addItem(winning.getWinningItemStack());
        }

        Bukkit.getScheduler().runTaskLater(cratesPlus, () -> gui.open(player), 1L);

    }

    public void openCrate(final Player player, final String crateName) {
        Crate crate = cratesPlus.getConfigHandler().getCrates().get(crateName.toLowerCase());
        if (crate == null) {
            return; // TODO Error handling here
        }

        final GUI gui = new GUI("Edit " + crate.getName(false) + " Crate");

        ItemStack itemStack;
        ItemMeta itemMeta;
        List<String> lore;


        // Rename Crate

        itemStack = new ItemStack(Material.NAME_TAG);
        itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GREEN + "Rename Crate");
        lore = new ArrayList<>();
        lore.add("");
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        gui.setItem(0, itemStack, new GUI.ClickHandler() {
            @Override
            public void doClick(Player player, GUI gui) {
                player.closeInventory();
                renaming.put(player.getUniqueId(), crateName);
                try {
                    //Send fake sign cause 1.13
                    player.sendBlockChange(player.getLocation(), Material.SIGN, (byte) 0);

                    Constructor signConstructor = ReflectionUtil.getNMSClass("PacketPlayOutOpenSignEditor").getConstructor(ReflectionUtil.getNMSClass("BlockPosition"));
                    Object packet = signConstructor.newInstance(ReflectionUtil.getBlockPosition(player));
                    SignInputHandler.injectNetty(player);
                    ReflectionUtil.sendPacket(player, packet);

                    player.sendBlockChange(player.getLocation(), player.getLocation().getBlock().getType(), player.getLocation().getBlock().getData());
                } catch (Exception e) {
                    player.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.RED + "Please use /crate rename <old> <new>");
                    renaming.remove(player.getUniqueId());
                }
            }
        });


        // Edit Crate Winnings

        itemStack = new ItemStack(Material.DIAMOND);
        itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.RED + "Edit Crate Winnings");
        lore = new ArrayList<>();
        lore.add("");
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        gui.setItem(2, itemStack, new GUI.ClickHandler() {
            @Override
            public void doClick(Player player, GUI gui) {
                player.sendMessage(ChatColor.RED + "This feature is currently disabled!");
//                GUI.ignoreClosing.add(player.getUniqueId());
//                openCrateWinnings(player, crateName);
            }
        });


        // Edit Crate Color

        itemStack = new ItemStack(LegacyMaterial.WOOL.getMaterial(), 1, (short) 3);
        itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GREEN + "Edit Crate Color");
        lore = new ArrayList<>();
        lore.add("");
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        gui.setItem(4, itemStack, new GUI.ClickHandler() {
            @Override
            public void doClick(Player player, GUI gui) {
                GUI.ignoreClosing.add(player.getUniqueId());
                openCrateColor(player, crate);
            }
        });


        // Delete Crate

        Material material;

        try {
            material = Material.valueOf("BARRIER");
        } catch (Exception i) {
            material = LegacyMaterial.REDSTONE_TORCH_ON.getMaterial();
        }

        itemStack = new ItemStack(material);
        itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GREEN + "Delete Crate");
        lore = new ArrayList<>();
        lore.add("");
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        gui.setItem(6, itemStack, new GUI.ClickHandler() {
            @Override
            public void doClick(Player player, GUI gui) {
                GUI.ignoreClosing.add(player.getUniqueId());
                confirmDelete(player, crate);
            }
        });

        Bukkit.getScheduler().runTaskLater(cratesPlus, () -> gui.open(player), 1L);

    }

    private void openCrateColor(final Player player, final Crate crate) {
        GUI gui = new GUI("Edit Crate Color");

        ItemStack aqua = new ItemStack(LegacyMaterial.WOOL.getMaterial(), 1, (short) 3);
        ItemMeta aquaMeta = aqua.getItemMeta();
        aquaMeta.setDisplayName(ChatColor.AQUA + "Aqua");
        aqua.setItemMeta(aquaMeta);
        gui.addItem(aqua, getColorClickHandler(crate, ChatColor.AQUA));

        ItemStack black = new ItemStack(LegacyMaterial.WOOL.getMaterial(), 1, (short) 15);
        ItemMeta blackMeta = black.getItemMeta();
        blackMeta.setDisplayName(ChatColor.BLACK + "Black");
        black.setItemMeta(blackMeta);
        gui.addItem(black, getColorClickHandler(crate, ChatColor.BLACK));

        ItemStack blue = new ItemStack(LegacyMaterial.WOOL.getMaterial(), 1, (short) 9);
        ItemMeta blueMeta = blue.getItemMeta();
        blueMeta.setDisplayName(ChatColor.BLUE + "Blue");
        blue.setItemMeta(blueMeta);
        gui.addItem(blue, getColorClickHandler(crate, ChatColor.BLUE));

        ItemStack darkAqua = new ItemStack(LegacyMaterial.WOOL.getMaterial(), 1, (short) 3);
        ItemMeta darkAquaMeta = darkAqua.getItemMeta();
        darkAquaMeta.setDisplayName(ChatColor.DARK_AQUA + "Dark Aqua");
        darkAqua.setItemMeta(darkAquaMeta);
        gui.addItem(darkAqua, getColorClickHandler(crate, ChatColor.DARK_AQUA));

        ItemStack darkBlue = new ItemStack(LegacyMaterial.WOOL.getMaterial(), 1, (short) 11);
        ItemMeta darkBlueMeta = darkBlue.getItemMeta();
        darkBlueMeta.setDisplayName(ChatColor.DARK_BLUE + "Dark Blue");
        darkBlue.setItemMeta(darkBlueMeta);
        gui.addItem(darkBlue, getColorClickHandler(crate, ChatColor.DARK_BLUE));

        ItemStack darkGray = new ItemStack(LegacyMaterial.WOOL.getMaterial(), 1, (short) 7);
        ItemMeta darkGrayMeta = darkGray.getItemMeta();
        darkGrayMeta.setDisplayName(ChatColor.DARK_GRAY + "Dark Gray");
        darkGray.setItemMeta(darkGrayMeta);
        gui.addItem(darkGray, getColorClickHandler(crate, ChatColor.DARK_GRAY));

        ItemStack darkGreen = new ItemStack(LegacyMaterial.WOOL.getMaterial(), 1, (short) 13);
        ItemMeta darkGreenMeta = darkGreen.getItemMeta();
        darkGreenMeta.setDisplayName(ChatColor.DARK_GREEN + "Dark Green");
        darkGreen.setItemMeta(darkGreenMeta);
        gui.addItem(darkGreen, getColorClickHandler(crate, ChatColor.DARK_GREEN));

        ItemStack darkPurple = new ItemStack(LegacyMaterial.WOOL.getMaterial(), 1, (short) 10);
        ItemMeta darkPurpleMeta = darkPurple.getItemMeta();
        darkPurpleMeta.setDisplayName(ChatColor.DARK_PURPLE + "Dark Purple");
        darkPurple.setItemMeta(darkPurpleMeta);
        gui.addItem(darkPurple, getColorClickHandler(crate, ChatColor.DARK_PURPLE));

        ItemStack darkRed = new ItemStack(LegacyMaterial.WOOL.getMaterial(), 1, (short) 14);
        ItemMeta darkRedMeta = darkRed.getItemMeta();
        darkRedMeta.setDisplayName(ChatColor.DARK_RED + "Dark Red");
        darkRed.setItemMeta(darkRedMeta);
        gui.addItem(darkRed, getColorClickHandler(crate, ChatColor.DARK_RED));

        ItemStack gold = new ItemStack(LegacyMaterial.WOOL.getMaterial(), 1, (short) 1);
        ItemMeta goldMeta = gold.getItemMeta();
        goldMeta.setDisplayName(ChatColor.GOLD + "Gold");
        gold.setItemMeta(goldMeta);
        gui.addItem(gold, getColorClickHandler(crate, ChatColor.GOLD));

        ItemStack gray = new ItemStack(LegacyMaterial.WOOL.getMaterial(), 1, (short) 8);
        ItemMeta grayMeta = gray.getItemMeta();
        grayMeta.setDisplayName(ChatColor.GRAY + "Gray");
        gray.setItemMeta(grayMeta);
        gui.addItem(gray, getColorClickHandler(crate, ChatColor.GRAY));

        ItemStack green = new ItemStack(LegacyMaterial.WOOL.getMaterial(), 1, (short) 5);
        ItemMeta greenMeta = gray.getItemMeta();
        greenMeta.setDisplayName(ChatColor.GREEN + "Green");
        green.setItemMeta(greenMeta);
        gui.addItem(green, getColorClickHandler(crate, ChatColor.GREEN));

        ItemStack lightPurple = new ItemStack(LegacyMaterial.WOOL.getMaterial(), 1, (short) 2);
        ItemMeta lightPurpleMeta = lightPurple.getItemMeta();
        lightPurpleMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Light Purple");
        lightPurple.setItemMeta(lightPurpleMeta);
        gui.addItem(lightPurple, getColorClickHandler(crate, ChatColor.LIGHT_PURPLE));

        ItemStack red = new ItemStack(LegacyMaterial.WOOL.getMaterial(), 1, (short) 14);
        ItemMeta redMeta = red.getItemMeta();
        redMeta.setDisplayName(ChatColor.RED + "Red");
        red.setItemMeta(redMeta);
        gui.addItem(red, getColorClickHandler(crate, ChatColor.RED));

        ItemStack white = new ItemStack(LegacyMaterial.WOOL.getMaterial(), 1, (short) 0);
        ItemMeta whiteMeta = white.getItemMeta();
        whiteMeta.setDisplayName(ChatColor.WHITE + "White");
        white.setItemMeta(whiteMeta);
        gui.addItem(white, getColorClickHandler(crate, ChatColor.WHITE));

        ItemStack yellow = new ItemStack(LegacyMaterial.WOOL.getMaterial(), 1, (short) 4);
        ItemMeta yellowMeta = yellow.getItemMeta();
        yellowMeta.setDisplayName(ChatColor.YELLOW + "Yellow");
        yellow.setItemMeta(yellowMeta);
        gui.addItem(yellow, getColorClickHandler(crate, ChatColor.YELLOW));

        gui.open(player);
    }

    private GUI.ClickHandler getColorClickHandler(Crate crate, ChatColor color) {
        return new GUI.ClickHandler() {
            @Override
            public void doClick(Player player, GUI gui) {
                GUI.ignoreClosing.add(player.getUniqueId());
                crate.setColor(color);
                player.sendMessage(color.name());
                openCrate(player, crate.getName());
            }
        };
    }

    private void confirmDelete(final Player player, final Crate crate) {
        final GUI gui = new GUI("Confirm Delete of \"" + crate.getName(false) + "\"");

        ItemStack crateItem = new ItemStack(crate.getBlock(), 1, (short) crate.getBlockData());
        ItemMeta crateMeta = crateItem.getItemMeta();
        crateMeta.setDisplayName(crate.getName());
        crateItem.setItemMeta(crateMeta);
        gui.setItem(3, crateItem);

        ItemStack cancel = new ItemStack(LegacyMaterial.WOOL.getMaterial(), 1, (short) 14);
        ItemMeta cancelMeta = cancel.getItemMeta();
        cancelMeta.setDisplayName(ChatColor.RED + "Cancel");
        cancel.setItemMeta(cancelMeta);
        gui.setItem(16, cancel, new GUI.ClickHandler() {
            @Override
            public void doClick(Player player, GUI gui) {
                GUI.ignoreClosing.add(player.getUniqueId());
                openCrate(player, crate.getName(false));
            }
        });

        ItemStack confirm = new ItemStack(LegacyMaterial.WOOL.getMaterial(), 1, (short) 5);
        ItemMeta confirmMeta = confirm.getItemMeta();
        confirmMeta.setDisplayName(ChatColor.GREEN + "Confirm");
        confirm.setItemMeta(confirmMeta);
        gui.setItem(18, confirm, new GUI.ClickHandler() {
            @Override
            public void doClick(Player player, GUI gui) {
                player.closeInventory();
                player.sendMessage("WILL DELETE");
            }
        });

        gui.open(player);
    }

    public HashMap<String, String> getLastCrateEditing() {
        return lastCrateEditing;
    }

    @EventHandler
    public void onPlayerInput(final PlayerInputEvent event) {
        if (renaming.containsKey(event.getPlayer().getUniqueId())) {
            String name = renaming.get(event.getPlayer().getUniqueId());
            renaming.remove(event.getPlayer().getUniqueId());
            String newName = "";
            for (String line : event.getLines()) {
                newName += line;
            }
            if (!name.isEmpty() && !newName.isEmpty())
                Bukkit.dispatchCommand(event.getPlayer(), "crate rename " + name + " " + newName);
            cratesPlus.getSettingsHandler().openCrate(event.getPlayer(), newName);
        } else if (cratesPlus.isCreating(event.getPlayer().getUniqueId())) {
            cratesPlus.removeCreating(event.getPlayer().getUniqueId());
            String name = "";
            for (String line : event.getLines()) {
                name += line;
            }
            if (!name.isEmpty()) {
                final String finalName = name;
                Bukkit.getScheduler().runTask(cratesPlus, () -> Bukkit.dispatchCommand(event.getPlayer(), "crate create " + finalName));
            }
        }
    }

}
