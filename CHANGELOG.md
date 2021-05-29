***

# v2.0.0

## Notes:
* **Testing Status:** TBD
* **File Updates:** data.yml - PhantomWorlds will automatically update this for you.

## Changelog:
* Re-programmed this plugin from the ground up
* Fixed a crucial bug where PW didn't know how to load nether & end worlds properly
* Added file migration methods that try to automatically update files if PhantomWorlds updates
* Added compatibility checker to the plugin, this will warn servers that don't run Spigot on startup (PhantomWorlds
  requires Spigot or a derivative such as Paper or Tuinity). It can also be triggered using the new `compatibility`
  subcommand.
* Added `info` subcommand to view generic information about the plugin.
* Added `debug` subcommand, currently which serves no use, although will be updated in the future to assist with
  providing users with technical support for the resource.