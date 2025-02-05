# 1.1.9

- Fixed "gateguard unlimited range to move to castle" bug
- Cleaned up some leftovers of old web version of the game
- Added experimental server port selection through properties
- Added logging the game version at startup
- Fixed win/loss not updating in the playerlist after each game
- Added version in logging response when the client version is incorrect
- Build system improvement, including "nightly" releases of the latest dev version
- Minor sound normalization
- fixed sounds not working when running non-JAR version
- added early loading of the game assets to improve the startup time

# 1.1.8

- Fixed Windows version crashing on launch
- Fixed hover on players showing the wrong stats
- Fixed new players not being saved
- Changed new player intro screen
- Fixed browser button in Linux

# 1.1.7

- Added auto database migration for standalone mode
- Minor UI fixes

# 1.1.6

- Updated Sound and Music to fix problems with playback
- Added volume controls for Sound and Music
- Added option for unit icons to show team instead of just colors
- Restricted Zealous relic for War Elephant and Confessor
- added client side connection check
- Added graceful server shutdown with feedback
- Added a per unit kill counter on hover of the units name
- Removed connection timing out in standalone mode
- Changed the min AI level that will be reduced on a loss from 15 to 2
- Disabled multi-player modes in standalone mode
- Added pausing in standalone mode
- Fix bug with flying gate guard
- Added showing a players wins/losses on hovering over their name

# 1.1.5

- The warrior's whirlwind attack now always attacks the target first and then continues in a clockwise order. With the zealous relic Will only trigger on the first attack.
- Restricted broken and redundent relic/unit combos for the AI
- Changed number of commanders and types for random armies
- Updated UI buttons with correct links and a discord link.
- Updated UI to show wins/losses and active pvp players and rankings
- Handling password hashes fixed
- AI game rebalanced to be much easier than before
- Gold reward for AI game readjusted
- Increased sleep and timeout counters

# 1.1.4

- Use toml for settings instead of current implementation
- Add remember password

# 1.1.3

- versioning scheme improved; protocol version now separated from game version
- parameterized SQL queries now done so correctly
- browser-starting code improved
- logging now more consistent, and done using Tinylog library
- server in standalone-client mode now runs as a thread instead of a process
- surrendering the tutorial game was made into skipping the tutorial instead

# 1.1.2

- games against AI made easier
- build process improved

# 1.1.1

- fixed a bug causing every single-player game to be tutorial
- cheat tool now gets you out of the tutorial

# 1.1.0
Released the source code of the game under AGPL 3.0, making it a FOSS project! Builds are available on GitHub as JAR files, and as Flatpak through Flathub.

Features:
- added TLS to the sockets, along with key+cert generation scripts
- improved security of password storage
- added standalone mode
- added server choice dialog
- removed MySQL as DB backend and replaced it with SQLite, allowing local gameplay without having to run DB service
- created new `Cheat` utility, which gives a lot of gold if the player owns the server

Gameplay:
- rebalanced the rewards towards less grind for units

Fixes:
- fixed the graphical bug making the game unusable on newer Java
- fixed some typo-like bugs, e.g. wrong AI scaling

Cleanups/Development Improvements:
- added Maven build config together with dependencies
- extracted certain amount of constants in order to enable better configurability
- changed a lot of `byte` types across the code to `short`, mostly to allow for a wider range of IDs
- cleaned up repasted code and replaced it with a dependency
- reorganized the code packages
- removed lots of old data
- removed parts of the code related to the applet mode
- removed some incomplete features
- removed existing cheats
