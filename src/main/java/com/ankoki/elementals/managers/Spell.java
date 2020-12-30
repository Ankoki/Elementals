package com.ankoki.elementals.managers;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public enum Spell {
    TRAVEL("travel", 0),
    FLOW("flow", 1);

    @Getter
    private final String spellName;
    @Getter
    private final int id;
}
