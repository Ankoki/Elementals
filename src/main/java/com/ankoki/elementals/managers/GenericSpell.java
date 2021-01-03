package com.ankoki.elementals.managers;

import org.bukkit.entity.Player;

public interface GenericSpell {

    boolean onCast(Player player);
    int getCooldown();
    Spell getSpell();
    boolean isEnabled();
}
