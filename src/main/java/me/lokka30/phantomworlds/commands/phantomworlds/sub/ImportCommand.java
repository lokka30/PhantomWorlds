package me.lokka30.phantomworlds.commands.phantomworlds.sub;
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

import me.lokka30.microlib.messaging.MultiMessage;
import me.lokka30.phantomworlds.PhantomWorlds;
import me.lokka30.phantomworlds.world.PhantomWorld;
import org.bukkit.GameMode;
import org.bukkit.World;
import revxrsal.commands.bukkit.BukkitCommandActor;

import java.util.Arrays;
import java.util.Collections;

/**
 * ImportCommand
 *
 * @author creatorfromhell
 * @since 2.0.5.0
 */
public class ImportCommand {

  public static void onCommand(final BukkitCommandActor actor, final World world) {

    if(world == null) {
      (new MultiMessage(
              PhantomWorlds.instance().messages.getConfig().getStringList(
                      "command.phantomworlds.subcommands.import.failure-exist"),
              Collections.singletonList(
                      new MultiMessage.Placeholder("prefix", PhantomWorlds.instance().messages.getConfig()
                              .getString("common.prefix", "&b&lPhantomWorlds: &7"), true)
              ))).send(actor.getSender());
      return;
    }

    final String cfgPath = "worlds-to-load." + world.getName() + ".";
    if(PhantomWorlds.instance().data.getConfig().contains(cfgPath)) {
      (new MultiMessage(
              PhantomWorlds.instance().messages.getConfig().getStringList(
                      "command.phantomworlds.subcommands.import.failure-already"),
              Arrays.asList(
                      new MultiMessage.Placeholder("prefix", PhantomWorlds.instance().messages.getConfig()
                              .getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                      new MultiMessage.Placeholder("world", world.getName(), false)
              ))).send(actor.getSender());
      return;
    }

    final PhantomWorld pworld = new PhantomWorld(
            world.getName(), world.getEnvironment(), world.canGenerateStructures(), null,
            null, world.isHardcore(), world.getSeed(), world.getWorldType(), world.getAllowMonsters(),
            world.getAllowAnimals(), world.getKeepSpawnInMemory(), world.getPVP(), world.getDifficulty(), GameMode.SURVIVAL
    );
    pworld.save();
    (new MultiMessage(
            PhantomWorlds.instance().messages.getConfig().getStringList(
                    "command.phantomworlds.subcommands.import.success"),
            Arrays.asList(
                    new MultiMessage.Placeholder("prefix", PhantomWorlds.instance().messages.getConfig()
                            .getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                    new MultiMessage.Placeholder("world", world.getName(), false)
            ))).send(actor.getSender());
  }
}