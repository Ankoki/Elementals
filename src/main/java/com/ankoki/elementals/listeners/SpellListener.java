package com.ankoki.elementals.listeners;

import com.ankoki.elementals.Elementals;
import com.ankoki.elementals.api.ElementalsAPI;
import com.ankoki.elementals.events.EntitySpellCastEvent;
import com.ankoki.elementals.events.GenericSpellCastEvent;
import com.ankoki.elementals.events.SpellCastEvent;
import com.ankoki.elementals.api.GenericSpell;
import com.ankoki.elementals.api.EntitySpell;
import com.ankoki.elementals.managers.CooldownManager;
import com.ankoki.elementals.managers.ItemManager;
import com.ankoki.elementals.api.Prolonged;
import com.ankoki.elementals.utils.Utils;
import com.ankoki.elementals.managers.Spell;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import redempt.redlib.commandmanager.Messages;

@SuppressWarnings("unused")
@RequiredArgsConstructor
public class SpellListener implements Listener {
    private final Elementals plugin;
    private final CooldownManager cooldown;
    private static final String REPLACE_SPELL = "%spell%";
    private static final String ON_STOP_CAST = "on-stop-cast";
    private static final String ON_CAST = "on-cast";

    @EventHandler
    private void onRightClick(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK && e.getAction() != Action.RIGHT_CLICK_AIR) return;
        if (e.getHand() != EquipmentSlot.HAND) return;
        Player player = e.getPlayer();
        ItemStack heldItem = player.getInventory().getItemInMainHand();
        if (heldItem.getType() != Material.AIR) {
            for (GenericSpell genericSpell : ElementalsAPI.getGenericSpells()) {
                ItemManager wand = new ItemManager(heldItem);
                Spell spell = genericSpell.getSpell();
                String spellName = spell.getSpellName();
                if (wand.hasSpell(spell)) {
                    if (player.hasPermission("elementals.cast") ||
                            player.hasPermission("elementals.cast.generic")) {
                        if (cooldown.canCast(player, spell, genericSpell.getCooldown())) {
                            if (plugin.spellEnabled(spell)) {
                                SpellCastEvent defaultEvent = new SpellCastEvent(player, spell,
                                        genericSpell.getCooldown());
                                Bukkit.getPluginManager().callEvent(defaultEvent);
                                GenericSpellCastEvent event = new GenericSpellCastEvent(player, spell,
                                        genericSpell.getCooldown());
                                Bukkit.getPluginManager().callEvent(event);
                                if (!event.isCancelled() && !defaultEvent.isCancelled()) {
                                    e.setCancelled(true);
                                    if (genericSpell instanceof Prolonged) {
                                        if (!ElementalsAPI.isCasting(player)) {
                                            if (genericSpell.onCast(player)) {
                                                ElementalsAPI.addCaster(player, spell);
                                                Utils.sendActionBar(player, Messages.msg(ON_CAST)
                                                        .replace(REPLACE_SPELL, spellName));
                                            }
                                        } else {
                                            ((Prolonged) genericSpell).onCancel(player);
                                            ElementalsAPI.removeCaster(player);
                                            Utils.sendActionBar(player, Messages.msg(ON_STOP_CAST)
                                                    .replace(REPLACE_SPELL, spellName));
                                        }
                                        return;
                                    }
                                    if (genericSpell.onCast(player)) {
                                        if (!player.hasPermission("elementals.bypass")) {
                                            cooldown.addCooldown(player, spell);
                                        }
                                        Utils.sendActionBar(player, Messages.msg(ON_CAST)
                                                .replace(REPLACE_SPELL, spellName));
                                        return;
                                    }
                                } else {
                                    Utils.sendActionBar(player, Messages.msg("cancelled-spell")
                                            .replace(REPLACE_SPELL, spellName));
                                }
                            } else {
                                Utils.sendActionBar(player, Messages.msg("disabled-spell")
                                        .replace(REPLACE_SPELL, spellName));
                            }
                        } else {
                            Utils.sendActionBar(player, Messages.msg("cooldown")
                                    .replace("%cooldown%", Integer.toString(genericSpell.getCooldown())));
                        }
                        return;
                    } else {
                        Utils.sendActionBar(player, Messages.msg("no-permission"));
                    }
                }
            }
            for (EntitySpell entitySpell : ElementalsAPI.getEntitySpells()) {
                ItemManager wand = new ItemManager(heldItem);
                Spell spell = entitySpell.getSpell();
                if (wand.hasSpell(spell)) {
                    if (player.hasPermission("elementals.cast") ||
                            player.hasPermission("elementals.cast.entity")) {
                        if (plugin.spellEnabled(spell)) {
                            if (ElementalsAPI.getCastedSpell(player) == spell) {
                                if (entitySpell instanceof Prolonged) {
                                    ((Prolonged) entitySpell).onCancel(player);
                                    ElementalsAPI.removeCaster(player);
                                    Utils.sendActionBar(player, Messages.msg(ON_STOP_CAST)
                                            .replace(REPLACE_SPELL, spell.getSpellName()));
                                }
                                return;
                            }
                            Utils.sendActionBar(player, Messages.msg("on-attempted-entitycast"));
                        }
                    }
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
            for (EntitySpell entitySpell : ElementalsAPI.getEntitySpells()) {
                ItemManager wand = new ItemManager(heldItem);
                Spell spell = entitySpell.getSpell();
                String spellName = spell.getSpellName();
                if (wand.hasSpell(spell)) {
                    if (player.hasPermission("elementals.cast") ||
                            player.hasPermission("elementals.cast.entity")) {
                        if (player.hasPermission("elementals.spell" + spellName.replace(" ", ""))) {
                            if (!ElementalsAPI.isCasting(player)) {
                                if (cooldown.canCast(player, spell, entitySpell.getCooldown())) {
                                    if (plugin.spellEnabled(spell)) {
                                        SpellCastEvent defaultEvent = new SpellCastEvent(player, entity,
                                                spell, entitySpell.getCooldown());
                                        Bukkit.getPluginManager().callEvent(defaultEvent);
                                        EntitySpellCastEvent event = new EntitySpellCastEvent(player, entity,
                                                spell, entitySpell.getCooldown());
                                        Bukkit.getPluginManager().callEvent(event);
                                        if (!event.isCancelled() && !defaultEvent.isCancelled()) {
                                            e.setCancelled(true);
                                            if (entitySpell instanceof Prolonged) {
                                                if (!ElementalsAPI.isCasting(player)) {
                                                    if (entitySpell.onCast(player, entity)) {
                                                        ElementalsAPI.addCaster(player, spell);
                                                        Utils.sendActionBar(player, Messages.msg(ON_CAST)
                                                                .replace(REPLACE_SPELL, spell
                                                                        .getSpellName()));
                                                    }
                                                } else {
                                                    ((Prolonged) entitySpell).onCancel(player);
                                                    ElementalsAPI.removeCaster(player);
                                                    if (!player.hasPermission("elementals.bypass")) {
                                                        cooldown.addCooldown(player, spell);
                                                    }
                                                    Utils.sendActionBar(player, Messages.msg(ON_STOP_CAST)
                                                            .replace(REPLACE_SPELL, spellName));
                                                }
                                                return;
                                            }
                                            if (entitySpell.onCast(player, entity)) {
                                                cooldown.addCooldown(player, spell);
                                                Utils.sendActionBar(player, Messages.msg(ON_CAST)
                                                        .replace(REPLACE_SPELL, spellName));
                                                return;
                                            }
                                        } else {
                                            Utils.sendActionBar(player, Messages.msg("cancelled-spell")
                                                    .replace(REPLACE_SPELL, spellName));
                                        }
                                    } else {
                                        Utils.sendActionBar(player, Messages.msg("disabled-spell")
                                                .replace(REPLACE_SPELL, spellName));
                                    }
                                } else {
                                    Utils.sendActionBar(player, Messages.msg("cooldown")
                                            .replace("%cooldown%", Integer.toString(entitySpell.getCooldown())));
                                }
                            } else {
                                Utils.sendActionBar(player, Messages.msg("already-casting"));
                            }
                        } else {
                            Utils.sendActionBar(player, Messages.msg("no-permission"));
                        }
                    } else {
                        Utils.sendActionBar(player, Messages.msg("no-permission"));
                    }
                }
            }
        }
    }
}
