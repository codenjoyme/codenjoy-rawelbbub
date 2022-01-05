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
import com.codenjoy.dojo.rawelbbub.model.Player;
import com.codenjoy.dojo.rawelbbub.services.GameSettings;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.State;
import com.codenjoy.dojo.services.Tickable;

import java.util.function.Consumer;

import static com.codenjoy.dojo.rawelbbub.services.GameSettings.Keys.PRIZE_SPRITE_CHANGE_TICKS;

public class Prize extends PointImpl implements Tickable, State<Element, Player> {

    private final Element element;
    private final int prizeOnField;
    private final int prizeWorking;

    private boolean active;
    private Consumer<Prize> onDestroy;
    private int timeout;
    private int ticks;
    private GameSettings settings;

    public Prize(Point pt, int prizeOnField, int prizeWorking, Element element) {
        super(pt);
        this.element = element;
        this.prizeOnField = prizeOnField;
        this.prizeWorking = prizeWorking;
        active = true;
    }

    public void init(GameSettings settings) {
        this.settings = settings;
    }

    @Override
    public Element state(Player player, Object... alsoAtPoint) {
        if (ticks % changeEveryTicks() == 0) {
            return Element.PRIZE;
        }

        return element;
    }

    public int changeEveryTicks() {
        return settings.integer(PRIZE_SPRITE_CHANGE_TICKS);
    }

    @Override
    public void tick() {
        if (!active) {
            return;
        }
        if (ticks == timeout) {
            active = false;

            if (onDestroy != null) {
                onDestroy.accept(this);
            }
        } else {
            ticks++;
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

    public void taken(Consumer<Prize> onDestroy) {
        if (this.onDestroy == null) {
            timeout = prizeOnField;
        } else {
            timeout = prizeWorking;
        }
        ticks = 0;
        this.onDestroy = onDestroy;
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