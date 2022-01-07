package com.codenjoy.dojo.rawelbbub.model;

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
import com.codenjoy.dojo.rawelbbub.model.items.Gun;
import com.codenjoy.dojo.rawelbbub.model.items.oil.Sliding;
import com.codenjoy.dojo.rawelbbub.model.items.prize.Prize;
import com.codenjoy.dojo.rawelbbub.model.items.Torpedo;
import com.codenjoy.dojo.rawelbbub.model.items.prize.Prizes;
import com.codenjoy.dojo.rawelbbub.services.GameSettings;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.field.PointField;
import com.codenjoy.dojo.services.joystick.Act;
import com.codenjoy.dojo.services.joystick.RoundsDirectionActJoystick;
import com.codenjoy.dojo.services.round.RoundPlayerHero;
import com.codenjoy.dojo.services.round.Timer;
import com.codenjoy.dojo.services.route.Route;
import com.codenjoy.dojo.services.route.RouteProcessor;

import java.util.List;

import static com.codenjoy.dojo.games.rawelbbub.Element.*;
import static com.codenjoy.dojo.rawelbbub.services.Event.*;
import static com.codenjoy.dojo.rawelbbub.services.GameSettings.Keys.*;
import static com.codenjoy.dojo.rawelbbub.services.GameSettings.MODE_FORWARD_BACKWARD;
import static com.codenjoy.dojo.services.route.Route.FORWARD;
import static java.util.stream.Collectors.toList;

public class Hero extends RoundPlayerHero<Field> 
        implements RoundsDirectionActJoystick, State<Element, Player>,
                   RouteProcessor {

    // ориентация героя по сторонам света
    protected Direction direction;

    // команда герою
    protected Route route;

    private boolean fire;
    private int score;

    private Gun gun;
    private Sliding sliding;

    private Prizes prizes;

    private Timer onFishnet;
    private Dice dice;
    private int killed;

    public Hero(Point pt, Direction direction) {
        super(pt);
        score = 0;
        this.direction = direction;
    }

    @Override
    public void init(Field field) {
        super.init(field);

        if (!isAI()) {
            field.heroes().add(this);
        }

        dice(field.dice());
        gun = new Gun(settings());
        sliding = new Sliding(field, direction, settings());
        prizes = new Prizes(new PointField().size(field.size()));

        route(null);
        fire = false;
        setAlive(true);
        gun.reset();
        prizes.clear();
        killed = 0;
        setAlive(true);
    }

    public boolean isAI() {
        return false;
    }

    @Override
    public GameSettings settings() {
        return field.settings();
    }

    @Override
    public void change(Direction direction) {
        this.direction = direction;
        route(FORWARD);
    }

    @Override
    public void act(Act act) {
        fire = true;
    }

    protected void fire() {
        act();
    }

    @Override
    public void validateTurnModeEnabled() {
        if (settings().integer(TURN_MODE) != MODE_FORWARD_BACKWARD) {
            throw new IllegalStateException("Please fix settings:\n" +
                    "\t settings().integer(TURN_MODE, MODE_FORWARD_BACKWARD);");
        }
    }

    @Override
    public void tickHero() {
        gunType();

        gun.tick();
        prizes.tick();

        checkOnFishnet();
    }

    @Override
    public boolean isSliding() {
        return field.isOil(this);
    }

    @Override
    public boolean canMove(Point pt) {
        if (field.isBarrierFor(this, pt)) {
            sliding.stop();
            return false;
        }

        return true;
    }

    @Override
    public void beforeMove() {
        if (sliding.active(this)) {
            direction = sliding.affect(direction);
        } else {
            sliding.reset(direction);
        }
    }

    @Override
    public void doMove(Point pt) {
        move(pt);
    }

    @Override
    public void die() {
        die(HERO_DIED);
    }

    public List<Torpedo> torpedoes() {
        return field.torpedoes().stream()
                .filter(torpedo -> torpedo.owner() == this)
                .collect(toList());
    }

    protected int ticksPerShoot() {
        return settings().integer(HERO_TICKS_PER_SHOOT);
    }

    public void checkOnFishnet() {
        if (field.isFishnet(this) && !prizes.contains(PRIZE_WALKING_ON_FISHNET)) {
            if (onFishnet == null || onFishnet.done()) {
                onFishnet = new Timer(settings().integerValue(PENALTY_WALKING_ON_FISHNET));
                onFishnet.start();
            }
            onFishnet.tick(() -> {});
        } else {
            onFishnet = null;
        }
    }

    @Override
    public Element state(Player player, Object... alsoAtPoint) {
        if (!isAlive()) {
            return Element.EXPLOSION;
        }

        return player.getHero() == this
                ? Element.hero(direction)
                : Element.otherHero(direction);
    }

    public void tryFire() {
        if (!fire) return;
        fire = false;

        if (!gun.tryToFire()) return;

        Direction direction = this.direction;
        if (sliding.active(this) && !sliding.lastSlipperiness()) {
            direction = sliding.previousDirection();
        }
        Torpedo torpedo = new Torpedo(field, direction, this, this);

        if (!field.torpedoes().contains(torpedo)) {
            field.torpedoes().add(torpedo);
        }
    }

    public Prizes prizes() {
        return prizes;
    }

    public void take(Prize prize) {
        getPlayer().event(CATCH_PRIZE.apply(prize.value()));
        prizes.add(prize, false);
    }

    private void gunType() {
        if (prizes.contains(PRIZE_BREAKING_BAD)) {
            gun.reset();
        }
    }

    public boolean canWalkOnFishnet() {
        return prizes.contains(PRIZE_WALKING_ON_FISHNET)
                || (onFishnet != null && onFishnet.done());
    }

    @Override
    public int scores() {
        return score;
    }

    public void addScore(int added) {
        score = Math.max(0, score + added);
    }

    public Dice dice() {
        return dice;
    }

    public void dice(Dice dice) {
        this.dice = dice;
    }

    public void killHero() {
        killed++;
        getPlayer().event(KILL_OTHER_HERO.apply(killed));
    }

    public void killed(int killed) {
        this.killed = killed;
    }

    public int killed() {
        return killed;
    }

    public boolean affect(Torpedo torpedo) {
        Hero hunter = torpedo.owner();
        if (this == hunter) {
            return false;
        }

        if (!prizes().contains(PRIZE_IMMORTALITY)) {
            // если у героя (или ai) нет приза бессмертия, то ему суждено на покой
            die();
        }

        if (isAlive()) {
            // нас таки подстрелили, но мы живы
            return true;
        }

        // если мы не живы больше, то охотнику положен приз
        if (field.hasPlayer(hunter.getPlayer())) {
            hunter.kill(this);
        }

        return true;
    }

    public void kill(Hero prey) {
        if (prey.isAI()) {
            event(KILL_AI);
        } else {
            killHero();
        }
    }

    @Override
    public Direction direction() {
        return direction;
    }

    @Override
    public void direction(Direction direction) {
        this.direction = direction;
    }

    @Override
    public void route(Route route) {
        this.route = route;
    }

    @Override
    public Route route() {
        return route;
    }
}