package com.ankoki.elementals.managers;

import com.ankoki.elementals.api.ElementalsAPI;
import de.tr7zw.changeme.nbtapi.NBTItem;
import lombok.RequiredArgsConstructor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

@SuppressWarnings("unused")
@RequiredArgsConstructor
public class ItemManager {
    private final ItemStack item;
    private ItemStack wand;

    public ItemManager addSpell(Spell spell) {
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName("§6" + spell.getSpellName() + " §eWand");
        item.setItemMeta(meta);
        NBTItem wand = new NBTItem(item);
        wand.addCompound(Integer.toString(spell.getId()));
        wand.setUUID("unstackable", UUID.randomUUID());
        this.wand = wand.getItem();
        return this;
    }

    public ItemManager removeSpell(Spell spell) {
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(null);
        item.setItemMeta(meta);
        NBTItem wand = new NBTItem(item);
        if (wand.hasKey(Integer.toString(spell.getId()))) {
            wand.removeKey("" + spell.getId());
            wand.setUUID("unstackable", null);
        }
        this.wand = wand.getItem();
        return this;
    }

    public ItemManager removeSpells() {
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(null);
        item.setItemMeta(meta);
        NBTItem wand = new NBTItem(item);
        for (Spell spell : ElementalsAPI.getAllSpells()) {
            if (wand.hasKey(Integer.toString(spell.getId()))) {
                wand.removeKey(Integer.toString(spell.getId()));
            }
            wand.setUUID("unstackable", null);
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