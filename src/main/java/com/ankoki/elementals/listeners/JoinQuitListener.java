package com.ankoki.elementals.listeners;

import com.ankoki.elementals.Elementals;
import com.ankoki.elementals.utils.Utils;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@SuppressWarnings("unused")
@RequiredArgsConstructor
public class JoinQuitListener implements Listener {
    private final Elementals plugin;

    @EventHandler(priority = EventPriority.LOWEST)
    private void playerJoin(PlayerJoinEvent e) {
        final Player player = e.getPlayer();
        if (player.isOp() || player.hasPermission("elementals.admin")) {
            if (!plugin.isLatest()) {
                player.sendMessage(Utils.coloured("&6৺ &eYou aren't running the latest version of &6Elementals&e!"));
                TextComponent github =
                        new TextComponent(Utils.coloured("           &6[&cClick me to download the latest version!&6]"));
                github.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder("Click me to visit Spigot!")
                        .color(net.md_5.bungee.api.ChatColor.GRAY)
                        .italic(true)
                        .create()));
                github.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL,
                        "https://www.github.com/Ankoki-Dev/Elementals/releases/latest"));
                player.spigot().sendMessage(github);
            }
        }
    }
}
