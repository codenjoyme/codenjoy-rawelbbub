package com.codenjoy.dojo.battlecity.model;

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


import com.codenjoy.dojo.battlecity.model.items.*;
import com.codenjoy.dojo.battlecity.services.Events;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.services.settings.Parameter;

import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.battlecity.model.Elements.*;
import static java.util.stream.Collectors.toList;

public class Battlecity implements Field {

    private int size;

    private PrizeGenerator prizeGen;
    private AiGenerator aiGen;

    private List<Player> players;

    private List<Wall> walls;
    private List<Border> borders;
    private List<Tree> trees;
    private List<Ice> ice;
    private List<River> rivers;
    private Prizes prizes;
    private List<Tank> ais;

    public Battlecity(int size, Dice dice,
                      Parameter<Integer> whichSpawnWithPrize,
                      Parameter<Integer> damagesBeforeAiDeath,
                      Parameter<Integer> prizeOnField,
                      Parameter<Integer> prizeWorking)
    {
        this.size = size;
        ais = new LinkedList<>();
        prizes = new Prizes();
        walls = new LinkedList<>();
        borders = new LinkedList<>();
        trees = new LinkedList<>();
        ice = new LinkedList<>();
        rivers = new LinkedList<>();
        players = new LinkedList<>();

        prizeGen = new PrizeGenerator(this, dice,
                prizeOnField,
                prizeWorking);

        aiGen = new AiGenerator(this, dice,
                whichSpawnWithPrize,
                damagesBeforeAiDeath);
    }

    public void addAiTanks(List<? extends Point> tanks) {
        aiGen.dropAll(tanks);
    }

    @Override
    public void clearScore() {
        players.forEach(Player::reset);
        walls.forEach(Wall::reset);
        allTanks().forEach(Tank::reset);
    }

    @Override
    public void tick() {
        removeDeadItems();

        aiGen.dropAll();

        List<Tank> tanks = allTanks();

        for (Tank tank : tanks) {
            tank.tick();
        }

        for (Bullet bullet : bullets()) {
            if (bullet.destroyed()) {
                bullet.remove();
            }
        }

        for (Tank tank : tanks) {
            if (tank.isAlive()) {
                tank.tryFire();
            }

            if (tank.prizes().contains(PRIZE_BREAKING_WALLS)) {
                tank.getBullets().stream().forEach(Bullet::heavy);
            }
        }

        for (Tank tank : tanks) {
            if (tank.isAlive()) {
                tank.move();

                List<Bullet> bullets = bullets();
                int index = bullets.indexOf(tank);
                if (index != -1) {
                    Bullet bullet = bullets.get(index);
                    affect(bullet);
                }
            }
        }

        for (Bullet bullet : bullets()) {
            bullet.move();
        }

        for (Wall wall : walls) {
            if (!tanks.contains(wall) && !bullets().contains(wall)) {
                wall.tick();
            }
        }

        prizes.tick();

        for (Player player : players) {
            if (player.isAlive()) {
                prizes.takeBy(player.getHero());
            }
        }
    }

    private void removeDeadItems() {
        removeDeadAi();
        players.removeIf(Player::isDestroyed);
        prizes.removeDead();
    }

    private void removeDeadAi() {
        List<Tank> dead = ais.stream()
                .filter(Tank::isDestroyed)
                .collect(toList());
        ais.removeAll(dead);
        dead.stream()
            .filter(Tank::isTankPrize)
            .forEach(tank -> prizeGen.drop(tank));
    }

    @Override
    public void affect(Bullet bullet) {
        if (borders.contains(bullet)) {
            bullet.remove();
            return;
        }

        if (allTanks().contains(bullet)) {
            int index = allTanks().indexOf(bullet);
            Tank tank = allTanks().get(index);
            if (tank == bullet.getOwner()) {
                return;
            }

            if (!tank.prizes().contains(PRIZE_IMMORTALITY)) {
                tank.kill(bullet);
            }

            if (!tank.isAlive()) {
                scoresForKill(bullet, tank);
            }

            bullet.remove();  // TODO заимплементить взрыв
            return;
        }

        for (Bullet bullet2 : bullets().toArray(new Bullet[0])) {
            if (bullet != bullet2 && bullet.equals(bullet2)) {
                bullet.boom();
                bullet2.boom();
                return;
            }
        }

        if (walls.contains(bullet)) {
            Wall wall = getWallAt(bullet);

            if (!wall.destroyed()) {
                wall.destroy(bullet);
                bullet.remove();  // TODO заимплементить взрыв
            }

            return;
        }

        if (prizes.affect(bullet)) {
            bullet.remove();
            return;
        }
    }

    @Override
    public boolean isRiver(Point pt) {
        return rivers.stream().anyMatch(river -> river.itsMe(pt));
    }

    @Override
    public boolean isIce(Point pt) {
        return ice.stream().anyMatch(ice -> ice.itsMe(pt));
    }

    @Override
    public void add(Prize prize) {
        prizes.add(prize);
    }

    @Override
    public void addAi(Tank tank) {
        ais.add(tank);
    }

    private Wall getWallAt(Point pt) {
        int index = walls.indexOf(pt);
        return walls.get(index);
    }

    private void scoresForKill(Bullet killedBullet, Tank diedTank) {
        Player died = null;
        boolean aiDied = ais.contains(diedTank);
        if (!aiDied) {
             died = getPlayer(diedTank);
        }

        Tank killerTank = killedBullet.getOwner();
        Player killer = null;
        if (!ais.contains(killerTank)) {
            killer = getPlayer(killerTank);
        }

        if (killer != null) {
            if (aiDied) {
                killer.event(Events.KILL_OTHER_AI_TANK);
            } else {
                killer.killHero();
                killer.event(Events.KILL_OTHER_HERO_TANK.apply(killer.score()));
            }
        }

        if (died != null) {
            died.event(Events.KILL_YOUR_TANK);
        }
    }

    private Player getPlayer(Tank tank) {
        for (Player player : players) {
            if (player.getHero().equals(tank)) {
                return player;
            }
        }

        throw new RuntimeException("Танк игрока не найден!");
    }

    @Override
    public boolean isBarrierFor(Tank tank, Point pt) {
        if (isBarrier(pt)) {
            return true;
        }

        if (isRiver(pt)) {
            if (tank.prizes().contains(PRIZE_WALKING_ON_WATER)) {
                return false;
            }
            return true;
        }

        return false;
    }

    @Override
    public boolean isBarrier(Point pt) {
        for (Wall wall : this.walls) {
            if (wall.itsMe(pt) && !wall.destroyed()) {
                return true;
            }
        }

        for (Point border : borders) {
            if (border.itsMe(pt)) {
                return true;
            }
        }

        for (Tank tank : allTanks()) {   //  TODO проверить как один танк не может проходить мимо другого танка игрока (не AI)
            if (tank.itsMe(pt)) {
                return true;
            }
        }

        return pt.isOutOf(size);
    }

    private List<Bullet> bullets() {
        return allTanks().stream()
                .flatMap(tank -> tank.getBullets().stream())
                .collect(toList());
    }

    @Override
    public List<Tank> aiTanks() {
        return ais;
    }

    public List<Tank> allTanks() {
        List<Tank> result = new LinkedList<>(ais);
        for (Player player : players) {
//            if (player.getTank().isAlive()) { // TODO разремарить с тестом
                result.add(player.getHero());
//            }
        }
        return result;
    }

    public List<Tank> tanks() {
        return players.stream()
                .map(Player::getHero)
                .collect(toList());
    }

    public List<Prize> prizes() {
        return prizes.all();
    }

    @Override
    public void remove(Player player) {   // TODO test me
        players.remove(player);
    }

    @Override
    public void newGame(Player player) {
        if (!players.contains(player)) {
            players.add(player);
        }
        player.newHero(this);
    }

    public int size() {
        return size;
    }

    @Override
    public BoardReader reader() {
        return new BoardReader() {
            private int size = Battlecity.this.size;

            @Override
            public int size() {
                return size;
            }

            @Override
            public Iterable<? extends Point> elements() {
                return new LinkedList<Point>() {{
                    addAll(Battlecity.this.getBorders());
                    addAll(Battlecity.this.allTanks());
                    addAll(Battlecity.this.getWalls());
                    addAll(Battlecity.this.bullets());
                    addAll(Battlecity.this.prizes());
                    addAll(Battlecity.this.getTrees());
                    addAll(Battlecity.this.getIce());
                    addAll(Battlecity.this.getRivers());
                }};
            }
        };
    }

    public List<Wall> getWalls() {
        List<Wall> result = new LinkedList<>();
        for (Wall wall : walls) {
            if (!wall.destroyed()) {
                result.add(wall);
            }
        }
        return result;
    }

    public List<Tree> getTrees() {
        return trees;
    }

    public List<Ice> getIce() {
        return ice;
    }

	public List<River> getRivers() {
		return rivers;
	}

    public List<Border> getBorders() {
        return borders;
    }


    public void addBorder(List<Border> borders) {
        this.borders.addAll(borders);
    }

    public void addTree(Tree tree) {
        trees.add(tree);
    }

    public void addBorder(Border border) {
        borders.add(border);
    }

    public void addIce(Ice ice) {
        this.ice.add(ice);
    }

    public void addRiver(River river) {
        rivers.add(river);
    }

    public AiGenerator getAiGenerator() {
        return aiGen;
    }

    public void addWall(List<Wall> walls) {
        this.walls.addAll(walls);
    }

    public void addWall(Wall wall) {
        walls.add(wall);
    }

    public void addRiver(List<River> rivers) {
        this.rivers.addAll(rivers);
    }

    public void addTree(List<Tree> trees) {
        this.trees.addAll(trees);
    }

    public void add(List<Ice> ice, Parameter<Integer> sliding) {
        this.ice.addAll(ice);
        Sliding.slidingValue = sliding.getValue();
    }
}
