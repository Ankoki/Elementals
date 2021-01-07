package com.ankoki.elementals.managers;

import com.ankoki.elementals.Elementals;
import lombok.RequiredArgsConstructor;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
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
    private boolean isReversing;
    private static final double DEGREES_TO_RADIANS = Math.PI/180;

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
     * @param rounds   The amount of times the ring will go up around
     *                 the player, if reverse is true, then this counts
     *                 towards one up and one down.
     * @param reverse If reverse is true, the rings will go up and
     *                then down again around the player.
     * @param colours  The colour you want the particles to be, they
     *                will be randomised and can be as many as wanted
     */
    public void spawnRings(int rounds, boolean reverse, Color... colours) {
        duration = reverse ? (20 * rounds) * 2 : 20 * rounds;
        List<Color> allColours = Arrays.asList(colours);
        yValue = 0;
        if (reverse) {
            isReversing = false;
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (duration == 0) {
                        this.cancel();
                    }
                    Player updatedPlayer = Bukkit.getPlayer(player.getUniqueId());
                    if (updatedPlayer != null) {
                        Location pLoc = updatedPlayer.getLocation().add(0, yValue, 0);
                        for (Location loc : getCircle(pLoc, 1, 50)) {
                            Color randomColour = allColours.get(new Random().nextInt(allColours.size()));
                            loc.getWorld().spawnParticle(Particle.REDSTONE, loc, 1,
                                    new Particle.DustOptions(randomColour, 1));
                        }
                        if (yValue > 2) {
                            isReversing = true;
                        } else if (yValue < 0) {
                            isReversing = false;
                        }
                        if (isReversing) {
                            yValue -= 0.1;
                        } else {
                            yValue += 0.1;
                        }
                        duration--;
                    } else {
                        this.cancel();
                    }
                }
            }.runTaskTimer(plugin, 0L, 1L);
        } else {
            new BukkitRunnable() {
                @Override
                public void run() {

                }
            }.runTaskTimer(plugin, 0L, 2L);
        }
    }

    // I cant do maths can you tell im gay
    /*
    private List<Location> getCircle(Location centre, int radius, int density, Vector rotationVector, double rotationAngle) {
        double points = 6.283185307179586 * radius;
        double delta = 360 / points;
        double theta = 0;
        List<Location> allLocations = new ArrayList<>();
        for (int i = 0; i <= points; i++) {
            double x = Math.cos(theta * DEGREES_TO_RADIANS) * radius;
            double y = Math.sin(theta * DEGREES_TO_RADIANS) * radius;
            Vector vector = new Vector (x, y, 0);
            vector.rotateAroundAxis(vector, rotationAngle);
            allLocations.add(centre.add(vector));
            theta += delta;
        }
        return allLocations;
    }*/

    private List<Location> getCircle(Location centre, double radius, int density) {
        World world = centre.getWorld();
        double increment = (2 * Math.PI)/density;
        List<Location> locations = new ArrayList<>();
        for (int i = 0; i < density; i++) {
            double angle = i * increment;
            double x = centre.getX() + (radius * Math.cos(angle));
            double z = centre.getZ() + (radius * Math.sin(angle));
            locations.add(new Location(world, x, centre.getY(), z));
        }
        return locations;
    }
}
