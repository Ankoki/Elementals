package com.ankoki.elementals.commands;

import com.ankoki.elementals.Elementals;
import com.ankoki.elementals.ElementalsAPI;
import com.ankoki.elementals.managers.ItemManager;
import com.ankoki.elementals.managers.ParticlesManager;
import com.ankoki.elementals.managers.Spell;
import com.ankoki.elementals.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.bukkit.Color;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import redempt.redlib.commandmanager.CommandHook;
import redempt.redlib.commandmanager.Messages;

@SuppressWarnings("unused")
@RequiredArgsConstructor
public class ElementalsCmd {
    private final Elementals plugin;

    @CommandHook("elementals")
    public void elementalsHook(Player sender) {
        sender.sendMessage(Messages.msg("command-message").replace("%version%", plugin.getDescription().getVersion()));
    }

    @CommandHook("enchant")
    public void enchantHook(Player player, Spell spell, ItemStack heldItem) {
        if (heldItem.getType().isItem()) {
            ItemManager wand = new ItemManager(heldItem);
            wand.removeSpells();
            wand.addSpell(spell);
            player.getInventory().setItem(player.getInventory().getHeldItemSlot(), wand.getItem());
            player.sendMessage(Messages.msg("on-enchant").replace("%spell%", spell.getSpellName()));
        } else {
            player.sendMessage(Messages.msg("enchant-blocks"));
        }
    }

    @CommandHook("disenchant")
    public void disenchantHook(Player player, ItemStack heldItem) {
        ItemManager wand = new ItemManager(heldItem);
        if (!wand.hasSpell()) {
            player.sendMessage(Messages.msg("no-spells"));
            return;
        }
        player.getInventory().setItem(player.getInventory().getHeldItemSlot(), wand.removeSpells().getItem());
        player.sendMessage(Messages.msg("on-disenchant"));
    }

    @CommandHook("information")
    public void informationHook(CommandSender sender) {
        sender.sendMessage(Utils.coloured("&6৺  &e&nElementals\n" + "\n" +
                            "&7    Elementals is a plugin which is developed for\n" +
                            "&7    anyone who enjoys the magical side of anything.\n" +
                            "&7    We have multiple spells which players can use and\n" +
                            "&7    we will continue to add ways to give them to people,\n" +
                            "&7    and different spells, primarily based on user feedback."));
    }

    @CommandHook("reload")
    public void reloadHook(CommandSender sender) {
        Messages.load(plugin);
        plugin.getConfigManager().load();
        sender.sendMessage(Messages.msg("on-reload"));
    }

    @CommandHook("test")
    public void testHook(Player player) {
        new ParticlesManager(player, plugin).spawnHelix(2,
                Color.AQUA, Color.BLUE, Color.TEAL);
        for (Spell spell : ElementalsAPI.getAllSpells()) {
            System.out.println(spell.getSpellName());
        }
        System.out.println("All spells printed");
        player.sendMessage("Spawning helix!");
    }
}
