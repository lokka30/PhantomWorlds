package me.lokka30.phantomworlds.commands;

import me.lokka30.phantomworlds.PhantomWorlds;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public interface ISubcommand {

    void parseCommand(PhantomWorlds main, CommandSender sender, Command cmd, String label, String[] args);

    List<String> parseTabCompletion(PhantomWorlds main, CommandSender sender, Command cmd, String label, String[] args);

}
