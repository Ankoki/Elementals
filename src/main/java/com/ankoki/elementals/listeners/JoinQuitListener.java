package com.ankoki.elementals.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinQuitListener implements Listener {

    @EventHandler
    private void playerJoin(PlayerJoinEvent e) {
        final Player player = e.getPlayer();
        if (player.isOp() || player.hasPermission("elementals.admin")) {

        }
    }
}
