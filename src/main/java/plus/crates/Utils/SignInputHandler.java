package plus.crates.Utils;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import plus.crates.CratesPlus;
import plus.crates.Events.PlayerInputEvent;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class SignInputHandler {
    private static Field channelField;

    static {
        try {
            channelField = ReflectionUtil.getNMSClass("NetworkManager").getDeclaredField("channel");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void injectNetty(final Player player) {
        try {
            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            final Channel channel = (Channel) channelField.get(playerConnection.getClass().getField("networkManager").get(playerConnection));
            if (channel != null) {
                channel.pipeline().addAfter("decoder", "update_sign", new MessageToMessageDecoder<Object>() {
                    @Override
                    protected void decode(ChannelHandlerContext channelHandlerContext, Object object, List list) throws Exception {
                        try {
                            if (object.toString().contains("PacketPlayInUpdateSign")) {
                                Class iChatBaseComponentClass = ReflectionUtil.getNMSClass("IChatBaseComponent");
                                Object packet = ReflectionUtil.getNMSClass("PacketPlayInUpdateSign").cast(object);
                                Method method = null;

                                try {
                                    method = packet.getClass().getMethod("c");
                                } catch (Exception e) {
                                    // Ignore it, this is mostly for 1.13+ support
                                }

                                if (method == null) {
                                    method = packet.getClass().getMethod("b");
                                }

                                ArrayList<String> lines = new ArrayList<>();
                                Object response = method.invoke(packet);

                                Object[] signLines = (Object[]) response;
                                for (Object line : signLines) {
                                    if (line instanceof String) {
                                        lines.add((String) line);
                                    } else if (iChatBaseComponentClass != null && iChatBaseComponentClass.isAssignableFrom(line.getClass())) {
                                        lines.add((String) iChatBaseComponentClass.getMethod("getText").invoke(iChatBaseComponentClass.cast(line)));
                                    }
                                }

                                if (lines.isEmpty()) {
                                    player.sendMessage(ChatColor.RED + "Unable to handle input");
                                } else {
                                    Bukkit.getScheduler().runTask(JavaPlugin.getPlugin(CratesPlus.class), () -> Bukkit.getPluginManager().callEvent(new PlayerInputEvent(player, lines)));
                                }
                            }
                            list.add(object);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void ejectNetty(Player player) {
        try {
            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            Channel channel = (Channel) channelField.get(playerConnection.getClass().getField("networkManager").get(playerConnection));
            if (channel != null) {
                if (channel.pipeline().get("update_sign") != null) {
                    channel.pipeline().remove("update_sign");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
