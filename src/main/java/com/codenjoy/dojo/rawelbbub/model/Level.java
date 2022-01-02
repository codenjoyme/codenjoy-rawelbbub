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
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.field.AbstractLevel;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import static com.codenjoy.dojo.games.rawelbbub.Element.*;
import static com.codenjoy.dojo.services.Direction.*;

public class Level extends AbstractLevel {

    public Level(String map) {
        super(map);
    }

    public List<Wall> walls() {
        return find(Wall::new, WALL);
    }

    public List<River> rivers() {
        return find(River::new, RIVER);
    }

    public List<Ice> ice() {
        return find(Ice::new, ICE);
    }

    public List<Tree> trees() {
        return find(Tree::new, TREE);
    }

    public List<Hero> ais() {
        return new LinkedList<>(){{
            addAll(find((pt, el) -> new AI(pt, DOWN),  AI_TANK_DOWN));
            addAll(find((pt, el) -> new AI(pt, UP),    AI_TANK_UP));
            addAll(find((pt, el) -> new AI(pt, LEFT),  AI_TANK_LEFT));
            addAll(find((pt, el) -> new AI(pt, RIGHT), AI_TANK_RIGHT));
        }};
    }

    public List<Hero> heroes() {
        return new LinkedList<>(){{
            addAll(find((pt, el) -> new Hero(pt, DOWN),  TANK_DOWN,  OTHER_TANK_DOWN));
            addAll(find((pt, el) -> new Hero(pt, UP),    TANK_UP,    OTHER_TANK_UP));
            addAll(find((pt, el) -> new Hero(pt, LEFT),  TANK_LEFT,  OTHER_TANK_LEFT));
            addAll(find((pt, el) -> new Hero(pt, RIGHT), TANK_RIGHT, OTHER_TANK_RIGHT));
        }};
    }

    public List<Border> borders() {
        return find(Border::new, BATTLE_WALL);
    }

    @Override
    public void addAll(Consumer<Iterable<? extends Point>> processor) {
        processor.accept(borders());
        processor.accept(walls());
        processor.accept(ais());
        processor.accept(ice());
        processor.accept(rivers());
        processor.accept(trees());
    }
}
