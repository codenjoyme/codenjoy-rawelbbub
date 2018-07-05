package com.codenjoy.dojo.battlecity.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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


import com.codenjoy.dojo.battlecity.services.Events;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;

public class Player extends GamePlayer<Tank, Field> {

    public static final int TICKS_PER_BULLETS = 4;

    private Tank hero;

    public Player(EventListener listener, Dice dice) {
        super(listener);
        hero = new Tank(0, 0, Direction.UP, dice, TICKS_PER_BULLETS);
    }

    public Tank getHero() {
        return hero;
    }

    @Override
    public boolean isAlive() {
        return hero.isAlive();
    }

    public void event(Events event) {
        switch (event) {
            case KILL_OTHER_TANK: increaseScore(); break;
            case KILL_YOUR_TANK: gameOver(); break;
        }

        super.event(event);
    }

    @Override
    public void gameOver() {
        super.gameOver();
        hero.kill(null);
    }

    public void newHero(Field tanks) {
        hero.removeBullets();
        hero.init(tanks);
    }
}