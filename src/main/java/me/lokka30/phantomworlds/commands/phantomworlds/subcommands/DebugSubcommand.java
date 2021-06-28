package me.lokka30.phantomworlds.commands.phantomworlds.subcommands;

import me.lokka30.phantomworlds.PhantomWorlds;
import me.lokka30.phantomworlds.commands.ISubcommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author lokka30
 * @since v2.0.0
 */
public class DebugSubcommand implements ISubcommand {

    /*
    TODO
     - Messages.yml
     - Permissions.yml
     - Test
     */

    /*
    cmd: /pw debug ...
    arg:   -     0  1+
    len:   0     1  2+
     */

    /**
     * @author lokka30
     * @since v2.0.0
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
                TODO Planned: dump a bunch of useful information (server version, software, PW version, configs in pastebin, ...) to a file 'dump.txt'.
                 */
                sender.sendMessage("Work in progress.");
                break;
            default:
                sender.sendMessage("Invalid debug system '" + args[1] + "'.");
                sender.sendMessage("Note: Please do not run this subcommand unless you are sure you are meant to be doing so.");
                break;
        }
    }

    /**
     * @author lokka30
     * @since v2.0.0
     */
    @Override
    public List<String> parseTabCompletion(PhantomWorlds main, CommandSender sender, Command cmd, String label, String[] args) {
        return new ArrayList<>();
        /*
        Purposely, no tab-complete suggestions are given.
        This command is intended as a feature described to users by the support / development team.
        Users may mess with the command with unintended effects if they use it unguidedly.
         */
    }
}
