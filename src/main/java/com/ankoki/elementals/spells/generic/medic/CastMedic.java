package com.ankoki.elementals.spells.generic.medic;

import com.ankoki.elementals.Elementals;
import com.ankoki.elementals.managers.GenericSpell;
import com.ankoki.elementals.managers.Spell;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class CastMedic implements GenericSpell {
    private final Elementals plugin;

    @Override
    public boolean onCast(Player player) {
        double health = player.getHealth() + 5;
        if (health > 20) health = 20;
        player.setHealth(health);
        return true;
    }

    @Override
    public int getCooldown() {
        return 30;
    }

    @Override
    public Spell getSpell() {
        return Spell.MEDIC;
    }
}
