package com.ankoki.elementals.managers;

import com.ankoki.elementals.Elementals;
import com.ankoki.elementals.ElementalsAPI;
import lombok.Getter;

public class Spell {
    @Getter
    private final String spellName;
    @Getter
    private final int id;
    @Getter
    private final boolean prolonged;

    public Spell(String spellName, int id, boolean prolonged) {
        this.spellName = spellName;
        this.id = id;
        this.prolonged = prolonged;
        try {
            ElementalsAPI.registerSpell(Elementals.getInstance(), this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}