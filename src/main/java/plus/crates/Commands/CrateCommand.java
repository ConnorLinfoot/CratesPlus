package plus.crates.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import plus.crates.Crates.Crate;
import plus.crates.Crates.KeyCrate;
import plus.crates.Crates.MysteryCrate;
import plus.crates.CratesPlus;
import plus.crates.Handlers.MessageHandler;
import plus.crates.Opener.Opener;
import plus.crates.Utils.*;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class CrateCommand implements CommandExecutor {
    private final CratesPlus cratesPlus;

    public CrateCommand(CratesPlus cratesPlus) {
        this.cratesPlus = cratesPlus;
    }

    @Override
    public boolean onCommand(final CommandSender sender, Command command, String string, String[] args) {

        if (sender instanceof Player && !sender.hasPermission("cratesplus.admin")) {
            if (args.length == 0 || (args.length > 0 && args[0].equalsIgnoreCase("claim"))) {
                // Assume player and show "claim" GUI
                doClaim((Player) sender);
                return true;
            }
            sender.sendMessage(cratesPlus.getPluginPrefix() + MessageHandler.getMessage("&cYou do not have the correct permission to run this command", (Player) sender, null, null));
            return false;
        }

        if (args.length >= 1) {
            switch (args[0].toLowerCase()) {
                default:
                    sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.RED + "Unknown arg");
                    break;
                case "testmessages":
                    MessageHandler.testMessages = !MessageHandler.testMessages;
                    sender.sendMessage(ChatColor.GREEN + "Test Messages " + (MessageHandler.testMessages ? "ENABLED" : "DISABLED"));
                    break;
                case "testeggs":
                    Player player = null;
                    if (sender instanceof Player)
                        player = (Player) sender;

                    sender.sendMessage(ChatColor.AQUA + "Creating creeper egg...");
                    ItemStack itemStack = cratesPlus.getVersion_util().getSpawnEgg(EntityType.CREEPER, 1);
                    sender.sendMessage(ChatColor.AQUA + "Testing creeper egg...");
                    SpawnEggNBT spawnEggNBT = SpawnEggNBT.fromItemStack(itemStack);
                    if (spawnEggNBT.getSpawnedType().equals(EntityType.CREEPER)) {
                        sender.sendMessage(ChatColor.GREEN + "Creeper egg successful");
                        if (player != null)
                            player.getInventory().addItem(itemStack);
                    } else {
                        sender.sendMessage(ChatColor.RED + "Creeper egg failed, please post console on GitHub");
                    }

                    sender.sendMessage(ChatColor.AQUA + "Creating spider egg...");
                    itemStack = cratesPlus.getVersion_util().getSpawnEgg(EntityType.SPIDER, 2);
                    sender.sendMessage(ChatColor.AQUA + "Testing spider egg...");
                    spawnEggNBT = SpawnEggNBT.fromItemStack(itemStack);
                    if (spawnEggNBT.getSpawnedType().equals(EntityType.SPIDER)) {
                        sender.sendMessage(ChatColor.GREEN + "Spider egg successful");
                        if (player != null)
                            player.getInventory().addItem(itemStack);
                    } else {
                        sender.sendMessage(ChatColor.RED + "Spider egg failed, please post console on GitHub");
                    }

                    sender.sendMessage(ChatColor.AQUA + "Creating silverfish egg...");
                    itemStack = cratesPlus.getVersion_util().getSpawnEgg(EntityType.SILVERFISH, 3);
                    sender.sendMessage(ChatColor.AQUA + "Testing silverfish egg...");
                    spawnEggNBT = SpawnEggNBT.fromItemStack(itemStack);
                    if (spawnEggNBT.getSpawnedType().equals(EntityType.SILVERFISH)) {
                        sender.sendMessage(ChatColor.GREEN + "Silverfish egg successful");
                        if (player != null)
                            player.getInventory().addItem(itemStack);
                    } else {
                        sender.sendMessage(ChatColor.RED + "Silverfish egg failed, please post console on GitHub");
                    }
                    break;
                case "claim":
                    if (sender instanceof Player) {
                        doClaim((Player) sender);
                    }
                    break;
                case "debug":
                    sender.sendMessage(ChatColor.AQUA + "Gathering debug data...");

                    Bukkit.getScheduler().runTaskAsynchronously(cratesPlus, () -> {
                        sender.sendMessage(ChatColor.AQUA + "Uploading config.yml...");
                        String configLink = cratesPlus.uploadConfig();
                        sender.sendMessage(ChatColor.AQUA + "Uploaded config.yml");

                        sender.sendMessage(ChatColor.AQUA + "Uploading data.yml...");
                        String dataLink = cratesPlus.uploadData();
                        sender.sendMessage(ChatColor.AQUA + "Uploaded data.yml");

                        sender.sendMessage(ChatColor.AQUA + "Uploading messages.yml...");
                        String messagesLink = cratesPlus.uploadMessages();
                        sender.sendMessage(ChatColor.AQUA + "Uploaded messages.yml");

                        sender.sendMessage(ChatColor.AQUA + "Generating plugin list...");
                        String plugins = "";
                        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
                            plugins += plugin.getName() + " - Version: " + plugin.getDescription().getVersion() + "\n";
                        }
                        sender.sendMessage(ChatColor.AQUA + "Completed plugin list");

                        sender.sendMessage(ChatColor.AQUA + "Uploading plugin list...");
                        String pluginsLink = MCDebug.paste("plugins.txt", plugins);
                        sender.sendMessage(ChatColor.AQUA + "Uploaded plugin list");

                        sender.sendMessage(ChatColor.AQUA + "Uploading data to MC Debug...");
                        String finalLinks = uploadDebugData(configLink, dataLink, messagesLink, pluginsLink);
                        String[] links = null;
                        if (finalLinks != null) {
                            links = finalLinks.split("\\|");
                        }

                        sender.sendMessage(ChatColor.GREEN + "Completed uploading debug data!");
                        if (links != null && links.length == 2) {
                            sender.sendMessage(ChatColor.GREEN + "You can use the following link to manage your data " + ChatColor.GOLD + links[1]);
                            sender.sendMessage(ChatColor.GREEN + "You can use the following link to share your data " + ChatColor.GOLD + links[0]);
                        } else {
                            sender.sendMessage(ChatColor.GREEN + "You can use the following link to share your data " + ChatColor.GOLD + finalLinks);
                        }

                    });
                    break;
                case "opener":
                case "openers":
                    if (args.length > 1) {
                        if (args.length < 3) {
                            sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.RED + "Correct usage: /" + string + " " + args[0] + " <crate> <opener>");
                        } else {
                            if (CratesPlus.getOpenHandler().openerExist(args[2])) {
                                Opener opener = CratesPlus.getOpenHandler().getOpener(args[2]);
                                if (cratesPlus.getConfigHandler().getCrate(args[1].toLowerCase()) == null) {
                                    sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.RED + "No crate exists with that name");
                                } else if (!cratesPlus.getConfigHandler().getCrate(args[1].toLowerCase()).supportsOpener(opener)) {
                                    sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.RED + "Opener does not support crate type");
                                } else {
//									cratesPlus.getConfigHandler().getCrate(args[1].toLowerCase()).setOpener(args[2]);
                                    sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.GREEN + "Set opener to " + args[2]);
                                }
                            } else {
                                sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.RED + "No opener is registered with that name");
                            }
                        }

                    } else {
                        sender.sendMessage(ChatColor.GOLD + "Registered Openers:");
                        sender.sendMessage(ChatColor.AQUA + "Name" + ChatColor.GRAY + " | " + ChatColor.YELLOW + "Plugin");
                        sender.sendMessage(ChatColor.AQUA + "");
                        for (Map.Entry<String, Opener> map : CratesPlus.getOpenHandler().getRegistered().entrySet()) {
                            sender.sendMessage(ChatColor.AQUA + map.getKey() + ChatColor.GRAY + " | " + ChatColor.YELLOW + map.getValue().getPlugin().getDescription().getName());
                        }
                    }
                    break;
                case "reload":
                    cratesPlus.reloadPlugin();
                    sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.GREEN + "CratesPlus was reloaded - This feature is not fully supported and may not work correctly");
                    break;
                case "settings":
                    if (!(sender instanceof Player)) {
                        sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.RED + "This command must be ran as a player");
                        return false;
                    }
                    cratesPlus.getSettingsHandler().openSettings((Player) sender);
                    break;
                case "create":
                    if (sender instanceof Player && args.length < 2) {
                        // Lets try and open a sign to do the name! :D
                        player = (Player) sender;

                        cratesPlus.addCreating(player.getUniqueId());
                        try {
                            Constructor signConstructor = ReflectionUtil.getNMSClass("PacketPlayOutOpenSignEditor").getConstructor(ReflectionUtil.getNMSClass("BlockPosition"));
                            Object packet = signConstructor.newInstance(ReflectionUtil.getBlockPosition(player));
                            SignInputHandler.injectNetty(player);
                            ReflectionUtil.sendPacket(player, packet);
                        } catch (Exception e) {
                            e.printStackTrace();
                            cratesPlus.removeCreating(player.getUniqueId());
                        }
                        return true;
                    }

                    if (args.length < 2) {
                        sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.RED + "Correct Usage: /crate create <name>");
                        return false;
                    }

                    String name = args[1];
                    FileConfiguration config = cratesPlus.getConfig();
                    if (config.isSet("Crates." + name)) {
                        sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.RED + name + " crate already exists");
                        return false;
                    }

                    // Setup example item
                    config.set("Crates." + name + ".Winnings.1.Type", "ITEM");
                    config.set("Crates." + name + ".Winnings.1.Item Type", "IRON_SWORD");
                    config.set("Crates." + name + ".Winnings.1.Item Data", 0);
                    config.set("Crates." + name + ".Winnings.1.Percentage", 0);
                    config.set("Crates." + name + ".Winnings.1.Name", "&6&lExample Sword");
                    config.set("Crates." + name + ".Winnings.1.Amount", 1);

                    // Setup key with defaults
                    config.set("Crates." + name + ".Key.Item", "TRIPWIRE_HOOK");
                    config.set("Crates." + name + ".Key.Name", "%type% Crate Key");
                    config.set("Crates." + name + ".Key.Enchanted", true);

                    config.set("Crates." + name + ".Knockback", 0.0);
                    config.set("Crates." + name + ".Broadcast", false);
                    config.set("Crates." + name + ".Firework", false);
                    config.set("Crates." + name + ".Preview", true);
                    config.set("Crates." + name + ".Block", "CHEST");
                    config.set("Crates." + name + ".Color", "WHITE");
                    cratesPlus.saveConfig();
                    cratesPlus.reloadConfig();

                    cratesPlus.getConfigHandler().registerCrate(cratesPlus, config, name);
                    cratesPlus.getSettingsHandler().setupCratesInventory();

                    sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.GREEN + name + " crate has been created");
                    break;
                case "rename":
                    if (args.length < 3) {
                        sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.RED + "Correct Usage: /crate rename <old name> <new name>");
                        return false;
                    }

                    String oldName = args[1];
                    String newName = args[2];

                    if (!cratesPlus.getConfigHandler().getCrates().containsKey(oldName.toLowerCase())) {
                        sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.RED + oldName + " crate was not found");
                        return false;
                    }
                    Crate crate = cratesPlus.getConfigHandler().getCrates().get(oldName.toLowerCase());

                    config = cratesPlus.getConfig();
                    if (config.isSet("Crates." + newName)) {
                        sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.RED + newName + " crate already exists");
                        return false;
                    }

                    LinfootUtil.copyConfigSection(config, "Crates." + crate.getName(), "Crates." + newName);

                    config.set("Crates." + crate.getName(), null);
                    cratesPlus.saveConfig();
                    cratesPlus.reloadConfig();

                    cratesPlus.getConfigHandler().getCrates().remove(oldName.toLowerCase());
                    cratesPlus.getConfigHandler().registerCrate(cratesPlus, config, newName);
                    cratesPlus.getSettingsHandler().setupCratesInventory();

                    sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.GREEN + oldName + " has been renamed to " + newName);
                    break;
                case "delete":
                    if (args.length < 2) {
                        sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.RED + "Correct Usage: /crate delete <name>");
                        return false;
                    }

                    name = args[1];
                    config = cratesPlus.getConfig();
                    if (!config.isSet("Crates." + name)) {
                        sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.RED + name + " crate doesn't exist");
                        return false;
                    }

                    config.set("Crates." + name, null);
                    cratesPlus.saveConfig();
                    cratesPlus.reloadConfig();
                    cratesPlus.getConfigHandler().getCrates().remove(name.toLowerCase());
                    cratesPlus.getSettingsHandler().setupCratesInventory();

                    sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.GREEN + name + " crate has been deleted");
                    break;
                case "mysterygui":
                    if (args.length < 2) {
                        sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.RED + "Correct Usage: /crate mysterygui <crate>");
                        return false;
                    }

                    String crateType = args[1];

                    crate = cratesPlus.getConfigHandler().getCrates().get(crateType.toLowerCase());
                    if (crate == null) {
                        sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.RED + "Crate not found");
                        return false;
                    }

                    if (!(crate instanceof MysteryCrate) || !(sender instanceof Player)) { // Too lazy to do separate messages
                        sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.RED + "Crate is not a Mystery Crate!");
                        return false;
                    }

                    ((MysteryCrate) crate).openGUI((Player) sender);
                    break;
                case "key":
                    cratesPlus.getLogger().warning("\"/crate key\" was used but is deprecated from version 5, please use \"give\" instead.");
                case "give":
                    if (args.length < 3) {
                        sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.RED + "Correct Usage: /crate give <player/all> <crate> [amount]");
                        return false;
                    }

                    Integer amount = 1;
                    if (args.length > 3) {
                        try {
                            amount = Integer.parseInt(args[3]);
                        } catch (Exception ignored) {
                            sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.RED + "Invalid amount");
                            return false;
                        }
                    }

                    OfflinePlayer offlinePlayer = null;
                    if (!args[1].equalsIgnoreCase("all")) {
                        offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
                        if (offlinePlayer == null || (!offlinePlayer.hasPlayedBefore() && !offlinePlayer.isOnline())) { // Check if the player is online as "hasPlayedBefore" doesn't work until they disconnect?
                            sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.RED + "The player " + args[1] + " was not found");
                            return false;
                        }
                    }

                    crateType = args[2];

                    crate = cratesPlus.getConfigHandler().getCrates().get(crateType.toLowerCase());
                    if (crate == null) {
                        sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.RED + "Crate not found");
                        return false;
                    }

                    if (offlinePlayer == null) {
                        if (args[1].equalsIgnoreCase("all")) {
                            crate.giveAll(amount);
                            sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.GREEN + "Given all online players a crate/key");
                        } else if (args[1].equalsIgnoreCase("alloffline")) {
                            /**
                             * TODO TEST THIS
                             */
                            crate.giveAllOffline(amount);
                            sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.GREEN + "Given all online and offline players a crate/key");
                        }
                    } else {
                        if (crate.give(offlinePlayer, amount))
                            sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.GREEN + "Given " + offlinePlayer.getName() + " a crate/key");
                        else
                            sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.RED + "Failed to give crate/key");
                    }

                    break;
                case "crate":
                case "keycrate":
                    if (args.length == 1) {
                        sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.RED + "Correct Usage: /crate crate <type> [player]");
                        return false;
                    }

                    if (args.length == 3) {
                        player = Bukkit.getPlayer(args[2]);
                    } else if (sender instanceof Player) {
                        player = (Player) sender;
                    } else {
                        sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.RED + "Correct Usage: /crate crate <type> [player]");
                        return false;
                    }

                    if (player == null) {
                        sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.RED + "The player " + args[2] + " was not found");
                        return false;
                    }

                    try {
                        crateType = args[1];
                    } catch (IllegalArgumentException e) {
                        sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.RED + "Please specify a valid crate type");
                        return false;
                    }

                    if (cratesPlus.getConfigHandler().getCrates().get(crateType.toLowerCase()) == null || !(cratesPlus.getConfigHandler().getCrates().get(crateType.toLowerCase()) instanceof KeyCrate)) {
                        sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.RED + "KeyCrate not found");
                        return false;
                    }

                    cratesPlus.getCrateHandler().giveCrate(player, crateType);

                    sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.GREEN + "Given " + player.getDisplayName() + ChatColor.RESET + ChatColor.GREEN + " a crate");
                    break;
            }
        } else {

            // Help Messages
            sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.AQUA + "----- CratePlus v" + cratesPlus.getDescription().getVersion() + " Help -----");
            sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.AQUA + "/crate reload " + ChatColor.YELLOW + "Reload configuration for CratesPlus (Experimental)");
//			sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.AQUA + "/crate settings " + ChatColor.YELLOW + "Edit settings of CratesPlus and crate winnings");
            sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.AQUA + "/crate create <name> " + ChatColor.YELLOW + "Create a new crate");
            sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.AQUA + "/crate rename <old name> <new name> " + ChatColor.YELLOW + "Rename a crate");
            sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.AQUA + "/crate delete <name> " + ChatColor.YELLOW + "Delete a crate");
            sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.AQUA + "/crate give <player/all> [crate] [amount] " + ChatColor.YELLOW + "Give player a crate/key, if no crate given it will be random");
            sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.AQUA + "/crate crate <type> [player] " + ChatColor.YELLOW + "Give player a crate to be placed, for use by admins");
            sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.AQUA + "/crate debug " + ChatColor.YELLOW + "Generates a debug link for sending info about your server and config");
//			sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.AQUA + "/crate claim " + ChatColor.YELLOW + "Claim any keys that are waiting for you");


            //			sender.sendMessage(cratesPlus.getPluginPrefix() + ChatColor.AQUA + "/crate opener <name/type> <opener> " + ChatColor.YELLOW + "- Change the opener for a specific crate or crate type");
        }

        return true;
    }

    private void doClaim(Player player) {
        if (!cratesPlus.getCrateHandler().hasPendingKeys(player.getUniqueId())) {
            player.closeInventory();
            player.sendMessage(ChatColor.RED + "You currently don't have any keys to claim");
            return;
        }
        GUI gui = new GUI("Claim Crate Keys");
        Integer i = 0;
        for (Map.Entry<String, Integer> map : cratesPlus.getCrateHandler().getPendingKey(player.getUniqueId()).entrySet()) {
            final String crateName = map.getKey();
            final KeyCrate crate = (KeyCrate) cratesPlus.getConfigHandler().getCrates().get(crateName.toLowerCase());
            if (crate == null)
                return; // Crate must have been removed?
            ItemStack keyItem = crate.getKey().getKeyItem(1);
            if (map.getValue() > 1) {
                ItemMeta itemMeta = keyItem.getItemMeta();
                itemMeta.setDisplayName(itemMeta.getDisplayName() + " x" + map.getValue());
                keyItem.setItemMeta(itemMeta);
            }
            gui.setItem(i, keyItem, new GUI.ClickHandler() {
                @Override
                public void doClick(Player player, GUI gui) {
                    cratesPlus.getCrateHandler().claimKey(player.getUniqueId(), crateName);
                }
            });
            i++;
        }
        gui.open(player);
    }

    private String uploadDebugData(String configLink, String dataLink, String messagesLink, String pluginsLink) {
        String urlStr = "http://mcdebug.xyz/api/v2/submit/?plugin=cratesplus&config=" + configLink + "&data=" + dataLink + "&messages=" + messagesLink + "&plugins=" + pluginsLink + "&bukkitVer=" + cratesPlus.getBukkitVersion();

        HttpURLConnection connection;
        try {
            //Create connection
            URL url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();
//			connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.flush();
            wr.close();

            //Get Response
            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            JSONParser jsonParser = new JSONParser();
            JSONObject obj = (JSONObject) jsonParser.parse(rd.readLine());
            return "https://mcdebug.xyz/cratesplus/share/" + obj.get("id") + "|" + "https://mcdebug.xyz/cratesplus/admin/" + obj.get("adminid");
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
