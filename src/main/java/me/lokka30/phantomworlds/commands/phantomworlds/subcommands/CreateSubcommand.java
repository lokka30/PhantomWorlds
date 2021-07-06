package me.lokka30.phantomworlds.commands.phantomworlds.subcommands;

import me.lokka30.microlib.QuickTimer;
import me.lokka30.phantomworlds.PhantomWorlds;
import me.lokka30.phantomworlds.commands.ISubcommand;
import me.lokka30.phantomworlds.managers.WorldManager;
import me.lokka30.phantomworlds.misc.MultiMessage;
import me.lokka30.phantomworlds.misc.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * @author lokka30
 * @since v2.0.0
 */
public class CreateSubcommand implements ISubcommand {

    /*
    TODO:
     - tab completion
     - test
     */

    /*
    cmd: /pw create <world> <environment> [options...]
    arg:   -      0       1             2           3+
    len:   0      1       2             3           4+
     */

    /**
     * @author lokka30
     * @since v2.0.0
     */
    @Override
    public void parseCommand(@NotNull PhantomWorlds main, CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("phantomworlds.command.phantomworlds.create")) {
            (new MultiMessage(
                    main.messages.getConfig().getStringList("common.no-permission"), Arrays.asList(
                    new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                    new MultiMessage.Placeholder("permission", "phantomworlds.command.phantomworlds.create", false)
            ))).send(sender);
            return;
        }

        if (args.length < 3) {
            (new MultiMessage(
                    main.messages.getConfig().getStringList("command.phantomworlds.subcommands.create.usage"), Arrays.asList(
                    new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                    new MultiMessage.Placeholder("label", label, false)
            ))).send(sender);
            return;
        }

        final String worldName = args[1];
        if (Bukkit.getWorld(worldName) != null) {
            (new MultiMessage(
                    main.messages.getConfig().getStringList("command.phantomworlds.subcommands.create.already-loaded"), Arrays.asList(
                    new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                    new MultiMessage.Placeholder("world", worldName, false),
                    new MultiMessage.Placeholder("label", label, false)
            ))).send(sender);
            return;
        }

        World.Environment environment;
        try {
            environment = World.Environment.valueOf(args[2].toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            (new MultiMessage(
                    main.messages.getConfig().getStringList("command.phantomworlds.subcommands.create.options.invalid-environment"), Arrays.asList(
                    new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                    new MultiMessage.Placeholder("type", args[2], false),
                    new MultiMessage.Placeholder("types", String.join(main.messages.getConfig().getString("common.list-delimiter", "&7, &b"), Utils.getStringsOfEnumValues(World.Environment.values())), true)
            ))).send(sender);
            return;
        }

        /* Default options: */
        boolean generateStructures = true;
        String generator = null;
        String generatorSettings = null;
        boolean hardcore = false;
        Long seed = null;
        WorldType worldType = WorldType.NORMAL;
        boolean spawnMobs = true;
        boolean spawnAnimals = true;
        boolean keepSpawnInMemory = false;
        boolean allowPvP = true;
        Difficulty difficulty = Difficulty.NORMAL;

        if (args.length > 3) {
            for (int index = 3; index < args.length; index++) {
                String arg = args[index];

                String[] split = arg.split(":");
                if (split.length != 2) {
                    (new MultiMessage(
                            main.messages.getConfig().getStringList("command.phantomworlds.subcommands.create.options.invalid-option"), Arrays.asList(
                            new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                            new MultiMessage.Placeholder("option", args[index], false),
                            new MultiMessage.Placeholder("options", String.join(main.messages.getConfig().getString("common.list-delimiter", "&7, &b"),
                                    Arrays.asList("genStructures", "gen", "genSettings", "hardcore", "seed", "type", "spawnMobs", "spawnAnimals", "keepSpawnInMemory", "allowPvP", "difficulty")
                            ), false)
                    ))).send(sender);
                    return;
                }

                String option = split[0].toLowerCase(Locale.ROOT);
                String value = split[1].toLowerCase(Locale.ROOT);

                if (option.startsWith("-")) {
                    option = option.substring(1);
                } // remove - character if present, those switching from PW v1 may still use it by accident.

                switch (option) {
                    case "generatestructures":
                    case "genstructures":
                    case "structures":
                        switch (value) {
                            case "true":
                            case "t":
                            case "yes":
                            case "y":
                                generateStructures = true;
                                break;
                            case "false":
                            case "f":
                            case "no":
                            case "n":
                                generateStructures = false;
                                break;
                            default:
                                (new MultiMessage(
                                        main.messages.getConfig().getStringList("command.phantomworlds.subcommands.create.options.invalid-value"), Arrays.asList(
                                        new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                                        new MultiMessage.Placeholder("value", value, false),
                                        new MultiMessage.Placeholder("option", option, false),
                                        new MultiMessage.Placeholder("expected", "Boolean (true/false)", false)
                                ))).send(sender);
                                return;
                        }
                        break;
                    case "generator":
                    case "gen":
                        generator = value;
                        break;
                    case "generatorsettings":
                    case "gensettings":
                        generatorSettings = value;
                        break;
                    case "hardcore":
                        switch (value) {
                            case "true":
                            case "t":
                            case "yes":
                            case "y":
                                hardcore = true;
                                break;
                            case "false":
                            case "f":
                            case "no":
                            case "n":
                                hardcore = false;
                                break;
                            default:
                                (new MultiMessage(
                                        main.messages.getConfig().getStringList("command.phantomworlds.subcommands.create.options.invalid-value"), Arrays.asList(
                                        new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                                        new MultiMessage.Placeholder("value", value, false),
                                        new MultiMessage.Placeholder("option", option, false),
                                        new MultiMessage.Placeholder("expected", "Boolean (true/false)", false)
                                ))).send(sender);
                                return;
                        }
                        break;
                    case "seed":
                        try {
                            seed = Long.valueOf(value);
                        } catch (NumberFormatException ex) {
                            (new MultiMessage(
                                    main.messages.getConfig().getStringList("command.phantomworlds.subcommands.create.options.invalid-value"), Arrays.asList(
                                    new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                                    new MultiMessage.Placeholder("value", value, false),
                                    new MultiMessage.Placeholder("option", option, false),
                                    new MultiMessage.Placeholder("expected", "Long (any number)", false)
                            ))).send(sender);
                            return;
                        }
                        break;
                    case "type":
                    case "worldtype":
                        try {
                            worldType = WorldType.valueOf(value.toUpperCase(Locale.ROOT));
                        } catch (IllegalArgumentException ex) {
                            (new MultiMessage(
                                    main.messages.getConfig().getStringList("command.phantomworlds.subcommands.create.options.invalid-value-list"), Arrays.asList(
                                    new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                                    new MultiMessage.Placeholder("value", value, false),
                                    new MultiMessage.Placeholder("option", option, false),
                                    new MultiMessage.Placeholder("expected", "WorldType", false),
                                    new MultiMessage.Placeholder("values", String.join(main.messages.getConfig().getString("common.list-delimiter", "&7, &b"), Utils.getStringsOfEnumValues(WorldType.values())), true)
                            ))).send(sender);
                            return;
                        }
                        break;
                    case "spawnmobs":
                    case "mobs":
                        switch (value) {
                            case "true":
                            case "t":
                            case "yes":
                            case "y":
                                spawnMobs = true;
                                break;
                            case "false":
                            case "f":
                            case "no":
                            case "n":
                                spawnMobs = false;
                                break;
                            default:
                                (new MultiMessage(
                                        main.messages.getConfig().getStringList("command.phantomworlds.subcommands.create.options.invalid-value"), Arrays.asList(
                                        new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                                        new MultiMessage.Placeholder("value", value, false),
                                        new MultiMessage.Placeholder("option", option, false),
                                        new MultiMessage.Placeholder("expected", "Boolean (true/false)", false)
                                ))).send(sender);
                                return;
                        }
                        break;
                    case "spawnanimals":
                    case "animals":
                        switch (value) {
                            case "true":
                            case "t":
                            case "yes":
                            case "y":
                                spawnAnimals = true;
                                break;
                            case "false":
                            case "f":
                            case "no":
                            case "n":
                                spawnAnimals = false;
                                break;
                            default:
                                (new MultiMessage(
                                        main.messages.getConfig().getStringList("command.phantomworlds.subcommands.create.options.invalid-value"), Arrays.asList(
                                        new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                                        new MultiMessage.Placeholder("value", value, false),
                                        new MultiMessage.Placeholder("option", option, false),
                                        new MultiMessage.Placeholder("expected", "Boolean (true/false)", false)
                                ))).send(sender);
                                return;
                        }
                        break;
                    case "keepspawninmemory":
                    case "spawninmempry":
                        switch (value) {
                            case "true":
                            case "t":
                            case "yes":
                            case "y":
                                keepSpawnInMemory = true;
                                break;
                            case "false":
                            case "f":
                            case "no":
                            case "n":
                                keepSpawnInMemory = false;
                                break;
                            default:
                                (new MultiMessage(
                                        main.messages.getConfig().getStringList("command.phantomworlds.subcommands.create.options.invalid-value"), Arrays.asList(
                                        new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                                        new MultiMessage.Placeholder("value", value, false),
                                        new MultiMessage.Placeholder("option", option, false),
                                        new MultiMessage.Placeholder("expected", "Boolean (true/false)", false)
                                ))).send(sender);
                                return;
                        }
                        break;
                    case "allowpvp":
                    case "pvp":
                        switch (value) {
                            case "true":
                            case "t":
                            case "yes":
                            case "y":
                                allowPvP = true;
                                break;
                            case "false":
                            case "f":
                            case "no":
                            case "n":
                                allowPvP = false;
                                break;
                            default:
                                (new MultiMessage(
                                        main.messages.getConfig().getStringList("command.phantomworlds.subcommands.create.options.invalid-value"), Arrays.asList(
                                        new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                                        new MultiMessage.Placeholder("value", value, false),
                                        new MultiMessage.Placeholder("option", option, false),
                                        new MultiMessage.Placeholder("expected", "Boolean (true/false)", false)
                                ))).send(sender);
                                return;
                        }
                        break;
                    case "difficulty":
                    case "diff":
                        try {
                            difficulty = Difficulty.valueOf(value.toUpperCase(Locale.ROOT));
                        } catch (IllegalArgumentException ex) {
                            (new MultiMessage(
                                    main.messages.getConfig().getStringList("command.phantomworlds.subcommands.create.options.invalid-value-list"), Arrays.asList(
                                    new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                                    new MultiMessage.Placeholder("value", value, false),
                                    new MultiMessage.Placeholder("option", option, false),
                                    new MultiMessage.Placeholder("expected", "Difficulty", false),
                                    new MultiMessage.Placeholder("values", String.join(main.messages.getConfig().getString("common.list-delimiter", "&7, &b"), Utils.getStringsOfEnumValues(Difficulty.values())), true)
                            ))).send(sender);
                            return;
                        }
                        break;
                    default:
                        (new MultiMessage(
                                main.messages.getConfig().getStringList("command.phantomworlds.subcommands.create.options.invalid-option"), Arrays.asList(
                                new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                                new MultiMessage.Placeholder("option", option, false),
                                new MultiMessage.Placeholder("options", String.join(main.messages.getConfig().getString("common.list-delimiter", "&7, &b"),
                                        Arrays.asList("genStructures", "gen", "genSettings", "hardcore", "seed", "type", "spawnMobs", "spawnAnimals", "keepSpawnInMemory", "allowPvP", "difficulty")
                                ), false)
                        ))).send(sender);
                        break;
                }
            }
        }

        WorldManager.PhantomWorld pworld = new WorldManager.PhantomWorld(
                worldName, environment, generateStructures, generator,
                generatorSettings, hardcore, seed, worldType, spawnMobs,
                spawnAnimals, keepSpawnInMemory, allowPvP, difficulty
        );

        final QuickTimer quickTimer = new QuickTimer();

        (new MultiMessage(
                main.messages.getConfig().getStringList("command.phantomworlds.subcommands.create.creation.starting"), Arrays.asList(
                new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                new MultiMessage.Placeholder("world", worldName, false)
        ))).send(sender);

        (new MultiMessage(
                main.messages.getConfig().getStringList("command.phantomworlds.subcommands.create.creation.saving-world-data"), Arrays.asList(
                new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                new MultiMessage.Placeholder("world", worldName, false)
        ))).send(sender);

        final String cfgPath = "worlds-to-load." + worldName + ".";
        main.data.getConfig().set(cfgPath + "environment", environment.toString());
        main.data.getConfig().set(cfgPath + "generateStructures", generateStructures);
        main.data.getConfig().set(cfgPath + "generator", generator);
        main.data.getConfig().set(cfgPath + "generatorSettings", generatorSettings);
        main.data.getConfig().set(cfgPath + "hardcore", hardcore);
        main.data.getConfig().set(cfgPath + "seed", seed);
        main.data.getConfig().set(cfgPath + "worldType", worldType.toString());
        main.data.getConfig().set(cfgPath + "spawnMobs", spawnMobs);
        main.data.getConfig().set(cfgPath + "spawnAnimals", spawnAnimals);
        main.data.getConfig().set(cfgPath + "keepSpawnInMemory", keepSpawnInMemory);
        main.data.getConfig().set(cfgPath + "allowPvP", allowPvP);
        main.data.getConfig().set(cfgPath + "difficulty", difficulty.toString());

        try {
            main.data.save();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        (new MultiMessage(
                main.messages.getConfig().getStringList("command.phantomworlds.subcommands.create.creation.constructing-world"), Arrays.asList(
                new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                new MultiMessage.Placeholder("world", worldName, false)
        ))).send(sender);

        pworld.create();

        (new MultiMessage(
                main.messages.getConfig().getStringList("command.phantomworlds.subcommands.create.creation.complete"), Arrays.asList(
                new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                new MultiMessage.Placeholder("world", worldName, false),
                new MultiMessage.Placeholder("time", quickTimer.getTimer() + "", false),
                new MultiMessage.Placeholder("label", label, false)
        ))).send(sender);
    }

    /**
     * @author lokka30
     * @since v2.0.0
     */
    @Override
    public List<String> parseTabCompletion(PhantomWorlds main, CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("phantomworlds.command.phantomworlds.create")) {
            return new ArrayList<>();
        }

        //TODO
        return null;
    }
}
