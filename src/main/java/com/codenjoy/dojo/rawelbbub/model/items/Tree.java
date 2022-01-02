package com.codenjoy.dojo.rawelbbub.model.items;

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

import com.codenjoy.dojo.games.rawelbbub.Element;
import com.codenjoy.dojo.rawelbbub.model.Hero;
import com.codenjoy.dojo.rawelbbub.model.Player;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.State;

import static com.codenjoy.dojo.rawelbbub.services.GameSettings.Keys.SHOW_MY_HERO_UNDER_TREE;
import static com.codenjoy.dojo.services.StateUtils.filterOne;

public class Tree extends PointImpl implements State<Element, Player> {

    public Tree(Point pt) {
        super(pt);
    }

    @Override
    public Element state(Player player, Object... alsoAtPoint) {
        Hero observer = player.getHero();

        Prize prize = filterOne(alsoAtPoint, Prize.class);
        if (prize != null) {
            return prize.state(player, alsoAtPoint);
        }

        Hero hero = filterOne(alsoAtPoint, Hero.class);
        if (hero != null && !hero.isAlive()) {
            return null;
        }

        if (observer.prizes().contains(Element.PRIZE_VISIBILITY)) {
            AI ai = filterOne(alsoAtPoint, AI.class);
            Bullet bullet = filterOne(alsoAtPoint, Bullet.class);
            if (hero != null || ai != null || bullet != null) {
                return null;
            }
        }

        if (hero == observer
                && observer.settings().bool(SHOW_MY_HERO_UNDER_TREE))
        {
            return null;
        }

        return Element.TREE;
    }
}
