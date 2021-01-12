package com.ankoki.elementals.spells.generic.travel;

import com.ankoki.elementals.Elementals;
import com.ankoki.elementals.ElementalsAPI;
import com.ankoki.elementals.managers.GenericSpell;
import com.ankoki.elementals.managers.ParticlesManager;
import com.ankoki.elementals.managers.Spell;
import com.ankoki.elementals.utils.Utils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import redempt.redlib.commandmanager.Messages;

@SuppressWarnings("unused")
@RequiredArgsConstructor
public class CastTravel implements GenericSpell {
    private final Elementals plugin;
    @Getter
    @Setter
    private int cooldown = 10;

    @Override
    public boolean onCast(Player player) {
        if (player.getTargetBlock(null, 10).getType().isBlock()) {
            float yaw = player.getLocation().getYaw();
            float pitch = player.getLocation().getPitch();
            Location loc = player.getTargetBlock(null, 10).getLocation();
            new ParticlesManager(player, plugin).spawnRings(1, true, Color.WHITE, Color.GRAY, Color.BLACK);
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                Player updatedPlayer = Bukkit.getPlayer(player.getUniqueId());
                if (updatedPlayer == null || !player.isOnline()) return;
                loc.setPitch(updatedPlayer.getLocation().getPitch());
                loc.setYaw(updatedPlayer.getLocation().getYaw());
                player.teleport(loc);
                player.getLocation().setYaw(yaw);
                player.getLocation().setPitch(pitch);
            }, 20L);
        } else {
            Utils.sendActionBar(player, Messages.msg("travel-solid"));
            return false;
        }
        return true;
    }

    @Override
    public Spell getSpell() {
        Spell spell = new Spell("Travel", 3730, false);
        try {
            ElementalsAPI.registerSpell(plugin, spell);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return spell;
    }
}
