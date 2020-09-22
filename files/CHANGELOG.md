# PhantomWorlds Changelog

### v1.0.0-ALPHA b0
* Initial version.

### v1.0.1-ALPHA b1
* Improved startup banner.
* Improvements and fixes to the commands.

### v1.0.2-ALPHA b2
* **[IMPORTANT]** `messages.yml` updated to version 2!
* Improved startup banner
* Removed command suggestions for `create` subcommand when `args.length = 2`
* Fixed `%world%` placeholders when player specified in `teleport` subcommand
* Added `args.length` limit and usage message to `list` subcommand
* Fixed `PhantomWorld#createWorld()` not adding itself to `PhantomWorlds#worldsMap`
* Removed accidentally left debug message when teleporting self to a world with the `teleport` subcommand