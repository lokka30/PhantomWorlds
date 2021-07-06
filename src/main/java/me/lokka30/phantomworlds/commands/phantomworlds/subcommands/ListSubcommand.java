package me.lokka30.phantomworlds.commands.phantomworlds.subcommands;

import me.lokka30.phantomworlds.PhantomWorlds;
import me.lokka30.phantomworlds.commands.ISubcommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lokka30
 * @since v2.0.0
 */
public class ListSubcommand implements ISubcommand {

    /*
    TODO
     - Messages.yml
     - Test
     */

    /*
    cmd: /pw list
    arg:   -    0
    len:   0    1
     */

    /**
     * @author lokka30
     * @since v2.0.0
     */
    @Override
    public void parseCommand(@NotNull PhantomWorlds main, CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("phantomworlds.command.phantomworlds.list")) {
            sender.sendMessage("No permission");
            return;
        }

        if (args.length != 1) {
            sender.sendMessage("Usage: /" + label + " list");
            return;
        }

        sender.sendMessage("Worlds loaded (" + Bukkit.getWorlds().size() + "):");
        Bukkit.getWorlds().forEach(world -> sender.sendMessage(" -> " + world.getName()));
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
