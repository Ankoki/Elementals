package com.ankoki.elementals.managers;

import com.ankoki.elementals.api.ElementalsAPI;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

@SuppressWarnings("unused")
public class ItemManager {
    private final ItemStack item;
    private ItemStack wand;

    public ItemManager(ItemStack item) {
        this.item = item;
        wand = item;
    }

    public void addSpell(Spell spell) {
        NBTItem wand = new NBTItem(item);
        wand.addCompound(Integer.toString(spell.getId()));
        wand.setUUID("unstackable", UUID.randomUUID());
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§6" + spell.getSpellName() + "§e Wand");
        ItemStack finWand = wand.getItem();
        finWand.setItemMeta(meta);
        this.wand = finWand;
    }

    public ItemManager removeSpell(Spell spell) {
        wand.getItemMeta().setDisplayName(null);
        NBTItem wand = new NBTItem(item);
        wand.removeKey("unstackable");
        if (wand.hasKey(Integer.toString(spell.getId()))) {
            wand.removeKey("" + spell.getId());
        }
        this.wand = wand.getItem();
        return this;
    }

    public ItemManager removeSpells() {
        wand.getItemMeta().setDisplayName(null);
        NBTItem wand = new NBTItem(item);
        wand.removeKey("unstackable");
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
