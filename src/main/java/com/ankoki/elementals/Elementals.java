package com.ankoki.elementals;

import com.ankoki.elementals.commands.ElementalsCmd;
import com.ankoki.elementals.managers.Castable;
import com.ankoki.elementals.managers.Spell;
import com.ankoki.elementals.spells.flow.WaterSpread;
import com.ankoki.elementals.spells.flow.CastFlow;
import com.ankoki.elementals.managers.EventManager;
import com.ankoki.elementals.listeners.SpellListener;
import com.ankoki.elementals.spells.rise.CastRise;
import com.ankoki.elementals.spells.travel.CastTravel;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import redempt.redlib.commandmanager.ArgType;
import redempt.redlib.commandmanager.CommandParser;
import redempt.redlib.commandmanager.ContextProvider;
import redempt.redlib.commandmanager.Messages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Elementals extends JavaPlugin {
    @Getter
    private final List<Castable> castableSpells = new ArrayList<>();
    private PluginDescriptionFile description;
    private PluginManager pluginManager;
    @Getter
    private String version;

    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();
        Messages.load(this);
        pluginManager = this.getServer().getPluginManager();
        description = this.getDescription();
        version = this.description.getVersion();

        if (!dependencyCheck()) {
            System.out.println(" # # # # # # # # # # # # # # #");
            System.out.println(" # ");
            System.out.println(" # Dependency RedLib was not found!");
            System.out.println(" # Have you got it installed?");
            System.out.println(" # ");
            System.out.println(" # # # # # # # # # # # # # # #");
            pluginManager.disablePlugin(this);
            return;
        }

        Plugin redLib = pluginManager.getPlugin("RedLib");
        assert redLib != null;
        if (!versionChecker(redLib, 2, 0)) {
            System.out.println(" # # # # # # # # # # # # # # #");
            System.out.println(" # ");
            System.out.println(" # Dependency RedLib is outdated!");
            System.out.println(" # Update to the latest version!");
            System.out.println(" # ");
            System.out.println(" # # # # # # # # # # # # # # #");
            pluginManager.disablePlugin(this);
            return;
        }

        registerListeners(new WaterSpread(this),
                new SpellListener(this),
                new EventManager());
        registerSpells(new CastFlow(this),
                new CastTravel(this),
                new CastRise(this));
        registerCommand();
        System.out.println(String.format("%s v%s was enabled in %.2f seconds",
                description.getName(), description.getVersion(), (float) System.currentTimeMillis() - start));
    }

    @Override
    public void onDisable() {
        long end = System.currentTimeMillis();
        pluginManager = null;
        description = null;
        System.out.println(String.format("Elementals was disabled in %.2f seconds",
                (float) System.currentTimeMillis() - end));
    }

    private void registerSpells(Castable... castables) {
        castableSpells.addAll(Arrays.asList(castables));
    }

    private void registerCommand() {
        ArgType<Spell> spellType = new ArgType<>("spell", Elementals::getSpell)
                .tabStream(c -> Arrays.stream(Spell.values()).map(Spell::getSpellName));
        new CommandParser(this.getResource("command.txt"))
                .setArgTypes(spellType)
                .setContextProviders(ContextProvider.mainHand)
                .parse()
                .register("elementals", new ElementalsCmd(this));
    }

    private void registerListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            pluginManager.registerEvents(listener, this);
        }
    }

    private boolean dependencyCheck() {
        Plugin redLib = pluginManager.getPlugin("RedLib");
        if (redLib == null) {
            return false;
        } else return redLib.isEnabled();
    }

    private boolean versionChecker(Plugin plugin, int first, int second, int third) {
        first *= 100;
        second *= 10;
        int pluginVer = Integer.parseInt(plugin.getDescription().getVersion().replace(".", ""));
        int required = first + second + third;
        return pluginVer >= required;
    }

    private boolean versionChecker(Plugin plugin, int first, int second) {
        first *= 10;
        int pluginVer = Integer.parseInt(plugin.getDescription().getVersion().replace(".", ""));
        int required = first + second;
        return pluginVer >= required;
    }

    private static Spell getSpell(String s) {
        return Spell.valueOf(s.toUpperCase());
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
