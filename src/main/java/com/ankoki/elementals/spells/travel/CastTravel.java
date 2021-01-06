package com.ankoki.elementals.spells.travel;

import com.ankoki.elementals.Elementals;
import com.ankoki.elementals.managers.GenericSpell;
import com.ankoki.elementals.managers.Spell;
import com.ankoki.elementals.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import redempt.redlib.commandmanager.Messages;

@SuppressWarnings("unused")
@RequiredArgsConstructor
public class CastTravel implements GenericSpell {
    private final Elementals plugin;

    @Override
    public boolean onCast(Player player) {
        if (player.getTargetBlock(null, 10).getType().isBlock()) {
            float yaw = player.getLocation().getYaw();
            float pitch = player.getLocation().getPitch();
            player.teleport(player.getTargetBlock(null, 10).getLocation());
            player.getLocation().setYaw(yaw);
            player.getLocation().setPitch(pitch);
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
