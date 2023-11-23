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
import revxrsal.commands.annotation.Description;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.annotation.Usage;
import revxrsal.commands.bukkit.BukkitCommandActor;

/**
 * PWCommand
 *
 * @author creatorfromhell
 * @since 2.0.5.0
 */
@Command({"pw", "phantomworlds"})
public class PWCommand {

  @Subcommand({"create", "+", "new"})
  public void create(BukkitCommandActor actor) {

  }

  @Subcommand({"delete", "-", "remove", "del"})
  public void delete(BukkitCommandActor actor) {

  }

  @Subcommand({"backup", "archive", "bu"})
  public void backup(BukkitCommandActor actor) {

  }

  @Subcommand({"create", "+", "new"})
  @DefaultFor({"pw", "phantomworlds"})
  public void list(BukkitCommandActor actor) {

  }

  @Subcommand({"debug"})
  public void debug(BukkitCommandActor actor) {

  }

  @Subcommand({"gamerule", "rule"})
  public void gamerule(BukkitCommandActor actor) {

  }

  @Subcommand({"info", "i"})
  public void info(BukkitCommandActor actor) {

  }

  @Subcommand({"reload", "r"})
  public void reload(BukkitCommandActor actor) {

  }

  @Subcommand({"setspawn", "spawn"})
  public void setspawn(BukkitCommandActor actor) {

  }

  @Subcommand({"teleport", "tp"})
  public void tp(BukkitCommandActor actor) {

  }

  @Subcommand({"unload", "u"})
  public void unload(BukkitCommandActor actor) {

  }
}