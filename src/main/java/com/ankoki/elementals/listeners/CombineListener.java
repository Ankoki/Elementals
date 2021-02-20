package com.ankoki.elementals.listeners;

import com.ankoki.elementals.Elementals;
import com.ankoki.elementals.api.ElementalsAPI;
import com.ankoki.elementals.events.BookEnchantEvent;
import com.ankoki.elementals.managers.ItemManager;
import com.ankoki.elementals.managers.ParticlesManager;
import com.ankoki.elementals.managers.Spell;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import redempt.redlib.commandmanager.Messages;

public class CombineListener implements Listener {

    @EventHandler
    private void combineListener(InventoryClickEvent e) {
        ItemStack clicked = e.getCurrentItem();
        ItemStack cursor = e.getCursor();
        if (!(e.getWhoClicked() instanceof Player) ||
                e.getClick() != ClickType.LEFT ||
                cursor == null ||
                clicked == null ||
                cursor.getType() == Material.AIR ||
                clicked.getType() == Material.AIR) return;
        Player player = (Player) e.getWhoClicked();
        if (clicked.getAmount() != 1) {
            player.sendMessage(Messages.msg("enchant-one-item"));
            return;
        }
        if (player.getGameMode() != GameMode.SURVIVAL) return;
        NBTItem nbtItem = new NBTItem(cursor);
        if (nbtItem.getString("spell") == null ||
                nbtItem.getString("spell").isEmpty()) return;
        if (!clicked.getType().isItem()) {
            player.sendMessage(Messages.msg("only-enchant-items"));
            return;
        }
        String spellName = nbtItem.getString("spell");
        Spell spell = ElementalsAPI.valueOf(spellName);
        if (spell == null) return;
        BookEnchantEvent event = new BookEnchantEvent(player, spell);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return;
        ItemManager managerItem = new ItemManager(clicked);
        if (managerItem.hasSpell()) {
            player.sendMessage(Messages.msg("already-got-spell"));
            return;
        }
        managerItem.addSpell(spell);
        e.getInventory().remove(clicked);
        e.getInventory().remove(cursor);
        e.getCursor().setType(Material.AIR);
        e.setCurrentItem(null);
        player.setItemOnCursor(null);
        player.getInventory().setItem(e.getSlot(), managerItem.getItem());
        new ParticlesManager(player, Elementals.getInstance()).spawnRings(2, true,
                Color.ORANGE,
                Color.YELLOW,
                Color.RED);
        player.sendMessage(Messages.msg("successful-book-enchant").replace("%spell%", spellName));
    }
}
