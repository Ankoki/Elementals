package com.ankoki.elementals.spells.possesion;

import com.ankoki.elementals.Elementals;
import com.ankoki.elementals.listeners.SpellListener;
import com.ankoki.elementals.managers.EntitySpell;
import com.ankoki.elementals.managers.Prolonged;
import com.ankoki.elementals.managers.Spell;
import com.ankoki.elementals.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import redempt.redlib.commandmanager.Messages;

@RequiredArgsConstructor
public class CastPossession extends Prolonged implements EntitySpell {
    private final Elementals plugin;
    private final SpellListener listener;

    @Override
    public boolean onCast(Player player, Entity entity) {
        if (entity instanceof Animals) {
            ((Animals) entity).getCollidableExemptions().add(player.getUniqueId());
            player.getCollidableExemptions().add(entity.getUniqueId());
            new BukkitRunnable() {
                @Override
                public void run() {
                    Player p = Bukkit.getPlayer(player.getUniqueId());
                    if (p == null || !player.isOnline()) {
                        if (player.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                            player.removePotionEffect(PotionEffectType.INVISIBILITY);
                        }
                        listener.removeCaster(player);
                        this.cancel();
                        return;
                    }
                    if (entity.isDead()) {
                        listener.removeCaster(player);
                        Utils.sendActionBar(player, Messages.msg("on-stop-cast")
                                .replace("%spell%", "Possession"));
                        if (p.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                            p.removePotionEffect(PotionEffectType.INVISIBILITY);
                        }
                        this.cancel();
                        return;
                    }
                    if (!listener.isCasting(p)) {
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
    public int getCooldown() {
        return 10;
    }

    @Override
    public Spell getSpell() {
        return Spell.POSSESSION;
    }
}
