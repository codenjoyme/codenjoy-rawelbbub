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


import com.codenjoy.dojo.rawelbbub.model.items.*;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.field.Accessor;
import com.codenjoy.dojo.services.round.RoundGameField;

import java.util.Optional;

public interface Field extends RoundGameField<Player, Hero> {

    boolean isBarrier(Point pt);

    void affect(Bullet bullet);

    boolean isRiver(Point pt);

    boolean isTree(Point pt);

    boolean isOil(Point pt);

    void add(Prize prize);

    Accessor<Hero> heroes();

    Accessor<Prize> prizes();

    Accessor<Tree> trees();

    Accessor<Oil> oil();

    Accessor<River> rivers();

    Accessor<Wall> walls();

    Accessor<Reefs> reefs();

    Accessor<AI> ais();

    int size();

    boolean isBarrierFor(Hero hero, Point pt);

    boolean isFree(Point pt);

    Optional<Point> freeRandom(Player player);

    Accessor<AIPrize> prizeAis();

    Accessor<Bullet> bullets();

    Dice dice();
}
