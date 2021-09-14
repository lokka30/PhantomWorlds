package me.lokka30.phantomworlds.commands.phantomworlds.subcommands;

import me.lokka30.phantomworlds.PhantomWorlds;
import me.lokka30.phantomworlds.commands.ISubcommand;
import me.lokka30.phantomworlds.misc.CompatibilityChecker;
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
public class CompatibilitySubcommand implements ISubcommand {

    /**
     * @author lokka30
     * @since v2.0.0
     */
    @Override
    public void parseCommand(@NotNull PhantomWorlds main, CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("phantomworlds.command.phantomworlds.compatibility")) {
            (new MultiMessage(
                    main.messages.getConfig().getStringList("common.no-permission"), Arrays.asList(
                    new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                    new MultiMessage.Placeholder("permission", "phantomworlds.command.phantomworlds.compatibility", false)
            ))).send(sender);
            return;
        }

        if (args.length != 1) {
            (new MultiMessage(
                    main.messages.getConfig().getStringList("command.phantomworlds.subcommands.compatibility.usage"), Arrays.asList(
                    new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                    new MultiMessage.Placeholder("label", label, false)
            ))).send(sender);
            return;
        }

        (new MultiMessage(
                main.messages.getConfig().getStringList("command.phantomworlds.subcommands.compatibility.start"), Collections.singletonList(
                new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"), true)
        ))).send(sender);

        main.compatibilityChecker.checkAll();

        if (main.compatibilityChecker.incompatibilities.isEmpty()) {
            (new MultiMessage(
                    main.messages.getConfig().getStringList("command.phantomworlds.subcommands.compatibility.found-none"), Collections.singletonList(
                    new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"), true)
            ))).send(sender);
            return;
        }

        (new MultiMessage(
                main.messages.getConfig().getStringList("command.phantomworlds.subcommands.compatibility.found"), Arrays.asList(
                new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                new MultiMessage.Placeholder("amount", main.compatibilityChecker.incompatibilities.size() + "", false)
        ))).send(sender);

        for (int i = 0; i < main.compatibilityChecker.incompatibilities.size(); i++) {
            CompatibilityChecker.Incompatibility incompatibility = main.compatibilityChecker.incompatibilities.get(i);

            (new MultiMessage(
                    main.messages.getConfig().getStringList("command.phantomworlds.subcommands.compatibility.entry"), Arrays.asList(
                    new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                    new MultiMessage.Placeholder("index", (i + 1) + "", false),
                    new MultiMessage.Placeholder("type", incompatibility.type.toString(), false),
                    new MultiMessage.Placeholder("reason", incompatibility.reason, true),
                    new MultiMessage.Placeholder("recommendation", incompatibility.recommendation, true)
            ))).send(sender);
        }
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
