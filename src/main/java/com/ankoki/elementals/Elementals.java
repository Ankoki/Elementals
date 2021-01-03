package com.ankoki.elementals;

import com.ankoki.elementals.commands.ElementalsCmd;
import com.ankoki.elementals.listeners.JoinListener;
import com.ankoki.elementals.managers.GenericSpell;
import com.ankoki.elementals.managers.EntitySpell;
import com.ankoki.elementals.managers.Spell;
import com.ankoki.elementals.spells.fireball.CastFireball;
import com.ankoki.elementals.spells.fireball.ProjectileHit;
import com.ankoki.elementals.spells.flow.WaterSpread;
import com.ankoki.elementals.spells.flow.CastFlow;
import com.ankoki.elementals.managers.EventManager;
import com.ankoki.elementals.listeners.SpellListener;
import com.ankoki.elementals.spells.possesion.CastPossession;
import com.ankoki.elementals.spells.rise.CastRise;
import com.ankoki.elementals.spells.travel.CastTravel;
import com.ankoki.elementals.utils.Utils;
import de.tr7zw.changeme.nbtapi.NBTItem;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
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

//Note: add EntitySpell and GenericSpell
public class Elementals extends JavaPlugin {
    @Getter
    private final List<GenericSpell> genericSpells = new ArrayList<>();
    @Getter
    private final List<EntitySpell> entitySpells = new ArrayList<>();
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
                new JoinListener(),
                new ProjectileHit());
        logger.info("Registering spells...");
        this.registerGenericSpells(new CastFlow(this),
                new CastTravel(this),
                new CastRise(this),
                new CastFireball(this));
        this.registerEntitySpells(new CastPossession(this));
        logger.info("Registering commands...");
        this.registerCommand();
        logger.info("Loading config...");
        this.loadConfiguration();
        logger.info("Loading NBTAPI...");
        NBTItem item = new NBTItem(new ItemStack(Material.BELL));
        item.addCompound("test");
        item.setInteger("spell", 2301);

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
        System.out.printf("Elementals was disabled in %.2f seconds%n",
                (float) System.currentTimeMillis() - end);
    }

    private void loadConfiguration() {
        configManager = new ConfigManager(this)
                .register(new CastFlow(this),
                        new CastTravel(this),
                        new CastRise(this),
                        new CastFireball(this),
                        new CastPossession(this))
                .saveDefaults()
                .load();
    }

    private void registerGenericSpells(GenericSpell... genericSpells) {
        this.genericSpells.addAll(Arrays.asList(genericSpells));
    }

    private void registerEntitySpells(EntitySpell... spells) { entitySpells.addAll(Arrays.asList(spells));}

    private void registerCommand() {
        ArgType<Spell> spellType = new ArgType<>("spell", Elementals::getSpell)
                .tabStream(c -> Arrays.stream(Spell.values()).map(Spell::getSpellName).map(String::toLowerCase));
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
}
