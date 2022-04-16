package me.lokka30.phantomworlds.managers;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import me.lokka30.phantomworlds.PhantomWorlds;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Contains an assortment of methods to handle world management in PW.
 *
 * @author lokka30
 * @since v2.0.0
 */
public class WorldManager {

    private final PhantomWorlds main;

    public WorldManager(PhantomWorlds main) {
        this.main = main;
    }

    /**
     * For all worlds listed in PW's data file, if they aren't already loaded by Bukkit, then tell
     * Bukkit to load them
     *
     * @author lokka30
     * @since v2.0.0
     */
    public void loadManagedWorlds() {
        main.getLogger().info("Loading managed worlds...");

        if(!main.data.getConfig().contains("worlds-to-load")) {
            return;
        }

        final HashSet<String> worldsToDiscardFromDataFile = new HashSet<>();

        //noinspection ConstantConditions
        for(String worldName : main.data.getConfig().getConfigurationSection("worlds-to-load")
            .getKeys(false)) {
            if(Bukkit.getWorld(worldName) != null) {
                continue;
            }

            final File worldContainer = Bukkit.getWorldContainer();
            final File worldFolder = new File(
                worldContainer.getAbsolutePath() + File.separator + worldName);

            if(!worldContainer.exists()) {
                main.getLogger()
                    .severe("World container doesn't exist for world " + worldName + "!");
                return;
            }

            if(!worldFolder.exists()) {
                // The world was deleted/moved by the user so it must be re-imported. PW should no longer attempt to load that world.
                main.getLogger().info("Discarding world '" + worldName + "' from PhantomWorlds' "
                    + "data file as it no longer exists on the server.");
                worldsToDiscardFromDataFile.add(worldName);
                continue;
            }

            if(main.data.getConfig().getBoolean("worlds-to-load." + worldName + ".skip-autoload", false)) {
                main.getLogger().info("Skipping autoload of world '" + worldName + "'.");
                continue;
            }

            main.getLogger().info("Loading world '" + worldName + "'...");
            getPhantomWorldFromData(main, worldName).create();
        }

        for(String worldName : worldsToDiscardFromDataFile) {
            main.data.getConfig().set("worlds-to-load." + worldName, null);
        }

        try {
            main.data.save();
        } catch(IOException ex) {
            main.getLogger().severe("Unable to save data file. Stack trace:");
            ex.printStackTrace();
        }
    }

    /**
     * This creates a PhantomWorld object by scanning the data file by the specified name.
     * Developers are expected to make sure the specified world exists prior to retrieving it.
     *
     * @author lokka30
     * @since v2.0.0
     */
    public PhantomWorld getPhantomWorldFromData(PhantomWorlds main, String name) {
        final String cfgPath = "worlds-to-load." + name + ".";

        return new PhantomWorld(
            main,
            name,
            World.Environment.valueOf(
                main.data.getConfig().getString(cfgPath + "environment", "NORMAL")),
            main.data.getConfig().getBoolean(cfgPath + "generateStructures", true),
            main.data.getConfig().getString(cfgPath + "generator", null),
            main.data.getConfig().getString(cfgPath + "generatorSettings", null),
            main.data.getConfig().getBoolean(cfgPath + "hardcore", false),
            main.data.getConfig().getLong(cfgPath + "seed", 0),
            WorldType.valueOf(main.data.getConfig().getString(cfgPath + "worldType", "NORMAL")),
            main.data.getConfig().getBoolean(cfgPath + "spawnMobs", true),
            main.data.getConfig().getBoolean(cfgPath + "spawnAnimals", true),
            main.data.getConfig().getBoolean(cfgPath + "keepSpawnInMemory", false),
            main.data.getConfig().getBoolean(cfgPath + "allowPvP", true),
            Difficulty.valueOf(main.data.getConfig().getString(cfgPath + "difficulty", null))
        );
    }

    /**
     * PhantomWorld object to make it easier to work with PW-managed worlds.
     *
     * @author lokka30
     * @since v2.0.0
     */
    public static class PhantomWorld {

        // Here comes the jungle!

        private final PhantomWorlds main;
        public final String name;
        public final World.Environment environment;
        public final boolean generateStructures;
        public final String generator;
        public final String generatorSettings;
        public final boolean hardcore;
        public final Long seed;
        public final WorldType worldType;
        public final boolean spawnMobs;
        public final boolean spawnAnimals;
        public final boolean keepSpawnInMemory;
        public final boolean allowPvP;
        public final Difficulty difficulty;

        public PhantomWorld(
            @NotNull PhantomWorlds main,
            @NotNull String name,
            @NotNull World.Environment environment,
            boolean generateStructures,
            @Nullable String generator,
            @Nullable String generatorSettings,
            boolean hardcore,
            @Nullable Long seed,
            @NotNull WorldType worldType,
            boolean spawnMobs,
            boolean spawnAnimals,
            boolean keepSpawnInMemory,
            boolean allowPvP,
            @NotNull Difficulty difficulty
        ) {
            this.main = main;
            this.name = name;
            this.environment = environment;
            this.generateStructures = generateStructures;
            this.generator = generator;
            this.generatorSettings = generatorSettings;
            this.hardcore = hardcore;
            this.seed = seed;
            this.worldType = worldType;
            this.spawnMobs = spawnMobs;
            this.spawnAnimals = spawnAnimals;
            this.keepSpawnInMemory = keepSpawnInMemory;
            this.allowPvP = allowPvP;
            this.difficulty = difficulty;
        }

        /**
         * Create/import the world with specified settings.
         *
         * @author lokka30
         * @since v2.0.0
         */
        public void create() {
            final WorldCreator worldCreator = new WorldCreator(name);

            worldCreator.environment(environment);
            worldCreator.generateStructures(generateStructures);
            try {
                worldCreator.hardcore(hardcore);
            } catch(NoSuchMethodError ignored) {
            }
            worldCreator.type(worldType);

            if(generator != null) {
                worldCreator.generator(generator);
            }
            if(generatorSettings != null) {
                worldCreator.generatorSettings(generatorSettings);
            }
            if(seed != null) {
                worldCreator.seed(seed);
            }

            World world = worldCreator.createWorld();

            if(world == null) {
                main.getLogger().severe("Unable to create/load world '" + name + "'!");
                return;
            }

            world.setSpawnFlags(spawnMobs, spawnAnimals);
            world.setKeepSpawnInMemory(keepSpawnInMemory);
            world.setPVP(allowPvP);
            world.setDifficulty(difficulty);
        }
    }
}
