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

import me.lokka30.phantomworlds.commands.phantomworlds.parameters.sub.BackupCommand;
import me.lokka30.phantomworlds.commands.phantomworlds.parameters.sub.CompatibilityCommand;
import me.lokka30.phantomworlds.commands.phantomworlds.parameters.sub.DebugCommand;
import me.lokka30.phantomworlds.commands.phantomworlds.parameters.sub.DeleteCommand;
import me.lokka30.phantomworlds.commands.phantomworlds.parameters.sub.InfoCommand;
import me.lokka30.phantomworlds.commands.phantomworlds.parameters.sub.ListCommand;
import me.lokka30.phantomworlds.commands.phantomworlds.parameters.sub.ReloadCommand;
import me.lokka30.phantomworlds.commands.phantomworlds.parameters.sub.SetSpawnCommand;
import me.lokka30.phantomworlds.commands.phantomworlds.parameters.sub.SpawnCommand;
import me.lokka30.phantomworlds.commands.phantomworlds.parameters.sub.TeleportCommand;
import me.lokka30.phantomworlds.commands.phantomworlds.parameters.sub.UnloadCommand;
import org.bukkit.World;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Optional;
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

  @Subcommand({"backup", "archive", "bu"})
  @CommandPermission("phantomworlds.command.phantomworlds.backup")
  public void backup(BukkitCommandActor actor, final World world) {
    BackupCommand.onCommand(actor, world);
  }

  @Subcommand({"create", "+", "new"})
  @CommandPermission("phantomworlds.command.phantomworlds.create")
  public void create(BukkitCommandActor actor) {
    //todo: port create command
  }

  @Subcommand({"compatibility"})
  @CommandPermission("phantomworlds.command.phantomworlds.compatibility")
  public void compatibility(BukkitCommandActor actor) {
    CompatibilityCommand.onCommand(actor);
  }

  @Subcommand({"debug"})
  @CommandPermission("phantomworlds.command.phantomworlds.debug")
  public void debug(BukkitCommandActor actor, final String level) {
    DebugCommand.onCommand(actor, level);
  }

  @Subcommand({"delete", "-", "remove", "del"})
  @CommandPermission("phantomworlds.command.phantomworlds.delete")
  public void delete(BukkitCommandActor actor, final World world) {
    DeleteCommand.onCommand(actor, world);
  }

  @Subcommand({"create", "+", "new"})
  @DefaultFor({"pw", "phantomworlds"})
  @CommandPermission("phantomworlds.command.phantomworlds.list")
  public void list(BukkitCommandActor actor) {
    ListCommand.onCommand(actor);
  }

  @Subcommand({"gamerule", "rule"})
  @CommandPermission("phantomworlds.command.phantomworlds.gamerule")
  public void gamerule(BukkitCommandActor actor) {
    //todo: port gamerule command
  }

  @Subcommand({"import", "im"})
  @CommandPermission("phantomworlds.command.phantomworlds.import")
  public void importCMD(BukkitCommandActor actor, final World world) {
    InfoCommand.onCommand(actor);
  }

  @Subcommand({"info", "i"})
  @CommandPermission("phantomworlds.command.phantomworlds.info")
  public void info(BukkitCommandActor actor) {
    InfoCommand.onCommand(actor);
  }

  @Subcommand({"reload", "r"})
  @CommandPermission("phantomworlds.command.phantomworlds.reload")
  public void reload(BukkitCommandActor actor) {
    ReloadCommand.onCommand(actor);
  }

  @Subcommand({"setspawn"})
  @CommandPermission("phantomworlds.command.phantomworlds.setspawn")
  public void setspawn(BukkitCommandActor actor, @Optional Double x, @Optional Double y, @Optional Double z, @Optional World world, @Optional Float yaw, @Optional Float pitch) {
    SetSpawnCommand.onCommand(actor, x, y, z, world, yaw, pitch);
  }

  @Subcommand({"spawn"})
  @CommandPermission("phantomworlds.command.phantomworlds.spawn")
  public void spawn(BukkitCommandActor actor, final World world, @Optional final Player player) {
    SpawnCommand.onCommand(actor, world, player);
  }

  @Subcommand({"teleport", "tp"})
  @CommandPermission("phantomworlds.command.phantomworlds.teleport")
  public void tp(BukkitCommandActor actor, final World world, @Optional final Player player) {
    TeleportCommand.onCommand(actor, world, player);
  }

  @Subcommand({"unload", "u"})
  @CommandPermission("phantomworlds.command.phantomworlds.unload")
  public void unload(BukkitCommandActor actor, final World world) {
    UnloadCommand.onCommand(actor, world);
  }
}