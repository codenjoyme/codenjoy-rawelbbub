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


import com.codenjoy.dojo.battlecity.model.levels.DefaultBorders;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Joystick;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.printer.Printer;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.services.PointImpl.pt;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class BattlecityTest {

    public int ticksPerBullets;
    public int size;
    private Parameter<Integer> spawnAiPrize;
    private Parameter<Integer> hitKillsAiPrize;

    private Battlecity game;
    private Joystick hero;
    private List<Player> players = new LinkedList<>();
    private PrinterFactory printerFactory = new PrinterFactoryImpl();
    private Settings settings = new SettingsImpl();

    private List<Tank> tanks;
	private List<Tree> trees;
	private List<Construction> constructions;
	private List<Ice> ice;
	private List<River> rivers;

	@Before
    public void setup() {
        size = 7;
        ticksPerBullets = 1;
        spawnAiPrize = setParameter("count spawn", 4);
        hitKillsAiPrize = setParameter("hits to kill", 3);
    }

    private Parameter<Integer> setParameter(String name, int value) {
        return settings.addEditBox(name).type(Integer.class).def(value);
    }

    private void givenGame(Tank tank, Construction... constructions) {
        game = new Battlecity(size, mock(Dice.class), Arrays.asList(constructions), spawnAiPrize, hitKillsAiPrize);
        initPlayer(game, tank);
        this.hero = tank;
    }

    private void givenGame(Tank tank, Border... walls) {
        List<Border> borders = new DefaultBorders(size).get();
        borders.addAll(Arrays.asList(walls));

        game = new Battlecity(size, mock(Dice.class), Arrays.asList(new Construction[0]), borders, spawnAiPrize, hitKillsAiPrize);
        initPlayer(game, tank);
        this.hero = tank;
    }

    private void givenGameWithAI(Tank tank, Tank... aiTanks) {
        game = new Battlecity(size, mock(Dice.class), Arrays.asList(new Construction[0]), spawnAiPrize, hitKillsAiPrize, aiTanks);
        initPlayer(game, tank);
        this.hero = tank;
    }

    private void givenGameWithTanks(List<Tank> tanks) {
        game = new Battlecity(size, mock(Dice.class), Arrays.asList(new Construction[0]),
                new DefaultBorders(size).get(), spawnAiPrize, hitKillsAiPrize);

        for (Tank tank : tanks) {
            initPlayer(game, tank);
        }
        this.hero = tanks.get(0);
    }

    private void givenGameWithTanks(List<Tank> tanks, List<Construction> constructions) {
        game = new Battlecity(size, mock(Dice.class), constructions,
                new DefaultBorders(size).get(), spawnAiPrize, hitKillsAiPrize);

        for (Tank tank : tanks) {
            initPlayer(game, tank);
        }
        this.hero = tanks.get(0);
    }

    private Player initPlayer(Battlecity game, Tank tank) {
        Player player = mock(Player.class);
        when(player.getHero()).thenReturn(tank);
        players.add(player);
        tank.init(game);
        game.newGame(player);
        return player;
    }

    private void givenGameWithTanks(Tank... tanks) {
        game = new Battlecity(size, mock(Dice.class), Arrays.asList(new Construction[]{}), spawnAiPrize, hitKillsAiPrize);
        for (Tank tank : tanks) {
            initPlayer(game, tank);
        }
        this.hero = tanks[0];
    }

    public static Tank tank(int x, int y, Direction direction, int ticksPerBullets) {
        Dice dice = getDice(x, y);
        return new Tank(pt(x, y), direction, dice, ticksPerBullets);
    }

    public Tank tank(int x, int y, Direction direction) {
        return tank(x, y, direction, ticksPerBullets);
    }

    private static Dice getDice(int x, int y) {
        Dice dice = mock(Dice.class);
        when(dice.next(anyInt())).thenReturn(x, y);
        return dice;
    }

    public Tank setAITank(int x, int y, Direction direction) {
        return setAITank(x, y, direction, ticksPerBullets);
    }

    public static Tank setAITank(int x, int y,  Direction direction, int ticksPerBullets) {
        Dice dice = getDice(x, y);
        Point pt = pt(x, y);
        return new AITank(pt, dice, direction,ticksPerBullets, false);
    }

    public static Tank aiTank(int x, int y, Direction direction, int ticksPerBullets ) {
        Dice dice = getDice(x, y);
        Point pt = pt(x, y);
        return new AITank(pt, dice, direction);
    }

    public Tank aiTank(int x, int y, Direction direction) {
        ticksPerBullets = 0;
        return aiTank(x, y, direction, ticksPerBullets);
    }

    public static Tank aiTankPrize(int x, int y, Direction direction, Parameter<Integer> hitKillsAiPrize) {
        Dice dice = getDice(x, y);
        Point pt = pt(x, y);
        return new AITankPrize(pt, dice, direction,  hitKillsAiPrize.getValue());
    }

    private static Dice getDice(Point pt, int indexPrizes) {
        Dice dice = mock(Dice.class);
        when(dice.next(anyInt()))
                .thenAnswer(x -> pt.getX())
                .thenAnswer(y -> pt.getY())
                .thenAnswer(ans -> indexPrizes);
        return dice;
    }

    public void givenGameWithConstruction(int x, int y) {
        givenGame(tank(1, 1, Direction.UP), new Construction(x, y));
    }

    public void givenGameWithConstruction(Tank tank, int x, int y) {
        givenGame(tank, new Construction(x, y));
    }

    public void givenGameWithTankAt(int x, int y) {
        givenGameWithTankAt(x, y, Direction.UP);
    }

    public void givenGameWithTankAt(int x, int y, Direction direction) {
        givenGame(tank(x, y, direction), new Construction[]{});
    }

    private boolean assertAiPrize(int totalAiPrize, int totalTanks) {
        List<Tank> tanks = game.getTanks();
        if (totalTanks == tanks.size()) {
            long count = tanks.stream().filter(x -> x.isTankPrize()).count();
            if (count == totalAiPrize) {
                return true;
            }
        }
        return false;
    }

    private boolean assertPrize() {
        List<Prize> prize = game.getPrize();
        if (prize.isEmpty()) {
            return false;
        }
        return true;
    }

    private void givenGameBeforeDropPrize(Point pt) {
        Bullet bullet = mock(Bullet.class);
        Dice dice = getDice(pt, 0);
        spawnAiPrize = setParameter("count spawn", 0);
        hitKillsAiPrize = setParameter("hits to kill", 1);
        Tank tank = tank(1, 1, Direction.UP);
        Tank aiTank = aiTankPrize(1, 5, Direction.DOWN, hitKillsAiPrize);

        game = new Battlecity(size, dice, Arrays.asList(new Construction(3, 3)), spawnAiPrize, hitKillsAiPrize);
        initPlayer(game, tank);
        this.hero = tank;
        game.addAI(aiTank);
        aiTank.kill(bullet);
    }

    @Test
    public void shouldDrawField() {
        givenGameWithTankAt(1, 1);
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    private void assertD(String field) {
        assertEquals(field, getPrinter().print());
    }

    private Printer<String> getPrinter() {
        return printerFactory.getPrinter(
                game.reader(), players.get(0));
    }

    @Test
    public void shouldBeConstruction_whenGameCreated() {
        givenGame(tank(1, 1, Direction.UP), new Construction(3, 3));
        assertEquals(1, game.getConstructions().size());

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldBeTankOnFieldWhenGameCreated() {
        givenGameWithTankAt(1, 1);
        assertNotNull(game.getTanks());
    }

    @Test
    public void shouldTankMove() {
        givenGameWithTankAt(1, 1);
        hero.up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.down();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.right();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ►   ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.left();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼◄    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldTankStayAtPreviousPositionWhenIsNearBorder() {
        givenGameWithTankAt(1, 1);
        hero.down();
        hero.down();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼☼☼☼☼☼☼\n");


        hero.left();
        hero.left();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼◄    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        Tank someTank = tank(5, 5, Direction.UP);
        game.addAI(someTank);

        someTank.right();
        someTank.right();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼    ˃☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼◄    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        someTank.up();
        someTank.up();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼    ˄☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼◄    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldBulletHasSameDirectionAsTank() {
        givenGameWithTankAt(1, 1);
        hero.act();
        game.tick();

        Tank realTank = (Tank) hero;
        assertEquals(realTank.getBullets().iterator().next().getDirection(), realTank.getDirection());
    }

    @Test
    public void shouldBulletGoInertiaWhenTankChangeDirection() {
        givenGameWithTankAt(3, 1);
        hero.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼  •  ☼\n" +
                "☼     ☼\n" +
                "☼  ▲  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.right();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  •  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼   ► ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldBulletDisappear_whenHittingTheWallUp() {
        givenGameWithTankAt(1, 1);
        hero.act();
        game.tick();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldBulletDisappear_whenHittingTheWallRight() {
        givenGameWithTankAt(1, 1, Direction.RIGHT);
        hero.act();
        game.tick();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼►   •☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼►    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldBulletDisappear_whenHittingTheWallLeft() {
        givenGameWithTankAt(5, 5, Direction.LEFT);
        hero.act();
        game.tick();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼•   ◄☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼    ◄☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldBulletDisappear_whenHittingTheWallDown() {
        givenGameWithTankAt(5, 5, Direction.DOWN);
        hero.act();
        game.tick();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼    ▼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    •☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼    ▼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // снарядом уничтожается стенка за три присеста - снизу
    @Test
    public void shouldBulletDestroyWall_whenHittingTheWallDown() {
        givenGameWithConstruction(1, 5);
        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼╩    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼╩    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼╨    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼╨    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

    }

    // снарядом уничтожается стенка за три присеста - слева
    @Test
    public void shouldBulletDestroyWall_whenHittingTheWallLeft() {
        givenGameWithConstruction(tank(1, 1, Direction.RIGHT), 5, 1);
        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼► • ╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼►   ╠☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼► • ╠☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼►   ╞☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼► • ╞☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼►    ☼\n" +
                "☼☼☼☼☼☼☼\n");

    }

    // снарядом уничтожается стенка за три присеста - справа
    @Test
    public void shouldBulletDestroyWall_whenHittingTheWallRight() {
        givenGameWithConstruction(tank(5, 1, Direction.LEFT), 1, 1);
        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬ • ◄☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╣   ◄☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╣ • ◄☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╡   ◄☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╡ • ◄☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    ◄☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // снарядом уничтожается стенка за три присеста - сверху
    @Test
    public void shouldBulletDestroyWall_whenHittingTheWallUp() {
        givenGameWithConstruction(tank(1, 5, Direction.DOWN), 1, 1);
        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼╬    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╦    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼╦    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╥    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼╥    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

    }

    // снарядом уничтожается стенка за три присеста - снизу но сквозь стену
    @Test
    public void shouldBulletDestroyWall_whenHittingTheWallDown_overWall() {
        givenGameWithConstruction(tank(1, 2, Direction.UP), 1, 5);
        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼╩    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼╩    ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼╨    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼╨    ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

    }

    // снарядом уничтожается стенка за три присеста - слева но сквозь стену
    @Test
    public void shouldBulletDestroyWall_whenHittingTheWallLeft_overWall() {
        givenGameWithConstruction(tank(2, 1, Direction.RIGHT), 5, 1);
        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ► •╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ►  ╠☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ► •╠☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ►  ╞☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ► •╞☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ►   ☼\n" +
                "☼☼☼☼☼☼☼\n");

    }

    // снарядом уничтожается стенка за три присеста - справа но через стену
    @Test
    public void shouldBulletDestroyWall_whenHittingTheWallRight_overWall() {
        givenGameWithConstruction(tank(4, 1, Direction.LEFT), 1, 1);
        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬• ◄ ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╣  ◄ ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╣• ◄ ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╡  ◄ ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╡• ◄ ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼   ◄ ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // снарядом уничтожается стенка за три присеста - сверху но сквозь стену
    @Test
    public void shouldBulletDestroyWall_whenHittingTheWallUp_overWall() {
        givenGameWithConstruction(tank(1, 4, Direction.DOWN), 1, 1);
        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼╬    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╦    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼╦    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╥    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼╥    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldBulletDestroyWall_whenHittingTheWallUp_whenTwoWalls() {
        givenGame(tank(1, 1, Direction.UP), new Construction(1, 5), new Construction(1, 4));
        hero.act();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╬    ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╩    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╩    ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╨    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╨    ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼╩    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

    }

    @Test
    public void shouldBulletDestroyWall_whenHittingTheWallRight_whenTwoWalls() {
        givenGame(tank(1, 1, Direction.RIGHT), new Construction(5, 1), new Construction(4, 1));
        hero.act();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼►  ╬╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼► •╬╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼►  ╠╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼► •╠╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼►  ╞╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼► •╞╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼►   ╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼► • ╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼►   ╠☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldBulletDestroyWall_whenHittingTheWallLeft_whenTwoWalls() {
        givenGame(tank(5, 1, Direction.LEFT), new Construction(1, 1), new Construction(2, 1));
        hero.act();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬╬  ◄☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬╬• ◄☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬╣  ◄☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬╣• ◄☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬╡  ◄☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬╡• ◄☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬   ◄☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬ • ◄☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╣   ◄☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldBulletDestroyWall_whenHittingTheWallDown_whenTwoWalls() {
        givenGame(tank(5, 5, Direction.DOWN), new Construction(5, 1), new Construction(5, 2));
        hero.act();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼    ▼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    ╬☼\n" +
                "☼    ╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼    ▼☼\n" +
                "☼     ☼\n" +
                "☼    •☼\n" +
                "☼    ╬☼\n" +
                "☼    ╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼    ▼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    ╦☼\n" +
                "☼    ╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼    ▼☼\n" +
                "☼     ☼\n" +
                "☼    •☼\n" +
                "☼    ╦☼\n" +
                "☼    ╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼    ▼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    ╥☼\n" +
                "☼    ╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼    ▼☼\n" +
                "☼     ☼\n" +
                "☼    •☼\n" +
                "☼    ╥☼\n" +
                "☼    ╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼    ▼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    ╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼    ▼☼\n" +
                "☼     ☼\n" +
                "☼    •☼\n" +
                "☼     ☼\n" +
                "☼    ╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼    ▼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    ╦☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // если я иду а спереди стена, то я не могу двигаться дальше
    @Test
    public void shouldDoNotMove_whenWallTaWay_goDownOrLeft() {
        givenGame(tank(3, 3, Direction.DOWN),
                new Construction(3, 4), new Construction(4, 3),
                new Construction(3, 2), new Construction(2, 3));

        hero.right();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ╬  ☼\n" +
                "☼ ╬►╬ ☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ╬  ☼\n" +
                "☼ ╬▲╬ ☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.left();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ╬  ☼\n" +
                "☼ ╬◄╬ ☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.down();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ╬  ☼\n" +
                "☼ ╬▼╬ ☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        removeAllNear();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ╩  ☼\n" +
                "☼ ╣▼╠ ☼\n" +
                "☼  ╦  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.right();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ╩  ☼\n" +
                "☼ ╣►╠ ☼\n" +
                "☼  ╦  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ╩  ☼\n" +
                "☼ ╣▲╠ ☼\n" +
                "☼  ╦  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.left();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ╩  ☼\n" +
                "☼ ╣◄╠ ☼\n" +
                "☼  ╦  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.down();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ╩  ☼\n" +
                "☼ ╣▼╠ ☼\n" +
                "☼  ╦  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        removeAllNear();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ╨  ☼\n" +
                "☼ ╡▼╞ ☼\n" +
                "☼  ╥  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    private void removeAllNear() {
        hero.up();
        game.tick();
        hero.act();
        game.tick();

        hero.left();
        game.tick();
        hero.act();
        game.tick();

        hero.right();
        game.tick();
        hero.act();
        game.tick();

        hero.down();
        game.tick();
        hero.act();
        game.tick();
    }


    // если я стреляю дважды, то выпускается два снаряда
    // при этом я проверяю, что они уничтожаются в порядке очереди
    @Test
    public void shouldShotWithSeveralBullets_whenHittingTheWallDown() {
        size = 9;
        givenGame(tank(7, 7, Direction.DOWN), new Construction(7, 1), new Construction(7, 2));
        hero.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ▼☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼      ╬☼\n" +
                "☼      ╬☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ▼☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼      ╬☼\n" +
                "☼      ╬☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ▼☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼      ╦☼\n" +
                "☼      ╬☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ▼☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼      ╥☼\n" +
                "☼      ╬☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ▼☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼      ╬☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ▼☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼      ╬☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ▼☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼      ╦☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ▼☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼      ╥☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ▼☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ▼☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ▼☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ▼☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ▼☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ▼☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");
    }

    // стоит проверить, как будут себя вести полуразрушенные конструкции, если их растреливать со всех других сторон
    @Test
    public void shouldDestroyFromUpAndDownTwice() {
        givenGame(tank(3, 4, Direction.DOWN), new Construction(3, 3));

        hero.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ▼  ☼\n" +
                "☼  ╦  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.right();
        game.tick();

        hero.down();
        game.tick();

        hero.down();
        game.tick();

        hero.left();
        game.tick();

        hero.up();
        game.tick();

        hero.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼  ─  ☼\n" +
                "☼  ▲  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼  ▲  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // стоять, если спереди другой танк
    @Test
    public void shouldStopWhenBeforeOtherTank() {
        Tank tank1 = tank(1, 2, Direction.DOWN);
        Tank tank2 = tank(1, 1, Direction.UP);
        givenGameWithTanks(tank1, tank2);

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼˄    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        tank1.down();
        tank2.up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼˄    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // геймовер, если убили не бот-танк
    @Test
    public void shouldDieWhenOtherTankKillMe() {
        Tank tank1 = tank(1, 1, Direction.UP);
        Tank tank2 = tank(1, 2, Direction.DOWN);
        Tank tank3 = tank(1, 3, Direction.DOWN);
        givenGameWithTanks(tank1, tank2, tank3);

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼˅    ☼\n" +
                "☼˅    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertAlive(tank1);
        assertAlive(tank2);
        assertAlive(tank3);

        tank1.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼˅    ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertAlive(tank1);
        assertGameOver(tank2);
        assertAlive(tank3);

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼˅    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        tank3.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼˅    ☼\n" +
                "☼     ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertGameOver(tank1);
        assertGameOver(tank2);
        assertAlive(tank3);

        game.tick();

        assertGameOver(tank1);
        assertGameOver(tank2);
        assertAlive(tank3);
    }

    private void assertGameOver(Tank tank) {
        assertFalse(tank.isAlive());
    }

    private void assertAlive(Tank tank) {
        assertTrue(tank.isAlive());
    }

    // стоять, если меня убили
    @Test
    public void shouldStopWhenKill() {
        Tank tank1 = tank(1, 2, Direction.DOWN);
        Tank tank2 = tank(1, 1, Direction.UP);
        givenGameWithTanks(tank1, tank2);

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼˄    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        tank1.act();
        tank2.left();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldNoConcurrentException() {
        Tank tank1 = tank(1, 2, Direction.DOWN);
        Tank tank2 = tank(1, 1, Direction.UP);
        givenGameWithTanks(tank1, tank2);

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼˄    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        tank2.act();
        tank1.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldDestroyBullet() {
        size = 9;
        Tank tank1 = tank(1, 7, Direction.DOWN);
        Tank tank2 = tank(1, 1, Direction.UP);
        givenGameWithTanks(tank1, tank2);

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼˄      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        tank1.act();
        tank2.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼•      ☼\n" +
                "☼       ☼\n" +
                "☼•      ☼\n" +
                "☼       ☼\n" +
                "☼˄      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼Ѡ      ☼\n" +
                "☼       ☼\n" +
                "☼˄      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼˄      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldDestroyBullet2() {
        size = 9;
        Tank tank1 = tank(1, 6, Direction.DOWN);
        Tank tank2 = tank(1, 1, Direction.UP);
        givenGameWithTanks(tank1, tank2);

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼˄      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        tank1.act();
        tank2.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼•      ☼\n" +
                "☼•      ☼\n" +
                "☼       ☼\n" +
                "☼˄      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼Ѡ      ☼\n" +
                "☼       ☼\n" +
                "☼˄      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼˄      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");
    }

    @Ignore
    @Test
    public void shouldRemoveAIWhenKillIt() {
//        givenGameWithAI(tank(1, 1, Direction.UP), tank(1, 5, Direction.UP), tank(5, 1, Direction.UP));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼˄    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲   ˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        hero.right();

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼˄    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼ ►  ˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        hero.up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ▲   ☼\n" +
                "☼   •˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertW("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ▲   ☼\n" +
                "☼    Ѡ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.setDice(getDice(3, 3));
        game.tick();

        assertW("☼☼☼☼☼☼☼\n" + // TODO разобраться почему тут скачет ассерт
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ▲   ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    private void assertW(String expected) {
        Printer<String> printer = getPrinter();
        assertEquals(expected, printer.print().replaceAll("[«¿»?•]", " "));
    }

    @Test
    public void shouldRegenerateDestroyedWall() {
        shouldBulletDestroyWall_whenHittingTheWallUp_whenTwoWalls();

        hero.act();
        game.tick();
        hero.act();
        game.tick();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        for (int i = 7; i <= Construction.REGENERATE_TIME; i++) {
            game.tick();
        }

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        game.tick();
        game.tick();
        game.tick();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldTankCantGoIfWallAtWay() {
        givenGame(tank(1, 1, Direction.UP), new Border(1, 2));
        hero.up();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldBulletCantGoIfWallAtWay() {
        givenGame(tank(1, 1, Direction.UP), new Border(1, 2));
        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.right();
        hero.act();
        game.tick();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼    ☼\n" +
                "☼ ►  •☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼    ☼\n" +
                "☼ ►   ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldOnlyOneBulletPerTick() {
        givenGame(tank(1, 1, Direction.UP), new Construction(1, 2), new Construction(1, 3));
        hero.act();
        hero.act();
        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬    ☼\n" +
                "☼╩    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        hero.act();
        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬    ☼\n" +
                "☼╨    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬    ☼\n" +
                "☼╨    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldTankCanFireIfAtWayEnemyBullet() {
        Tank tank1 = tank(1, 1, Direction.UP);
        Tank tank2 = tank(1, 5, Direction.DOWN);
        givenGameWithTanks(tank1, tank2);

        tank1.act();
        tank2.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼˅    ☼\n" +
                "☼     ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldTankCanGoIfDestroyConstruction() {
        givenGame(tank(1, 1, Direction.UP), new Construction(1, 2), new Construction(1, 3));
        hero.act();
        game.tick();
        hero.act();
        game.tick();
        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.up();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬    ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldConstructionCantRegenerateOnTank() {
        shouldTankCanGoIfDestroyConstruction();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬    ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        for (int i = 3; i <= Construction.REGENERATE_TIME; i++) {
            game.tick();
        }

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬    ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.right();
        game.tick();

        for (int i = 2; i <= Construction.REGENERATE_TIME; i++) {
            assertD("☼☼☼☼☼☼☼\n" +
                    "☼     ☼\n" +
                    "☼     ☼\n" +
                    "☼╬    ☼\n" +
                    "☼ ►   ☼\n" +
                    "☼     ☼\n" +
                    "☼☼☼☼☼☼☼\n");

            game.tick();
        }

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬    ☼\n" +
                "☼╬►   ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

    }

    @Test
    public void shouldConstructionCantRegenerateOnBullet() {
        givenGame(tank(1, 1, Direction.UP), new Construction(1, 3));
        hero.act();
        game.tick();
        hero.act();
        game.tick();
        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        for (int i = 3; i <= Construction.REGENERATE_TIME; i++) {
            game.tick();
        }

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldNTicksPerBullet() {
        ticksPerBullets = 4;
        givenGame(tank(1, 1, Direction.UP), new Construction(1, 3));

        hero.act();
        game.tick();

        String field =
                "☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╩    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n";
        assertD(field);

        for (int i = 1; i < ticksPerBullets; i++) {
            hero.act();
            game.tick();

            assertD(field);
        }

        hero.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╨    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");


    }

    @Ignore
    @Test
    public void shouldNewAIWhenKillOther() {
        givenGameWithAI(tank(1, 1, Direction.UP), tank(1, 5, Direction.UP), tank(5, 1, Direction.LEFT));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼˄    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲   ˂☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        hero.right();

        game.tick();

        hero.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ► •˂☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        game.tick();

        // TODO разобраться почему тут скачет тест
        assertEquals(2, getPrinter().print().replaceAll("[Ѡ ►☼\n•]", "").length());
    }

    @Test
    public void shouldOnlyRotateIfNoBarrier() {
        givenGame(tank(1, 1, Direction.UP), new Construction(3, 1));
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲ ╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.right();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ►╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldOnlyRotateIfBarrier() {
        givenGame(tank(2, 1, Direction.UP), new Construction(3, 1));
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ▲╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.right();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ►╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldEnemyCanKillTankOnConstruction() {
        givenGame(tank(1, 1, Direction.UP), new Construction(1, 2));
        Tank tank2 = tank(1, 3, Direction.DOWN);
        initPlayer(game, tank2);

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼˅    ☼\n" +
                "☼╬    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();
        hero.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼˅    ☼\n" +
                "☼╨    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        hero.up();  // команда поигнорится потому что вначале ходят все танки, а потом летят все снаряды
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼˅    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼˅    ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        tank2.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼˅    ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

    }

    @Test
    public void shouldDieWhenMoveOnBullet() {
        size = 9;
        Tank tank1 = tank(1, 6, Direction.DOWN);
        Tank tank2 = tank(1, 1, Direction.UP);
        givenGameWithTanks(tank1, tank2);

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼˄      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        tank1.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼•      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼˄      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        tank2.up();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼Ѡ      ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldDieWhenMoveOnBullet2() {
        size = 9;
        Tank tank1 = tank(1, 6, Direction.DOWN);
        Tank tank2 = tank(1, 2, Direction.UP);
        givenGameWithTanks(tank1, tank2);

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼˄      ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        tank1.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼•      ☼\n" +
                "☼       ☼\n" +
                "☼˄      ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        tank2.up();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼Ѡ      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldDieWhenMoveOnBullet3() {
        size = 9;
        Tank tank1 = tank(1, 6, Direction.DOWN);
        Tank tank2 = tank(1, 3, Direction.UP);
        givenGameWithTanks(tank1, tank2);

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼˄      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        tank1.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼•      ☼\n" +
                "☼˄      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        tank2.up();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼Ѡ      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

    }

    // если стенка недорушенная, снаряд летит, и ресетнули игру, то все конструкции восстанавливаются
    @Test
    public void shouldRemoveBulletsAndResetConstructions_whenReset() {
        size = 11;
        ticksPerBullets = 3;
        givenGame(tank(1, 1, Direction.UP), new Construction(1, 9), new Construction(1, 8));

        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼╬        ☼\n" +
                "☼╬        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼•        ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼╬        ☼\n" +
                "☼╬        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼•        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼╬        ☼\n" +
                "☼╬        ☼\n" +
                "☼•        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼╬        ☼\n" +
                "☼╩        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼•        ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero.act(); // не выйдет
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼╬        ☼\n" +
                "☼╩        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼•        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼╬        ☼\n" +
                "☼╩        ☼\n" +
                "☼•        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼╬        ☼\n" +
                "☼╨        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼•        ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        game.tick();

        hero.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼╬        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼•        ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        game.tick();

        hero.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼╩        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼•        ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // допустим за игру он прибил 5 танков
        Player player = players.iterator().next();
        player.setKilled(5);

        // when
        game.clearScore();

        // смогу стрельнуть, пушка ресетнется
        hero.act();
        game.tick();

        // then
        // но после рисета это поле чистится
        assertEquals(0, player.score());

        // и стенки тоже ресетнулись
        // и снаряд полетел
        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼╬        ☼\n" +
                "☼╬        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼•        ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        game.tick();

        hero.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼╬        ☼\n" +
                "☼╩        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼•        ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    // первый выстрел иногда получается сделать дважды
    @Test
    public void shouldCantFireTwice() {
        size = 11;
        ticksPerBullets = 4;
        givenGameWithTankAt(1, 1);

        game.clearScore();

        game.tick(); // внутри там тикает так же gun, но первого выстрела еще небыло
        game.tick();

        hero.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼•        ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼•        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼•        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼•        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    // 1. Кусты
    @Test
    public void shouldBeConstructionTree_whenGameCreated() {
		tanks = new LinkedList<>(Arrays.asList(tank(1, 1, Direction.UP)));
		trees = new LinkedList<>(Arrays.asList(new Tree(3, 3)));

		givenGameWithTanks(tanks);
		game.setTrees(trees);

        assertEquals(1, game.getTrees().size());

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼  ▒  ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldBeConstructionTwoTree_whenGameCreated() {

		List<Tank> tanks = new LinkedList<>(Arrays.asList(tank(1, 1, Direction.UP)));
		List<Tree> trees = new LinkedList<>(Arrays.asList(new Tree(3, 3), new Tree(5, 1)));
		givenGameWithTanks(tanks);
		game.setTrees(trees);

        assertEquals(2, game.getTrees().size());

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼  ▒  ☼\n" +
                "☼     ☼\n" +
                "☼▲   ▒☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // 1.1) При выстреле пуля должна пролетать сквозь кусты
    @Test
    public void shouldBulletFlyUnderTree_right() {
        size = 11;
		tanks = new LinkedList<>(Arrays.asList(tank(1, 1, Direction.UP)));
		trees = new LinkedList<>(Arrays.asList(new Tree(6, 1)));

		givenGameWithTanks(tanks);
		game.setTrees(trees);

        hero.right();
        hero.act();
        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼►    ▒   ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼ ►•  ▒   ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼ ►  •▒   ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼ ►   ▒•  ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼ ►   ▒  •☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼ ►   ▒   ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldBulletDestroyWallUnderTree_whenHittingTheWallUp_whenTwoWalls() {
        size = 7;
		tanks = new LinkedList<>(Arrays.asList(tank(1, 1, Direction.UP)));
		trees = new LinkedList<>(Arrays.asList(new Tree(1, 2)));
		constructions = new LinkedList<>(Arrays.asList(new Construction(1, 5), new Construction(1, 4)));

		givenGameWithTanks(tanks, constructions);
		game.setTrees(trees);

        hero.act();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼▒    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╬    ☼\n" +
                "☼•    ☼\n" +
                "☼▒    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╩    ☼\n" +
                "☼     ☼\n" +
                "☼▒    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╩    ☼\n" +
                "☼•    ☼\n" +
                "☼▒    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╨    ☼\n" +
                "☼     ☼\n" +
                "☼▒    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╨    ☼\n" +
                "☼•    ☼\n" +
                "☼▒    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▒    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼▒    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼╩    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▒    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // Когда пуля и дерево находятся в одной координате когда отработывает метод tick()
    @Test
    public void shouldBulletFlyUnderTwoTree_up() {
        size = 11;
		tanks = new LinkedList<>(Arrays.asList(tank(5, 1, Direction.UP)));
		trees = new LinkedList<>(Arrays.asList(new Tree(5, 5), new Tree(5, 6)));

        givenGameWithTanks(tanks);
        game.setTrees(trees);

        hero.act();
        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    ▒    ☼\n" +
                "☼    ▒    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    ▲    ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    ▒    ☼\n" +
                "☼    ▒    ☼\n" +
                "☼         ☼\n" +
                "☼    •    ☼\n" +
                "☼         ☼\n" +
                "☼    ▲    ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    ▒    ☼\n" +
                "☼    ▒    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    ▲    ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    •    ☼\n" +
                "☼    ▒    ☼\n" +
                "☼    ▒    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    ▲    ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼    •    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    ▒    ☼\n" +
                "☼    ▒    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    ▲    ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    ▒    ☼\n" +
                "☼    ▒    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    ▲    ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    //1.2) кусты - когда игрок заходит под них, там видно кусты и больше никакого движения
    @Test
    public void shouldTankMove_underTree() {
        size = 7;
		tanks = new LinkedList<>(Arrays.asList(tank(1, 1, Direction.UP)));
		trees = new LinkedList<>(Arrays.asList(new Tree(1, 3), new Tree(1, 4)));

        givenGameWithTanks(tanks);
        game.setTrees(trees);

        hero.up();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼▒    ☼\n" +
                "☼▒    ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.up();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼▒    ☼\n" +
                "☼▒    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.up();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼▒    ☼\n" +
                "☼▒    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.right();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼▒►   ☼\n" +
                "☼▒    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.right();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼▒ ►  ☼\n" +
                "☼▒    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldBulletFlyUnderTree_jointly_shouldTankMoveUnderTree() {
        size = 11;
		tanks = new LinkedList<>(Arrays.asList(tank(9, 1, Direction.UP)));
		trees = new LinkedList<>(Arrays.asList(new Tree(9, 5), new Tree(9, 6), new Tree(9, 3)));

        givenGameWithTanks(tanks);
        game.setTrees(trees);

        hero.act();
        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼        ▒☼\n" +
                "☼        ▒☼\n" +
                "☼         ☼\n" +
                "☼        ▒☼\n" +
                "☼         ☼\n" +
                "☼        ▲☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero.up();
        game.tick();
        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼        ▒☼\n" +
                "☼        ▒☼\n" +
                "☼         ☼\n" +
                "☼        ▒☼\n" +
                "☼        ▲☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero.up();
        game.tick();
        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼        ▒☼\n" +
                "☼        ▒☼\n" +
                "☼         ☼\n" +
                "☼        ▒☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero.up();
        game.tick();
        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼        •☼\n" +
                "☼        ▒☼\n" +
                "☼        ▒☼\n" +
                "☼        ▲☼\n" +
                "☼        ▒☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero.up();
        game.tick();

        hero.up();
        game.tick();

        hero.left();
        game.tick();
        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼       ◄▒☼\n" +
                "☼        ▒☼\n" +
                "☼         ☼\n" +
                "☼        ▒☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero.left();
        game.tick();

        hero.left();
        game.tick();
        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼     ◄  ▒☼\n" +
                "☼        ▒☼\n" +
                "☼         ☼\n" +
                "☼        ▒☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    //1.3) так же не видно врагов под кустами
    @Test
    public void shouldOtherTankMove_underTree() {
		size = 11;
		Tank tankHero = tank(1, 1, Direction.UP);
		Tank otherTank = tank(1, 9, Direction.DOWN);

		tanks = Arrays.asList(tankHero, otherTank);
		trees = Arrays.asList(new Tree(1, 6), new Tree(1, 7));

        givenGameWithTanks(tanks);
        game.setTrees(trees);

		assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
				"☼˅        ☼\n" +
				"☼         ☼\n" +
				"☼▒        ☼\n" +
				"☼▒        ☼\n" +
				"☼         ☼\n" +
				"☼         ☼\n" +
				"☼         ☼\n" +
				"☼         ☼\n" +
				"☼▲        ☼\n" +
				"☼☼☼☼☼☼☼☼☼☼☼\n");

        otherTank.down();
        game.tick();
		assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
				"☼         ☼\n" +
				"☼˅        ☼\n" +
				"☼▒        ☼\n" +
				"☼▒        ☼\n" +
				"☼         ☼\n" +
				"☼         ☼\n" +
				"☼         ☼\n" +
				"☼         ☼\n" +
				"☼▲        ☼\n" +
				"☼☼☼☼☼☼☼☼☼☼☼\n");

		otherTank.down();
		game.tick();

		otherTank.down();
		game.tick();
		assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
				"☼         ☼\n" +
				"☼         ☼\n" +
				"☼▒        ☼\n" +
				"☼▒        ☼\n" +
				"☼         ☼\n" +
				"☼         ☼\n" +
				"☼         ☼\n" +
				"☼         ☼\n" +
				"☼▲        ☼\n" +
				"☼☼☼☼☼☼☼☼☼☼☼\n");

		otherTank.down();
		game.tick();
		assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
				"☼         ☼\n" +
				"☼         ☼\n" +
				"☼▒        ☼\n" +
				"☼▒        ☼\n" +
				"☼˅        ☼\n" +
				"☼         ☼\n" +
				"☼         ☼\n" +
				"☼         ☼\n" +
				"☼▲        ☼\n" +
				"☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    //1.4) под кустами не видно так же и ботов белых
    @Test
    public void shouldAITankMove_underTree() {
        size = 11;
        Tank tankHero = tank(1, 1, Direction.UP);
        Tank aiTank = setAITank(1, 9, Direction.DOWN);

        tanks = Arrays.asList(tankHero, aiTank);
        trees = Arrays.asList(new Tree(1, 6), new Tree(1, 7));

        givenGameWithTanks(tanks);
        game.setTrees(trees);

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼¿        ☼\n" +
                "☼         ☼\n" +
                "☼▒        ☼\n" +
                "☼▒        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        aiTank.down();
        game.tick();
        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼¿        ☼\n" +
                "☼▒        ☼\n" +
                "☼▒        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        aiTank.down();
        game.tick();

        aiTank.down();
        game.tick();
        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼▒        ☼\n" +
                "☼▒        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        aiTank.down();
        game.tick();
        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼▒        ☼\n" +
                "☼▒        ☼\n" +
                "☼¿        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldEnemyCanKillTankUnderTree() {
        size = 11;
        Tank tankHero = tank(1, 3, Direction.UP);
        Tank enemyTank = tank(1, 9, Direction.DOWN);

        tanks = Arrays.asList(tankHero, enemyTank);
        trees = Arrays.asList(new Tree(1, 4), new Tree(1, 5), new Tree(1, 6));

        givenGameWithTanks(tanks);
        game.setTrees(trees);

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼˅        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼▒        ☼\n" +
                "☼▒        ☼\n" +
                "☼▒        ☼\n" +
                "☼▲        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero.up();
        game.tick();//герой запрятался в кустах
        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼˅        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼▒        ☼\n" +
                "☼▒        ☼\n" +
                "☼▒        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        enemyTank.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼˅        ☼\n" +
                "☼         ☼\n" +
                "☼•        ☼\n" +
                "☼▒        ☼\n" +
                "☼▒        ☼\n" +
                "☼▒        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();
        assertTrue(tankHero.isAlive());
        game.tick();//герой должен погибнуть
        assertFalse(tankHero.isAlive());
        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼˅        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼▒        ☼\n" +
                "☼▒        ☼\n" +
                "☼▒        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldTwoTankCanPassThroughEachOtherUnderTree() {
        Tank tankHero = tank(1, 1, Direction.UP);
        Tank enemyTank = tank(1, 4, Direction.DOWN);

        tanks = Arrays.asList(tankHero, enemyTank);
        trees = Arrays.asList(new Tree(1, 2), new Tree(1, 3));

        givenGameWithTanks(tanks);
        game.setTrees(trees);

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼˅    ☼\n" +
                "☼▒    ☼\n" +
                "☼▒    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        enemyTank.down();
        tankHero.up();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▒    ☼\n" +
                "☼▒    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        enemyTank.down();
        tankHero.up();
        game.tick();

        enemyTank.down();
        game.tick();
        tankHero.up();
        //Два танка не могут проехать через друг друга
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▒    ☼\n" +
                "☼▒    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        enemyTank.right();
        tankHero.right();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▒˃   ☼\n" +
                "☼▒►   ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

	//2. Лёд
    @Test
    public void shouldBeConstructionIce_whenGameCreated() {
	    //given
        tanks = new LinkedList<>(Arrays.asList(tank(1, 1, Direction.UP)));
        ice = new LinkedList<>(Arrays.asList(new Ice(3, 3)));
        //when
        givenGameWithTanks(tanks);
        game.setIce(ice);
        //then
        assertEquals(1, game.getIce().size());

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼  █  ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    //2.1) когда герой двигается по льду, происходит скольжение (он проскальзывает одну команду).

    /*Если только заезжаем - то сразу же начинается занос, то есть запоминается команда которой
    заезжали на лед*/
    /*Если съезжаем на землю, то любой занос прекращается тут же*/
    @Test
    public void shouldTankMoveUP_onIce_afterBeforeGround() {
        size = 11;
        tanks = new LinkedList<>(Arrays.asList(tank(5, 2, Direction.UP)));
        ice = new LinkedList<>(Arrays.asList(
                new Ice(5, 3),
                new Ice(5, 4),
                new Ice(5, 5)));

        givenGameWithTanks(tanks);
        game.setIce(ice);

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    █    ☼\n" +
                "☼    █    ☼\n" +
                "☼    █    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        //заежаем на лёд
        hero.up();
        game.tick();
        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    █    ☼\n" +
                "☼    █    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        //находимся на льду
        //выполнили команаду right(), но танк не реагирует, так как происходит скольжение
        //двигается дальше с предедущей командой up()
        hero.right();
        game.tick();
        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    █    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼    █    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        //двигаемся дальше в направлении up()
        hero.up();
        game.tick();
        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    ▲    ☼\n" +
                "☼    █    ☼\n" +
                "☼    █    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        //выполнили команаду right(), но танк не реагирует, так как происходит скольжение
        //двигается дальше с предедущей командой up()
        hero.right();
        game.tick();
        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    ▲    ☼\n" +
                "☼    █    ☼\n" +
                "☼    █    ☼\n" +
                "☼    █    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        //выехали со льда
        //двигается дальше в направлении up()
        hero.up();
        game.tick();
        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    ▲    ☼\n" +
                "☼         ☼\n" +
                "☼    █    ☼\n" +
                "☼    █    ☼\n" +
                "☼    █    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldTankMoveLeftThenUpThenDown_onIce() {
        size = 11;
        tanks = new LinkedList<>(Arrays.asList(tank(5, 2, Direction.UP)));
        ice = new LinkedList<>(Arrays.asList(
                new Ice(5, 3),
                new Ice(5, 4),
                new Ice(5, 5)));

        givenGameWithTanks(tanks);
        game.setIce(ice);

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    █    ☼\n" +
                "☼    █    ☼\n" +
                "☼    █    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        //заежаем на лёд
        hero.up();
        game.tick();
        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    █    ☼\n" +
                "☼    █    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        //lEFT -> UP(скольжение)
        hero.left();
        game.tick();
        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    █    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼    █    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        //DOWN -> DOWN (выполнилась)
        hero.down();
        game.tick();
        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    █    ☼\n" +
                "☼    █    ☼\n" +
                "☼    ▼    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        //UP -> DOWN (скольжение)
        hero.up();
        game.tick();
        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    █    ☼\n" +
                "☼    █    ☼\n" +
                "☼    █    ☼\n" +
                "☼    ▼    ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    //2.2) также когда на нем двигается враг он проскальзывает команду на два тика
    @Test
    public void shouldOtherTankMoveLeftThenUpThenDown_onIce() {
        //given
        size = 11;
        Tank tankHero = tank(1, 1, Direction.UP);
        Tank otherTank = tank(5, 6, Direction.DOWN);
        //when
        tanks = Arrays.asList(tankHero, otherTank);
        ice = new LinkedList<>(Arrays.asList(
                new Ice(5, 3),
                new Ice(5, 4),
                new Ice(5, 5)));

        givenGameWithTanks(tanks);
        game.setIce(ice);
        //then
        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    ˅    ☼\n" +
                "☼    █    ☼\n" +
                "☼    █    ☼\n" +
                "☼    █    ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        //враг заежает на лёд
        otherTank.down();
        game.tick();
        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    ˅    ☼\n" +
                "☼    █    ☼\n" +
                "☼    █    ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        //lEFT -> DOWN(скольжение)
        otherTank.left();
        game.tick();
        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    █    ☼\n" +
                "☼    ˅    ☼\n" +
                "☼    █    ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        //UP -> UP (выполнилась)
        otherTank.up();
        game.tick();
        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    ˄    ☼\n" +
                "☼    █    ☼\n" +
                "☼    █    ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        //DOWN -> UP (скольжение)
        //сьезд со льда
        otherTank.down();
        game.tick();
        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    ˄    ☼\n" +
                "☼    █    ☼\n" +
                "☼    █    ☼\n" +
                "☼    █    ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    //2.3) также когда на нем двигается бот он проскальзывает команду на два тика

    //3. Река
    @Test
    public void shouldBeConstructionWater_whenGameCreated() {
	    //given
        tanks = new LinkedList<>(Arrays.asList(tank(1, 1, Direction.UP)));
        rivers = new LinkedList<>(Arrays.asList(new River(3, 3)));
        //when
        givenGameWithTanks(tanks);
        game.setRivers(rivers);
        //then
        assertEquals(1, game.getRivers().size());
        //then
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼  ▓  ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

	//3.1) река - через нее герою нельзя пройти. но можно стрелять
	@Test
	public void shouldTankCanGoIfRiverAtWay() {
	    //given
		tanks = new LinkedList<>(Arrays.asList(tank(1, 1, Direction.UP)));
		rivers = new LinkedList<>(Arrays.asList(new River(1, 2)));
        //when
        givenGameWithTanks(tanks);
        game.setRivers(rivers);
        //then
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▓    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.up();
        game.tick();
        hero.up();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▓    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

	@Test
	public void shouldBulletCanGoIfRiverAtWay() {
	    //given
		tanks = new LinkedList<>(Arrays.asList(tank(1, 1, Direction.UP)));
		rivers = new LinkedList<>(Arrays.asList(new River(1, 2)));
        //when
        givenGameWithTanks(tanks);
        game.setRivers(rivers);
        //then
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▓    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

		hero.up();
		game.tick();
		hero.act();
		game.tick();
		assertD("☼☼☼☼☼☼☼\n" +
				"☼     ☼\n" +
				"☼     ☼\n" +
				"☼•    ☼\n" +
				"☼▓    ☼\n" +
				"☼▲    ☼\n" +
				"☼☼☼☼☼☼☼\n");

		hero.right();
		hero.act();
		game.tick();
		assertD("☼☼☼☼☼☼☼\n" +
				"☼•    ☼\n" +
				"☼     ☼\n" +
				"☼     ☼\n" +
				"☼▓    ☼\n" +
				"☼ ►•  ☼\n" +
				"☼☼☼☼☼☼☼\n");

		game.tick();
		assertD("☼☼☼☼☼☼☼\n" +
				"☼     ☼\n" +
				"☼     ☼\n" +
				"☼     ☼\n" +
				"☼▓    ☼\n" +
				"☼ ►  •☼\n" +
				"☼☼☼☼☼☼☼\n");
	}

    @Test
    public void shouldDoNotMove_whenRiverToWay_goRightOrUpOrLeftOrDown() {
	    //given
        tanks = new LinkedList<>(Arrays.asList(tank(3, 3, Direction.UP)));
        rivers = new LinkedList<>(Arrays.asList(
                new River(2, 3),
                new River(4, 3),
                new River(3, 2),
                new River(3, 4)));
        //when
        givenGameWithTanks(tanks);
        game.setRivers(rivers);
        //then
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ▓  ☼\n" +
                "☼ ▓▲▓ ☼\n" +
                "☼  ▓  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.right();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ▓  ☼\n" +
                "☼ ▓►▓ ☼\n" +
                "☼  ▓  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.up();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ▓  ☼\n" +
                "☼ ▓▲▓ ☼\n" +
                "☼  ▓  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.left();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ▓  ☼\n" +
                "☼ ▓◄▓ ☼\n" +
                "☼  ▓  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

		hero.down();
		game.tick();
		assertD("☼☼☼☼☼☼☼\n" +
				"☼     ☼\n" +
				"☼  ▓  ☼\n" +
				"☼ ▓▼▓ ☼\n" +
				"☼  ▓  ☼\n" +
				"☼     ☼\n" +
				"☼☼☼☼☼☼☼\n");
    }

    //3.2) река - через нее врагу нельзя пройти. но можно стрелять
    @Test
    public void shouldOtherTankBullet_canGoIfRiverAtWay() {
	    //given
        Tank tankHero = tank(3, 2, Direction.UP);
        Tank otherTank = tank(1, 1, Direction.UP);

        tanks = Arrays.asList(tankHero, otherTank);
        rivers = new LinkedList<>(Arrays.asList(new River(1, 2)));
        //when
        givenGameWithTanks(tanks);
        game.setRivers(rivers);
        //then
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▓ ▲  ☼\n" +
                "☼˄    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        otherTank.up();
        game.tick();
        otherTank.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼▓ ▲  ☼\n" +
                "☼˄    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        otherTank.right();
        otherTank.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▓ ▲  ☼\n" +
                "☼ ˃•  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▓ ▲  ☼\n" +
                "☼ ˃  •☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldOtherTankDoNotMove_whenRiverToWay_goRightOrUpOrLeftOrDown() {
	    //given
        Tank tankHero = tank(5, 1, Direction.UP);
        Tank otherTank = tank(3, 3, Direction.UP);

        tanks = Arrays.asList(tankHero, otherTank);
        rivers = new LinkedList<>(Arrays.asList(
                new River(2, 3),
                new River(4, 3),
                new River(3, 2),
                new River(3, 4)));
        //when
        givenGameWithTanks(tanks);
        game.setRivers(rivers);
        //then
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ▓  ☼\n" +
                "☼ ▓˄▓ ☼\n" +
                "☼  ▓  ☼\n" +
                "☼    ▲☼\n" +
                "☼☼☼☼☼☼☼\n");

        otherTank.right();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ▓  ☼\n" +
                "☼ ▓˃▓ ☼\n" +
                "☼  ▓  ☼\n" +
                "☼    ▲☼\n" +
                "☼☼☼☼☼☼☼\n");

        otherTank.up();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ▓  ☼\n" +
                "☼ ▓˄▓ ☼\n" +
                "☼  ▓  ☼\n" +
                "☼    ▲☼\n" +
                "☼☼☼☼☼☼☼\n");

        otherTank.left();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ▓  ☼\n" +
                "☼ ▓˂▓ ☼\n" +
                "☼  ▓  ☼\n" +
                "☼    ▲☼\n" +
                "☼☼☼☼☼☼☼\n");

        otherTank.down();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ▓  ☼\n" +
                "☼ ▓˅▓ ☼\n" +
                "☼  ▓  ☼\n" +
                "☼    ▲☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    //3.3) река - через нее боту нельзя пройти. но можно стрелять
    @Test
    public void shouldAITankBullet_canGoIfRiverAtWay() {
	    //given
        Tank tankHero = tank(3, 2, Direction.UP);
        Tank aiTank = setAITank(1, 1, Direction.UP);

        tanks = Arrays.asList(tankHero, aiTank);
        rivers = new LinkedList<>(Arrays.asList(new River(1, 2)));
        //when
        givenGameWithTanks(tanks);
        game.setRivers(rivers);;
        //then
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▓ ▲  ☼\n" +
                "☼?    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        aiTank.up();
        game.tick();
        aiTank.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼▓ ▲  ☼\n" +
                "☼?    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        aiTank.right();
        aiTank.act();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▓ ▲  ☼\n" +
                "☼ »•  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▓ ▲  ☼\n" +
                "☼  » •☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▓ ▲  ☼\n" +
                "☼   » ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldAITankDoNotMove_whenRiverToWay_goRightOrUpOrLeftOrDown() {
	    //given
        Tank tankHero = tank(5, 1, Direction.UP);
        Tank aiTank = setAITank(3, 3, Direction.DOWN);

        tanks = Arrays.asList(tankHero, aiTank);
        rivers = new LinkedList<>(Arrays.asList(
                new River(2, 3),
                new River(4, 3),
                new River(3, 2),
                new River(3, 4)));
        //when
        givenGameWithTanks(tanks);
        game.setRivers(rivers);
        //then
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ▓  ☼\n" +
                "☼ ▓¿▓ ☼\n" +
                "☼  ▓  ☼\n" +
                "☼    ▲☼\n" +
                "☼☼☼☼☼☼☼\n");

        aiTank.up();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ▓  ☼\n" +
                "☼ ▓?▓ ☼\n" +
                "☼  ▓  ☼\n" +
                "☼    ▲☼\n" +
                "☼☼☼☼☼☼☼\n");

        aiTank.left();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ▓  ☼\n" +
                "☼ ▓«▓ ☼\n" +
                "☼  ▓  ☼\n" +
                "☼    ▲☼\n" +
                "☼☼☼☼☼☼☼\n");

        aiTank.down();
        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ▓  ☼\n" +
                "☼ ▓¿▓ ☼\n" +
                "☼  ▓  ☼\n" +
                "☼    ▲☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    //TODO 4. Добовляем бота
    //TODO    4.1) добавляем бота, который спаунится каждые N ходов (задается в сеттингах),
    //TODO         который цветной и его убить можно только за M выстрелов (тоже сеттинги)
    //TODO    4.2) во время смерти такого AI вываливается приз
    //создаем АИтанк с призами
    @Test
    public void shouldCreatedAiPrize() {
        size = 9;
        spawnAiPrize = setParameter("count spawn", 0);
        hitKillsAiPrize = setParameter("hits to kill", 3);
        Tank tank = tank(1, 1, Direction.UP);
        Tank aiTank = aiTankPrize(7, 7, Direction.DOWN, hitKillsAiPrize);
        givenGameWithAI(tank, aiTank);

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ¿☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        aiTank.down();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼      ¿☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        assertEquals(true, assertAiPrize(1, 2));
    }

    //У АИтанка с призами после 4-го хода должен смениться Element
    @Test
    public void shouldSwapElementAfterFourTicks() {
        size = 9;
        spawnAiPrize = setParameter("count spawn", 0);
        hitKillsAiPrize = setParameter("hits to kill", 3);
        Tank tank = tank(1, 1, Direction.UP);
        Tank aiTank = aiTankPrize(7, 7, Direction.DOWN, hitKillsAiPrize);
        givenGameWithAI(tank, aiTank);

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ¿☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        aiTank.down();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼      ¿☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        aiTank.left();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼    •« ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        aiTank.up();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼     ? ☼\n" +
                "☼  •    ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        aiTank.right();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼      »☼\n" +
                "☼•      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        aiTank.down();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼      ◘☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        assertEquals(true, assertAiPrize(1, 2));
    }

    //если spawnAiPrize = 3, а спаунится сразу 2 АИтанка, то 2-й должен быть АИтанком с призами
    @Test
    public void shouldSpawnAiPrizeWhenTwoAi() {
        size = 9;
        spawnAiPrize = setParameter("count spawn", 3);
        Tank tank = tank(1, 1, Direction.UP);
        Tank aiTank1 = aiTank(2, 7, Direction.DOWN);
        Tank aiTank2 = aiTank(7, 7, Direction.DOWN);
        givenGameWithAI(tank, aiTank1, aiTank2);

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼ ¿    ¿☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertEquals(true, assertAiPrize(1, 3));
    }

    //если spawnAiPrize = 3 и спаунится сразу 3 АИтанка, то 2-й должен быть АИтанком с призами
    @Test
    public void shouldSpawnAiPrizeWhenThreeAi() {
        size = 9;
        spawnAiPrize = setParameter("count spawn", 3);
        Tank tank = tank(1, 1, Direction.UP);
        Tank aiTank1 = aiTank(2, 7, Direction.DOWN);
        Tank aiTank2 = aiTank(5, 7, Direction.DOWN);
        Tank aiTank3 = aiTank(7, 7, Direction.DOWN);
        givenGameWithAI(tank, aiTank1, aiTank2, aiTank3);

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼ ¿  ¿ ¿☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertEquals(true, assertAiPrize(1, 4));
    }

    //если spawnAiPrize = 3, а спаунятся сразу 6 АИтанков, то должно быть 2 АИтанка с призами
    @Test
    public void shouldSpawnTwoAiPrizeWhenSixAi() {
        size = 9;
        spawnAiPrize = setParameter("count spawn", 3);
        Tank tank = tank(1, 1, Direction.UP);
        Tank aiTank1 = aiTank(2, 7, Direction.DOWN);
        Tank aiTank2 = aiTank(3, 7, Direction.DOWN);
        Tank aiTank3 = aiTank(4, 7, Direction.DOWN);
        Tank aiTank4 = aiTank(5, 7, Direction.DOWN);
        Tank aiTank5 = aiTank(6, 7, Direction.DOWN);
        Tank aiTank6 = aiTank(7, 7, Direction.DOWN);
        givenGameWithAI(tank, aiTank1, aiTank2, aiTank3, aiTank4, aiTank5, aiTank6);

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼ ¿¿¿¿¿¿☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertEquals(true, assertAiPrize(2, 7));
    }

    //если spawnAiPrize = 3, а 3 АИтанка спаунятся по 1-му за каждый ход,
    //то АИтанк с призами спаунится после 2-го хода
    @Test
    public void shouldSpawnAiPrizeWhenAddOneByOneAI() {
        size = 9;
        spawnAiPrize = setParameter("count spawn", 3);
        Tank tank = tank(1, 1, Direction.UP);
        Tank aiTank1 = aiTank(2, 7, Direction.DOWN);
        Tank aiTank2 = aiTank(5, 7, Direction.DOWN);
        Tank aiTank3 = aiTank(6, 7, Direction.DOWN);
        givenGameWithAI(tank);

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        game.addAI(aiTank1);
        aiTank1.down();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼ ¿     ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        game.addAI(aiTank2);
        aiTank2.down();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼    ¿  ☼\n" +
                "☼ ¿     ☼\n" +
                "☼ •     ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        game.addAI(aiTank3);
        aiTank3.down();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼     ¿ ☼\n" +
                "☼    ¿  ☼\n" +
                "☼ ¿  •  ☼\n" +
                "☼       ☼\n" +
                "☼ •     ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        assertEquals(true, assertAiPrize(1, 4));
    }

    //в АИтанк с призами надо попасть 3 раза, чтобы убить
    @Test
    public void shouldKillAiPrizeInThreeHits() {
        size = 7;
        spawnAiPrize = setParameter("count spawn", 0);
        hitKillsAiPrize = setParameter("hits to kill", 3);
        Tank tank = tank(1, 1, Direction.UP);
        Tank aiTank = aiTankPrize(1, 5, Direction.DOWN, hitKillsAiPrize);
        givenGameWithAI(tank, aiTank);

        assertD("☼☼☼☼☼☼☼\n" +
                "☼¿    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        aiTank.down();
        tank.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        aiTank.up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼?    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        aiTank.down();
        tank.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        aiTank.up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼?    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        aiTank.down();
        tank.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼◘    ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        aiTank.up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldDropPrizeInPointKilledAiPrize() {
        size = 7;
        givenGameBeforeDropPrize(pt(1,5));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼1    ☼\n" +
                "☼     ☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertEquals(true, assertPrize());

    }

    @Test
    public void shouldDropPrizeInFreePoint() {
        size = 7;
        givenGameBeforeDropPrize(pt(4,5));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼   1 ☼\n" +
                "☼     ☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertEquals(true, assertPrize());
    }

    @Test
    public void shouldNotDropPrizeInPointPlayerTank() {
        size = 7;
        givenGameBeforeDropPrize(pt(1,1));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertEquals(false, assertPrize());
    }

    @Test
    public void shouldNotDropPrizeInPointConstruction() {
        size = 7;
        givenGameBeforeDropPrize(pt(3,3));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertEquals(false, assertPrize());
    }

    @Test
    public void shouldNotDropPrizeInPointField() {
        size = 7;
        givenGameBeforeDropPrize(pt(0,2));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertEquals(false, assertPrize());
    }

    //TODO 5. Добавляем приз
    //TODO    5.1) добавляем приз неуязвимости, он работает L тиков (сеттинги) и теряется после
    //TODO    5.2) если в тебя выстрелят во время действия этого приза - ты остаешься жить
    //TODO 6. Ддобавляется приз пульки которые разбивают бетонную стену
    //TODO 7. Рендомная борда (тут надо подглядеть как в icancode подобное устроено) и сделать так же

}
