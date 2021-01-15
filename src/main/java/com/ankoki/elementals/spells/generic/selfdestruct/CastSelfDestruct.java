package com.ankoki.elementals.spells.generic.selfdestruct;

import com.ankoki.elementals.Elementals;
import com.ankoki.elementals.api.GenericSpell;
import com.ankoki.elementals.managers.ParticlesManager;
import com.ankoki.elementals.managers.Spell;
import lombok.RequiredArgsConstructor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

@RequiredArgsConstructor
public class CastSelfDestruct implements GenericSpell {
    private final Elementals plugin;
    private final Spell spell = new Spell("Self Destruct", 3738, false);

    @Override
    public boolean onCast(Player player) {
        new ParticlesManager(player, plugin).drawDome(Color.ORANGE, Color.RED, Color.YELLOW);
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, Integer.MAX_VALUE));
        player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 40, 1));
        new BukkitRunnable() {
            final Location loc = player.getLocation();
            final World world = loc.getWorld();
            @Override
            public void run() {
                assert world != null; //There is no way world can be null in this situation
                world.createExplosion(loc, 5L, true);
                player.removePotionEffect(PotionEffectType.SLOW);
                player.removePotionEffect(PotionEffectType.LEVITATION);
            }
        }.runTaskLater(plugin, 40L);
        return true;
    }

    @Override
    public int getCooldown() {
        return 5;
    }

    @Override
    public Spell getSpell() {
        return spell;
    }
}