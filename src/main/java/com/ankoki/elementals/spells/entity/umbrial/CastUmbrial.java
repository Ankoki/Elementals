package com.ankoki.elementals.spells.entity.umbrial;

import com.ankoki.elementals.Elementals;
import com.ankoki.elementals.api.EntitySpell;
import com.ankoki.elementals.managers.ParticlesManager;
import com.ankoki.elementals.managers.Spell;
import com.ankoki.elementals.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.bukkit.Color;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

@RequiredArgsConstructor
public class CastUmbrial implements EntitySpell {
    private final Elementals plugin;
    private final Spell spell = new Spell("Umbrial", 3739, false);

    @Override
    public boolean onCast(Player player, Entity entity) {
        if (!(entity instanceof Player)) {
            Utils.sendActionBar(player, "&eYou need to be looking at a player to do this!");
            return false;
        }
        Player targeted = (Player) entity;
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 80, 1));
        new BukkitRunnable() {
            int duration = 40;
            @Override
            public void run() {
                if (Utils.canCast(player)) {
                    if (Utils.canCast(targeted)) {
                        player.teleport(targeted.getLocation());
                        new ParticlesManager(targeted, plugin).drawCone(5, 100, Color.RED, Color.SILVER);
                        duration--;
                        if (duration <= 0) {
                            player.removePotionEffect(PotionEffectType.INVISIBILITY);
                            targeted.damage(6);
                            this.cancel();
                        }
                    } else {
                        this.cancel();
                    }
                } else {
                    player.removePotionEffect(PotionEffectType.INVISIBILITY);
                    Utils.sendActionBar(player, "&eThe targeted player died, or disconnected!");
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 2L);
        return true;
    }

    @Override
    public int getCooldown() {
        return 20;
    }

    @Override
    public Spell getSpell() {
        return spell;
    }
}
