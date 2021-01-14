package com.ankoki.elementals.spells.generic.rise;

import com.ankoki.elementals.Elementals;
import com.ankoki.elementals.api.GenericSpell;
import com.ankoki.elementals.managers.ParticlesManager;
import com.ankoki.elementals.managers.Spell;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@RequiredArgsConstructor
public class CastRise implements GenericSpell {
    private final Elementals plugin;
    private final Spell spell = new Spell("Rise", 3736, false);
    @Getter
    @Setter
    private int cooldown = 10;

    @Override
    public boolean onCast(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION,
                5*20, 1, true, false));
        new ParticlesManager(player, plugin).spawnCloud(5*20);
        return true;
    }

    @Override
    public Spell getSpell() {
        return spell;
    }
}
