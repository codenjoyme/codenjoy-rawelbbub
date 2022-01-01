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


import com.codenjoy.dojo.rawelbbub.services.Event;
import com.codenjoy.dojo.rawelbbub.services.GameSettings;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.event.Calculator;
import com.codenjoy.dojo.services.round.RoundGamePlayer;

public class Player extends RoundGamePlayer<Tank, Field> {

    private Calculator<Integer> calculator;
    private int killed;

    public Player(EventListener listener, GameSettings settings){
        super(listener, settings);
        calculator = settings().calculator();
        reset();
    }

    @Override
    public void start(int round, Object startEvent) {
        super.start(round, startEvent);
        // hero.reset(); // TODO test me
        // reset(); // TODO test me
    }

    public void reset() {
        killed = 0;
    }

    public boolean isDestroyed() {
        return !isAlive();
    }

    public void event(Event event) {
        hero.addScore(calculator.score(event));
        super.event(event);
    }

    public int killHero() {
        return killed++;
    }

    @Override
    public Tank createHero(Point pt) {
        reset();
        return new Tank(pt, Direction.UP);
    }

    public int score() {
        return killed;
    }

    void setKilled(int killed) {
        this.killed = killed;
    }

    private GameSettings settings() {
        return (GameSettings) settings;
    }
}
