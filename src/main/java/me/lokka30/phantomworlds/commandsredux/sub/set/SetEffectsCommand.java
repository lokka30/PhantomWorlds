package me.lokka30.phantomworlds.commandsredux.sub.set;
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

import me.lokka30.microlib.messaging.MultiMessage;
import me.lokka30.phantomworlds.PhantomWorlds;
import me.lokka30.phantomworlds.misc.Utils;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * SetEffectsCommand
 *
 * @author creatorfromhell
 * @since 2.0.5.0
 */
public class SetEffectsCommand {
  public static void onCommand(final CommandSender sender, final World world, final List<String> effects) {
    if(!Utils.checkWorld(sender, "command.phantomworlds.subcommands.seteffects.usage", world)) {
      return;
    }

    final StringBuilder eff = new StringBuilder();

    final World finalWorld = (world == null)? ((Player)sender).getWorld() : world;

    final String cfgPath = "worlds-to-load." + finalWorld.getName();

    for(String effString : effects) {

      if(eff.length() > 0) {
        eff.append(", ");
      }

      final String[] effSettings = effString.split(",");

      int duration = -1;
      if(effSettings.length > 1) {
        try {
          duration = Integer.parseInt(effSettings[1]);
        } catch(NumberFormatException ignore) {
        }
      }


      int amplifier = 1;
      if(effSettings.length > 2) {
        try {
          amplifier = Integer.parseInt(effSettings[2]);
        } catch(NumberFormatException ignore) {
        }
      }

      eff.append(effSettings[0]);

      PhantomWorlds.instance().data.getConfig().set(cfgPath + ".effects." + effSettings[0] + ".duration", duration);
      PhantomWorlds.instance().data.getConfig().set(cfgPath + ".effects." + effSettings[0] + ".amplifier", amplifier);
    }

    try {
      PhantomWorlds.instance().data.save();
    } catch(final IOException ex) {
      throw new RuntimeException(ex);
    }

    (new MultiMessage(
            PhantomWorlds.instance().messages.getConfig()
                    .getStringList("command.phantomworlds.subcommands.seteffects.success"), Arrays.asList(
            new MultiMessage.Placeholder("prefix",
                    PhantomWorlds.instance().messages.getConfig().getString("common.prefix", "&b&lPhantomWorlds: &7"),
                    true),
            new MultiMessage.Placeholder("world", finalWorld.getName(), false),
            new MultiMessage.Placeholder("effects", eff.toString(), false)
    ))).send(sender);
  }
}