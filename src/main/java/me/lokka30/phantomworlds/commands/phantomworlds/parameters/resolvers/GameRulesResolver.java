package me.lokka30.phantomworlds.commands.phantomworlds.parameters.resolvers;
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

import me.lokka30.phantomworlds.commands.phantomworlds.utils.WorldFolder;
import me.lokka30.phantomworlds.commands.phantomworlds.utils.WorldRule;
import org.bukkit.GameRule;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.process.ValueResolver;

import java.util.ArrayList;
import java.util.ListIterator;

/**
 * GameRulesResolver
 *
 * @author creatorfromhell
 * @since 2.0.5.0
 */
public class GameRulesResolver implements ValueResolver<WorldRule[]> {

  @Override
  public WorldRule[] resolve(@NotNull ValueResolverContext context) throws Throwable {
    final WorldRule[] rules = new WorldRule[context.arguments().size()];
    final ListIterator<String> it = context.arguments().listIterator();

    while(it.hasNext()) {
      final String value = it.next();


    }
    return rules;
  }
}