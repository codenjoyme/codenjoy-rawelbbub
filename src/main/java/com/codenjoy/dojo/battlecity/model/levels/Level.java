package com.codenjoy.dojo.battlecity.model.levels;

import com.codenjoy.dojo.battlecity.model.*;
import com.codenjoy.dojo.services.BoardReader;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.LengthToXY;
import com.codenjoy.dojo.services.Point;

import java.util.LinkedList;
import java.util.List;

public class Level implements Field {

    private final LengthToXY xy;

    private String map =
            "☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ " +
            "☼   ¿         ¿         ¿                 ¿         ¿         ¿   ☼ " +
            "☼                                                                 ☼ " +
            "☼     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ☼ " +
            "☼     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ☼ " +
            "☼     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ☼ " +
            "☼     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ☼ " +
            "☼     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬ ☼ ☼ ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ☼ " +
            "☼     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬ ☼ ☼ ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ☼ " +
            "☼     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ☼ " +
            "☼     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ☼ " +
            "☼     ╬ ╬ ╬     ╬ ╬ ╬                         ╬ ╬ ╬     ╬ ╬ ╬     ☼ " +
            "☼     ╬ ╬ ╬     ╬ ╬ ╬                         ╬ ╬ ╬     ╬ ╬ ╬     ☼ " +
            "☼                         ╬ ╬ ╬     ╬ ╬ ╬                         ☼ " +
            "☼                         ╬ ╬ ╬     ╬ ╬ ╬                         ☼ " +
            "☼           ╬ ╬ ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬ ╬ ╬           ☼ " +
            "☼ ☼ ☼       ╬ ╬ ╬ ╬ ╬                         ╬ ╬ ╬ ╬ ╬       ☼ ☼ ☼ " +
            "☼                                                                 ☼ " +
            "☼                         ╬ ╬ ╬     ╬ ╬ ╬                         ☼ " +
            "☼     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ☼ " +
            "☼     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ☼ " +
            "☼     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬ ╬ ╬ ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ☼ " +
            "☼     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬ ╬ ╬ ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ☼ " +
            "☼     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ☼ " +
            "☼     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ☼ " +
            "☼     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ☼ " +
            "☼     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ☼ " +
            "☼     ╬ ╬ ╬                                             ╬ ╬ ╬     ☼ " +
            "☼     ╬ ╬ ╬                                             ╬ ╬ ╬     ☼ " +
            "☼     ╬ ╬ ╬               ╬ ╬ ╬ ╬ ╬ ╬ ╬ ╬               ╬ ╬ ╬     ☼ " +
            "☼     ╬ ╬ ╬               ╬ ╬ ╬ ╬ ╬ ╬ ╬ ╬               ╬ ╬ ╬     ☼ " +
            "☼                         ╬ ╬         ╬ ╬                         ☼ " +
            "☼                         ╬ ╬         ╬ ╬                         ☼ " +
            "☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ";

    public Level() {
        removeSpaces();
        xy = new LengthToXY(size());
    }

    private void removeSpaces() {
        String result = "";
        for (int i = 0; i < map.length(); i += 2) {
            result += map.charAt(i);
        }
        map = result;
    }

    @Override
    public int size() {
        return (int) Math.sqrt(map.length());
    }

    @Override
    public List<Construction> getConstructions() {
        List<Construction> result = new LinkedList<Construction>();
        for (int index = 0; index < map.length(); index++) {
            if (map.charAt(index) == Elements.CONSTRUCTION.ch) {
                result.add(new Construction(xy.getXY(index)));
            }
        }
        return result;
    }

    @Override
    public boolean isBarrier(int x, int y) {
        return false; // do nothing
    }

    @Override
    public boolean outOfField(int x, int y) {
        return false;  // do nothing
    }

    @Override
    public void affect(Bullet bullet) {
        // do nothing
    }

    @Override
    public List<Bullet> getBullets() {
        return new LinkedList<Bullet>(); // do nothing
    }

    @Override
    public BoardReader reader() {
        return new BoardReader() {
            @Override
            public int size() {
                return Level.this.size();
            }

            @Override
            public Iterable<? extends Point> elements() {
                List<Point> result = new LinkedList<Point>();
                result.addAll(Level.this.getBorders());
                result.addAll(Level.this.getBullets());
                result.addAll(Level.this.getConstructions());
                result.addAll(Level.this.getTanks());
                return result;
            }
        };
    }

    @Override
    public List<Tank> getTanks() {
        List<Tank> result = new LinkedList<Tank>();
        for (int index = 0; index < map.length(); index++) {
            if (map.charAt(index) == Elements.AI_TANK_DOWN.ch) {
                Point pt = xy.getXY(index);
                result.add(new AITank(pt.getX(), pt.getY(), Direction.DOWN));
            }
        }
        return result;
    }

    @Override
    public List<Border> getBorders() {
        List<Border> result = new LinkedList<Border>();
        for (int index = 0; index < map.length(); index++) {
            if (map.charAt(index) == Elements.BATTLE_WALL.ch) {
                result.add(new Border(xy.getXY(index)));
            }
        }
        return result;
    }
}
