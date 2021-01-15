package com.ankoki.elementals.spells.generic.flow;

import com.ankoki.elementals.Elementals;
import com.ankoki.elementals.api.ElementalsAPI;
import com.ankoki.elementals.listeners.SpellListener;
import com.ankoki.elementals.api.GenericSpell;
import com.ankoki.elementals.api.Prolonged;
import com.ankoki.elementals.managers.Spell;
import com.ankoki.elementals.utils.Utils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import redempt.redlib.commandmanager.Messages;

@SuppressWarnings("ConstantConditions")
@RequiredArgsConstructor
public class CastFlow extends Prolonged implements GenericSpell {
    private final Elementals plugin;
    private final Spell spell = new Spell("Flow", 3734, true);
    @Getter
    @Setter
    private int cooldown = 60;

    @Override
    public boolean onCast(Player player) {
        if (Utils.canSeeSource(player, 4, Material.WATER)) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    Location targetBlock = player.getTargetBlock(null, 4).getLocation();
                    if (targetBlock.getBlock().getType() != Material.AIR &&
                        targetBlock.getBlock().getType() != Material.WATER) {
                        ElementalsAPI.removeCaster(player);
                        this.cancel();
                        return;
                    }
                    if (!ElementalsAPI.isCasting(player)) {
                        ElementalsAPI.removeCaster(player);
                        this.cancel();
                        return;
                    }
                    targetBlock.getWorld().getBlockAt(targetBlock);
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
    public Spell getSpell() {
        return spell;
    }
}
