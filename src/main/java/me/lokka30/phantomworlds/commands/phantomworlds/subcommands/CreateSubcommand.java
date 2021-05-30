package me.lokka30.phantomworlds.commands.phantomworlds.subcommands;

import me.lokka30.phantomworlds.PhantomWorlds;
import me.lokka30.phantomworlds.commands.ISubcommand;
import me.lokka30.phantomworlds.managers.WorldManager;
import me.lokka30.phantomworlds.misc.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class CreateSubcommand implements ISubcommand {

    /*
    TODO:
    - tab completion
    - test
    - permissions.yml
    - messages.yml
    - test
     */

    @Override
    public void parseCommand(@NotNull PhantomWorlds main, CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("phantomworlds.command.phantomworlds.create")) {
            sender.sendMessage("No permission");
            return;
        }

        if (args.length < 3) {
            sender.sendMessage("Incorrect usage. Try /pw create <WorldName> <Environment> [options...]");
            return;
        }

        final String worldName = args[1];
        if (Bukkit.getWorld(worldName) != null) {
            sender.sendMessage("World '" + worldName + "' is already loaded - teleport to it using /pw tp.");
            return;
        }

        World.Environment environment;
        try {
            environment = World.Environment.valueOf(args[2]);
        } catch (IllegalArgumentException ex) {
            sender.sendMessage("Environment type '" + args[2] + "' does not exist. Valid environment types: " + String.join(", ", Utils.getStringsOfEnumValues(World.Environment.values())));
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
                String arg = args[index].toLowerCase(Locale.ROOT);

                if (!arg.contains(":")) {
                    sender.sendMessage("Invalid world option '" + args[index] + "'.");
                    return;
                }

                String[] split = arg.split(":");
                String option = split[0];
                String value = split[1];

                if (option.startsWith("-")) {
                    option = option.substring(1);
                } // remove - character if present, those switching from PW v1 may still use it by accident.

                switch (option) {
                    case "generateStructures":
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
                                sender.sendMessage("Invalid world option value '" + value + "' for option '" + option + "'.");
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
                                sender.sendMessage("Invalid world option value '" + value + "' for option '" + option + "': Boolean (true/false) expected");
                                return;
                        }
                        break;
                    case "seed":
                        try {
                            seed = Long.valueOf(value);
                        } catch (NumberFormatException ex) {
                            sender.sendMessage("Invalid world option value '" + value + "' for option '" + option + "': Long (decimal number) expected");
                            return;
                        }
                        break;
                    case "type":
                    case "worldtype":
                        try {
                            worldType = WorldType.valueOf(value);
                        } catch (IllegalArgumentException ex) {
                            sender.sendMessage("Invalid world option value '" + value + "' for option '" + option + "': WorldType (" + String.join(", ", Utils.getStringsOfEnumValues(World.Environment.values())) + ") expected");
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
                                sender.sendMessage("Invalid world option value '" + value + "' for option '" + option + "': Boolean (true/false) expected");
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
                                sender.sendMessage("Invalid world option value '" + value + "' for option '" + option + "': Boolean (true/false) expected");
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
                                sender.sendMessage("Invalid world option value '" + value + "' for option '" + option + "': Boolean (true/false) expected");
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
                                sender.sendMessage("Invalid world option value '" + value + "' for option '" + option + "': Boolean (true/false) expected");
                                return;
                        }
                        break;
                    case "difficulty":
                    case "diff":
                        try {
                            difficulty = Difficulty.valueOf(value);
                        } catch (IllegalArgumentException ex) {
                            sender.sendMessage("Invalid world option value '" + value + "' for option '" + option + "': Difficulty (" + String.join(", ", Utils.getStringsOfEnumValues(Difficulty.values())) + ") expected");
                            return;
                        }
                        break;
                    default:
                        sender.sendMessage("Invalid world option '" + option + "'. Valid world options: generateStructures, generator, generatorSettings, hardcore, " +
                                "seed, worldType, spawnMobs, spawnAnimals, keepSpawnInMemory, allowPvP, difficulty");
                        break;
                }
            }
        }

        WorldManager.PhantomWorld pworld = new WorldManager.PhantomWorld(
                worldName, environment, generateStructures, generator,
                generatorSettings, hardcore, seed, worldType, spawnMobs,
                spawnAnimals, keepSpawnInMemory, allowPvP, difficulty
        );

        sender.sendMessage("Starting creation of world '" + worldName + "'...");

        main.data.getConfig().set("worlds-to-load." + worldName + ".environment", environment.toString());
        main.data.getConfig().set("worlds-to-load." + worldName + ".generateStructures", generateStructures);
        main.data.getConfig().set("worlds-to-load." + worldName + ".generator", generator);
        main.data.getConfig().set("worlds-to-load." + worldName + ".generatorSettings", generatorSettings);
        main.data.getConfig().set("worlds-to-load." + worldName + ".hardcore", hardcore);
        main.data.getConfig().set("worlds-to-load." + worldName + ".seed", seed);
        main.data.getConfig().set("worlds-to-load." + worldName + ".worldType", worldType);
        main.data.getConfig().set("worlds-to-load." + worldName + ".spawnMobs", spawnMobs);
        main.data.getConfig().set("worlds-to-load." + worldName + ".spawnAnimals", spawnAnimals);
        main.data.getConfig().set("worlds-to-load." + worldName + ".keepSpawnInMemory", keepSpawnInMemory);
        main.data.getConfig().set("worlds-to-load." + worldName + ".allowPvP", allowPvP);
        main.data.getConfig().set("worlds-to-load." + worldName + ".difficulty", difficulty.toString());

        try {
            main.data.save();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        pworld.create();

        sender.sendMessage("World '" + worldName + "' has been created. Teleport to it using '/pw tp " + worldName + "'.");
    }

    @Override
    public List<String> parseTabCompletion(PhantomWorlds main, CommandSender sender, Command cmd, String label, String[] args) {
        //TODO
        return null;
    }
}
