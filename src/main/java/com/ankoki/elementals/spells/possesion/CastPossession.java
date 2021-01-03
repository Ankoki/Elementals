package com.ankoki.elementals.spells.possesion;

import com.ankoki.elementals.Elementals;
import com.ankoki.elementals.managers.EntitySpell;
import com.ankoki.elementals.managers.Prolonged;
import com.ankoki.elementals.managers.Spell;
import com.ankoki.elementals.utils.Utils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import redempt.redlib.commandmanager.Messages;
import redempt.redlib.configmanager.annotations.ConfigValue;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class CastPossession extends Prolonged implements EntitySpell {
    private final Elementals plugin;
    private final List<Player> casting = new ArrayList<>();

    @Override
    public boolean onCast(Player player, Entity entity) {
        if (entity instanceof Animals) {
            ((Animals) entity).setCollidable(false);
            casting.add(player);
            new BukkitRunnable() {
                @Override
                public void run() {
                    Player p = Bukkit.getPlayer(player.getUniqueId());
                    if (p == null || !player.isOnline()) {
                        casting.remove(player);
                        this.cancel();
                        return;
                    }
                    if (entity.isDead()) {
                        casting.remove(player);
                        Utils.sendActionBar(player, Messages.msg("on-stop-cast")
                                .replace("%spell%", "Possession"));
                        if (p.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                            p.removePotionEffect(PotionEffectType.INVISIBILITY);
                        }
                        this.cancel();
                        return;
                    }
                    if (!casting.contains(p)) {
                        if (p.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                            p.removePotionEffect(PotionEffectType.INVISIBILITY);
                        }
                        this.cancel();
                        return;
                    }
                    p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,
                            Integer.MAX_VALUE,
                            255));
                    entity.teleport(p.getLocation());
                    ((Animals) entity).setCollidable(false);
                }
            }.runTaskTimer(plugin, 0L, 1L);
            return true;
        }
        Utils.sendActionBar(player, "&eYou need to be looking at an animal!");
        return false;
    }

    @Override
    public void onCancel(Player player) {
        casting.remove(player);
    }

    @Override
    public int getCooldown() {
        return 10;
    }

    @Override
    public Spell getSpell() {
        return Spell.POSSESSION;
    }
}
