package com.ankoki.elementals.spells.flow;

import com.ankoki.elementals.Elementals;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

@RequiredArgsConstructor
public class WaterSpread implements Listener {
    private final Elementals plugin;

    @EventHandler
    private void onFlow(BlockFromToEvent e) {
        if (!(e.getBlock().getType() == Material.WATER)) return;
        if (plugin.getFlowLocations().contains(e.getBlock().getLocation())) {
            e.setCancelled(true);
        }
    }
}
