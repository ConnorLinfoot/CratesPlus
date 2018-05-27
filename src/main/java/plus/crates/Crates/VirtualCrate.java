package plus.crates.Crates;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import plus.crates.Handlers.ConfigHandler;
import plus.crates.Utils.GUI;
import plus.crates.Utils.LinfootUtil;

import java.util.ArrayList;

public class VirtualCrate extends Crate {
    private ArrayList<String> crates = new ArrayList<>();

    public VirtualCrate(ConfigHandler configHandler, String name) {
        super(configHandler, name);
        loadCrate();
    }

    protected void loadCrate() {
        if (getCratesPlus().getConfig().isSet("Crates." + name + ".Crates")) {
            for (String key : getCratesPlus().getConfig().getStringList("Crates." + name + ".Crates")) {
                Crate crate = getConfigHandler().getCrate(key.toLowerCase());
                if (crate == null) {
                    getCratesPlus().getLogger().warning("Failed to find crate \"" + key + "\"");
//                } else if (!(crate instanceof KeyCrate || crate instanceof MysteryCrate)) {
                } else if (!(crate instanceof KeyCrate)) { // KeyCrate support only for now
                    getCratesPlus().getLogger().warning("Crate \"" + key + "\" is not supported by Virtual Crates");
                } else {
                    crates.add(key.toLowerCase());
                }
            }
        } else {
            getCratesPlus().getLogger().warning("No crates have been defined for \"" + getName() + "\"");
        }
    }

    @Override
    public boolean give(OfflinePlayer offlinePlayer, Integer amount) {
        return false;
    }

    public void openGUI(Player player) {
        GUI gui = new GUI(getName());

        for (String crateName : crates) {
            Crate crate = getCratesPlus().getConfigHandler().getCrate(crateName);
            if (crate == null)
                continue;
            gui.addItem(LinfootUtil.buildItemStack(new ItemStack(crate.getBlock(), 1, (short) getBlockData()), crate.getName(true), null), new GUI.ClickHandler() {
                @Override
                public void doClick(Player player, GUI gui) {
                    // TODO
                    player.sendMessage(ChatColor.YELLOW + "// TODO");
                }
            });
        }

        gui.open(player);
    }

}
