package com.ankoki.elementals.listeners;

import com.ankoki.elementals.Elementals;
import com.ankoki.elementals.events.RightClickEvent;
import com.ankoki.elementals.managers.Castable;
import com.ankoki.elementals.managers.ItemManager;
import com.ankoki.elementals.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.WeakHashMap;

@RequiredArgsConstructor
public class SpellListener implements Listener {
    private final Elementals plugin;

    private final WeakHashMap<Player, Long> cooldown = new WeakHashMap<>();

    @EventHandler
    private void onRightClick(RightClickEvent e) {
        Player player = e.getPlayer();
        for (Castable castable : plugin.getCastableSpells()) {
            if (ItemManager.hasSpell(player.getInventory().getItemInMainHand(), castable.getSpell())) {
                long pCooldown = cooldown.get(player) == null ? 0 : cooldown.get(player);
                if ((System.currentTimeMillis() - pCooldown) > (castable.getCooldown() * 1000L)) {
                    if (castable.onCast(player)) {
                        cooldown.put(player, System.currentTimeMillis());
                    }
                } else {
                    Utils.sendActionBar(player, String.format("&eYou can only cast a spell every %s&e seconds", castable.getCooldown()));
                }
            }
        }
    }
}
