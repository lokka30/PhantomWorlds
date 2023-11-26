package me.lokka30.phantomworlds.listeners.player;
/*
 * Phantom Worlds
 * Copyright (C) 2023 Daniel "creatorfromhell" Vidmar
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import me.lokka30.phantomworlds.PhantomWorlds;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * PlayerLoginListener
 *
 * @author creatorfromhell
 * @since 2.0.5.0
 */
public class PlayerJoinListener implements Listener {

  final PhantomWorlds plugin;

  public PlayerJoinListener(PhantomWorlds plugin) {
    this.plugin = plugin;
  }

  @EventHandler
  public void onJoin(PlayerJoinEvent event) {
    final String spawnWorld = PhantomWorlds.instance().settings.getConfig().getString("spawn-world", "world");
    final World sWorld = Bukkit.getWorld(spawnWorld);
    if(sWorld == null) {
      plugin.getLogger().warning("Configured spawn world doesn't exist! Not changing player spawn location.");
      return;
    }

    final World world = (event.getPlayer().hasPlayedBefore())? event.getPlayer().getWorld() : sWorld;

    //Check if we manage the spawn for the world the player needs to join in.
    final String cfgPath = "worlds-to-load." + world.getName() + ".spawn";
    if(PhantomWorlds.instance().data.getConfig().contains(cfgPath)) {
      final double x = PhantomWorlds.instance().data.getConfig().getDouble(cfgPath + ".x", world.getSpawnLocation().getX());
      final double y = PhantomWorlds.instance().data.getConfig().getDouble(cfgPath + ".y", world.getSpawnLocation().getY());
      final double z = PhantomWorlds.instance().data.getConfig().getDouble(cfgPath + ".z", world.getSpawnLocation().getZ());
      final float yaw = (float)PhantomWorlds.instance().data.getConfig().getDouble(cfgPath + ".yaw", world.getSpawnLocation().getYaw());
      final float pitch = (float)PhantomWorlds.instance().data.getConfig().getDouble(cfgPath + ".pitch", world.getSpawnLocation().getPitch());

      event.getPlayer().teleport(new Location(world, x, y, z, yaw, pitch));
    } else {

      //We don't manage so send the player to the spawn world
      if(!event.getPlayer().hasPlayedBefore()) {
        event.getPlayer().teleport(sWorld.getSpawnLocation());
      }
    }
  }
}