package me.lokka30.phantomworlds;

import me.lokka30.microlib.QuickTimer;
import me.lokka30.microlib.UpdateChecker;
import me.lokka30.microlib.YamlConfigFile;
import me.lokka30.phantomworlds.commands.phantomworlds.PhantomWorldsCommand;
import me.lokka30.phantomworlds.managers.FileManager;
import me.lokka30.phantomworlds.managers.WorldManager;
import me.lokka30.phantomworlds.misc.CompatibilityChecker;
import me.lokka30.phantomworlds.misc.UpdateCheckerResult;
import me.lokka30.phantomworlds.misc.Utils;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicInteger;

public class PhantomWorlds extends JavaPlugin {

    /**
     * Contributing code to the plugin?
     * Feel free to add your name to the list!
     */
    @SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
    public final HashSet<String> contributors = new HashSet<>(Arrays.asList("none"));

    public final String supportedServerVersions = "1.7.x to 1.16.x";

    public final FileManager fileManager = new FileManager(this);
    public final WorldManager worldManager = new WorldManager(this);

    public final CompatibilityChecker compatibilityChecker = new CompatibilityChecker();
    public UpdateCheckerResult updateCheckerResult = null;

    public YamlConfigFile settings = new YamlConfigFile(this, new File(getDataFolder(), "settings.yml"));
    public YamlConfigFile advancedSettings = new YamlConfigFile(this, new File(getDataFolder(), "advancedSettings.yml"));
    public YamlConfigFile messages = new YamlConfigFile(this, new File(getDataFolder(), "messages.yml"));
    public YamlConfigFile data = new YamlConfigFile(this, new File(getDataFolder(), "data.yml"));

    @Override
    public void onEnable() {
        final QuickTimer timer = new QuickTimer();

        checkCompatibility();
        loadFiles();
        loadWorlds();
        registerCommands();
        registerListeners();
        miscStartupProcedures();

        Utils.LOGGER.info("&f~ Start-up complete. &7Took &b" + timer.getTimer() + "ms");
    }

    @Override
    public void onDisable() {
        final QuickTimer timer = new QuickTimer();

        /* ... any on-disable content should be put here. nothing for now */

        Utils.LOGGER.info("&f~ Shut-down complete. &7Took &b" + timer.getTimer() + "ms");
    }

    void checkCompatibility() {
        Utils.LOGGER.info("Checking compatibility with server...");

        compatibilityChecker.checkAll();

        if (compatibilityChecker.incompatibilities.isEmpty()) return;

        AtomicInteger index = new AtomicInteger(1);
        compatibilityChecker.incompatibilities.forEach(incompatibility -> {
            Utils.LOGGER.warning("Incompatibility #" + index.getAndIncrement() + " &8(&7Type: " + incompatibility.type + "&8)&7:");
            Utils.LOGGER.info("&8 &m->&f Reason: &7" + incompatibility.reason);
            Utils.LOGGER.info("&8 &m->&f Recommendation: &7" + incompatibility.recommendation);
        });
    }

    void loadFiles() {
        Utils.LOGGER.info("Loading files...");
        for (FileManager.PWFile pwFile : FileManager.PWFile.values()) {
            fileManager.init(pwFile);
        }
    }

    void loadWorlds() {
        Utils.LOGGER.info("Loading worlds...");
        worldManager.loadManagedWorlds();
    }

    void registerCommands() {
        Utils.LOGGER.info("Registering commands...");
        Utils.registerCommand(this, new PhantomWorldsCommand(this), "phantomworlds");
    }

    void registerListeners() {
        Utils.LOGGER.info("Registering listeners...");
        /* Register any listeners here. */
    }

    void miscStartupProcedures() {
        Utils.LOGGER.info("Running misc startup procedures...");

        /* bStats Metrics */
        new Metrics(this, 8916);

        /* Update Checker */
        if (settings.getConfig().getBoolean("run-update-checker", true)) {
            final UpdateChecker updateChecker = new UpdateChecker(this, 84017);
            updateChecker.getLatestVersion(latestVersion -> {
                updateCheckerResult = new UpdateCheckerResult(
                        !latestVersion.equals(updateChecker.getCurrentVersion()),
                        updateChecker.getCurrentVersion(),
                        latestVersion
                );

                if (updateCheckerResult.isOutdated()) {
                    if (!messages.getConfig().getBoolean("update-checker.console.enabled", true)) return;

                    messages.getConfig().getStringList("update-checker.console.text").forEach(message -> Utils.LOGGER.info(message
                            .replace("%currentVersion%", updateCheckerResult.getCurrentVersion())
                            .replace("%latestVersion%", updateCheckerResult.getLatestVersion())
                    ));
                }
            });
        }
    }
}
