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

import java.util.*;

/**
 * @author lokka30
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
     * @author lokka30
     * @since v2.0.0
     */
    @Override
    public void parseCommand(@NotNull PhantomWorlds main, CommandSender sender, Command cmd, String label, String[] args) {
    	String subCommand = args[0].toLowerCase(Locale.ROOT);
    	if(subCommand.equals("tp")) {
    		subCommand = "teleport";
    	}
    	
    	if (!sender.hasPermission("phantomworlds.command.phantomworlds." + subCommand)) {
            (new MultiMessage(
                    main.messages.getConfig().getStringList("common.no-permission"), Arrays.asList(
                    new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                    new MultiMessage.Placeholder("permission", "phantomworlds.command.phantomworlds." + subCommand, false)
            ))).send(sender);
            return;
        }

    	
    	if ((args.length < 2 || args.length > 3) && subCommand.equals("teleport")
    			|| (args.length < 1 || args.length > 2) && subCommand.equals("spawn")) {
            (new MultiMessage(
                    main.messages.getConfig().getStringList("command.phantomworlds.subcommands." + subCommand + ".usage"), Arrays.asList(
                    new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                    new MultiMessage.Placeholder("label", label, false)
            ))).send(sender);
            return;
        }
    	
    	switch(subCommand) {
    		case "teleport":
    			teleportToWorld(main, sender, subCommand, label, args[2], args[1]);
    			break;
    		case "spawn":
    			String world;
    			if(args.length != 2) {
    				world = Bukkit.getPlayer(sender.getName()).getWorld().toString();
    			} else {
    				world = Bukkit.getPlayer(args[1]).getWorld().toString();
    			}
    			teleportToWorld(main, sender, subCommand, label, args[1], world);
    			break;
    		default:
    			return;	
    	}
    }

    /**
     * @author lokka30
     * @since v2.0.0
     */
    @Override
    public List<String> parseTabCompletion(PhantomWorlds main, CommandSender sender, Command cmd, String label, String[] args) {
    	String subCommand = args[0].toLowerCase(Locale.ROOT);
    	
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
    
    private void teleportToWorld(PhantomWorlds main, CommandSender sender, String subCommand, String label, String targetPlayer, String world) {
         if (Bukkit.getWorld(world) == null) {
             (new MultiMessage(
                     main.messages.getConfig().getStringList("command.phantomworlds.subcommands.common.invalid-world"), Arrays.asList(
                     new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                     new MultiMessage.Placeholder("world", world, false)
             ))).send(sender);
             return;
         }

         Player target;
         if (targetPlayer != null) {
             target = Bukkit.getPlayer(targetPlayer);

             // If the target is offline or invisible to the sender, then stop
             if (target == null || !Utils.getPlayersCanSeeList(sender).contains(target.getName())) {
                 (new MultiMessage(
                         main.messages.getConfig().getStringList("command.phantomworlds.subcommands.common.player-offline"), Arrays.asList(
                         new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                         new MultiMessage.Placeholder("player", targetPlayer, false)
                 ))).send(sender);
                 return;
             }
         } else {
             if (sender instanceof Player) {
                 target = (Player) sender;
             } else {
                 (new MultiMessage(
                         main.messages.getConfig().getStringList("command.phantomworlds.subcommands." + subCommand + ".usage-console"), Arrays.asList(
                         new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                         new MultiMessage.Placeholder("label", label, false)
                 ))).send(sender);
                 return;
             }
         }

         //noinspection ConstantConditions
         target.teleport(Bukkit.getWorld(world).getSpawnLocation());

         (new MultiMessage(
                 main.messages.getConfig().getStringList("command.phantomworlds.subcommands." + subCommand + ".success"), Arrays.asList(
                 new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                 new MultiMessage.Placeholder("player", target.getName(), false),
                 new MultiMessage.Placeholder("world", world, false)
         ))).send(sender);
    }
}
