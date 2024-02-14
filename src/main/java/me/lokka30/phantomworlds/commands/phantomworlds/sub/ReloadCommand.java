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
import revxrsal.commands.bukkit.BukkitCommandActor;

import java.util.Collections;

/**
 * ReloadCommand
 *
 * @author creatorfromhell
 * @since 2.0.5.0
 */
public class ReloadCommand {

  public static void onCommand(final BukkitCommandActor actor) {
    (new MultiMessage(
            PhantomWorlds.instance().messages.getConfig()
                    .getStringList("command.phantomworlds.subcommands.reload.reloading-files"),
            Collections.singletonList(
                    new MultiMessage.Placeholder("prefix",
                            PhantomWorlds.instance().messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"),
                            true)
            ))).send(actor.getSender());

    PhantomWorlds.instance().loadFiles();

    (new MultiMessage(
            PhantomWorlds.instance().messages.getConfig()
                    .getStringList("command.phantomworlds.subcommands.reload.reloading-worlds"),
            Collections.singletonList(
                    new MultiMessage.Placeholder("prefix",
                            PhantomWorlds.instance().messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"),
                            true)
            ))).send(actor.getSender());

    PhantomWorlds.instance().loadWorlds();

    (new MultiMessage(
            PhantomWorlds.instance().messages.getConfig()
                    .getStringList("command.phantomworlds.subcommands.reload.reload-complete"),
            Collections.singletonList(
                    new MultiMessage.Placeholder("prefix",
                            PhantomWorlds.instance().messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"),
                            true)
            ))).send(actor.getSender());
  }
}