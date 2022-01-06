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
import com.codenjoy.dojo.rawelbbub.model.items.ai.AI;
import com.codenjoy.dojo.rawelbbub.services.Event;
import com.codenjoy.dojo.rawelbbub.services.GameSettings;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.multiplayer.TriFunction;
import com.codenjoy.dojo.utils.gametest.AbstractBaseGameTest;
import org.junit.After;
import org.junit.Before;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.codenjoy.dojo.client.Utils.split;
import static com.codenjoy.dojo.rawelbbub.model.items.ai.AiGenerator.NO_MORE_AIS;
import static com.codenjoy.dojo.rawelbbub.services.GameSettings.Keys.COUNT_AIS;

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
    protected void beforeCreateField() {
        int spawn = level().aisSpawn().size();
        if (settings().integer(COUNT_AIS) > spawn) {
            settings().integer(COUNT_AIS, spawn);
        } else {
            // спавн мест больше чем будет AI, все ок
        }
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
        field().ais().forEach(AI::stop);
        field().prizeAis().forEach(AI::stop);
    }

    public AI dropAI(Point pt) {
        settings().integer(COUNT_AIS, settings().integer(COUNT_AIS) + 1);
        AI result = field().getAiGenerator().drop(pt);
        result.stop();
        return result;
    }

    public void assertPrize(String expected) {
        assertEquals(expected,
                String.format("%s prizes with %s heroes",
                        field().prizeAis().size(),
                        field().heroesAndAis().size()));
    }

    public AI ai(int index) {
        LinkedList<AI> all = new LinkedList<>() {{
            addAll(field().ais().copy());
            addAll(field().prizeAis().copy());
        }};
        Collections.sort(all, Comparator.reverseOrder());
        return all.get(index);
    }

    public void assertIcebergs(String expected) {
        assertEquals(expected, split(field().icebergs(), "], \nIceberg"));
    }

    public void willPrize(int diceValue) {
        willPrize(diceValue,  // вероятность приза
                NO_MORE_AIS); // больше AI генерить не будем
    }

    public void willPrize(int diceValue, int newAiSpawnIndex) {
        dice(diceValue, newAiSpawnIndex);
    }

    public void noMoreAi() {
        dice(NO_MORE_AIS); // больше AI генерить не будем
    }
}