package me.lokka30.phantomworlds.commands.phantomworlds.subcommands;

import me.lokka30.phantomworlds.PhantomWorlds;
import me.lokka30.phantomworlds.commands.ISubcommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ListSubcommand implements ISubcommand {

    /*
    TODO
    - Test
    - Messages.yml
    - Permissions.yml
    - Test
     */

    @Override
    public void parseCommand(@NotNull PhantomWorlds main, CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("phantomworlds.command.phantomworlds.list")) {
            sender.sendMessage("No permission");
            return;
        }

        sender.sendMessage("Worlds loaded (" + Bukkit.getWorlds().size() + "):");
        Bukkit.getWorlds().forEach(world -> sender.sendMessage(" -> " + world.getName()));
    }

    @Override
    public List<String> parseTabCompletion(PhantomWorlds main, CommandSender sender, Command cmd, String label, String[] args) {
        return new ArrayList<>();
    }
}
