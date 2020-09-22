package me.lokka30.phantomworlds;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PhantomWorldsCommand implements TabExecutor {

    private final PhantomWorlds instance;
    private final String prefix;

    public PhantomWorldsCommand(PhantomWorlds instance) {
        this.instance = instance;
        this.prefix = instance.messagesCfg.getString("prefix");
    }

    private List<String> getWorldNamesList() {
        ArrayList<String> names = new ArrayList<>();
        for (World world : Bukkit.getWorlds()) {
            names.add(world.getName());
        }
        return names;
    }

    private List<String> getPlayersCanSeeList(CommandSender sender) {
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

    private String colorize(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            for (String message : instance.messagesCfg.getStringList("main.info")) {
                sender.sendMessage(colorize(message
                        .replace("%prefix%", prefix)
                        .replace("%label%", label)
                        .replace("%version%", instance.getDescription().getVersion())));
            }
        } else {
            switch (args[0].toLowerCase()) {
                case "create":
                    if (sender.hasPermission("phantomworlds.create")) {
                        if (args.length >= 3) {
                            if (instance.worldsMap.containsKey(args[1])) {
                                sender.sendMessage(colorize(Objects.requireNonNull(instance.messagesCfg.getString("create.world-already-exists"))
                                        .replace("%prefix%", prefix))
                                        .replace("%world%", args[1]));
                            } else {
                                World.Environment environment;
                                switch (args[2].toLowerCase()) {
                                    case "normal":
                                    case "default":
                                    case "regular":
                                        environment = World.Environment.NORMAL;
                                        break;
                                    case "nether":
                                    case "thenether":
                                    case "the-nether":
                                    case "the_nether":
                                    case "hell":
                                        environment = World.Environment.NETHER;
                                        break;
                                    case "end":
                                    case "theend":
                                    case "the-end":
                                    case "the_end":
                                        environment = World.Environment.THE_END;
                                        break;
                                    default:
                                        sender.sendMessage(colorize(Objects.requireNonNull(instance.messagesCfg.getString("create.invalid-environment"))
                                                .replace("%prefix%", prefix))
                                                .replace("%environment%", args[2]));
                                        return true;
                                }

                                long seed = -1;
                                String generatorId = null;
                                WorldType worldType = null;
                                boolean generateStructures = true;

                                if (args.length > 3) {
                                    for (int i = 3; i <= args.length; i++) {
                                        if (args[i].toLowerCase().startsWith("-s:")) {
                                            if (args[i].length() > 3) {
                                                try {
                                                    seed = Long.parseLong(args[i].substring(3));
                                                } catch (NumberFormatException exception) {
                                                    sender.sendMessage(colorize(Objects.requireNonNull(instance.messagesCfg.getString("create.invalid-setting-s"))
                                                            .replace("%prefix%", prefix)));
                                                    return true;
                                                }
                                            } else {
                                                sender.sendMessage(colorize(Objects.requireNonNull(instance.messagesCfg.getString("create.invalid-setting-s"))
                                                        .replace("%prefix%", prefix)));
                                                return true;
                                            }
                                        } else if (args[i].toLowerCase().startsWith("-g:")) {
                                            if (args[i].length() > 3) {
                                                generatorId = args[i].substring(3);
                                            } else {
                                                sender.sendMessage(colorize(Objects.requireNonNull(instance.messagesCfg.getString("create.invalid-setting-g"))
                                                        .replace("%prefix%", prefix)));
                                            }
                                        } else if (args[i].toLowerCase().startsWith("-t:")) {
                                            if (args[i].length() > 3) {
                                                String worldTypeStr = args[i].substring(3);
                                                switch (worldTypeStr.toUpperCase()) {
                                                    case "NORMAL":
                                                        worldType = WorldType.NORMAL;
                                                        break;
                                                    case "FLAT":
                                                    case "SUPERFLAT":
                                                    case "SUPER_FLAT":
                                                        worldType = WorldType.FLAT;
                                                        break;
                                                    case "LARGE_BIOMES":
                                                    case "LARGEBIOMES":
                                                        worldType = WorldType.LARGE_BIOMES;
                                                        break;
                                                    case "AMPLIFIED":
                                                        worldType = WorldType.AMPLIFIED;
                                                        break;
                                                    default:
                                                        sender.sendMessage(colorize(Objects.requireNonNull(instance.messagesCfg.getString("create.invalid-setting-t"))
                                                                .replace("%prefix%", prefix)));
                                                        return true;
                                                }
                                            } else {
                                                sender.sendMessage(colorize(Objects.requireNonNull(instance.messagesCfg.getString("create.invalid-setting-t"))
                                                        .replace("%prefix%", prefix)));
                                            }
                                        } else if (args[i].toLowerCase().startsWith("-gs:")) {
                                            if (args[i].length() > 3) {
                                                switch (args[i].substring(3).toLowerCase()) {
                                                    case "true":
                                                    case "t":
                                                    case "yes":
                                                    case "y":
                                                        generateStructures = true;
                                                        break;
                                                    case "false":
                                                    case "f":
                                                    case "no":
                                                    case "n":
                                                        generateStructures = false;
                                                        break;
                                                    default:
                                                        sender.sendMessage(colorize(Objects.requireNonNull(instance.messagesCfg.getString("create.invalid-setting-gs"))
                                                                .replace("%prefix%", prefix)));
                                                        return true;
                                                }
                                            }
                                        } else {
                                            sender.sendMessage(colorize(Objects.requireNonNull(instance.messagesCfg.getString("create.invalid-setting"))
                                                    .replace("%prefix%", prefix))
                                                    .replace("%setting%", args[i]));
                                            return true;
                                        }
                                    }
                                }

                                sender.sendMessage(colorize(Objects.requireNonNull(instance.messagesCfg.getString("create.process.start"))
                                        .replace("%prefix%", prefix))
                                        .replace("%world%", args[1]));

                                WorldCreator worldCreator = new WorldCreator(args[1]);
                                if (seed != -1) {
                                    worldCreator.seed(seed);
                                }
                                if (generatorId != null) {
                                    worldCreator.generator(generatorId);
                                }
                                if (worldType != null) {
                                    worldCreator.type(worldType);
                                }
                                worldCreator.generateStructures(generateStructures);
                                worldCreator.environment(environment);
                                Bukkit.createWorld(worldCreator);

                                sender.sendMessage(colorize(Objects.requireNonNull(instance.messagesCfg.getString("create.process.done"))
                                        .replace("%prefix%", prefix))
                                        .replace("%world%", args[1]));
                            }
                        } else {
                            sender.sendMessage(colorize(Objects.requireNonNull(instance.messagesCfg.getString("create.usage"))
                                    .replace("%prefix%", prefix)
                                    .replace("%label%", label)));
                        }
                    } else {
                        sender.sendMessage(colorize(Objects.requireNonNull(instance.messagesCfg.getString("no-permission"))
                                .replace("%prefix%", prefix)));
                    }
                    break;
                case "delete":
                    if (sender.hasPermission("phantomworlds.delete")) {
                        if (args.length == 2) {
                            if (instance.worldsMap.containsKey(args[1])) {
                                PhantomWorld phantomWorld = instance.worldsMap.get(args[1]);
                                try {
                                    phantomWorld.deleteWorld();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                //Make sure the player is still online (they might have been in the deleted world)
                                if (sender instanceof Player) {
                                    Player player = (Player) sender;
                                    if (player.isOnline()) {
                                        sender.sendMessage(colorize(Objects.requireNonNull(instance.messagesCfg.getString("delete.success"))
                                                .replace("%prefix%", prefix)
                                                .replace("%world%", phantomWorld.getName())));
                                    }
                                } else {
                                    sender.sendMessage(colorize(Objects.requireNonNull(instance.messagesCfg.getString("delete.success"))
                                            .replace("%prefix%", prefix)
                                            .replace("%world%", phantomWorld.getName())));
                                }
                            } else {
                                sender.sendMessage(colorize(Objects.requireNonNull(instance.messagesCfg.getString("world-not-loaded"))
                                        .replace("%prefix%", prefix))
                                        .replace("%world%", args[1]));
                            }
                        } else {
                            sender.sendMessage(colorize(Objects.requireNonNull(instance.messagesCfg.getString("delete.usage"))
                                    .replace("%prefix%", prefix)
                                    .replace("%label%", label)));
                        }
                    } else {
                        sender.sendMessage(colorize(Objects.requireNonNull(instance.messagesCfg.getString("no-permission"))
                                .replace("%prefix%", prefix)));
                    }
                    break;
                case "teleport":
                case "tp":
                    if (sender.hasPermission("phantomworlds.teleport")) {
                        if (args.length == 2) {
                            if (sender instanceof Player) {
                                Player player = (Player) sender;

                                World world = Bukkit.getWorld(args[1]);

                                if (world == null) {
                                    sender.sendMessage(colorize(Objects.requireNonNull(instance.messagesCfg.getString("world-not-loaded"))
                                            .replace("%prefix%", prefix))
                                            .replace("%world%", args[1]));
                                } else {
                                    player.teleport(world.getSpawnLocation());
                                    sender.sendMessage(colorize(Objects.requireNonNull(instance.messagesCfg.getString("teleport.self"))
                                            .replace("%prefix%", prefix)
                                            .replace("%world%", world.getName())));
                                }
                            } else {
                                sender.sendMessage(colorize(Objects.requireNonNull(instance.messagesCfg.getString("teleport.usage-console"))
                                        .replace("%prefix%", prefix)
                                        .replace("%label%", label)));
                            }
                        } else if (args.length == 3) {
                            if (sender.hasPermission("phantomworlds.teleport.others")) {
                                World world = Bukkit.getWorld(args[1]);

                                if (world == null) {
                                    sender.sendMessage(colorize(Objects.requireNonNull(instance.messagesCfg.getString("world-not-loaded"))
                                            .replace("%prefix%", prefix))
                                            .replace("%world%", args[1]));
                                } else {
                                    Player target = Bukkit.getPlayer(args[2]);

                                    if (target == null) {
                                        sender.sendMessage(colorize(Objects.requireNonNull(instance.messagesCfg.getString("player-offline"))
                                                .replace("%prefix%", prefix)
                                                .replace("%player%", args[2])));
                                    } else {

                                        // Vanished player compatibility
                                        if (!sender.hasPermission("phantomworlds.vanishbypass") && sender instanceof Player) {
                                            Player player = (Player) sender;
                                            if (!player.canSee(target)) {
                                                sender.sendMessage(colorize(Objects.requireNonNull(instance.messagesCfg.getString("player-offline"))
                                                        .replace("%prefix%", prefix)
                                                        .replace("%player%", args[2])));
                                                return true;
                                            }
                                        }

                                        target.teleport(world.getSpawnLocation());
                                        target.sendMessage(colorize(Objects.requireNonNull(instance.messagesCfg.getString("teleport.by"))
                                                .replace("%prefix%", prefix)
                                                .replace("%player%", sender.getName()
                                                        .replace("%world%", world.getName()))));
                                        sender.sendMessage(colorize(Objects.requireNonNull(instance.messagesCfg.getString("teleport.other"))
                                                .replace("%prefix%", prefix)
                                                .replace("%player%", target.getName())
                                                .replace("%world%", world.getName())));
                                    }
                                }
                            } else {
                                sender.sendMessage(colorize(Objects.requireNonNull(instance.messagesCfg.getString("no-permission"))
                                        .replace("%prefix%", prefix)));
                            }
                        } else {
                            sender.sendMessage(colorize(Objects.requireNonNull(instance.messagesCfg.getString("teleport.usage"))
                                    .replace("%prefix%", prefix)
                                    .replace("%label%", label)));
                        }
                    } else {
                        sender.sendMessage(colorize(Objects.requireNonNull(instance.messagesCfg.getString("no-permission"))
                                .replace("%prefix%", prefix)));
                    }
                    break;
                case "list":
                    if (sender.hasPermission("phantomworlds.list")) {
                        if (args.length == 1) {
                            sender.sendMessage(colorize(Objects.requireNonNull(instance.messagesCfg.getString("list.header"))
                                    .replace("%prefix%", prefix)
                                    .replace("%amount%", instance.worldsMap.size() + "")));
                            for (String worldName : instance.worldsMap.keySet()) {
                                sender.sendMessage(colorize(Objects.requireNonNull(instance.messagesCfg.getString("list.entry"))
                                        .replace("%prefix%", prefix)
                                        .replace("%world%", worldName)));
                            }
                        } else {
                            sender.sendMessage(colorize(Objects.requireNonNull(instance.messagesCfg.getString("list.usage"))
                                    .replace("%label%", label)
                                    .replace("%prefix%", prefix)));
                        }
                    } else {
                        sender.sendMessage(colorize(Objects.requireNonNull(instance.messagesCfg.getString("no-permission"))
                                .replace("%prefix%", prefix)));
                    }
                    break;
                case "spawn":
                    if (sender.hasPermission("phantomworlds.spawn")) {
                        if (args.length == 1) {
                            if (sender instanceof Player) {
                                Player player = (Player) sender;
                                player.teleport(player.getWorld().getSpawnLocation());
                                player.sendMessage(colorize(Objects.requireNonNull(instance.messagesCfg.getString("spawn.self"))
                                        .replace("%prefix%", prefix)));
                            } else {
                                sender.sendMessage(colorize(Objects.requireNonNull(instance.messagesCfg.getString("spawn.usage-console"))
                                        .replace("%prefix%", prefix)));
                            }
                        } else if (args.length == 2) {
                            if (sender.hasPermission("phantomworlds.spawn.others")) {
                                Player target = Bukkit.getPlayer(args[1]);
                                if (target == null) {
                                    sender.sendMessage(colorize(Objects.requireNonNull(instance.messagesCfg.getString("player-offline"))
                                            .replace("%prefix%", prefix))
                                            .replace("%player%", args[1]));
                                } else {
                                    target.teleport(target.getWorld().getSpawnLocation());
                                    target.sendMessage(colorize(Objects.requireNonNull(instance.messagesCfg.getString("spawn.by"))
                                            .replace("%prefix%", prefix)
                                            .replace("%player%", sender.getName())));
                                    sender.sendMessage(colorize(Objects.requireNonNull(instance.messagesCfg.getString("spawn.other"))
                                            .replace("%prefix%", prefix)
                                            .replace("%player%", target.getName())));
                                }
                            } else {
                                sender.sendMessage(colorize(Objects.requireNonNull(instance.messagesCfg.getString("no-permission"))
                                        .replace("%prefix%", prefix)));
                            }
                        } else {
                            sender.sendMessage(colorize(Objects.requireNonNull(instance.messagesCfg.getString("spawn.usage"))
                                    .replace("%prefix%", prefix)));
                        }
                    } else {
                        sender.sendMessage(colorize(Objects.requireNonNull(instance.messagesCfg.getString("no-permission"))
                                .replace("%prefix%", prefix)));
                    }
                    break;
                case "setspawn":
                    if (sender.hasPermission("phantomworlds.setspawn")) {
                        if (args.length == 1) {
                            if (sender instanceof Player) {
                                Player player = (Player) sender;
                                player.getWorld().setSpawnLocation(player.getLocation());
                                sender.sendMessage(colorize(Objects.requireNonNull(instance.messagesCfg.getString("setspawn.success"))
                                        .replace("%prefix%", prefix)));
                            } else {
                                sender.sendMessage(colorize(Objects.requireNonNull(instance.messagesCfg.getString("players-only"))
                                        .replace("%prefix%", prefix)));
                            }
                        } else {
                            sender.sendMessage(colorize(Objects.requireNonNull(instance.messagesCfg.getString("setspawn.usage"))
                                    .replace("%prefix%", prefix)
                                    .replace("%label%", label)));
                        }
                    } else {
                        sender.sendMessage(colorize(Objects.requireNonNull(instance.messagesCfg.getString("no-permission"))
                                .replace("%prefix%", prefix)));
                    }
                    break;
                default:
                    sender.sendMessage(colorize(Objects.requireNonNull(instance.messagesCfg.getString("main.usage"))
                            .replace("%prefix%", prefix)
                            .replace("%label%", label)));
                    break;
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> suggestions = new ArrayList<>();

        if (args.length == 1) {
            if (sender.hasPermission("phantomworlds.create")) {
                suggestions.add("create");
            }
            if (sender.hasPermission("phantomworlds.delete")) {
                suggestions.add("delete");
            }
            if (sender.hasPermission("phantomworlds.teleport")) {
                suggestions.add("teleport");
                suggestions.add("tp");
            }
            if (sender.hasPermission("phantomworlds.list")) {
                suggestions.add("list");
            }
            if (sender.hasPermission("phantomworlds.spawn")) {
                suggestions.add("spawn");
            }
            if (sender.hasPermission("phantomworlds.setspawn")) {
                suggestions.add("setspawn");
            }
        } else if (args.length == 2) {
            switch (args[0].toLowerCase()) {
                case "delete":
                case "teleport":
                    suggestions.addAll(getWorldNamesList());
                    break;
                case "spawn":
                    suggestions.addAll(getPlayersCanSeeList(sender));
                default:
                    //No suggestions.
                    break;
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("create")) {
                suggestions.add("normal");
                suggestions.add("nether");
                suggestions.add("end");
            } else if (args[0].equalsIgnoreCase("teleport")) {
                suggestions.addAll(getPlayersCanSeeList(sender));
            }
        } else if (args.length == 4) {
            if (args[0].equalsIgnoreCase("create")) {
                suggestions.add("-s:");
                suggestions.add("-g:");
                suggestions.add("-t:flat");
                suggestions.add("-t:largebiomes");
                suggestions.add("-t:amplified");
                suggestions.add("-gs:true");
                suggestions.add("-gs:false");
            }
        }

        return suggestions;
    }
}
