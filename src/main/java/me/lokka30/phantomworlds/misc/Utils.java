package me.lokka30.phantomworlds.misc;

import me.lokka30.microlib.MicroLogger;
import me.lokka30.phantomworlds.PhantomWorlds;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashSet;

public class Utils {

    public static final MicroLogger LOGGER = new MicroLogger("&b&lPhantomWorlds: &7");

    public static HashSet<String> getLoadedWorlds() {
        final HashSet<String> loadedWorlds = new HashSet<>();
        Bukkit.getWorlds().forEach(world -> loadedWorlds.add(world.toString()));
        return loadedWorlds;
    }

    public static void registerCommand(PhantomWorlds main, CommandExecutor clazz, String command) {
        if (main.getCommand(command) == null) {
            Utils.LOGGER.error("&3Commands: &7Unable to register command '&b/" + command + "&7' - PluginCommand is null.");
        } else {
            //noinspection ConstantConditions
            main.getCommand(command).setExecutor(clazz);
            Utils.LOGGER.info("&3Commands: &7Registered command '&b/" + command + "&7'.");
        }
    }

    public static void unloadWorld(PhantomWorlds main, World world) {
        final ArrayList<Player> players = (ArrayList<Player>) world.getPlayers();
        players.forEach(player -> player.kickPlayer(main.messages.getConfig().getString("ABC", "ABC"))); //todo change msg

        Bukkit.unloadWorld(world, true);
    }
}
