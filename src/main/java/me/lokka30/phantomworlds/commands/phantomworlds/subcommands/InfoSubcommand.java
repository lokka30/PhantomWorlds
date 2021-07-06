package me.lokka30.phantomworlds.commands.phantomworlds.subcommands;

import me.lokka30.phantomworlds.PhantomWorlds;
import me.lokka30.phantomworlds.commands.ISubcommand;
import me.lokka30.phantomworlds.misc.MultiMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author lokka30
 * @since v2.0.0
 */
public class InfoSubcommand implements ISubcommand {

    /*
    cmd: /pw info
    arg:   -    0
    len:   0    1
     */

    /**
     * @author lokka30
     * @since v2.0.0
     */
    @Override
    public void parseCommand(@NotNull PhantomWorlds main, CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("phantomworlds.command.phantomworlds.info")) {
            (new MultiMessage(
                    main.messages.getConfig().getStringList("common.no-permission"), Arrays.asList(
                    new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                    new MultiMessage.Placeholder("permission", "phantomworlds.command.phantomworlds.info", false)
            ))).send(sender);
            return;
        }

        if (args.length != 1) {
            (new MultiMessage(
                    main.messages.getConfig().getStringList("command.phantomworlds.subcommands.info.usage"), Arrays.asList(
                    new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                    new MultiMessage.Placeholder("label", label, false)
            ))).send(sender);
            return;
        }

        (new MultiMessage(
                main.messages.getConfig().getStringList("command.phantomworlds.subcommands.info.success"), Arrays.asList(
                new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                new MultiMessage.Placeholder("version", main.getDescription().getVersion(), false),
                new MultiMessage.Placeholder("authors", String.join(main.messages.getConfig().getString("common.list-delimiter", "&7, &b"), main.getDescription().getAuthors()), false),
                new MultiMessage.Placeholder("contributors", String.join(main.messages.getConfig().getString("common.list-delimiter", "&7, &b"), main.contributors), false),
                new MultiMessage.Placeholder("supportedServerVersions", main.supportedServerVersions, false)
        ))).send(sender);
    }

    /**
     * @author lokka30
     * @since v2.0.0
     */
    @Override
    public List<String> parseTabCompletion(PhantomWorlds main, CommandSender sender, Command cmd, String label, String[] args) {
        return new ArrayList<>();
    }
}
