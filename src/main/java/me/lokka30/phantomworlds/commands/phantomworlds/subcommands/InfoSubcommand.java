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
public class InfoSubcommand implements Subcommand {

    /*
    cmd: /pw info
    arg:   -    0
    len:   0    1
     */

  /**
   * @since v2.0.0
   */
  @Override
  public void parseCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if(!sender.hasPermission("phantomworlds.command.phantomworlds.info")) {
      (new MultiMessage(
              PhantomWorlds.instance().messages.getConfig().getStringList("common.no-permission"), Arrays.asList(
              new MultiMessage.Placeholder("prefix",
                      PhantomWorlds.instance().messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"),
                      true),
              new MultiMessage.Placeholder("permission",
                      "phantomworlds.command.phantomworlds.info", false)
      ))).send(sender);
      return;
    }

    if(args.length != 1) {
      (new MultiMessage(
              PhantomWorlds.instance().messages.getConfig()
                      .getStringList("command.phantomworlds.subcommands.info.usage"), Arrays.asList(
              new MultiMessage.Placeholder("prefix",
                      PhantomWorlds.instance().messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"),
                      true),
              new MultiMessage.Placeholder("label", label, false)
      ))).send(sender);
      return;
    }

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

  /**
   * @since v2.0.0
   */
  @Override
  public List<String> parseTabCompletion(CommandSender sender, Command cmd, String label, String[] args) {
    return Collections.emptyList();
  }
}