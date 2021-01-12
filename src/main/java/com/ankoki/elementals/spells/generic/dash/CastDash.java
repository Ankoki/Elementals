package com.ankoki.elementals.spells.generic.dash;

import com.ankoki.elementals.Elementals;
import com.ankoki.elementals.ElementalsAPI;
import com.ankoki.elementals.managers.GenericSpell;
import com.ankoki.elementals.managers.ParticlesManager;
import com.ankoki.elementals.managers.Spell;
import lombok.RequiredArgsConstructor;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

@RequiredArgsConstructor
public class CastDash implements GenericSpell {
    private final Elementals plugin;

    @Override
    public boolean onCast(Player player) {
        Vector unitVector = new Vector(player.getLocation().getDirection().getX(), 0, player.getLocation().getDirection().getZ());
        unitVector.normalize();
        player.setVelocity(unitVector.multiply(2));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20*10, 2));
        new ParticlesManager(player, plugin).spawnHelix(10, Color.BLUE, Color.AQUA, Color.TEAL);
        return true;
    }

    @Override
    public int getCooldown() {
        return 30;
    }

    @Override
    public Spell getSpell() {
        Spell spell = new Spell("Dash", 3732, false);
        try {
            ElementalsAPI.addSpell(spell);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return spell;
    }
}
