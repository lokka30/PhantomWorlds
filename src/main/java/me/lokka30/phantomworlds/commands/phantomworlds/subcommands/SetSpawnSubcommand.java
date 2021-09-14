package me.lokka30.phantomworlds.commands.phantomworlds.subcommands;

import me.lokka30.microlib.messaging.MessageUtils;
import me.lokka30.phantomworlds.PhantomWorlds;
import me.lokka30.phantomworlds.commands.ISubcommand;
import me.lokka30.microlib.messaging.MultiMessage;
import me.lokka30.phantomworlds.misc.Utils;
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

        if (args.length > 7 || (args.length > 1 && args.length < 4)) {
            (new MultiMessage(
                    main.messages.getConfig().getStringList("command.phantomworlds.subcommands.setspawn.usage"), Arrays.asList(
                    new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                    new MultiMessage.Placeholder("label", label, false)
            ))).send(sender);
            return;
        }

        // console must specify x, y, z, and world
        if (!(sender instanceof Player) && args.length < 5) {
            (new MultiMessage(
                    main.messages.getConfig().getStringList("command.phantomworlds.subcommands.setspawn.usage-console"), Arrays.asList(
                    new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                    new MultiMessage.Placeholder("label", label, false)
            ))).send(sender);
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
            (new MultiMessage(
                    main.messages.getConfig().getStringList("command.phantomworlds.subcommands.common.invalid-world"), Arrays.asList(
                    new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                    new MultiMessage.Placeholder("world", args[1], false)
            ))).send(sender);
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
                    (new MultiMessage(
                            main.messages.getConfig().getStringList("command.phantomworlds.subcommands.setspawn.invalid-number"), Arrays.asList(
                            new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                            new MultiMessage.Placeholder("arg", args[i], false)
                    ))).send(sender);
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
                        sender.sendMessage(MessageUtils.colorizeAll("&b&lPhantomWorlds: &7Encountered an issue whilst retrieving coordinate args. Please report this to the author."));
                        throw new IllegalArgumentException("Unexpected value: " + i);
                }
            }
        } else {
            // Impossible for non-player to access this condition. No need to check sender=player
            x = Utils.roundTwoDecimalPlaces(((Player) sender).getLocation().getX());
            y = Utils.roundTwoDecimalPlaces(((Player) sender).getLocation().getY());
            z = Utils.roundTwoDecimalPlaces(((Player) sender).getLocation().getZ());
        }

        // yaw
        if (args.length >= 6) {
            try {
                yaw = Float.parseFloat(args[5]);
            } catch (NumberFormatException ex) {
                (new MultiMessage(
                        main.messages.getConfig().getStringList("command.phantomworlds.subcommands.setspawn.invalid-number"), Arrays.asList(
                        new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                        new MultiMessage.Placeholder("arg", args[5], false)
                ))).send(sender);
                return;
            }
        } else {
            if (sender instanceof Player) {
                yaw = (float) Utils.roundTwoDecimalPlaces(((Player) sender).getLocation().getYaw());
            } else {
                yaw = 0;
            }
        }

        // pitch
        if (args.length == 7) {
            try {
                pitch = Float.parseFloat(args[6]);
            } catch (NumberFormatException ex) {
                (new MultiMessage(
                        main.messages.getConfig().getStringList("command.phantomworlds.subcommands.setspawn.invalid-number"), Arrays.asList(
                        new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                        new MultiMessage.Placeholder("arg", args[6], false)
                ))).send(sender);
                return;
            }
        } else {
            if (sender instanceof Player) {
                pitch = (float) Utils.roundTwoDecimalPlaces(((Player) sender).getLocation().getPitch());
            } else {
                pitch = 0;
            }
        }

        final Location location = new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
        //noinspection ConstantConditions //This stop IntelliJ from complaining about the world being possibly null which we have already verified isn't the case.
        location.getWorld().setSpawnLocation(location);

        (new MultiMessage(
                main.messages.getConfig().getStringList("command.phantomworlds.subcommands.setspawn.success"), Arrays.asList(
                new MultiMessage.Placeholder("prefix", main.messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                new MultiMessage.Placeholder("world", worldName, false),
                new MultiMessage.Placeholder("x", x + "", false),
                new MultiMessage.Placeholder("y", y + "", false),
                new MultiMessage.Placeholder("z", z + "", false),
                new MultiMessage.Placeholder("yaw", yaw + "", false),
                new MultiMessage.Placeholder("pitch", pitch + "", false)
        ))).send(sender);
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

        switch (args.length) {
            case 2:
            case 3:
            case 4:
            case 6:
            case 7:
                return Utils.ONE_TO_NINE;
            case 5:
                return new ArrayList<>(Utils.getLoadedWorldsNameList());
            default:
                return new ArrayList<>();
        }
    }
}
