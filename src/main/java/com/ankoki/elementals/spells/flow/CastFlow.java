package com.ankoki.elementals.spells.flow;

import com.ankoki.elementals.Elementals;
import com.ankoki.elementals.managers.Castable;
import com.ankoki.elementals.managers.Spell;
import com.ankoki.elementals.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class CastFlow implements Castable {
    private final Elementals plugin;

    private final List<Player> casting = new ArrayList<>();

    @Override
    public boolean onCast(Player player) {
        ItemStack wand = player.getInventory().getItemInMainHand();
        if (Utils.isLookingAt(player, Material.WATER)) {
            if (!casting.contains(player)) {
                casting.add(player);
                Utils.sendActionBar(player, "&eYou are casting &bFLOW&e!");
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (casting.contains(player)) {
                            if (player.getTargetBlock(null, 4).getType() != Material.AIR &&
                                    player.getTargetBlock(null, 4).getType() != Material.WATER) {
                                this.cancel();
                                casting.remove(player);
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
                        } else {
                            this.cancel();
                        }
                    }
                }.runTaskTimer(plugin, 0L, 1L);
            } else {
                casting.remove(player);
                Utils.sendActionBar(player, "&eYou have stopped casting &bFLOW&e!");
            }
        } else {
            Utils.sendActionBar(player, "&eThis spell requries water!");
            return false;
        }
        return true;
    }

    @Override
    public int getCooldown() {
        return 5;
    }

    @Override
    public Spell getSpell() {
        return Spell.FLOW;
    }
}
