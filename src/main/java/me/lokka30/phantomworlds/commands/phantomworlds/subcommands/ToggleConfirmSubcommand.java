package me.lokka30.phantomworlds.commands.phantomworlds.subcommands;

import me.lokka30.phantomworlds.PhantomWorlds;
import me.lokka30.phantomworlds.commands.ISubcommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ToggleConfirmSubcommand implements ISubcommand {

    /*
    TODO
    - Command
    - Tab completion
    - Test
    - Messages.yml
    - Permissions.yml
    - Test
     */

    /*
    Syntax:
    /pw toggleconfirm [on/off] [player]
     */

    @Override
    public void parseCommand(@NotNull PhantomWorlds main, CommandSender sender, Command cmd, String label, String[] args) {
        sender.sendMessage("Work in progress.");
    }

    @Override
    public List<String> parseTabCompletion(PhantomWorlds main, CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("phantomworlds.command.phantomworlds.toggleconfirm")) {
            return new ArrayList<>();
        }

        //TODO
        return null;
    }
}
