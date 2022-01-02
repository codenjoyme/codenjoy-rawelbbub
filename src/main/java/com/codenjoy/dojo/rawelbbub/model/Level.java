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
import com.codenjoy.dojo.services.field.AbstractLevel;
import com.codenjoy.dojo.services.field.PointField;

import java.util.LinkedList;
import java.util.List;

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
            addAll(find((pt, el) -> new AI(pt, DOWN), AI_DOWN));
            addAll(find((pt, el) -> new AI(pt, UP), AI_UP));
            addAll(find((pt, el) -> new AI(pt, LEFT), AI_LEFT));
            addAll(find((pt, el) -> new AI(pt, RIGHT), AI_RIGHT));
        }};
    }

    public List<Hero> heroes() {
        return new LinkedList<>(){{
            addAll(find((pt, el) -> new Hero(pt, DOWN), HERO_DOWN, OTHER_HERO_DOWN));
            addAll(find((pt, el) -> new Hero(pt, UP), HERO_UP, OTHER_HERO_UP));
            addAll(find((pt, el) -> new Hero(pt, LEFT), HERO_LEFT, OTHER_HERO_LEFT));
            addAll(find((pt, el) -> new Hero(pt, RIGHT), HERO_RIGHT, OTHER_HERO_RIGHT));
        }};
    }

    public List<Border> borders() {
        return find(Border::new, BATTLE_WALL);
    }

    @Override
    protected void fill(PointField field) {
        field.addAll(borders());
        field.addAll(walls());
        // TODO это делается не тут, а позже потому что в каждой
        //      такой точке может возникнуть как простой AI так и призовой
        // field.addAll(ais());
        field.addAll(ice());
        field.addAll(rivers());
        field.addAll(trees());
    }
}