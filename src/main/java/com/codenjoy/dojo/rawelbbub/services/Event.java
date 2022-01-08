package com.codenjoy.dojo.rawelbbub.services;

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


import com.codenjoy.dojo.services.event.EventObject;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;

import static java.util.Arrays.asList;

public class Event implements EventObject<Event.Type, Integer> {

    public static final Event HERO_DIED = new Event(Type.HERO_DIED);
    public static final Function<Integer, Event> KILL_OTHER_HERO = amount -> new Event(Type.KILL_OTHER_HERO, amount);
    public static final Function<Integer, Event> KILL_ENEMY_HERO = amount -> new Event(Type.KILL_ENEMY_HERO, amount);
    public static final Event KILL_AI = new Event(Type.KILL_AI);
    public static final Event START_ROUND = new Event(Type.START_ROUND);
    public static final Event WIN_ROUND = new Event(Type.WIN_ROUND);
    public static final Function<Integer, Event> CATCH_PRIZE = type -> new Event(Type.CATCH_PRIZE, type);

    private Type type;
    private int value;

    enum Type {

        START_ROUND,
        WIN_ROUND,

        KILL_OTHER_HERO,
        KILL_ENEMY_HERO,
        KILL_AI,
        HERO_DIED,

        CATCH_PRIZE
    }

    public Event(Type type) {
        this.type = type;
        this.value = 1;
    }

    public Event(Type type, int value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event events = (Event) o;
        return type == events.type &&
                value == events.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }

    @Override
    public String toString() {
        if (asList(Type.KILL_OTHER_HERO, Type.KILL_ENEMY_HERO, Type.CATCH_PRIZE).contains(type)) {
            return String.format("%s[%s]", type, value);
        }
        return type.name();
    }

    @Override
    public Integer value() {
        return value;
    }

    @Override
    public Type type() {
        return type;
    }
}