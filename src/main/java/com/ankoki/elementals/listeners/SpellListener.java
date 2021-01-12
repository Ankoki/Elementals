package com.ankoki.elementals.listeners;

import com.ankoki.elementals.Elementals;
import com.ankoki.elementals.events.EntitySpellCastEvent;
import com.ankoki.elementals.events.RightClickEvent;
import com.ankoki.elementals.events.GenericSpellCastEvent;
import com.ankoki.elementals.managers.GenericSpell;
import com.ankoki.elementals.managers.EntitySpell;
import com.ankoki.elementals.managers.ItemManager;
import com.ankoki.elementals.managers.Prolonged;
import com.ankoki.elementals.utils.Utils;
import com.ankoki.elementals.managers.Spell;
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

@SuppressWarnings("unused")
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
                        if (plugin.spellEnabled(genericSpell.getSpell())) {
                            GenericSpellCastEvent event = new GenericSpellCastEvent(player, genericSpell.getSpell(),
                                    genericSpell.getCooldown());
                            Bukkit.getPluginManager().callEvent(event);
                            if (!event.isCancelled()) {
                                e.setCancelled(true);
                                if (genericSpell instanceof Prolonged) {
                                    if (!isCasting(player)) {
                                        if (genericSpell.onCast(player)) {
                                            addCaster(player, genericSpell.getSpell());
                                            Utils.sendActionBar(player, Messages.msg("on-cast")
                                                    .replace("%spell%", genericSpell.getSpell().getSpellName()));
                                        }
                                    } else {
                                        ((Prolonged) genericSpell).onCancel(player);
                                        removeCaster(player);
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
                                Utils.sendActionBar(player, Messages.msg("cancelled-spell")
                                        .replace("%spell%", genericSpell.getSpell().getSpellName()));
                            }
                        } else {
                            Utils.sendActionBar(player, Messages.msg("disabled-spell")
                                    .replace("%spell%", genericSpell.getSpell().getSpellName()));
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
                    if (plugin.spellEnabled(entitySpell.getSpell())) {
                        if (castingSpell.get(player) == entitySpell.getSpell()) {
                            if (entitySpell instanceof Prolonged) {
                                ((Prolonged) entitySpell).onCancel(player);
                                removeCaster(player);
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
                    if (!isCasting(player)) {
                        if (cooldownExpired(player, entitySpell.getSpell(), entitySpell.getCooldown())) {
                            if (plugin.spellEnabled(entitySpell.getSpell())) {
                                EntitySpellCastEvent event = new EntitySpellCastEvent(player, entity,
                                        entitySpell.getSpell(), entitySpell.getCooldown());
                                Bukkit.getPluginManager().callEvent(event);
                                if (!event.isCancelled()) {
                                    e.setCancelled(true);
                                    if (entitySpell instanceof Prolonged) {
                                        if (!isCasting(player)) {
                                            if (entitySpell.onCast(player, entity)) {
                                                addCaster(player, entitySpell.getSpell());
                                                Utils.sendActionBar(player, Messages.msg("on-cast")
                                                        .replace("%spell%", entitySpell.getSpell()
                                                                .getSpellName()));
                                            }
                                        } else {
                                            ((Prolonged) entitySpell).onCancel(player);
                                            removeCaster(player);
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
                                    Utils.sendActionBar(player, Messages.msg("cancelled-spell")
                                            .replace("%spell%", entitySpell.getSpell().getSpellName()));
                                }
                            } else {
                                Utils.sendActionBar(player, Messages.msg("disabled-spell")
                                        .replace("%spell%", entitySpell.getSpell().getSpellName()));
                            }
                        } else {
                            Utils.sendActionBar(player, Messages.msg("cooldown")
                                    .replace("%cooldown%", Integer.toString(entitySpell.getCooldown())));
                        }
                    } else {
                        Utils.sendActionBar(player, Messages.msg("already-casting"));
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
        if (castingSpell.containsKey(player)) {
            if (castingSpell.get(player).isProlonged()) {
                Utils.sendActionBar(player, Messages.msg("on-stop-cast")
                        .replace("%spell%", castingSpell.get(player).getSpellName()));
                removeCaster(player);
            }
        }
    }

    @EventHandler
    private void offhandSwap(PlayerSwapHandItemsEvent e) {
        Player player = e.getPlayer();
        if (isCasting(player)) {
            if (castingSpell.get(player).isProlonged()) {
                Utils.sendActionBar(player, Messages.msg("on-stop-cast")
                        .replace("%spell%", castingSpell.get(player).getSpellName()));
                removeCaster(player);
            }
        }
    }

    public void removeCaster(Player player) {
        castingSpell.remove(player);
    }

    public void addCaster(Player player, Spell spell) {
        castingSpell.put(player, spell);
    }

    public boolean isCasting(Player player) {
        return castingSpell.containsKey(player);
    }
}
