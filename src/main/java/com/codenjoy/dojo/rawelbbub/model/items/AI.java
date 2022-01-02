package com.codenjoy.dojo.rawelbbub.model.items;

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
import com.codenjoy.dojo.rawelbbub.model.Hero;
import com.codenjoy.dojo.rawelbbub.model.Player;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;

import static com.codenjoy.dojo.rawelbbub.services.GameSettings.Keys.AI_TICKS_PER_SHOOT;
import static com.codenjoy.dojo.rawelbbub.services.GameSettings.Keys.TICKS_STUCK_BY_RIVER;

public class AI extends Hero {

    public static final int MAX = 10;

    private Dice dice;
    public boolean dontShoot = false;
    public boolean dontMove = false;
    private int act;
    private int count;

    public AI(Point pt, Direction direction) {
        super(pt, direction);
        this.count = 0;
        setActive(true);
        setAlive(true);
    }

    @Override
    protected int ticksPerShoot() {
        return settings().integer(AI_TICKS_PER_SHOOT);
    }

    public int ticksStuckByRiver() {
        return settings().integer(TICKS_STUCK_BY_RIVER);
    }

    @Override
    public void tick() {
        if (!dontShoot) {
            shootIfReady();
        }
        if (!dontMove) {
            aIMove();
        }
        super.tick();
    }

    private void shootIfReady() {
        if (act++ % ticksPerShoot() == 0) {
            fire();
        }
    }

    private void aIMove() {
        int c = 0;
        Point pt;
        do {
            pt = direction.change(this);
            if (field.isBarrier(pt)) {
                direction = Direction.random(dice);
            }

            if (count == ticksStuckByRiver()) {
                direction = Direction.random(dice);
                count = 0;
            }

            if (field.isRiver(pt)) {
                count++;
            }

        } while (field.isBarrier(pt) && c++ < MAX);

        moving = true;
    }

    @Override
    public void die() {
        setAlive(false);
    }

    @Override
    public Element state(Player player, Object... alsoAtPoint) {
        Element tree = player.getHero().treeState(this, alsoAtPoint);
        if (tree != null) {
            return tree;
        }

        if (!isAlive()) {
            return Element.BANG;
        }

        Element sub = subState();
        if (sub != null) {
            return sub;
        }

        switch (direction) {
            case LEFT:  return Element.AI_LEFT;
            case RIGHT: return Element.AI_RIGHT;
            case UP:    return Element.AI_UP;
            case DOWN:  return Element.AI_DOWN;
            default: throw new RuntimeException(
                    "Неправильное состояние танка!");
        }
    }

    protected Element subState() {
        return null;
    }

    public void dice(Dice dice) {
        this.dice = dice;
    }
}
