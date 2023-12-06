package me.lokka30.phantomworlds;

import me.lokka30.microlib.files.YamlConfigFile;
import me.lokka30.microlib.maths.QuickTimer;
import me.lokka30.microlib.other.UpdateChecker;
import me.lokka30.phantomworlds.commands.phantomworlds.PWCommand;
import me.lokka30.phantomworlds.commands.phantomworlds.parameters.resolvers.WorldFolderResolver;
import me.lokka30.phantomworlds.commands.phantomworlds.parameters.suggestion.WorldFolderSuggestion;
import me.lokka30.phantomworlds.commands.phantomworlds.utils.WorldFolder;
import me.lokka30.phantomworlds.listeners.player.PlayerChangeWorldListener;
import me.lokka30.phantomworlds.listeners.player.PlayerDeathListener;
import me.lokka30.phantomworlds.listeners.player.PlayerJoinListener;
import me.lokka30.phantomworlds.listeners.player.PlayerPortalListener;
import me.lokka30.phantomworlds.listeners.world.WorldInitListener;
import me.lokka30.phantomworlds.managers.FileManager;
import me.lokka30.phantomworlds.managers.WorldManager;
import me.lokka30.phantomworlds.misc.CompatibilityChecker;
import me.lokka30.phantomworlds.misc.UpdateCheckerResult;
import me.lokka30.phantomworlds.scheduler.BackupScheduler;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import revxrsal.commands.bukkit.BukkitCommandHandler;

import java.io.File;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * This is the main class of the PhantomWorlds plugin.
 *
 * @author lokka30
 * @since v2.0.0
 */
public class PhantomWorlds extends JavaPlugin {

    /*
     *TODO:
     * - Translate backslash character in world names as a space so world names with a space can be used in the plugin
     * - Vanish compatibility
     *  - don't send 'by' messages unless the sender is not a player / target can see the (player) sender
     *  - add vanish compatibility to 'teleport' subcommand
     *  - add ability to toggle vanish compatibility
     * - log in console (LogLevel:INFO) when a command is prevented due to a target player seemingly being vanished to the command sender.
     */

  private static PhantomWorlds instance;

  protected BukkitCommandHandler command;

  private BukkitTask backupService = null;

  /**
   * If you have contributed code to the plugin, add your name to the end of this list! :)
   */
  public static final String[] CONTRIBUTORS = new String[]{"madison-allen"};

  public static final String BACKUP_FOLDER = "backups";
  public static final String ARCHIVE_FOLDER = "archives";

  /**
   * This is reported in the 'pw info' command to inform the command sender of what MC versions that
   * this version of PW is designed to run on, and is therefore supported.
   */
  public final String supportedServerVersions = "1.7.x and newer";

  /**
   * Frequently used vars.
   */
  public final FileManager fileManager = new FileManager();
  public final WorldManager worldManager = new WorldManager();

  /**
   * Miscellaneous vars.
   */
  public final CompatibilityChecker compatibilityChecker = new CompatibilityChecker();
  public UpdateCheckerResult updateCheckerResult = null;

  /**
   * Data/configuration files.
   */
  public final YamlConfigFile settings = new YamlConfigFile(this,
          new File(getDataFolder(), "settings.yml"));
  public final YamlConfigFile advancedSettings = new YamlConfigFile(this,
          new File(getDataFolder(), "advancedSettings.yml"));
  public final YamlConfigFile messages = new YamlConfigFile(this,
          new File(getDataFolder(), "messages.yml"));
  public final YamlConfigFile data = new YamlConfigFile(this,
          new File(getDataFolder(), "data.yml"));

  /*
      Used to check if world are loaded
   */
  private boolean isWorldLoaded = false;

  /**
   * This method is called by Bukkit when it loads PhantomWorlds.
   *
   * @since v2.0.0
   */
  @Override
  public void onEnable() {

    instance = this;

    QuickTimer timer = new QuickTimer(TimeUnit.MILLISECONDS);
    checkCompatibility();
    loadFiles();

    registerCommands();
    registerListeners();
    miscStartupProcedures();

    if(settings.getConfig().getBoolean("backup-scheduler", true)) {
      getLogger().info("Starting up Backup scheduler...");
      backupService = new BackupScheduler().runTaskTimerAsynchronously(this, settings.getConfig().getInt("backup-delay") * 20L, settings.getConfig().getInt("backup-delay") * 20L);
    }

    getLogger().info("Start-up complete (took " + timer.getDuration() + "ms)");
  }

  public boolean isWorldLoaded() {
    return isWorldLoaded;
  }

  /**
   * This method is called by Bukkit when it disables PhantomWorlds.
   *
   * @since v2.0.0
   */
  @Override
  public void onDisable() {
    final QuickTimer timer = new QuickTimer(TimeUnit.MILLISECONDS);

    if(backupService != null) {
      getLogger().info("Shutting down backup scheduler...");
      backupService.cancel();
    }

    getLogger().info("Shut-down complete (took " + timer.getDuration() + "ms)");
  }

  /**
   * Run the compatibility checkker. Reports in the console if it finds any possible issues.
   *
   * @since v2.0.0
   */
  void checkCompatibility() {
    getLogger().info("Checking compatibility with server...");

    compatibilityChecker.checkAll();

    if(compatibilityChecker.incompatibilities.isEmpty()) {
      return;
    }

    for(int i = 0; i < compatibilityChecker.incompatibilities.size(); i++) {
      CompatibilityChecker.Incompatibility incompatibility = compatibilityChecker.incompatibilities.get(
              i);
      getLogger().warning(
              "Incompatibility #" + (i + 1) + " (Type: " + incompatibility.type + "):");
      getLogger().info(" -> Reason: " + incompatibility.reason);
      getLogger().info(" -> Recommendation: " + incompatibility.recommendation);
    }
  }

  /**
   * (Re)load all data/configuration files. Creates them if they don't exist. Applies version
   * checking where suitable.
   *
   * @since v2.0.0
   */
  public void loadFiles() {
    getLogger().info("Checking for backup directory...");

    final File backup = new File(getDataFolder(), BACKUP_FOLDER);
    if(!backup.exists()) {
      backup.mkdirs();
    }

    getLogger().info("Loading files...");

    for(FileManager.PWFile pwFile : FileManager.PWFile.values()) {
      fileManager.init(pwFile);
    }
  }

  /**
   * Checks on the worlds that are created through PhantomWorlds. If they aren't already loaded, PW
   * loads them.
   *
   * @since v2.0.0
   */
  public void loadWorlds() {
    getLogger().info("Loading worlds...");
    worldManager.loadManagedWorlds();
    isWorldLoaded = true;
  }

  /**
   * Registers the commands for the plugin. In this case, only one command is registered (with an
   * array of sub-commands of course).
   *
   * @since v2.0.0
   */
  void registerCommands() {
    getLogger().info("Registering commands...");
    //Utils.registerCommand(new PhantomWorldsCommand(), "phantomworlds");

    this.command = BukkitCommandHandler.create(this);

    //Set our command help writer
    command.setHelpWriter((command, actor) -> {
      if(command.getDescription() == null) {
        return "";
      }

      final String description = PhantomWorlds.instance().messages.getConfig().getString(command.getDescription());
      if(description == null) {
        return "";
      }

      return ChatColor.translateAlternateColorCodes('&', description);
    });

    //Register Resolvers
    this.command.registerValueResolver(WorldFolder.class, new WorldFolderResolver());

    //Register Suggestors
    this.command.getAutoCompleter().registerParameterSuggestions(WorldFolder.class, new WorldFolderSuggestion());

    this.command.register(new PWCommand());
    this.command.registerBrigadier();
  }

  /**
   * Registers the listeners for the plugin. These classes run code when Events happen on the
   * server, e.g. 'player joins server' or 'player changes world'.
   *
   * @since v2.0.0
   */
  void registerListeners() {
    getLogger().info("Registering listeners...");
    getServer().getPluginManager().registerEvents(new PlayerChangeWorldListener(this), this);
    getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);
    getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
    getServer().getPluginManager().registerEvents(new PlayerPortalListener(this), this);

    getServer().getPluginManager().registerEvents(new WorldInitListener(this), this);
  }

  /**
   * Miscellaneous startup procedures.
   *
   * @since v2.0.0
   */
  void miscStartupProcedures() {
    getLogger().info("Running misc startup procedures...");

    /* bStats Metrics */
    new Metrics(this, 8916);

    /* Update Checker */
    if(settings.getConfig().getBoolean("run-update-checker", true)) {
      try {
        final UpdateChecker updateChecker = new UpdateChecker(this, 84017);
        updateChecker.getLatestVersion(latestVersion->{
          updateCheckerResult = new UpdateCheckerResult(
                  !latestVersion.equals(updateChecker.getCurrentVersion()),
                  updateChecker.getCurrentVersion(),
                  latestVersion
          );

          if(updateCheckerResult.isOutdated()) {
            if(!messages.getConfig()
                    .getBoolean("update-checker.console.enabled", true)) {
              return;
            }

            messages.getConfig().getStringList("update-checker.console.text")
                    .forEach(message->getLogger().info(message
                            .replace("%currentVersion%",
                                    updateCheckerResult.getCurrentVersion())
                            .replace("%latestVersion%", updateCheckerResult.getLatestVersion())
                    ));
          }
        });
      } catch(Exception ex) {
        getLogger().warning("Unable to check for updates - check your internet connection: "
                + ex.getMessage());
      }
    }
  }

  public static PhantomWorlds instance() {
    return instance;
  }

  public static Logger logger() {
    return instance.getLogger();
  }

  public static WorldManager worldManager() {
    return instance.worldManager;
  }
}