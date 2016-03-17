**4.0.0** *(Mar 18 2016)* - Coming Soon
- Feature: Keys now work per crate and not on a global level
- Feature: Added new %winning% placeholder
- Feature: Added color selector to in-game editor
- Feature: Added preview option to crates
- Feature: Crates can now have a block type
- Feature: Option to disable keys from being dropped
- Feature: Percentages now support decimals
- Feature: New cooldown option to stop spamming of crates (Mostly with no GUI)
- Feature: Added per crate option to have a required permission
- Improvement: Improved 1.9 support such as off-hand block checking/opening
- Improvement: Improved how enchantments are added, now supports working like "sharpness"
- Improvement: Better handling of invalid enchantments, will no longer completely break and will show a warning in console
- Improvement: Holograms now use packets
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
- Fixed lore duplication bug when editing crates

**3.1.6** *(Mar 4 2016)* - [Download](https://www.spigotmc.org/resources/cratesplus-custom-crates-free-1-8-x.5018/download?version=72888)
- Fixed CratesPlus not working correctly in CraftBukkit and players been able to take items as a result of this.

**3.1.5** *(Mar 1 2016)* - [Download](https://www.spigotmc.org/resources/cratesplus-custom-crates-free-1-8-x.5018/download?version=71841)
- Fixed bug with it giving 2 items on win!

**3.1.4** *(Feb 29 2016)* - [Download](https://www.spigotmc.org/resources/cratesplus-custom-crates-free-1-8-x.5018/download?version=71810)
- Added compatibility with 1.8 AND 1.9 ;D

**3.1.3** *(Feb 29 2016)* - [Download](https://www.spigotmc.org/resources/cratesplus-custom-crates-free-1-8-x.5018/download?version=71808)
- Fixed percentages not working if you changed the GUI time
- Commands no longer also give the player the item
- Updated to work with Spigot 1.9

**3.1.2** *(Sep 19 2015)* - [Download](https://www.spigotmc.org/resources/cratesplus-custom-crates-free-1-8-x.5018/download?version=45323)
- Fixed an issue with inventory click and CraftBukkit

**3.1.1** *(Sep 18 2015)* - [Download](https://www.spigotmc.org/resources/cratesplus-custom-crates-free-1-8-x.5018/download?version=45143)
- Fixed backup config not working

**3.1.0** *(Sep 18 2015)* - [Download](https://www.spigotmc.org/resources/cratesplus-custom-crates-free-1-8-x.5018/download?version=45142)
- Better invalid item handling, previews will skip invalid items and wins will show an error if no valid items is found after 5 tries
- Added config options to be able to change the text shown on holograms (Support up to 4 lines, removed more-info hologram option since this has been added)
- Players can't open crate if inventory is full
- All new config style (Again) Designed to make it easier and better for winnings including better command support
- Commands can now have a item other than paper and custom names
- Lore Support (Should work with most custom enchantments)
- GUI now has a nice little animation, along with config options to fully disable the GUI and change the time.
- Default config no longer contains any crates, see example config on Spigot page
- Added percentage support considering it was asked for so much, percentages in a crate MUST add up to 100% for it to function correctly
- Crate Keys now have a "Enchanted" option instead of "Enchantments" list
- Now gives the player the item won if they close the GUI without selecting the prize
- After clicking winning item GUI will auto-close

**3.0.3** *(Aug 3 2015)* - [Download](https://www.spigotmc.org/resources/cratesplus-custom-crates-free-1-8-x.5018/download?version=37596)
- Fixed a small bug that's annoyed me for a while where if the player had multiple of the same key in different slots it would take all the keys.

**3.0.2** *(Jun 25 2015)* - [Download](https://www.spigotmc.org/resources/cratesplus-custom-crates-free-1-8-x.5018/download?version=31200)
- Added Metrics for tracking stats.

**3.0.1** *(Jun 7 2015)* - [Download](https://www.spigotmc.org/resources/cratesplus-custom-crates-free-1-8-x.5018/download?version=28510)
- Fixed updater not working correctly

**3.0.0** *(Jun 7 2015)* - [Download](https://www.spigotmc.org/resources/cratesplus-custom-crates-free-1-8-x.5018/download?version=28505)
- Added "/crate settings" - Allows in-game crate editing
- Added "/crate create <name>"
- Added "/crate delete <name>"
- Added "/crate reload"
- Added "/crate key all [type]"
- Added "/crate rename <old> <new>"
- You must now "sneak" to break Crates
- Left clicking a Crate will ALWAYS show you the winnings available
- Right clicking a crate will ALWAYS attempt to use a key, Knockback will be used if no key is been held
- Added "More Info Hologram" to config for better hologram info
- Removed "non-gui" options from config
- Removed "interact" option from config, now always uses interact event
- Custom item names (View new config, it should auto update and give you a link for a backup)
- Crate names are no longer case sensitive
- Fixed bug with data not always been used correctly

**2.0.1** *(Apr 8 2015)* - [Download](https://www.spigotmc.org/resources/cratesplus-custom-crates-free-1-8-x.5018/download?version=20207)
- Fixed an issue with not been able to run "/crate key" without specifying a key type (Thanks to kyle1264x on Spigot for reporting this)

**2.0.0** *(Apr 7 2015)* - [Download](https://www.spigotmc.org/resources/cratesplus-custom-crates-free-1-8-x.5018/download?version=20057)
- An all new config layout (Old versions will be converted to the new version on first run and a backup of the original will be given via a link)
- Custom crates and a whole new Crate setup
- Events, Developers can hook in if they wish to do stuff (CrateOpenEvent, CratePreviewEvent)

**1.3.1** *(Apr 7 2015)* - [Download](https://www.spigotmc.org/resources/cratesplus-custom-crates-free-1-8-x.5018/download?version=20026)
- Fix for preview sizes not working correctly

**1.3.0** *(Mar 25 2015)* - [Download](https://www.spigotmc.org/resources/cratesplus-custom-crates-free-1-8-x.5018/download?version=18365)
- You can now use the Interact event if the Inventory Open event doesn't work for you
- You can now change the plugin prefix

**1.2.0** *(Mar 21 2015)* - [Download](https://www.spigotmc.org/resources/cratesplus-custom-crates-free-1-8-x.5018/download?version=17899)
- Added Crate previews
- Added a winning GUI
- Can now use item data ID's for items such as wool, spawn eggs etc
- Added it so you can now specify a crate key type with the command
- Can now change settings of the key

**1.1.0** *(Mar 18 2015)* - [Download](https://www.spigotmc.org/resources/cratesplus-custom-crates-free-1-8-x.5018/download?version=17519)
- Added update checker (Works with Spigot)
- Minor changes to how the plugin gets the material from the config
- Minor changes to the color function for the firework
- Messages can now be changed in the config file

**1.0.0** *(Mar 18 2015)* - [Download](https://www.spigotmc.org/resources/cratesplus-custom-crates-free-1-8-x.5018/download?version=17486)
- Initial release!