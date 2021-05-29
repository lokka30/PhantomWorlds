package me.lokka30.phantomworlds.commands.phantomworlds;

import me.lokka30.phantomworlds.PhantomWorlds;
import me.lokka30.phantomworlds.commands.phantomworlds.subcommands.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PhantomWorldsCommand implements TabExecutor {

    private final PhantomWorlds main;

    public PhantomWorldsCommand(PhantomWorlds main) {
        this.main = main;
    }

    final CreateSubcommand createSubcommand = new CreateSubcommand();
    final UnloadSubcommand unloadSubcommand = new UnloadSubcommand();
    final TeleportSubcommand teleportSubcommand = new TeleportSubcommand();
    final ListSubcommand listSubcommand = new ListSubcommand();
    final SpawnSubcommand spawnSubcommand = new SpawnSubcommand();
    final SetSpawnSubcommand setSpawnSubcommand = new SetSpawnSubcommand();
    final InfoSubcommand infoSubcommand = new InfoSubcommand();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case "create":
                    createSubcommand.parseCommand(main, sender, cmd, label, args);
                    break;
                case "unload":
                    unloadSubcommand.parseCommand(main, sender, cmd, label, args);
                    break;
                case "teleport":
                case "tp":
                    teleportSubcommand.parseCommand(main, sender, cmd, label, args);
                    break;
                case "list":
                    listSubcommand.parseCommand(main, sender, cmd, label, args);
                    break;
                case "spawn":
                    spawnSubcommand.parseCommand(main, sender, cmd, label, args);
                    break;
                case "setspawn":
                    setSpawnSubcommand.parseCommand(main, sender, cmd, label, args);
                    break;
                case "info":
                    infoSubcommand.parseCommand(main, sender, cmd, label, args);
                    break;
                default:
                    sender.sendMessage("Invalid subcommand."); //TODO MSG
                    sendAvailableSubcommands(sender, label);
                    break;
            }
        }
        return true;
    }

    /**
     * Displays messages that list available subcommands for /phantomworlds
     *
     * @param label  label of the command (alias used).
     * @param sender commandsender of the command
     */
    void sendAvailableSubcommands(CommandSender sender, String label) {
        sender.sendMessage("/" + label + " create/info/list/setspawn/spawn/teleport/unload ..."); //TODO MSG
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (args.length == 1) {
            switch (args[0].toLowerCase(Locale.ROOT)) {
                case "create":
                    return createSubcommand.parseTabCompletion(main, sender, cmd, label, args);
                case "info":
                    return infoSubcommand.parseTabCompletion(main, sender, cmd, label, args);
                case "list":
                    return listSubcommand.parseTabCompletion(main, sender, cmd, label, args);
                case "setspawn":
                    return setSpawnSubcommand.parseTabCompletion(main, sender, cmd, label, args);
                case "spawn":
                    return spawnSubcommand.parseTabCompletion(main, sender, cmd, label, args);
                case "teleport":
                    return teleportSubcommand.parseTabCompletion(main, sender, cmd, label, args);
                case "unload":
                    return unloadSubcommand.parseTabCompletion(main, sender, cmd, label, args);
                default:
                    break;
            }
        }
        return new ArrayList<>(); //no suggestions available.
    }
}
