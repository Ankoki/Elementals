package com.ankoki.elementals.events;

import com.ankoki.elementals.managers.Spell;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ProlongedSpellCancelEvent extends Event {

    @Getter
    @Setter
    private boolean cancelled;
    @Getter
    private static final HandlerList handlerList = new HandlerList();
    @Getter
    private final Player player;
    @Getter
    private final Spell spell;
    @Getter
    private final long cooldown;
    @Getter
    private final boolean entitySpell;

    public ProlongedSpellCancelEvent(Player player, Spell spell, long cooldown, boolean entitySpell) {
        this.player = player;
        this.spell = spell;
        this.cooldown = cooldown;
        this.entitySpell = entitySpell;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}
