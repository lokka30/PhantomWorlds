package me.lokka30.phantomworlds.commands.phantomworlds;

import me.lokka30.microlib.messaging.MultiMessage;
import me.lokka30.phantomworlds.PhantomWorlds;
import me.lokka30.phantomworlds.commands.phantomworlds.subcommands.CreateSubcommand;
import me.lokka30.phantomworlds.commands.phantomworlds.subcommands.GameruleSubcommand;
import me.lokka30.phantomworlds.commands.phantomworlds.subcommands.SetSpawnSubcommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Command: /phantomworlds
 *
 * @author lokka30
 * @since v2.0.0
 */
public class PhantomWorldsCommand implements TabExecutor {

  final CreateSubcommand createSubcommand = new CreateSubcommand();
  final GameruleSubcommand gameruleSubcommand = new GameruleSubcommand();
  final SetSpawnSubcommand setSpawnSubcommand = new SetSpawnSubcommand();

  /**
   * @since v2.0.0
   */
  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd,
                           @NotNull String label, String[] args) {
    if(!sender.hasPermission("phantomworlds.command.phantomworlds")) {
      (new MultiMessage(
              PhantomWorlds.instance().messages.getConfig().getStringList("common.no-permission"), Arrays.asList(
              new MultiMessage.Placeholder("prefix",
                      PhantomWorlds.instance().messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"),
                      true),
              new MultiMessage.Placeholder("permission", "phantomworlds.command.phantomworlds",
                      false)
      ))).send(sender);
      return true;
    }

    if(args.length > 0) {
      switch(args[0].toLowerCase()) {
        case "create":
          createSubcommand.parseCommand(sender, cmd, label, args);
          break;
        case "gamerule":
          gameruleSubcommand.parseCommand(sender, cmd, label, args);
          break;
        case "setspawn":
          setSpawnSubcommand.parseCommand(sender, cmd, label, args);
          break;
        default:
          (new MultiMessage(
                  PhantomWorlds.instance().messages.getConfig()
                          .getStringList("command.phantomworlds.invalid-subcommand"),
                  Arrays.asList(
                          new MultiMessage.Placeholder("prefix", PhantomWorlds.instance().messages.getConfig()
                                  .getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                          new MultiMessage.Placeholder("arg", args[0], false)
                  ))).send(sender);

          sendAvailableSubcommands(sender, label);
          break;
      }
    } else {
      sendAvailableSubcommands(sender, label);
    }
    return true;
  }

  /**
   * Displays messages that list available subcommands for /phantomworlds
   *
   * @param label label of the command (alias used).
   * @param sender commandsender of the command
   *
   * @since v2.0.0
   */
  void sendAvailableSubcommands(CommandSender sender, String label) {
    (new MultiMessage(
            PhantomWorlds.instance().messages.getConfig().getStringList("command.phantomworlds.usage"), Arrays.asList(
            new MultiMessage.Placeholder("prefix",
                    PhantomWorlds.instance().messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"),
                    true),
            new MultiMessage.Placeholder("label", label, false)
    ))).send(sender);
  }

  /**
   * @since v2.0.0
   */
  @Override
  public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd,
                                    @NotNull String label, String[] args) {
    if(args.length == 1) {
      return Arrays.asList("create", "gamerule", "info", "list", "setspawn", "reload", "teleport", "tp",
              "spawn", "unload", "debug", "compatibility");
    }

    switch(args[0].toLowerCase(Locale.ROOT)) {
      case "create":
        return createSubcommand.parseTabCompletion(sender, cmd, label, args);
      case "gamerule":
        return gameruleSubcommand.parseTabCompletion(sender, cmd, label, args);
      case "setspawn":
        return setSpawnSubcommand.parseTabCompletion(sender, cmd, label, args);
      default:
        return Collections.emptyList();
    }
  }
}