package com.ankoki.elementals;

import com.ankoki.elementals.commands.ElementalsCmd;
import com.ankoki.elementals.managers.Castable;
import com.ankoki.elementals.spells.flow.WaterSpread;
import com.ankoki.elementals.spells.flow.CastFlow;
import com.ankoki.elementals.managers.EventManager;
import com.ankoki.elementals.listeners.SpellListener;
import com.ankoki.elementals.spells.travel.CastTravel;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * Fun lil project because i wanted to test
 * lombok and how it worked asf
 */
public class Elementals extends JavaPlugin {
    @Getter
    private final List<Castable> castableSpells = new ArrayList<>();
    private PluginDescriptionFile pdf;
    private PluginManager pm;

    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();
        pm = this.getServer().getPluginManager();
        pdf = this.getDescription();
        registerListeners(new WaterSpread(this),
                new SpellListener(this),
                new EventManager());
        registerSpells(new CastFlow(this),
                new CastTravel(this));
        registerCmds();
        System.out.println(String.format("%s v%s was enabled in %.2f seconds", pdf.getName(), pdf.getVersion(), (float) System.currentTimeMillis() - start));
    }

    @Override
    public void onDisable() {
        long end = System.currentTimeMillis();
        pm = null;
        pdf = null;
        System.out.println(String.format("Elementals was disabled in %.2f seconds", (float) System.currentTimeMillis() - end));
    }

    private void registerSpells(Castable... castables) {
        castableSpells.addAll(Arrays.asList(castables));
    }

    private void registerListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            pm.registerEvents(listener, this);
        }
    }

    private void registerCmds() {
        this.getServer().getPluginCommand("elementals").setExecutor(new ElementalsCmd());
    }

    /**
     * Lists for spells that i dont wanna make
     * static so i get them from here asf
     */
    @Getter
    private final List<Location> flowLocations = new ArrayList<>();
    public void addFlowLocation(Location location) {
        flowLocations.add(location);
    }

    public void removeFlowLocation(Location location) {
        flowLocations.remove(location);
    }
}
