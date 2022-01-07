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

import com.codenjoy.dojo.games.rawelbbub.Element;
import com.codenjoy.dojo.rawelbbub.model.Field;
import com.codenjoy.dojo.rawelbbub.model.Hero;
import com.codenjoy.dojo.rawelbbub.model.Player;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;

import static com.codenjoy.dojo.rawelbbub.services.GameSettings.Keys.AI_TICKS_PER_SHOOT;
import static com.codenjoy.dojo.rawelbbub.services.GameSettings.Keys.TICKS_STUCK_BY_FISHNET;
import static com.codenjoy.dojo.services.route.Route.FORWARD;

public class AI extends Hero {

    public static final int MAX = 10;

    public boolean dontShoot = false;
    public boolean dontMove = false;
    private int act;
    private int stuck;

    public AI(Point pt, Direction direction) {
        super(pt, direction);
        this.stuck = 0;
        setActive(true);
        setAlive(true);
    }

    @Override
    public boolean isAI() {
        return true;
    }

    @Override
    protected int ticksPerShoot() {
        return settings().integer(AI_TICKS_PER_SHOOT);
    }

    public int ticksStuckByFishnet() {
        return settings().integer(TICKS_STUCK_BY_FISHNET);
    }

    @Override
    public void tickHero() {
        if (!dontShoot) {
            shootIfReady();
        }
        if (!dontMove) {
            changeDirection();
        }
        super.tickHero();
    }

    @Override
    public void init(Field field) {
        super.init(field);

        if (!withPrize()) {
            field.ais().add(this);
        }
    }

    private void shootIfReady() {
        if (act++ % ticksPerShoot() == 0) {
            fire();
        }
    }

    private void changeDirection() {
        int count = 0;
        Point pt;
        do {
            pt = direction.change(this);
            if (field.isBarrier(pt)) {
                direction = Direction.random(dice());
            }

            if (stuck == ticksStuckByFishnet()) {
                direction = Direction.random(dice());
                stuck = 0;
            }

            if (field.isFishnet(pt)) {
                stuck++;
            }
        } while (field.isBarrier(pt) && count++ < MAX);

        route(FORWARD);
    }

    @Override
    public void die() {
        setAlive(false);
    }

    @Override
    public Element state(Player player, Object... alsoAtPoint) {
        if (!isAlive()) {
            return Element.EXPLOSION;
        }
        return Element.ai(direction);
    }

    public boolean withPrize() {
        return false;
    }

    public void stop() {
        dontShoot = true;
        dontMove = true;
    }
}