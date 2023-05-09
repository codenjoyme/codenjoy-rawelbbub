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
import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.event.Calculator;
import com.codenjoy.dojo.services.event.ScoresImpl;
import org.junit.Before;
import org.junit.Test;

import static com.codenjoy.dojo.rawelbbub.services.GameSettings.Keys.*;
import static org.junit.Assert.assertEquals;

public class ScoresTest {

    private PlayerScores scores;
    private GameSettings settings;

    public void heroDied() {
        scores.event(Event.HERO_DIED);
    }

    public void killAI() {
        scores.event(Event.KILL_AI);
    }

    public void killOtherHero(int amount) {
        scores.event(Event.KILL_OTHER_HERO.apply(amount));
    }

    public void killEnemyHero(int amount) {
        scores.event(Event.KILL_ENEMY_HERO.apply(amount));
    }

    @Before
    public void setup() {
        settings = new TestGameSettings();
    }

    private void givenScores(int score) {
        scores = new ScoresImpl<>(score, new Calculator<>(new Scores(settings)));
    }

    @Test
    public void shouldCollectScores() {
        // given
        givenScores(140);

        // when
        killOtherHero(1);
        killOtherHero(2);
        killOtherHero(3);

        killAI();
        killAI();

        heroDied();

        killEnemyHero(1);
        killEnemyHero(2);

        // then
        assertEquals(140
                    + (1 + 2 + 3) * settings.integer(KILL_OTHER_HERO_SCORE)
                    + (1 + 2) * settings.integer(KILL_ENEMY_HERO_SCORE)
                    + 2 * settings.integer(KILL_AI_SCORE)
                    + settings.integer(HERO_DIED_PENALTY),
                scores.getScore());
    }

    @Test
    public void shouldNotBeLessThanZero() {
        // given
        givenScores(0);

        // when
        heroDied();

        // then
        assertEquals(0, scores.getScore());
    }

    @Test
    public void shouldClearScore() {
        // given
        givenScores(0);

        killOtherHero(1);

        // when
        scores.clear();

        // then
        assertEquals(0, scores.getScore());
    }

    @Test
    public void shouldCollectScores_whenKillOtherHero() {
        // given
        givenScores(140);

        // when
        killOtherHero(1);

        // then
        assertEquals(140
                    + settings.integer(KILL_OTHER_HERO_SCORE),
                scores.getScore());

        // when
        killOtherHero(2);

        // then
        assertEquals(140
                    + (1 + 2) * settings.integer(KILL_OTHER_HERO_SCORE),
                scores.getScore());

        // when
        killOtherHero(3);

        // then
        assertEquals(140
                     + (1 + 2 + 3) * settings.integer(KILL_OTHER_HERO_SCORE),
                scores.getScore());
    }

    @Test
    public void shouldCollectScores_whenKillEnemyHero() {
        // given
        givenScores(140);

        // when
        killEnemyHero(1);

        // then
        assertEquals(140
                    + settings.integer(KILL_ENEMY_HERO_SCORE),
                scores.getScore());

        // when
        killEnemyHero(2);

        // then
        assertEquals(140
                    + (1 + 2) * settings.integer(KILL_ENEMY_HERO_SCORE),
                scores.getScore());

        // when
        killEnemyHero(3);

        // then
        assertEquals(140
                    + (1 + 2 + 3) * settings.integer(KILL_ENEMY_HERO_SCORE),
                scores.getScore());
    }

    @Test
    public void shouldCollectScores_whenHeroDied() {
        // given
        givenScores(140);

        // when
        heroDied();
        heroDied();

        // then
        assertEquals(140
                    + 2 * settings.integer(HERO_DIED_PENALTY),
                scores.getScore());
    }

    @Test
    public void shouldCollectScores_whenKillAI() {
        // given
        givenScores(140);

        // when
        killAI();
        killAI();

        // then
        assertEquals(140
                    + 2 * settings.integer(KILL_AI_SCORE),
                scores.getScore());
    }
}