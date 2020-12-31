package com.ankoki.elementals.managers;

import com.ankoki.elementals.Elementals;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
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
     * Spawns a cloud of redstone particles under the players feet,
     * cannot really be seen unless the player is in the air/in water.
     *
     * @param durationInTicks How long you want the cloud at the
     *                        players feet to last for in ticks.
     */
    public void giveCloud(int durationInTicks) {
        duration = durationInTicks;
        new BukkitRunnable() {
            @Override
            public void run() {
                if (duration == 0) {
                    this.cancel();
                } else {
                    Player updatedPlayer = Bukkit.getPlayer(player.getUniqueId());
                    if (updatedPlayer != null) {
                        World world = updatedPlayer.getWorld();
                        world.spawnParticle(Particle.REDSTONE,
                                updatedPlayer.getLocation().subtract(0, 0.3, 0), 5,
                                new Particle.DustOptions(Color.WHITE, 3));
                        world.spawnParticle(Particle.REDSTONE,
                                updatedPlayer.getLocation().subtract(0.5, 0.3, 0), 5,
                                new Particle.DustOptions(Color.WHITE, 3));
                        world.spawnParticle(Particle.REDSTONE,
                                updatedPlayer.getLocation().subtract(0, 0.3, 0.5), 5,
                                new Particle.DustOptions(Color.WHITE, 3));
                        world.spawnParticle(Particle.REDSTONE,
                                updatedPlayer.getLocation().subtract(0.5, 0.3, 0.5), 5,
                                new Particle.DustOptions(Color.WHITE, 3));
                        world.spawnParticle(Particle.REDSTONE,
                                updatedPlayer.getLocation().subtract(0, 0.5, 0), 5,
                                new Particle.DustOptions(Color.WHITE, 3));
                        duration--;
                    } else {
                        this.cancel();
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 1L);
    }
}
