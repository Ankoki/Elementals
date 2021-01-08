package com.ankoki.elementals.spells.generic.fireball;

import com.ankoki.elementals.Elementals;
import com.ankoki.elementals.managers.GenericSpell;
import com.ankoki.elementals.managers.Spell;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

@RequiredArgsConstructor
public class CastFireball implements GenericSpell {
    private final Elementals plugin;

    @Override
    public boolean onCast(Player player) {
        Fireball fireball = player.launchProjectile(Fireball.class);
        fireball.setMetadata("elementalsSpell", new FixedMetadataValue(plugin, true));
        return true;
    }

    @Override
    public int getCooldown() {
        return 10;
    }

    @Override
    public Spell getSpell() {
        return Spell.FIREBALL;
    }
}