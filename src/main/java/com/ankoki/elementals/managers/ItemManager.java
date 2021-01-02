package com.ankoki.elementals.managers;

import de.tr7zw.changeme.nbtapi.NBTItem;
import lombok.RequiredArgsConstructor;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class ItemManager {
    private final ItemStack item;
    private ItemStack wand;

    public ItemManager addSpell(Spell spell) {
        NBTItem wand = new NBTItem(item);
        wand.addCompound(spell.getSpellName());
        this.wand = wand.getItem();
        return this;
    }

    public ItemManager removeSpell(Spell spell) {
        NBTItem wand = new NBTItem(item);
        if (wand.hasKey(spell.getSpellName())) {
            wand.removeKey(spell.getSpellName());
        }
        this.wand = wand.getItem();
        return this;
    }

    public ItemManager removeSpells() {
        NBTItem wand = new NBTItem(item);
        for (Spell spell : Spell.values()) {
            if (wand.hasKey(spell.getSpellName())) {
                wand.removeKey(spell.getSpellName());
            }
        }
        this.wand = wand.getItem();
        return this;
    }

    public boolean hasSpell() {
        NBTItem wand = new NBTItem(item);
        for (Spell spell : Spell.values()) {
            if (wand.hasKey(spell.getSpellName())) {
                return true;
            }
        }
        return false;
    }

    public boolean hasSpell(Spell spell) {
        return new NBTItem(item).hasKey(spell.getSpellName());
    }

    public ItemStack getItem() {
        return wand;
    }
}
