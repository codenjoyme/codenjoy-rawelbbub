package com.codenjoy.dojo.rawelbbub.services;

/*-
 * #%L
 * expansion - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2022 Codenjoy
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

    public static final int MODE_ALL_DIRECTIONS = 0;
    public static final int MODE_FORWARD_BACKWARD = 1;
    public static final int MODE_SIDE_VIEW = 2;

    public enum Keys implements Key {

        AI_TICKS_PER_SHOOT("[Game] Ticks until the next AI shoot"),
        HERO_TICKS_PER_SHOOT("[Game] Ticks until the next hero shoot"),
        OIL_SLIPPERINESS("[Game] The amount of leaked oil. The more - the more naughty the submarine."),
        PENALTY_WALKING_ON_FISHNET("[Game] Penalty time when walking on fishnet"),
        SHOW_MY_HERO_UNDER_SEAWEED("[Game] Show my tank under seaweed"),
        ICEBERG_REGENERATE_TIME("[Game] Iceberg regenerate time"),
        TICKS_STUCK_BY_FISHNET("[Game] Ticks AI gets stuck by fishnet"),
        COUNT_AIS("[Game] Count AIs on the board"),
        TURN_MODE("[Game] Turn mode"),

        AI_PRIZE_PROBABILITY("[Prize] Count spawn for AI with prize"),
        AI_PRIZE_SURVIVABILITY("[Prize] Hits to kill AI with prize"),
        PRIZE_AVAILABLE_TIMEOUT("[Prize] The period of prize validity on the field after the appearance"),
        PRIZE_EFFECT_TIMEOUT("[Prize] Working time of the prize after catch up"),
        PRIZES_COUNT("[Prize] The total number of prize AI and prizes on the board"),
        PRIZE_BLINK_TIMEOUT("[Prize] Prize sprite changes every ticks"),

        CHANCE_IMMORTALITY("[Chance] Prize immortality"),
        CHANCE_BREAKING_BAD("[Chance] Prize breaking bad"),
        CHANCE_WALKING_ON_FISHNET("[Chance] Prize walking on fishnet"),
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

        integer(AI_TICKS_PER_SHOOT, 10);
        integer(HERO_TICKS_PER_SHOOT, 4);
        integer(OIL_SLIPPERINESS, 3);
        integer(PENALTY_WALKING_ON_FISHNET, 2);
        bool(SHOW_MY_HERO_UNDER_SEAWEED, false);
        integer(ICEBERG_REGENERATE_TIME, 10);
        integer(TICKS_STUCK_BY_FISHNET, 5);
        integer(COUNT_AIS, 3);
        integer(TURN_MODE, MODE_ALL_DIRECTIONS);

        integer(AI_PRIZE_PROBABILITY, 4);
        integer(AI_PRIZE_SURVIVABILITY, 3);
        integer(PRIZE_AVAILABLE_TIMEOUT, 50);
        integer(PRIZE_EFFECT_TIMEOUT, 30);
        integer(PRIZES_COUNT, 3);
        integer(PRIZE_BLINK_TIMEOUT, 2);

        integer(CHANCE_RESERVED, 30);
        integer(CHANCE_IMMORTALITY, 20);
        integer(CHANCE_BREAKING_BAD, 20);
        integer(CHANCE_WALKING_ON_FISHNET, 20);
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
            .put(CHANCE_BREAKING_BAD, PRIZE_BREAKING_BAD)
            .put(CHANCE_WALKING_ON_FISHNET, PRIZE_WALKING_ON_FISHNET)
            .put(CHANCE_VISIBILITY, PRIZE_VISIBILITY)
            .put(CHANCE_NO_SLIDING, PRIZE_NO_SLIDING)
            .run();
    }

    public boolean isTurnForwardMode() {
        return integer(TURN_MODE) == MODE_FORWARD_BACKWARD;
    }

    public boolean isSideViewMode() {
        return integer(TURN_MODE) == MODE_SIDE_VIEW;
    }

    public Calculator<Integer> calculator() {
        return new Calculator<>(new Scores(this));
    }
}