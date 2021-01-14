package com.ankoki.elementals.api;

import com.ankoki.elementals.managers.Spell;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public interface EntitySpell {

    boolean onCast(Player player, Entity entity);
    int getCooldown();
    Spell getSpell();
}
