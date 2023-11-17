package me.lokka30.phantomworlds;

import me.lokka30.microlib.files.YamlConfigFile;
import me.lokka30.microlib.maths.QuickTimer;
import me.lokka30.microlib.other.UpdateChecker;
import me.lokka30.phantomworlds.commands.phantomworlds.PhantomWorldsCommand;
import me.lokka30.phantomworlds.listeners.WorldInitListener;
import me.lokka30.phantomworlds.managers.FileManager;
import me.lokka30.phantomworlds.managers.WorldManager;
import me.lokka30.phantomworlds.misc.CompatibilityChecker;
import me.lokka30.phantomworlds.misc.UpdateCheckerResult;
import me.lokka30.phantomworlds.misc.Utils;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * This is the main class of the PhantomWorlds plugin.
 *
 * @author lokka30
 * @since v2.0.0
 */
public class PhantomWorlds extends JavaPlugin {

    /*
    TODO:
     * - Translate backslash character in world names as a space so world names with a space can be used in the plugin
     * - Vanish compatibility
     *  - don't send 'by' messages unless the sender is not a player / target can see the (player) sender
     *  - add vanish compatibility to 'teleport' subcommand
     *  - add ability to toggle vanish compatibility
     * - log in console (LogLevel:INFO) when a command is prevented due to a target player seemingly being vanished to the command sender.
     */

    /**
     * If you have contributed code to the plugin, add your name to the end of this list! :)
     */
    public static final String[] CONTRIBUTORS = new String[]{"madison-allen"};

    /**
     * This is reported in the 'pw info' command to inform the command sender of what MC versions
     * that this version of PW is designed to run on, and is therefore supported.
     */
    public final String supportedServerVersions = "1.7.x and newer";

    /**
     * Frequently used vars.
     */
    public final FileManager fileManager = new FileManager(this);
    public final WorldManager worldManager = new WorldManager(this);

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
    private boolean isWorldLoaded;


    /**
     * This method is called by Bukkit when it loads PhantomWorlds.
     *
     * @author lokka30
     * @since v2.0.0
     */
    @Override
    public void onEnable(){
        QuickTimer timer = new QuickTimer(TimeUnit.MILLISECONDS);
        checkCompatibility();
        loadFiles();

        isWorldLoaded = false;
        getServer().getPluginManager().registerEvents(new WorldInitListener(this),this);

        registerCommands();
        registerListeners();
        miscStartupProcedures();

        getLogger().info("Start-up complete (took " + timer.getDuration() + "ms)");
    }

    public boolean isWorldLoaded() {
        return isWorldLoaded;
    }

    /**
     * This method is called by Bukkit when it disables PhantomWorlds.
     *
     * @author lokka30
     * @since v2.0.0
     */
    @Override
    public void onDisable() {
        final QuickTimer timer = new QuickTimer(TimeUnit.MILLISECONDS);

        /* ... any on-disable content should be put here. nothing for now */

        getLogger().info("Shut-down complete (took " + timer.getDuration() + "ms)");
    }

    /**
     * Run the compatibility checkker. Reports in the console if it finds any possible issues.
     *
     * @author lokka30
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
     * @author lokka30
     * @since v2.0.0
     */
    public void loadFiles() {
        getLogger().info("Loading files...");
        for(FileManager.PWFile pwFile : FileManager.PWFile.values()) {
            fileManager.init(pwFile);
        }
    }

    /**
     * Checks on the worlds that are created through PhantomWorlds. If they aren't already loaded,
     * PW loads them.
     *
     * @author lokka30
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
     * @author lokka30
     * @since v2.0.0
     */
    void registerCommands() {
        getLogger().info("Registering commands...");
        Utils.registerCommand(this, new PhantomWorldsCommand(this), "phantomworlds");
    }

    /**
     * Registers the listeners for the plugin. These classes run code when Events happen on the
     * server, e.g. 'player joins server' or 'player changes world'.
     *
     * @author lokka30
     * @since v2.0.0
     */
    void registerListeners() {
        getLogger().info("Registering listeners...");
        /* Register any listeners here. */
    }

    /**
     * Miscellaneous startup procedures.
     *
     * @author lokka30
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
                updateChecker.getLatestVersion(latestVersion -> {
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
                            .forEach(message -> getLogger().info(message
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
}
