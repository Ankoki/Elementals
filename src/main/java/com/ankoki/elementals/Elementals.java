package com.ankoki.elementals;

import com.ankoki.elementals.commands.ElementalsCmd;
import com.ankoki.elementals.listeners.JoinListener;
import com.ankoki.elementals.managers.Castable;
import com.ankoki.elementals.managers.Spell;
import com.ankoki.elementals.spells.flow.WaterSpread;
import com.ankoki.elementals.spells.flow.CastFlow;
import com.ankoki.elementals.managers.EventManager;
import com.ankoki.elementals.listeners.SpellListener;
import com.ankoki.elementals.spells.rise.CastRise;
import com.ankoki.elementals.spells.travel.CastTravel;
import com.ankoki.elementals.utils.Utils;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import redempt.redlib.commandmanager.ArgType;
import redempt.redlib.commandmanager.CommandParser;
import redempt.redlib.commandmanager.ContextProvider;
import redempt.redlib.commandmanager.Messages;
import redempt.redlib.configmanager.ConfigManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class Elementals extends JavaPlugin {
    @Getter
    private final List<Castable> castableSpells = new ArrayList<>();
    private PluginDescriptionFile description;
    private PluginManager pluginManager;
    private Logger logger;
    @Getter
    private String version;
    @Getter
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();
        Messages.load(this);
        pluginManager = this.getServer().getPluginManager();
        description = this.getDescription();
        logger = this.getLogger();
        version = this.description.getVersion();

        if (!dependencyCheck()) {
            logger.severe(" # # # # # # # # # # # # # # #");
            logger.severe(" # ");
            logger.severe(" # Dependency RedLib was not found or outdated!");
            logger.severe(" # Check you have it installed or updated!");
            logger.severe(" # Disabling...");
            logger.severe(" # ");
            logger.severe(" # # # # # # # # # # # # # # #");
            pluginManager.disablePlugin(this);
            return;
        }
        logger.info("Registering listeners...");
        this.registerListeners(new WaterSpread(this),
                new SpellListener(this),
                new EventManager(),
                new JoinListener());
        logger.info("Registering spells...");
        this.registerSpells(new CastFlow(this),
                new CastTravel(this),
                new CastRise(this));
        logger.info("Registering commands...");
        this.registerCommand();
        logger.info("Loading config...");
        this.loadConfiguration();

        logger.info(String.format("%s v%s was enabled in %.2f seconds",
                description.getName(), description.getVersion(), (float) System.currentTimeMillis() - start));
    }

    @Override
    public void onDisable() {
        long end = System.currentTimeMillis();
        pluginManager = null;
        description = null;
        logger = null;
        version = null;
        System.out.println(String.format("Elementals was disabled in %.2f seconds",
                (float) System.currentTimeMillis() - end));
    }

    private void loadConfiguration() {
        configManager = new ConfigManager(this)
                .register(this)
                .saveDefaults()
                .load()
                .save();
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
        } else if (!redLib.isEnabled()) {
            return false;
        } else return Utils.checkPluginVersion(redLib, 2, 0);
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

    @Getter
    private final List<Player> casting = new ArrayList<>();
    public void addCaster(Player player) {
        casting.add(player);
    }
    public void removeCaster(Player player) {
        casting.remove(player);
    }
    public boolean isCasting(Player player) { return casting.contains(player); }
}
