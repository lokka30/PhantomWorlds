package me.lokka30.phantomworlds.commands;

import me.lokka30.phantomworlds.PhantomWorlds;
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
     * @author lokka30
     * @since v2.0.0
     */
    void parseCommand(PhantomWorlds main, CommandSender sender, Command cmd, String label, String[] args);

    /**
     * @author lokka30
     * @since v2.0.0
     */
    List<String> parseTabCompletion(PhantomWorlds main, CommandSender sender, Command cmd, String label, String[] args);

}
