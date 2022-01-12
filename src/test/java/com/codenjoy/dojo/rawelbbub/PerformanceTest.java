package com.codenjoy.dojo.rawelbbub;

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

import com.codenjoy.dojo.client.local.DiceGenerator;
import com.codenjoy.dojo.rawelbbub.services.GameRunner;
import com.codenjoy.dojo.rawelbbub.services.GameSettings;
import com.codenjoy.dojo.services.Dice;
import org.junit.Test;

import static com.codenjoy.dojo.rawelbbub.services.GameSettings.Keys.COUNT_AIS;
import static com.codenjoy.dojo.services.round.RoundSettings.Keys.ROUNDS_ENABLED;
import static com.codenjoy.dojo.utils.TestUtils.assertPerformance;

public class PerformanceTest {

    @Test
    public void test() {

        // about 9 sec
        int ais = 20;
        int players = 50;
        int ticks = 1000;

        int expectedCreation = 1100;
        int expectedTick = 3500;
        int expectedPrint = 4000;

        Dice dice = new DiceGenerator().getDice(2000);
        GameRunner runner = new GameRunner(){

            @Override
            public Dice getDice() {
                return dice;
            }

            @Override
            public GameSettings getSettings() {
                return new GameSettings()
                    .integer(COUNT_AIS, ais);
            }
        };

        boolean printBoard = false;
        assertPerformance(runner,
                players, ticks,
                expectedCreation, expectedTick, expectedPrint,
                printBoard);
    }
}