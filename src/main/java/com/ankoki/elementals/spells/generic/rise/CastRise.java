package com.ankoki.elementals.spells.generic.rise;

import com.ankoki.elementals.Elementals;
import com.ankoki.elementals.managers.GenericSpell;
import com.ankoki.elementals.managers.ParticlesManager;
import com.ankoki.elementals.managers.Spell;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@RequiredArgsConstructor
public class CastRise implements GenericSpell {
    private final Elementals plugin;

    @Override
    public boolean onCast(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION,
                5*20, 1, true, false));
        new ParticlesManager(player, plugin).spawnCloud(5*20);
        return true;
    }

    @Override
    public int getCooldown() {
        return 15;
    }

    @Override
    public Spell getSpell() {
        return Spell.RISE;
    }
}
