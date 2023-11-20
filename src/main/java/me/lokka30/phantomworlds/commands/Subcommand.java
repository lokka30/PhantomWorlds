package me.lokka30.phantomworlds.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * This interface makes it easier to create and utilise subcommands.
 *
 * @author lokka30
 * @since v2.0.0
 */
public interface Subcommand {

  /**
   * @since v2.0.0
   */
  void parseCommand(CommandSender sender, Command cmd, String label, String[] args);

  /**
   * @since v2.0.0
   */
  List<String> parseTabCompletion(CommandSender sender, Command cmd, String label, String[] args);

}
