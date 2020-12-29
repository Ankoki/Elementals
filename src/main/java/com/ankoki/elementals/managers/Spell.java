package com.ankoki.elementals.managers;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Spell {
    TRAVEL("Travel", 0),
    FLOW("Flow", 1);

    @Getter
    private final String spellName;
    @Getter
    private final int id;
}
