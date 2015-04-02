package com.codenjoy.dojo.battlecity.model;

import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.PointImpl;

public abstract class MovingObject extends PointImpl {
    protected Direction direction;
    protected int speed;
    protected boolean moving;

    public MovingObject(int x, int y, Direction direction) {
        super(x, y);
        this.direction = direction;
        moving = false;
    }

    public Direction getDirection() {
        return direction;
    }

    public void move() {
        for (int i = 0; i < speed; i++) {
            if (!moving) {
                return;
            }

            int newX = direction.changeX(x);
            int newY = direction.changeY(y);
            moving(newX, newY);
        }
    }

    protected abstract void moving(int newX, int newY);

}
