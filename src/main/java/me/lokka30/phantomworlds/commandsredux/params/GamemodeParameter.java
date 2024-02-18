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
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

/**
 * PotionEffectParameter
 *
 * @author creatorfromhell
 * @since 2.0.5.0
 */
public class GamemodeParameter extends ArgumentResolver<CommandSender, GameMode> {

  private static final Map<String, GameMode> GAME_MODE_ARGUMENTS = new HashMap<>();

  static {
    for (GameMode value : GameMode.values()) {
      GAME_MODE_ARGUMENTS.put(value.name().toLowerCase(), value);

      //noinspection deprecation
      GAME_MODE_ARGUMENTS.put(String.valueOf(value.getValue()), value);
    }
  }

  @Override
  protected ParseResult<GameMode> parse(Invocation<CommandSender> invocation, Argument<GameMode> context, String argument) {
    GameMode gameMode = GAME_MODE_ARGUMENTS.get(argument.toLowerCase());

    if (gameMode == null) {
      return ParseResult.failure("Invalid gamemode argument!");
    }

    return ParseResult.success(gameMode);
  }

  @Override
  public SuggestionResult suggest(Invocation<CommandSender> invocation, Argument<GameMode> argument, SuggestionContext context) {
    return SuggestionResult.of(GAME_MODE_ARGUMENTS.keySet());
  }
}
