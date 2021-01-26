package com.ankoki.elementals.spells.entity.poison;

import com.ankoki.elementals.Elementals;
import com.ankoki.elementals.api.EntitySpell;
import com.ankoki.elementals.managers.ParticlesManager;
import com.ankoki.elementals.managers.Spell;
import com.ankoki.elementals.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@RequiredArgsConstructor
public class CastPoison implements EntitySpell {
    private final Elementals plugin;
    private final Spell spell = new Spell("Poison", 37310, false);

    @Override
    public boolean onCast(Player player, Entity entity) {
        if (entity instanceof LivingEntity) {
            ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.POISON, 20*5, 1));
            new ParticlesManager(player, plugin).scatterParticles(3, Particle.SPELL_WITCH, Particle.END_ROD);
            player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 20*5, 1));
            return true;
        }
        Utils.sendActionBar(player, "&eYou need to look at a living entity!");
        return false;
    }

    @Override
    public int getCooldown() {
        return 10;
    }

    @Override
    public Spell getSpell() {
        return spell;
    }
}
