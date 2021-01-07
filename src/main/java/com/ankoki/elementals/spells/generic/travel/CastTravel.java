package com.ankoki.elementals.spells.generic.travel;

import com.ankoki.elementals.Elementals;
import com.ankoki.elementals.managers.GenericSpell;
import com.ankoki.elementals.managers.ParticlesManager;
import com.ankoki.elementals.managers.Spell;
import com.ankoki.elementals.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import redempt.redlib.commandmanager.Messages;

@SuppressWarnings("unused")
@RequiredArgsConstructor
public class CastTravel implements GenericSpell {
    private final Elementals plugin;

    @Override
    public boolean onCast(Player player) {
        if (player.getTargetBlock(null, 10).getType().isBlock()) {
            float yaw = player.getLocation().getYaw();
            float pitch = player.getLocation().getPitch();
            new ParticlesManager(player, plugin).spawnRings(1, true, Color.WHITE, Color.GRAY, Color.BLACK);
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                player.teleport(player.getTargetBlock(null, 10).getLocation());
                player.getLocation().setYaw(yaw);
                player.getLocation().setPitch(pitch);
            }, 20L);
        } else {
            Utils.sendActionBar(player, Messages.msg("travel-solid"));
            return false;
        }
        return true;
    }

    @Override
    public int getCooldown() {
        return 10;
    }

    @Override
    public Spell getSpell() {
        return Spell.TRAVEL;
    }
}
