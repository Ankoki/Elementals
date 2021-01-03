package com.ankoki.elementals.listeners;

import com.ankoki.elementals.Elementals;
import com.ankoki.elementals.events.RightClickEvent;
import com.ankoki.elementals.events.SpellCastEvent;
import com.ankoki.elementals.managers.*;
import com.ankoki.elementals.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import redempt.redlib.commandmanager.Messages;

import java.util.WeakHashMap;

@RequiredArgsConstructor
public class SpellListener implements Listener {
    private final Elementals plugin;

    private final WeakHashMap<Player, WeakHashMap<Spell, Long>> spellCooldown = new WeakHashMap<>();
    private final WeakHashMap<Spell, Long> cooldown = new WeakHashMap<>();
    private final WeakHashMap<Player, Spell> castingSpell = new WeakHashMap<>();

    @EventHandler
    private void onRightClick(RightClickEvent e) {
        Player player = e.getPlayer();
        ItemStack heldItem = player.getInventory().getItemInMainHand();
        if (heldItem.getType() != Material.AIR) {
            for (GenericSpell genericSpell : plugin.getGenericSpells()) {
                ItemManager wand = new ItemManager(heldItem);
                if (wand.hasSpell(genericSpell.getSpell())) {
                    if (cooldownExpired(player, genericSpell.getSpell(), genericSpell.getCooldown())) {
                        SpellCastEvent event = new SpellCastEvent(player, genericSpell.getSpell(), genericSpell.getCooldown());
                        Bukkit.getPluginManager().callEvent(event);
                        if (!event.isCancelled()) {
                            if (genericSpell.isEnabled()) {
                                if (genericSpell instanceof Prolonged) {
                                    if (!castingSpell.containsKey(player)) {
                                        if (genericSpell.onCast(player)) {
                                            castingSpell.put(player, genericSpell.getSpell());
                                            Utils.sendActionBar(player, Messages.msg("on-cast")
                                                    .replace("%spell%", genericSpell.getSpell().getSpellName()));
                                        }
                                    } else {
                                        ((Prolonged) genericSpell).onCancel(player);
                                        castingSpell.remove(player);
                                        Utils.sendActionBar(player, Messages.msg("on-stop-cast")
                                                .replace("%spell%", genericSpell.getSpell().getSpellName()));
                                    }
                                    return;
                                }
                                if (genericSpell.onCast(player)) {
                                    cooldown.put(genericSpell.getSpell(), System.currentTimeMillis());
                                    spellCooldown.put(player, cooldown);
                                    Utils.sendActionBar(player, Messages.msg("on-cast")
                                            .replace("%spell%", genericSpell.getSpell().getSpellName()));
                                    return;
                                }
                            } else {
                                Utils.sendActionBar(player, Messages.msg("disabled-spell")
                                        .replace("%spell%", genericSpell.getSpell().getSpellName()));
                            }
                        } else {
                            Utils.sendActionBar(player, Messages.msg("cancelled-spell"));
                        }
                    } else {
                        Utils.sendActionBar(player, Messages.msg("cooldown")
                                .replace("%cooldown%", Integer.toString(genericSpell.getCooldown())));
                    }
                    return;
                }
            }
            for (EntitySpell entitySpell : plugin.getEntitySpells()) {
                ItemManager wand = new ItemManager(heldItem);
                if (wand.hasSpell(entitySpell.getSpell())) {
                    if (castingSpell.get(player) == entitySpell.getSpell()) {
                        if (entitySpell instanceof Prolonged) {
                            ((Prolonged) entitySpell).onCancel(player);
                            castingSpell.remove(player);
                            Utils.sendActionBar(player, Messages.msg("on-stop-cast")
                                    .replace("%spell%", entitySpell.getSpell().getSpellName()));
                        }
                        return;
                    }
                    Utils.sendActionBar(player, Messages.msg("on-attempted-entitycast"));
                }
            }
        }
    }

    @EventHandler
    private void onEntityInteract(PlayerInteractAtEntityEvent e) {
        Player player = e.getPlayer();
        Entity entity = e.getRightClicked();
        ItemStack heldItem = player.getInventory().getItemInMainHand();
        if (heldItem.getType() != Material.AIR) {
            for (EntitySpell entitySpell : plugin.getEntitySpells()) {
                ItemManager wand = new ItemManager(heldItem);
                if (wand.hasSpell(entitySpell.getSpell())) {
                    if (cooldownExpired(player, entitySpell.getSpell(), entitySpell.getCooldown())) {
                        SpellCastEvent event = new SpellCastEvent(player, entitySpell.getSpell(), entitySpell.getCooldown());
                        Bukkit.getPluginManager().callEvent(event);
                        if (!event.isCancelled()) {
                            if (entitySpell.isEnabled()) {
                                if (entitySpell instanceof Prolonged) {
                                    if (!castingSpell.containsKey(player)) {
                                        if (entitySpell.onCast(player, entity)) {
                                            castingSpell.put(player, entitySpell.getSpell());
                                            Utils.sendActionBar(player, Messages.msg("on-cast")
                                                    .replace("%spell%", entitySpell.getSpell().getSpellName()));
                                        }
                                    } else {
                                        ((Prolonged) entitySpell).onCancel(player);
                                        castingSpell.remove(player);
                                        Utils.sendActionBar(player, Messages.msg("on-stop-cast")
                                                .replace("%spell%", entitySpell.getSpell().getSpellName()));
                                    }
                                    return;
                                }
                                if (entitySpell.onCast(player, entity)) {
                                    cooldown.put(entitySpell.getSpell(), System.currentTimeMillis());
                                    spellCooldown.put(player, cooldown);
                                    Utils.sendActionBar(player, Messages.msg("on-cast")
                                            .replace("%spell%", entitySpell.getSpell().getSpellName()));
                                    return;
                                }
                            } else {
                                Utils.sendActionBar(player, Messages.msg("disabled-spell")
                                        .replace("%spell%", entitySpell.getSpell().getSpellName()));
                            }
                        } else {
                            Utils.sendActionBar(player, Messages.msg("cancelled-spell"));
                        }
                    } else {
                        Utils.sendActionBar(player, Messages.msg("cooldown")
                                .replace("%cooldown%", Integer.toString(entitySpell.getCooldown())));
                    }
                }
            }
        }
    }

    private boolean cooldownExpired(Player player, Spell spell, long cooldown) {
        boolean cooldownExists = spellCooldown.get(player) != null;
        long pCooldown;
        if (cooldownExists) {
            pCooldown = spellCooldown.get(player).get(spell) == null ? 0 : spellCooldown.get(player).get(spell);
        } else {
            pCooldown = 0;
        }
        return ((System.currentTimeMillis() - pCooldown) > (cooldown * 1000L));
    }

    /*
     * These events are for the spells which cast for an extended amount
     * of time, such as Flow, and rely on holding the same wand throughout.
     */
    @EventHandler
    private void onSwitch(PlayerItemHeldEvent e) {
        Player player = e.getPlayer();
        if (castingSpell.get(player).isProlonged()) {

        }
    }

    @EventHandler
    private void offhandSwap(PlayerSwapHandItemsEvent e) {
        Player player = e.getPlayer();
        if (castingSpell.get(player).isProlonged()) {

        }
    }

}
