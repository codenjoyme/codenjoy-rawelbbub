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


import com.codenjoy.dojo.rawelbbub.model.items.*;
import com.codenjoy.dojo.rawelbbub.services.Event;
import com.codenjoy.dojo.rawelbbub.services.GameSettings;
import com.codenjoy.dojo.services.BoardUtils;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.services.round.RoundField;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static com.codenjoy.dojo.games.rawelbbub.Element.PRIZE_BREAKING_WALLS;
import static com.codenjoy.dojo.games.rawelbbub.Element.PRIZE_IMMORTALITY;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toList;

public class Rawelbbub extends RoundField<Player> implements Field {

    private Dice dice;
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
    private List<Hero> ais;

    private GameSettings settings;

    public Rawelbbub(int size, Dice dice, GameSettings settings) {
        super(Event.START_ROUND, Event.WIN_ROUND, settings);
        this.size = size;
        this.dice = dice;
        this.settings = settings;
        ais = new LinkedList<>();
        prizes = new Prizes();
        walls = new LinkedList<>();
        borders = new LinkedList<>();
        trees = new LinkedList<>();
        ice = new LinkedList<>();
        rivers = new LinkedList<>();
        players = new LinkedList<>();
        prizeGen = new PrizeGenerator(this, dice, settings);
        aiGen = new AiGenerator(this, dice, settings);
    }

    @Override
    protected List<Player> players() {
        return players;
    }

    @Override
    public void cleanStuff() {
        removeDeadItems();
    }


    public void addAis(List<? extends Point> ais) {
        aiGen.dropAll(ais);
    }

    @Override
    public void clearScore() {
        players.forEach(Player::reset);
        walls.forEach(Wall::reset);
        heroesAndAis().forEach(Hero::reset);
    }

    @Override
    public void tickField() {
        aiGen.allHave(withPrize());
        aiGen.dropAll();

        List<Hero> tanks = heroesAndAis();

        for (Hero tank : tanks) {
            tank.tick();
        }

        for (Bullet bullet : bullets()) {
            if (bullet.destroyed()) {
                bullet.remove();
            }
        }

        for (Hero tank : tanks) {
            if (tank.isAlive()) {
                tank.tryFire();
            }

            if (tank.prizes().contains(PRIZE_BREAKING_WALLS)) {
                tank.getBullets().forEach(Bullet::heavy);
            }
        }

        for (Hero tank : tanks) {
            if (tank.isAlive()) {
                tank.move();

                List<Bullet> bullets = bullets();
                int index = bullets.indexOf(tank);
                if (index != -1) {
                    Bullet bullet = bullets.get(index);
                    if (bullet.getTick() != 0) {
                        affect(bullet);
                    }
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
        List<Hero> dead = ais.stream()
                .filter(not(Hero::isAlive))
                .collect(toList());
        ais.removeAll(dead);
        dead.stream()
            .filter(Hero::withPrize)
            .forEach(tank -> prizeGen.drop(tank));
    }

    @Override
    public void affect(Bullet bullet) {
        if (borders.contains(bullet)) {
            bullet.remove();
            return;
        }

        if (heroesAndAis().contains(bullet)) {
            int index = heroesAndAis().indexOf(bullet);
            Hero tank = heroesAndAis().get(index);
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
            if (bullet != bullet2
                    && bullet.equals(bullet2)
                    && bullet2.getTick() != 0)
            {
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
    public boolean isTree(Point pt) {
        return trees.stream().anyMatch(tree -> tree.itsMe(pt));
    }

    @Override
    public boolean isIce(Point pt) {
        return ice.stream().anyMatch(ice -> ice.itsMe(pt));
    }

    @Override
    public void add(Prize prize) {
        prize.init(settings);
        prizes.add(prize);
    }

    @Override
    public void addAi(Hero hero) {
        ais.add(hero);
    }

    private Wall getWallAt(Point pt) {
        int index = walls.indexOf(pt);
        return walls.get(index);
    }

    private void scoresForKill(Bullet killedBullet, Hero diedTank) {
        Hero killerTank = killedBullet.getOwner();
        Player killer = null;
        if (!ais.contains(killerTank)) {
            killer = player(killerTank);
        }

        if (killer != null) {
            if (ais.contains(diedTank)) {
                killer.event(Event.KILL_OTHER_AI_TANK);
            } else {
                killer.killHero();
                killer.event(Event.KILL_OTHER_HERO_TANK.apply(killer.score()));
            }
        }
    }

    private Player player(Hero tank) {
        return players.stream()
                .filter(player -> player.getHero().equals(tank))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Танк игрока не найден!"));
    }

    @Override
    public boolean isBarrierFor(Hero hero, Point pt) {
        return isBarrier(pt)
                || (isRiver(pt) && !hero.canWalkOnWater());
    }

    @Override
    public boolean isFree(Point pt) {
        return !(isBarrier(pt) || isTree(pt) || isRiver(pt) || isIce(pt));
    }

    @Override
    public Optional<Point> freeRandom(Player player) {
        return BoardUtils.freeRandom(size, dice, pt -> isFree(pt));
    }

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

        for (Hero tank : heroesAndAis()) {   //  TODO проверить как один танк не может проходить мимо другого танка игрока (не AI)
            if (tank.itsMe(pt)) {
                return true;
            }
        }

        return pt.isOutOf(size);
    }

    private List<Bullet> bullets() {
        return heroesAndAis().stream()
                .flatMap(tank -> tank.getBullets().stream())
                .collect(toList());
    }

    @Override
    public List<Hero> ais() {
        return ais;
    }

    public List<Hero> heroesAndAis() {
        List<Hero> result = new LinkedList<>(ais);
        for (Player player : players) {
            if (player.getHero() != null) {
                result.add(player.getHero());
            }
        }
        return result;
    }

    public List<Hero> heroes() {
        return players.stream()
                .map(Player::getHero)
                .collect(toList());
    }

    public List<Prize> prizes() {
        return prizes.all();
    }

    @Override
    protected void onAdd(Player player) {
        player.newHero(this);
    }

    @Override
    protected void onRemove(Player player) {
        // do nothing
    }

    public int size() {
        return size;
    }

    @Override
    public BoardReader<Player> reader() {
        return new BoardReader<>() {
            private int size = Rawelbbub.this.size;

            @Override
            public int size() {
                return size;
            }

            @Override
            public void addAll(Player player, Consumer<Iterable<? extends Point>> processor) {
                processor.accept(borders());
                processor.accept(heroesAndAis());
                processor.accept(walls());
                processor.accept(bullets());
                processor.accept(prizes());
                processor.accept(trees());
                processor.accept(ice());
                processor.accept(rivers());
            }
        };
    }

    public List<Wall> walls() {
        return walls.stream()
                .filter(not(Wall::destroyed))
                .collect(toList());
    }

    private int withPrize() {
        int withPrize = (int) heroesAndAis().stream().filter(Hero::withPrize).count();
        return prizes().size() + withPrize;
    }

    public List<Tree> trees() {
        return trees;
    }

    public List<Ice> ice() {
        return ice;
    }

	public List<River> rivers() {
		return rivers;
	}

    public List<Border> borders() {
        return borders;
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

    public void addWall(Wall wall) {
        wall.init(settings);
        walls.add(wall);
    }

    public AiGenerator getAiGenerator() {
        return aiGen;
    }

    public void addWall(List<Wall> walls) {
        walls.forEach(this::addWall);
    }

    public void addBorder(List<Border> borders) {
        borders.forEach(this::addBorder);
    }

    public void addRiver(List<River> rivers) {
        rivers.forEach(this::addRiver);
    }

    public void addTree(List<Tree> trees) {
        trees.forEach(this::addTree);
    }

    public void addIce(List<Ice> ice) {
        ice.forEach(this::addIce);
    }

    @Override
    public GameSettings settings() {
        return settings;
    }
}
