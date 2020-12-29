package com.ankoki.elementals.managers;

import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.inventory.ItemStack;

public class ItemManager {

    public static ItemStack addSpell(ItemStack item, Spell spell) {
        NBTItem wand = new NBTItem(item);
        wand.addCompound(spell.getSpellName());
        return wand.getItem();
    }

    public static boolean hasSpell(ItemStack item, Spell spell) {
        return new NBTItem(item).hasKey(spell.getSpellName());
    }
}
