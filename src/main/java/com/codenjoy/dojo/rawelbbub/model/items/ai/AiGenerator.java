package com.codenjoy.dojo.rawelbbub.model.items.ai;

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

import com.codenjoy.dojo.rawelbbub.model.Field;
import com.codenjoy.dojo.rawelbbub.services.GameSettings;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.dice.DiceRandomWrapper;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.rawelbbub.services.GameSettings.Keys.*;
import static com.codenjoy.dojo.services.Direction.DOWN;

public class AiGenerator {

    public static final int NO_MORE_AIS = -1;
    private final Field field;
    private final Dice dice;
    private GameSettings settings;
    private int spawnIteration;
    private List<Point> spawn;

    public AiGenerator(Field field, Dice dice, GameSettings settings) {
        this.field = field;
        this.dice = dice;
        this.settings = settings;
    }

    public void dropAll() {
        int actual = field.ais().size() + field.prizeAis().size();
        int needed = capacity() - actual;

        for (int count = 0; count < needed; count++) {
            Point pt = freePosition();
            if (pt == null) continue;

            drop(pt);
        }
    }

    private Point freePosition() {
        List<Point> places = new LinkedList<>(spawn);
        Point pt;
        do {
            int index = dice.next(places.size());
            if (index == NO_MORE_AIS || index >= places.size()) return null; // для тестов только
            pt = places.remove(index);
        } while (isBusy(pt) && !places.isEmpty());

        if (isBusy(pt)) {
            return null;
        }
        return pt;
    }

    private boolean isBusy(Point pt) {
        return field.isBarrier(pt)
                || field.isFishnet(pt);
    }

    private AI create(Point pt) {
        if (isBusy(pt)) {
            pt = freePosition();
        }

        if (isPrizeAiTurn() && prizeNeeded()) {
            return new AIPrize(pt, DOWN);
        } else {
            return new AI(pt, DOWN);
        }
    }

    private boolean isPrizeAiTurn() {
        if (spawnAiPrize() == 0) {
            return false;
        }
        return spawnIteration % spawnAiPrize() == 0;
    }

    private int spawnAiPrize() {
        return settings.integer(SPAWN_AI_PRIZE);
    }

    private int aiPrizeLimit() {
        return settings.integer(AI_PRIZE_LIMIT);
    }

    private int capacity() {
        return settings.integer(COUNT_AIS);
    }

    public AI drop(Point pt) {
        AI result = create(pt);
        result.init(field);
        spawnIteration++;
        return result;
    }

    private boolean prizeNeeded() {
        return (aiPrizeLimit() - field.totalPrizes()) > 0;
    }

    public void spawnOn(List<Point> spawn) {
        Collections.shuffle(spawn, new DiceRandomWrapper(dice));
        this.spawn = spawn;
    }
}