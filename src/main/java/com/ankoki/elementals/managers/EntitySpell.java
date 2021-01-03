package com.ankoki.elementals.managers;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public interface EntitySpell {

    boolean onCast(Player player, Entity entity);
    int getCooldown();
    Spell getSpell();
}
