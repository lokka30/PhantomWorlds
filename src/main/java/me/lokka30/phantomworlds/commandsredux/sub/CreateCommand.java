package me.lokka30.phantomworlds.commandsredux.sub;
/*
 * Phantom Worlds
 * Copyright (C) 2023 - 2024 Daniel "creatorfromhell" Vidmar
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import me.lokka30.microlib.maths.QuickTimer;
import me.lokka30.microlib.messaging.MultiMessage;
import me.lokka30.phantomworlds.PhantomWorlds;
import me.lokka30.phantomworlds.misc.Utils;
import me.lokka30.phantomworlds.world.PhantomWorld;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * CreateCommand
 *
 * @author creatorfromhell
 * @since 2.0.5.0
 */
public class CreateCommand {

  public static void onCommand(final CommandSender sender, final String worldName, final World.Environment environment, final List<String> settings) {

    if(Bukkit.getWorld(worldName) != null) {
      (new MultiMessage(
              PhantomWorlds.instance().messages.getConfig()
                      .getStringList("command.phantomworlds.subcommands.create.already-loaded"),
              Arrays.asList(
                      new MultiMessage.Placeholder("prefix", PhantomWorlds.instance().messages.getConfig()
                              .getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                      new MultiMessage.Placeholder("world", worldName, false),
                      new MultiMessage.Placeholder("label", "pw", false)
              ))).send(sender);
      return;
    }

    if(environment == null) {
      (new MultiMessage(
              PhantomWorlds.instance().messages.getConfig().getStringList(
                      "command.phantomworlds.subcommands.create.options.invalid-environment"),
              Arrays.asList(
                      new MultiMessage.Placeholder("prefix", PhantomWorlds.instance().messages.getConfig()
                              .getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                      new MultiMessage.Placeholder("type", "", false),
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
    GameMode mode = GameMode.SURVIVAL;
    for(final String setting : settings) {

      final String[] split = setting.split(":", 2);
      if(split.length != 2) {
        (new MultiMessage(
                PhantomWorlds.instance().messages.getConfig().getStringList(
                        "command.phantomworlds.subcommands.create.options.invalid-option"),
                Arrays.asList(
                        new MultiMessage.Placeholder("prefix", PhantomWorlds.instance().messages.getConfig()
                                .getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                        new MultiMessage.Placeholder("option", setting, false),
                        new MultiMessage.Placeholder("options", String.join(
                                PhantomWorlds.instance().messages.getConfig()
                                        .getString("common.list-delimiter", "&7, &b"),
                                Arrays.asList("genStructures", "gen", "genSettings", "hardcore",
                                        "seed", "type", "spawnMobs", "spawnAnimals",
                                        "keepSpawnInMemory", "allowPvP", "difficulty", "gamemode")
                        ), true)
                ))).send(sender);
        return;
      }

      final String option = split[0].toLowerCase(Locale.ROOT);
      final StringBuilder value = new StringBuilder(split[1]);

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
        case "gamemode":
          mode = GameMode.valueOf(value.toString());
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

    final PhantomWorld pworld = new PhantomWorld(
            worldName, environment, generateStructures, generator,
            generatorSettings, hardcore, seed, worldType, spawnMobs,
            spawnAnimals, keepSpawnInMemory, allowPvP, difficulty, mode
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

    pworld.save();

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
                    new MultiMessage.Placeholder("label", "pw", false)
            ))).send(sender);
  }
}