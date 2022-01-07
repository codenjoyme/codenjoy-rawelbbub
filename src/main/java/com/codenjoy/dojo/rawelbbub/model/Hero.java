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

import static com.codenjoy.dojo.games.rawelbbub.Element.*;
import static com.codenjoy.dojo.rawelbbub.services.Event.*;
import static com.codenjoy.dojo.rawelbbub.services.GameSettings.Keys.*;
import static com.codenjoy.dojo.rawelbbub.services.GameSettings.MODE_FORWARD_BACKWARD;
import static com.codenjoy.dojo.services.Direction.*;
import static java.util.stream.Collectors.toList;

public class Hero extends RoundPlayerHero<Field> 
        implements RoundsDirectionActJoystick, State<Element, Player> {

    protected Direction direction;
    protected Direction moving;
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

        moving = null;
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
        return (GameSettings) field.settings();
    }

    @Override
    public void change(Direction direction) {
        this.direction = direction;
        moving = UP;
    }

    @Override
    public void act(Act act) {
        fire = true;
    }

    protected void fire() {
        act();
    }

    protected void forward() {
        validateTurnMode();

        moving = UP;
    }

    private void validateTurnMode() {
        if (settings().integer(TURN_MODE) != MODE_FORWARD_BACKWARD) {
            throw new IllegalStateException("Please fix settings:\n" +
                    "\t settings().integer(TURN_MODE, MODE_FORWARD_BACKWARD);");
        }
    }

    protected void backward() {
        validateTurnMode();

        moving = DOWN;
    }

    protected void turnLeft() {
        validateTurnMode();

        moving = LEFT;
    }

    protected void turnRight() {
        validateTurnMode();

        moving = RIGHT;
    }

    @Override
    public void tickHero() {
        gunType();

        gun.tick();
        prizes.tick();

        checkOnFishnet();
    }

    public Direction direction() {
        return direction;
    }

    public void move() {
        boolean willMove = moving != null || field.isOil(this);
        if (!willMove) return;

        if (moving == null) {
            // если занос, то полный ход, куда бы не были направлены
            moving = UP;
        }

        switch (moving) {
            case LEFT:  // поворот налево
                direction = direction.counterClockwise();
                break;

            case RIGHT: // поворот налево
                direction = direction.clockwise();
                break;

            case UP:   // полный ход
                break;

            case DOWN: // задний ход
                direction = direction.inverted();
                break;
        }

        if (sliding.active(this)) {
            direction = sliding.affect(direction);
        } else {
            sliding.reset(direction);
        }

        switch (moving) {
            // повороты не влияют на изменения положения
            case LEFT:
            case RIGHT:
                break;

            // полный ход (в направлении direction)
            case UP:
                moving(direction.change(this));
                break;

            // задний ход (в направлении противоположном direction)
            case DOWN:
                moving(direction.change(this));
                direction = direction.inverted();
                break;
        }

        moving = null;
    }

    public void moving(Point pt) {
        if (field.isBarrierFor(this, pt)) {
            sliding.stop();
        } else {
            move(pt);
        }
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
}