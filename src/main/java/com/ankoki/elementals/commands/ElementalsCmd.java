package com.ankoki.elementals.commands;

import com.ankoki.elementals.Elementals;
import com.ankoki.elementals.api.ElementalsAPI;
import com.ankoki.elementals.managers.BookManager;
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

    @CommandHook("help")
    public void helpHook(CommandSender sender) {
        sender.sendMessage(Utils.coloured("&6৺  &e&nElementals Help\n" + "\n" +
                "&7/elementals:" +
                "&7    enchant <spell> &8- &7Enchants held item with the said spell" +
                "&7    disenchant &8- &7Disenchants the held item" +
                "&7    information &8- &7Information about the plugin" +
                "&7    help &8- &7Your here! Shows this message" +
                "&7    book <spell> &8- &7Gives you an enchanted book you can combine with an item."));
    }

    @CommandHook("book")
    public void bookHook(Player player, Spell spell) {
        player.getInventory().addItem(new BookManager(spell).getBook());
        player.sendMessage(Utils.coloured("&6৺ &eYou have recieved a " + spell.getSpellName() + " book!"));
    }

    @CommandHook("spells")
    public void spellsHook(CommandSender sender) {
        sender.sendMessage(Utils.coloured("&6৺ &eWe currently have the following spells!"));
        for (Spell spell : ElementalsAPI.getAllSpells()) {
            sender.sendMessage(Utils.coloured("&8» &7" + spell.getSpellName()));
        }
    }
}
