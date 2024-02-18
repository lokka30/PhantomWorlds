package me.lokka30.phantomworlds.commandsredux;
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

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.description.Description;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.optional.OptionalArg;
import dev.rollczi.litecommands.annotations.permission.Permission;
import me.lokka30.phantomworlds.commandsredux.sub.BackupCommand;
import me.lokka30.phantomworlds.commandsredux.sub.CompatibilityCommand;
import me.lokka30.phantomworlds.commandsredux.sub.CreateCommand;
import me.lokka30.phantomworlds.commandsredux.sub.DebugCommand;
import me.lokka30.phantomworlds.commandsredux.sub.DeleteCommand;
import me.lokka30.phantomworlds.commandsredux.sub.ImportCommand;
import me.lokka30.phantomworlds.commandsredux.sub.InfoCommand;
import me.lokka30.phantomworlds.commandsredux.sub.ListCommand;
import me.lokka30.phantomworlds.commandsredux.sub.LoadCommand;
import me.lokka30.phantomworlds.commandsredux.sub.ReloadCommand;
import me.lokka30.phantomworlds.commandsredux.sub.SetSpawnCommand;
import me.lokka30.phantomworlds.commandsredux.sub.SpawnCommand;
import me.lokka30.phantomworlds.commandsredux.sub.TeleportCommand;
import me.lokka30.phantomworlds.commandsredux.sub.UnloadCommand;
import me.lokka30.phantomworlds.commandsredux.sub.set.SetEffectsCommand;
import me.lokka30.phantomworlds.commandsredux.sub.set.SetGamemodeCommand;
import me.lokka30.phantomworlds.commandsredux.sub.set.SetPortalCommand;
import me.lokka30.phantomworlds.commandsredux.sub.set.SetWhitelistCommand;
import me.lokka30.phantomworlds.commandsredux.utils.WorldFolder;
import org.bukkit.GameMode;
import org.bukkit.PortalType;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * PWCommand
 *
 * @author creatorfromhell
 * @since 2.0.5.0
 */
@Command(name = "phantomworlds", aliases = {"pw"})
public class PWCommand {

  @Execute(name = "backup", aliases = {"archive", "bu"})
  @Permission("phantomworlds.command.phantomworlds.backup")
  @Description("command.phantomworlds.help.backup")
  public void backup(@Context CommandSender commandSender, @OptionalArg("world") final World world) {
    BackupCommand.onCommand(commandSender, world);
  }

  @Execute(name = "create", aliases = {"+", "new"})
  @Permission("phantomworlds.command.phantomworlds.create")
  @Description("command.phantomworlds.help.create")
  public void create(@Context CommandSender commandSender, @Arg("world name") final String name, @Arg("environment")World.Environment environment, @Arg("world-setting") List<String> settings) {
    CreateCommand.onCommand(commandSender, name, environment, settings);
  }

  @Execute(name = "compatibility")
  @Permission("phantomworlds.command.phantomworlds.compatibility")
  @Description("command.phantomworlds.help.compatibility")
  public void compatibility(@Context CommandSender commandSender) {
    CompatibilityCommand.onCommand(commandSender);
  }

  @Execute(name = "debug")
  @Permission("phantomworlds.command.phantomworlds.debug")
  @Description("command.phantomworlds.help.debug")
  public void debug(@Context CommandSender commandSender, @OptionalArg("level") final String level) {
    DebugCommand.onCommand(commandSender, level);
  }

  @Execute(name = "delete", aliases = {"-", "remove", "del"})
  @Permission("phantomworlds.command.phantomworlds.delete")
  @Description("command.phantomworlds.help.delete")
  public void delete(@Context CommandSender commandSender, @OptionalArg("world") final World world) {
    DeleteCommand.onCommand(commandSender, world);
  }

  @Execute(name = "list", aliases = {"l"})
  @Permission("phantomworlds.command.phantomworlds.list")
  @Description("command.phantomworlds.help.list")
  public void list(@Context CommandSender commandSender) {
    ListCommand.onCommand(commandSender);
  }

  @Execute(name = "import", aliases = {"im"})
  @Permission("phantomworlds.command.phantomworlds.import")
  @Description("command.phantomworlds.help.import")
  public void importCMD(@Context CommandSender commandSender, @OptionalArg("world") final World world) {
    ImportCommand.onCommand(commandSender, world);
  }

  @Execute(name = "info", aliases = {"i"})
  @Permission("phantomworlds.command.phantomworlds.info")
  @Description("command.phantomworlds.help.info")
  public void info(@Context CommandSender commandSender) {
    InfoCommand.onCommand(commandSender);
  }

  @Execute(name = "load")
  @Permission("phantomworlds.command.phantomworlds.load")
  @Description("command.phantomworlds.help.load")
  public void load(@Context CommandSender commandSender, @OptionalArg("world folder") final WorldFolder world) {
    LoadCommand.onCommand(commandSender, world);
  }

  @Execute(name = "reload", aliases = {"r"})
  @Permission("phantomworlds.command.phantomworlds.reload")
  @Description("command.phantomworlds.help.reload")
  public void reload(@Context CommandSender commandSender) {
    ReloadCommand.onCommand(commandSender);
  }

  @Execute(name = "set effects", aliases = {"set eff"})
  @Permission("phantomworlds.command.phantomworlds.set.effects")
  @Description("command.phantomworlds.help.seteffects")
  public void setEffects(@Context CommandSender commandSender, @Arg("world") World world, @Arg("potion-effects") List<String> effects) {
    SetEffectsCommand.onCommand(commandSender, world, effects);
  }

  @Execute(name = "set gamemode", aliases = {"set mode"})
  @Permission("phantomworlds.command.phantomworlds.set.gamemode")
  @Description("command.phantomworlds.help.setgamemode")
  public void setGamemode(@Context CommandSender commandSender, @Arg("world") World world, @Arg("mode") GameMode mode) {
    SetGamemodeCommand.onCommand(commandSender, world, mode);
  }

  @Execute(name = "set portal")
  @Permission("phantomworlds.command.phantomworlds.set.portal")
  @Description("command.phantomworlds.help.setportal")
  public void setPortal(@Context CommandSender commandSender, @Arg("world") World world, @Arg("portal type") PortalType portal, @Arg("world to") World worldTo) {
    SetPortalCommand.onCommand(commandSender, world, portal, worldTo);
  }

  @Execute(name = "set whitelist")
  @Permission("phantomworlds.command.phantomworlds.set.whitelist")
  @Description("command.phantomworlds.help.setwhitelist")
  public void setWhitelist(@Context CommandSender commandSender, @Arg("world") World world, @Arg("whitelist") boolean whitelist) {
    SetWhitelistCommand.onCommand(commandSender, world, whitelist);
  }

  @Execute(name = "setspawn", aliases = {"ss"})
  @Permission("phantomworlds.command.phantomworlds.setspawn")
  @Description("command.phantomworlds.help.setspawn")
  public void setspawn(@Context CommandSender commandSender, @OptionalArg("x") Double x, @OptionalArg("y") Double y, @OptionalArg("z") Double z, @OptionalArg("world") World world, @OptionalArg("yaw") Float yaw, @OptionalArg("pitch") Float pitch) {
    SetSpawnCommand.onCommand(commandSender, x, y, z, world, yaw, pitch);
  }

  @Execute(name = "spawn")
  @Permission("phantomworlds.command.phantomworlds.spawn")
  @Description("command.phantomworlds.help.spawn")
  public void spawn(@Context CommandSender commandSender, @OptionalArg("world") final World world, @OptionalArg("target") final Player player) {
    SpawnCommand.onCommand(commandSender, world, player);
  }

  @Execute(name = "teleport", aliases = {"tp"})
  @Permission("phantomworlds.command.phantomworlds.teleport")
  @Description("command.phantomworlds.help.tp")
  public void tp(@Context CommandSender commandSender, @OptionalArg("world") final World world, @OptionalArg("target") final Player player) {
    TeleportCommand.onCommand(commandSender, world, player);
  }

  @Execute(name = "unload", aliases = {"u"})
  @Permission("phantomworlds.command.phantomworlds.unload")
  @Description("command.phantomworlds.help.unload")
  public void unload(@Context CommandSender commandSender, @OptionalArg("world") final World world) {
    UnloadCommand.onCommand(commandSender, world);
  }
}