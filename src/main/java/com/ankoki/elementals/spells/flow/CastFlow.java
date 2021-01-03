package com.ankoki.elementals.spells.flow;

import com.ankoki.elementals.Elementals;
import com.ankoki.elementals.managers.GenericSpell;
import com.ankoki.elementals.managers.Prolonged;
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

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class CastFlow extends Prolonged implements GenericSpell {
    private final Elementals plugin;
    @Getter
    @ConfigValue("flow-enabled")
    private boolean enabled = true;
    private final List<Player> casting = new ArrayList<>();

    @Override
    public boolean onCast(Player player) {
        if (Utils.canSee(player, 10, Material.WATER)) {
            casting.add(player);
            new BukkitRunnable() {
                @Override
                public void run() {
                    Location targetBlock = player.getTargetBlock(null, 4).getLocation();
                    if (targetBlock.getBlock().getType() != Material.AIR &&
                        targetBlock.getBlock().getType() != Material.WATER) {
                        casting.remove(player);
                        Utils.sendActionBar(player, Messages.msg("flow-interrupted"));
                        this.cancel();
                        return;
                    } else if (!casting.contains(player)) {
                        casting.remove(player);
                        Utils.sendActionBar(player, Messages.msg("flow-interrupted"));
                        this.cancel();
                        return;
                    }
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
            return true;
        } else {
            Utils.sendActionBar(player, Messages.msg("flow-no-water"));
        }
        return false;
    }

    @Override
    public void onCancel(Player player) {
        casting.remove(player);
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
