package com.ankoki.elementals.managers;

import com.ankoki.elementals.events.LeftClickEvent;
import com.ankoki.elementals.events.RightClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class EventManager implements Listener {

    @EventHandler
    private void interact(PlayerInteractEvent e) {
        if (e.getHand() != EquipmentSlot.HAND) return;
        if (e.getAction() == Action.LEFT_CLICK_AIR ||
                e.getAction() == Action.LEFT_CLICK_BLOCK) {
            LeftClickEvent event = new LeftClickEvent(e.getPlayer());
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                e.setCancelled(true);
            }
        } else if (e.getAction() == Action.RIGHT_CLICK_AIR ||
                e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            RightClickEvent event = new RightClickEvent(e.getPlayer());
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                e.setCancelled(true);
            }
        }
    }
}
