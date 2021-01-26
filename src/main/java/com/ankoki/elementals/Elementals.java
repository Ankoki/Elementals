package com.ankoki.elementals;

import com.ankoki.elementals.api.ElementalsAPI;
import com.ankoki.elementals.commands.ElementalsCmd;
import com.ankoki.elementals.listeners.CombineListener;
import com.ankoki.elementals.listeners.JoinQuitListener;
import com.ankoki.elementals.listeners.SwapListener;
import com.ankoki.elementals.managers.CooldownManager;
import com.ankoki.elementals.managers.Spell;
import com.ankoki.elementals.spells.entity.poison.CastPoison;
import com.ankoki.elementals.spells.entity.umbrial.CastUmbrial;
import com.ankoki.elementals.spells.generic.dash.CastDash;
import com.ankoki.elementals.spells.generic.fireball.CastFireball;
import com.ankoki.elementals.spells.generic.fireball.ProjectileHit;
import com.ankoki.elementals.spells.generic.flow.WaterSpread;
import com.ankoki.elementals.spells.generic.flow.CastFlow;
import com.ankoki.elementals.listeners.SpellListener;
import com.ankoki.elementals.spells.entity.possesion.CastPossession;
import com.ankoki.elementals.spells.generic.medic.CastMedic;
import com.ankoki.elementals.spells.generic.regrowth.CastRegrowth;
import com.ankoki.elementals.spells.generic.rise.CastRise;
import com.ankoki.elementals.spells.generic.selfdestruct.CastSelfDestruct;
import com.ankoki.elementals.spells.generic.travel.CastTravel;
import com.ankoki.elementals.utils.UpdateChecker;
import com.ankoki.elementals.utils.Utils;
import com.ankoki.elementals.utils.Version;
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
import redempt.redlib.configmanager.annotations.ConfigValue;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Elementals extends JavaPlugin {
    private PluginDescriptionFile description;
    private PluginManager pluginManager;
    private Logger logger;
    private String pluginVersion;
    @Getter
    private ConfigManager configManager;
    @Getter
    @ConfigValue("disabled-spells")
    @SuppressWarnings("FieldMayBeFinal")
    private List<Spell> disabledSpells = ConfigManager.list(Spell.class);
    public static Version SERVER_VERSION;
    @Getter
    private static Elementals instance;
    @Getter
    public boolean latest;
    @Getter
    public String latestTag;

    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();
        pluginManager = this.getServer().getPluginManager();

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

        description = this.getDescription();
        logger = this.getLogger();
        pluginVersion = this.description.getVersion();
        SERVER_VERSION = this.getPluginVersion();

        if (this.getPluginVersion() == Version.UNKNOWN) {
            logger.severe(" # # # # # # # # # # # # # # #");
            logger.severe(" # ");
            logger.severe(" # You are running on an unknown minecraft version!");
            logger.severe(" # If you are running below 1.7... what are you doing?");
            logger.severe(" # Anything below 1.13 is not supported by Elementals!");
            logger.severe(" # If you are running on a very new plugin version, don't worry!");
            logger.severe(" # There is will be an update to provide support shortly!");
            logger.severe(" # Disabling...");
            logger.severe(" # ");
            logger.severe(" # # # # # # # # # # # # # # #");
            pluginManager.disablePlugin(this);
            return;
        }

        if (this.getPluginVersion().isLegacy()) {
            logger.severe(" # # # # # # # # # # # # # # #");
            logger.severe(" # ");
            logger.severe(" # Your server is running on a legacy minecraft version!");
            logger.severe(" # This plugin only supports 1.13+!");
            logger.severe(" # Disabling...");
            logger.severe(" # ");
            logger.severe(" # # # # # # # # # # # # # # #");
            pluginManager.disablePlugin(this);
            return;
        }

        instance = this;
        CooldownManager cooldownManager = new CooldownManager();
        //Registering Listeners
        SpellListener spellListener = new SpellListener(this, cooldownManager);
        this.registerListeners(new WaterSpread(this),
                spellListener,
                new SwapListener(),
                new JoinQuitListener(this),
                new ProjectileHit(),
                new CombineListener());
        //Registering Spells
        ElementalsAPI.getEntitySpells().clear();
        ElementalsAPI.getGenericSpells().clear();
        ElementalsAPI.registerGenericSpells(this,
                new CastFlow(this),
                new CastTravel(this),
                new CastRise(this),
                new CastFireball(this),
                new CastDash(this),
                new CastMedic(this),
                new CastRegrowth(this),
                new CastSelfDestruct(this));
        ElementalsAPI.registerEntitySpells(this,
                new CastPossession(this),
                new CastUmbrial(this),
                new CastPoison(this));
        //Loading NBTAPI
        NBTItem testItem = new NBTItem(new ItemStack(Material.LEAD));
        testItem.addCompound("test");
        testItem.setInteger("testInt", 1);
        //Registering commands
        this.registerCommand();
        //Loading config and messages
        Messages.load(this);
        this.loadConfiguration();
        logger.info(String.format("%s v%s was enabled in %.2f seconds (%sms)",
                description.getName(), pluginVersion, (float) System.currentTimeMillis() - start,
                (System.currentTimeMillis() - start)));
        UpdateChecker checker = new UpdateChecker(this);
        latest = checker.isLatest();
        if (isBeta()) {
            latestTag = checker.getLatestTag();
            logger.warning("You are currently runninng on an untested and in development version of Elementals!");
            logger.info("How you obtained this I have 0 clue, so be sure to message me on discord (Ankoki#0001) so i can help sort this:)");
            String compare = pluginVersion.replace("beta-", "");
            if (compare.equalsIgnoreCase(latestTag)) {
                logger.info("Even though you are in a beta version, there is a newer release of Elementals! " +
                        "Please go to https://github.com/Ankoki-Dev/Elementals/releases/tag/" + checker.getLatestTag() +
                        " to get the latest version!");
            }
        } else {
            if (!latest) {
                latestTag = checker.getLatestTag();
                logger.info("There is a newer release of Elementals! " +
                        "Please go to https://github.com/Ankoki-Dev/Elementals/releases/tag/" + checker.getLatestTag() +
                        " to get the latest version!");
            }
        }
    }

    @Override
    public void onDisable() {
        long end = System.currentTimeMillis();
        pluginManager = null;
        description = null;
        logger = null;
        pluginVersion = null;
        instance = null;
        System.out.printf("Elementals was disabled in %.2f seconds%n",
                (float) System.currentTimeMillis() - end);
    }

    private void registerCommand() {
        ArgType<Spell> spellType = new ArgType<>("spell", ElementalsAPI::valueOf)
                .tabStream(c -> (ElementalsAPI.getAllSpells()).stream().map(Spell::getSpellName).map(String::toLowerCase));
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
        if (redLib == null) return false;
        else if (!redLib.isEnabled()) return false;
        else return Utils.checkPluginVersion(redLib, 2, 0);
    }

    private void loadConfiguration() {
        configManager = new ConfigManager(this)
                .addConverter(Spell.class, ElementalsAPI::valueOf, Spell::toString)
                .register(this)
                .saveDefaults()
                .load();
    }

    private Version getPluginVersion() {
        try {
            String packageName = this.getServer().getClass().getPackage().getName();
            String version = packageName.substring(packageName.lastIndexOf('.') + 1);
            return Version.valueOf(version);
        } catch (Exception ex) {
            return Version.UNKNOWN;
        }
    }

    public boolean spellEnabled(Spell spell) {
        return !disabledSpells.contains(spell);
    }

    private boolean isBeta() {
        return pluginVersion.endsWith("-beta");
    }

    /*
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
