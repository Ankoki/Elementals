package com.ankoki.elementals.api;

import com.ankoki.elementals.managers.Spell;
import org.bukkit.entity.Player;

public interface GenericSpell {

    default void init(){};
    boolean onCast(Player player);
    int getCooldown();
    Spell getSpell();
}
