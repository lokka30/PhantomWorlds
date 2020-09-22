# PhantomWorlds Changelog

### v1.0.0-ALPHA b0
* Initial version.

### v1.0.1-ALPHA b1
* Improved startup banner.
* Improvements and fixes to the commands.

### v1.0.2-ALPHA b2
* **[IMPORTANT]** `messages.yml` updated to version `2`!
* Improved startup banner
* Removed command suggestions for `create` subcommand when `args.length = 2`
* Fixed `%world%` placeholders when player specified in `teleport` subcommand
* Added `args.length` limit and usage message to `list` subcommand
* Fixed `PhantomWorld#createWorld()` not adding itself to `PhantomWorlds#worldsMap`
* Removed accidentally left debug message when teleporting self to a world with the `teleport` subcommand

### v1.0.3-ALPHA b3
* **[IMPORTANT]** `messages.yml` updated to version `3`!
* Fixed `messages.yml` version not being updated in code.
* Improved startup banner.
* Added command alias `/pw`.
* Refactored data management.
* Removed `final` modifier on `PhantomWorlds#worldsMap`, should fix a bug where new worlds weren't inserted into the map.

### v1.0.4-ALPHA b4
* Fixed `delete` subcommand's kick message not replacing the `%prefix%` placeholder.
* Fixed `IndexOutOfBoundsException` in `create` subcommand when world settings are specified.

### v1.0.5-ALPHA b5
* Fixed tab completion not occuring for `create` subcommand for when more than one world setting is specified.
* Fixed `-gs` setting in subcommand `create` not checking the correct substring.

### v1.0.6-ALPHA b6
* Fixed `loadWorlds()`.