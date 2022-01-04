package com.codenjoy.dojo.rawelbbub.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
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

import com.codenjoy.dojo.client.Utils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GameSettingsTest {

    @Test
    public void shouldGetAllKeys() {
        assertEquals("[AI_TICKS_PER_SHOOT, \n" +
                        "HERO_TICKS_PER_SHOOT, \n" +
                        "OIL_SLIPPERINESS, \n" +
                        "PENALTY_WALKING_ON_FISHNET, \n" +
                        "SHOW_MY_HERO_UNDER_SEAWEED, \n" +
                        "ICEBERG_REGENERATE_TIME, \n" +
                        "TICKS_STUCK_BY_FISHNET, \n" +
                        "SPAWN_AI_PRIZE, \n" +
                        "KILL_HITS_AI_PRIZE, \n" +
                        "PRIZE_ON_FIELD, \n" +
                        "PRIZE_WORKING, \n" +
                        "AI_PRIZE_LIMIT, \n" +
                        "AI_PRIZE_SPRITE_CHANGE_TICKS, \n" +
                        "PRIZE_SPRITE_CHANGE_TICKS, \n" +
                        "CHANCE_IMMORTALITY, \n" +
                        "CHANCE_BREAKING_BAD, \n" +
                        "CHANCE_WALKING_ON_FISHNET, \n" +
                        "CHANCE_VISIBILITY, \n" +
                        "CHANCE_NO_SLIDING, \n" +
                        "HERO_DIED_PENALTY, \n" +
                        "KILL_OTHER_HERO_SCORE, \n" +
                        "KILL_AI_SCORE, \n" +
                        "SCORE_COUNTING_TYPE]",
                Utils.split(new GameSettings().allKeys(), ", \n"));
    }

}
