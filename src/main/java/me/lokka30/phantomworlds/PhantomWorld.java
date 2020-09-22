package me.lokka30.phantomworlds;

import org.bukkit.*;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class PhantomWorld {

    private final PhantomWorlds instance;
    private final String name;

    public PhantomWorld(PhantomWorlds instance, String name) {
        this.instance = instance;
        this.name = name;
    }

    public void createWorld() throws IOException {
        Bukkit.createWorld(new WorldCreator(name));
        instance.dataCfg.set("worlds", instance.dataCfg.getStringList("worlds").add(name));
        instance.dataCfg.save(instance.dataFile);
    }

    public void deleteWorld() throws IOException {
        for(Player player : Objects.requireNonNull(Bukkit.getWorld(name)).getPlayers()) {
            player.kickPlayer(colorize(instance.messagesCfg.getString("delete.kick")));
        }

        Bukkit.unloadWorld(name, true);
        File file = new File(Bukkit.getWorldContainer(), name);
        if(!file.delete()) {
            throw new IOException("Unable to delete world as world folder didn't exist");
        }

        instance.worldsMap.remove(name);
        instance.dataCfg.set("worlds", instance.dataCfg.getStringList("worlds").remove(name));
        instance.dataCfg.save(instance.dataFile);
    }

    public String getName() {
        return name;
    }

    private String colorize(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}
