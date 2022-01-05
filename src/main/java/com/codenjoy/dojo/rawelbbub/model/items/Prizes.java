package com.codenjoy.dojo.rawelbbub.model.items;

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
import com.codenjoy.dojo.rawelbbub.model.Hero;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.Tickable;
import com.codenjoy.dojo.services.field.Accessor;
import com.codenjoy.dojo.services.field.PointField;

import java.util.Objects;

import static java.util.stream.Collectors.toList;

public class Prizes implements Tickable {

    private PointField field;

    public Prizes(PointField field) {
        this.field = field;
    }

    @Override
    public void tick() {
        prizes().forEach(Prize::tick);
        removeDead();
    }

    private Accessor<Prize> prizes() {
        return field.of(Prize.class);
    }

    public void takeBy(Hero hero) {
        Prize prize = prizes().getFirstAt(hero);
        if (prize == null) return;

        hero.take(prize);
        prizes().removeExact(prize);
    }

    private void removeDead() {
        prizes().removeIf(Prize::destroyed);
    }

    public Prize prizeAt(Point pt) {
        return prizes().getFirstAt(pt);
    }

    public void add(Prize prize, boolean onField) {
        prizes().add(prize);
        prize.start(onField);
    }

    public boolean affect(Torpedo torpedo) {
        Prize prize = prizeAt(torpedo);
        if (prize != null) {
            prize.kill();
            return true;
        }
        return false;
    }

    public boolean contains(Element elements) {
        return prizes().stream()
                .anyMatch(prize -> elements.equals(prize.element()));
    }

    public int size() {
        return prizes().size();
    }

    public void clear() {
        prizes().clear();
    }

    @Override
    public String toString() {
        return prizes().stream()
                .map(Objects::toString)
                .collect(toList())
                .toString();
    }
}