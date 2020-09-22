package me.lokka30.phantomworlds;

import me.lokka30.microlib.MicroLogger;
import me.lokka30.microlib.UpdateChecker;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public class PhantomWorlds extends JavaPlugin {

    public final MicroLogger logger = new MicroLogger("&b&lPhantomWorlds: &7");
    public final File settingsFile = new File(getDataFolder(), "settings.yml");
    public final File messagesFile = new File(getDataFolder(), "messages.yml");
    public final File dataFile = new File(getDataFolder(), "data.yml");
    public YamlConfiguration settingsCfg, messagesCfg, dataCfg;

    public final HashMap<String, PhantomWorld> worldsMap = new HashMap<>();

    /**
     * Called by Bukkit when the plugin is being loaded up.
     */
    @Override
    public void onEnable() {
        long timerStart = System.currentTimeMillis();

        /* Loading Banner */
        logger.log(MicroLogger.LogLevel.INFO, "&8&m+-----------------------------------------------+");
        logger.log(MicroLogger.LogLevel.INFO, "&b&lPhantomWorlds &bv" + getDescription().getVersion() + "&7, developed by lokka30");
        logger.log(MicroLogger.LogLevel.INFO, "&f(Loading Plugin)");
        logger.log(MicroLogger.LogLevel.INFO, "&8&m+-----------------------------------------------+");

        /* Load files */
        logger.log(MicroLogger.LogLevel.INFO, "Loading files...");
        loadFiles();

        /* Loading worlds */
        logger.log(MicroLogger.LogLevel.INFO, "Loading worlds...");
        loadWorlds();

        /* Register commands */
        logger.log(MicroLogger.LogLevel.INFO, "Registering commands...");
        registerCommands();

        /* Start metrics */
        logger.log(MicroLogger.LogLevel.INFO, "Starting bStats metrics...");
        new Metrics(this, 8916);

        /* Check for updates */
        //checkForUpdates(); //TODO disabled as resource id needs to be changed in the method.

        long timerDuration = System.currentTimeMillis() - timerStart;
        logger.log(MicroLogger.LogLevel.INFO, "Loading complete! &8(&7took &b" + timerDuration + "ms&8)");
    }

    /**
     * Do all the things for creating and loading the resource files.
     * <p>
     * Contrary to most other enabling methods here, this method has 'public' visibility,
     * as it is accessed by the command for when a reload is called.
     */
    public void loadFiles() {
        /* Settings */
        createIfNotExists(settingsFile, "settings.yml");
        settingsCfg = YamlConfiguration.loadConfiguration(settingsFile);
        checkFileVersion(settingsCfg, "settings.yml", 1);

        /* Messages */
        createIfNotExists(messagesFile, "messages.yml");
        messagesCfg = YamlConfiguration.loadConfiguration(messagesFile);
        checkFileVersion(messagesCfg, "messages.yml", 1);

        /* Data */
        createIfNotExists(dataFile, "data.yml");
        dataCfg = YamlConfiguration.loadConfiguration(dataFile);
        checkFileVersion(dataCfg, "data.yml", 1);

        /* License Notice */
        createIfNotExists(new File(getDataFolder(), "license.txt"), "license.txt");
    }

    /**
     * If the file doesn't exist, then create it.
     *
     * @param file the file to check if exists or not
     * @param name the name of the file in the 'resources' folder
     */
    private void createIfNotExists(File file, String name) {
        if (!file.exists()) {
            logger.log(MicroLogger.LogLevel.INFO, "File '&b" + name + "&7' didn't exist, creating it.");
            saveResource(name, false);
        }
    }

    /**
     * Check if the configuration file is running the correct version - if not, alert the console.
     *
     * @param cfg            the configuration file to check.
     * @param name           the name of the configuration file.
     * @param currentVersion the version that the configuration file should be running.
     */
    private void checkFileVersion(YamlConfiguration cfg, String name, int currentVersion) {
        int installedVersion = cfg.getInt("advanced.file-version");

        if (installedVersion > currentVersion) {
            logger.log(MicroLogger.LogLevel.WARNING, "File '&b" + name + "&7' seems to be newer than the plugin's default file's version. Please use the older file, version &b" + currentVersion + "&7.");
        } else if (installedVersion < currentVersion) {
            logger.log(MicroLogger.LogLevel.WARNING, "File '&b" + name + "&7' is outdated. Please use the new file, version &b" + currentVersion + "&7 - you must merge or replace your current file's changes.");
        }
    }

    /**
     *
     */
    public void loadWorlds() {
        worldsMap.clear();

        for (World world : Bukkit.getWorlds()) {
            worldsMap.put(world.getName(), new PhantomWorld(this, world.getName()));
        }

        for (String worldName : dataCfg.getStringList("worlds")) {
            if (!worldsMap.containsKey(worldName)) {
                PhantomWorld phantomWorld = new PhantomWorld(this, worldName);
                Bukkit.createWorld(new WorldCreator(worldName));
                try {
                    phantomWorld.createWorld();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                worldsMap.put(worldName, phantomWorld);
            }
        }
    }

    /**
     * Register commands.
     */
    private void registerCommands() {
        Objects.requireNonNull(getCommand("phantomworlds")).setExecutor(new PhantomWorldsCommand(this));
    }

    /**
     * Asynchronously check for updates, if enabled in the settings configuration.
     */
    @SuppressWarnings("unused") //TODO Remove the warning suppression and change the resourceId below.
    private void checkForUpdates() {
        if (settingsCfg.getBoolean("run-update-checker")) {
            UpdateChecker updateChecker = new UpdateChecker(this, 12345);
            String currentVersion = updateChecker.getCurrentVersion();
            String latestVersion = updateChecker.getLatestVersion();

            if (!currentVersion.equals(latestVersion)) {
                logger.log(MicroLogger.LogLevel.WARNING, "A new update is available on SpigotMC! Latest version is '&b" + latestVersion + "&7', you're running '&b" + currentVersion + "&7'.");
            }
        }
    }
}
