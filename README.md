# Read Me!
This was project to mess around with Lombok that I'm actually enjoying developing and is now released. 
Depends on RedLib and if you're planning to create a fork you will need Lombok installed on your IDE.
You can find the RedLib source code [here](https://www.github.com/Redempt/RedLib). 
You can find the RedLib download on spigot [here](https://www.spigotmc.org/resources/redlib.78713/).  
There is a really cool thing [here](https://www.youtube.com/watch?v=ub82Xb1C8os) if you want to take a look.  
## Elementals  
Elementals is a magic and spell based plugin where you can cast spells with any item which you desire too.
Any item can be enchanted and there is a wide variety of spells to choose from!  
## Server Owners
This is for people who want to use Elementals on their server.  
When the plugin is fully released, there will be download links and releases here. To use the plugin, you
just need to drop it in your plugins folder along with the [RedLib](https://www.spigotmc.org/resources/redlib.78713/)
plugin. You can then customize all the messages in the messages.txt and in the RedLib
plugin folder to change the incorrect command messages.  
### Spells
We currently have 11 spells.  
There are two types of spell, Entity and Generic. Generic spells can be cast looking at anything, and Entity spells 
can be cast while looking at an entity. If a spell is prolonged, you can start and stop casting it.  
#### Possession
Possession is a prolonged Entity Spell which can be casted on entities and makes you able to take
control of them and their movements. This only works on animals such as sheep, wolves, cows,
chickens and pigs.  
#### Umbrial
Umbrial is an Entity Spell which can be casted on players and this allows you to become invisible and 
enter the player you have targeted, you then rip out of their body, damaging them by 3 hearts.  
#### Poison  
Poison is an Entity Spell which can be casted on any living entity, and poisons the target entity for 6 seconds, 
and enables you to have absorbtion for the time they are poisoned.
#### Dash
Dash is a Generic Spell which pushes you towards the location you're looking at and gives you speed, which
allows people to utilize this for PvP sitatuions to either get away from or catch up to enemies.  
#### Fireball
Fireball is a Generic Spell which shoots a fireball towards where that player is looking. This is useful to
attack enemies which are far away.  
#### Flow
Flow is a prolonged Generic Spell which is used for controlling water and moves water to where you are looking.
#### Medic
Medic is a Generic Spell which heals the player. This is useful during fights.  
#### Regrowth
Regrowth is a Prolonged Generic Spell which heals all players in the shown radius of the player.  
#### Rise
Rise is a Generic Spell which causes you to levitate on a cloud of particles for 10 seconds. This can be useful 
when escaping enemies.  
#### Self Destruct
Self Destruct is a Generic Spell which freezes you and causes an explosion around you with a high force. This most 
likely will kill you.
#### Travel
Travel is a Generic Spell which allows you to teleport to the location you are looking at. 
### Command
There is currently one command in Elementals, and this is /elementals. The current applicable arguments are as follows, 
where (1|2) are choices, and [option] is optional.  
`/elementals [(enchant|disenchant|info|reload)] [spell]`  
This manages anything you need to within the plugin, and other things may be added in the future.  
### Permissions
| Permission | Default | Description |  
| :-- | :--: | :-- |  
| `elementals.admin` | FALSE | This allows you to do anything, similar to *. This is the wildcard perm. |  
| `elementals.reload` | FALSE | Lets the player reload the messages and config. |  
| `elementals.enchant` | FALSE | Allows the player to enchant wands. |  
| `elementals.disenchant` | FALSE | Allows the player to disenchant wands. |  
| `elementals.bypass` | FALSE | This bypasses the cooldown for spells. |  
| `elementals.help` | FALSE | This alllows you to use the /elementals help command. |  
| `elementals.cast` | TRUE | This determines wether or not a player can cast a spell. |  
| `elementals.cast.generic` | TRUE | This determines wether or not a player can cast a generic spell. |  
| `elementals.cast.entity` | TRUE | This determines wether or not a player can cast an entity spell. |  
| `elementals.spell.<spellname>` | TRUE | If you have the permission, you can cast the spell! |  
### Enjoy!
I hope you enjoy using this plugin, and if you have any suggestions, feedback, or anything of the sort, make sure to 
let me know, either in the [issue tracker](https://www.github.com/Ankoki-Dev/Elementals/issues), or 
[my discord](https://www.discord.gg/aCDNj8s):)
## Developers
There is now a way to register your own spells!  
There are two types of spell you can create.  
### Generic Spell
A generic spell is cast when you right click anywhere. You make this spell by implementing the GenericSpell 
interface. An example of a basic GenericSpell is as follows:

```java
import com.ankoki.elementals.api.GenericSpell;
import com.ankoki.elementals.managers.Spell;
import org.bukkit.entity.Player;

public class YourSpell implements GenericSpell {
    /*This is your spell. It requires a name, an id, and if your spell is prolonged.
    The ID and spell name must be indivudual, or you will generate an exception. 
    We will tackle prolonged spells later on.*/
    private final Spell spell = new Spell("SpellName", 262, false);

    //What you want to happen when the spell is cast
    @Override
    public boolean onCast(Player player) {
        player.setFlying(true);
        return true; //You return true when the spell is cast successfully, this enables the cooldown.
    }

    //This returns the spell you created
    @Override
    public Spell getSpell() {
        return spell;
    }

    //The cooldown you want to be applied when the spell is cast successfully, in seconds.
    @Override
    public int getCooldown() {
        return 3;
    }
}
```
This will create a spell which sets you to flying. However, you will need to register your spell when your
plugin is enabled. This is easy enough to do. You need to use this line in your onEnable():  
`ElementalsAPI.registerGenericSpells(new YourSpell());`  
This will register your spell with Elementals and now you have a functioning Generic Spell!
### Entity Spell
An entity spell is cast when you right click on an entitiy. You make this spell by implementing the EntitySpell
interface. An example of a basic EntitySpell is as follows:

```java
import com.ankoki.elementals.api.EntitySpell;
import com.ankoki.elementals.managers.Spell;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class YourSpell implements EntitySpell {
    /*This is your spell. It requires a name, an id, and if your spell is prolonged.
    The ID and spell name must be indivudual, or you will generate an exception. 
    We will tackle prolonged spells later on.*/
    private final Spell spell = new Spell("SpellName", 262, false);

    //What you want to happen when the spell is cast
    @Override
    public boolean onCast(Player player, Entity entity) {
        if (entity instanceof Player) {
            Player clickedPlayer = (Player) entity;
            clickedPlayer.setHealth(0);
            return true;
        }
        return false; //As the spell wasn't cast successfully, we return false and the cooldown isn't set.
    }

    //This returns the spell you created
    @Override
    public Spell getSpell() {
        return spell;
    }

    //The cooldown you want to be applied when the spell is cast successfully, in seconds.
    @Override
    public int getCooldown() {
        return 300;
    }
}
```
This creates a spell which kills the player you right click. An important note to take in is that you need to 
check what type of entity was clicked. You still also need to register the EntitySpells, and the way is nearly 
identical to the previous way:  
`ElementalsAPI.registerEntitySpells(new YourSpell());`  
You now know how to make both type of spells!
### Prolonged
The last thing that is essential to know is that you can make a spell prolonged, and is casted until 
the player attempts to cast again, leaves the game, or swaps tool. A prime example of a Prolonged spell in 
Elementals is Regrowth. To make a prolonged spell, there is not much too it. We will make this a GenericSpell type.

```java

import com.ankoki.elementals.api.ElementalsAPI;
import com.ankoki.elementals.api.GenericSpell;
import com.ankoki.elementals.managers.ParticlesManager;
import com.ankoki.elementals.api.Prolonged;
import com.ankoki.elementals.managers.Spell;
import com.ankoki.elementals.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

//You need to extend Prolonged to create a prolonged spell
public class YourSpell extends Prolonged implements GenericSpell {
    //Remember the spell asks if the spell is prolonged, and so the last boolean needs to be true to account for this.
    private final Spell spell = new Spell("SpellName", 262, true);
    private final JavaPlugin plugin;

    public YourSpell(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    //The onCast is the same, this is just an example of something you can do.
    @Override
    public boolean onCast(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!Utils.canCast(player) || !ElementalsAPI.isCasting(player)) {
                    this.cancel();
                }
                Player updated = Bukkit.getPlayer(player.getUniqueId());
                new ParticlesManager(updated, plugin).spawnRings(1, true, Color.BLUE);
            }
        }.runTaskTimer(plugin, 0L, 2L);
        return true;
    }

    @Override
    public Spell getSpell() {
        return spell;
    }

    @Override
    public int getCooldown() {
        return 3;
    }

    /*This gets called when the player stops casting the spell, you can do other things here.
    This method is optional and you do not have to include it. It'll still be cancellable if you do not include it*/
    @Override
    public void onCancel(Player player) {
        new ParticlesManager(player, plugin).drawCircle(3, 100, Color.RED);
    }
}
```
It's as simple as that! To register a prolonged spell, you don't need to do anything different. You can just 
use the `ElementalsAPI.registerGenericSpell(new YourSpell());` like when registering a normal spell!  
If you are using runnables to spawn particles whilst the player is casting a spell, you will want
to take a look at the `ElementalsAPI.isCasting(Player player);` method, which checks if the player is casting a spell. 
An example usage of this is as follows.

```java
import com.ankoki.elementals.api.ElementalsAPI;
import com.ankoki.elementals.api.GenericSpell;
import com.ankoki.elementals.api.Prolonged;
import com.ankoki.elementals.managers.ParticlesManager;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class YourSpell extends Prolonged implements GenericSpell {
    private final JavaPlugin plugin;
    private final Spell spell = new Spell("YourSpell", 262, true);

    public YourSpell(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCast(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                //This is the method you can call to check if the player is still casting.
                if (!ElementalsAPI.isCasting(player)) {
                    this.cancel();
                    return;
                }
                new ParticlesManager(player).drawDome(Color.ORANGE, Color.PURPLE);
            }
        }.runTaskTimer(plugin, 0L, 60L);
        return true;
    }

    @Override
    public Spell getSpell() {
        return spell;
    }

    @Override
    public int getCooldown() {
        return 3;
    }

    @Override
    public void onCancel(Player player) {
        /* An alternative to doing the isCasting() method is to set your BukkitRunnable to 
         * an int field and in the onCancel(), call task.cancel();
         */
    }
}
```
### Giving Spells to an Item
There is a built-in class for you to give your players an enchanted item! Heres a small example of how to use my 
ItemManager class to give and remove spells!  

```java
import com.ankoki.elementals.Elementals;
import com.ankoki.elementals.api.ElementalsAPI;
import com.ankoki.elementals.managers.ItemManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class Listening implements Listener {

    @EventHandler
    private void block(BlockBreakEvent e) {
        Player player = e.getPlayer();
        ItemStack heldItem = player.getInventory().getItemInMainHand();
        player.getInventory().setItem(player.getInventory().getHeldItemSlot(),
                new ItemManager(heldItem)
                        .addSpell(ElementalsAPI.getAllSpells() //Gets the list of all spells from the api
                                .get(new Random() //Gets a random number based on the size.
                                        .nextInt(ElementalsAPI.getAllSpells().size()))).getItem());
        player.sendMessage("§7You have been given a random spell!");
    }
}
```
This is **OBVIOUSLY** ugly and not easy on the eyes, however was just made to be an example of how you add a spell to 
an item and get the item with a spell back.
### Help
Remember you can access any Managers or Libraries that are in Elementals, so feel free to check them out. I highly 
recommend using the ParticlesManager as this provides an ease of use when drawing circles, and the Utils class is 
also useful for things such as coloured text and other small things which can be useful!
If there is anything you need/want added, or you have any suggestions, feel free to let me know in either the
[issue tracker](https://www.github.com/Ankoki-Dev/Elementals/issues), or [my discord](https://www.discord.gg/aCDNj8s) 
and I'll be happy to help!
# Version
Current release we are on is 1.0! This will be updated as more releases are out!