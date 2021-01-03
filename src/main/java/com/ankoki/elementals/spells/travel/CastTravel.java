package com.ankoki.elementals.spells.travel;

import com.ankoki.elementals.Elementals;
import com.ankoki.elementals.managers.GenericSpell;
import com.ankoki.elementals.managers.Spell;
import com.ankoki.elementals.utils.Utils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import redempt.redlib.commandmanager.Messages;
import redempt.redlib.configmanager.annotations.ConfigValue;

@RequiredArgsConstructor
public class CastTravel implements GenericSpell {
    private final Elementals plugin;
    @Getter
    @ConfigValue("travel-enabled")
    private boolean enabled = true;

    @Override
    public boolean onCast(Player player) {
        if (player.getTargetBlock(null, 10).getType().isBlock()) {
            player.teleport(player.getTargetBlock(null, 10).getLocation());
        } else {
            Utils.sendActionBar(player, Messages.msg("travel-solid"));
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
