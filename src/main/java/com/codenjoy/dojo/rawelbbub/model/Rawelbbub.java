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
import com.codenjoy.dojo.rawelbbub.services.GameSettings;
import com.codenjoy.dojo.services.BoardUtils;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.field.Accessor;
import com.codenjoy.dojo.services.field.PointField;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.services.round.RoundField;
import com.codenjoy.dojo.utils.whatsnext.WhatsNextUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.codenjoy.dojo.games.rawelbbub.Element.PRIZE_BREAKING_WALLS;
import static com.codenjoy.dojo.games.rawelbbub.Element.PRIZE_IMMORTALITY;
import static com.codenjoy.dojo.rawelbbub.services.Event.*;
import static java.util.function.Predicate.not;

public class Rawelbbub extends RoundField<Player, Hero> implements Field {

    private Level level;
    private PointField field;
    private List<Player> players;
    private Dice dice;
    private GameSettings settings;

    private Prizes prizes;
    private PrizeGenerator prizeGen;
    private AiGenerator aiGen;

    public Rawelbbub(Dice dice, Level level, GameSettings settings) {
        super(START_ROUND, WIN_ROUND, settings);

        this.level = level;
        this.dice = dice;
        this.settings = settings;
        this.field = new PointField();
        this.players = new LinkedList<>();
        this.prizes = new Prizes(field);

        clearScore();
    }

    @Override
    public void clearScore() {
        if (level == null) return;

        level.saveTo(field);
        field.init(this);

        prizeGen = new PrizeGenerator(this, dice, settings);
        aiGen = new AiGenerator(this, dice, settings);

        addAis(level.ais());

        players.forEach(Player::reset);
        heroesAndAis().forEach(Hero::reset);

        super.clearScore();
    }

    @Override
    protected void onAdd(Player player) {
        player.newHero(this);
    }

    @Override
    protected void onRemove(Player player) {
        Hero hero = player.getHero();
        heroes().removeExact(hero);
        bullets().removeIf(bullet -> bullet.owner() == hero);
    }

    @Override
    protected List<Player> players() {
        return players;
    }

    @Override
    public void cleanStuff() {
        removeDeadItems();
    }

    @Override
    public void tickField() {
        aiGen.allHave(withPrize());
        aiGen.dropAll();

        List<Hero> heroes = heroesAndAis();

        for (Hero hero : heroes) {
            hero.tick();
        }

        for (Bullet bullet : bullets().copy()) {
            if (bullet.destroyed()) {
                bullet.remove();
            }
        }

        for (Hero hero : heroes) {
            if (hero.isAlive()) {
                hero.tryFire();
            }

            if (hero.prizes().contains(PRIZE_BREAKING_WALLS)) {
                hero.getBullets().forEach(Bullet::heavy);
            }
        }

        for (Hero hero : heroes) {
            if (hero.isAlive()) {
                hero.move();

                Bullet bullet = bullets().getFirstAt(hero);
                if (bullet != null) {
                    if (bullet.getTick() != 0) {
                        affect(bullet);
                    }
                }
            }
        }

        for (Bullet bullet : bullets().copy()) {
            bullet.move();
        }

        for (Wall wall : walls()) {
            if (!heroes.contains(wall) && !bullets().contains(wall)) {
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

    public int size() {
        return field.size();
    }

    @Override
    public boolean isBarrierFor(Hero hero, Point pt) {
        return isBarrier(pt)
                || (isFishnet(pt) && !hero.canWalkOnFishnet());
    }

    public boolean isBarrier(Point pt) {
        for (Wall wall : walls()) {
            if (wall.itsMe(pt) && !wall.destroyed()) {
                return true;
            }
        }

        for (Point reefs : reefs()) {
            if (reefs.itsMe(pt)) {
                return true;
            }
        }

        for (Hero hero : heroesAndAis()) {   //  TODO проверить как один танк не может проходить мимо другого танка игрока (не AI)
            if (hero.itsMe(pt)) {
                return true;
            }
        }

        return pt.isOutOf(size());
    }

    @Override
    public Optional<Point> freeRandom(Player player) {
        return BoardUtils.freeRandom(size(), dice, this::isFree);
    }

    @Override
    public boolean isFree(Point pt) {
        return !isBarrier(pt)
                && !isSeaweed(pt)
                && !isFishnet(pt)
                && !isOil(pt);
    }

    @Override
    public GameSettings settings() {
        return settings;
    }

    @Override
    public Dice dice() {
        return dice;
    }

    @Override
    public BoardReader<Player> reader() {
        return field.reader(
                Reefs.class,
                Seaweed.class,
                Hero.class,
                AI.class,
                AIPrize.class,
                Prize.class,
                Bullet.class,
                Wall.class,
                Oil.class,
                Fishnet.class);
    }

    @Override
    public List<Player> load(String board, Function<Hero, Player> player) {
        level = new Level(board);
        return WhatsNextUtils.load(this, level.heroes(), player);
    }

    public void addAis(List<? extends Point> ais) {
        aiGen.dropAll(ais);
    }

    private void removeDeadItems() {
        removeDeadAi();
        prizes.removeDead();
    }

    private void removeDeadAi() {
        removeDeadAnd(ais())
                .forEach(ai -> {
                });
        removeDeadAnd(prizeAis())
                .forEach(ai -> prizeGen.drop(ai));
    }

    private <E extends AI> Stream<E> removeDeadAnd(Accessor<E> accessor) {
        return accessor.copy().stream()
                .filter(not(AI::isAlive))
                .peek(accessor::removeExact);
    }

    @Override
    public void affect(Bullet bullet) {
        if (reefs().contains(bullet)) {
            bullet.remove();
            return;
        }

        if (heroesAndAis().contains(bullet)) {
            int index = heroesAndAis().indexOf(bullet);
            Hero prey = heroesAndAis().get(index);
            if (prey == bullet.owner()) {
                return;
            }

            if (!prey.prizes().contains(PRIZE_IMMORTALITY)) {
                prey.kill(bullet);
            }

            if (!prey.isAlive()) {
                scoresForKill(bullet, prey);
            }

            bullet.remove();  // TODO заимплементить взрыв
            return;
        }

        for (Bullet bullet2 : bullets().copy()) {
            if (bullet != bullet2
                    && bullet.equals(bullet2)
                    && bullet2.getTick() != 0) {
                bullet.boom();
                bullet2.boom();
                return;
            }
        }

        if (walls().contains(bullet)) {
            Wall wall = walls().getFirstAt(bullet);
            if (!wall.destroyed()) {
                wall.destroy(bullet);
                bullet.remove();  // TODO заимплементить взрыв
                return;
            }
        }

        if (prizes.affect(bullet)) {
            bullet.remove();
            return;
        }
    }

    @Override
    public boolean isFishnet(Point pt) {
        return fishnet().contains(pt);
    }

    @Override
    public boolean isSeaweed(Point pt) {
        return seaweed().contains(pt);
    }

    @Override
    public boolean isOil(Point pt) {
        return oil().contains(pt);
    }

    @Override
    public void add(Prize prize) {
        prize.init(settings);
        prizes.add(prize);
    }

    private void scoresForKill(Bullet bullet, Hero prey) {
        Player hunter = (Player) bullet.owner().getPlayer();
        if (!players.contains(hunter)) {
            return;
        }

        if (prey.isAI()) {
            hunter.event(KILL_AI);
        } else {
            hunter.killHero();
            hunter.event(KILL_OTHER_HERO.apply(hunter.score()));
        }
    }

    public List<Hero> heroesAndAis() {
        List<Hero> result = new LinkedList<>();
        result.addAll(ais().all());
        result.addAll(prizeAis().all());
        result.addAll(heroes().all());
        return result;
    }

    private int withPrize() {
        return prizes().size() + prizeAis().size();
    }

    public AiGenerator getAiGenerator() {
        return aiGen;
    }

    @Override
    public Accessor<Hero> heroes() {
        return field.of(Hero.class);
    }

    @Override
    public Accessor<Prize> prizes() {
        return field.of(Prize.class);
    }

    @Override
    public Accessor<Seaweed> seaweed() {
        return field.of(Seaweed.class);
    }

    @Override
    public Accessor<Oil> oil() {
        return field.of(Oil.class);
    }

    @Override
    public Accessor<Fishnet> fishnet() {
        return field.of(Fishnet.class);
    }

    @Override
    public Accessor<Wall> walls() {
//        TODO
//         .filter(not(Wall::destroyed))
//                .collect(toList());
        return field.of(Wall.class);
    }

    @Override
    public Accessor<Reefs> reefs() {
        return field.of(Reefs.class);
    }

    @Override
    public Accessor<AI> ais() {
        return field.of(AI.class);
    }

    @Override
    public Accessor<AIPrize> prizeAis() {
        return field.of(AIPrize.class);
    }

    @Override
    public Accessor<Bullet> bullets() {
        return field.of(Bullet.class);
    }
}