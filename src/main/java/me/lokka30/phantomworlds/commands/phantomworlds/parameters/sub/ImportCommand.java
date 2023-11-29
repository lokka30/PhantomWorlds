package me.lokka30.phantomworlds.commands.phantomworlds.parameters.sub;
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
import me.lokka30.phantomworlds.world.PhantomWorld;
import org.bukkit.GameMode;
import org.bukkit.World;
import revxrsal.commands.bukkit.BukkitCommandActor;

/**
 * ImportCommand
 *
 * @author creatorfromhell
 * @since 2.0.5.0
 */
public class ImportCommand {

  public static void onCommand(final BukkitCommandActor actor, final World world) {

    if(world == null) {
      //TODO: send message world doesn't exist, can't import.
      return;
    }

    final String cfgPath = "worlds-to-load." + world.getName() + ".";
    if(PhantomWorlds.instance().data.getConfig().contains(cfgPath)) {
      //TODO: Already a PhantomWorlds managed world.
      return;
    }

    final PhantomWorld pworld = new PhantomWorld(
            world.getName(), world.getEnvironment(), world.canGenerateStructures(), null,
            null, world.isHardcore(), world.getSeed(), world.getWorldType(), world.getAllowMonsters(),
            world.getAllowAnimals(), world.getKeepSpawnInMemory(), world.getPVP(), world.getDifficulty(), GameMode.SURVIVAL
    );
    pworld.save();
    //TODO: world imported and will be managed by phantomworlds
  }
}