package com.ankoki.elementals.managers;

import org.bukkit.entity.Player;

/**
 * Unused asf
 * Will implement this when i can be bothered to make the new system lol
 */
public interface Castable {

    boolean onCast(Player player);
    int getCooldown();
    Spell getSpell();
}
