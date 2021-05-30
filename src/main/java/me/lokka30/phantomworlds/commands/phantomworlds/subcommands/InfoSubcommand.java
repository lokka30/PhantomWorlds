package me.lokka30.phantomworlds.commands.phantomworlds.subcommands;

import me.lokka30.phantomworlds.PhantomWorlds;
import me.lokka30.phantomworlds.commands.ISubcommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

//TODO
public class InfoSubcommand implements ISubcommand {

    @Override
    public void parseCommand(@NotNull PhantomWorlds main, CommandSender sender, Command cmd, String label, String[] args) {
        /*
        TODO
        Inform the commandsender of the version, authors, contributors,
        and supported server versions of this version of PhantomWorlds.
         */
        sender.sendMessage("Running PhantomWorlds v" + main.getDescription().getVersion());
        sender.sendMessage("Authors: " + String.join(", ", main.getDescription().getAuthors()));
        sender.sendMessage("Contributors: " + String.join(", ", main.contributors));
        sender.sendMessage("Supported server versions: " + main.supportedServerVersions);
    }

    @Override
    public List<String> parseTabCompletion(PhantomWorlds main, CommandSender sender, Command cmd, String label, String[] args) {
        /*
        TODO
         */
        return null;
    }
}
