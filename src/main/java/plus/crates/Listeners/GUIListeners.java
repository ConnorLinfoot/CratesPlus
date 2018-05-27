package plus.crates.Listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import plus.crates.Utils.GUI;

public class GUIListeners implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (GUI.guis.containsKey(event.getWhoClicked().getUniqueId())) {
            event.setCancelled(true);
            if (event.getCurrentItem() == null || event.getRawSlot() != event.getSlot() || event.getCurrentItem().getType().equals(Material.AIR))
                return;
            GUI gui = GUI.guis.get(event.getWhoClicked().getUniqueId());
            gui.handleClick((Player) event.getWhoClicked(), event.getSlot());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryClose(InventoryCloseEvent event) {
        if (GUI.ignoreClosing.contains(event.getPlayer().getUniqueId())) {
            GUI.ignoreClosing.remove(event.getPlayer().getUniqueId());
            return;
        }
        if (GUI.guis.containsKey(event.getPlayer().getUniqueId()))
            GUI.guis.remove(event.getPlayer().getUniqueId());
        if (GUI.pageTracker.containsKey(event.getPlayer().getUniqueId()))
            GUI.pageTracker.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (GUI.ignoreClosing.contains(event.getPlayer().getUniqueId()))
            GUI.ignoreClosing.remove(event.getPlayer().getUniqueId());
        if (GUI.guis.containsKey(event.getPlayer().getUniqueId()))
            GUI.guis.remove(event.getPlayer().getUniqueId());
        if (GUI.pageTracker.containsKey(event.getPlayer().getUniqueId()))
            GUI.pageTracker.remove(event.getPlayer().getUniqueId());
    }

}
