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

import me.lokka30.phantomworlds.PhantomWorlds;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.process.ValueResolver;

/**
 * AliasWorldResolver
 *
 * @author creatorfromhell
 * @since 2.0.5.0
 */
public class AliasWorldResolver implements ValueResolver<World> {

  @Override
  public World resolve(@NotNull ValueResolverContext context) throws Throwable {
    final String value = context.arguments().pop();

    final String name = PhantomWorlds.worldManager().aliases.getOrDefault(value, value);

    return Bukkit.getWorld(name);
  }
}