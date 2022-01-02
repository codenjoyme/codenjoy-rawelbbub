package com.codenjoy.dojo.rawelbbub.model;

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


import com.codenjoy.dojo.games.rawelbbub.Element;
import com.codenjoy.dojo.rawelbbub.TestGameSettings;
import com.codenjoy.dojo.rawelbbub.model.items.Ice;
import com.codenjoy.dojo.rawelbbub.model.items.River;
import com.codenjoy.dojo.rawelbbub.model.items.Tree;
import com.codenjoy.dojo.rawelbbub.services.GameSettings;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.multiplayer.LevelProgress;
import com.codenjoy.dojo.services.multiplayer.Single;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;

import static com.codenjoy.dojo.services.PointImpl.pt;
import static com.codenjoy.dojo.services.round.RoundSettings.Keys.ROUNDS_ENABLED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MultiplayerTest {

    private int size = 5;
    private Rawelbbub field;
    private Game game1;
    private Game game2;
    private Player player1;
    private Player player2;
    private PrinterFactory printerFactory;
    private GameSettings settings;
    private Dice dice;
    private Level level;

    @Before
    public void setUp() {
        dice = mock(Dice.class);
        settings = new TestGameSettings();
        printerFactory = new PrinterFactoryImpl();
    }

    public void givenGame() {
        String map = StringUtils.leftPad("", size*size, Element.NONE.ch());
        int levelNumber = LevelProgress.levelsStartsFrom1;
        settings.setLevelMaps(levelNumber, new String[]{map});
        level = settings.level(levelNumber, dice, Level::new);

        field = new Rawelbbub(level, dice, settings);

        field.addBorder(new DefaultBorders(size).get());

        player1 = new Player(null, settings);
        player2 = new Player(null, settings);
        game1 = new Single(player1, printerFactory);
        game1.on(field);
        game2 = new Single(player2, printerFactory);
        game2.on(field);
    }

    @Test
    public void shouldRandomPosition_whenNewGame() {
        dice(1, 1,
                2, 2);

        givenGame();

        game1.newGame();

        assertD("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼▲  ☼\n" +
                "☼☼☼☼☼\n", player1
        );

        game2.newGame();

        assertD("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ˄ ☼\n" +
                "☼▲  ☼\n" +
                "☼☼☼☼☼\n", player1
        );
    }

    @Test
    public void shouldCantDoAnything_whenRoundIsNotStarted_whenRoundsEnabled() {
        settings.bool(ROUNDS_ENABLED, true);

        dice(1, 1,
                1, 2,
                2, 2);

        givenGame();

        game1.newGame();
        game2.newGame();

        assertEquals(false, hero1().isActive());
        assertEquals(true, hero1().isAlive());

        assertEquals(false, hero2().isActive());
        assertEquals(true, hero2().isAlive());

        assertD("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼˄  ☼\n" +
                "☼▲  ☼\n" +
                "☼☼☼☼☼\n", player1
        );

        hero1().right();
        hero1().act();

        hero2().right();
        field.tick();

        assertEquals(false, hero1().isActive());
        assertEquals(true, hero1().isAlive());

        assertEquals(false, hero2().isActive());
        assertEquals(true, hero2().isAlive());

        assertD("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼˄  ☼\n" +
                "☼▲  ☼\n" +
                "☼☼☼☼☼\n", player1
        );
    }

    @Test
    public void shouldCanDoAnything_whenRoundsDisabled() {
        settings.bool(ROUNDS_ENABLED, false);

        dice(1, 1,
                1, 2,
                2, 2);

        givenGame();

        game1.newGame();
        game2.newGame();

        assertEquals(true, hero1().isActive());
        assertEquals(true, hero1().isAlive());

        assertEquals(true, hero2().isActive());
        assertEquals(true, hero2().isAlive());

        assertD("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼˄  ☼\n" +
                "☼▲  ☼\n" +
                "☼☼☼☼☼\n", player1
        );

        hero1().right();
        hero1().act();

        hero2().right();
        field.tick();

        assertEquals(true, hero1().isActive());
        assertEquals(true, hero1().isAlive());

        assertEquals(true, hero2().isActive());
        assertEquals(true, hero2().isAlive());

        assertD("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ˃ ☼\n" +
                "☼ ►•☼\n" +
                "☼☼☼☼☼\n", player1
        );
    }

    public Hero hero2() {
        return (Hero) game2.getPlayer().getHero();
    }

    public Hero hero1() {
        return (Hero) game1.getPlayer().getHero();
    }

    @Test
    public void shouldRandomPosition_whenKillHero() {
        dice(1, 1,
                1, 2,
                2, 2);

        givenGame();

        game1.newGame();
        game2.newGame();

        assertD("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼˄  ☼\n" +
                "☼▲  ☼\n" +
                "☼☼☼☼☼\n", player1
        );

        hero1().act();
        field.tick();

        assertD("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼Ѡ  ☼\n" +
                "☼▲  ☼\n" +
                "☼☼☼☼☼\n", player1
        );

        assertTrue(game2.isGameOver());
        game2.newGame();

        field.tick();

        assertD("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ˄ ☼\n" +
                "☼▲  ☼\n" +
                "☼☼☼☼☼\n", player1
        );

    }

    @Test
    public void shouldRandomPosition_atFreeSpace_whenKillHero() {
        dice(1, 1,
                1, 2,
                0, 0, // skipped, not free, because hero
                2, 2);

        givenGame();

        game1.newGame();
        game2.newGame();

        assertD("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼˄  ☼\n" +
                "☼▲  ☼\n" +
                "☼☼☼☼☼\n", player1
        );

        hero1().act();
        field.tick();

        assertD("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼Ѡ  ☼\n" +
                "☼▲  ☼\n" +
                "☼☼☼☼☼\n", player1
        );

        assertTrue(game2.isGameOver());
        game2.newGame();

        field.tick();

        assertD("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ˄ ☼\n" +
                "☼▲  ☼\n" +
                "☼☼☼☼☼\n", player1
        );

    }

    @Test
    public void shouldRandomPosition_atFreeSpace_whenTrySpawnUnderTreeRiverOrIce() {
        dice(1, 1,
                1, 2,
                3, 3, // skipped, not free, because tree
                3, 2, // skipped, not free, because river
                3, 1, // skipped, not free, because ice
                2, 2);

        givenGame();
        field.addTree(new Tree(pt(3, 3)));
        field.addRiver(new River(pt(3, 2)));
        field.addIce(new Ice(pt(3, 1)));

        game1.newGame();
        game2.newGame();

        assertD("☼☼☼☼☼\n" +
                "☼  %☼\n" +
                "☼˄ ~☼\n" +
                "☼▲ #☼\n" +
                "☼☼☼☼☼\n", player1
        );

        hero1().act();
        field.tick();

        assertD("☼☼☼☼☼\n" +
                "☼  %☼\n" +
                "☼Ѡ ~☼\n" +
                "☼▲ #☼\n" +
                "☼☼☼☼☼\n", player1
        );

        assertTrue(game2.isGameOver());
        game2.newGame();

        field.tick();

        assertD("☼☼☼☼☼\n" +
                "☼  %☼\n" +
                "☼ ˄~☼\n" +
                "☼▲ #☼\n" +
                "☼☼☼☼☼\n", player1
        );

    }

    private void dice(int... values) {
        OngoingStubbing<Integer> when = when(dice.next(anyInt()));
        for (int value : values) {
            when = when.thenReturn(value);
        }
    }

    private void assertD(String field, Player player) {
        assertEquals(field, printerFactory.getPrinter(
                this.field.reader(), player).print());
    }

}
