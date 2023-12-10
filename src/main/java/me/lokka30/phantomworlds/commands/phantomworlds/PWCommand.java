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

import me.lokka30.microlib.messaging.MultiMessage;
import me.lokka30.phantomworlds.PhantomWorlds;
import me.lokka30.phantomworlds.commands.phantomworlds.sub.BackupCommand;
import me.lokka30.phantomworlds.commands.phantomworlds.sub.CompatibilityCommand;
import me.lokka30.phantomworlds.commands.phantomworlds.sub.DebugCommand;
import me.lokka30.phantomworlds.commands.phantomworlds.sub.DeleteCommand;
import me.lokka30.phantomworlds.commands.phantomworlds.sub.ImportCommand;
import me.lokka30.phantomworlds.commands.phantomworlds.sub.InfoCommand;
import me.lokka30.phantomworlds.commands.phantomworlds.sub.ListCommand;
import me.lokka30.phantomworlds.commands.phantomworlds.sub.LoadCommand;
import me.lokka30.phantomworlds.commands.phantomworlds.sub.ReloadCommand;
import me.lokka30.phantomworlds.commands.phantomworlds.sub.SetSpawnCommand;
import me.lokka30.phantomworlds.commands.phantomworlds.sub.SpawnCommand;
import me.lokka30.phantomworlds.commands.phantomworlds.sub.TeleportCommand;
import me.lokka30.phantomworlds.commands.phantomworlds.sub.UnloadCommand;
import me.lokka30.phantomworlds.commands.phantomworlds.utils.WorldFolder;
import org.bukkit.World;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Default;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.BukkitCommandActor;
import revxrsal.commands.bukkit.annotation.CommandPermission;
import revxrsal.commands.help.CommandHelp;

import java.util.Arrays;

/**
 * PWCommand
 *
 * @author creatorfromhell
 * @since 2.0.5.0
 */
@Command({"pw", "phantomworlds"})
public class PWCommand {

  @Subcommand({"help", "?"})
  @DefaultFor({"pw", "phantomworlds"})
  public void help(BukkitCommandActor actor, CommandHelp<String> helpEntries, @Default("1") int page) {

    (new MultiMessage(
            PhantomWorlds.instance().messages.getConfig()
                    .getStringList("command.phantomworlds.help-header"), Arrays.asList(
            new MultiMessage.Placeholder("prefix",
                    PhantomWorlds.instance().messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"),
                    true),
            new MultiMessage.Placeholder("page", String.valueOf(page), false),
            new MultiMessage.Placeholder("max", String.valueOf(helpEntries.paginate(page, 5).size()), false)

    ))).send(actor.getSender());

    for(String str : PhantomWorlds.COMMAND_HELP) {
      actor.getSender().sendMessage(str);
    }
  }

  @Subcommand({"backup", "archive", "bu"})
  @CommandPermission("phantomworlds.command.phantomworlds.backup")
  @Description("command.phantomworlds.help.backup")
  public void backup(BukkitCommandActor actor, @Optional final World world) {
    BackupCommand.onCommand(actor, world);
  }

  @Subcommand({"create", "+", "new"})
  @CommandPermission("phantomworlds.command.phantomworlds.create")
  @Description("command.phantomworlds.help.create")
  public void create(BukkitCommandActor actor) {
    //todo: port create command
  }

  @Subcommand({"compatibility"})
  @CommandPermission("phantomworlds.command.phantomworlds.compatibility")
  @Description("command.phantomworlds.help.compatibility")
  public void compatibility(BukkitCommandActor actor) {
    CompatibilityCommand.onCommand(actor);
  }

  @Subcommand({"debug"})
  @CommandPermission("phantomworlds.command.phantomworlds.debug")
  @Description("command.phantomworlds.help.debug")
  public void debug(BukkitCommandActor actor, @Optional final String level) {
    DebugCommand.onCommand(actor, level);
  }

  @Subcommand({"delete", "-", "remove", "del"})
  @CommandPermission("phantomworlds.command.phantomworlds.delete")
  @Description("command.phantomworlds.help.delete")
  public void delete(BukkitCommandActor actor, @Optional final World world) {
    DeleteCommand.onCommand(actor, world);
  }

  @Subcommand({"list", "l"})
  @CommandPermission("phantomworlds.command.phantomworlds.list")
  @Description("command.phantomworlds.help.list")
  public void list(BukkitCommandActor actor) {
    ListCommand.onCommand(actor);
  }

  @Subcommand({"import", "im"})
  @CommandPermission("phantomworlds.command.phantomworlds.import")
  @Description("command.phantomworlds.help.import")
  public void importCMD(BukkitCommandActor actor, @Optional final World world) {
    ImportCommand.onCommand(actor, world);
  }

  @Subcommand({"info", "i"})
  @CommandPermission("phantomworlds.command.phantomworlds.info")
  @Description("command.phantomworlds.help.info")
  public void info(BukkitCommandActor actor) {
    InfoCommand.onCommand(actor);
  }

  @Subcommand({"load"})
  @CommandPermission("phantomworlds.command.phantomworlds.load")
  @Description("command.phantomworlds.help.load")
  public void load(BukkitCommandActor actor, @Optional final WorldFolder world) {
    LoadCommand.onCommand(actor, world);
  }

  @Subcommand({"reload", "r"})
  @CommandPermission("phantomworlds.command.phantomworlds.reload")
  @Description("command.phantomworlds.help.reload")
  public void reload(BukkitCommandActor actor) {
    ReloadCommand.onCommand(actor);
  }

  @Subcommand({"setspawn"})
  @CommandPermission("phantomworlds.command.phantomworlds.setspawn")
  @Description("command.phantomworlds.help.setspawn")
  public void setspawn(BukkitCommandActor actor, @Optional Double x, @Optional Double y, @Optional Double z, @Optional World world, @Optional Float yaw, @Optional Float pitch) {
    SetSpawnCommand.onCommand(actor, x, y, z, world, yaw, pitch);
  }

  @Subcommand({"spawn"})
  @CommandPermission("phantomworlds.command.phantomworlds.spawn")
  @Description("command.phantomworlds.help.spawn")
  public void spawn(BukkitCommandActor actor, @Optional final World world, @Optional final Player player) {
    SpawnCommand.onCommand(actor, world, player);
  }

  @Subcommand({"teleport", "tp"})
  @CommandPermission("phantomworlds.command.phantomworlds.teleport")
  @Description("command.phantomworlds.help.tp")
  public void tp(BukkitCommandActor actor, @Optional final World world, @Optional final Player player) {
    TeleportCommand.onCommand(actor, world, player);
  }

  @Subcommand({"unload", "u"})
  @CommandPermission("phantomworlds.command.phantomworlds.unload")
  @Description("command.phantomworlds.help.unload")
  public void unload(BukkitCommandActor actor, @Optional final World world) {
    UnloadCommand.onCommand(actor, world);
  }
}