package com.ankoki.elementals.spells.generic.travel;

import com.ankoki.elementals.Elementals;
import com.ankoki.elementals.managers.GenericSpell;
import com.ankoki.elementals.managers.ParticlesManager;
import com.ankoki.elementals.managers.Spell;
import com.ankoki.elementals.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
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
            Location loc = player.getTargetBlock(null, 10).getLocation();
            new ParticlesManager(player, plugin).spawnRings(1, true, Color.WHITE, Color.GRAY, Color.BLACK);
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                Player updatedPlayer = Bukkit.getPlayer(player.getUniqueId());
                loc.setPitch(updatedPlayer.getLocation().getPitch());
                loc.setYaw(updatedPlayer.getLocation().getYaw());
                player.teleport(loc);
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
