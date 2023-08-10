package me.lokka30.phantomworlds.misc;

import me.lokka30.microlib.messaging.MessageUtils;
import me.lokka30.phantomworlds.PhantomWorlds;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * This class contains Utility methods which are public & static which are used by multiple classes.
 * If a method is only used by one class then it is advised to keep it in the class to avoid
 * bloating this class.
 *
 * @author lokka30
 * @since v2.0.0
 */
public class Utils {

    /**
     * This is used for tab completion where numbers are expected, for example, coordinates in the
     * setspawn subcommand.
     */
    public static final List<String> ZERO_THRU_NINE = Arrays.asList("0", "1", "2", "3", "4", "5",
        "6", "7", "8", "9");

    /**
     * This method returns a list of the names of worlds that are loaded on the server. Used in tab
     * completion, for example.
     *
     * @return set of world names
     * @author lokka30
     * @since v2.0.0
     */
    public static HashSet<String> getLoadedWorldsNameList() {
        final HashSet<String> loadedWorlds = new HashSet<>();
        Bukkit.getWorlds().forEach(world -> loadedWorlds.add(world.getName()));
        return loadedWorlds;
    }

    /**
     * Attempts to register specified command. Sends status to console as logs.
     *
     * @param main    PhantomWorlds main class
     * @param clazz   CommandExecutor to be registered
     * @param command Name of the command as stated in plugin.yml
     * @author lokka30
     * @since v2.0.0
     */
    public static void registerCommand(@NotNull PhantomWorlds main, @NotNull CommandExecutor clazz,
        @NotNull String command) {
        if(main.getCommand(command) == null) {
            main.getLogger().severe("Unable to register command '/" + command + "' - PluginCommand "
                + "is null. Was plugin.yml tampered with?");
        } else {
            //noinspection ConstantConditions
            main.getCommand(command).setExecutor(clazz);
            main.getLogger().info("Registered command '/" + command + "'.");
        }
    }

    /**
     * Tells the server to unload specified world so it can be deleted. Additionally: -> Kicks all
     * players from it before unloading. -> It does not transfer users to other worlds for security
     * purposes. This may be changed in the future.
     *
     * @param main  PhantomWorlds main class
     * @param world World to be unloaded
     * @author lokka30
     * @since v2.0.0
     */
    public static void unloadWorld(@NotNull PhantomWorlds main, @NotNull World world) {
        // inform console
        main.getLogger().info(String.format(
            "Unloading world %s; kicking %s players from the world...",
            world.getName(),
            world.getPlayers().size()
        ));

        // kick players in world
        // using an iterator to avoid a possible ConcurrentModificationException
        world.getPlayers().iterator().forEachRemaining(player ->
            // yikes, this gets messy. :P
            player.kickPlayer(MessageUtils.colorizeAll(
                String.join("\n",
                        main.messages.getConfig()
                            .getStringList("command.phantomworlds.subcommands.unload.kick")
                    )
                    .replace("%prefix%",
                        main.messages.getConfig()
                            .getString("common.prefix", "PhantomWorlds: "))
                    .replace("%world%", world.getName())
            ))
        );

        // time to unload the world
        Bukkit.unloadWorld(world, true);
    }

    /**
     * For the CommandSender specified, this method will list every player that the tab list will
     * show them. This does not work with vanish plugins that **exclusively** use packets, as it
     * relies on Bukkit's 'hidePlayer' system.
     *
     * @param sender commandsender to check. if console, all players are visible.
     * @return list of usernames
     * @author lokka30
     * @since v2.0.0
     */
    public static List<String> getPlayersCanSeeList(@NotNull CommandSender sender) {
        List<String> suggestions = new ArrayList<>();

        if(!sender.hasPermission("phantomworlds.knows-vanished-users")
            && sender instanceof Player) {
            Player player = (Player) sender;
            for(Player listedPlayer : Bukkit.getOnlinePlayers()) {
                if(player.canSee(listedPlayer)) {
                    suggestions.add(listedPlayer.getName());
                }
            }
        } else {
            for(Player listedPlayer : Bukkit.getOnlinePlayers()) {
                suggestions.add(listedPlayer.getName());
            }
        }

        return suggestions;
    }

    /**
     * @param values Enum#values() call
     * @return a list of string conversions of each enum value
     * @author lokka30
     * @since v2.0.0
     */
    public static List<String> enumValuesToStringList(Object[] values) {
        List<String> strings = new ArrayList<>();
        for(Object value : values) {
            strings.add(value.toString());
        }
        return strings;
    }

    /**
     * Credit: <a href="https://stackoverflow.com/q/11701399">StackOverflow</a>
     *
     * @param val value to round
     * @return val, rounded to 2 decimal places.
     * @author Aayush Mahajan, ppeterka
     */
    public static double roundTwoDecimalPlaces(double val) {
        return Math.round(val * 100) / 100.0;
    }
}
