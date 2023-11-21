package me.lokka30.phantomworlds.listeners.player;
/*
 * The New Kings
 * Copyright (C) 2022 - 2023 Daniel "creatorfromhell" Vidmar
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
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import static org.bukkit.event.player.PlayerTeleportEvent.TeleportCause.END_PORTAL;

/**
 * EntityPortalListener
 *
 * @author creatorfromhell
 * @since 0.0.1.0
 */
public class PlayerPortalListener implements Listener {

  final PhantomWorlds plugin;

  public PlayerPortalListener(PhantomWorlds plugin) {
    this.plugin = plugin;
  }

  @EventHandler
  public void onPortal(PlayerPortalEvent event) {
    if(event.getFrom().getWorld() == null) return;

    final String cfgPath = "worlds-to-load." + event.getFrom().getWorld().getName();

    final boolean end = event.getCause().equals(END_PORTAL);
    final String config = (end)? ".end" : ".nether";

    if(PhantomWorlds.instance().data.getConfig().contains(cfgPath + config)) {
      final String to = PhantomWorlds.instance().data.getConfig().getString(cfgPath + config);

      if(to == null || Bukkit.getWorld(to) == null) {
        plugin.getLogger().warning("Configured portal world doesn't exist! Not changing player portal location.");
        return;
      }

      final Location toLocation = event.getTo();
      if(toLocation == null) {
        plugin.getLogger().warning("Configured portal world doesn't exist! Not changing player portal location.");
        return;
      }
      toLocation.setWorld(Bukkit.getWorld(to));
    }
  }
}