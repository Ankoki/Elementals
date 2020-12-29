package com.ankoki.elementals.spells.travel;

import com.ankoki.elementals.Elementals;
import com.ankoki.elementals.managers.Castable;
import com.ankoki.elementals.managers.Spell;
import com.ankoki.elementals.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class CastTravel implements Castable {
    private final Elementals plugin;

    @Override
    public boolean onCast(Player player) {
        if (player.getTargetBlock(null, 10).getType().isBlock()) {
            player.teleport(player.getTargetBlock(null, 10).getLocation());
            Utils.sendActionBar(player, "&eYou used &7TRAVEL &eand teleported!");
        } else {
            Utils.sendActionBar(player, "&eYou can only teleport to solid blocks!");
            return false;
        }
        return true;
    }

    @Override
    public int getCooldown() {
        return 10;
    }

    @Override
    public Spell getSpell() {
        return Spell.TRAVEL;
    }
}
