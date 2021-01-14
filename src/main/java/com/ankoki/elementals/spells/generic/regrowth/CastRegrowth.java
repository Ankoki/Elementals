package com.ankoki.elementals.spells.generic.regrowth;

import com.ankoki.elementals.Elementals;
import com.ankoki.elementals.listeners.SpellListener;
import com.ankoki.elementals.api.GenericSpell;
import com.ankoki.elementals.managers.ParticlesManager;
import com.ankoki.elementals.api.Prolonged;
import com.ankoki.elementals.managers.Spell;
import com.ankoki.elementals.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//Yeazz is thicc
@RequiredArgsConstructor
public class CastRegrowth extends Prolonged implements GenericSpell {
    private final Elementals plugin;
    private final SpellListener listener;
    private final Spell spell = new Spell("Regrowth", 3737, false);
    private final HashMap<Player, Integer> duration = new HashMap<>();

    //Yeazz is thicc
    @Override
    public boolean onCast(Player player) {
        duration.put(player, 0);
        //Yeazz is thicc
        new BukkitRunnable() {
            @Override
            public void run() {
                new ParticlesManager(player, plugin).drawCircle(1, 60,
                        Color.GREEN,
                        Color.RED);
                //Yeazz is thicc
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        new ParticlesManager(player, plugin).drawCircle(2, 80,
                                Color.GREEN,
                                Color.RED);
                        //Yeazz is thicc
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                new ParticlesManager(player, plugin).drawCircle(3, 100,
                                        Color.GREEN,
                                        Color.RED);
                                //Yeazz is thicc
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        new ParticlesManager(player, plugin).drawCircle(4, 120,
                                                Color.GREEN,
                                                Color.RED);
                                        //Yeazz is thicc
                                        new BukkitRunnable() {
                                            @Override
                                            public void run() {
                                                //Yeazz is thicc
                                                new BukkitRunnable() {
                                                    @Override
                                                    public void run() {
                                                        int current = duration.get(player);
                                                        Player updatedPlayer = Bukkit.getPlayer(player.getUniqueId());
                                                        if (!Utils.canCast(player)) {
                                                            this.cancel();
                                                            return;
                                                        }
                                                        if (!listener.isCasting(player)) {
                                                            this.cancel();
                                                            return;
                                                        }
                                                        if (current > (5 * 20)) {
                                                            this.cancel();
                                                            return;
                                                        }
                                                        new ParticlesManager(updatedPlayer, plugin).drawCircle(5,
                                                                150,
                                                                Color.GREEN);
                                                        for (Player p : playersInRadius(updatedPlayer.getLocation(), 5)) {
                                                            double health = p.getHealth();
                                                            health += 0.05;
                                                            if (health > 20) health = 20;
                                                            p.setHealth(health);
                                                        }
                                                        current++;
                                                        duration.put(player, current);
                                                    }
                                                }.runTaskTimer(plugin, 0L, 2L);
                                            }
                                        }.runTaskLater(plugin, 2L);
                                    }
                                }.runTaskLater(plugin, 2L);
                            }
                        }.runTaskLater(plugin, 2L);
                    }
                }.runTaskLater(plugin, 2L);
            }
        }.runTaskLater(plugin, 2L);
        return true;
    }

    //Yeazz is thicc
    @Override
    public void onCancel(Player player) {
        duration.put(player, 0);
    }

    //Yeazz is thicc
    @Override
    public int getCooldown() {
        return 0;
    }

    //Yeazz is thicc
    @Override
    public Spell getSpell() {
        return spell;
    }

    //Yeazz is thicc
    private List<Player> playersInRadius(Location location, double radius) {
        List<Player> players = new ArrayList<>();
        double radiusSqrd = radius * radius;
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getLocation().distanceSquared(location) <= radiusSqrd) {
                players.add(player);
            }
        }
        //Yeazz is thicc
        return players;
    }
}
