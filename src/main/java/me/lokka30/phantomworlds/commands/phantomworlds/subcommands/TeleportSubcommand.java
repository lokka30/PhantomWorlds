package me.lokka30.phantomworlds.commands.phantomworlds.subcommands;

import me.lokka30.microlib.messaging.MultiMessage;
import me.lokka30.phantomworlds.PhantomWorlds;
import me.lokka30.phantomworlds.commands.ISubcommand;
import me.lokka30.phantomworlds.misc.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * @author lokka30, madison-allen
 * @since v2.0.0
 */
public class TeleportSubcommand implements ISubcommand {

    /*
    cmd: /pw teleport <world> [player]
    arg:   -        0       1        2
    len:   0        1       2        3
     */
	
	/*
    cmd: /pw spawn [player]
    arg:   -     0       1
    len:   0     1       2
     */

    /**
     * @author lokka30, madison-allen
     * @since v2.0.0
     */
    @Override
    public void parseCommand(@NotNull PhantomWorlds main, CommandSender sender, Command cmd, String label, String[] args) {
    	String subCommandLabel = args[0].toLowerCase(Locale.ROOT);
    	if(subCommandLabel.equals("tp")) subCommandLabel = "teleport";
    	
    	if (!sender.hasPermission("phantomworlds.command.phantomworlds." + subCommandLabel)) {
            (new MultiMessage(
                    main.messages.getConfig().getStringList("common.no-permission"), Arrays.asList(
                    new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                    new MultiMessage.Placeholder("permission", "phantomworlds.command.phantomworlds." + subCommandLabel, false)
            ))).send(sender);
            return;
        }

    	String targetPlayerName = null;
    	switch(subCommandLabel) {
    		case "teleport":
                if(!(args.length == 2 || args.length == 3)) {
                    (new MultiMessage(
                            main.messages.getConfig().getStringList("command.phantomworlds.subcommands." + subCommandLabel + ".usage"), Arrays.asList(
                            new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                            new MultiMessage.Placeholder("label", label, false)
                    ))).send(sender);
                    return;
                }

    		    if(args.length == 3) targetPlayerName = args[2];

    			teleportToWorld(main, sender, subCommandLabel, label, targetPlayerName, args[1]);

    			break;
    		case "spawn":
                if(!(args.length == 1 || args.length == 2)) {
                    (new MultiMessage(
                            main.messages.getConfig().getStringList("command.phantomworlds.subcommands." + subCommandLabel + ".usage"), Arrays.asList(
                            new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                            new MultiMessage.Placeholder("label", label, false)
                    ))).send(sender);
                    return;
                }

    			if(args.length == 2) {
                    targetPlayerName = args[1];
    			} else {
                    targetPlayerName = sender.getName();
    			}

                teleportToWorld(main, sender, subCommandLabel, label, targetPlayerName, null);

    			break;
    		default:
    			throw new IllegalStateException("Unexpected value: " + subCommandLabel);
    	}
    }

    /**
     * @author lokka30, madison-allen
     * @since v2.0.0
     */
    @Override
    public List<String> parseTabCompletion(PhantomWorlds main, CommandSender sender, Command cmd, String label, String[] args) {
    	String subCommand = args[0].toLowerCase(Locale.ROOT);
    	if(subCommand.equals("tp")) subCommand = "teleport";
    	
    	if (!sender.hasPermission("phantomworlds.command.phantomworlds." + subCommand)) return Collections.emptyList();

        switch(subCommand) {
			case "teleport":
				switch(args.length) {
					case 2:
						return new ArrayList<>(Utils.getLoadedWorldsNameList());
					case 3:
						return Utils.getPlayersCanSeeList(sender);
					default:
                        return Collections.emptyList();
				}
			case "spawn":
				if(args.length == 2) {
					return Utils.getPlayersCanSeeList(sender);
				} else {
                    return Collections.emptyList();
				}
			default:
                return Collections.emptyList();
        }
    }


    private void teleportToWorld(@NotNull PhantomWorlds main, @NotNull CommandSender sender, @NotNull String subCommand, @NotNull String label, @Nullable String targetPlayerName, @Nullable String worldName) {
        Player targetPlayer;
        if (targetPlayerName != null) {
            targetPlayer = Bukkit.getPlayer(targetPlayerName);

            // If the target is offline or invisible to the sender, then stop
            if (targetPlayer == null || !Utils.getPlayersCanSeeList(sender).contains(targetPlayer.getName())) {
                (new MultiMessage(
                        main.messages.getConfig().getStringList("command.phantomworlds.subcommands.common.player-offline"), Arrays.asList(
                        new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                        new MultiMessage.Placeholder("player", targetPlayerName, false)
                ))).send(sender);
                return;
            }
        } else {
            if (sender instanceof Player) {
                targetPlayer = (Player) sender;
            } else {
                (new MultiMessage(
                        main.messages.getConfig().getStringList("command.phantomworlds.subcommands." + subCommand + ".usage-console"), Arrays.asList(
                        new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                        new MultiMessage.Placeholder("label", label, false)
                ))).send(sender);
                return;
            }
        }

        if(worldName == null) {
            worldName = targetPlayer.getWorld().getName();
        }

        if (Bukkit.getWorld(worldName) == null) {
             (new MultiMessage(
                     main.messages.getConfig().getStringList("command.phantomworlds.subcommands.common.invalid-world"), Arrays.asList(
                     new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                     new MultiMessage.Placeholder("world", worldName, false)
             ))).send(sender);
             return;
         }

         //noinspection ConstantConditions
         targetPlayer.teleport(Bukkit.getWorld(worldName).getSpawnLocation());

         (new MultiMessage(
                 main.messages.getConfig().getStringList("command.phantomworlds.subcommands." + subCommand + ".success"), Arrays.asList(
                 new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                 new MultiMessage.Placeholder("player", targetPlayer.getName(), false),
                 new MultiMessage.Placeholder("world", worldName, false)
         ))).send(sender);
    }
}
