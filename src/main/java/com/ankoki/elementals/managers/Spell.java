package com.ankoki.elementals.managers;

import com.ankoki.elementals.ElementalsAPI;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Spell {
    @Getter
    private String spellName;
    @Getter
    private int id;
    @Getter
    private boolean prolonged;

    public Spell(String spellName, int id, boolean prolonged) {
        this.spellName = spellName;
        this.id = id;
        this.prolonged = prolonged;
        try {
            ElementalsAPI.addSpell(this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
