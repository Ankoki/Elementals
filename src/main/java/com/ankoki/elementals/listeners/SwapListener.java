package com.ankoki.elementals.listeners;

import com.ankoki.elementals.api.ElementalsAPI;
import com.ankoki.elementals.managers.Spell;
import com.ankoki.elementals.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import redempt.redlib.commandmanager.Messages;

public class SwapListener implements Listener {

    @EventHandler
    private void onSwitch(PlayerItemHeldEvent e) {
        Player player = e.getPlayer();
        if (ElementalsAPI.isCasting(player)) {
            Spell spell = ElementalsAPI.getCastedSpell(player);
            if (spell.isProlonged()) {
                Utils.sendActionBar(player, Messages.msg("on-stop-cast")
                        .replace("%spell%", spell.getSpellName()));
                ElementalsAPI.removeCaster(player);
            }
        }
    }

    @EventHandler
    private void offhandSwap(PlayerSwapHandItemsEvent e) {
        Player player = e.getPlayer();
        if (ElementalsAPI.isCasting(player)) {
            Spell spell = ElementalsAPI.getCastedSpell(player);
            if (spell.isProlonged()) {
                Utils.sendActionBar(player, Messages.msg("on-stop-cast")
                        .replace("%spell%", spell.getSpellName()));
                ElementalsAPI.removeCaster(player);
            }
        }
    }

    @EventHandler
    private void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        if (ElementalsAPI.isCasting(player)) {
            if (ElementalsAPI.getCastedSpell(player).isProlonged()) {
                ElementalsAPI.removeCaster(player);
            }
        }
    }
}
