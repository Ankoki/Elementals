package com.ankoki.elementals.managers;

import de.tr7zw.changeme.nbtapi.NBTItem;
import lombok.RequiredArgsConstructor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@RequiredArgsConstructor
public class ItemManager {
    private final ItemStack item;
    private ItemStack wand = item;
    private ItemMeta meta = item.getItemMeta();

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
        return this;
    }

    public boolean hasSpell(Spell spell) {
        return new NBTItem(item).hasKey(spell.getSpellName());
    }

    public ItemStack getItem() {
        return wand;
    }
}
