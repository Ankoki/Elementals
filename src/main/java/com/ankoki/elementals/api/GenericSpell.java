package com.ankoki.elementals.api;

import com.ankoki.elementals.managers.Spell;
import org.bukkit.entity.Player;

public interface GenericSpell {

    boolean onCast(Player player);
    int getCooldown();
    Spell getSpell();
}
