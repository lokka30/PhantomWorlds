package me.lokka30.phantomworlds.commands.phantomworlds.subcommands;

import me.lokka30.microlib.maths.QuickTimer;
import me.lokka30.microlib.messaging.MultiMessage;
import me.lokka30.phantomworlds.PhantomWorlds;
import me.lokka30.phantomworlds.commands.Subcommand;
import me.lokka30.phantomworlds.misc.Utils;
import me.lokka30.phantomworlds.world.PhantomWorld;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author lokka30
 * @since v2.0.0
 */
public class CreateSubcommand implements Subcommand {

  final ArrayList<String> TAB_COMPLETIONS_FOR_OPTIONS_ARGS;

  public CreateSubcommand() {
    TAB_COMPLETIONS_FOR_OPTIONS_ARGS = generateOptionsTabCompletionList();
  }

    /*
    cmd: /pw create <world> <environment> [options...]
    arg:   -      0       1             2           3+
    len:   0      1       2             3           4+
     */

  /**
   * @since v2.0.0
   */
  @Override
  public void parseCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if(!sender.hasPermission("phantomworlds.command.phantomworlds.create")) {
      (new MultiMessage(
              PhantomWorlds.instance().messages.getConfig().getStringList("common.no-permission"), Arrays.asList(
              new MultiMessage.Placeholder("prefix",
                      PhantomWorlds.instance().messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"),
                      true),
              new MultiMessage.Placeholder("permission",
                      "phantomworlds.command.phantomworlds.create", false)
      ))).send(sender);
      return;
    }

    if(args.length < 3) {
      (new MultiMessage(
              PhantomWorlds.instance().messages.getConfig()
                      .getStringList("command.phantomworlds.subcommands.create.usage"), Arrays.asList(
              new MultiMessage.Placeholder("prefix",
                      PhantomWorlds.instance().messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"),
                      true),
              new MultiMessage.Placeholder("label", label, false)
      ))).send(sender);
      return;
    }

    final String worldName = args[1];
    if(Bukkit.getWorld(worldName) != null) {
      (new MultiMessage(
              PhantomWorlds.instance().messages.getConfig()
                      .getStringList("command.phantomworlds.subcommands.create.already-loaded"),
              Arrays.asList(
                      new MultiMessage.Placeholder("prefix", PhantomWorlds.instance().messages.getConfig()
                              .getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                      new MultiMessage.Placeholder("world", worldName, false),
                      new MultiMessage.Placeholder("label", label, false)
              ))).send(sender);
      return;
    }

    World.Environment environment;
    try {
      environment = World.Environment.valueOf(args[2].toUpperCase(Locale.ROOT));
    } catch(IllegalArgumentException ex) {
      (new MultiMessage(
              PhantomWorlds.instance().messages.getConfig().getStringList(
                      "command.phantomworlds.subcommands.create.options.invalid-environment"),
              Arrays.asList(
                      new MultiMessage.Placeholder("prefix", PhantomWorlds.instance().messages.getConfig()
                              .getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                      new MultiMessage.Placeholder("type", args[2], false),
                      new MultiMessage.Placeholder("types", String.join(
                              PhantomWorlds.instance().messages.getConfig().getString("common.list-delimiter", "&7, &b"),
                              Utils.enumValuesToStringList(World.Environment.values())), true)
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

    if(args.length > 3) {
      for(int index = 3; index < args.length; index++) {
        String arg = args[index];

        String[] split = arg.split(":", 2);
        if(split.length != 2) {
          (new MultiMessage(
                  PhantomWorlds.instance().messages.getConfig().getStringList(
                          "command.phantomworlds.subcommands.create.options.invalid-option"),
                  Arrays.asList(
                          new MultiMessage.Placeholder("prefix", PhantomWorlds.instance().messages.getConfig()
                                  .getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                          new MultiMessage.Placeholder("option", args[index], false),
                          new MultiMessage.Placeholder("options", String.join(
                                  PhantomWorlds.instance().messages.getConfig()
                                          .getString("common.list-delimiter", "&7, &b"),
                                  Arrays.asList("genStructures", "gen", "genSettings", "hardcore",
                                          "seed", "type", "spawnMobs", "spawnAnimals",
                                          "keepSpawnInMemory", "allowPvP", "difficulty")
                          ), true)
                  ))).send(sender);
          return;
        }

        String option = split[0].toLowerCase(Locale.ROOT);
        StringBuilder value = new StringBuilder(split[1]);

        if(option.startsWith("-")) {
          option = option.substring(1);
        } // remove - character if present, those switching from PW v1 may still use it by accident.

        switch(option) {
          case "generatestructures":
          case "genstructures":
          case "structures":

            final Optional<Boolean> gen = Utils.parseFromString(sender, value, option);
            if(!gen.isPresent()) {
              return;
            }
            generateStructures = gen.get();
            break;

          case "generator":
          case "gen":
            generator = value.toString();
            break;

          case "generatorsettings":
          case "gensettings":
            generatorSettings = value.toString();
            break;
          case "hardcore":

            final Optional<Boolean> hard = Utils.parseFromString(sender, value, option);
            if(!hard.isPresent()) {
              return;
            }
            hardcore = hard.get();
            break;

          case "seed":
            try {
              seed = Long.valueOf(value.toString());
            } catch(NumberFormatException ex) {
              (new MultiMessage(
                      PhantomWorlds.instance().messages.getConfig().getStringList(
                              "command.phantomworlds.subcommands.create.options.invalid-value"),
                      Arrays.asList(
                              new MultiMessage.Placeholder("prefix", PhantomWorlds.instance().messages.getConfig()
                                      .getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                              new MultiMessage.Placeholder("value", value.toString(), false),
                              new MultiMessage.Placeholder("option", option, false),
                              new MultiMessage.Placeholder("expected", "Long (any number)",
                                      false)
                      ))).send(sender);
              return;
            }
            break;
          case "type":
          case "worldtype":
            try {
              worldType = WorldType.valueOf(
                      value.toString().toUpperCase(Locale.ROOT));
            } catch(IllegalArgumentException ex) {
              (new MultiMessage(
                      PhantomWorlds.instance().messages.getConfig().getStringList(
                              "command.phantomworlds.subcommands.create.options.invalid-value-list"),
                      Arrays.asList(
                              new MultiMessage.Placeholder("prefix", PhantomWorlds.instance().messages.getConfig()
                                      .getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                              new MultiMessage.Placeholder("value", value.toString(), false),
                              new MultiMessage.Placeholder("option", option, false),
                              new MultiMessage.Placeholder("expected", "WorldType", false),
                              new MultiMessage.Placeholder("values", String.join(
                                      PhantomWorlds.instance().messages.getConfig()
                                              .getString("common.list-delimiter", "&7, &b"),
                                      Utils.enumValuesToStringList(WorldType.values())), true)
                      ))).send(sender);
              return;
            }
            break;
          case "spawnmobs":
          case "mobs":

            final Optional<Boolean> mobs = Utils.parseFromString(sender, value, option);
            if(!mobs.isPresent()) {
              return;
            }
            spawnMobs = mobs.get();
            break;

          case "spawnanimals":
          case "animals":

            final Optional<Boolean> animals = Utils.parseFromString(sender, value, option);
            if(!animals.isPresent()) {
              return;
            }
            spawnAnimals = animals.get();
            break;

          case "keepspawninmemory":
          case "spawninmemory":

            final Optional<Boolean> spawn = Utils.parseFromString(sender, value, option);
            if(!spawn.isPresent()) {
              return;
            }
            keepSpawnInMemory = spawn.get();
            break;

          case "allowpvp":
          case "pvp":

            final Optional<Boolean> pvp = Utils.parseFromString(sender, value, option);
            if(!pvp.isPresent()) {
              return;
            }
            allowPvP = pvp.get();
            break;

          case "difficulty":
          case "diff":
            try {
              difficulty = Difficulty.valueOf(
                      value.toString().toUpperCase(Locale.ROOT));
            } catch(IllegalArgumentException ex) {
              (new MultiMessage(
                      PhantomWorlds.instance().messages.getConfig().getStringList(
                              "command.phantomworlds.subcommands.create.options.invalid-value-list"),
                      Arrays.asList(
                              new MultiMessage.Placeholder("prefix", PhantomWorlds.instance().messages.getConfig()
                                      .getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                              new MultiMessage.Placeholder("value", value.toString(), false),
                              new MultiMessage.Placeholder("option", option, false),
                              new MultiMessage.Placeholder("expected", "Difficulty", false),
                              new MultiMessage.Placeholder("values", String.join(
                                      PhantomWorlds.instance().messages.getConfig()
                                              .getString("common.list-delimiter", "&7, &b"),
                                      Utils.enumValuesToStringList(Difficulty.values())), true)
                      ))).send(sender);
              return;
            }
            break;
          default:
            (new MultiMessage(
                    PhantomWorlds.instance().messages.getConfig().getStringList(
                            "command.phantomworlds.subcommands.create.options.invalid-option"),
                    Arrays.asList(
                            new MultiMessage.Placeholder("prefix", PhantomWorlds.instance().messages.getConfig()
                                    .getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                            new MultiMessage.Placeholder("option", option, false),
                            new MultiMessage.Placeholder("options", String.join(
                                    PhantomWorlds.instance().messages.getConfig()
                                            .getString("common.list-delimiter", "&7, &b"),
                                    Arrays.asList("genStructures", "gen", "genSettings", "hardcore",
                                            "seed", "type", "spawnMobs", "spawnAnimals",
                                            "keepSpawnInMemory", "allowPvP", "difficulty")
                            ), true)
                    ))).send(sender);
            return;
        }
      }
    }

    final PhantomWorld pworld = new PhantomWorld(
            worldName, environment, generateStructures, generator,
            generatorSettings, hardcore, seed, worldType, spawnMobs,
            spawnAnimals, keepSpawnInMemory, allowPvP, difficulty
    );

    final QuickTimer quickTimer = new QuickTimer(TimeUnit.MILLISECONDS);

    (new MultiMessage(
            PhantomWorlds.instance().messages.getConfig()
                    .getStringList("command.phantomworlds.subcommands.create.creation.starting"),
            Arrays.asList(
                    new MultiMessage.Placeholder("prefix",
                            PhantomWorlds.instance().messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"),
                            true),
                    new MultiMessage.Placeholder("world", worldName, false)
            ))).send(sender);

    (new MultiMessage(
            PhantomWorlds.instance().messages.getConfig().getStringList(
                    "command.phantomworlds.subcommands.create.creation.saving-world-data"),
            Arrays.asList(
                    new MultiMessage.Placeholder("prefix",
                            PhantomWorlds.instance().messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"),
                            true),
                    new MultiMessage.Placeholder("world", worldName, false)
            ))).send(sender);

    final String cfgPath = "worlds-to-load." + worldName + ".";
    PhantomWorlds.instance().data.getConfig().set(cfgPath + "environment", environment.toString());
    PhantomWorlds.instance().data.getConfig().set(cfgPath + "generateStructures", generateStructures);
    PhantomWorlds.instance().data.getConfig().set(cfgPath + "generator", generator);
    PhantomWorlds.instance().data.getConfig().set(cfgPath + "generatorSettings", generatorSettings);
    PhantomWorlds.instance().data.getConfig().set(cfgPath + "hardcore", hardcore);
    PhantomWorlds.instance().data.getConfig().set(cfgPath + "seed", seed);
    PhantomWorlds.instance().data.getConfig().set(cfgPath + "worldType", worldType.toString());
    PhantomWorlds.instance().data.getConfig().set(cfgPath + "spawnMobs", spawnMobs);
    PhantomWorlds.instance().data.getConfig().set(cfgPath + "spawnAnimals", spawnAnimals);
    PhantomWorlds.instance().data.getConfig().set(cfgPath + "keepSpawnInMemory", keepSpawnInMemory);
    PhantomWorlds.instance().data.getConfig().set(cfgPath + "allowPvP", allowPvP);
    PhantomWorlds.instance().data.getConfig().set(cfgPath + "difficulty", difficulty.toString());

    try {
      PhantomWorlds.instance().data.save();
    } catch(final IOException ex) {
      throw new RuntimeException(ex);
    }

    (new MultiMessage(
            PhantomWorlds.instance().messages.getConfig().getStringList(
                    "command.phantomworlds.subcommands.create.creation.constructing-world"),
            Arrays.asList(
                    new MultiMessage.Placeholder("prefix",
                            PhantomWorlds.instance().messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"),
                            true),
                    new MultiMessage.Placeholder("world", worldName, false)
            ))).send(sender);

    pworld.create();

    (new MultiMessage(
            PhantomWorlds.instance().messages.getConfig()
                    .getStringList("command.phantomworlds.subcommands.create.creation.complete"),
            Arrays.asList(
                    new MultiMessage.Placeholder("prefix",
                            PhantomWorlds.instance().messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"),
                            true),
                    new MultiMessage.Placeholder("world", worldName, false),
                    new MultiMessage.Placeholder("time", Long.toString(quickTimer.getDuration()), false),
                    new MultiMessage.Placeholder("label", label, false)
            ))).send(sender);
  }

  /**
   * @since v2.0.0
   */
  @Override
  public List<String> parseTabCompletion(CommandSender sender, Command cmd, String label, String[] args) {
    if(!sender.hasPermission("phantomworlds.command.phantomworlds.create")) {
      return Collections.emptyList();
    }

    if(args.length == 2) {
      return Collections.singletonList("ExampleWorldName");
    }

    if(args.length == 3) {
      return Utils.enumValuesToStringList(World.Environment.values());
    }

    if(args.length > 3) {
      return TAB_COMPLETIONS_FOR_OPTIONS_ARGS;
    }

    return Collections.emptyList();
  }

  ArrayList<String> generateOptionsTabCompletionList() {
    final ArrayList<String> suggestions = new ArrayList<>();

    suggestions.addAll(addTrueFalseValues("generatestructures"));
    suggestions.addAll(addTrueFalseValues("genstructures"));
    suggestions.addAll(addTrueFalseValues("structures"));
    suggestions.addAll(addTrueFalseValues("spawnmobs"));
    suggestions.addAll(addTrueFalseValues("mobs"));
    suggestions.addAll(addTrueFalseValues("spawnanimals"));
    suggestions.addAll(addTrueFalseValues("animals"));
    suggestions.addAll(addTrueFalseValues("keepspawninmemory"));
    suggestions.addAll(addTrueFalseValues("spawninmemory"));
    suggestions.addAll(addTrueFalseValues("hardcore"));
    suggestions.addAll(addTrueFalseValues("allowpvp"));
    suggestions.addAll(addTrueFalseValues("pvp"));
    suggestions.addAll(addTrueFalseValues("difficulty"));
    suggestions.addAll(addTrueFalseValues("diff"));

    suggestions.add("generator:");
    suggestions.add("gen:");

    suggestions.add("generatorsettings:");
    suggestions.add("gensettings:");

    suggestions.add("seed:");

    for(WorldType worldType : WorldType.values()) {
      suggestions.add("type:" + worldType.toString());
    }

    return suggestions;
  }

  ArrayList<String> addTrueFalseValues(String option) {
    final ArrayList<String> list = new ArrayList<>();
    option = option + ":";

    list.add(option + "true");
    list.add(option + "t");
    list.add(option + "yes");
    list.add(option + "y");
    list.add(option + "false");
    list.add(option + "f");
    list.add(option + "no");
    list.add(option + "n");

    return list;
  }
}
