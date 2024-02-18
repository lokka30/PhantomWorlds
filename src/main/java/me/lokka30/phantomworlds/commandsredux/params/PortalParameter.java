package me.lokka30.phantomworlds.commandsredux.params;
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

import dev.rollczi.litecommands.argument.Argument;
import dev.rollczi.litecommands.argument.parser.ParseResult;
import dev.rollczi.litecommands.argument.resolver.ArgumentResolver;
import dev.rollczi.litecommands.invocation.Invocation;
import dev.rollczi.litecommands.suggestion.SuggestionContext;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import org.bukkit.PortalType;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

/**
 * PotionEffectParameter
 *
 * @author creatorfromhell
 * @since 2.0.5.0
 */
public class PortalParameter extends ArgumentResolver<CommandSender, PortalType> {

  private static final Map<String, PortalType> PORTAL_ARGUMENTS = new HashMap<>();

  static {
    PORTAL_ARGUMENTS.put("end", PortalType.ENDER);
    PORTAL_ARGUMENTS.put("nether", PortalType.NETHER);
  }

  @Override
  protected ParseResult<PortalType> parse(Invocation<CommandSender> invocation, Argument<PortalType> context, String argument) {
    PortalType gameMode = PORTAL_ARGUMENTS.get(argument.toLowerCase());

    if (gameMode == null) {
      return ParseResult.failure("Invalid portal type argument!");
    }

    return ParseResult.success(gameMode);
  }

  @Override
  public SuggestionResult suggest(Invocation<CommandSender> invocation, Argument<PortalType> argument, SuggestionContext context) {
    return SuggestionResult.of(PORTAL_ARGUMENTS.keySet());
  }
}
