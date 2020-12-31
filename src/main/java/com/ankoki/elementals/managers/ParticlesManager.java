package com.ankoki.elementals.managers;

import com.ankoki.elementals.Elementals;
import lombok.RequiredArgsConstructor;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

@RequiredArgsConstructor
public class ParticlesManager {
    private final Player player;
    private final Elementals plugin;
    private int duration = 0;

    /**
     * Find a way to update the player's location in the runnable
     * coz right now its anti pog asf
     */
    public void giveCloud(int durationInTicks) {
        World world = player.getWorld();
        duration = durationInTicks;
        new BukkitRunnable() {
            @Override
            public void run() {
                if (duration == 0) {
                    this.cancel();
                } else {
                    world.spawnParticle(Particle.REDSTONE,
                            player.getLocation().subtract(0, 0, 0), 5,
                            new Particle.DustOptions(Color.WHITE, 3));
                    world.spawnParticle(Particle.REDSTONE,
                            player.getLocation().subtract(0.5, 0, 0), 5,
                            new Particle.DustOptions(Color.WHITE, 3));
                    world.spawnParticle(Particle.REDSTONE,
                            player.getLocation().subtract(0, 0, 0.5), 5,
                            new Particle.DustOptions(Color.WHITE, 3));
                    world.spawnParticle(Particle.REDSTONE,
                            player.getLocation().subtract(0.5, 0, 0.5), 5,
                            new Particle.DustOptions(Color.WHITE, 3));
                    world.spawnParticle(Particle.REDSTONE,
                            player.getLocation().subtract(0, 0.2, 0), 5,
                            new Particle.DustOptions(Color.WHITE, 3));
                    duration--;
                }
            }
        }.runTaskTimer(plugin, 0, 1L);
    }
}
