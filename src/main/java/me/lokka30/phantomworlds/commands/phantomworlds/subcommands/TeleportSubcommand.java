package me.lokka30.phantomworlds.commands.phantomworlds.subcommands;

import me.lokka30.phantomworlds.PhantomWorlds;
import me.lokka30.phantomworlds.commands.ISubcommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lokka30
 * @since v2.0.0
 */
public class TeleportSubcommand implements ISubcommand {

    /*
    TODO
    - Command
    - Tab completion
    - Test
    - Messages.yml
    - Permissions.yml
    - Test
     */

    /**
     * @author lokka30
     * @since v2.0.0
     */
    @Override
    public void parseCommand(@NotNull PhantomWorlds main, CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("phantomworlds.command.phantomworlds.teleport")) {
            sender.sendMessage("No permission.");
            return;
        }

        if (args.length < 2 || args.length > 3) {
            sender.sendMessage("Usage: /" + label + " teleport <world> [player]");
            return;
        }

        sender.sendMessage("Work in progress.");
    }

    /**
     * @author lokka30
     * @since v2.0.0
     */
    @Override
    public List<String> parseTabCompletion(PhantomWorlds main, CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("phantomworlds.command.phantomworlds.teleport")) {
            return new ArrayList<>();
        }

        //TODO
        return null;
    }
}
