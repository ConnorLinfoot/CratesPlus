CratesPlus
=====================

A free and great crates plugin for Minecraft.


[![Build Status](http://ci.connorlinfoot.com:8080/buildStatus/icon?job=CratesPlus)](http://ci.connorlinfoot.com:8080/job/CratesPlus/)


Changeslogs:


2.1 (In progress, some items may not make the final release):
 - Added "/crate reload" command
 - In-Game crate editing/managing
 - Better titles
 - Custom crate types

2.0.1:
 - Fixed an issue with not been able to run "/crate key" without specifying a key type (Thanks to kyle1264x on Spigot for reporting this)

2.0:
 - An all new config layout (Old versions will be converted to the new version on first run and a backup of the original will be given via a link)
 - Custom crates and a whole new Crate setup
 - Events, Developers can hook in if they wish to do stuff (CrateOpenEvent, CratePreviewEvent)

1.3.1:
 - Fix for preview sizes not working correctly

1.3:
 - You can now use the Interact event if the Inventory Open event doesn't work for you
 - You can now change the plugin prefix

1.2:
 - Added Crate previews
 - Added a winning GUI
 - Can now use item data ID's for items such as wool, spawn eggs etc
 - Added it so you can now specify a crate key type with the command
 - Can now change settings of the key

1.1:
 - Added update checker (Works with Spigot)
 - Minor changes to how the plugin gets the material from the config
 - Minor changes to the color function for the firework
 - Messages can now be changed in the config file