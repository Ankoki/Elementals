package com.ankoki.elementals.utils;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Utils {

    public static String coloured(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static boolean isLookingAt(Player player, Material material) {
        try {
            return player.getTargetBlockExact(10, FluidCollisionMode.SOURCE_ONLY).getType() == material;
        } catch (NullPointerException ex) {
            return material == Material.AIR;
        }
    }

    public static boolean canSee(Player player, Material material, int distance) {
        for (Block block : player.getLineOfSight(null, distance)) {
            if (block.getType() == material) {
                return true;
            }
        }
        return false;
    }

    public static void sendActionBar(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(Utils.coloured(message)));
    }
}
