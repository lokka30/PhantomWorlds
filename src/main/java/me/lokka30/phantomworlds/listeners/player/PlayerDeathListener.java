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
import me.lokka30.phantomworlds.misc.Utils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

/**
 * PlayerDeathListener
 *
 * @author creatorfromhell
 * @since 2.0.5.0
 */
public class PlayerDeathListener implements Listener {

  final PhantomWorlds plugin;

  public PlayerDeathListener(PhantomWorlds plugin) {
    this.plugin = plugin;
  }

  @EventHandler
  public void onDeath(PlayerDeathEvent event) {
    if(!PhantomWorlds.instance().settings.getConfig().getBoolean("spawning.respawn-world", false)) {
      return;
    }

    if(event.getEntity().getBedSpawnLocation() == null) {

      event.getEntity().teleport(Utils.parseSpawn(event.getEntity().getWorld()));
    }
  }
}