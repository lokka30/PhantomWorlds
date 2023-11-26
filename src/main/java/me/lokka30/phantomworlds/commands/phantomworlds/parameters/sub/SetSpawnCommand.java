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

import me.lokka30.microlib.messaging.MultiMessage;
import me.lokka30.phantomworlds.PhantomWorlds;
import me.lokka30.phantomworlds.misc.Utils;
import org.bukkit.Location;
import org.bukkit.World;
import revxrsal.commands.bukkit.BukkitCommandActor;

import java.io.IOException;
import java.util.Arrays;

/**
 * SetSpawnCommand
 *
 * @author creatorfromhell
 * @since 2.0.5.0
 */
public class SetSpawnCommand {

  public static void onCommand(final BukkitCommandActor actor, Double x, Double y, Double z, World world, Float yaw, Float pitch) {

    if(actor.isConsole() || actor.getAsPlayer() == null) {
      if(x == null || y == null || z == null || world == null) {
        (new MultiMessage(
                PhantomWorlds.instance().messages.getConfig()
                        .getStringList("command.phantomworlds.subcommands.setspawn.usage-console"),
                Arrays.asList(
                        new MultiMessage.Placeholder("prefix", PhantomWorlds.instance().messages.getConfig()
                                .getString("common.prefix", "&b&lPhantomWorlds: &7"), true),
                        new MultiMessage.Placeholder("label", "setspawn", false)
                ))).send(actor.getSender());
        return;
      }
    }

    final World finalWorld = (world == null)? actor.getAsPlayer().getWorld() : world;
    final double finalX = (x == null)? actor.getAsPlayer().getLocation().getX() : x;
    final double finalY = (y == null)? actor.getAsPlayer().getLocation().getY() : y;
    final double finalZ = (z == null)? actor.getAsPlayer().getLocation().getZ() : z;
    float finalYaw = (yaw == null)? 0 : yaw;
    float finalPitch = (pitch == null)? 0 : pitch;

    if(yaw == null && actor.getAsPlayer() != null) {
      finalYaw = actor.getAsPlayer().getLocation().getYaw();
    }

    if(pitch == null && actor.getAsPlayer() != null) {
      finalPitch = actor.getAsPlayer().getLocation().getPitch();
    }

    final String cfgPath = "worlds-to-load." + finalWorld.getName();
    if(PhantomWorlds.instance().data.getConfig().contains(cfgPath)) {
      //PhantomWorlds manages this world so let's set the spawn here for better accuracy.
      PhantomWorlds.instance().data.getConfig().set(cfgPath + ".spawn.x", finalX);
      PhantomWorlds.instance().data.getConfig().set(cfgPath + ".spawn.y", finalX);
      PhantomWorlds.instance().data.getConfig().set(cfgPath + ".spawn.z", finalX);
      PhantomWorlds.instance().data.getConfig().set(cfgPath + ".spawn.yaw", finalX);
      PhantomWorlds.instance().data.getConfig().set(cfgPath + ".spawn.pitch", finalX);

      try {
        PhantomWorlds.instance().data.save();
      } catch(final IOException ex) {
        throw new RuntimeException(ex);
      }

    } else {
      //PhantomWorlds doesn't manage the spawn here so let Mojang deal with it.
      try {
        finalWorld.setSpawnLocation(new Location(finalWorld, finalX, finalY, finalZ, finalYaw, finalPitch));
      } catch(NoSuchMethodError err) {
        //This is dumb that the setSpawn method in spigot uses integers... great design.
        finalWorld.setSpawnLocation((int)finalX, (int)finalY, (int)finalZ);
        // 1.8 doesn't let us set pitch and yaw ... yawn
      }
    }

    (new MultiMessage(
            PhantomWorlds.instance().messages.getConfig()
                    .getStringList("command.phantomworlds.subcommands.setspawn.success"), Arrays.asList(
            new MultiMessage.Placeholder("prefix",
                    PhantomWorlds.instance().messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"),
                    true),
            new MultiMessage.Placeholder("world", finalWorld.getName(), false),
            new MultiMessage.Placeholder("x", String.valueOf(Utils.roundTwoDecimalPlaces(finalX)), false),
            new MultiMessage.Placeholder("y", String.valueOf(Utils.roundTwoDecimalPlaces(finalY)), false),
            new MultiMessage.Placeholder("z", String.valueOf(Utils.roundTwoDecimalPlaces(finalZ)), false),
            new MultiMessage.Placeholder("yaw", String.valueOf(Utils.roundTwoDecimalPlaces(finalYaw)), false),
            new MultiMessage.Placeholder("pitch", String.valueOf(Utils.roundTwoDecimalPlaces(finalPitch)), false)
    ))).send(actor.getSender());
  }
}