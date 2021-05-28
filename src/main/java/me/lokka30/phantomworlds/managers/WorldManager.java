package me.lokka30.phantomworlds.managers;

import me.lokka30.phantomworlds.PhantomWorlds;
import me.lokka30.phantomworlds.misc.Utils;
import org.bukkit.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public class WorldManager {

    private final PhantomWorlds main;

    public WorldManager(PhantomWorlds main) {
        this.main = main;
    }

    /**
     * For all worlds listed in PW's data file, if they aren't already loaded by Bukkit,
     * then tell Bukkit to load them
     */
    public void loadManagedWorlds() {
        Utils.LOGGER.info("&3World Manager: &7Loading managed worlds...");

        if (!main.data.getConfig().contains("worlds-to-load")) return;

        //noinspection ConstantConditions
        for (String worldName : main.data.getConfig().getConfigurationSection("worlds-to-load").getKeys(false)) {
            if (Bukkit.getWorld(worldName) != null) continue;

            Utils.LOGGER.info("Loading world '&b" + worldName + "&7'...");
            getPhantomWorldFromData(main, worldName).create();
        }
    }

    public PhantomWorld getPhantomWorldFromData(PhantomWorlds main, String name) {
        World.Environment environment = World.Environment.valueOf(main.data.getConfig().getString("worlds-to-load." + name + ".environment", "NORMAL"));
        boolean generateStructures = main.data.getConfig().getBoolean("worlds-to-load." + name + ".generateStructures", true);
        String generator = main.data.getConfig().getString("worlds-to-load." + name + ".generator", null);
        String generatorSettings = main.data.getConfig().getString("worlds-to-load." + name + ".generatorSettings", null);
        boolean hardcore = main.data.getConfig().getBoolean("worlds-to-load." + name + ".hardcore", false);
        long seed = main.data.getConfig().getLong("worlds-to-load." + name + ".seed", 0);
        WorldType worldType = WorldType.valueOf(main.data.getConfig().getString("worlds-to-load." + name + ".worldType", "NORMAL"));
        boolean spawnMobs = main.data.getConfig().getBoolean("worlds-to-load." + name + ".spawnMobs", true);
        boolean spawnAnimals = main.data.getConfig().getBoolean("worlds-to-load." + name + ".spawnAnimals", true);
        boolean keepSpawnInMemory = main.data.getConfig().getBoolean("worlds-to-load." + name + ".keepSpawnInMemory", false);
        boolean allowPvP = main.data.getConfig().getBoolean("worlds-to-load." + name + ".allowPvP", true);
        Difficulty difficulty = Difficulty.valueOf(main.data.getConfig().getString("worlds-to-load." + name + ".difficulty", null));

        return new PhantomWorld(name, environment, generateStructures, generator, generatorSettings, hardcore, seed, worldType, spawnMobs, spawnAnimals, keepSpawnInMemory, allowPvP, difficulty);
    }

    public static class PhantomWorld {
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

        public PhantomWorld(@NotNull String name,
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
                            @NotNull Difficulty difficulty) {
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

        public void create() {
            final WorldCreator worldCreator = new WorldCreator(name);

            worldCreator.environment(environment);
            worldCreator.generateStructures(generateStructures);
            worldCreator.hardcore(hardcore);
            worldCreator.type(worldType);

            if (generator != null) {
                worldCreator.generator(generator);
            }
            if (generatorSettings != null) {
                worldCreator.generatorSettings(generatorSettings);
            }
            if (seed != null) {
                worldCreator.seed(seed);
            }

            World world = worldCreator.createWorld();

            if (world == null) {
                Utils.LOGGER.error("Unable to create/load world '&b" + name + "&7'!");
                return;
            }

            world.setSpawnFlags(spawnMobs, spawnAnimals);
            world.setKeepSpawnInMemory(keepSpawnInMemory);
            world.setPVP(allowPvP);
            world.setDifficulty(difficulty);
        }

        public void saveToData(PhantomWorlds main) {
            main.data.getConfig().set("worlds-to-load." + name + ".environment", environment.toString());
            main.data.getConfig().set("worlds-to-load." + name + ".generateStructures", generateStructures);
            main.data.getConfig().set("worlds-to-load." + name + ".generator", generator);
            main.data.getConfig().set("worlds-to-load." + name + ".generatorSettings", generatorSettings);
            main.data.getConfig().set("worlds-to-load." + name + ".hardcore", hardcore);
            main.data.getConfig().set("worlds-to-load." + name + ".seed", seed);
            main.data.getConfig().set("worlds-to-load." + name + ".worldType", worldType);
            main.data.getConfig().set("worlds-to-load." + name + ".spawnMobs", spawnMobs);
            main.data.getConfig().set("worlds-to-load." + name + ".spawnAnimals", spawnAnimals);
            main.data.getConfig().set("worlds-to-load." + name + ".keepSpawnInMemory", keepSpawnInMemory);
            main.data.getConfig().set("worlds-to-load." + name + ".allowPvP", allowPvP);
            main.data.getConfig().set("worlds-to-load." + name + ".difficulty", difficulty.toString());

            try {
                main.data.save();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
