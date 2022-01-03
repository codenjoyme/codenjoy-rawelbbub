package com.codenjoy.dojo.rawelbbub.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2020 Codenjoy
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

import com.codenjoy.dojo.rawelbbub.model.items.AI;
import com.codenjoy.dojo.rawelbbub.model.items.AIPrize;
import com.codenjoy.dojo.rawelbbub.services.GameSettings;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;

import java.util.List;

import static com.codenjoy.dojo.rawelbbub.services.GameSettings.Keys.AI_PRIZE_LIMIT;
import static com.codenjoy.dojo.rawelbbub.services.GameSettings.Keys.SPAWN_AI_PRIZE;
import static com.codenjoy.dojo.services.PointImpl.pt;

public class AiGenerator {

    private final Field field;
    private final Dice dice;

    private int capacity;
    private int spawn;
    private int haveWithPrize;
    private int aiPrize;

    private GameSettings settings;

    public AiGenerator(Field field, Dice dice, GameSettings settings) {
        this.field = field;
        this.dice = dice;
        this.settings = settings;
        this.spawn = 0;
        this.aiPrize = 0;
    }

    public void dropAll() {
        int actual = field.ais().size() + field.prizeAis().size();
        int needed = capacity - actual;

        for (int count = 0; count < needed; count++) {
            Point pt = freePosition();
            if (pt == null) continue;

            drop(pt);
        }
    }

    private Point findFreePosition(int y, int size) {
        Point pt = pt(0, y);

        int c = 0;
        do {
            pt.setX(dice.next(size));

        } while ((field.isBarrier(pt) || field.isFishnet(pt)) && c++ < size);

        if (field.isBarrier(pt)) {
            return null;
        }
        return pt;
    }

    private Point freePosition() {
        return findFreePosition(field.size() - 2, field.size());
    }

    private AI create(Point pt) {
        if (field.isFishnet(pt)) {
            pt = freePosition();
        }

        if (isPrizeAiTurn() && canDrop()) {
            return new AIPrize(pt, Direction.DOWN);
        } else {
            return new AI(pt, Direction.DOWN);
        }
    }

    private boolean isPrizeAiTurn() {
        if (settings.integer(SPAWN_AI_PRIZE) == 0) {
            return false;
        }
        return spawn % settings.integer(SPAWN_AI_PRIZE) == 0;
    }

    public AI drop(Point pt) {
        AI result = create(pt);
        result.init(field);
        spawn++;
        return result;
    }

    public void dropAll(List<? extends Point> points) {
        capacity = points.size();
        for (Point pt : points) {
            drop(pt);
        }
    }

    public void allHave(int haveWithPrize) {
        this.haveWithPrize = haveWithPrize;
    }

    private int neededWithPrize() {
        if (settings.integer(AI_PRIZE_LIMIT) == 0) {
            aiPrize = 0;
        }
        int neededWithPrize = settings.integer(AI_PRIZE_LIMIT) - haveWithPrize;

        if (aiPrize < neededWithPrize) {
            aiPrize++;
        } else {
            aiPrize = 0;
        }
        return aiPrize;
    }

    private boolean canDrop() {
        int moreWithPrize = neededWithPrize();

        if(moreWithPrize == 0) {
            return false;
        }

        if (moreWithPrize > 0) {
            return true;
        }
       return false;
    }
}
