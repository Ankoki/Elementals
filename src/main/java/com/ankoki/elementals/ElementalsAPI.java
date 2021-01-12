package com.ankoki.elementals;

import com.ankoki.elementals.managers.Spell;
import com.ankoki.elementals.utils.exceptions.IdInUseException;
import com.ankoki.elementals.utils.exceptions.NameInUseException;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ElementalsAPI {

    @Getter
    private static final List<Spell> allSpells = new ArrayList<>();

    public static void addSpell(Spell... spells) throws IdInUseException, NameInUseException {
        for (Spell newSpell : spells) {
            for (Spell spell : allSpells) {
                if (spell.getId() == newSpell.getId()) {
                    throw new IdInUseException();
                }
                if (spell.getSpellName().equalsIgnoreCase(newSpell.getSpellName())) {
                    throw new NameInUseException();
                }
            }
        }
        allSpells.addAll(Arrays.asList(spells));
    }

    public static Spell valueOf(String s) {
        for (Spell listSpell : ElementalsAPI.allSpells) {
            if (listSpell.getSpellName().equalsIgnoreCase(s)) {
                return listSpell;
            }
        }
        return null;
    }
}
