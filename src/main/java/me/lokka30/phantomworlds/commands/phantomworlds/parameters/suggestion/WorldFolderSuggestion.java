package me.lokka30.phantomworlds.commands.phantomworlds.parameters.suggestion;
/*
 * The New Economy
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

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.autocomplete.SuggestionProvider;
import revxrsal.commands.command.CommandActor;
import revxrsal.commands.command.ExecutableCommand;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * RegionSuggestion
 *
 * @author creatorfromhell
 * @since 0.1.2.0
 */
public class WorldFolderSuggestion implements SuggestionProvider {

  @Override
  public @NotNull Collection<String> getSuggestions(@NotNull List<String> list, @NotNull CommandActor commandActor, @NotNull ExecutableCommand executableCommand) throws Throwable {

    final List<String> folders = new ArrayList<>();
    final File directory = Bukkit.getWorldContainer();

    for(File file : directory.listFiles()) {
      final File levelDat = new File(file, "level.dat");
      if(file.isDirectory() && levelDat.exists()) {
        folders.add(file.getName());
      }
    }
    return folders;
  }
}