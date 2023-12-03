package me.lokka30.phantomworlds.managers;

import me.lokka30.phantomworlds.PhantomWorlds;
import me.lokka30.phantomworlds.misc.Utils;
import me.lokka30.phantomworlds.misc.WorldLoadResponse;
import me.lokka30.phantomworlds.world.PhantomWorld;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.WorldType;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;

import static me.lokka30.phantomworlds.misc.Utils.zipFolder;

/**
 * Contains an assortment of methods to handle world management in PW.
 *
 * @author lokka30
 * @since v2.0.0
 */
public class WorldManager {

  /**
   * For all worlds listed in PW's data file, if they aren't already loaded by Bukkit, then tell
   * Bukkit to load them
   *
   * @since v2.0.0
   */
  public void loadManagedWorlds() {
    PhantomWorlds.logger().info("Loading managed worlds...");

    if(!PhantomWorlds.instance().data.getConfig().contains("worlds-to-load")) {
      return;
    }

    final HashSet<String> worldsToDiscardFromDataFile = new HashSet<>();

    //This should be outside our for each
    if(!Bukkit.getWorldContainer().exists()) {
      PhantomWorlds.logger().severe("World container doesn't exist!");
      return;
    }

    //noinspection ConstantConditions
    for(final String worldName : PhantomWorlds.instance().data.getConfig().getConfigurationSection("worlds-to-load").getKeys(false)) {

      final WorldLoadResponse response = loadWorld(worldName);

      if(response.equals(WorldLoadResponse.INVALID)) {
        worldsToDiscardFromDataFile.add(worldName);
      }

    }

    for(String worldName : worldsToDiscardFromDataFile) {
      PhantomWorlds.instance().data.getConfig().set("worlds-to-load." + worldName, null);
    }

    try {
      PhantomWorlds.instance().data.save();
    } catch(IOException ex) {
      PhantomWorlds.logger().severe("Unable to save data file. Stack trace:");
      ex.printStackTrace();
    }
  }

  /**
   * Used to load a world based on the name.
   * @param worldName The name of the world.
   * @return The {@link WorldLoadResponse response} from the loading process.
   */
  public WorldLoadResponse loadWorld(final String worldName) {

    if(Bukkit.getWorld(worldName) != null) {
      return WorldLoadResponse.NOT_FOUND;
    }

    final File worldFolder = new File(Bukkit.getWorldContainer(), worldName);
    if(!worldFolder.exists()) {

      // The world was deleted/moved by the user so it must be re-imported. PW should no longer attempt to load that world.
      PhantomWorlds.logger().info("Discarding world '" + worldName + "' from PhantomWorlds' "
              + "data file as it no longer exists on the server.");
      return WorldLoadResponse.INVALID;
    }

    if(PhantomWorlds.instance().data.getConfig().getBoolean("worlds-to-load." + worldName + ".skip-autoload", false)) {
      PhantomWorlds.logger().info("Skipping autoload of world '" + worldName + "'.");
      return WorldLoadResponse.CONFIG_SKIPPED;
    }

    PhantomWorlds.logger().info("Loading world '" + worldName + "'...");
    getPhantomWorldFromData(worldName).create();
    return WorldLoadResponse.LOADED;
  }

  /**
   * This creates a PhantomWorld object by scanning the data file by the specified name. Developers
   * are expected to make sure the specified world exists prior to retrieving it.
   *
   * @since v2.0.0
   */
  public PhantomWorld getPhantomWorldFromData(final String name) {
    final String cfgPath = "worlds-to-load." + name + ".";

    final PhantomWorld world = new PhantomWorld(
            name,
            World.Environment.valueOf(
                    PhantomWorlds.instance().data.getConfig().getString(cfgPath + "environment", "NORMAL")),
            PhantomWorlds.instance().data.getConfig().getBoolean(cfgPath + "generateStructures", true),
            PhantomWorlds.instance().data.getConfig().getString(cfgPath + "generator", null),
            PhantomWorlds.instance().data.getConfig().getString(cfgPath + "generatorSettings", null),
            PhantomWorlds.instance().data.getConfig().getBoolean(cfgPath + "hardcore", false),
            PhantomWorlds.instance().data.getConfig().getLong(cfgPath + "seed", 0),
            WorldType.valueOf(PhantomWorlds.instance().data.getConfig().getString(cfgPath + "worldType", "NORMAL")),
            PhantomWorlds.instance().data.getConfig().getBoolean(cfgPath + "spawnMobs", true),
            PhantomWorlds.instance().data.getConfig().getBoolean(cfgPath + "spawnAnimals", true),
            PhantomWorlds.instance().data.getConfig().getBoolean(cfgPath + "keepSpawnInMemory", false),
            PhantomWorlds.instance().data.getConfig().getBoolean(cfgPath + "allowPvP", true),
            Difficulty.valueOf(PhantomWorlds.instance().data.getConfig().getString(cfgPath + "difficulty", null)),
            GameMode.valueOf(PhantomWorlds.instance().data.getConfig().getString(cfgPath + "gameMode", "SURVIVAL"))
    );

    if(PhantomWorlds.instance().data.getConfig().contains(cfgPath + "rules") &&
            PhantomWorlds.instance().data.getConfig().isConfigurationSection(cfgPath + "rules")) {
      for(final String rule : PhantomWorlds.instance().data.getConfig().getConfigurationSection(cfgPath + "rules").getKeys(false)) {
        world.getGamerules().put(rule, PhantomWorlds.instance().data.getConfig().getString(cfgPath + "rules." + rule));
      }
    }
    return world;
  }

  public boolean backupWorld(final String world) {
    return backupWorld(world, new File(PhantomWorlds.instance().getDataFolder(), PhantomWorlds.BACKUP_FOLDER));
  }
  
  public boolean backupWorld(final String world, final File backupFolder) {
    final File worldFolder = new File(Bukkit.getWorldContainer(), world);

    try {
      final File worldBackupFolder = new File(backupFolder, world);
      worldBackupFolder.mkdir();

      final String timestamp = String.valueOf(System.currentTimeMillis());
      final String zipFilePath = new File(worldBackupFolder, world + "-" + timestamp + ".zip").getPath();
      zipFolder(worldFolder, zipFilePath);

      PhantomWorlds.logger().info("World '" + world + "' backed up to: " + worldBackupFolder.getPath());
      return true;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean backupAndDeleteWorld(final String worldName) {
    final World world = Bukkit.getWorld(worldName);

    if(world == null) {
      PhantomWorlds.logger().warning("Unable to locate world '" + worldName + "'! Halting deletion.");
      return false;
    }

    if(PhantomWorlds.instance().settings.getConfig().getBoolean("delete-archive", true)) {
      if(!backupWorld(world.getName(), new File(PhantomWorlds.instance().getDataFolder(), PhantomWorlds.ARCHIVE_FOLDER))) {
        PhantomWorlds.logger().warning("Unable to backup world '" + worldName + "'! Halting deletion.");
        return false;
      }
    }

    if(!Bukkit.unloadWorld(world, true)) {
      PhantomWorlds.logger().warning("Unable to unload world '" + worldName + "'! Halting deletion.");
      return false;
    }
    final File worldFolder = new File(Bukkit.getWorldContainer(), worldName);
    if(!worldFolder.exists()) {
      PhantomWorlds.logger().warning("Unable to locate folder for world '" + worldName + "'! Halting deletion.");
      return false;
    }

    if(!Utils.deleteFolder(worldFolder)) {
      PhantomWorlds.logger().warning("Unable to delete world '" + worldName + "'! Halting deletion.");
      return false;
    }
    return true;
  }
}