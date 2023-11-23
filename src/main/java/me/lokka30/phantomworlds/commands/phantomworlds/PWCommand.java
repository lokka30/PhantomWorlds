package me.lokka30.phantomworlds.commands.phantomworlds;
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

import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.BukkitCommandActor;
import revxrsal.commands.bukkit.annotation.CommandPermission;

/**
 * PWCommand
 *
 * @author creatorfromhell
 * @since 2.0.5.0
 */
@Command({"pw", "phantomworlds"})
public class PWCommand {

  @Subcommand({"create", "+", "new"})
  @CommandPermission("phantomworlds.command.phantomworlds.create")
  public void create(BukkitCommandActor actor) {

  }

  @Subcommand({"delete", "-", "remove", "del"})
  @CommandPermission("phantomworlds.command.phantomworlds.delete")
  public void delete(BukkitCommandActor actor) {

  }

  @Subcommand({"backup", "archive", "bu"})
  @CommandPermission("phantomworlds.command.phantomworlds.backup")
  public void backup(BukkitCommandActor actor) {

  }

  @Subcommand({"create", "+", "new"})
  @DefaultFor({"pw", "phantomworlds"})
  @CommandPermission("phantomworlds.command.phantomworlds.list")
  public void list(BukkitCommandActor actor) {

  }

  @Subcommand({"debug"})
  @CommandPermission("phantomworlds.command.phantomworlds.debug")
  public void debug(BukkitCommandActor actor) {

  }

  @Subcommand({"gamerule", "rule"})
  @CommandPermission("phantomworlds.command.phantomworlds.gamerule")
  public void gamerule(BukkitCommandActor actor) {

  }

  @Subcommand({"info", "i"})
  @CommandPermission("phantomworlds.command.phantomworlds.info")
  public void info(BukkitCommandActor actor) {

  }

  @Subcommand({"reload", "r"})
  @CommandPermission("phantomworlds.command.phantomworlds.reload")
  public void reload(BukkitCommandActor actor) {

  }

  @Subcommand({"setspawn"})
  @CommandPermission("phantomworlds.command.phantomworlds.setspawn")
  public void setspawn(BukkitCommandActor actor) {

  }

  @Subcommand({"teleport", "tp"})
  @CommandPermission("phantomworlds.command.phantomworlds.teleport")
  public void tp(BukkitCommandActor actor) {

  }

  @Subcommand({"unload", "u"})
  @CommandPermission("phantomworlds.command.phantomworlds.unload")
  public void unload(BukkitCommandActor actor) {

  }
}