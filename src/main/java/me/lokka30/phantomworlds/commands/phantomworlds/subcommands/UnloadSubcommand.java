package me.lokka30.phantomworlds.commands.phantomworlds.subcommands;

import me.lokka30.phantomworlds.PhantomWorlds;
import me.lokka30.phantomworlds.commands.ISubcommand;
import me.lokka30.phantomworlds.misc.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lokka30
 * @since v2.0.0
 */
public class UnloadSubcommand implements ISubcommand {

    /*
    TODO
     - Messages.yml
     - Test
     */

    /*
    cmd: /pw unload <world>
    arg:   -      0       1
    len:   0      1       2
     */

    /**
     * @author lokka30
     * @since v2.0.0
     */
    @Override
    public void parseCommand(@NotNull PhantomWorlds main, CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("phantomworlds.command.phantomworlds.unload")) {
            sender.sendMessage("No permission.");
            return;
        }

        if (args.length != 2) {
            sender.sendMessage("Usage: /" + label + " unload <world>");
            return;
        }

        if (Bukkit.getWorld(args[1]) == null) {
            sender.sendMessage("World '" + args[1] + "' is not loaded.");
            return;
        }

        if (sender instanceof Player) {
            //noinspection ConstantConditions
            if (Bukkit.getWorld(args[1]).getPlayers().contains((Player) sender)) {
                sender.sendMessage("You can't unload a world that you are currently in.");
                return;
            }
        }

        //noinspection ConstantConditions
        Utils.unloadWorld(main, Bukkit.getWorld(args[1]));

        sender.sendMessage("World '" + args[1] + "' has been unloaded. If the world's folder is still present next restart/reload, PhantomWorlds will load it again.");
    }

    /**
     * @author lokka30
     * @since v2.0.0
     */
    @Override
    public List<String> parseTabCompletion(PhantomWorlds main, CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("phantomworlds.command.phantomworlds.unload")) {
            return new ArrayList<>();
        }

        if (args.length == 2) {
            return new ArrayList<>(Utils.getLoadedWorldsNameList());
        } else {
            return new ArrayList<>();
        }
    }
}
