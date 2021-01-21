package com.ankoki.elementals.managers;

import com.ankoki.elementals.utils.Lore;
import com.ankoki.elementals.utils.Utils;
import de.tr7zw.changeme.nbtapi.NBTItem;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

@RequiredArgsConstructor
public class BookManager {
    private final Spell spell;
    private final UUID uuid = UUID.randomUUID();

    public ItemStack getBook() {
        ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Utils.coloured("&c❤ &8" + spell.getSpellName()));
        meta.setLore(new Lore("&8♦ ",
                "&8» &9Enchant your item's with this book!",
                "&8» &9Drag and drop this book onto the wanted item!",
                "&8» &9This book will give your item " + spell.getSpellName() + "&9!",
                "&8♦")
                .getL());
        item.setItemMeta(meta);
        NBTItem nbtItem = new NBTItem(item);
        nbtItem.setUUID("unstackable", uuid);
        nbtItem.setString("spell", spell.getSpellName());
        return nbtItem.getItem();
    }
}
