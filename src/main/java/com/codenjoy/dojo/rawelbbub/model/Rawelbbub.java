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


import com.codenjoy.dojo.rawelbbub.model.items.*;
import com.codenjoy.dojo.rawelbbub.services.GameSettings;
import com.codenjoy.dojo.services.BoardUtils;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.field.Accessor;
import com.codenjoy.dojo.services.field.PointField;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.services.round.RoundField;
import com.codenjoy.dojo.services.round.RoundGamePlayer;
import com.codenjoy.dojo.utils.whatsnext.WhatsNextUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.codenjoy.dojo.games.rawelbbub.Element.PRIZE_BREAKING_BAD;
import static com.codenjoy.dojo.rawelbbub.services.Event.*;
import static java.util.function.Predicate.not;

public class Rawelbbub extends RoundField<Player, Hero> implements Field {

    private Level level;
    private PointField field;
    private List<Player> players;
    private Dice dice;
    private GameSettings settings;

    private Prizes prizes;
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

        aiGen = new AiGenerator(this, dice, settings);

        addAis(level.ais());

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
        torpedoes().removeIf(torpedo -> torpedo.owner() == hero);
    }

    @Override
    protected List<Player> players() {
        return players;
    }

    @Override
    public boolean hasPlayer(RoundGamePlayer player) {
        return players.contains(player);
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

        for (Hero hero : heroes) {
            if (hero.isAlive()) {
                hero.tryFire();
            }

            if (hero.prizes().contains(PRIZE_BREAKING_BAD)) {
                hero.torpedoes().forEach(Torpedo::heavy);
            }
        }

        for (Hero hero : heroes) {
            if (hero.isAlive()) {
                hero.move();

                torpedoes().hasAt(hero, torpedo -> {
                    if (!torpedo.justFired()) {
                        if (hero.affect(torpedo)) {
                            torpedo.remove();
                        }
                    }
                });
            }
        }

        for (Torpedo torpedo : torpedoes().copy()) {
            torpedo.move();
        }

        for (Iceberg iceberg : icebergs()) {
            if (!heroes.contains(iceberg) && !torpedoes().contains(iceberg)) {
                iceberg.tick();
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
        for (Iceberg iceberg : icebergs()) {
            if (iceberg.itsMe(pt) && !iceberg.destroyed()) {
                return true;
            }
        }

        for (Point reefs : reefs()) {
            if (reefs.itsMe(pt)) {
                return true;
            }
        }

        for (Hero hero : heroesAndAis()) {   //  TODO проверить как один герой не может проходить мимо другого игрока (не AI)
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
                Torpedo.class,
                Prize.class,
                Iceberg.class,
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
        removeTorpedoes();
    }

    private void removeTorpedoes() {
        torpedoes().copy().forEach(torpedo -> {
            if (torpedo.destroyed()) {
                torpedo.remove();
            }
        });
    }

    private void removeDeadAi() {
        removeDeadAnd(ais())
                .forEach(ai -> {});
        removeDeadAnd(prizeAis())
                .forEach(this::dropPrize);
    }

    private void dropPrize(Point pt) {
        prizes.add(new Prize(pt, this), true);
    }

    private <E extends AI> Stream<E> removeDeadAnd(Accessor<E> accessor) {
        return accessor.copy().stream()
                .filter(not(AI::isAlive))
                .peek(accessor::removeExact);
    }

    @Override
    public boolean affect(Torpedo torpedo) {
        if (reefs().contains(torpedo)) {
            torpedo.remove();
            return true;
        }

        if (heroesAndAis().contains(torpedo)) {
            int index = heroesAndAis().indexOf(torpedo);
            Hero prey = heroesAndAis().get(index);

            boolean affect = prey.affect(torpedo);
            if (affect) {
                torpedo.remove();
            }
            return affect;
        }

        for (Torpedo torpedo2 : torpedoes().getAt(torpedo)) {
            if (torpedo2.affect(torpedo)) {
                torpedo.boom();
                torpedo2.boom();
                return true;
            }
        }

        if (icebergs().contains(torpedo)) {
            Iceberg iceberg = icebergs().getFirstAt(torpedo);
            if (iceberg.affect(torpedo)) {
                torpedo.boom();
                return true;
            }
        }

        if (prizes.affect(torpedo)) {
            torpedo.boom();
            return true;
        }

        return false;
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
    public Accessor<Iceberg> icebergs() {
//        TODO
//         .filter(not(Iceberg::destroyed))
//                .collect(toList());
        return field.of(Iceberg.class);
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
    public Accessor<Torpedo> torpedoes() {
        return field.of(Torpedo.class);
    }
}