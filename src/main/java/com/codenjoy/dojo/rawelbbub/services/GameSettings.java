package com.codenjoy.dojo.rawelbbub.services;

/*-
 * #%L
 * expansion - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 - 2020 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.codenjoy.dojo.games.rawelbbub.Element;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.event.Calculator;
import com.codenjoy.dojo.services.event.ScoresImpl;
import com.codenjoy.dojo.services.settings.AllSettings;
import com.codenjoy.dojo.services.settings.Chance;
import com.codenjoy.dojo.services.settings.SettingsImpl;

import java.util.Arrays;
import java.util.List;

import static com.codenjoy.dojo.games.rawelbbub.Element.*;
import static com.codenjoy.dojo.rawelbbub.services.GameSettings.Keys.*;
import static com.codenjoy.dojo.services.settings.Chance.CHANCE_RESERVED;

public class GameSettings extends SettingsImpl
        implements AllSettings<GameSettings> {

    public enum Keys implements Key {

        AI_TICKS_PER_SHOOT("[Game] Ticks until the next AI shoot"),
        HERO_TICKS_PER_SHOOT("[Game] Ticks until the next hero shoot"),
        SLIPPERINESS("[Game] Value of hero sliding on ice"),
        PENALTY_WALKING_ON_WATER("[Game] Penalty time when walking on water"),
        SHOW_MY_HERO_UNDER_TREE("[Game] Show my hero under tree"),
        WALL_REGENERATE_TIME("[Game] Wall regenerate time"),
        TICKS_STUCK_BY_RIVER("[Game] Ticks AI gets stuck by river"),

        SPAWN_AI_PRIZE("[Prize] Count spawn for AI with prize"),
        KILL_HITS_AI_PRIZE("[Prize] Hits to kill AI with prize"),
        PRIZE_ON_FIELD("[Prize] The period of prize validity on the field after the appearance"),
        PRIZE_WORKING("[Prize] Working time of the prize after catch up"),
        AI_PRIZE_LIMIT("[Prize] The total number of prize AI and prizes on the board"),
        AI_PRIZE_SPRITE_CHANGE_TICKS("[Prize] AI sprite changes every ticks"),
        PRIZE_SPRITE_CHANGE_TICKS("[Prize] Prize sprite changes every ticks"),

        CHANCE_IMMORTALITY("[Chance] Prize immortality"),
        CHANCE_BREAKING_WALLS("[Chance] Prize breaking walls"),
        CHANCE_WALKING_ON_WATER("[Chance] Prize walking on water"),
        CHANCE_VISIBILITY("[Chance] Prize visibility"),
        CHANCE_NO_SLIDING("[Chance] Prize no sliding"),

        HERO_DIED_PENALTY("[Score] Kill your hero penalty"),
        KILL_OTHER_HERO_SCORE("[Score] Kill other hero score"),
        KILL_AI_SCORE("[Score] Kill other AI score"),

        SCORE_COUNTING_TYPE(ScoresImpl.SCORE_COUNTING_TYPE.key());

        private String key;

        Keys(String key) {
            this.key = key;
        }

        @Override
        public String key() {
            return key;
        }
    }

    @Override
    public List<Key> allKeys() {
        return Arrays.asList(Keys.values());
    }

    public GameSettings() {
        initAll();

        // сколько участников в комнате
        playersPerRoom().update(20);

        integer(AI_TICKS_PER_SHOOT, 10);
        integer(HERO_TICKS_PER_SHOOT, 4);
        integer(SLIPPERINESS, 3);
        integer(PENALTY_WALKING_ON_WATER, 2);
        bool(SHOW_MY_HERO_UNDER_TREE, false);
        integer(WALL_REGENERATE_TIME, 30);
        integer(TICKS_STUCK_BY_RIVER, 5);

        integer(SPAWN_AI_PRIZE, 4);
        integer(KILL_HITS_AI_PRIZE, 3);
        integer(PRIZE_ON_FIELD, 50);
        integer(PRIZE_WORKING, 30);
        integer(AI_PRIZE_LIMIT, 3);
        integer(AI_PRIZE_SPRITE_CHANGE_TICKS, 4);
        integer(PRIZE_SPRITE_CHANGE_TICKS, 2);

        integer(CHANCE_RESERVED, 30);
        integer(CHANCE_IMMORTALITY, 20);
        integer(CHANCE_BREAKING_WALLS, 20);
        integer(CHANCE_WALKING_ON_WATER, 20);
        integer(CHANCE_VISIBILITY, 20);
        integer(CHANCE_NO_SLIDING, 20);

        integer(HERO_DIED_PENALTY, -0);
        integer(KILL_OTHER_HERO_SCORE, 50);
        integer(KILL_AI_SCORE, 25);


        Levels.setup(this);
    }

    public Chance<Element> chance(Dice dice) {
        return new Chance<Element>(dice, this)
            .put(CHANCE_IMMORTALITY, PRIZE_IMMORTALITY)
            .put(CHANCE_BREAKING_WALLS, PRIZE_BREAKING_WALLS)
            .put(CHANCE_WALKING_ON_WATER, PRIZE_WALKING_ON_WATER)
            .put(CHANCE_VISIBILITY, PRIZE_VISIBILITY)
            .put(CHANCE_NO_SLIDING, PRIZE_NO_SLIDING)
            .run();
    }

    public Calculator<Integer> calculator() {
        return new Calculator<>(new Scores(this));
    }
}
