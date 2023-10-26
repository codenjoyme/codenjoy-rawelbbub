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


import com.codenjoy.dojo.rawelbbub.TestGameSettings;
import com.codenjoy.dojo.services.event.EventObject;
import com.codenjoy.dojo.services.event.ScoresMap;
import com.codenjoy.dojo.utils.scorestest.AbstractScoresTest;
import org.junit.Test;

import static com.codenjoy.dojo.rawelbbub.services.GameSettings.Keys.*;

public class ScoresTest extends AbstractScoresTest {

    @Override
    public GameSettings settings() {
        return new TestGameSettings()
                .integer(KILL_OTHER_HERO_SCORE, 1)
                .integer(KILL_ENEMY_HERO_SCORE, 2)
                .integer(KILL_AI_SCORE, 3)
                .integer(HERO_DIED_PENALTY, -1);
    }

    @Override
    protected Class<? extends ScoresMap> scores() {
        return Scores.class;
    }

    @Override
    protected Class<? extends EventObject> events() {
        return Event.class;
    }

    @Override
    protected Class<? extends Enum> eventTypes() {
        return Event.Type.class;
    }

    @Test
    public void shouldCollectScores() {
        assertEvents("100:\n" +
                "KILL_OTHER_HERO,1 > +1 = 101\n" +
                "KILL_OTHER_HERO,2 > +2 = 103\n" +
                "KILL_OTHER_HERO,3 > +3 = 106\n" +
                "KILL_AI > +3 = 109\n" +
                "KILL_AI > +3 = 112\n" +
                "HERO_DIED > -1 = 111\n" +
                "KILL_ENEMY_HERO,1 > +2 = 113\n" +
                "KILL_ENEMY_HERO,2 > +4 = 117");
    }

    @Test
    public void shouldNotBeLessThanZero() {
        assertEvents("1:\n" +
                "HERO_DIED > -1 = 0\n" +
                "HERO_DIED > +0 = 0\n" +
                "HERO_DIED > +0 = 0");
    }

    @Test
    public void shouldCleanScore() {
        assertEvents("0:\n" +
                "KILL_OTHER_HERO,1 > +1 = 1\n" +
                "(CLEAN) > -1 = 0\n" +
                "KILL_ENEMY_HERO,2 > +4 = 4");
    }

    @Test
    public void shouldCollectScores_whenKillOtherHero() {
        // given
        settings.integer(KILL_OTHER_HERO_SCORE, 1);

        // when then
        assertEvents("100:\n" +
                "KILL_OTHER_HERO,1 > +1 = 101\n" +
                "KILL_OTHER_HERO,2 > +2 = 103\n" +
                "KILL_OTHER_HERO,3 > +3 = 106");
    }

    @Test
    public void shouldCollectScores_whenKillEnemyHero() {
        // given
        settings.integer(KILL_ENEMY_HERO_SCORE, 1);

        // when then
        assertEvents("100:\n" +
                "KILL_ENEMY_HERO,1 > +1 = 101\n" +
                "KILL_ENEMY_HERO,2 > +2 = 103\n" +
                "KILL_ENEMY_HERO,3 > +3 = 106");
    }

    @Test
    public void shouldCollectScores_whenHeroDied() {
        // given
        settings.integer(HERO_DIED_PENALTY, -1);

        // when then
        assertEvents("100:\n" +
                "HERO_DIED > -1 = 99\n" +
                "HERO_DIED > -1 = 98");
    }

    @Test
    public void shouldCollectScores_whenKillAI() {
        // given
        settings.integer(KILL_AI_SCORE, 1);

        // when then
        assertEvents("100:\n" +
                "KILL_AI > +1 = 101\n" +
                "KILL_AI > +1 = 102");
    }
}