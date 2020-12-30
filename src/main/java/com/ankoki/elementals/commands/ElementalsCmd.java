package com.ankoki.elementals.commands;

import com.ankoki.elementals.managers.ItemManager;
import com.ankoki.elementals.managers.Spell;
import com.ankoki.elementals.utils.Utils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ElementalsCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (sender.hasPermission("elementals.admin") || sender.isOp()) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                ItemStack heldItem = player.getInventory().getItemInMainHand();
                if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("add")) {
                        if (heldItem.getType() != Material.AIR) {
                            ItemManager wand = new ItemManager(heldItem);
                            Spell spell = Spell.valueOf(args[1].toUpperCase());
                            if (spell != null) {
                                wand.addSpell(spell);
                                player.getInventory().setItem(player.getInventory().getHeldItemSlot(), wand.getItem());
                                player.sendMessage(Utils.colouredPrefix("You have enchanted this wand with " + spell.getSpellName()));
                            } else {
                                player.sendMessage(Utils.colouredPrefix("This isn't a valid spell!"));
                            }
                        } else {
                            player.sendMessage(Utils.colouredPrefix("You need to be holding an item!"));
                        }
                    } else if (args[0].equalsIgnoreCase("remove")) {
                        if (heldItem.getType() != Material.AIR) {
                            ItemManager wand = new ItemManager(heldItem);
                            Spell spell = Spell.valueOf(args[1].toUpperCase());
                            if (spell != null) {
                                wand.removeSpell(spell);
                                player.getInventory().setItem(player.getInventory().getHeldItemSlot(), wand.getItem());
                                player.sendMessage(Utils.colouredPrefix("You have removed " + spell.getSpellName() + " from this wand!"));
                            } else {
                                player.sendMessage(Utils.colouredPrefix("This isn't a valid spell!"));
                            }
                        } else {
                            player.sendMessage(Utils.colouredPrefix("You need to be holding an item!"));
                        }
                    } else {
                        player.sendMessage(Utils.colouredPrefix("This isn't a valid command!"));
                    }
                } else {
                    player.sendMessage(Utils.colouredPrefix("This isn't a valid command!"));
                }
            } else {
                sender.sendMessage(Utils.colouredPrefix("You need to be a player to do this!"));
            }
        } else {
            sender.sendMessage(Utils.colouredPrefix("This command doesn't exist!"));
        }
        return true;
    }

}
