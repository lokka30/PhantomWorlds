package me.lokka30.phantomworlds.listeners.player;
/*
 * Phantom Worlds
 * Copyright (C) 2023 - 2024 Daniel "creatorfromhell" Vidmar
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
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 * PlayerTeleportEvent
 *
 * @author creatorfromhell
 * @since 2.0.5.0
 */
public class PlayerTeleportListener implements Listener {

  final PhantomWorlds plugin;

  public PlayerTeleportListener(PhantomWorlds plugin) {
    this.plugin = plugin;
  }

  @EventHandler
  public void onPortal(PlayerTeleportEvent event) {
    if(event.getTo() == null || event.getTo().getWorld() == null || event.getFrom().getWorld() == null) {
      return;
    }

    if(event.getFrom().getWorld().getUID().equals(event.getTo().getWorld().getUID())) {
      return;
    }

    final String cfgPath = "worlds-to-load." + event.getTo().getWorld().getName();

    if(PhantomWorlds.instance().data.getConfig().getBoolean(cfgPath + ".whitelist", false)
            && !event.getPlayer().hasPermission("phantomworlds.world.access." + event.getTo().getWorld().getName())) {
      event.setCancelled(true);
      return;
    }

    if(!event.getPlayer().isOp() && event.getPlayer().hasPermission("phantomworlds.world.deny." + event.getTo().getWorld().getName())) {
      event.setCancelled(true);
    }
  }
}