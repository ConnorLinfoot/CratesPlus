**4.0.0** *(Mar 18 2016)* - [Download](https://www.spigotmc.org/resources/cratesplus-custom-crates-1-8-1-9-free.5018/download?version=76292)
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

**3.1.0** *(Sep 18 2015)* - [Download](https://www.spigotmc.org/resources/cratesplus-custom-crates-free-1-8-x.5018/download?version=45142)
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

**3.0.0** *(Jun 7 2015)* - [Download](https://www.spigotmc.org/resources/cratesplus-custom-crates-free-1-8-x.5018/download?version=28505)
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

**2.0.0** *(Apr 7 2015)* - [Download](https://www.spigotmc.org/resources/cratesplus-custom-crates-free-1-8-x.5018/download?version=20057)
- Feature: Custom crates and a whole new Crate setup
- Feature: Events, Developers can hook in if they wish to do stuff (CrateOpenEvent, CratePreviewEvent)
- Improvement: An all new config layout (Old versions will be converted to the new version on first run and a backup of the original will be given via a link)

**1.3.1** *(Apr 7 2015)* - [Download](https://www.spigotmc.org/resources/cratesplus-custom-crates-free-1-8-x.5018/download?version=20026)
- Bug: Fix for preview sizes not working correctly

**1.3.0** *(Mar 25 2015)* - [Download](https://www.spigotmc.org/resources/cratesplus-custom-crates-free-1-8-x.5018/download?version=18365)
- Improvement: You can now use the Interact event if the Inventory Open event doesn't work for you
- Improvement: You can now change the plugin prefix

**1.2.0** *(Mar 21 2015)* - [Download](https://www.spigotmc.org/resources/cratesplus-custom-crates-free-1-8-x.5018/download?version=17899)
- Feature: Added Crate previews
- Feature: Added a winning GUI
- Feature: Can now change settings of the key
- Improvement: Can now use item data ID's for items such as wool, spawn eggs etc
- Improvement: Added it so you can now specify a crate key type with the command

**1.1.0** *(Mar 18 2015)* - [Download](https://www.spigotmc.org/resources/cratesplus-custom-crates-free-1-8-x.5018/download?version=17519)
- Feature: Added update checker (Works with Spigot)
- Feature: Messages can now be changed in the config file
- Improvement: Minor changes to how the plugin gets the material from the config
- Improvement: Minor changes to the color function for the firework

**1.0.0** *(Mar 18 2015)* - [Download](https://www.spigotmc.org/resources/cratesplus-custom-crates-free-1-8-x.5018/download?version=17486)
- Initial release!