package com.ankoki.elementals.managers;

import com.ankoki.elementals.Elementals;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@SuppressWarnings("unused")
@RequiredArgsConstructor
public class ParticlesManager {
    private final Player player;
    private final Elementals plugin;
    private int duration = 0;
    private double yValue = 0.1;

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
     * @param timeInTicks   The amount of time in ticks the ring will take before
     *                      stopping the player, and if reverse is true, it will count
     *                      up and down as one time.
     * @param reverse If reverse is true, the rings will go up and
     *                then down again around the player.
     * @param colours  The colour you want the particles to be, they
     *                will be randomised and can be as many as wanted
     */
    public void spawnRings(int timeInTicks, boolean reverse, Color... colours) {
        duration = timeInTicks;
        List<Color> allColours = Arrays.asList(colours);
        yValue = 0.1;
        if (reverse) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (duration == 0) {
                        this.cancel();
                    }
                    Player updatedPlayer = Bukkit.getPlayer(player.getUniqueId());
                    if (updatedPlayer != null) {
                        Color randomColour = allColours.get(new Random().nextInt(allColours.size()));
                        World world = updatedPlayer.getWorld();
                        world.spawnParticle(Particle.REDSTONE,
                                updatedPlayer.getLocation().subtract(0, yValue, 0.5), 5,
                                new Particle.DustOptions(randomColour, 2));
                        world.spawnParticle(Particle.REDSTONE,
                                updatedPlayer.getLocation().subtract(0.5, yValue, 0),5,
                                new Particle.DustOptions(randomColour, 2));
                        world.spawnParticle(Particle.REDSTONE,
                                updatedPlayer.getLocation().subtract(0.5, yValue, 0.5),5,
                                new Particle.DustOptions(randomColour, 2));
                        world.spawnParticle(Particle.REDSTONE,
                                updatedPlayer.getLocation().add(0, -yValue, 0.5), 5,
                                new Particle.DustOptions(randomColour, 2));
                        world.spawnParticle(Particle.REDSTONE,
                                updatedPlayer.getLocation().add(0.5, -yValue, 0),5,
                                new Particle.DustOptions(randomColour, 2));
                        world.spawnParticle(Particle.REDSTONE,
                                updatedPlayer.getLocation().add(0.5, -yValue, 0.5),5,
                                new Particle.DustOptions(randomColour, 2));
                        yValue -= 0.1;
                        duration--;
                    } else {
                        this.cancel();
                    }
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
