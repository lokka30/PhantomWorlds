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
import org.bukkit.command.CommandSender;

import java.util.Arrays;

/**
 * InfoCommand
 *
 * @author creatorfromhell
 * @since 2.0.5.0
 */
public class InfoCommand {

  public static void onCommand(final CommandSender sender) {

    (new MultiMessage(
            PhantomWorlds.instance().messages.getConfig()
                    .getStringList("command.phantomworlds.subcommands.info.success"), Arrays.asList(
            new MultiMessage.Placeholder("prefix",
                    PhantomWorlds.instance().messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"),
                    true),
            new MultiMessage.Placeholder("version", PhantomWorlds.instance().getDescription().getVersion(), false),
            new MultiMessage.Placeholder("authors",
                    String.join(PhantomWorlds.instance().messages.getConfig().getString("common.list-delimiter", "&7, &b"),
                            PhantomWorlds.instance().getDescription().getAuthors()), false),
            new MultiMessage.Placeholder("contributors",
                    String.join(PhantomWorlds.instance().messages.getConfig().getString("common.list-delimiter", "&7, &b"),
                            PhantomWorlds.CONTRIBUTORS), false),
            new MultiMessage.Placeholder("supportedServerVersions", PhantomWorlds.instance().supportedServerVersions,
                    false)
    ))).send(sender);
  }
}