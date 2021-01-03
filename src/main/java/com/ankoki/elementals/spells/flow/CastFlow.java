package com.ankoki.elementals.spells.flow;

import com.ankoki.elementals.Elementals;
import com.ankoki.elementals.managers.Castable;
import com.ankoki.elementals.managers.Spell;
import com.ankoki.elementals.utils.Utils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import redempt.redlib.commandmanager.Messages;
import redempt.redlib.configmanager.annotations.ConfigValue;

@RequiredArgsConstructor
public class CastFlow implements Castable {
    private final Elementals plugin;
    @Getter
    @ConfigValue("flow-enabled")
    private boolean enabled = true;

    @Override
    public boolean onCast(Player player) {
        if (Utils.canSee(player, 10, Material.WATER)) {
            plugin.addCaster(player);
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!Utils.canSee(player, 4, Material.WATER) &&
                            !Utils.canSee(player, 4, Material.AIR)) {
                        this.cancel();
                        plugin.removeCaster(player);
                        Utils.sendActionBar(player, Messages.msg("flow-interrupted"));
                        return;
                    } else if (!plugin.isCasting(player)) {
                        this.cancel();
                        plugin.removeCaster(player);
                        Utils.sendActionBar(player, Messages.msg("flow-interrupted"));
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
            Utils.sendActionBar(player, Messages.msg("flow-no-water"));
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
