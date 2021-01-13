package com.ankoki.elementals.spells.generic.dash;

import com.ankoki.elementals.Elementals;
import com.ankoki.elementals.managers.GenericSpell;
import com.ankoki.elementals.managers.ParticlesManager;
import com.ankoki.elementals.managers.Spell;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

@RequiredArgsConstructor
public class CastDash implements GenericSpell {
    private final Elementals plugin;
    private final Spell spell = new Spell("Dash", 3732, false);
    @Getter
    @Setter
    private int cooldown = 20;

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
    public Spell getSpell() {
        return spell;
    }
}
