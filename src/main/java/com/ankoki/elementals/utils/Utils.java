package com.ankoki.elementals.utils;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class Utils {

    public static String coloured(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String colouredPrefix(String message) {
        return ChatColor.translateAlternateColorCodes('&', "&6৺ &eElementals &7" + message);
    }

    public static boolean isLookingAt(Player player, Material material) {
        try {
            return player.getTargetBlockExact(10, FluidCollisionMode.SOURCE_ONLY).getType() == material;
        } catch (NullPointerException ex) {
            return material == Material.AIR;
        }
    }

    public static void sendActionBar(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(Utils.coloured(message)));
    }
}
