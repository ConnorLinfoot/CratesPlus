### Version 4.4 Snapshots
---
**Snapshot 3** *(Apr 17 2017)* - [Download](http://ci.connorlinfoot.com:8080/job/CratesPlus/864/artifact/target/CratesPlus-4.4-SNAPSHOT-3.jar)
- Feature: Added support for storing all meta data when using the built in GUI for managing winnings. This includes enchantments, potions, books, player heads etc.
- Feature: Added support for ItemFlags (1.9+). Check the list of [Flags](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/inventory/ItemFlag.html).

**Snapshot 2** *(Apr 14 2017)* - [Download](http://ci.connorlinfoot.com:8080/job/CratesPlus/859/artifact/target/CratesPlus-4.4-SNAPSHOT-2.jar)
- Feature: Added new command placeholder "rand" for generating a random number (Example: %rand;100;500% would generate a number between 100 and 500)
- Improvement: Added %displayname% placeholder to commands.
- Improvement: Changed "BasicGUI" default length to 5.
- Bug: Fixed NPE with player interact.

**Snapshot 1** *(Apr 9 2017)* - [Download](http://ci.connorlinfoot.com:8080/job/CratesPlus/851/artifact/target/CratesPlus-4.4-SNAPSHOT-1.jar)
- Feature: You can now set the lore of a key in the config file. (Warning: changing the lore WILL break existing keys given to players)
- Bug: Fixed spawn eggs not working in 1.11+

### Version 4.3 Snapshots
---
**Snapshot 3** *(Apr 1 2017)* - [Download](http://ci.connorlinfoot.com:8080/job/CratesPlus/836/artifact/target/CratesPlus-4.3-SNAPSHOT-3.jar)
- Improvement: Keys lost on death will now be put in the claim GUI instead.
- Improvement: Better handling of rename for 1.7.
- Improvement: I really like purple.
- Improvement: No but seriously, I ***really*** like purple. 

**Snapshot 2** *(Mar 27 2017)* - [Download](http://ci.connorlinfoot.com:8080/job/CratesPlus/826/artifact/target/CratesPlus-4.3-SNAPSHOT-2.jar)
- Improvement: Slight tweaks to the custom metrics class for the upcoming MC Stats.
- Bug: Fixed "Block Data" for the crate itself not working after feature was added in last snapshot.

**Snapshot 1** *(Mar 27 2017)* - [Download](http://ci.connorlinfoot.com:8080/job/CratesPlus/824/artifact/target/CratesPlus-4.3-SNAPSHOT-1.jar)
- Improvement: Now supports "Block Data" for setting the crates data ID.
- Improvement: Debug link now uses v2 of MC Debug, will allow the link that is shared to be viewed easier and even managed.
- Improvement: Keys are now checked with their lore as well as the name, this should help prevent plugins with things like /rename exploiting this.
- Improvement: Added a custom metrics class, will be used with a new MC Stats tool coming soon.
- Bug: Fixed issue with debug link and config backups, now using GitHub Gist instead of Hastebin.
- Bug: Hologram warning now only shows up once if it finds an incompatible version.

### Version 4.2 Snapshots
---
**Snapshot 7** *(Aug 1 2016)* - [Download](http://ci.connorlinfoot.com:8080/job/CratesPlus/578/artifact/target/CratesPlus-4.2-SNAPSHOT-7.jar)
- Spawn Eggs now work in 1.9+, the plugin will store the data separate for this. See example configuration. Run '/crate testeggs' to test this and report any errors.
- Colored names from the settings GUI are now handled with '&' instead of the buggy '§'.
- You can no longer edit a crate via the GUI if it has command items. This will hopefully be improved later on.

**Snapshot 6** *(Aug 1 2016)* - [Download](http://ci.connorlinfoot.com:8080/job/CratesPlus/577/artifact/target/CratesPlus-4.2-SNAPSHOT-6.jar)
- Holograms will no longer load in if the crate isn't found at the location registered, it will also remove it from the data.yml, useful if the crate was removed with something such as world edit. Idea from Mammothskier.
- Removed lines from broadcast message, if you want these add them into the messages.yml and use \n for line breaks.
- Added "Possible Wins Title" option.

**Snapshot 5** *(Jul 4 2016)* - [Download](http://ci.connorlinfoot.com:8080/job/CratesPlus/547/artifact/target/CratesPlus-4.2-SNAPSHOT-5.jar)
- Built in hologram handler is now locked to 1.8 - 1.9 only as 1.7 and 1.10 does not work with it and it'll be removed in 5.0.
- Minor fixes for 1.7, finally decided to actually test it.
- Fixed issue that if it's a players first time playing they can't be given a key.
- Fixed crates not being recognised when not lowercase with opener command.
- Disconnecting when using the GUI Opener will now add a key into your claim GUI instead of it being lost. Other openers must handle this themselves.

**Snapshot 4** *(Jun 28 2016)* - [Download](http://ci.connorlinfoot.com:8080/job/CratesPlus/541/artifact/target/CratesPlus-4.2-SNAPSHOT-4.jar)
- Fixed snapshot updater showing an SSL error.
- Snapshots now force the snapshot updater.
- Fixed crates not being able to be opened since I changed the CrateOpenEvent.

**Snapshot 3** *(Jun 28 2016)* - [Download](http://ci.connorlinfoot.com:8080/job/CratesPlus/540/artifact/target/CratesPlus-4.2-SNAPSHOT-3.jar)
- You can now open the claim GUI as an "admin".
- Minor changes to the claim GUI.
- Added "Claim Join" message, shown when a player joins who has pending keys to claim.
- You can now create a crate using signs.

**Snapshot 2** *(Jun 28 2016)* - [Download](http://ci.connorlinfoot.com:8080/job/CratesPlus/538/artifact/target/CratesPlus-4.2-SNAPSHOT-2.jar)
- Fixed per crate holograms not working if you had capitals in your name.
- Added option for "Chest Sound" with the NoGUI opener.
- Updating from prior 4.1 to 4.2 will now use the correct opener and not default to the BasicGUI opener.
- You can now give offline players crate keys, will go into the claim GUI.
- When keys are given to a player with a full inventory the remaining keys go into the claim GUI.
- Removed the ability to cancel the CrateOpenEvent as if cancelled keys were still being taken.

**Snapshot 1** *(Jun 21 2016)* - [Download](http://ci.connorlinfoot.com:8080/job/CratesPlus/528/artifact/target/CratesPlus-4.2-SNAPSHOT-1.jar)
- Fixed per crate cooldowns not working and issues with cooldown in the config.
- Fixed events so listeners can actually use them now without getting errors.
- You can now rename a crate using the settings GUI, *should* open a sign GUI for input of text.
- If no commands found it will display a warning in console.
- Fixed issues with renames not keeping some data.
- Holograms/Crate Locations will now update after rename, you may need to restart your server currently for this to take affect.
- Fixed errors with 1.7 and Barriers in the menus, currently uses a redstone torch on 1.7.
- Changed the way it handles Bukkit versions. Shouldn't break on new Bukkit updates but will give a warning.
- Added "debug" arg for generating a report on your server and config to help with errors/issues.
- Made some changes to the example config file.
- Will now tell you when it can't find a message instead of just saying "null".
- Removed the chest open sound and added it only into the NoGUI opener as others shouldn't need it nor be forced to use it.

### Version 4.1 Snapshots
---
**Snapshot 8** *(May 27 2016)* - [Download](http://ci.connorlinfoot.com:8080/job/CratesPlus/498/artifact/target/CratesPlus-4.1.0-SNAPSHOT-8.jar)
- Fixed holograms from HolographicDisplays being too low and not removing.
- Fixed holograms being too high with individual holograms and not removing.

**Snapshot 7** *(May 27 2016)* - [Download](http://ci.connorlinfoot.com:8080/job/CratesPlus/497/artifact/target/CratesPlus-4.1.0-SNAPSHOT-7.jar)
- Fixed NPE when trying to use per crate openers.
- "Disable Key Dropping" has been renamed to "Disable Key Swapping" and now blocks putting the keys in a chest and removes the item on death.
- Removed Spigot from dependencies so that I can't accidentally add Spigot only features.

**Snapshot 6** *(May 27 2016)* - [Download](http://ci.connorlinfoot.com:8080/job/CratesPlus/496/artifact/target/CratesPlus-4.1.0-SNAPSHOT-6.jar)
- Improvements to the crate command for openers to match recent changes.
- Added warning if multiple openers are registered with the same name.
- Added support for [Individual Holograms](https://www.spigotmc.org/resources/individual-holograms.8514/) as a hologram handler.
- Added warning about deprecating holograms from the built in handler as I want to leave this to other plugins.
- Fixed issue of stacks that were bigger than max stack size not being rewarded correctly after first time.
- Improved how winnings are selected (I think) changed the way it selects them to be fully based on weight.

**Snapshot 5** *(May 26 2016)* - [Download](http://ci.connorlinfoot.com:8080/job/CratesPlus/494/artifact/target/CratesPlus-4.1.0-SNAPSHOT-5.jar)
- "GUI Time" is no longer used and handled by the "BasicGUI" openers config.
- Added "Cooldown" option per crate and renamed "Cooldown" to "Default Cooldown"
- More major changes to the Opener API that'll once again break previous versions.

**Snapshot 4** *(May 25 2016)* - [Download](http://ci.connorlinfoot.com:8080/job/CratesPlus/492/artifact/target/CratesPlus-4.1.0-SNAPSHOT-4.jar)
- "Use GUI" config option has been removed for the new "Default Opener" option
- Crates can now have an "Opener" per crate in their config.
- Openers can now access their own configuration file for storing options.
- Removed methods for crateName in CrateOpenEvent and CratePreviewEvent.
- Other changes to the Opener API that'll break Openers built for snapshot 3. For instance, openings must now run the finish method once they've done with their opening.

**Snapshot 3** *(May 24 2016)* - [Download](http://ci.connorlinfoot.com:8080/job/CratesPlus/490/artifact/target/CratesPlus-4.1.0-SNAPSHOT-3.jar)
- Added support for Holographic displays if found instead of the built in hologram handler. Please test this and report bugs.
- Thanks to the above the plugin *should* now work with 1.7.
- Major code changes to how opening crates are handled. This shouldn't change the experience at the moment but will allow other plugins to handle the opening of crates in the near future.
- Removed a lot of unnecessary static variables and methods.
- Fixed NPE on plugin load.

**Snapshot 2** *(May 22 2016)* - [Download](http://ci.connorlinfoot.com:8080/job/CratesPlus/485/artifact/target/CratesPlus-4.1.0-SNAPSHOT-2.jar)
- Disable percentages from showing per crate.
- New messages.yml for all messages, current messages should be copied if they exist.
- New message "Chance Message" used for displaying the chance of a winning.
- Now requires Java 7, according to MC Stats nobody is using Java 6. Which is a good thing!
- Removed an old test command (/crates h)

**Snapshot 1** *(May 19 2016)* - [Download](http://ci.connorlinfoot.com:8080/job/CratesPlus/478/artifact/target/CratesPlus-4.1.0-SNAPSHOT-1.jar)
- Per crate holograms, thanks to [xorinzor](https://github.com/xorinzor).
- Better error messages with invalid configuration, thanks to [xorinzor](https://github.com/xorinzor).

### Version 4.0 Snapshots
---
**Snapshot 23** *(Mar 18 2016)* - [Download](http://ci.connorlinfoot.com:8080/job/CratesPlus/410/artifact/target/CratesPlus-4.0.0-SNAPSHOT-23.jar)
- Players are no longer forced to keep the GUI open
- Wins are no longer processed asynchronously to fix bugs/warnings

**Snapshot 22** *(Mar 18 2016)* - [Download](http://ci.connorlinfoot.com:8080/job/CratesPlus/409/artifact/target/CratesPlus-4.0.0-SNAPSHOT-22.jar)
- Fixed issue of GUI "crashing" if firework was enabled

**Snapshot 21** *(Mar 17 2016)* - [Download](http://ci.connorlinfoot.com:8080/job/CratesPlus/408/artifact/target/CratesPlus-4.0.0-SNAPSHOT-21.jar)
- Moved most configuration related stuff into the ConfigHandler
- Added "Ignore Version" option to skip version warnings if running a server which is not recognised as 1.8/1.9
- Added more enchantment nice names

**Snapshot 20** *(Mar 16 2016)* - [Download](http://ci.connorlinfoot.com:8080/job/CratesPlus/404/artifact/target/CratesPlus-4.0.0-SNAPSHOT-20.jar)
- Finished implementation of cooldown between opening crates
- Started implementation of a config handler (Just to make things easier for myself in the future)

**Snapshot 19** *(Mar 15 2016)* - [Download](http://ci.connorlinfoot.com:8080/job/CratesPlus/402/artifact/target/CratesPlus-4.0.0-SNAPSHOT-19.jar)
- Fixed broadcasts not running on non-GUI win.
- Made firework run after winning done.

**Snapshot 18** *(Mar 14 2016)* - [Download](http://ci.connorlinfoot.com:8080/job/CratesPlus/400/artifact/target/CratesPlus-4.0.0-SNAPSHOT-18.jar)
- Increased Hologram delay to hopefully fix issues of it now showing on join
- Block meta data now gets removed on crate destroy

**Snapshot 17** *(Mar 13 2016)* - [Download](http://ci.connorlinfoot.com:8080/job/CratesPlus/397/artifact/target/CratesPlus-4.0.0-SNAPSHOT-17.jar)
- Improvements to holograms

**Snapshot 16** *(Mar 13 2016)* - [Download](http://ci.connorlinfoot.com:8080/job/CratesPlus/395/artifact/target/CratesPlus-4.0.0-SNAPSHOT-16.jar)
- Items should no longer accidentally stack in preview screen.
- Fixed enchantments being broken with levels
- Better handling of invalid enchantments, will no longer completely break and will show a warning in console
- Holograms now use packets! Please report any bugs if you find any with this.

**Snapshot 15** *(Mar 11 2016)* - [Download](http://ci.connorlinfoot.com:8080/job/CratesPlus/391/artifact/target/CratesPlus-4.0.0-SNAPSHOT-15.jar)
- Disabled "createtest" command, will be adding this most likely in v4.1/5.0.
- Decimal support completed, should now take into account decimals when doing winnings.
- Fixed bug of it giving multiple items on win without a GUI enabled.
- Minor clean ups on new code for data.yml, removing a crate now also removes it from the data.yml.
- Other clean up on code around in places too.

**Snapshot 14** *(Mar 10 2016)* - [Download](http://ci.connorlinfoot.com:8080/job/CratesPlus/389/artifact/target/CratesPlus-4.0.0-SNAPSHOT-14.jar)
- Fixed issue with data.yml (For real REAL this time! Stupid regex!)

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

### Version 3.1 Snapshots
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

### Version 3.0 Snapshots
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