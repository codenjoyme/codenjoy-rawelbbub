package com.codenjoy.dojo.rawelbbub.model.items;

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


import com.codenjoy.dojo.games.rawelbbub.Element;
import com.codenjoy.dojo.rawelbbub.model.Field;
import com.codenjoy.dojo.rawelbbub.model.Player;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.field.Fieldable;
import com.codenjoy.dojo.services.settings.SettingsReader;

import static com.codenjoy.dojo.rawelbbub.services.GameSettings.Keys.ICEBERG_REGENERATE_TIME;
import static com.codenjoy.dojo.services.Direction.*;

public class Iceberg extends PointImpl implements Tickable, Fieldable<Field>, State<Element, Player> {

    private Element ch;
    private int timer;
    private boolean overDamage;
    private SettingsReader settings;

    public Iceberg(Point pt) {
        super(pt);
        reset();
        overDamage = false;
    }

    @Override
    public void init(Field field) {
        settings = field.settings();
    }

    public void destroy(Bullet bullet) {
        if (bullet.isHeavy()) {
            overDamage = true;
        }

        destroyFrom(bullet.getDirection());
    }

    public void destroyFrom(Direction bulletDirection) {
        if (ch.power() == 1 || overDamage) {
            ch = Element.ICEBERG_DESTROYED;
            return;
        }
        if (bulletDirection.equals(UP)) {
            switch (ch) {
                case ICEBERG_HUGE: ch = Element.ICEBERG_MEDIUM_DOWN; break;
                case ICEBERG_MEDIUM_DOWN: ch = Element.ICEBERG_SMALL_DOWN_DOWN; break;
                case ICEBERG_MEDIUM_UP: ch = Element.ICEBERG_SMALL_UP_DOWN; break;
                case ICEBERG_MEDIUM_LEFT: ch = Element.ICEBERG_SMALL_DOWN_LEFT; break;
                case ICEBERG_MEDIUM_RIGHT: ch = Element.ICEBERG_SMALL_DOWN_RIGHT; break;
            }
        } else if (bulletDirection.equals(RIGHT)) {
            switch (ch) {
                case ICEBERG_HUGE: ch = Element.ICEBERG_MEDIUM_LEFT; break;
                case ICEBERG_MEDIUM_LEFT: ch = Element.ICEBERG_SMALL_LEFT_LEFT; break;
                case ICEBERG_MEDIUM_RIGHT: ch = Element.ICEBERG_SMALL_LEFT_RIGHT; break;
                case ICEBERG_MEDIUM_UP: ch = Element.ICEBERG_SMALL_UP_LEFT; break;
                case ICEBERG_MEDIUM_DOWN: ch = Element.ICEBERG_SMALL_DOWN_LEFT; break;
            }
        } else if (bulletDirection.equals(LEFT)) {
            switch (ch) {
                case ICEBERG_HUGE: ch = Element.ICEBERG_MEDIUM_RIGHT; break;
                case ICEBERG_MEDIUM_RIGHT: ch = Element.ICEBERG_SMALL_RIGHT_RIGHT; break;
                case ICEBERG_MEDIUM_UP: ch = Element.ICEBERG_SMALL_UP_RIGHT; break;
                case ICEBERG_MEDIUM_DOWN: ch = Element.ICEBERG_SMALL_DOWN_RIGHT; break;
                case ICEBERG_MEDIUM_LEFT: ch = Element.ICEBERG_SMALL_LEFT_RIGHT; break;
            }
        } else if (bulletDirection.equals(DOWN)) {
            switch (ch) {
                case ICEBERG_HUGE: ch = Element.ICEBERG_MEDIUM_UP; break;
                case ICEBERG_MEDIUM_UP: ch = Element.ICEBERG_SMALL_UP_UP; break;
                case ICEBERG_MEDIUM_RIGHT: ch = Element.ICEBERG_SMALL_UP_RIGHT; break;
                case ICEBERG_MEDIUM_DOWN: ch = Element.ICEBERG_SMALL_UP_DOWN; break;
                case ICEBERG_MEDIUM_LEFT: ch = Element.ICEBERG_SMALL_UP_LEFT; break;
            }
        }
    }

    @Override
    public Element state(Player player, Object... alsoAtPoint) {
        if (!destroyed()) {
            return ch;
        } else  {
            return Element.WATER;
        }
    }

    @Override
    public void tick() {
        if (timer == settings.integer(ICEBERG_REGENERATE_TIME)) {
            timer = 0;
            reset();
        }
        if (destroyed()) {
            timer++;
        }
    }

    public void reset() {
        ch = Element.ICEBERG_HUGE;
    }

    public boolean destroyed() {
        return ch.power() == 0;
    }
}
