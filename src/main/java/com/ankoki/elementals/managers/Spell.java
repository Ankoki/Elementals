package com.ankoki.elementals.managers;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Spell {
    TRAVEL("Travel", 3730),
    FLOW("Flow", 3731),
    RISE("Rise", 3732);

    @Getter
    private final String spellName;
    @Getter
    private final int id;
}
