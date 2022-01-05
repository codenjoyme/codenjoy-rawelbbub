package com.codenjoy.dojo.rawelbbub.model.items.prize;

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
import com.codenjoy.dojo.rawelbbub.services.GameSettings;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.State;
import com.codenjoy.dojo.services.Tickable;

import static com.codenjoy.dojo.rawelbbub.services.GameSettings.Keys.*;

public class Prize extends PointImpl implements Tickable, State<Element, Player> {

    private final Element element;
    private boolean active;
    private int timeout;
    private int ticks;
    private GameSettings settings;

    public Prize(Point pt, Field field) {
        super(pt);
        this.settings = (GameSettings) field.settings();
        this.element = settings.chance(field.dice()).any();
        active = true;
    }

    @Override
    public Element state(Player player, Object... alsoAtPoint) {
        if (ticks % changeEveryTicks() == 0) {
            return Element.PRIZE;
        }

        return element;
    }

    private int changeEveryTicks() {
        return settings.integer(PRIZE_SPRITE_CHANGE_TICKS);
    }

    private int prizeWorking() {
        return settings.integer(PRIZE_WORKING);
    }

    private int prizeOnField() {
        return settings.integer(PRIZE_ON_FIELD);
    }

    @Override
    public void tick() {
        if (!active) {
            return;
        }
        if (ticks != timeout) {
            ticks++;
        } else {
            kill();
        }
    }

    public boolean destroyed() {
        return !active;
    }

    public void kill() {
        active = false;
    }

    public Element element() {
        return element;
    }

    public void start(boolean onField) {
        timeout = onField ? prizeOnField() : prizeWorking();
        ticks = 0;
    }

    public int value() {
        return Integer.parseInt(String.valueOf(element.ch()));
    }

    @Override
    public String toString() {
        return String.format("%s(%s/%s)",
                element.name(),
                ticks,
                timeout);
    }
}