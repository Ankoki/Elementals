package com.ankoki.elementals.managers;

import com.ankoki.elementals.Elementals;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

@SuppressWarnings("unused")
@RequiredArgsConstructor
public class ParticlesManager {
    private final Player player;
    private final Elementals plugin;
    private int duration = 0;

    /**
     * Spawns a cloud of redstone particles under the players feet,
     * cannot really be seen unless the player is in the air/in water.
     *
     * @param durationInTicks How long you want the cloud at the
     *                        players feet to last for in ticks.
     */
    public void trackCloud(int durationInTicks) {
        duration = durationInTicks/2;
        new BukkitRunnable() {
            @Override
            public void run() {
                if (duration == 0) {
                    this.cancel();
                    return;
                }
                Player updatedPlayer = Bukkit.getPlayer(player.getUniqueId());
                if (updatedPlayer != null) {
                    World world = updatedPlayer.getWorld();
                    world.spawnParticle(Particle.REDSTONE,
                            updatedPlayer.getLocation().subtract(0, 0.1, 0), 5,
                            new Particle.DustOptions(Color.WHITE, 3));
                    world.spawnParticle(Particle.REDSTONE,
                            updatedPlayer.getLocation().subtract(0.5, 0.1, 0), 5,
                            new Particle.DustOptions(Color.WHITE, 3));
                    world.spawnParticle(Particle.REDSTONE,
                            updatedPlayer.getLocation().subtract(0, 0.1, 0.5), 5,
                            new Particle.DustOptions(Color.WHITE, 3));
                    world.spawnParticle(Particle.REDSTONE,
                            updatedPlayer.getLocation().subtract(0.5, 0.1, 0.5), 5,
                            new Particle.DustOptions(Color.WHITE, 3));
                    world.spawnParticle(Particle.REDSTONE,
                            updatedPlayer.getLocation().subtract(0, 0.3, 0), 5,
                            new Particle.DustOptions(Color.WHITE, 3));
                    duration--;
                } else {
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 2L);
    }

    /**
     * Spawns a series of rings around the player which go up
     * and has the player as the center.
     *
     * @param times   The amount of times the ring will loop around
     *                the player, and if reverse is true, it will count
     *                up and down as one time.
     * @param reverse If reverse is true, the rings will go up and
     *                then down again around the player.
     */
    public void spawnRings(int times, boolean reverse) {
        duration = times;
        if (reverse) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (duration == 0) {
                        this.cancel();
                    }
                    duration--;
                }
            }.runTaskTimer(plugin, 0L, 2L);
        } else {
            new BukkitRunnable() {
                @Override
                public void run() {

                }
            }.runTaskTimer(plugin, 0L, 2L);
        }
    }
}
