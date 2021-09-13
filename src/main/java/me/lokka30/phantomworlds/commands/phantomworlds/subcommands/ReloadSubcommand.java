package me.lokka30.phantomworlds.commands.phantomworlds.subcommands;

import me.lokka30.phantomworlds.PhantomWorlds;
import me.lokka30.phantomworlds.commands.ISubcommand;
import me.lokka30.microlib.messaging.MultiMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author lokka30
 * @since v2.0.0
 */
public class ReloadSubcommand implements ISubcommand {

    /*
    cmd: /pw reload
    arg:   -      0
    len:   0      1
     */

    /**
     * @author lokka30
     * @since v2.0.0
     */
    @Override
    public void parseCommand(@NotNull PhantomWorlds main, CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("phantomworlds.command.phantomworlds.reload")) {
            (new MultiMessage(
                    main.messages.getConfig().getStringList("common.no-permission"), Arrays.asList(
                    new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                    new MultiMessage.Placeholder("permission", "phantomworlds.command.phantomworlds.reload", false)
            ))).send(sender);
            return;
        }

        if (args.length != 1) {
            (new MultiMessage(
                    main.messages.getConfig().getStringList("command.phantomworlds.subcommands.reload.usage"), Arrays.asList(
                    new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                    new MultiMessage.Placeholder("label", label, false)
            ))).send(sender);
            return;
        }

        (new MultiMessage(
                main.messages.getConfig().getStringList("command.phantomworlds.subcommands.reload.reloading-files"), Collections.singletonList(
                new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"), true)
        ))).send(sender);

        main.loadFiles();

        (new MultiMessage(
                main.messages.getConfig().getStringList("command.phantomworlds.subcommands.reload.reloading-worlds"), Collections.singletonList(
                new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"), true)
        ))).send(sender);

        main.loadWorlds();

        (new MultiMessage(
                main.messages.getConfig().getStringList("command.phantomworlds.subcommands.reload.reload-complete"), Collections.singletonList(
                new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"), true)
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
