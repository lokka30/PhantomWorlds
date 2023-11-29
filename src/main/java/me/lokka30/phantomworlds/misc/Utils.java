package me.lokka30.phantomworlds.misc;

import me.lokka30.microlib.messaging.MessageUtils;
import me.lokka30.microlib.messaging.MultiMessage;
import me.lokka30.phantomworlds.PhantomWorlds;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
   *
   * @since v2.0.0
   */
  public static HashSet<String> getLoadedWorldsNameList() {
    final HashSet<String> loadedWorlds = new HashSet<>();
    Bukkit.getWorlds().forEach(world->loadedWorlds.add(world.getName()));
    return loadedWorlds;
  }

  /**
   * Attempts to register specified command. Sends status to console as logs.
   *
   * @param clazz CommandExecutor to be registered
   * @param command Name of the command as stated in plugin.yml
   *
   * @since v2.0.0
   */
  public static void registerCommand(@NotNull final CommandExecutor clazz, @NotNull final String command) {
    if(PhantomWorlds.instance().getCommand(command) == null) {
      PhantomWorlds.logger().severe("Unable to register command '/" + command + "' - PluginCommand "
              + "is null. Was plugin.yml tampered with?");
    } else {
      //noinspection ConstantConditions
      PhantomWorlds.instance().getCommand(command).setExecutor(clazz);
      PhantomWorlds.logger().info("Registered command '/" + command + "'.");
    }
  }

  /**
   * Tells the server to unload specified world so it can be deleted. Additionally: -> Kicks all
   * players from it before unloading. -> It does not transfer users to other worlds for security
   * purposes. This may be changed in the future.
   *
   * @param world World to be unloaded
   *
   * @since v2.0.0
   */
  public static void unloadWorld(@NotNull final World world) {
    // inform console
    PhantomWorlds.logger().info(String.format(
            "Unloading world %s; kicking %s players from the world...",
            world.getName(),
            world.getPlayers().size()
    ));

    // kick players in world
    // using an iterator to avoid a possible ConcurrentModificationException
    world.getPlayers().iterator().forEachRemaining(player->
            // yikes, this gets messy. :P
            player.kickPlayer(MessageUtils.colorizeAll(
                    String.join("\n",
                                    PhantomWorlds.instance().messages.getConfig()
                                            .getStringList("command.phantomworlds.subcommands.unload.kick")
                            )
                            .replace("%prefix%",
                                    PhantomWorlds.instance().messages.getConfig()
                                            .getString("common.prefix", "PhantomWorlds: "))
                            .replace("%world%", world.getName())
            ))
    );

    // time to unload the world
    Bukkit.unloadWorld(world, true);
  }

  /**
   * For the CommandSender specified, this method will list every player that the tab list will show
   * them. This does not work with vanish plugins that **exclusively** use packets, as it relies on
   * Bukkit's 'hidePlayer' system.
   *
   * @param sender commandsender to check. if console, all players are visible.
   *
   * @return list of usernames
   *
   * @since v2.0.0
   */
  public static List<String> getPlayersCanSeeList(@NotNull final CommandSender sender) {
    final List<String> suggestions = new ArrayList<>();

    if(!sender.hasPermission("phantomworlds.knows-vanished-users")
            && sender instanceof Player) {
      final Player player = (Player)sender;
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
   *
   * @return a list of string conversions of each enum value
   *
   * @since v2.0.0
   */
  public static List<String> enumValuesToStringList(final Object[] values) {
    final List<String> strings = new ArrayList<>();
    for(Object value : values) {
      strings.add(value.toString());
    }
    return strings;
  }

  /**
   * Credit: <a href="https://stackoverflow.com/q/11701399">StackOverflow</a>
   *
   * @param val value to round
   *
   * @return val, rounded to 2 decimal places.
   */
  public static double roundTwoDecimalPlaces(final double val) {
    return Math.round(val * 100) / 100.0;
  }

  public static Optional<Boolean> parseFromString(CommandSender sender, final StringBuilder value, final String option) {
    switch(value.toString().toLowerCase(Locale.ROOT)) {
      case "false":
      case "f":
      case "no":
      case "n":
        return Optional.of(false);
      case "true":
      case "t":
      case "yes":
      case "y":
        return Optional.of(true);
      default:
        (new MultiMessage(
                PhantomWorlds.instance().messages.getConfig().getStringList(
                        "command.phantomworlds.subcommands.create.options.invalid-value"),
                Arrays.asList(
                        new MultiMessage.Placeholder("prefix",
                                PhantomWorlds.instance().messages.getConfig().getString("common.prefix",
                                        "&b&lPhantomWorlds: &7"), true),
                        new MultiMessage.Placeholder("value", value.toString(),
                                false),
                        new MultiMessage.Placeholder("option", option, false),
                        new MultiMessage.Placeholder("expected",
                                "Boolean (true/false)", false)
                ))).send(sender);
        return Optional.empty();
    }
  }

  public static void zipFolder(File sourceFolder, String destinationZipFile) throws IOException {
    try (
            FileOutputStream fos = new FileOutputStream(destinationZipFile);
            ZipOutputStream zos = new ZipOutputStream(fos)
    ) {
      zipFolder(sourceFolder, sourceFolder.getName(), zos);
    }
  }

  public static void zipFolder(final File folder, final String parentFolder, final ZipOutputStream zos) throws IOException {

    if(folder == null || folder.exists()) {
      return;
    }

    final File[] files = folder.listFiles();
    if(files == null) {
      return;
    }

    for (File file : files) {
      if (file.isDirectory()) {
        zipFolder(file, parentFolder + File.separator + file.getName(), zos);
        continue;
      }

      final ZipEntry zipEntry = new ZipEntry(parentFolder + File.separator + file.getName());
      zos.putNextEntry(zipEntry);

      try (FileInputStream fis = new FileInputStream(file)) {
        final byte[] buffer = new byte[1024];
        int length;
        while ((length = fis.read(buffer)) > 0) {
          zos.write(buffer, 0, length);
        }
      }
      zos.closeEntry();
    }
  }

  public static boolean deleteFolder(File folder) {
    if (folder.exists()) {
      final File[] files = folder.listFiles();
      if (files != null) {
        for (File file : files) {
          if (file.isDirectory()) {
            deleteFolder(file);
          } else {
            file.delete();
          }
        }
      }
    }
    return folder.delete();
  }

  public static void teleportToWorld(@NotNull CommandSender sender, @NotNull String subCommand,
                                     @NotNull String label, @Nullable String targetPlayerName,
                                     @Nullable String worldName) {
    Player targetPlayer;
    if(targetPlayerName != null) {
      targetPlayer = Bukkit.getPlayer(targetPlayerName);

      // If the target is offline or invisible to the sender, then stop
      if(targetPlayer == null || !Utils.getPlayersCanSeeList(sender)
              .contains(targetPlayer.getName())) {
        (new MultiMessage(
                PhantomWorlds.instance().messages.getConfig()
                        .getStringList("command.phantomworlds.subcommands.common.player-offline"),
                Arrays.asList(
                        new MultiMessage.Placeholder("prefix", PhantomWorlds.instance().messages.getConfig()
                                .getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                        new MultiMessage.Placeholder("player", targetPlayerName, false)
                ))).send(sender);
        return;
      }
    } else {
      if(sender instanceof Player) {
        targetPlayer = (Player)sender;
      } else {
        (new MultiMessage(
                PhantomWorlds.instance().messages.getConfig().getStringList(
                        "command.phantomworlds.subcommands." + subCommand + ".usage-console"),
                Arrays.asList(
                        new MultiMessage.Placeholder("prefix", PhantomWorlds.instance().messages.getConfig()
                                .getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                        new MultiMessage.Placeholder("label", label, false)
                ))).send(sender);
        return;
      }
    }

    if(worldName == null) {
      worldName = targetPlayer.getWorld().getName();
    }

    final World world = Bukkit.getWorld(worldName);
    if(world == null) {
      (new MultiMessage(
              PhantomWorlds.instance().messages.getConfig()
                      .getStringList("command.phantomworlds.subcommands.common.invalid-world"),
              Arrays.asList(
                      new MultiMessage.Placeholder("prefix", PhantomWorlds.instance().messages.getConfig()
                              .getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                      new MultiMessage.Placeholder("world", worldName, false)
              ))).send(sender);
      return;
    }
    targetPlayer.teleport(parseSpawn(world));

    (new MultiMessage(
            PhantomWorlds.instance().messages.getConfig()
                    .getStringList("command.phantomworlds.subcommands." + subCommand + ".success"),
            Arrays.asList(
                    new MultiMessage.Placeholder("prefix",
                            PhantomWorlds.instance().messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"),
                            true),
                    new MultiMessage.Placeholder("player", targetPlayer.getName(), false),
                    new MultiMessage.Placeholder("world", worldName, false)
            ))).send(sender);
  }

  public static Location parseSpawn(final World world) {
    final String cfgPath = "worlds-to-load." + world.getName() + ".spawn";
    if(PhantomWorlds.instance().data.getConfig().contains(cfgPath)) {
      final double x = PhantomWorlds.instance().data.getConfig().getDouble(cfgPath + ".x", world.getSpawnLocation().getX());
      final double y = PhantomWorlds.instance().data.getConfig().getDouble(cfgPath + ".y", world.getSpawnLocation().getY());
      final double z = PhantomWorlds.instance().data.getConfig().getDouble(cfgPath + ".z", world.getSpawnLocation().getZ());
      final float yaw = (float)PhantomWorlds.instance().data.getConfig().getDouble(cfgPath + ".yaw", world.getSpawnLocation().getYaw());
      final float pitch = (float)PhantomWorlds.instance().data.getConfig().getDouble(cfgPath + ".pitch", world.getSpawnLocation().getPitch());

      return new Location(world, x, y, z, yaw, pitch);
    }
    return world.getSpawnLocation();
  }
}