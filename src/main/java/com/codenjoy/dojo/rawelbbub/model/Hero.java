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


import com.codenjoy.dojo.games.rawelbbub.Element;
import com.codenjoy.dojo.rawelbbub.model.items.Bullet;
import com.codenjoy.dojo.rawelbbub.model.items.Prize;
import com.codenjoy.dojo.rawelbbub.model.items.Prizes;
import com.codenjoy.dojo.rawelbbub.services.GameSettings;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.State;
import com.codenjoy.dojo.services.field.PointField;
import com.codenjoy.dojo.services.joystick.Act;
import com.codenjoy.dojo.services.joystick.RoundsDirectionActJoystick;
import com.codenjoy.dojo.services.round.RoundPlayerHero;
import com.codenjoy.dojo.services.round.Timer;

import java.util.List;

import static com.codenjoy.dojo.games.rawelbbub.Element.PRIZE_BREAKING_WALLS;
import static com.codenjoy.dojo.games.rawelbbub.Element.PRIZE_WALKING_ON_FISHNET;
import static com.codenjoy.dojo.rawelbbub.services.Event.CATCH_PRIZE;
import static com.codenjoy.dojo.rawelbbub.services.Event.HERO_DIED;
import static com.codenjoy.dojo.rawelbbub.services.GameSettings.Keys.HERO_TICKS_PER_SHOOT;
import static com.codenjoy.dojo.rawelbbub.services.GameSettings.Keys.PENALTY_WALKING_ON_FISHNET;
import static java.util.stream.Collectors.toList;

public class Hero extends RoundPlayerHero<Field> 
        implements RoundsDirectionActJoystick, State<Element, Player> {

    protected Direction direction;
    protected boolean moving;
    private boolean fire;
    private int score;

    private Gun gun;
    private Sliding sliding;

    private Prizes prizes;

    private Timer onFishnet;
    private Dice dice;

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

        reset();
        setAlive(true);
    }

    public boolean isAI() {
        return false;
    }

    @Override
    public GameSettings settings() {
        return (GameSettings) field.settings();
    }

    @Override
    public void change(Direction direction) {
        this.direction = direction;
        moving = true;
    }

    @Override
    public void act(Act act) {
        fire = true;
    }

    protected void fire() {
        act();
    }

    @Override
    public void tick() {
        // TODO добавить проверку if (!isActiveAndAlive()) return;

        gunType();

        gun.tick();
        prizes.tick();

        checkOnFishnet();
    }

    public Direction getDirection() {
        return direction;
    }

    public void move() {
        moving = moving || field.isOil(this);
        if (!moving) return;

        if (sliding.active(this)) {
            direction = sliding.affect(direction);
        } else {
            sliding.reset(direction);
        }

        moving(direction.change(this));
    }

    public void moving(Point pt) {
        if (field.isBarrierFor(this, pt)) {
            sliding.stop();
        } else {
            move(pt);
        }
        moving = false;
    }

    @Override
    public void die() {
        die(HERO_DIED);
    }

    public List<Bullet> getBullets() {
        return field.bullets().stream()
                .filter(bullet -> bullet.owner() == this)
                .collect(toList());
    }

    protected int ticksPerShoot() {
        return settings().integer(HERO_TICKS_PER_SHOOT);
    }

    public void kill(Bullet bullet) {
        if (isAlive()) {
            die();
        }
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

        if (player.getHero() != this) {
            switch (direction) {
                case LEFT:  return Element.OTHER_HERO_LEFT;
                case RIGHT: return Element.OTHER_HERO_RIGHT;
                case UP:    return Element.OTHER_HERO_UP;
                case DOWN:  return Element.OTHER_HERO_DOWN;
                default:    throw new RuntimeException("Неправильное состояние танка!");
            }
        }

        switch (direction) {
            case LEFT:  return Element.HERO_LEFT;
            case RIGHT: return Element.HERO_RIGHT;
            case UP:    return Element.HERO_UP;
            case DOWN:  return Element.HERO_DOWN;
            default:    throw new RuntimeException("Неправильное состояние танка!");
        }
    }

    public void reset() {
        moving = false;
        fire = false;
        setAlive(true);
        gun.reset();
        prizes.clear();
    }

    public void tryFire() {
        if (!fire) return;
        fire = false;

        if (!gun.tryToFire()) return;

        Direction direction = this.direction;
        if (sliding.active(this) && !sliding.lastSlipperiness()) {
            direction = sliding.getPreviousDirection();
        }
        Bullet bullet = new Bullet(field, direction, copy(), this,
                it -> field.bullets().removeExact(it));

        if (!field.bullets().contains(bullet)) {
            field.bullets().add(bullet);
        }
    }

    public Prizes prizes() {
        return prizes;
    }

    public void take(Prize prize) {
        getPlayer().event(CATCH_PRIZE.apply(prize.value()));
        prizes.add(prize);
    }

    private void gunType() {
        if (prizes.contains(PRIZE_BREAKING_WALLS)) {
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
}
