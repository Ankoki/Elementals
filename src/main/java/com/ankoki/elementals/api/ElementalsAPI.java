package com.ankoki.elementals.api;

import com.ankoki.elementals.Elementals;
import com.ankoki.elementals.managers.Spell;
import com.ankoki.elementals.utils.exceptions.IdInUseException;
import com.ankoki.elementals.utils.exceptions.NameInUseException;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.WeakHashMap;

@SuppressWarnings("unused")
public final class ElementalsAPI {

    @Getter
    private static final List<Spell> allSpells = new ArrayList<>();
    @Getter
    private static final List<GenericSpell> genericSpells = new ArrayList<>();
    @Getter
    private static final List<EntitySpell> entitySpells = new ArrayList<>();
    private static final WeakHashMap<Player, Spell> castingSpell = new WeakHashMap<>();

    private ElementalsAPI() throws IllegalAccessException {
        throw new IllegalAccessException("You cannot make a new instance of this class!");
    }

    /**
     * Registers the spell which is used in a plugin
     *
     * @param plugin The owning plugin that is registering this spell.
     * @param spells Spell instances you want to register.
     * @throws IdInUseException   If the ID of the spell is in use, this is thrown.
     * @throws NameInUseException If the name of the spell is in use (including case), this is thrown.
     */
    public static void registerSpell(JavaPlugin plugin, Spell... spells)
            throws IdInUseException, NameInUseException {
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
     * @param plugin        The main class instance
     * @param genericSpells The generic spells you want to register
     */
    public static void registerGenericSpells(JavaPlugin plugin, GenericSpell... genericSpells) {
        ElementalsAPI.genericSpells.addAll(Arrays.asList(genericSpells));
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
     * @param plugin       The main class instance
     * @param entitySpells The entity spells you want to register
     */
    public static void registerEntitySpells(JavaPlugin plugin, EntitySpell... entitySpells) {
        ElementalsAPI.entitySpells.addAll(Arrays.asList(entitySpells));
        String owningPl = plugin.getDescription().getName();
        if (entitySpells.length == 1) {
            System.out.println(owningPl + " has successfully registered 1 new EntitySpell!");
        } else {
            System.out.println(owningPl + " has successfully registered " + entitySpells.length + " new EntitySpells!");
        }
    }

    /**
     * If a player is casting, it will stop the player from casting.
     *
     * @param player The player you want to stop casting.
     */
    public static void removeCaster(Player player) {
        castingSpell.remove(player);
    }

    /**
     * Adds a player to the casting list.
     *
     * @param player The player you want to mark as casting.
     * @param spell The spell the player is casting.
     */
    public static void addCaster(Player player, Spell spell) {
        castingSpell.put(player, spell);
    }

    /**
     * Returns wether or not the provided player is casting.
     *
     * @param player The player you want to see if they are casting
     * @return True if the player is casting, false if not.
     */
    public static boolean isCasting(Player player) {
        return castingSpell.containsKey(player);
    }

    /**
     * Returns if the spell a player is casting is prolonged.
     *
     * @param player The player you want to check if their spell is prolonged.
     * @return If the player's casted spell is prolonged.
     */
    public static boolean isCastingProlonged(Player player) {
        try {
            return castingSpell.get(player).isProlonged();
        } catch (Exception ex) { return false; }
    }

    /**
     * Returns the spell the player is casting, if they are casting a spell.
     *
     * @param player The player you want to get the spell of.
     * @return The spell the player is casting. Null if they are not casting a spell.
     */
    public static Spell getCastedSpell(Player player) {
        return castingSpell.get(player);
    }
}
