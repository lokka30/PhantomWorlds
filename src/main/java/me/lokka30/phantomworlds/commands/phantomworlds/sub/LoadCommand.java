package me.lokka30.phantomworlds.commands.phantomworlds.sub;
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
import me.lokka30.phantomworlds.commands.phantomworlds.utils.WorldFolder;
import me.lokka30.phantomworlds.misc.WorldLoadResponse;
import org.bukkit.Bukkit;
import revxrsal.commands.bukkit.BukkitCommandActor;

import java.io.File;
import java.util.Arrays;

import static me.lokka30.phantomworlds.misc.WorldLoadResponse.ALREADY_LOADED;
import static me.lokka30.phantomworlds.misc.WorldLoadResponse.INVALID;
import static me.lokka30.phantomworlds.misc.WorldLoadResponse.LOADED;

/**
 * LoadCommand
 *
 * @author creatorfromhell
 * @since 2.0.5.0
 */
public class LoadCommand {

  public static void onCommand(final BukkitCommandActor actor, final WorldFolder world) {

    if(world == null || world.getFolder() == null) {
      (new MultiMessage(
              PhantomWorlds.instance().messages.getConfig()
                      .getStringList("command.phantomworlds.usages.load"), Arrays.asList(
              new MultiMessage.Placeholder("prefix",
                      PhantomWorlds.instance().messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"),
                      true),
              new MultiMessage.Placeholder("label", "pw", false)
      ))).send(actor.getSender());
      return;
    }

    final WorldLoadResponse response = PhantomWorlds.worldManager().loadWorld(world.getFolder());

    if(response == ALREADY_LOADED) {
      (new MultiMessage(
              PhantomWorlds.instance().messages.getConfig()
                      .getStringList("command.phantomworlds.subcommands.create.already-loaded"),
              Arrays.asList(
                      new MultiMessage.Placeholder("prefix", PhantomWorlds.instance().messages.getConfig()
                              .getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                      new MultiMessage.Placeholder("world", world.getFolder(), false),
                      new MultiMessage.Placeholder("label", "pw", false)
              ))).send(actor.getSender());
      return;
    }

    if(response == INVALID) {
      (new MultiMessage(
              PhantomWorlds.instance().messages.getConfig()
                      .getStringList("command.phantomworlds.subcommands.create.failure-folder"),
              Arrays.asList(
                      new MultiMessage.Placeholder("prefix", PhantomWorlds.instance().messages.getConfig()
                              .getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                      new MultiMessage.Placeholder("world", world.getFolder(), false),
                      new MultiMessage.Placeholder("label", "pw", false)
              ))).send(actor.getSender());
      return;
    }

    if(response != LOADED) {

      (new MultiMessage(
              PhantomWorlds.instance().messages.getConfig()
                      .getStringList("command.phantomworlds.subcommands.create.failure-loading"),
              Arrays.asList(
                      new MultiMessage.Placeholder("prefix", PhantomWorlds.instance().messages.getConfig()
                              .getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                      new MultiMessage.Placeholder("world", world.getFolder(), false),
                      new MultiMessage.Placeholder("label", "pw", false)
              ))).send(actor.getSender());
      return;
    }

    (new MultiMessage(
            PhantomWorlds.instance().messages.getConfig().getStringList(
                    "command.phantomworlds.subcommands.load.success"),
            Arrays.asList(
                    new MultiMessage.Placeholder("prefix", PhantomWorlds.instance().messages.getConfig()
                            .getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                    new MultiMessage.Placeholder("world", world.getFolder(), false)
            ))).send(actor.getSender());
  }
}