package me.lokka30.phantomworlds.commands.phantomworlds.subcommands;

import me.lokka30.microlib.messaging.MultiMessage;
import me.lokka30.phantomworlds.PhantomWorlds;
import me.lokka30.phantomworlds.commands.Subcommand;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author lokka30
 * @since v2.0.0
 */
public class ListSubcommand implements Subcommand {

    /*
    cmd: /pw list
    arg:   -    0
    len:   0    1
     */

  /**
   * @since v2.0.0
   */
  @Override
  public void parseCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if(!sender.hasPermission("phantomworlds.command.phantomworlds.list")) {
      (new MultiMessage(
              PhantomWorlds.instance().messages.getConfig().getStringList("common.no-permission"), Arrays.asList(
              new MultiMessage.Placeholder("prefix",
                      PhantomWorlds.instance().messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"),
                      true),
              new MultiMessage.Placeholder("permission",
                      "phantomworlds.command.phantomworlds.list", false)
      ))).send(sender);
      return;
    }

    if(args.length != 1) {
      (new MultiMessage(
              PhantomWorlds.instance().messages.getConfig()
                      .getStringList("command.phantomworlds.subcommands.list.usage"), Arrays.asList(
              new MultiMessage.Placeholder("prefix",
                      PhantomWorlds.instance().messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"),
                      true),
              new MultiMessage.Placeholder("label", label, false)
      ))).send(sender);
      return;
    }

    (new MultiMessage(
            PhantomWorlds.instance().messages.getConfig()
                    .getStringList("command.phantomworlds.subcommands.list.header"), Arrays.asList(
            new MultiMessage.Placeholder("prefix",
                    PhantomWorlds.instance().messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"),
                    true),
            new MultiMessage.Placeholder("amount", String.valueOf(Bukkit.getWorlds().size()), false)
    ))).send(sender);

    for(World world : Bukkit.getWorlds()) {
      (new MultiMessage(
              PhantomWorlds.instance().messages.getConfig()
                      .getStringList("command.phantomworlds.subcommands.list.entry"), Arrays.asList(
              new MultiMessage.Placeholder("prefix",
                      PhantomWorlds.instance().messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"),
                      true),
              new MultiMessage.Placeholder("world", world.getName(), false)
      ))).send(sender);
    }
  }

  /**
   * @since v2.0.0
   */
  @Override
  public List<String> parseTabCompletion(CommandSender sender, Command cmd, String label, String[] args) {
    return Collections.emptyList();
  }
}
