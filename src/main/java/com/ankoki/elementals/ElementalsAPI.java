package com.ankoki.elementals;

import com.ankoki.elementals.managers.EntitySpell;
import com.ankoki.elementals.managers.GenericSpell;
import com.ankoki.elementals.managers.Spell;
import com.ankoki.elementals.utils.exceptions.IdInUseException;
import com.ankoki.elementals.utils.exceptions.NameInUseException;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ElementalsAPI {

    @Getter
    private static final List<Spell> allSpells = new ArrayList<>();
    @Getter
    private static final List<GenericSpell> genericSpells = new ArrayList<>();
    @Getter
    private static final List<EntitySpell> entitySpells = new ArrayList<>();

    /**
     * Registers the spell which is used in a plugin
     *
     * @param plugin The owning plugin that is registering this spell.
     * @param spells Spell instances you want to register.
     * @throws IdInUseException If the ID of the spell is in use, this is thrown.
     * @throws NameInUseException If the name of the spell is in use (including case), this is thrown.
     */
    public static void registerSpell(JavaPlugin plugin, Spell... spells)
            throws IdInUseException, NameInUseException {
        for (Spell newSpell : spells) {
            for (Spell spell : allSpells) {
                if (spell.getId() == newSpell.getId()) {
                    throw new IdInUseException();
                    //System.out.println("apparently in use " + spell.getId());
                    //System.out.println("apparently it equals this" + spell.getId() + " this is the spell " +spell.getSpellName());
                }
                if (spell.getSpellName().equalsIgnoreCase(newSpell.getSpellName())) {
                    throw new NameInUseException();
                    //System.out.println("appartenly in use " + spell.getSpellName());
                    //System.out.println("apparently it equals this" + spell.getId() + " this is the spell " +spell.getSpellName());
                }
            }
        }
        allSpells.addAll(Arrays.asList(spells));
        if (plugin == Elementals.getInstance()) return;
        String owningPl = plugin.getDescription().getName();
        for (Spell newSpell : spells) {
            System.out.println(owningPl + " has registered the spell " + newSpell.getSpellName() + "!");
        }
        if (spells.length == 1) {
            System.out.println(owningPl + " has successfully registered 1 spell!");
        } else {
            System.out.println(owningPl + " has successfully registered " + spells.length + " spells!");
        }
    }

    /**
     * Gets the instance of a spell based on the spells name.
     *
     * @param spellName Name of the spell you want to find.
     * @return Spell that has the matching spell name.
     */
    public static Spell valueOf(String spellName) {
        for (Spell listSpell : ElementalsAPI.getAllSpells()) {
            if (listSpell.getSpellName().equalsIgnoreCase(spellName)) {
                return listSpell;
            }
        }
        return null;
    }

    /**
     * This registers your spells and takes in your plugin instance and multiple GenericSpells
     *
     * @param plugin The main class instance
     * @param genericSpells The generic spells you want to register
     */
    public static void registerGenericSpells(JavaPlugin plugin, GenericSpell... genericSpells) {
        ElementalsAPI.genericSpells.addAll(Arrays.asList(genericSpells));
        //if (plugin == Elementals.getInstance()) return;
        String pluginName = plugin.getDescription().getName();
        if (genericSpells.length == 1) {
            System.out.println(pluginName + " has successfully registered 1 new GenericSpell!");
        } else {
            System.out.println(pluginName + " has successfully registered " + genericSpells.length + " new GenericSpells!");
        }
    }

    /**
     * This registers your spells and takes in your plugin instance and multiple GenericSpells
     *
     * @param plugin The main class instance
     * @param entitySpells The entity spells you want to register
     */
    public static void registerEntitySpells(JavaPlugin plugin, EntitySpell... entitySpells) {
        ElementalsAPI.entitySpells.addAll(Arrays.asList(entitySpells));
        //if (plugin == Elementals.getInstance()) return;
        String owningPl = plugin.getDescription().getName();
        if (entitySpells.length == 1) {
            System.out.println(owningPl + " has successfully registered 1 new EntitySpell!");
        } else {
            System.out.println(owningPl + " has successfully registered " + entitySpells.length + " new EntitySpells!");
        }
    }

}
