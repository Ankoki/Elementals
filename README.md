This was project to mess around with Lombok that I'm actually enjoying developing and now plan on releasing eventually. 
Depends on RedLib and if you're planning to create a fork you will need Lombok installed on your IDE.
You can find the RedLib source code [here](https://www.github.com/Redempt/RedLib). 
You can find the RedLib download on spigot [here](https://www.spigotmc.org/resources/redlib.78713/).  
There is a really cool thing [here](https://www.youtube.com/watch?v=ub82Xb1C8os) if you want to take a look.  
##Elementals  
Elementals is a magic and spell based plugin where you can cast spells with any item which you desire too.
Any item can be enchanted and there is a wide variety of spells to choose from!  
##Server Owners
This is for people who want to use Elementals on their server.  
When the plugin is fully released, there will be download links and releases here. To use the plugin, you
just need to drop it in your plugins folder along with the [RedLib](https://www.spigotmc.org/resources/redlib.78713/)
plugin. You can then customize all the messages in the messages.txt and in the RedLib
plugin folder to change the incorrect command messages.  
###Spells
We currently have 6 spells.  
There are two types of spell, Entity and Generic. Generic spells can be cast looking at anything, and Entity spells 
can be cast while looking at an entity. If a spell is prolonged, you can start and stop casting it.  
####Possession
Possession is a prolonged Entity Spell which can be casted on entities and makes you able to take
control of them and their movements. This only works on animals such as sheep, wolves, cows,
chickens and pigs.  
####Dash
Dash is a Generic Spell which pushes you towards the location you're looking at and gives you speed, which
allows people to utilize this for PvP sitatuions to either get away from or catch up to enemies.  
####Fireball
Fireball is a Generic Spell which shoots a fireball towards where that player is looking. This is useful to
attack enemies which are far away.  
####Flow
Flow is a prolonged Generic Spell which is used for controlling water and moves water to where you are looking.  
####Rise
Rise is a Generic Spell which causes you to levitate on a cloud of particles for 10 seconds. This can be useful 
when escaping enemies.  
####Travel
Travel is a Generic Spell which allows you to teleport to the location you are looking at.  
####Medic
Medic is a Generic Spell which heals the player. This is useful during fights.  
###Command
There is currently one command in Elementals, and this is /elementals. The current applicable arguments are as follows, 
where (1|2) are choices, and [option] is optional.  
/elementals [(enchant|disenchant|info|reload)] [spell]  
This manages anything you need to within the plugin, and other things may be added in the future.  
### Permissions
elementals.admin : This allows you to do anything, similar to *. This is the wildcard permission.  
elementals.reload : Lets the player reload the messages and config.  
elementals.enchant : Allows the player to enchant wands.  
elementals.disenchant : Allows the player to disenchant wands.  
elementals.bypass : This bypasses the cooldown for spells.
###Enjoy!
I hope you enjoy using this plugin, and if you have any suggestions, feedback, or anything of the sort, make sure to 
let me know, either in the [issue tracker](https://www.github.com/Ankoki-Dev/Elementals/issues), or 
[my discord](https://www.discord.gg/aCDNj8s):)
##Developers
There is currently no proper way to register your own spells, however I am working on a way to implement this, 
this spot will be updated when there is a proper way.
