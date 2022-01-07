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
import com.codenjoy.dojo.rawelbbub.model.Player;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;

import static com.codenjoy.dojo.rawelbbub.services.GameSettings.Keys.KILL_HITS_AI_PRIZE;

public class AIPrize extends AI {

    private int damage;
    private int ticks;
    private boolean wounded;

    public AIPrize(Point pt, Direction direction) {
        super(pt, direction);
        damage = 0;
        ticks = 0;
        wounded = false;
    }

    @Override
    public void init(Field field) {
        super.init(field);

        field.prizeAis().add(this);
    }

    @Override
    public void tickHero() {
        super.tickHero();

        ticks++;
        wounded = false;
    }

    public void die() {
        damage++;
        wounded = true;

        if (damage == vitality()) {
            damage = 0;
            super.die();
        }
    }

    private int vitality() {
        return settings().integer(KILL_HITS_AI_PRIZE);
    }

    @Override
    public Element state(Player player, Object... alsoAtPoint) {
        if (!isAlive() || wounded) {
            return Element.EXPLOSION;
        }
        return Element.aiPrize(direction, settings().isModeSideView());
    }

    @Override
    public boolean withPrize() {
        return true;
    }
}