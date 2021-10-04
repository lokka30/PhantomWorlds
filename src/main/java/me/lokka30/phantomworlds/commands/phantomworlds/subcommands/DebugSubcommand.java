package me.lokka30.phantomworlds.commands.phantomworlds.subcommands;

import me.lokka30.microlib.messaging.MessageUtils;
import me.lokka30.microlib.messaging.MultiMessage;
import me.lokka30.phantomworlds.PhantomWorlds;
import me.lokka30.phantomworlds.commands.ISubcommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * @author lokka30
 * @since v2.0.0
 */
public class DebugSubcommand implements ISubcommand {

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
            (new MultiMessage(
                    main.messages.getConfig().getStringList("common.no-permission"), Arrays.asList(
                    new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                    new MultiMessage.Placeholder("permission", "phantomworlds.command.phantomworlds.debug", false)
            ))).send(sender);
            return;
        }

        if (args.length == 1) {
            sender.sendMessage(MessageUtils.colorizeStandardCodes("&b&lPhantomWorlds: &7Usage: &b/" + label + " debug <method>"));
            return;
        }

        //noinspection SwitchStatementWithTooFewBranches
        switch (args[1].toLowerCase(Locale.ROOT)) {
            case "dump":
                sender.sendMessage(MessageUtils.colorizeStandardCodes("&b&lPhantomWorlds: &7Incomplete."));
                break;
            default:
                sender.sendMessage(MessageUtils.colorizeStandardCodes("&b&lPhantomWorlds: &7Invalid debug method '%method%'.")
                        .replace("%method%", args[1])
                );

                sender.sendMessage(MessageUtils.colorizeStandardCodes("&b&lPhantomWorlds: &7Note: Please do not run this subcommand unless you are sure you are meant to be doing so."));
                break;
        }
    }

    /**
     * @author lokka30
     * @since v2.0.0
     */
    @Override
    public List<String> parseTabCompletion(PhantomWorlds main, CommandSender sender, Command cmd, String label, String[] args) {
        /*
        Purposely, no tab-complete suggestions are given.
        This command is intended as a feature described to users by the support / development team.
        Users may mess with the command with unintended effects if they use it without guidance.
         */
        return Collections.emptyList();
    }
}
