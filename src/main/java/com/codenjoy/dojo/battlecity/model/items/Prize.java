package com.codenjoy.dojo.battlecity.model.items;

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


import com.codenjoy.dojo.battlecity.model.Elements;
import com.codenjoy.dojo.battlecity.model.Player;
import com.codenjoy.dojo.services.*;

import static com.codenjoy.dojo.services.StateUtils.filterOne;


public class Prize extends PointImpl implements Tickable, State<Elements, Player> {

    public static final int CHANGE_EVERY_TICKS = 2;
    private Elements elements;
    private Elements prevElements;
    private int prizeOnField;
    private int timer;

    public Prize(Point pt, int prizeOnField, Elements elements) {
        super(pt);
        this.elements = elements;
        this.prizeOnField = prizeOnField;
        timer = 0;
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        Tree tree = filterOne(alsoAtPoint, Tree.class);
        Ice ice = filterOne(alsoAtPoint, Ice.class);
        prevElements = getElements(tree, ice);

        if (timer % CHANGE_EVERY_TICKS == 0) {
            return Elements.PRIZE;
        }

        return elements;
    }

    private Elements getElements(Tree tree, Ice ice) {
        if (tree != null) {
            return Elements.TREE;
        }

        if (ice != null) {
            return Elements.ICE;
        }

        return Elements.NONE;
    }

    @Override
    public void tick() {
        if (timer == prizeOnField) {
            timer = 0;
            elements = prevElements;
        }

        timer++;
    }
}