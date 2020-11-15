package plus.crates.Utils;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import plus.crates.Events.PlayerInputEvent;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class SignInputHandler {
    private static Field channelField;
    private static final String HANDLER_NAME = "update_sign";
    private static final Class<?> UPDATE_SIGN_PACKET = ReflectionUtil.getNMSClass("PacketPlayInUpdateSign");
    private static final Class<?> I_CHAT_BASE = ReflectionUtil.getNMSClass("IChatBaseComponent");
    private static final Class<?> I_CHAT_BASE_ARRAY = ReflectionUtil.getNMSClassAsArray("IChatBaseComponent");

    static {
        try {
            channelField = ReflectionUtil.getNMSClass("NetworkManager").getDeclaredField("channel");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void injectNetty(final JavaPlugin plugin, final Player player) {
        try {
            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            final Channel channel = (Channel) channelField.get(playerConnection.getClass().getField("networkManager").get(playerConnection));
            if (channel != null) {
                if (channel.pipeline().get(HANDLER_NAME) != null) return; // Already exists

                channel.pipeline().addAfter("decoder", HANDLER_NAME, new MessageToMessageDecoder<Object>() {
                    @Override
                    protected void decode(ChannelHandlerContext channelHandlerContext, Object object, List list) throws Exception {
                        if (object.getClass().equals(UPDATE_SIGN_PACKET)) {
                            Object packet = UPDATE_SIGN_PACKET.cast(object);

                            String[] lines = null;

                            for (Method method : UPDATE_SIGN_PACKET.getMethods()) {
                                if (method.getReturnType().equals(String[].class)) {
                                    lines = (String[]) method.invoke(packet);
                                    break;
                                } else if (method.getReturnType().equals(I_CHAT_BASE_ARRAY)) {
                                    lines = new String[4];
                                    Object array = method.invoke(packet);
                                    for (int i = 0; i < Array.getLength(array); i++) {
                                        lines[i] = (String) I_CHAT_BASE.getMethod("getText").invoke(Array.get(array, i));
                                    }
                                    break;
                                }
                            }

                            if (lines != null) {
                                String[] finalLines = lines;
                                Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getPluginManager().callEvent(new PlayerInputEvent(player, finalLines)));
                            }
                        }
                        list.add(object);
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
                if (channel.pipeline().get(HANDLER_NAME) != null) {
                    channel.pipeline().remove(HANDLER_NAME);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
