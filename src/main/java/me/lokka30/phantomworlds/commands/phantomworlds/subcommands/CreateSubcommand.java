package me.lokka30.phantomworlds.commands.phantomworlds.subcommands;

import me.lokka30.phantomworlds.PhantomWorlds;
import me.lokka30.phantomworlds.commands.ISubcommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CreateSubcommand implements ISubcommand {

    /*TODO
    Save world data code:

    public void saveToData(PhantomWorlds main) {
        main.data.getConfig().set("worlds-to-load." + name + ".environment", environment.toString());
        main.data.getConfig().set("worlds-to-load." + name + ".generateStructures", generateStructures);
        main.data.getConfig().set("worlds-to-load." + name + ".generator", generator);
        main.data.getConfig().set("worlds-to-load." + name + ".generatorSettings", generatorSettings);
        main.data.getConfig().set("worlds-to-load." + name + ".hardcore", hardcore);
        main.data.getConfig().set("worlds-to-load." + name + ".seed", seed);
        main.data.getConfig().set("worlds-to-load." + name + ".worldType", worldType);
        main.data.getConfig().set("worlds-to-load." + name + ".spawnMobs", spawnMobs);
        main.data.getConfig().set("worlds-to-load." + name + ".spawnAnimals", spawnAnimals);
        main.data.getConfig().set("worlds-to-load." + name + ".keepSpawnInMemory", keepSpawnInMemory);
        main.data.getConfig().set("worlds-to-load." + name + ".allowPvP", allowPvP);
        main.data.getConfig().set("worlds-to-load." + name + ".difficulty", difficulty.toString());

        try {
            main.data.save();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
     }
     */

    @Override
    public void parseCommand(@NotNull PhantomWorlds main, CommandSender sender, Command cmd, String label, String[] args) {
        //TODO
    }

    @Override
    public List<String> parseTabCompletion(PhantomWorlds main, CommandSender sender, Command cmd, String label, String[] args) {
        //TODO
        return null;
    }
}
