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
import me.lokka30.phantomworlds.misc.Utils;
import org.bukkit.World;
import org.bukkit.entity.Player;
import revxrsal.commands.bukkit.BukkitCommandActor;

import java.util.Arrays;

/**
 * UnloadCommand
 *
 * @author creatorfromhell
 * @since 2.0.5.0
 */
public class UnloadCommand {

  public static void onCommand(final BukkitCommandActor actor, final World world) {
    if(!Utils.checkWorld(actor.getSender(), "command.phantomworlds.subcommands.unload.usage", world)) {
      return;
    }

    if(actor.getSender() instanceof Player) {

      if(world.getPlayers().contains((Player)actor.getSender())) {
        (new MultiMessage(
                PhantomWorlds.instance().messages.getConfig().getStringList(
                        "command.phantomworlds.subcommands.unload.in-specified-world"),
                Arrays.asList(
                        new MultiMessage.Placeholder("prefix", PhantomWorlds.instance().messages.getConfig()
                                .getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                        new MultiMessage.Placeholder("world", world.getName(), false)
                ))).send(actor.getSender());
        return;
      }
    }

    //noinspection ConstantConditions
    Utils.unloadWorld(world);

    (new MultiMessage(
            PhantomWorlds.instance().messages.getConfig()
                    .getStringList("command.phantomworlds.subcommands.unload.success"), Arrays.asList(
            new MultiMessage.Placeholder("prefix",
                    PhantomWorlds.instance().messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"),
                    true),
            new MultiMessage.Placeholder("world", world.getName(), false)
    ))).send(actor.getSender());
  }
}