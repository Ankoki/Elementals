package com.ankoki.elementals.managers;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Spell {
    TRAVEL("Travel", 3730, false),
    FLOW("Flow", 3731, true),
    RISE("Rise", 3732, false),
    FIREBALL("Fireball", 3733, false),
    POSSESSION("Possession", 3734, true);

    @Getter
    private final String spellName;
    @Getter
    private final int id;
    @Getter
    private final boolean isProlonged;
}
