package com.ankoki.elementals.listeners;

import com.ankoki.elementals.Elementals;
import com.ankoki.elementals.events.RightClickEvent;
import com.ankoki.elementals.events.SpellCastEvent;
import com.ankoki.elementals.managers.Castable;
import com.ankoki.elementals.managers.ItemManager;
import com.ankoki.elementals.managers.Spell;
import com.ankoki.elementals.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import redempt.redlib.commandmanager.Messages;

import java.util.WeakHashMap;

@RequiredArgsConstructor
public class SpellListener implements Listener {
    private final Elementals plugin;

    private final WeakHashMap<Spell, WeakHashMap<Player, Long>> spellCooldown = new WeakHashMap<>();
    private final WeakHashMap<Player, Long> cooldown = new WeakHashMap<>();

    @EventHandler
    private void onRightClick(RightClickEvent e) {
        Player player = e.getPlayer();
        ItemStack heldItem = player.getInventory().getItemInMainHand();
        if (heldItem.getType() != Material.AIR) {
            for (Castable castable : plugin.getCastableSpells()) {
                ItemManager wand = new ItemManager(heldItem);
                if (wand.hasSpell(castable.getSpell())) {
                    boolean cooldownExists = spellCooldown.get(castable.getSpell()) != null;
                    long pCooldown;
                    if (cooldownExists) {
                        pCooldown = spellCooldown.get(castable.getSpell()).get(player) == null ? 0 : spellCooldown.get(castable.getSpell()).get(player);
                    } else {
                        pCooldown = 0;
                    }
                    if ((System.currentTimeMillis() - pCooldown) > (castable.getCooldown() * 1000L)) {
                        SpellCastEvent event = new SpellCastEvent(player, castable.getSpell(), castable.getCooldown());
                        Bukkit.getPluginManager().callEvent(event);
                        if (!event.isCancelled()) {
                            if (castable.onCast(player)) {
                                cooldown.put(player, System.currentTimeMillis());
                                spellCooldown.put(castable.getSpell(), cooldown);
                                Utils.sendActionBar(player, Messages.msg("on-cast").replace("%spell%", castable.getSpell().getSpellName()));
                                return;
                            }
                        } else {
                            Utils.sendActionBar(player, Messages.msg("cancelled-spell"));
                        }
                    } else {
                        Utils.sendActionBar(player, Messages.msg("cooldown").replace("%cooldown%", Integer.toString(castable.getCooldown())));
                    }
                }
            }
        }
    }
}
