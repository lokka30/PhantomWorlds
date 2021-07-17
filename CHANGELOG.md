***

# v2.0.0

## Notes:

* **Testing Status:** TBD
* **File Updates:** data.yml - PhantomWorlds will automatically update this for you.

## Changelog:

### Significant Changes

* Re-programmed this plugin from the ground up. Improved code quality.
* Fixed a crucial bug where PW didn't know how to load nether & end worlds properly
* Added file migration methods that try to automatically update files if PhantomWorlds updates
* Added a collection of new customisable world options in `/pw create`: `generatorSettings`, `hardcore`, `spawnMobs`
  , `spawnAnimals`, `keepSpawnInMemory`, `allowPvP` & `difficulty`.
* Removed the requirement to have the character `-` before every world option specified in `create`. For
  example, `/pw create helloworld normal -hello:true -g:void-generator`
  becomes `/pw create helloworld normal hello:true g:void-generator`.

### Insignificant Changes

* Added compatibility checker to the plugin, this will warn servers that don't run Spigot on startup (PhantomWorlds
  requires Spigot or a derivative such as Paper or Tuinity). It can also be triggered using the new `compatibility`
  subcommand.
* Added `info` subcommand to view generic information about the plugin.
* Added `debug` subcommand, currently which serves no use, although will be updated in the future to assist with
  providing users with technical support for the resource.