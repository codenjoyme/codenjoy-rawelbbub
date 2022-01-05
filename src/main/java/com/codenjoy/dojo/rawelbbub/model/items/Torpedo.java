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
import com.codenjoy.dojo.rawelbbub.model.Field;
import com.codenjoy.dojo.rawelbbub.model.Hero;
import com.codenjoy.dojo.rawelbbub.model.Player;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.MovingObject;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.State;

import java.util.function.Consumer;

public class Torpedo extends MovingObject implements State<Element, Player> {

    private final Field field;
    private Hero owner;
    private Consumer<Torpedo> onDestroy;
    private boolean heavy;
    private int tick;

    public Torpedo(Field field, Direction direction,
                   Point pt, Hero owner,
                   Consumer<Torpedo> onDestroy)
    {
        super(pt, direction);
        this.field = field;
        this.owner = owner;
        moving = true;
        this.onDestroy = onDestroy;
        speed = 2;
        heavy = false;
    }

    public void remove() {
        moving = false;
        if (onDestroy != null) {
            onDestroy.accept(this);
        }
    }

    @Override
    public void moving(Point pt) {
        tick++;
        if (pt.isOutOf(field.size())) {
            remove(); // TODO заимплементить взрыв
        } else {
            move(pt);
            field.affect(this);
        }
    }

    public Hero owner() {
        return owner;
    }

    public void boom() {
        moving = false;
        owner = null;
    }

    public boolean destroyed() {
        return owner == null;
    }

    public boolean justFired() {
        return tick == 0;
    }

    @Override
    public Element state(Player player, Object... alsoAtPoint) {
        if (destroyed()) {
            return Element.EXPLOSION;
        } else {
            return Element.TORPEDO;
        }
    }

    public void heavy() {
        heavy = true;
    }

    public boolean isHeavy() {
        return heavy;
    }

    public boolean affect(Torpedo torpedo) {
        return torpedo != this
                && torpedo.equals(this)
                && !this.justFired();
    }
}
