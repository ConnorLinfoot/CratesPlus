**4.2.3** *(Dec 20 2016)* - [Download](https://www.spigotmc.org/resources/cratesplus-free-crates-plugin-1-7-1-11-1.5018/download?version=130903)
- Feature: Support for Bukkit/Spigot 1.11.1.

**4.2.2** *(Dec 11 2016)* - [Download](https://www.spigotmc.org/resources/cratesplus-free-crates-plugin-1-7-1-10-2.5018/download?version=129396)
- Feature: Support for Bukkit/Spigot 1.11.
- Bug: Fixed command being ran async.
- Bug: Fixed issue with location being null stopping plugin starting correctly.
- Bug: Fixed block break on 1.7 (hopefully, not actually tested this)

**4.2.1** *(Aug 3 2016)* - [Download](https://www.spigotmc.org/resources/cratesplus-free-crates-plugin-1-7-1-10-2.5018/download?version=106853)
- Bug: Fixed issue with the runnable causing an error on Bukkit 1.7.
- Bug: Fixed issue when breaking a crate with 1.7 would cause an exception.


**4.2** *(Aug 1 2016)* - [Download](https://www.spigotmc.org/resources/cratesplus-free-crates-plugin-1-8-1-9-1-10.5018/download?version=106361)
- Feature: Crates can now be created and renamed using a sign GUI.
- Feature: New '/crate debug' for help when reporting issues.
- Feature: You can now give offline players crate keys, will go into the claim GUI.
- Feature: When keys are given to a player with a full inventory the remaining keys go into the claim GUI.
- Feature: Spawn Eggs now work in 1.9+, the plugin will store the data separate for this. See example configuration. Run '/crate testeggs' to test this and report any errors.
- Improvement: If a winning is a command but has no commands it will now show a warning.
- Improvement: Changed how it handles different Bukkit versions. Shouldn't break on newer versions but will display a warning.
- Improvement: Added "Claim Join" message, shown when a player joins who has pending keys to claim.
- Improvement: Disconnecting when using the GUI Opener will now add a key into your claim GUI instead of it being lost. Other openers must handle this themselves.
- Improvement: Added "Possible Wins Title" option.
- Improvement: Removed lines from broadcast message, if you want these add them into the messages.yml and use \n for line breaks.
- Improvement: Holograms will no longer load in if the crate isn't found at the location registered, it will also remove it from the data.yml, useful if the crate was removed with something such as world edit. Idea from Mammothskier.
- Improvement: You can no longer edit a crate via the GUI if it has command items. This will hopefully be improved later on.
- Bug: Fixed per crate cooldowns not working and other issues with cooldowns in the config.
- Bug: Fixed events so listeners can now actually use them.
- Bug: Fixed some data being lost upon renaming of a crate.
- Bug: Fixed issues with 1.7 and barriers in menus and other minor 1.7 issues.
- Bug: Moved the chest open sound to the NoGUI opener as it shouldn't be used on others.
- Bug: Fixed per crate holograms not working if you had capitals in your name.
- Bug: Updating from prior 4.1 to 4.2 will now use the correct opener and not default to the BasicGUI opener.
- Bug: Removed the ability to cancel the CrateOpenEvent as if cancelled keys were still being taken.
- Bug: Fixed snapshot updater showing an SSL error.
- Bug: Fixed crates not being recognised when not lowercase with opener command.
- Bug: Built in hologram handler is now locked to 1.8 - 1.9 only as 1.7 and 1.10 does not work with it and it'll be removed in 5.0.
- Bug: Colored names from the settings GUI are now handled with '&' instead of the buggy '§'.

**4.1.1** *(Jun 11 2016)* - [Download](https://www.spigotmc.org/resources/cratesplus-free-crates-plugin-1-8-1-9-1-10.5018/download?version=95734)
- Feature: Added 1.10 support.

**4.1** *(May 27 2016)* - [Download](https://www.spigotmc.org/resources/cratesplus-free-crates-plugin-1-7-1-8-1-9.5018/download?version=91898)
- Feature: Openers! An easy way for any developer to create a custom way of opening crates!
- Feature: Holograms can now be per crate.
- Feature: Holograms will now try to use the Individual Holograms plugin or HolographicDisplays plugin. The built in handler still works but will be removed in the future.
- Feature: Because of the hologram changes mentioned above this plugin now supports CraftBukkit/Spigot 1.7.
- Improvement: "Disable Key Dropping" has been renamed to "Disable Key Swapping" and now stops keys being dropped (including on death) and from putting the keys in a chest/other inventory.
- Improvement: Messages are now in their own file - messages.yml.
- Improvement: New "Chance Message" in messages.yml to customize chance message.
- Improvement: Hide percentages all together per crate with "Hide Percentages"
- Improvement: Better error messages when there is invalid configuration.
- Bug: Fixed decimal percentages being worked out correctly.
- Bug: Fixed stacks bigger than max stack sized being reset to max size after being won once.
- From this version CratesPlus now requires at least Java 7.

**4.0** *(Mar 18 2016)* - [Download](https://www.spigotmc.org/resources/cratesplus-custom-crates-1-8-1-9-free.5018/download?version=76292)
- Feature: Keys now work per crate and not on a global level
- Feature: Added new %winning%/%prize% placeholder
- Feature: Added color selector to in-game editor
- Feature: Added preview option to crates
- Feature: Crates can now have a block type
- Feature: Option to disable keys from being dropped
- Feature: Percentages now support decimals
- Feature: New cooldown option to stop spamming of crates (Mostly with no GUI)
- Feature: Added per crate option to have a required permission
- Feature: Players are no longer "forced" to keep the GUI's open and can now leave and re-open the GUI
- Improvement: Improved 1.9 support such as off-hand block checking/opening
- Improvement: Improved how enchantments are added, now supports working like "sharpness"
- Improvement: Better handling of invalid enchantments, will no longer completely break and will show a warning in console
- Improvement: Holograms now use packets
- Improvement: Holograms now support unlimited lines
- Improvement: Message now shows in console if percentages add to more than 100%
- Improvement: Lore's no longer contain the percentage once the winning item is given
- Improvement: Added fallback for when type is BLOCK and Block Type is used
- Improvement: You can now specify an amount of keys to give with the key command
- Improvement: Color code support for hologram's added
- Improvement: Broadcast and Firework now run after crate has actually opened
- Improvement: Removed unnecessary util for handling json
- Bug: Fixed percentages not working if you changed the GUI time
- Bug: Having items with stacks larger than max stack size should no longer cause issues/break
- Bug: Commands no longer also give the player the item
- Also changed the package from "com.connorlinfoot.cratesplus" to "plus.crates" so anyone using the events will need to update their imports

**3.1.7** *(Mar 5 2016)* - [Download](https://www.spigotmc.org/resources/cratesplus-custom-crates-free-1-8-x.5018/download?version=73107)
- Bug: Fixed lore duplication bug when editing crates

**3.1.6** *(Mar 4 2016)* - [Download](https://www.spigotmc.org/resources/cratesplus-custom-crates-free-1-8-x.5018/download?version=72888)
- Bug: Fixed CratesPlus not working correctly in CraftBukkit and players been able to take items as a result of this.

**3.1.5** *(Mar 1 2016)* - [Download](https://www.spigotmc.org/resources/cratesplus-custom-crates-free-1-8-x.5018/download?version=71841)
- Bug: Fixed giving 2 items on win!

**3.1.4** *(Feb 29 2016)* - [Download](https://www.spigotmc.org/resources/cratesplus-custom-crates-free-1-8-x.5018/download?version=71810)
- Feature: Added compatibility with 1.8 AND 1.9

**3.1.3** *(Feb 29 2016)* - [Download](https://www.spigotmc.org/resources/cratesplus-custom-crates-free-1-8-x.5018/download?version=71808)
- Feature: Updated to work with Spigot 1.9
- Bug: Fixed percentages not working if you changed the GUI time
- Bug: Commands no longer also give the player the item

**3.1.2** *(Sep 19 2015)* - [Download](https://www.spigotmc.org/resources/cratesplus-custom-crates-free-1-8-x.5018/download?version=45323)
- Bug: Fixed an issue with inventory click and CraftBukkit

**3.1.1** *(Sep 18 2015)* - [Download](https://www.spigotmc.org/resources/cratesplus-custom-crates-free-1-8-x.5018/download?version=45143)
- Bug: Fixed backup config not working

**3.1** *(Sep 18 2015)* - [Download](https://www.spigotmc.org/resources/cratesplus-custom-crates-free-1-8-x.5018/download?version=45142)
- Feature: Added config options to be able to change the text shown on holograms (Support up to 4 lines, removed more-info hologram option since this has been added)
- Feature: GUI now has a nice little animation, along with config options to fully disable the GUI and change the time.
- Feature: Added percentage support considering it was asked for so much, percentages in a crate MUST add up to 100% for it to function correctly
- Improvement: Better invalid item handling, previews will skip invalid items and wins will show an error if no valid items is found after 5 tries
- Improvement: Players can't open crate if inventory is full
- Improvement: All new config style (Again) Designed to make it easier and better for winnings including better command support
- Improvement: Commands can now have a item other than paper and custom names
- Improvement: Lore Support (Should work with most custom enchantments)
- Improvement: Default config no longer contains any crates, see example config on Spigot page
- Improvement: Crate Keys now have a "Enchanted" option instead of "Enchantments" list
- Improvement: Now gives the player the item won if they close the GUI without selecting the prize
- Improvement: After clicking winning item GUI will auto-close

**3.0.3** *(Aug 3 2015)* - [Download](https://www.spigotmc.org/resources/cratesplus-custom-crates-free-1-8-x.5018/download?version=37596)
- Bug: Fixed a small bug that's annoyed me for a while where if the player had multiple of the same key in different slots it would take all the keys.

**3.0.2** *(Jun 25 2015)* - [Download](https://www.spigotmc.org/resources/cratesplus-custom-crates-free-1-8-x.5018/download?version=31200)
- Improvement: Added Metrics for tracking stats.

**3.0.1** *(Jun 7 2015)* - [Download](https://www.spigotmc.org/resources/cratesplus-custom-crates-free-1-8-x.5018/download?version=28510)
- Bug: Fixed updater not working correctly

**3.0** *(Jun 7 2015)* - [Download](https://www.spigotmc.org/resources/cratesplus-custom-crates-free-1-8-x.5018/download?version=28505)
- Feature: Added "/crate settings" - Allows in-game crate editing
- Feature: Added "/crate create <name>"
- Feature: Added "/crate delete <name>"
- Feature: Added "/crate reload"
- Feature: Added "/crate key all [type]"
- Feature: Added "/crate rename <old> <new>"
- Feature: Custom item names (View new config, it should auto update and give you a link for a backup)
- Improvement: You must now "sneak" to break Crates
- Improvement: Left clicking a Crate will ALWAYS show you the winnings available
- Improvement: Right clicking a crate will ALWAYS attempt to use a key, Knockback will be used if no key is been held
- Improvement: Added "More Info Hologram" to config for better hologram info
- Improvement: Removed "non-gui" options from config
- Improvement: Removed "interact" option from config, now always uses interact event
- Improvement: Crate names are no longer case sensitive
- Bug: Fixed issue with data not always being used correctly

**2.0.1** *(Apr 8 2015)* - [Download](https://www.spigotmc.org/resources/cratesplus-custom-crates-free-1-8-x.5018/download?version=20207)
- Bug: Fixed an issue with not been able to run "/crate key" without specifying a key type (Thanks to kyle1264x on Spigot for reporting this)

**2.0** *(Apr 7 2015)* - [Download](https://www.spigotmc.org/resources/cratesplus-custom-crates-free-1-8-x.5018/download?version=20057)
- Feature: Custom crates and a whole new Crate setup
- Feature: Events, Developers can hook in if they wish to do stuff (CrateOpenEvent, CratePreviewEvent)
- Improvement: An all new config layout (Old versions will be converted to the new version on first run and a backup of the original will be given via a link)

**1.3.1** *(Apr 7 2015)* - [Download](https://www.spigotmc.org/resources/cratesplus-custom-crates-free-1-8-x.5018/download?version=20026)
- Bug: Fix for preview sizes not working correctly

**1.3** *(Mar 25 2015)* - [Download](https://www.spigotmc.org/resources/cratesplus-custom-crates-free-1-8-x.5018/download?version=18365)
- Improvement: You can now use the Interact event if the Inventory Open event doesn't work for you
- Improvement: You can now change the plugin prefix

**1.2** *(Mar 21 2015)* - [Download](https://www.spigotmc.org/resources/cratesplus-custom-crates-free-1-8-x.5018/download?version=17899)
- Feature: Added Crate previews
- Feature: Added a winning GUI
- Feature: Can now change settings of the key
- Improvement: Can now use item data ID's for items such as wool, spawn eggs etc
- Improvement: Added it so you can now specify a crate key type with the command

**1.1** *(Mar 18 2015)* - [Download](https://www.spigotmc.org/resources/cratesplus-custom-crates-free-1-8-x.5018/download?version=17519)
- Feature: Added update checker (Works with Spigot)
- Feature: Messages can now be changed in the config file
- Improvement: Minor changes to how the plugin gets the material from the config
- Improvement: Minor changes to the color function for the firework

**1.0** *(Mar 18 2015)* - [Download](https://www.spigotmc.org/resources/cratesplus-custom-crates-free-1-8-x.5018/download?version=17486)
- Initial release!