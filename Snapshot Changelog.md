### Version 4.0.0 Snapshots
---
**Snapshot 13** *(Mar 10 2016)* - [Download](http://ci.connorlinfoot.com:8080/job/CratesPlus/386/artifact/target/CratesPlus-4.0.0-SNAPSHOT-13.jar)
- Added per crate option to have a required permission
- Changelogs are now .md files (Little nicer to view)
- Fixed issue with data.yml (For real this time! Stupid negative cords!)

**Snapshot 12** *(Mar 9 2016)* - [Download](http://ci.connorlinfoot.com:8080/job/CratesPlus/382/artifact/target/CratesPlus-4.0.0-SNAPSHOT-12.jar)
- Hopefully fixed issue with data.yml and changed format (You will need to place crates again...)

**Snapshot 11** *(Mar 8 2016)* - [Download](http://ci.connorlinfoot.com:8080/job/CratesPlus/382/artifact/target/CratesPlus-4.0.0-SNAPSHOT-11.jar)
- Fixed issue where block meta data wasn't being kept on restart, now stores all crate data in a data.yml file. (You will need to place crates again!)
- Some custom work on getting enchantments from nicer names like "sharpness", will hopefully improve this more soon.

**Snapshot 10** *(Mar 7 2016)* - [Download](http://ci.connorlinfoot.com:8080/job/CratesPlus/378/artifact/target/CratesPlus-4.0.0-SNAPSHOT-10.jar)
- Fixed NPE on block break
- Added "Item ID" which will attempt to get an item type by its ID
- Added "createbeta" argument which will use a sign to get the name for the crate. Just a test concept, not done yet!
- Fixed issue where crate keys were not enchanted
- Removed bottom line from chance lore

**Snapshot 9** *(Mar 5 2016)* - [Download](http://ci.connorlinfoot.com:8080/job/CratesPlus/375/artifact/target/CratesPlus-4.0.0-SNAPSHOT-9.jar)
- Added block to example config
- Hopefully fixed an issue with an NPE and 1.9 off hand support
- Added option to block keys been dropped
- Now shows plugin version when running /crate and made help commands nicer
- Updated "Create" command with new options
- Updated "Rename" command with new options and made some slight changes

**Snapshot 8** *(Mar 4 2016)* - [Download](http://ci.connorlinfoot.com:8080/job/CratesPlus/374/artifact/target/CratesPlus-4.0.0-SNAPSHOT-8.jar)
- Better handling of config for crates
- Changed codebase to use tabs instead of 4 spaces
- New Block option for crates should now work, give it a test. It shouldn't break already placed crates but we'll see!
- Added some stuff with tracking locations for future changes to holograms

**Snapshot 7** *(Mar 3 2016)* - [Download](http://ci.connorlinfoot.com:8080/job/CratesPlus/372/artifact/target/CratesPlus-4.0.0-SNAPSHOT-7.jar)
- Completed crate color selector
- Added preview option to crates
- Renamed package to plus.crates from com.connorlinfoot.cratesplus
- Fixed NumberFormatException when using Spigot update checker on snapshot builds
- Fixed issues with some placeholders not working correctly
- Added 1.9 compatibility (Keeping support for 1.8 too!)
- Added support for off hand when opening crates in 1.9
- Disabled block placing of key in off hand in 1.9

**Snapshot 6** *(Feb 24 2016)* - [Download](http://ci.connorlinfoot.com:8080/job/CratesPlus/363/artifact/target/CratesPlus-4.0.0-SNAPSHOT-6.jar)
- Updated snapshot updater link to new API
- Fixed lore being duplicated on save in GUI editor
- Fixed GUI editor not even working most of the time
- Started work on changing crate color by GUI, will be finished in next snapshot

**Snapshot 5** *(Feb 23 2016)* - [Download](http://ci.connorlinfoot.com:8080/job/CratesPlus/360/artifact/target/CratesPlus-4.0.0-SNAPSHOT-5.jar)
- Removed old InventoryClose listener file
- Removed deprecated methods
- Cleaned up bits of code and utils
- Removed unnecessary util for handling json

**Snapshot 4** *(Feb 22 2016)* - [Download](http://ci.connorlinfoot.com:8080/job/CratesPlus/358/artifact/target/CratesPlus-4.0.0-SNAPSHOT-4.jar)
- Fixed crate broadcast running 44 times on win
- Added snapshot branch for updates

**Snapshot 3** *(Feb 22 2016)* - [Download](http://ci.connorlinfoot.com:8080/job/CratesPlus/355/artifact/target/CratesPlus-4.0-SNAPSHOT-3.jar)
- Added new %winning% placeholder
- Improved placeholders so they should be handled a bit nicer with messages and also holograms

**Snapshot 2** *(Feb 22 2016)* - [Download](http://ci.connorlinfoot.com:8080/job/CratesPlus/354/artifact/target/CratesPlus-4.0-SNAPSHOT-2.jar)
- Added fallbacks for when type is BLOCK and Block Type is used
- Color code support for hologram's added
- Fixed amount not working with /crate key

**Snapshot 1** *(Dec 14 2015)* - [Download](http://ci.connorlinfoot.com:8080/job/CratesPlus/284/artifact/target/CratesPlus-4.0-SNAPSHOT-1.jar)
- Moved Crate Key settings under the individual crates, allows different keys per crates!
- Fixed percentages not working if you changed the GUI time
- Message now shows in console if percentages add up to more than 100%
- Having items with stacks larger than max stack size should no longer breaks inventories/have weird issues
- Lore no longer contains percentage after player has been given the item
- Commands no longer also give the player the item
- Changes to key command - You can now specify an amount of keys to give
- Should now work with CraftBukkit and not just Spigot...
- Minor config conversion added
- Minor code cleaning
- I would recommend not disabling the GUI as the non-gui win doesn't work correctly at the moment. May end up dropping it.

### Version 3.1.0 Snapshots
---
**Snapshot 15** *(Sep 14 2015)* - [Download](http://ci.connorlinfoot.com:8080/job/CratesPlus/191/artifact/target/CratesPlus-3.1-SNAPSHOT-15.jar)
- Lore's now support color codes
- Fixed an issue with non-gui not working when no percentages are being used and they also now run commands

**Snapshot 14** *(Sep 14 2015)* - [Download](http://ci.connorlinfoot.com:8080/job/CratesPlus/190/artifact/target/CratesPlus-3.1-SNAPSHOT-14.jar)
- Added Lore support to Crates.
- Fixed a small bug which showed the wrong error when using /crate crate

**Snapshot 13** *(Sep 14 2015)* - [Download](http://ci.connorlinfoot.com:8080/job/CratesPlus/189/artifact/target/CratesPlus-3.1-SNAPSHOT-13.jar)
- Removed Crates from Example config. You must now setup at least 1 crate before getting started.

**Snapshot 12** *(Sep 14 2015)* - [Download](http://ci.connorlinfoot.com:8080/job/CratesPlus/188/artifact/target/CratesPlus-3.1-SNAPSHOT-12.jar)
- Disable GUI now works... We're close!

**Snapshot 11** *(Sep 9 2015)* - [Download](http://ci.connorlinfoot.com:8080/job/CratesPlus/182/artifact/target/CratesPlus-3.1-SNAPSHOT-11.jar)
- Yes I know... It's been a while (Shh)
- Added options to disable GUI for winning and change time. (Disable GUI doesn't work yet)
- Probably 1 more snapshot before release this weekend!

**Snapshot 10** *(Aug 10 2015)* - [Download](http://ci.connorlinfoot.com:8080/job/CratesPlus/151/artifact/target/CratesPlus-3.1-SNAPSHOT-10.jar)
- Better system for remembering commands that doesn't require the use of Spigot
- Cleaned up bits of code
- Final testing will happen tonight for V3.1, if successful expect very close full release

**Snapshot 9** *(Aug 10 2015)* - [Download](http://ci.connorlinfoot.com:8080/job/CratesPlus/150/artifact/target/CratesPlus-3.1-SNAPSHOT-9.jar)
- Added V3 -> V4 Config Converter (Pls test<3)

**Snapshot 8** *(Aug 10 2015)* - [Download](http://ci.connorlinfoot.com:8080/job/CratesPlus/149/artifact/target/CratesPlus-3.1-SNAPSHOT-8.jar)
- Fixed create command to work with the new config system
- Fixed rename command to work with the new config system

**Snapshot 7** *(Aug 10 2015)* - [Download](http://ci.connorlinfoot.com:8080/job/CratesPlus/148/artifact/target/CratesPlus-3.1-SNAPSHOT-7.jar)
- Added percentage support considering it was asked for so much, percentages in a crate MUST add up to 100% for it to function correct

**Snapshot 6** *(Aug 3 2015)* - [Download](http://ci.connorlinfoot.com:8080/job/CratesPlus/140/artifact/target/CratesPlus-3.1-SNAPSHOT-6.jar)
- Commands now working, see default config for example
- Command item type's added, now set your command to any item and not just the default map
- Fixed bug when you had the same key in multiple inventory slots it would take all of them
- Fixed NONE showing up as item names
- "&" color code supported added to item names
- Fixed wins not been "random"

**Snapshot 5** *(Jun 21 2015)* - [Download](http://ci.connorlinfoot.com:8080/job/CratesPlus/96/artifact/target/CratesPlus-3.1-SNAPSHOT-5.jar)
- Updates to the Beta GUI, more coming soon
- All new config style (Again) Designed to make it easier and better for winnings including better command support - NOTE There is currently NO config converter, do NOT use this with an old config file!
- Commands will NOT work correctly in this version, I'm working on that for snapshot 6
- Added Metrics, this can be disabled in the config. I've added this so I can track stats, I may build a custom system in the future
- Beta GUI is now always used, the option in the config has been removed and no longer affects anything

**Snapshot 4** *(Jun 16 2015)* - [Download](http://ci.connorlinfoot.com:8080/job/CratesPlus/89/artifact/target/CratesPlus-3.1-SNAPSHOT-4.jar)
- Added "None" support to the commands name which will just default to "Command"
- Added "Enable GUI Beta Animation" to the config, have fun ;) Improvements will be coming
- Crate Keys now have a "Enchanted" option instead of "Enchantments" list

**Snapshot 3** *(Jun 14 2015)* - [Download](http://ci.connorlinfoot.com:8080/job/CratesPlus/87/artifact/target/CratesPlus-3.1-SNAPSHOT-3.jar)
- Custom name support added to commands
- Multiple command support, separate commands by a pipe |

**Snapshot 2** *(Jun 14 2015)* - [Download](http://ci.connorlinfoot.com:8080/job/CratesPlus/86/artifact/target/CratesPlus-3.1-SNAPSHOT-2.jar)
 - Added config options to be able to change the text shown on holograms (Support up to 4 lines, removed more-info hologram option since this has been added)
 - Now gives the player the item won if they close the GUI without selecting the prize
 - After clicking winning item GUI will auto-close
 - Players can't open crate if inventory is full
 - Improved invalid item handling (Don't think this even worked in snapshot 1)

**Snapshot 1** *(Jun 14 2015)* - [Download](http://ci.connorlinfoot.com:8080/job/CratesPlus/85/artifact/target/CratesPlus-3.1-SNAPSHOT-1.jar)
- Better invalid item handling, previews will skip invalid items and wins will show an error if no valid item is found after 5 tries

### Version 3.0.0 Snapshots
---
**Snapshot 6** *(Jun 7 2015)* - [Download](http://ci.connorlinfoot.com:8080/job/CratesPlus/74/artifact/target/CratesPlus-3.0-SNAPSHOT-6.jar)
- Changed GUI's and reload GUI option works
- Added "/crate rename <old> <new>"
- Cleaned up updater a little

**Snapshot 5** *(Jun 7 2015)* - [Download](http://ci.connorlinfoot.com:8080/job/CratesPlus/73/artifact/target/CratesPlus-3.0-SNAPSHOT-5.jar)
- Removed auto updating from config (Don't really want this actually)
- Removed option for "dev" branch, always uses Spigot
- Won't check for updates if running a snapshot build
- Crate names are no longer case sensitive
- Fixed more issues with how item data was handled

**Snapshot 4** *(Jun 6 2015)* - [Download](http://ci.connorlinfoot.com:8080/job/CratesPlus/72/artifact/target/CratesPlus-3.0-SNAPSHOT-4.jar)
- Custom item names (View new config, it should auto update and give you a link for a backup)
- Removed "listener" debug message
- Fixed some issues with creating/deleting crates
- Added "warning" for when a version is a snapshot
- You must now "sneak" to break Crates
- Left clicking a Crate will ALWAYS show you the winnings available
- Right clicking a crate will ALWAYS attempt to use a key, Knockback will be used if no key is been held
- Added "More Info Hologram" to config for better hologram info
- Removed "non-gui" options from config
- Removed "interact" option from config, now always uses interact event
- Fixed bug with commands been ran even on previews
- Fixed bug with data not always been used correctly
- Fixed bug when closing winnings GUI

**Snapshot 3** *(Jun 5 2015)* - [Download](http://ci.connorlinfoot.com:8080/job/CratesPlus/70/artifact/target/CratesPlus-3.0-SNAPSHOT-3.jar)
- Improved crate winnings reload
- Fixes for crate deletion and creation

**Snapshot 2** *(Jun 5 2015)* - [Download](http://ci.connorlinfoot.com:8080/job/CratesPlus/68/artifact/target/CratesPlus-3.0-SNAPSHOT-2.jar)
- Fixed an issue with crate winnings not updating (Temp fix, will be improved soon)

**Snapshot 1** *(Jun 5 2015)* - [Download](http://ci.connorlinfoot.com:8080/job/CratesPlus/67/artifact/target/CratesPlus-3.0-SNAPSHOT-1.jar)
- Worked on updater, still got more work to do but it's more stable.
- Added /crate settings - Allows in-game crate editing, some features will be added soon
- Added /crate create <name>
- Added /crate delete <name>
- Added /crate reload
- Added /crate key all [type]
- Sound on crate opening when using interact event
- Bug fixes and better handling with non-found crates