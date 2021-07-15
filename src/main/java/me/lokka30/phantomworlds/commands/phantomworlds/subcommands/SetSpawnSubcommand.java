package me.lokka30.phantomworlds.commands.phantomworlds.subcommands;

import me.lokka30.phantomworlds.PhantomWorlds;
import me.lokka30.phantomworlds.commands.ISubcommand;
import me.lokka30.phantomworlds.misc.MultiMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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
public class SetSpawnSubcommand implements ISubcommand {

    /*
    TODO
     - Messages.yml
     - Round coordinate decimals
     - Test
     - Tab Completion
     - Test
     */

    /*
    cmd: /pw setSpawn [x] [y] [z] [world] [yaw] [pitch]
    arg:   -        0   1   2   3       4     5       6
    len:   0        1   2   3   4       5     6       7
     */

    /**
     * @author lokka30
     * @since v2.0.0
     */
    @Override
    public void parseCommand(@NotNull PhantomWorlds main, CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("phantomworlds.command.phantomworlds.setspawn")) {
            (new MultiMessage(
                    main.messages.getConfig().getStringList("common.no-permission"), Arrays.asList(
                    new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                    new MultiMessage.Placeholder("permission", "phantomworlds.command.phantomworlds.setspawn", false)
            ))).send(sender);
            return;
        }

        if (args.length > 7) {
            (new MultiMessage(
                    main.messages.getConfig().getStringList("command.phantomworlds.subcommands.setspawn.usage"), Arrays.asList(
                    new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                    new MultiMessage.Placeholder("label", label, false)
            ))).send(sender);
            return;
        }

        // console must specify x, y, z, and world
        if (!(sender instanceof Player) && args.length < 5) {
            sender.sendMessage("Invalid usage for console. Try /pw setspawn <x> <y> <z> <world> [yaw] [pitch]");
            return;
        }

        // Determine world name.
        String worldName;
        if (args.length >= 5) {
            worldName = args[4];
        } else {
            // Impossible for non-player to access this condition. No need to check sender=player
            worldName = ((Player) sender).getWorld().getName();
        }
        if (Bukkit.getWorld(worldName) == null) {
            sender.sendMessage("World '" + worldName + "' is not loaded.");
            return;
        }

        double x = 0, y = 0, z = 0; // these are (I hope) redundant initializers, just there to stop IntellIJ from complaining
        float yaw, pitch;

        // Determine x, y, z.
        if (args.length >= 4) {
            // Go through args[1], [2], and [3], for x, y, and z.
            for (int i = 1; i <= 3; i++) {
                double val;

                try {
                    val = Double.parseDouble(args[i]);
                } catch (NumberFormatException ex) {
                    sender.sendMessage("'" + args[i] + "' is not a valid number.");
                    return;
                }

                switch (i) {
                    case 1:
                        x = val;
                        break;
                    case 2:
                        y = val;
                        break;
                    case 3:
                        z = val;
                        break;
                    default:
                        throw new IllegalArgumentException("Unexpected value: " + i);
                }
            }
        } else {
            // Impossible for non-player to access this condition. No need to check sender=player
            x = ((Player) sender).getLocation().getX();
            y = ((Player) sender).getLocation().getY();
            z = ((Player) sender).getLocation().getZ();
        }

        // yaw
        if (args.length >= 6) {
            try {
                yaw = Float.parseFloat(args[5]);
            } catch (NumberFormatException ex) {
                sender.sendMessage("'" + args[5] + "' is not a valid number.");
                return;
            }
        } else {
            if (sender instanceof Player) {
                yaw = ((Player) sender).getLocation().getYaw();
            } else {
                yaw = 0;
            }
        }

        // pitch
        if (args.length == 7) {
            try {
                pitch = Float.parseFloat(args[6]);
            } catch (NumberFormatException ex) {
                sender.sendMessage("'" + args[6] + "' is not a valid number.");
                return;
            }
        } else {
            if (sender instanceof Player) {
                pitch = ((Player) sender).getLocation().getPitch();
            } else {
                pitch = 0;
            }
        }

        final Location location = new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
        //noinspection ConstantConditions //This stop IntelliJ from complaining about the world being possibly null which we have already verified isn't the case.
        location.getWorld().setSpawnLocation(location);

        sender.sendMessage("Spawn location set for world '" + worldName + "' at x=" + x + ", y=" + y + ", z=" + z + ", with yaw=" + yaw + " and pitch=" + pitch + ".");
    }

    /**
     * @author lokka30
     * @since v2.0.0
     */
    @Override
    public List<String> parseTabCompletion(PhantomWorlds main, CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("phantomworlds.command.phantomworlds.setspawn")) {
            return new ArrayList<>();
        }

        return null; //TODO
    }
}
