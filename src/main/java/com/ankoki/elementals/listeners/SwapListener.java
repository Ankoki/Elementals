package com.ankoki.elementals.listeners;

import com.ankoki.elementals.managers.Spell;
import com.ankoki.elementals.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import redempt.redlib.commandmanager.Messages;

@RequiredArgsConstructor
public class SwapListener implements Listener {
    private final SpellListener listener;

    @EventHandler
    private void onSwitch(PlayerItemHeldEvent e) {
        Player player = e.getPlayer();
        if (listener.isCasting(player)) {
            Spell spell = listener.getCastedSpell(player);
            if (spell.isProlonged()) {
                Utils.sendActionBar(player, Messages.msg("on-stop-cast")
                        .replace("%spell%", spell.getSpellName()));
                listener.removeCaster(player);
            }
        }
    }

    @EventHandler
    private void offhandSwap(PlayerSwapHandItemsEvent e) {
        Player player = e.getPlayer();
        if (listener.isCasting(player)) {
            Spell spell = listener.getCastedSpell(player);
            if (spell.isProlonged()) {
                Utils.sendActionBar(player, Messages.msg("on-stop-cast")
                        .replace("%spell%", spell.getSpellName()));
                listener.removeCaster(player);
            }
        }
    }

    @EventHandler
    private void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        if (listener.isCasting(player)) {
            if (listener.getCastedSpell(player).isProlonged()) {
                listener.removeCaster(player);
            }
        }
    }
}
