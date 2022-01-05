package com.codenjoy.dojo.rawelbbub.model;

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


import org.junit.Test;

import static com.codenjoy.dojo.services.round.RoundSettings.Keys.ROUNDS_ENABLED;

public class MultiplayerTest extends AbstractGameTest {

    @Test
    public void shouldAllHeroesInOneBoard_whenNewGame() {
        // given when
        givenFl("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ▲ ☼\n" +
                "☼▲  ☼\n" +
                "☼☼☼☼☼\n");
        
        // then
        assertF("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ▲ ☼\n" +
                "☼˄  ☼\n" +
                "☼☼☼☼☼\n", 0);

        assertF("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ˄ ☼\n" +
                "☼▲  ☼\n" +
                "☼☼☼☼☼\n", 1);
    }

    @Test
    public void shouldCantDoAnything_whenRoundIsNotStarted_whenRoundsEnabled() {
        // given
        settings().bool(ROUNDS_ENABLED, true);

        givenFl("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼˄  ☼\n" +
                "☼▲  ☼\n" +
                "☼☼☼☼☼\n");

        assertEquals(false, hero(0).isActive());
        assertEquals(true, hero(0).isAlive());

        assertEquals(false, hero(1).isActive());
        assertEquals(true, hero(1).isAlive());

        assertF("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼˄  ☼\n" +
                "☼▲  ☼\n" +
                "☼☼☼☼☼\n", 0);

        verifyAllEvents("");

        // when
        hero(0).right();
        hero(0).fire();

        hero(1).right();
        tick();

        // then
        assertEquals(false, hero(0).isActive());
        assertEquals(true, hero(0).isAlive());

        assertEquals(false, hero(1).isActive());
        assertEquals(true, hero(1).isAlive());

        assertF("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼˄  ☼\n" +
                "☼▲  ☼\n" +
                "☼☼☼☼☼\n", 0);

        verifyAllEvents(
                "listener(0) => [[....4....]]\n" +
                "listener(1) => [[....4....]]\n");
    }

    @Test
    public void shouldCanDoAnything_whenRoundsDisabled() {
        // given
        settings().bool(ROUNDS_ENABLED, false);

        givenFl("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼˄  ☼\n" +
                "☼▲  ☼\n" +
                "☼☼☼☼☼\n");

        assertEquals(true, hero(0).isActive());
        assertEquals(true, hero(0).isAlive());

        assertEquals(true, hero(1).isActive());
        assertEquals(true, hero(1).isAlive());

        assertF("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼˄  ☼\n" +
                "☼▲  ☼\n" +
                "☼☼☼☼☼\n", 0);

        // when
        hero(0).right();
        hero(0).fire();

        hero(1).right();
        tick();

        // then
        assertEquals(true, hero(0).isActive());
        assertEquals(true, hero(0).isAlive());

        assertEquals(true, hero(1).isActive());
        assertEquals(true, hero(1).isAlive());

        assertF("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ˃ ☼\n" +
                "☼ ►¤☼\n" +
                "☼☼☼☼☼\n", 0);
    }
    
    @Test
    public void shouldRandomPosition_whenKillHero() {
        // given
        givenFl("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼˄  ☼\n" +
                "☼▲  ☼\n" +
                "☼☼☼☼☼\n");

        assertF("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼˄  ☼\n" +
                "☼▲  ☼\n" +
                "☼☼☼☼☼\n", 0);

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼Ѡ  ☼\n" +
                "☼▲  ☼\n" +
                "☼☼☼☼☼\n", 0);

        verifyAllEvents(
                "listener(0) => [KILL_OTHER_HERO[1]]\n" +
                "listener(1) => [HERO_DIED]\n");

        assertEquals(true, game(1).isGameOver());

        // when
        dice(2, 2);
        game(1).newGame();

        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ˄ ☼\n" +
                "☼▲  ☼\n" +
                "☼☼☼☼☼\n", 0);

    }

    @Test
    public void shouldRandomPosition_atFreeSpace_whenKillHero() {
        // given
        givenFl("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼˄  ☼\n" +
                "☼▲  ☼\n" +
                "☼☼☼☼☼\n");

        assertF("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼˄  ☼\n" +
                "☼▲  ☼\n" +
                "☼☼☼☼☼\n", 0);

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼Ѡ  ☼\n" +
                "☼▲  ☼\n" +
                "☼☼☼☼☼\n", 0);

        verifyAllEvents(
                "listener(0) => [KILL_OTHER_HERO[1]]\n" +
                "listener(1) => [HERO_DIED]\n");

        assertEquals(true, game(1).isGameOver());

        // when
        dice(0, 0, // skipped, not free, because hero
            2, 2);
        game(1).newGame();

        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ˄ ☼\n" +
                "☼▲  ☼\n" +
                "☼☼☼☼☼\n", 0);

    }

    @Test
    public void shouldRandomPosition_atFreeSpace_whenTrySpawnUnderSeaweedFishnetOrIce() {
        // given
        givenFl("☼☼☼☼☼\n" +
                "☼  %☼\n" +
                "☼˄ ~☼\n" +
                "☼▲ #☼\n" +
                "☼☼☼☼☼\n");

        assertF("☼☼☼☼☼\n" +
                "☼  %☼\n" +
                "☼˄ ~☼\n" +
                "☼▲ #☼\n" +
                "☼☼☼☼☼\n", 0);

        // when
        hero(0).fire();
        tick();

        assertF("☼☼☼☼☼\n" +
                "☼  %☼\n" +
                "☼Ѡ ~☼\n" +
                "☼▲ #☼\n" +
                "☼☼☼☼☼\n", 0);

        verifyAllEvents(
                "listener(0) => [KILL_OTHER_HERO[1]]\n" +
                "listener(1) => [HERO_DIED]\n");

        assertEquals(true, game(1).isGameOver());

        // when
        dice(3, 3, // skipped, not free, because seaweed
            3, 2, // skipped, not free, because fishnet
            3, 1, // skipped, not free, because oil leak
            2, 2);
        game(1).newGame();

        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼  %☼\n" +
                "☼ ˄~☼\n" +
                "☼▲ #☼\n" +
                "☼☼☼☼☼\n", 0);
    }
}