package com.ankoki.elementals.spells.fireball;

import org.bukkit.World;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

@SuppressWarnings("unused")
public class ProjectileHit implements Listener {

    @EventHandler
    private void onProjectileHit(ProjectileHitEvent e) {
        if (e.getEntity().hasMetadata("elementalsSpell")) {
            Projectile projectile = e.getEntity();
            World world = projectile.getWorld();
            world.createExplosion(projectile.getLocation(), 1F, true, false);
        }
    }
}
