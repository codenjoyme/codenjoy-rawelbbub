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
import com.codenjoy.dojo.games.rawelbbub.ElementUtils;
import com.codenjoy.dojo.rawelbbub.model.Field;
import com.codenjoy.dojo.rawelbbub.model.Player;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.Tickable;
import com.codenjoy.dojo.services.field.Fieldable;
import com.codenjoy.dojo.services.printer.state.State;
import com.codenjoy.dojo.services.settings.SettingsReader;

import static com.codenjoy.dojo.rawelbbub.services.GameSettings.Keys.ICEBERG_REGENERATE_TIME;

public class Iceberg extends PointImpl implements Tickable, Fieldable<Field>, State<Element, Player> {

    private Element element;
    private int timer;
    private SettingsReader settings;

    public Iceberg(Point pt) {
        super(pt);
        element = Element.ICEBERG_HUGE;
    }

    @Override
    public void init(Field field) {
        settings = field.settings();
    }

    public boolean affect(Torpedo torpedo) {
        if (destroyed()) {
            return false;
        }

        if (torpedo.isHeavy()) {
            element = Element.WATER;
        } else {
            element = ElementUtils.destroyFrom(element, torpedo.direction().inverted());
        }
        timer = 0;
        return true;
    }

    @Override
    public Element state(Player player, Object... alsoAtPoint) {
        return element;
    }

    @Override
    public void tick() {
        if (element == Element.ICEBERG_HUGE) {
            timer = 0;
            return;
        }

        timer++;
        if (timer % settings.integer(ICEBERG_REGENERATE_TIME) == 0) {
            element = ElementUtils.grow(element);
        }
    }

    public boolean destroyed() {
        return ElementUtils.power.get(element) == 0;
    }

    @Override
    public String toString() {
        return String.format("Iceberg[%s='%s',timer=%s,destroyed=%s]",
                super.toString(),
                element.ch(),
                timer,
                destroyed());
    }
}