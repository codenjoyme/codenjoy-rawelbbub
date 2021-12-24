package com.codenjoy.dojo.battlecity.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
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


import com.codenjoy.dojo.battlecity.TestGameSettings;
import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.event.ScoresImpl;
import org.junit.Before;
import org.junit.Test;

import static com.codenjoy.dojo.battlecity.services.GameSettings.Keys.*;
import static org.junit.Assert.assertEquals;

public class ScoresTest {

    private PlayerScores scores;
    private GameSettings settings;

    public void killYourTank() {
        scores.event(Event.KILL_YOUR_TANK);
    }

    public void killOtherAITank() {
        scores.event(Event.KILL_OTHER_AI_TANK);
    }

    public void killOtherHeroTank(int amount) {
        scores.event(Event.KILL_OTHER_HERO_TANK.apply(amount));
    }

    @Before
    public void setup() {
        settings = new TestGameSettings();
        scores = new ScoresImpl(0, new Scores(settings));
    }

    @Test
    public void shouldCollectScores() {
        scores = new ScoresImpl(140, new Scores(settings));

        killOtherHeroTank(1);
        killOtherHeroTank(2);
        killOtherHeroTank(3);

        killOtherAITank();
        killOtherAITank();

        killYourTank();

        assertEquals(140
                + (1 + 2 + 3) * settings.integer(KILL_OTHER_HERO_TANK_SCORE)
                + 2 * settings.integer(KILL_OTHER_AI_TANK_SCORE)
                + settings.integer(KILL_YOUR_TANK_PENALTY),
                scores.getScore());
    }

    @Test
    public void shouldStillZeroAfterDead() {
        killYourTank();

        assertEquals(0, scores.getScore());
    }

    @Test
    public void shouldClearScore() {
        killOtherHeroTank(1);

        scores.clear();

        assertEquals(0, scores.getScore());
    }
}