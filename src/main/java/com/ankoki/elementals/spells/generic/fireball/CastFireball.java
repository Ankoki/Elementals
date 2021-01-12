package com.ankoki.elementals.spells.generic.fireball;

import com.ankoki.elementals.Elementals;
import com.ankoki.elementals.ElementalsAPI;
import com.ankoki.elementals.managers.GenericSpell;
import com.ankoki.elementals.managers.Spell;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

@RequiredArgsConstructor
public class CastFireball implements GenericSpell {
    private final Elementals plugin;
    private final Spell spell;
    @Getter
    @Setter
    private int cooldown = 10;

    public CastFireball(Elementals plugin) {
        this.plugin = plugin;
        this.spell = new Spell("Fireball", 3733, false);
        try {
            ElementalsAPI.registerSpell(plugin, spell);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean onCast(Player player) {
        Fireball fireball = player.launchProjectile(Fireball.class);
        fireball.setMetadata("elementalsSpell", new FixedMetadataValue(plugin, true));
        return true;
    }

    @Override
    public Spell getSpell() {
        return spell;
    }
}
