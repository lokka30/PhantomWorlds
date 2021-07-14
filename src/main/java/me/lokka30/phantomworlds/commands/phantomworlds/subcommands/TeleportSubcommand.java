package me.lokka30.phantomworlds.commands.phantomworlds.subcommands;

import me.lokka30.phantomworlds.PhantomWorlds;
import me.lokka30.phantomworlds.commands.ISubcommand;
import me.lokka30.phantomworlds.misc.MultiMessage;
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
     - Messages.yml
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
            sender.sendMessage("Usage: /" + label + " teleport <world> [player]");
            return;
        }

        if (Bukkit.getWorld(args[1]) == null) {
            sender.sendMessage("Invalid world '" + args[1] + "'.");
            return;
        }

        Player target;
        if (args.length == 3) {
            target = Bukkit.getPlayer(args[2]);

            // If the target is offline or invisible to the sender, then stop
            if (target == null || !Utils.getPlayersCanSeeList(sender).contains(target.getName())) {
                sender.sendMessage("'" + args[2] + "' is not online.");
                return;
            }
        } else {
            if (sender instanceof Player) {
                target = (Player) sender;
            } else {
                sender.sendMessage("Usage (console): /pw teleport <world> <player>");
                return;
            }
        }

        //noinspection ConstantConditions
        target.teleport(Bukkit.getWorld(args[1]).getSpawnLocation());

        sender.sendMessage("Teleported '" + target.getName() + "' to the spawn point of world '" + args[1] + "'.");
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
            case 1:
                return new ArrayList<>(Utils.getLoadedWorldsNameList());
            case 2:
                return Utils.getPlayersCanSeeList(sender);
            default:
                return new ArrayList<>();
        }
    }
}
