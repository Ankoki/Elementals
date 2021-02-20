package com.ankoki.elementals.events;

import com.ankoki.elementals.managers.Spell;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BookEnchantEvent extends Event implements Cancellable {

    @Getter
    @Setter
    private boolean cancelled;
    @Getter
    private static final HandlerList handlerList = new HandlerList();
    @Getter
    private final Player player;
    @Getter
    private final Spell spell;

    public BookEnchantEvent(Player player, Spell spell) {
        this.player = player;
        this.spell = spell;
    }


    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}
