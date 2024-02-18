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

import me.lokka30.phantomworlds.misc.Utils;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * TeleportCommand
 *
 * @author creatorfromhell
 * @since 2.0.5.0
 */
public class TeleportCommand {

  public static void onCommand(final CommandSender sender, final World world, final Player player) {
    if(!Utils.checkWorld(sender, "command.phantomworlds.subcommands.teleport.usage", world)) {
      return;
    }
    Utils.teleportToWorld(sender, "teleport", "teleport", (player == null)? sender.getName() : player.getName(), world.getName());
  }
}