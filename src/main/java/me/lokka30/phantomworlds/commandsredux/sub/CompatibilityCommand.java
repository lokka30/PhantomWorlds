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

import me.lokka30.microlib.messaging.MultiMessage;
import me.lokka30.phantomworlds.PhantomWorlds;
import me.lokka30.phantomworlds.misc.CompatibilityChecker;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collections;

/**
 * CompatibilityCommand
 *
 * @author creatorfromhell
 * @since 2.0.5.0
 */
public class CompatibilityCommand {

  public static void onCommand(final CommandSender sender) {
    (new MultiMessage(
            PhantomWorlds.instance().messages.getConfig()
                    .getStringList("command.phantomworlds.subcommands.compatibility.start"),
            Collections.singletonList(
                    new MultiMessage.Placeholder("prefix",
                            PhantomWorlds.instance().messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"),
                            true)
            ))).send(sender);

    PhantomWorlds.instance().compatibilityChecker.checkAll();

    if(PhantomWorlds.instance().compatibilityChecker.incompatibilities.isEmpty()) {
      (new MultiMessage(
              PhantomWorlds.instance().messages.getConfig()
                      .getStringList("command.phantomworlds.subcommands.compatibility.found-none"),
              Collections.singletonList(
                      new MultiMessage.Placeholder("prefix", PhantomWorlds.instance().messages.getConfig()
                              .getString("common.prefix", "&b&lPhantomWorlds: &7"), true)
              ))).send(sender);
      return;
    }

    (new MultiMessage(
            PhantomWorlds.instance().messages.getConfig()
                    .getStringList("command.phantomworlds.subcommands.compatibility.found"),
            Arrays.asList(
                    new MultiMessage.Placeholder("prefix",
                            PhantomWorlds.instance().messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"),
                            true),
                    new MultiMessage.Placeholder("amount",
                            String.valueOf(PhantomWorlds.instance().compatibilityChecker.incompatibilities.size()), false)
            ))).send(sender);

    for(int i = 0; i < PhantomWorlds.instance().compatibilityChecker.incompatibilities.size(); i++) {
      CompatibilityChecker.Incompatibility incompatibility = PhantomWorlds.instance().compatibilityChecker.incompatibilities.get(
              i);

      (new MultiMessage(
              PhantomWorlds.instance().messages.getConfig()
                      .getStringList("command.phantomworlds.subcommands.compatibility.entry"),
              Arrays.asList(
                      new MultiMessage.Placeholder("prefix", PhantomWorlds.instance().messages.getConfig()
                              .getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                      new MultiMessage.Placeholder("index", String.valueOf(i + 1), false),
                      new MultiMessage.Placeholder("type", incompatibility.type.toString(), false),
                      new MultiMessage.Placeholder("reason", incompatibility.reason, true),
                      new MultiMessage.Placeholder("recommendation", incompatibility.recommendation,
                              true)
              ))).send(sender);
    }
  }
}