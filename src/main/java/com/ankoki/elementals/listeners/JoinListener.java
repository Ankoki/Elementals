package com.ankoki.elementals.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import redempt.redlib.commandmanager.Messages;

public class JoinListener implements Listener {

    @EventHandler
    private void playerJoin(PlayerJoinEvent e) {
        final Player player = e.getPlayer();
        if (player.isOp() || player.hasPermission("elementals.admin")) {
            if (!Messages.msg("op-join").equals("") && !Messages.msg("op-join").equals(" ")) {
                player.sendMessage(Messages.msg("op-join"));
            }
        }
    }
}
