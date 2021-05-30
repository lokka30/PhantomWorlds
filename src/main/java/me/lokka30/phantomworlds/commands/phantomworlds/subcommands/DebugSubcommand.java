package me.lokka30.phantomworlds.commands.phantomworlds.subcommands;

import me.lokka30.phantomworlds.PhantomWorlds;
import me.lokka30.phantomworlds.commands.ISubcommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DebugSubcommand implements ISubcommand {

    /*
    TODO
    - Test
    - Messages.yml
    - Permissions.yml
    - Test
     */

    @Override
    public void parseCommand(@NotNull PhantomWorlds main, CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("phantomworlds.command.phantomworlds.debug")) {
            sender.sendMessage("No permission");
            return;
        }

        if (args.length == 1) {
            sender.sendMessage("Please specify a valid debug system to initialise.");
            return;
        }

        //noinspection SwitchStatementWithTooFewBranches
        switch (args[1].toLowerCase(Locale.ROOT)) {
            case "dump":
                /*
                Planned: dump a bunch of useful information (server version, software, PW version, configs in pastebin, ...) to a file 'dump.txt'.
                 */
                sender.sendMessage("Work in Progress.");
                break;
            default:
                sender.sendMessage("Invalid debug system '" + args[1] + "'.");
                break;
        }
    }

    @Override
    public List<String> parseTabCompletion(PhantomWorlds main, CommandSender sender, Command cmd, String label, String[] args) {
        return new ArrayList<>();
    }
}
