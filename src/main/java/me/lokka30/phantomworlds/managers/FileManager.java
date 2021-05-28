package me.lokka30.phantomworlds.managers;

import me.lokka30.phantomworlds.PhantomWorlds;
import me.lokka30.phantomworlds.misc.Utils;
import org.bukkit.World;

import java.io.IOException;

public class FileManager {

    private final PhantomWorlds main;

    public FileManager(PhantomWorlds main) {
        this.main = main;
    }

    /**
     * Run all loading sequences for a file
     * from this method
     *
     * @param pwFile file to load
     */
    public void init(PWFile pwFile) {
        Utils.LOGGER.info("&3File Manager: &7Loading file &b" + pwFile + "&7...");

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
            Utils.LOGGER.error("&3File Manager: &7Unable to init file &b" + pwFile + "&7. Stack trace:");
            ex.printStackTrace();
        }
    }

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

    private void migrate(PWFile pwFile, int currentVersion) {
        // Values of -1 indicate that it is not to be migrated
        if (pwFile.latestFileVersion == -1) return;

        switch (pwFile) {
            case SETTINGS:
            case ADVANCED_SETTINGS:
            case MESSAGES:
                if (currentVersion == PWFile.SETTINGS.latestFileVersion) return;

                Utils.LOGGER.error("&3File Manager: &7You are running the incorrect &b" + pwFile + "&7 version! Please back it up and allow the plugin to generate a new file.");
                break;
            case DATA:
                if (currentVersion == PWFile.DATA.latestFileVersion) return;

                //noinspection SwitchStatementWithTooFewBranches
                switch (currentVersion) {
                    case 1:
                        Utils.LOGGER.info("&3File Manager: &7Automatically migrating the &b" + pwFile + "&7 file to the latest format (it was outdated).");

                        if (!main.data.getConfig().contains("worlds")) return;

                        for (String worldName : main.data.getConfig().getStringList("worlds")) {
                            main.data.getConfig().set("managed-worlds." + worldName + ".environment", World.Environment.NORMAL.toString());
                        }

                        main.data.getConfig().set("worlds", null);

                        Utils.LOGGER.info("&3File Manager: &7The &b" + pwFile + "&7 file has been migrated.");
                        break;
                    default:
                        break;
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value " + pwFile);
        }
    }

    public enum PWFile {
        SETTINGS(1),
        ADVANCED_SETTINGS(1),
        MESSAGES(4),
        DATA(2),
        LICENSE(-1); // -1 = 'do not migrate me!'

        public final int latestFileVersion;

        PWFile(int latestFileVersion) {
            this.latestFileVersion = latestFileVersion;
        }
    }
}
