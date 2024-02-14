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

import me.lokka30.phantomworlds.misc.Utils;
import org.bukkit.World;
import org.bukkit.entity.Player;
import revxrsal.commands.bukkit.BukkitCommandActor;

/**
 * SpawnCommand
 *
 * @author creatorfromhell
 * @since 2.0.5.0
 */
public class SpawnCommand {

  public static void onCommand(final BukkitCommandActor actor, final World world, final Player player) {
    if(actor.isConsole() && !Utils.checkWorld(actor.getSender(), "command.phantomworlds.subcommands.spawn.usage", world)) {
      return;
    }
    Utils.teleportToWorld(actor.getSender(), "spawn", "spawn", (player == null)? actor.getSender().getName() : player.getName(), (actor.isPlayer() && world == null)? actor.getAsPlayer().getWorld().getName() : world.getName());
  }
}