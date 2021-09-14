package me.lokka30.phantomworlds.commands.phantomworlds.subcommands;

import me.lokka30.phantomworlds.PhantomWorlds;
import me.lokka30.phantomworlds.commands.ISubcommand;
import me.lokka30.microlib.messaging.MultiMessage;
import me.lokka30.phantomworlds.misc.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author lokka30
 * @since v2.0.0
 */
public class TeleportSubcommand implements ISubcommand {

    /*
    TODO
     - Test
     */


    /*
    cmd: /pw teleport <world> [player]
    arg:   -        0       1        2
    len:   0        1       2        3
     */

    /**
     * @author lokka30
     * @since v2.0.0
     */
    @Override
    public void parseCommand(@NotNull PhantomWorlds main, CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("phantomworlds.command.phantomworlds.teleport")) {
            (new MultiMessage(
                    main.messages.getConfig().getStringList("common.no-permission"), Arrays.asList(
                    new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                    new MultiMessage.Placeholder("permission", "phantomworlds.command.phantomworlds.teleport", false)
            ))).send(sender);
            return;
        }

        if (args.length < 2 || args.length > 3) {
            (new MultiMessage(
                    main.messages.getConfig().getStringList("command.phantomworlds.subcommands.teleport.usage"), Arrays.asList(
                    new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                    new MultiMessage.Placeholder("label", label, false)
            ))).send(sender);
            return;
        }

        if (Bukkit.getWorld(args[1]) == null) {
            (new MultiMessage(
                    main.messages.getConfig().getStringList("command.phantomworlds.subcommands.common.invalid-world"), Arrays.asList(
                    new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                    new MultiMessage.Placeholder("world", args[1], false)
            ))).send(sender);
            return;
        }

        Player target;
        if (args.length == 3) {
            target = Bukkit.getPlayer(args[2]);

            // If the target is offline or invisible to the sender, then stop
            if (target == null || !Utils.getPlayersCanSeeList(sender).contains(target.getName())) {
                (new MultiMessage(
                        main.messages.getConfig().getStringList("command.phantomworlds.subcommands.common.player-offline"), Arrays.asList(
                        new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                        new MultiMessage.Placeholder("player", args[2], false)
                ))).send(sender);
                return;
            }
        } else {
            if (sender instanceof Player) {
                target = (Player) sender;
            } else {
                (new MultiMessage(
                        main.messages.getConfig().getStringList("command.phantomworlds.subcommands.teleport.usage-console"), Arrays.asList(
                        new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                        new MultiMessage.Placeholder("label", label, false)
                ))).send(sender);
                return;
            }
        }

        //noinspection ConstantConditions
        target.teleport(Bukkit.getWorld(args[1]).getSpawnLocation());

        (new MultiMessage(
                main.messages.getConfig().getStringList("command.phantomworlds.subcommands.teleport.success"), Arrays.asList(
                new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                new MultiMessage.Placeholder("player", target.getName(), false),
                new MultiMessage.Placeholder("world", args[1], false)
        ))).send(sender);
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

        switch (args.length) {
            case 2:
                return new ArrayList<>(Utils.getLoadedWorldsNameList());
            case 3:
                return Utils.getPlayersCanSeeList(sender);
            default:
                return new ArrayList<>();
        }
    }
}
