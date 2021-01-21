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
    public void spawnCloud(int durationInTicks) {
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
                            yValue = 0;
                        }
                        yValue += 0.1;
                        duration--;
                    } else {
                        this.cancel();
                    }
                }
            }.runTaskTimer(plugin, 0L, 2L);
        }
    }

    /**
     * Spawns a helix at the player's location surrounding them in
     * a hurricane like shape
     *
     * @param height This specifies how tall you want the helix
     *               to be in blocks
     * @param allColours An array of colours that the particles
     *                   that are being spawned can possibly be.
     *                   These are randomised.
     */
    public void spawnHelix(int height, Color... allColours) {
        int radius = 2;
        for(double y = 0; y <= height; y += 0.05) {
            double x = radius * Math.cos(y);
            double z = radius * Math.sin(y);
            Color randomColour = allColours[new Random().nextInt(allColours.length)];
            Player updatedPlayer = Bukkit.getPlayer(player.getUniqueId());
            if (updatedPlayer == null) return;
            if (!updatedPlayer.isOnline()) return;
            Location centre = player.getLocation();
            Location spawn = new Location(centre.getWorld(),
                    centre.getX() + x,
                    centre.getY() + y,
                    centre.getZ() + z);
            centre.getWorld().spawnParticle(Particle.REDSTONE, spawn, 1,
                    new Particle.DustOptions(randomColour, 1));
        }
    }

    /**
     * Scatters particles around the player.
     *
     * @param size The size of the particles that will be spawned.
     * @param particles The particles you want to spawn around the player.
     *                  These will be randomised.
     */
    public void scatterParticles(int size, Particle... particles) {
        List<Location> locations = new ArrayList<>();
        locations.add(player.getLocation().add(0.2, 1.8, 0.3));
        locations.add(player.getLocation().add(0.3, 1.5, 0.1));
        locations.add(player.getLocation().add(0.1, 0.8, 0.3));
        locations.add(player.getLocation().add(0.1, 0.7, 0.2));
        locations.add(player.getLocation().add(0.2, 0.5, 0.1));
        for (Location loc : locations) {
            Particle particle = particles[new Random().nextInt(particles.length)];
            loc.getWorld().spawnParticle(particle, loc, size);
        }
    }

    /**
     * Draws a circle around the player with a radius and density which is
     * specified in the input.
     *
     * @param radius The radius you want this to be in blocks.
     * @param density The amount of particles you want spawned.
     * @param colours Randomised colours.
     */
    public void drawCircle(double radius, int density, Color... colours) {
        World world = player.getWorld();
        for (Location loc : this.getCircle(player.getLocation(), radius, density)) {
            Color colour = colours[new Random().nextInt(colours.length)];
            world.spawnParticle(Particle.REDSTONE, loc, 1,
                    new Particle.DustOptions(colour, 1));
        }
    }

    /**
     * Draws a cone around the player, starting from the bottom.
     *
     * @param radius The radius around the player the bottom ring will be.
     * @param density How many particles will be spawned per circle.
     * @param colours The randomised colours that the rings will be.
     */
    public void drawCone(double radius, int density, Color... colours) {
        World world = player.getWorld();
        double updatedRadius = radius;
        for (int i =  0; updatedRadius >= 0; i += 0.5) {
            for (Location loc : this.getCircle(player.getLocation(), updatedRadius, density)) {
                Color colour = colours[new Random().nextInt(colours.length)];
                world.spawnParticle(Particle.REDSTONE, loc.add(0, i, 0), 1,
                        new Particle.DustOptions(colour, 1));
            }
            updatedRadius -= 1;
        }
    }

    /**
     * Causes a dome of particles to come out of the players head.
     *
     * @param particles The particles you want the to come out of the player.
     *                  Note this cannot be redstone. Use ParticlesManager#drawDome(Color... colours)
     *                  for a dome of redstone particles.
     *
     * @throws IllegalArgumentException If any of the particles is Particle.REDSTONE.
     */
    public void drawDome(Particle... particles) {
        for (Particle particle : particles) {
            if (particle == Particle.REDSTONE) {
                throw new IllegalArgumentException("Redstone cannot be used here! Use ParticleManager#drawCone(Color... colours)!");
            }
        }
        new BukkitRunnable() {
            double t = Math.PI/64;
            final Location loc = player.getLocation();
            final World world = loc.getWorld();
            public void run() {
                t += 0.1*Math.PI;
                for (double theta = 0; theta <= 2*Math.PI; theta = theta + Math.PI/16) {
                    double x = t*Math.cos(theta);
                    double y = 2*Math.exp(-0.1*t) * Math.sin(t) + 1.5;
                    double z = t*Math.sin(theta);
                    loc.add(x,y,z);
                    Particle particle = particles[new Random().nextInt(particles.length)];
                    world.spawnParticle(particle, loc, 1);
                    loc.subtract(x,y,z);
                }
                if (t > 20) {
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    /**
     * Causes a dome of particles to come out of the players head.
     *
     * @param colours The colours you want the redstone particles that are spawned
     *                to be.
     */
    public void drawDome(Color... colours) {
        new BukkitRunnable() {
            double t = Math.PI/64;
            final Location loc = player.getLocation();
            final World world = loc.getWorld();
            public void run() {
                t += 0.1*Math.PI;
                for (double theta = 0; theta <= 2*Math.PI; theta = theta + Math.PI/16) {
                    double x = t*Math.cos(theta);
                    double y = 2*Math.exp(-0.1*t) * Math.sin(t) + 1.5;
                    double z = t*Math.sin(theta);
                    loc.add(x,y,z);
                    Color colour = colours[new Random().nextInt(colours.length)];
                    world.spawnParticle(Particle.REDSTONE, loc, 1,
                            new Particle.DustOptions(colour, 1));
                    loc.subtract(x,y,z);
                }
                if (t > 20) {
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 1);
    }

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
