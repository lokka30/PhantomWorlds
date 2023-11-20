package me.lokka30.phantomworlds.commands.phantomworlds.subcommands;

import me.lokka30.microlib.messaging.MultiMessage;
import me.lokka30.phantomworlds.PhantomWorlds;
import me.lokka30.phantomworlds.commands.Subcommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author lokka30
 * @since v2.0.0
 */
public class ReloadSubcommand implements Subcommand {

    /*
    cmd: /pw reload
    arg:   -      0
    len:   0      1
     */

  /**
   * @since v2.0.0
   */
  @Override
  public void parseCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if(!sender.hasPermission("phantomworlds.command.phantomworlds.reload")) {
      (new MultiMessage(
              PhantomWorlds.instance().messages.getConfig().getStringList("common.no-permission"), Arrays.asList(
              new MultiMessage.Placeholder("prefix",
                      PhantomWorlds.instance().messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"),
                      true),
              new MultiMessage.Placeholder("permission",
                      "phantomworlds.command.phantomworlds.reload", false)
      ))).send(sender);
      return;
    }

    if(args.length != 1) {
      (new MultiMessage(
              PhantomWorlds.instance().messages.getConfig()
                      .getStringList("command.phantomworlds.subcommands.reload.usage"), Arrays.asList(
              new MultiMessage.Placeholder("prefix",
                      PhantomWorlds.instance().messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"),
                      true),
              new MultiMessage.Placeholder("label", label, false)
      ))).send(sender);
      return;
    }

    (new MultiMessage(
            PhantomWorlds.instance().messages.getConfig()
                    .getStringList("command.phantomworlds.subcommands.reload.reloading-files"),
            Collections.singletonList(
                    new MultiMessage.Placeholder("prefix",
                            PhantomWorlds.instance().messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"),
                            true)
            ))).send(sender);

    PhantomWorlds.instance().loadFiles();

    (new MultiMessage(
            PhantomWorlds.instance().messages.getConfig()
                    .getStringList("command.phantomworlds.subcommands.reload.reloading-worlds"),
            Collections.singletonList(
                    new MultiMessage.Placeholder("prefix",
                            PhantomWorlds.instance().messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"),
                            true)
            ))).send(sender);

    PhantomWorlds.instance().loadWorlds();

    (new MultiMessage(
            PhantomWorlds.instance().messages.getConfig()
                    .getStringList("command.phantomworlds.subcommands.reload.reload-complete"),
            Collections.singletonList(
                    new MultiMessage.Placeholder("prefix",
                            PhantomWorlds.instance().messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"),
                            true)
            ))).send(sender);
  }

  /**
   * @since v2.0.0
   */
  @Override
  public List<String> parseTabCompletion(CommandSender sender, Command cmd, String label, String[] args) {
    return Collections.emptyList();
  }
}
