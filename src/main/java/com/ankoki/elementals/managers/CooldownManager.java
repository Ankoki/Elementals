package com.ankoki.elementals.managers;

import org.bukkit.entity.Player;

import java.util.WeakHashMap;

public final class CooldownManager {
    private final WeakHashMap<Player, WeakHashMap<Spell, Long>> cooldown = new WeakHashMap<>();

    public void addCooldown(Player player, Spell spell) {
        WeakHashMap<Spell, Long> spellCooldown = cooldown.get(player) == null ?
                new WeakHashMap<>() : cooldown.get(player);
        spellCooldown.put(spell, System.currentTimeMillis());
        cooldown.put(player, spellCooldown);
    }

    public boolean canCast(Player player, Spell spell, long duration) {
        if (!cooldown.containsKey(player)) return true;
        WeakHashMap<Spell, Long> spellCooldown = cooldown.get(player);
        if (!spellCooldown.containsKey(spell)) return true;
        return ((System.currentTimeMillis() - spellCooldown.get(spell)) >= (duration * 1000L));
    }
}
