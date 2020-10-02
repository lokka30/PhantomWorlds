package me.lokka30.phantomworlds;

import me.lokka30.microlib.MicroUtils;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

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
        addToData();
    }

    public void unloadWorld() {
        for (Player player : Objects.requireNonNull(Bukkit.getWorld(name)).getPlayers()) {
            player.kickPlayer(MicroUtils.colorize(Objects.requireNonNull(instance.messagesCfg.getString("unload.kick"))
                    .replace("%prefix%", Objects.requireNonNull(instance.messagesCfg.getString("prefix")))));
        }

        Bukkit.unloadWorld(name, true);

        removeFromData();
    }

    public String getName() {
        return name;
    }

    public void addToData() {
        if (!instance.worldsMap.containsKey(name)) {
            instance.worldsMap.put(name, this);
        }

        List<String> worldsList = instance.dataCfg.getStringList("worlds");
        if (!worldsList.contains(name)) {
            worldsList.add(name);
            instance.dataCfg.set("worlds", worldsList);
            saveData();
        }
    }

    public void removeFromData() {
        instance.worldsMap.remove(name);
    }

    private void saveData() {
        try {
            instance.dataCfg.save(instance.dataFile);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
