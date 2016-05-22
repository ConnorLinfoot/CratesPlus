package plus.crates.Handlers;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import plus.crates.Crate;
import plus.crates.CratesPlus;
import plus.crates.Winning;

public class MessageHandler {

	public static String getMessage(String messageName, Player player, Crate crate, Winning winning) {
		if (!CratesPlus.messagesConfig.isSet(messageName))
			return null;
		String message = CratesPlus.messagesConfig.getString(messageName);
		message = doPlaceholders(message, player, crate, winning);
		message = ChatColor.translateAlternateColorCodes('&', message);
		return message;
	}

	public static String doPlaceholders(String message, Player player, Crate crate, Winning winning) {
		message = ChatColor.translateAlternateColorCodes('&', message);
		if (player != null)
			message = message.replaceAll("%name%", player.getName()).replaceAll("%displayname%", player.getDisplayName()).replaceAll("%uuid%", player.getUniqueId().toString());
		if (crate != null)
			message = message.replaceAll("%crate%", crate.getName(true) + ChatColor.RESET);
		if (winning != null)
			message = message.replaceAll("%prize%", winning.getWinningItemStack().getItemMeta().getDisplayName() + ChatColor.RESET);
		if (winning != null)
			message = message.replaceAll("%winning%", winning.getWinningItemStack().getItemMeta().getDisplayName() + ChatColor.RESET).replaceAll("%percentage%", String.valueOf(winning.getPercentage()));
		return message;
	}

}
