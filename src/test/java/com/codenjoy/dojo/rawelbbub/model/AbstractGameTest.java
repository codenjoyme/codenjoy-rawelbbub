package com.codenjoy.dojo.rawelbbub.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2022 Codenjoy
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
import com.codenjoy.dojo.rawelbbub.model.items.AI;
import com.codenjoy.dojo.rawelbbub.services.Event;
import com.codenjoy.dojo.rawelbbub.services.GameSettings;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.multiplayer.TriFunction;
import com.codenjoy.dojo.utils.gametest.AbstractBaseGameTest;
import org.junit.After;
import org.junit.Before;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class AbstractGameTest
        extends AbstractBaseGameTest<Player, Rawelbbub, GameSettings, Level, Hero> {

    @Before
    public void setup() {
        super.setup();
    }

    @After
    public void after() {
        super.after();
    }

    @Override
    protected void afterCreateField() {
        stopAllAis();
    }

    @Override
    protected GameSettings setupSettings() {
        return new TestGameSettings();
    }

    @Override
    protected Function<String, Level> createLevel() {
        return Level::new;
    }

    @Override
    protected BiFunction<EventListener, GameSettings, Player> createPlayer() {
        return Player::new;
    }

    @Override
    protected TriFunction<Dice, Level, GameSettings, Rawelbbub> createField() {
        return Rawelbbub::new;
    }

    @Override
    protected Class<?> eventClass() {
        return Event.class;
    }

    // other methods

    private void stopAllAis() {
        field().ais().stream()
                .filter(ai -> ai instanceof AI)
                .map(ai -> (AI) ai)
                .forEach(ai -> {
                    ai.dontShoot = true;
                    ai.dontMove = true;
                });
    }

    public AI dropAI(Point pt) {
        AI ai = field().getAiGenerator().drop(pt);
        ai.dontMove = true;
        ai.dontShoot = true;
        return ai;
    }

    public void assertPrize(String expected) {
        List<Hero> all = field().heroesAndAis();
        long prizes = all.stream().filter(Hero::withPrize).count();

        assertEquals(expected,
                String.format("%s prizes with %s heroes",
                        prizes, all.size()));
    }

    protected AI ai(int index) {
        return (AI) field().ais().get(index);
    }
}