package me.lokka30.phantomworlds.scheduler;
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
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * BackupScheduler
 *
 * @author creatorfromhell
 * @since 2.0.5.0
 */
public class BackupScheduler extends BukkitRunnable {
  /**
   * When an object implementing interface {@code Runnable} is used to create a thread, starting the
   * thread causes the object's {@code run} method to be called in that separately executing
   * thread.
   * <p>
   * The general contract of the method {@code run} is that it may take any action whatsoever.
   *
   * @see Thread#run()
   */
  @Override
  public void run() {
    PhantomWorlds.logger().info("Running World Backup Task...");

    for(final World world : Bukkit.getWorlds()) {
      PhantomWorlds.logger().info("Backing up world '" + world.getName() + "'...");
      PhantomWorlds.worldManager().backupWorld(world.getName());
    }
    PhantomWorlds.logger().info("World Backup Task has completed!");
  }
}