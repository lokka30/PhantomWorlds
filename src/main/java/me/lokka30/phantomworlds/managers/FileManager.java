package me.lokka30.phantomworlds.managers;

import me.lokka30.phantomworlds.PhantomWorlds;
import me.lokka30.phantomworlds.misc.Utils;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.io.IOException;

/**
 * Contains methods that concern the loading of PW's data/config files.
 *
 * @author lokka30
 * @since v2.0.0
 */
public class FileManager {

    private final PhantomWorlds main;

    public FileManager(PhantomWorlds main) {
        this.main = main;
    }

    /**
     * Run all loading sequences for a file from this method.
     *
     * @param pwFile file to load
     *
     * @author lokka30
     * @since v2.0.0
     */
    public void init(PWFile pwFile) {
        Utils.LOGGER.info("&3Files: &7Loading file &b" + pwFile + "&7...");

        try {
            load(pwFile);

            switch (pwFile) {
                case SETTINGS:
                    migrate(pwFile, main.settings.getConfig().getInt("advanced.file-version"));
                    break;
                case ADVANCED_SETTINGS:
                    migrate(pwFile, main.advancedSettings.getConfig().getInt("advanced.file-version"));
                    break;
                case MESSAGES:
                    migrate(pwFile, main.messages.getConfig().getInt("advanced.file-version"));
                    break;
                case DATA:
                    migrate(pwFile, main.data.getConfig().getInt("advanced.file-version"));
                    break;
                default:
                    break;
            }
        } catch (IOException ex) {
            Utils.LOGGER.error("&3Files: &7Unable to init file &b" + pwFile + "&7. Stack trace:");
            ex.printStackTrace();
        }
    }

    /**
     * Run MicroLib's YamlConfigFile load sequence for each file.
     * The license file is simply replaced.
     *
     * @author lokka30
     * @since v2.0.0
     */
    private void load(PWFile pwFile) throws IOException {
        switch (pwFile) {
            case SETTINGS:
                main.settings.load();
                break;
            case ADVANCED_SETTINGS:
                main.advancedSettings.load();
                break;
            case MESSAGES:
                main.messages.load();
                break;
            case DATA:
                main.data.load();
                break;
            case LICENSE:
                main.saveResource("license.txt", true);
                break;
            default:
                throw new IllegalStateException("Unexpected value " + pwFile);
        }
    }

    /**
     * Attempt to update outdated files automatically.
     *
     * @author lokka30
     * @since v2.0.0
     */
    private void migrate(PWFile pwFile, int currentVersion) {
        // Values of -1 indicate that it is not to be migrated
        if (pwFile.latestFileVersion == -1) return;

        switch (pwFile) {
            case SETTINGS:
                if (currentVersion == PWFile.SETTINGS.latestFileVersion) return;
                alertIncorrectVersion(pwFile);
                break;
            case ADVANCED_SETTINGS:
                if (currentVersion == PWFile.ADVANCED_SETTINGS.latestFileVersion) return;
                alertIncorrectVersion(pwFile);
                break;
            case MESSAGES:
                if (currentVersion == PWFile.MESSAGES.latestFileVersion) return;
                alertIncorrectVersion(pwFile);
                break;
            case DATA:
                if (currentVersion == PWFile.DATA.latestFileVersion) return;

                //Switch below is for future-proofing the code, in case more data versions are added.
                //noinspection SwitchStatementWithTooFewBranches
                switch (currentVersion) {
                    case 1:
                        Utils.LOGGER.info("&3Files: &7Automatically migrating the &b" + pwFile + "&7 file to the latest format (it was outdated).");

                        if (!main.data.getConfig().contains("worlds")) return;

                        for (String worldName : main.data.getConfig().getStringList("worlds")) {
                            if (Bukkit.getWorld(worldName) != null)
                                continue; // Don't add worlds that are already loaded (most likely by Bukkit).

                            main.data.getConfig().set("worlds-to-load." + worldName + ".environment", World.Environment.NORMAL.toString());
                        }

                        main.data.getConfig().set("worlds", null);

                        main.data.getConfig().set("advanced.file-version", 2);

                        Utils.LOGGER.info("&3Files: &7The &b" + pwFile + "&7 file has been migrated.");
                        break;
                    default:
                        alertIncorrectVersion(pwFile);
                        break;
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value " + pwFile);
        }
    }

    void alertIncorrectVersion(PWFile pwFile) {
        Utils.LOGGER.error("&3Files: &7You are running the incorrect version of the " +
                "file &b" + pwFile + "&7! Please back it up and allow the plugin to generate a new file, or you will most likely experience errors.");
    }

    /**
     * Each data/config file and their latest (current) version are mapped here.
     *
     * @author lokka30
     * @since v2.0.0
     */
    public enum PWFile {
        SETTINGS(1),
        ADVANCED_SETTINGS(1),
        MESSAGES(5),
        DATA(2),
        LICENSE(-1);

        public final int latestFileVersion; // If == -1: 'do not migrate me!'

        PWFile(int latestFileVersion) {
            this.latestFileVersion = latestFileVersion;
        }
    }
}
