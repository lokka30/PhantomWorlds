package me.lokka30.phantomworlds;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class PhantomWorld {

    private final PhantomWorlds instance;
    private final String name;

    public PhantomWorld(PhantomWorlds instance, String name) {
        this.instance = instance;
        this.name = name;
    }

    public void createWorld() {
        Bukkit.createWorld(new WorldCreator(name));
        instance.worldsMap.put(name, this);
        addToData();
    }

    public void deleteWorld() throws IOException {
        for (Player player : Objects.requireNonNull(Bukkit.getWorld(name)).getPlayers()) {
            player.kickPlayer(colorize(Objects.requireNonNull(instance.messagesCfg.getString("delete.kick"))
                    .replace("%prefix%", Objects.requireNonNull(instance.messagesCfg.getString("prefix")))));
        }

        Bukkit.unloadWorld(name, true);

        File folder = new File(Bukkit.getWorldContainer(), name);
        if (!folder.delete()) {
            throw new IOException("Unable to delete world as world folder didn't exist");
        }

        instance.worldsMap.remove(name);
        removeFromData();
    }

    public String getName() {
        return name;
    }

    private String colorize(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    private void addToData() {
        List<String> worlds = instance.dataCfg.getStringList("worlds");
        worlds.add(name);
        instance.dataCfg.set("worlds", worlds);
    }

    private void removeFromData() {
        List<String> worlds = instance.dataCfg.getStringList("worlds");
        worlds.remove(name);
        instance.dataCfg.set("worlds", worlds);
    }
}
