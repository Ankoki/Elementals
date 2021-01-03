package com.ankoki.elementals.utils;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Utils {

    /**
     * Replaces minecraft's § in messages with &, the default and
     * standard accepted format.
     *
     * @param message The message you want to translate.
     * @return Translated message.
     */
    public static String coloured(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    /**
     * Returns wether or not the block a player is looking at is
     * of the material type, this includes water source blocks.
     *
     * @param player The player you want to check.
     * @param material The material you want to check if the player is
     *                 looking at.
     * @return Wether the player is looking at the specified material type
     */
    public static boolean isLookingAt(Player player, Material material) {
        try {
            return player.getTargetBlockExact(10, FluidCollisionMode.SOURCE_ONLY).getType() == material;
        } catch (NullPointerException ex) {
            return material == Material.AIR;
        }
    }

    /**
     * Checks the blocks infront of a player to
     * see wether or not any of them are of the
     * specified material/materials.
     *
     * @param player The player you want to check.
     * @param distance How far in front of the player you want to check.
     * @param materials Materials you want to check if the player can see.
     * @return Wether or not a player can see a block of that type.
     */
    public static boolean canSee(Player player, int distance, Material... materials) {
        for (Block block : player.getLineOfSight(null, distance)) {
            for (Material material : materials) {
                if (block.getType() == material) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Sends an action bar message to player which has translated
     * colour codes built in.
     *
     * @param player The player you want to send this action bar.
     * @param message The message you want to send to the player's
     *                action bar.
     */
    public static void sendActionBar(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(Utils.coloured(message)));
    }

    /**
     * Checks the vertsion of a plugin compared too a version you want
     * it to match up with, for example, if you wanted to make sure if a plugin
     * had a version higher than 1.2, you can use versionChecker(Plugin, 1, 2);
     *
     * @param plugin Plugin is the plugin you want to check the version of, usually
     *               obtained through Bukkit.getPluginManager.getPlugin("pluginName");
     * @param major The major of the version you want to check against, if there was
     *              a version 1.2, 1 would be the major.
     * @param minor The minor of the version you want to check against, if there was
     *              a version 1.2, 2 would be the minor.
     * @return Wether the plugins version is equal to or greater than the inputted
     *         version.
     */
    public static boolean checkPluginVersion(Plugin plugin, int major, int minor) {
        major *= 10;
        int pluginVer = Integer.parseInt(plugin.getDescription().getVersion().replace(".", ""));
        int required = major + minor;
        return pluginVer >= required;
    }

}
