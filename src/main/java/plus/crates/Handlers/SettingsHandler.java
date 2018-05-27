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
            material = Material.REDSTONE_TORCH_ON;
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
        itemMeta.setDisplayName(ChatColor.WHITE + "Rename Crate");
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
                    Constructor signConstructor = ReflectionUtil.getNMSClass("PacketPlayOutOpenSignEditor").getConstructor(ReflectionUtil.getNMSClass("BlockPosition"));
                    Object packet = signConstructor.newInstance(ReflectionUtil.getBlockPosition(player));
                    SignInputHandler.injectNetty(player);
                    ReflectionUtil.sendPacket(player, packet);
                } catch (Exception e) {
                    player.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.RED + "Please use /crate rename <old> <new>");
                    renaming.remove(player.getUniqueId());
                }
            }
        });


        // Edit Crate Winnings

        itemStack = new ItemStack(Material.DIAMOND);
        itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.WHITE + "Edit Crate Winnings");
        lore = new ArrayList<>();
        lore.add("");
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        gui.setItem(2, itemStack, new GUI.ClickHandler() {
            @Override
            public void doClick(Player player, GUI gui) {
                GUI.ignoreClosing.add(player.getUniqueId());
                openCrateWinnings(player, crateName);
            }
        });


        // Edit Crate Color

        itemStack = new ItemStack(Material.WOOL, 1, (short) 3);
        itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.WHITE + "Edit Crate Color");
        lore = new ArrayList<>();
        lore.add("");
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        gui.setItem(4, itemStack);


        // Delete Crate

        Material material;

        try {
            material = Material.valueOf("BARRIER");
        } catch (Exception i) {
            material = Material.REDSTONE_TORCH_ON;
        }

        itemStack = new ItemStack(material);
        itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.WHITE + "Delete Crate");
        lore = new ArrayList<>();
        lore.add("");
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        gui.setItem(6, itemStack);

        Bukkit.getScheduler().runTaskLater(cratesPlus, () -> gui.open(player), 1L);

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
