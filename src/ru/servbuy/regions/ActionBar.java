package ru.servbuy.regions;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ActionBar
{
    public static String version;
    public static boolean works = true;
    public static Plugin plugin;

    @EventHandler
    public static void sendActionBar(final Player player, final String message) {
        if (!player.isOnline() || !player.hasPermission("rginfo.see")) return;
        if(version.startsWith("v1_16"))
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
        else if (version.substring(3).compareTo("12") > 0 && !version.startsWith("v1_8_") && !version.startsWith("v1_9_"))
            sendActionBarPost112(player, message);
        else
            sendActionBarPre112(player, message);
    }

    private static void sendActionBarPost112(final Player player, final String message) {
        try {
            final Class<?> craftPlayerClass = Class.forName("org.bukkit.craftbukkit." + ActionBar.version + ".entity.CraftPlayer");
            final Object craftPlayer = craftPlayerClass.cast(player);
            final Class<?> c4 = Class.forName("net.minecraft.server." + ActionBar.version + ".PacketPlayOutChat");
            final Class<?> c5 = Class.forName("net.minecraft.server." + ActionBar.version + ".Packet");
            final Class<?> c7 = Class.forName("net.minecraft.server." + ActionBar.version + ".IChatBaseComponent");
            final Class<?> c6 = Class.forName("net.minecraft.server." + ActionBar.version + ".ChatComponentText");
            final Class<?> chatMessageTypeClass = Class.forName("net.minecraft.server." + ActionBar.version + ".ChatMessageType");
            final Object[] chatMessageTypes = chatMessageTypeClass.getEnumConstants();
            Object chatMessageType = null;
            for (final Object obj : chatMessageTypes)
                if (obj.toString().equals("GAME_INFO"))
                    chatMessageType = obj;
            final Object o = c6.getConstructor(String.class).newInstance(message);
            final Object ppoc = c4.getConstructor(c7, chatMessageTypeClass).newInstance(o, chatMessageType);
            nmsFooter(craftPlayerClass, craftPlayer, c5, ppoc);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            ActionBar.works = false;
        }
    }

    private static void sendActionBarPre112(final Player player, final String message) {
        try {
            final Class<?> craftPlayerClass = Class.forName("org.bukkit.craftbukkit." + ActionBar.version + ".entity.CraftPlayer");
            final Object craftPlayer = craftPlayerClass.cast(player);
            final Class<?> c4 = Class.forName("net.minecraft.server." + ActionBar.version + ".PacketPlayOutChat");
            final Class<?> c5 = Class.forName("net.minecraft.server." + ActionBar.version + ".Packet");
            final Class<?> c7 = Class.forName("net.minecraft.server." + ActionBar.version + ".IChatBaseComponent");
            final Class<?> c6 = Class.forName("net.minecraft.server." + ActionBar.version + ".ChatSerializer");
            final Method m3 = c6.getDeclaredMethod("a", String.class);
            final Object cbc = c7.cast(m3.invoke(c6, "{\"text\": \"" + message + "\"}"));
            Object ppoc = c4.getConstructor(c7, Byte.TYPE).newInstance(cbc, (byte)2);
            nmsFooter(craftPlayerClass, craftPlayer, c5, ppoc);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            ActionBar.works = false;
        }
    }

    private static void nmsFooter(Class<?> craftPlayerClass, Object craftPlayer, Class<?> c5, Object ppoc) throws NoSuchMethodException, NoSuchFieldException, IllegalAccessException, InvocationTargetException {
        final Method m4 = craftPlayerClass.getDeclaredMethod("getHandle");
        final Field f1 = m4.invoke(craftPlayer).getClass().getDeclaredField("playerConnection");
        final Object pc = f1.get(m4.invoke(craftPlayer));
        pc.getClass().getDeclaredMethod("sendPacket", c5).invoke(pc, ppoc);
    }

}
