package me.lokka30.phantomworlds.commands.phantomworlds.subcommands;
/*
 * Phantom Worlds
 * Copyright (C) 2023 Daniel "creatorfromhell" Vidmar
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

import me.lokka30.microlib.messaging.MultiMessage;
import me.lokka30.phantomworlds.PhantomWorlds;
import me.lokka30.phantomworlds.commands.Subcommand;
import me.lokka30.phantomworlds.misc.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.units.qual.A;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * GameruleSubcommand
 *
 * @author creatorfromhell
 * @since 2.0.5.0
 */
public class GameruleSubcommand implements Subcommand {

  final ArrayList<String> TAB_COMPLETIONS_FOR_OPTIONS_ARGS;

  public GameruleSubcommand() {
    TAB_COMPLETIONS_FOR_OPTIONS_ARGS = generateOptionsTabCompletionList();
  }

    /*
    cmd: /pw set <world> <environment> [options...]
    arg:   -      0       1             2           3+
    len:   0      1       2             3           4+
     */

  /**
   * @since v2.0.0
   */
  @Override
  public void parseCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if(!sender.hasPermission("phantomworlds.command.phantomworlds.gamerule")) {
      (new MultiMessage(
              PhantomWorlds.instance().messages.getConfig().getStringList("common.no-permission"), Arrays.asList(
              new MultiMessage.Placeholder("prefix",
                      PhantomWorlds.instance().messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"),
                      true),
              new MultiMessage.Placeholder("permission",
                      "phantomworlds.command.phantomworlds.gamerule", false)
      ))).send(sender);
      return;
    }

    if(args.length < 2) {
      (new MultiMessage(
              PhantomWorlds.instance().messages.getConfig()
                      .getStringList("command.phantomworlds.subcommands.gamerule.usage"), Arrays.asList(
              new MultiMessage.Placeholder("prefix",
                      PhantomWorlds.instance().messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"),
                      true),
              new MultiMessage.Placeholder("label", label, false)
      ))).send(sender);
      return;
    }

    final String worldName = args[1];

    if(Bukkit.getWorld(worldName) == null) {
      (new MultiMessage(
              PhantomWorlds.instance().messages.getConfig()
                      .getStringList("command.phantomworlds.subcommands.common.invalid-world"),
              Arrays.asList(
                      new MultiMessage.Placeholder("prefix", PhantomWorlds.instance().messages.getConfig()
                              .getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                      new MultiMessage.Placeholder("world", worldName, false)
              ))).send(sender);
      return;
    }

    if(args.length >= 3) {

      final Map<String, String> gamerules = new HashMap<>();

      for(int index = 2; index < args.length; index++) {
        final String arg = args[index];

        final String[] split = arg.split(":", 2);
        if(split.length != 2) {
          (new MultiMessage(
                  PhantomWorlds.instance().messages.getConfig().getStringList(
                          "command.phantomworlds.subcommands.gamerule.rules.invalid-rule"),
                  Arrays.asList(
                          new MultiMessage.Placeholder("prefix", PhantomWorlds.instance().messages.getConfig()
                                  .getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                          new MultiMessage.Placeholder("rule", args[index], false),
                          new MultiMessage.Placeholder("rules", String.join(
                                  PhantomWorlds.instance().messages.getConfig()
                                          .getString("common.list-delimiter", "&7, &b"),
                                  generateRules()
                          ), true)
                  ))).send(sender);
          continue;
        }

        final String rule = split[0].toLowerCase(Locale.ROOT);
        final String value = split[1];

        final GameRule<?> ruleInstance = GameRule.getByName(rule);
        if(ruleInstance == null) {
          (new MultiMessage(
                  PhantomWorlds.instance().messages.getConfig().getStringList(
                          "command.phantomworlds.subcommands.gamerule.rules.invalid-rule"),
                  Arrays.asList(
                          new MultiMessage.Placeholder("prefix", PhantomWorlds.instance().messages.getConfig()
                                  .getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                          new MultiMessage.Placeholder("rule", args[index], false),
                          new MultiMessage.Placeholder("rules", String.join(
                                  PhantomWorlds.instance().messages.getConfig()
                                          .getString("common.list-delimiter", "&7, &b"),
                                  generateRules()
                          ), true)
                  ))).send(sender);
          continue;
        }

        //Validate our rule value:
        if(ruleInstance.getType() == Integer.class) {
          try {
            Integer.valueOf(value);
          } catch(Exception ignore) {
            (new MultiMessage(
                    PhantomWorlds.instance().messages.getConfig().getStringList(
                            "command.phantomworlds.subcommands.gamerule.rules.invalid-value"),
                    Arrays.asList(
                            new MultiMessage.Placeholder("prefix", PhantomWorlds.instance().messages.getConfig()
                                    .getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                            new MultiMessage.Placeholder("value", value, false),
                            new MultiMessage.Placeholder("rule", rule, false),
                            new MultiMessage.Placeholder("expected", "Numeric", false)
                    ))).send(sender);
            continue;
          }
        }

        gamerules.put(rule, value);
      }

      final String cfgPath = "worlds-to-load." + worldName + ".rules.";
      for(Map.Entry<String, String> entry : gamerules.entrySet()) {
        PhantomWorlds.instance().data.getConfig().set(cfgPath + entry.getKey(), entry.getValue());
      }

      try {
        PhantomWorlds.instance().data.save();
      } catch(final IOException ex) {
        throw new RuntimeException(ex);
      }
    }
  }

  /**
   * @since v2.0.0
   */
  @Override
  public List<String> parseTabCompletion(CommandSender sender, Command cmd, String label, String[] args) {
    if(!sender.hasPermission("phantomworlds.command.phantomworlds.gamerule")) {
      return Collections.emptyList();
    }

    if(args.length == 2) {
      return new ArrayList<>(Utils.getLoadedWorldsNameList());
    }

    if(args.length >= 3) {
      return TAB_COMPLETIONS_FOR_OPTIONS_ARGS;
    }
    return Collections.emptyList();
  }

  ArrayList<String> generateOptionsTabCompletionList() {
    final ArrayList<String> suggestions = new ArrayList<>();

    for(GameRule<?> rule : GameRule.values()) {
      if(rule.getType() == Boolean.class) {
        suggestions.addAll(addTrueFalseValues(rule.getName()));
      } else {
        suggestions.add(rule.getName() + ":<insert integer>");
      }
    }
    return suggestions;
  }

  ArrayList<String> generateRules() {
    final ArrayList<String> rules = new ArrayList<>();


    for(GameRule<?> rule : GameRule.values()) {
      rules.add(rule.getName());
    }

    return rules;
  }

  ArrayList<String> addTrueFalseValues(String option) {
    final ArrayList<String> list = new ArrayList<>();
    option = option + ":";

    list.add(option + "true");
    list.add(option + "false");

    return list;
  }
}