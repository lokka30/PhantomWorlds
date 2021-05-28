package me.lokka30.phantomworlds.commands;

import me.lokka30.microlib.MessageUtils;
import me.lokka30.phantomworlds.PhantomWorlds;
import me.lokka30.phantomworlds.misc.Utils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PhantomWorldsCommand implements TabExecutor {

    private final PhantomWorlds main;

    public PhantomWorldsCommand(PhantomWorlds main) {
        this.main = main;
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

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String prefix = main.messages.getConfig().getString("prefix", "&b&lPhantomWorlds:&7");

        if (args.length == 0) {
            for (String message : main.messages.getConfig().getStringList("main.info")) {
                sender.sendMessage(MessageUtils.colorizeAll(message
                        .replace("%prefix%", prefix)
                        .replace("%label%", label)
                        .replace("%version%", main.getDescription().getVersion())));
            }
        } else {
            switch (args[0].toLowerCase()) {
                case "create":
                    if (sender.hasPermission("phantomworlds.create")) {
                        if (args.length >= 3) {
                            if (Utils.getLoadedWorlds().contains(args[1])) {
                                sender.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(main.messages.getConfig().getString("create.world-already-exists"))
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
                                        sender.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(main.messages.getConfig().getString("create.invalid-environment"))
                                                .replace("%prefix%", prefix))
                                                .replace("%environment%", args[2]));
                                        return true;
                                }

                                long seed = -1;
                                String generatorId = null;
                                WorldType worldType = null;
                                boolean generateStructures = true;

                                if (args.length > 3) {
                                    for (int i = 3; i < args.length; i++) {
                                        if (args[i].toLowerCase().startsWith("-s:")) {
                                            if (args[i].length() > 3) {
                                                try {
                                                    seed = Long.parseLong(args[i].substring(3));
                                                } catch (NumberFormatException exception) {
                                                    sender.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(main.messages.getConfig().getString("create.invalid-setting-s"))
                                                            .replace("%prefix%", prefix)));
                                                    return true;
                                                }
                                            } else {
                                                sender.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(main.messages.getConfig().getString("create.invalid-setting-s"))
                                                        .replace("%prefix%", prefix)));
                                                return true;
                                            }
                                        } else if (args[i].toLowerCase().startsWith("-g:")) {
                                            if (args[i].length() > 3) {
                                                generatorId = args[i].substring(3);
                                            } else {
                                                sender.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(main.messages.getConfig().getString("create.invalid-setting-g"))
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
                                                        sender.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(main.messages.getConfig().getString("create.invalid-setting-t"))
                                                                .replace("%prefix%", prefix)));
                                                        return true;
                                                }
                                            } else {
                                                sender.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(main.messages.getConfig().getString("create.invalid-setting-t"))
                                                        .replace("%prefix%", prefix)));
                                            }
                                        } else if (args[i].toLowerCase().startsWith("-gs:")) {
                                            if (args[i].length() > 4) {
                                                switch (args[i].substring(4).toLowerCase()) {
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
                                                        sender.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(main.messages.getConfig().getString("create.invalid-setting-gs"))
                                                                .replace("%prefix%", prefix)));
                                                        return true;
                                                }
                                            } else {
                                                sender.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(main.messages.getConfig().getString("create.invalid-setting-gs"))
                                                        .replace("%prefix%", prefix)));
                                                return true;
                                            }
                                        } else {
                                            sender.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(main.messages.getConfig().getString("create.invalid-setting"))
                                                    .replace("%prefix%", prefix))
                                                    .replace("%setting%", args[i]));
                                            return true;
                                        }
                                    }
                                }

                                sender.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(main.messages.getConfig().getString("create.process.start"))
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
                                new PhantomWorld(main, args[1]).addToData(); //TODO

                                sender.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(main.messages.getConfig().getString("create.process.done"))
                                        .replace("%prefix%", prefix))
                                        .replace("%world%", args[1]));
                            }
                        } else {
                            sender.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(main.messages.getConfig().getString("create.usage"))
                                    .replace("%prefix%", prefix)
                                    .replace("%label%", label)));
                        }
                    } else {
                        sender.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(main.messages.getConfig().getString("no-permission"))
                                .replace("%prefix%", prefix)));
                    }
                    break;
                case "unload":
                    if (sender.hasPermission("phantomworlds.unload")) {
                        if (args.length == 2) {
                            if (Utils.getLoadedWorlds().contains(args[1])) {
                                //noinspection ConstantConditions
                                Utils.unloadWorld(main, Bukkit.getWorld(args[1]));

                                //Make sure the player is still online (they might have been in the unloaded world)
                                if (sender instanceof Player) {
                                    Player player = (Player) sender;
                                    if (player.isOnline()) {
                                        sender.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(main.messages.getConfig().getString("unload.success"))
                                                .replace("%prefix%", prefix))
                                                .replace("%world%", args[1])); // positioned 1 bracket earlier so that it is not colorized
                                    }
                                } else {
                                    sender.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(main.messages.getConfig().getString("unload.success"))
                                            .replace("%prefix%", prefix))
                                            .replace("%world%", args[1])); // positioned 1 bracket earlier so that it is not colorized
                                }
                            } else {
                                sender.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(main.messages.getConfig().getString("world-not-loaded"))
                                        .replace("%prefix%", prefix))
                                        .replace("%world%", args[1])); // positioned 1 bracket earlier so that it is not colorized
                            }
                        } else {
                            sender.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(main.messages.getConfig().getString("unload.usage"))
                                    .replace("%prefix%", prefix)
                                    .replace("%label%", label)));
                        }
                    } else {
                        sender.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(main.messages.getConfig().getString("no-permission"))
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
                                    sender.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(main.messages.getConfig().getString("world-not-loaded"))
                                            .replace("%prefix%", prefix))
                                            .replace("%world%", args[1]));
                                } else {
                                    player.teleport(world.getSpawnLocation());
                                    sender.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(main.messages.getConfig().getString("teleport.self"))
                                            .replace("%prefix%", prefix)
                                            .replace("%world%", world.getName())));
                                }
                            } else {
                                sender.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(main.messages.getConfig().getString("teleport.usage-console"))
                                        .replace("%prefix%", prefix)
                                        .replace("%label%", label)));
                            }
                        } else if (args.length == 3) {
                            if (sender.hasPermission("phantomworlds.teleport.others")) {
                                World world = Bukkit.getWorld(args[1]);

                                if (world == null) {
                                    sender.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(main.messages.getConfig().getString("world-not-loaded"))
                                            .replace("%prefix%", prefix))
                                            .replace("%world%", args[1]));
                                } else {
                                    Player target = Bukkit.getPlayer(args[2]);

                                    if (target == null) {
                                        sender.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(main.messages.getConfig().getString("player-offline"))
                                                .replace("%prefix%", prefix)
                                                .replace("%player%", args[2])));
                                    } else {

                                        // Vanished player compatibility
                                        if (!sender.hasPermission("phantomworlds.vanishbypass") && sender instanceof Player) {
                                            Player player = (Player) sender;
                                            if (!player.canSee(target)) {
                                                sender.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(main.messages.getConfig().getString("player-offline"))
                                                        .replace("%prefix%", prefix)
                                                        .replace("%player%", args[2])));
                                                return true;
                                            }
                                        }

                                        target.teleport(world.getSpawnLocation());
                                        target.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(main.messages.getConfig().getString("teleport.by"))
                                                .replace("%prefix%", prefix)
                                                .replace("%player%", sender.getName())
                                                .replace("%world%", world.getName())));
                                        sender.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(main.messages.getConfig().getString("teleport.other"))
                                                .replace("%prefix%", prefix)
                                                .replace("%player%", target.getName())
                                                .replace("%world%", world.getName())));
                                    }
                                }
                            } else {
                                sender.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(main.messages.getConfig().getString("no-permission"))
                                        .replace("%prefix%", prefix)));
                            }
                        } else {
                            sender.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(main.messages.getConfig().getString("teleport.usage"))
                                    .replace("%prefix%", prefix)
                                    .replace("%label%", label)));
                        }
                    } else {
                        sender.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(main.messages.getConfig().getString("no-permission"))
                                .replace("%prefix%", prefix)));
                    }
                    break;
                case "list":
                    if (sender.hasPermission("phantomworlds.list")) {
                        if (args.length == 1) {
                            sender.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(main.messages.getConfig().getString("list.header"))
                                    .replace("%prefix%", prefix)
                                    .replace("%amount%", Utils.getLoadedWorlds().size() + "")));
                            for (String worldName : Utils.getLoadedWorlds()) {
                                sender.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(main.messages.getConfig().getString("list.entry"))
                                        .replace("%prefix%", prefix)
                                        .replace("%world%", worldName)));
                            }
                        } else {
                            sender.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(main.messages.getConfig().getString("list.usage"))
                                    .replace("%label%", label)
                                    .replace("%prefix%", prefix)));
                        }
                    } else {
                        sender.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(main.messages.getConfig().getString("no-permission"))
                                .replace("%prefix%", prefix)));
                    }
                    break;
                case "spawn":
                    if (sender.hasPermission("phantomworlds.spawn")) {
                        if (args.length == 1) {
                            if (sender instanceof Player) {
                                Player player = (Player) sender;
                                player.teleport(player.getWorld().getSpawnLocation());
                                player.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(main.messages.getConfig().getString("spawn.self"))
                                        .replace("%prefix%", prefix)));
                            } else {
                                sender.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(main.messages.getConfig().getString("spawn.usage-console"))
                                        .replace("%prefix%", prefix)));
                            }
                        } else if (args.length == 2) {
                            if (sender.hasPermission("phantomworlds.spawn.others")) {
                                Player target = Bukkit.getPlayer(args[1]);
                                if (target == null) {
                                    sender.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(main.messages.getConfig().getString("player-offline"))
                                            .replace("%prefix%", prefix))
                                            .replace("%player%", args[1]));
                                } else {
                                    target.teleport(target.getWorld().getSpawnLocation());
                                    target.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(main.messages.getConfig().getString("spawn.by"))
                                            .replace("%prefix%", prefix)
                                            .replace("%player%", sender.getName())));
                                    sender.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(main.messages.getConfig().getString("spawn.other"))
                                            .replace("%prefix%", prefix)
                                            .replace("%player%", target.getName())));
                                }
                            } else {
                                sender.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(main.messages.getConfig().getString("no-permission"))
                                        .replace("%prefix%", prefix)));
                            }
                        } else {
                            sender.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(main.messages.getConfig().getString("spawn.usage"))
                                    .replace("%prefix%", prefix)));
                        }
                    } else {
                        sender.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(main.messages.getConfig().getString("no-permission"))
                                .replace("%prefix%", prefix)));
                    }
                    break;
                case "setspawn":
                    if (sender.hasPermission("phantomworlds.setspawn")) {
                        if (args.length == 1) {
                            if (sender instanceof Player) {
                                Player player = (Player) sender;
                                player.getWorld().setSpawnLocation(player.getLocation());
                                sender.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(main.messages.getConfig().getString("setspawn.success"))
                                        .replace("%prefix%", prefix)));
                            } else {
                                sender.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(main.messages.getConfig().getString("players-only"))
                                        .replace("%prefix%", prefix)));
                            }
                        } else {
                            sender.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(main.messages.getConfig().getString("setspawn.usage"))
                                    .replace("%prefix%", prefix)
                                    .replace("%label%", label)));
                        }
                    } else {
                        sender.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(main.messages.getConfig().getString("no-permission"))
                                .replace("%prefix%", prefix)));
                    }
                    break;
                default:
                    sender.sendMessage(MessageUtils.colorizeAll(Objects.requireNonNull(main.messages.getConfig().getString("main.usage"))
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
            if (sender.hasPermission("phantomworlds.unload")) {
                suggestions.add("unload");
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
                case "unload":
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
        } else if (args.length >= 4 && args[0].equalsIgnoreCase("create")) {
            suggestions.add("-s:");
            suggestions.add("-g:");
            suggestions.add("-t:flat");
            suggestions.add("-t:largebiomes");
            suggestions.add("-t:amplified");
            suggestions.add("-gs:true");
            suggestions.add("-gs:false");
        }

        return suggestions;
    }
}
