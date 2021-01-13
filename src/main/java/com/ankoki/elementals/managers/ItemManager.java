package com.ankoki.elementals.managers;

import com.ankoki.elementals.ElementalsAPI;
import de.tr7zw.changeme.nbtapi.NBTItem;
import lombok.RequiredArgsConstructor;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("unused")
@RequiredArgsConstructor
public class ItemManager {
    private final ItemStack item;
    private ItemStack wand;

    public void addSpell(Spell spell) {
        NBTItem wand = new NBTItem(item);
        wand.addCompound(Integer.toString(spell.getId()));
        this.wand = wand.getItem();
    }

    public ItemManager removeSpell(Spell spell) {
        NBTItem wand = new NBTItem(item);
        if (wand.hasKey(Integer.toString(spell.getId()))) {
            wand.removeKey("" + spell.getId());
        }
        this.wand = wand.getItem();
        return this;
    }

    public ItemManager removeSpells() {
        NBTItem wand = new NBTItem(item);
        for (Spell spell : ElementalsAPI.getAllSpells()) {
            if (wand.hasKey(Integer.toString(spell.getId()))) {
                wand.removeKey(Integer.toString(spell.getId()));
            }
        }
        this.wand = wand.getItem();
        return this;
    }

    public boolean hasSpell() {
        NBTItem wand = new NBTItem(item);
        for (Spell spell : ElementalsAPI.getAllSpells()) {
            if (wand.hasKey(Integer.toString(spell.getId()))) {
                return true;
            }
        }
        return false;
    }

    public boolean hasSpell(Spell spell) {
        return new NBTItem(item).hasKey(Integer.toString(spell.getId()));
    }

    public ItemStack getItem() {
        return wand;
    }
}
