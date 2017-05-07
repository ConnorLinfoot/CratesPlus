package plus.crates.Handlers;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import plus.crates.Crates.Crate;
import plus.crates.CratesPlus;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MessageHandler {
	private CratesPlus cratesPlus;

	public MessageHandler(CratesPlus cratesPlus) {
		this.cratesPlus = cratesPlus;
	}

	public String getMessage(String messageName, Player player, Crate crate, plus.crates.Crates.Winning winning) {
		if (!cratesPlus.getMessagesConfig().isSet(messageName))
			return "Message \"" + messageName + "\" not configured";
		String message = cratesPlus.getMessagesConfig().getString(messageName);
		message = doPlaceholders(message, player, crate, winning);
		message = ChatColor.translateAlternateColorCodes('&', message);
		if (isAprilFools()) {
			message = ChatColor.LIGHT_PURPLE + ChatColor.stripColor(message);
		}
		return message;
	}

	public boolean getMessageBool(String messageName) {
		return cratesPlus.getMessagesConfig().getBoolean(messageName, false);
	}

	public String doPlaceholders(String message, Player player, Crate crate, plus.crates.Crates.Winning winning) {
		message = ChatColor.translateAlternateColorCodes('&', message);
		if (player != null)
			message = message.replaceAll("%name%", player.getName()).replaceAll("%displayname%", player.getDisplayName()).replaceAll("%uuid%", player.getUniqueId().toString());
		if (crate != null)
			message = message.replaceAll("%crate%", crate.getName(true) + ChatColor.RESET);
		if (winning != null)
			message = message.replaceAll("%prize%", winning.getWinningItemStack().getItemMeta().getDisplayName() + ChatColor.RESET).replaceAll("%winning%", winning.getWinningItemStack().getItemMeta().getDisplayName() + ChatColor.RESET).replaceAll("%percentage%", String.valueOf(winning.getPercentage()));
		return message;
	}

	public boolean isAprilFools() {
		DateFormat df = new SimpleDateFormat("dd/MM");
		Date dateobj = new Date();
		return df.format(dateobj).equals("01/04");
	}

}
