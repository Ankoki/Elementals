package com.ankoki.elementals.spells.flow;

import com.ankoki.elementals.Elementals;
import com.ankoki.elementals.managers.Castable;
import com.ankoki.elementals.managers.Spell;
import com.ankoki.elementals.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;

@RequiredArgsConstructor
public class CastFlow implements Castable {
    private final Elementals plugin;

    @Override
    public boolean onCast(Player player) {
        if (Utils.isLookingAt(player, Material.WATER)) {
            Utils.sendActionBar(player, "&eYou are casting &bFLOW&e!");
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!Utils.canSee(player, Material.WATER, 4) &&
                            !Utils.canSee(player, Material.AIR, 4)) {
                        this.cancel();
                        Utils.sendActionBar(player, "&eYou have stopped casting &bFLOW&e!");
                        return;
                    }
                    Location targetBlock = player.getTargetBlock(null, 4).getLocation();
                    targetBlock.getWorld()
                            .getBlockAt(targetBlock)
                            .setType(Material.WATER);
                    plugin.addFlowLocation(targetBlock);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            targetBlock.getWorld()
                                    .getBlockAt(targetBlock)
                                    .setType(Material.AIR);
                            plugin.removeFlowLocation(targetBlock);
                        }
                    }.runTaskLater(plugin, 10L);
                }
            }.runTaskTimer(plugin, 0L, 1L);
        } else {
            Utils.sendActionBar(player, "&eThis spell requries water!");
            return false;
        }
        return false;
    }

    @Override
    public int getCooldown() {
        return 30;
    }

    @Override
    public Spell getSpell() {
        return Spell.FLOW;
    }
}
