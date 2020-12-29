package com.ankoki.elementals.commands;

import com.ankoki.elementals.managers.ItemManager;
import com.ankoki.elementals.managers.Spell;
import com.ankoki.elementals.utils.Utils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ElementalsCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (sender.hasPermission("elementals.admin") || sender.isOp()) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.getInventory().getItemInMainHand().getType() != Material.AIR) {
                    player.getInventory().setItem(player.getInventory().getHeldItemSlot(), ItemManager.addSpell(player.getInventory().getItemInMainHand(), Spell.FLOW));
                    player.sendMessage(Utils.colouredPrefix("You have enchanted this wand with FLOW!"));
                } else {
                    player.sendMessage(Utils.colouredPrefix("You need to be holding an item!"));
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
