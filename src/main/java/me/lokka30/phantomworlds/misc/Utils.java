package me.lokka30.phantomworlds.misc;

import me.lokka30.microlib.MicroLogger;
import me.lokka30.phantomworlds.PhantomWorlds;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Utils {

    public static final MicroLogger LOGGER = new MicroLogger("&b&lPhantomWorlds: &7");

    public static HashSet<String> getLoadedWorlds() {
        final HashSet<String> loadedWorlds = new HashSet<>();
        Bukkit.getWorlds().forEach(world -> loadedWorlds.add(world.getName()));
        return loadedWorlds;
    }

    public static void registerCommand(@NotNull PhantomWorlds main, @NotNull CommandExecutor clazz, @NotNull String command) {
        if (main.getCommand(command) == null) {
            Utils.LOGGER.error("&3Commands: &7Unable to register command '&b/" + command + "&7' - PluginCommand is null.");
        } else {
            //noinspection ConstantConditions
            main.getCommand(command).setExecutor(clazz);
            Utils.LOGGER.info("&3Commands: &7Registered command '&b/" + command + "&7'.");
        }
    }

    public static void unloadWorld(@NotNull PhantomWorlds main, @NotNull World world) {
        final ArrayList<Player> players = (ArrayList<Player>) world.getPlayers();
        players.forEach(player -> player.kickPlayer(main.messages.getConfig().getString("ABC", "ABC"))); //todo change msg

        Bukkit.unloadWorld(world, true);
    }

    /**
     * For the CommandSender specified, this method will list every player
     * that the tab list will show them. This does not work with vanish plugins
     * that **exclusively** use packets, as it relies on Bukkit's 'hidePlayer'
     * system.
     *
     * @param sender commandsender to check. if console, all players are visible.
     * @return list of usernames
     */
    public static List<String> getPlayersCanSeeList(@NotNull CommandSender sender) {
        List<String> suggestions = new ArrayList<>();

        if (!sender.hasPermission("phantomworlds.vanishbypass") && sender instanceof Player) {
            Player player = (Player) sender;
            for (Player listedPlayer : Bukkit.getOnlinePlayers()) {
                if (player.canSee(listedPlayer)) {
                    suggestions.add(listedPlayer.getName());
                }
            }
        } else {
            for (Player listedPlayer : Bukkit.getOnlinePlayers()) {
                suggestions.add(listedPlayer.getName());
            }
        }

        return suggestions;
    }
}
