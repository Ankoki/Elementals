package com.ankoki.elementals.spells.generic.medic;

import com.ankoki.elementals.Elementals;
import com.ankoki.elementals.ElementalsAPI;
import com.ankoki.elementals.managers.GenericSpell;
import com.ankoki.elementals.managers.Spell;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class CastMedic implements GenericSpell {
    private final Elementals plugin;
    @Getter
    @Setter
    private int cooldown = 30;

    @Override
    public boolean onCast(Player player) {
        double health = player.getHealth() + 5;
        if (health > 20) health = 20;
        player.setHealth(health);
        return true;
    }

    @Override
    public Spell getSpell() {
        Spell spell = new Spell("Medic", 3735, false);
        try {
            ElementalsAPI.registerSpell(plugin, spell);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return spell;
    }
}
