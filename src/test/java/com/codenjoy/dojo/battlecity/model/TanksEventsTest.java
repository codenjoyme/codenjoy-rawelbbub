package com.codenjoy.dojo.battlecity.model;

import com.codenjoy.dojo.battlecity.services.BattlecityEvents;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.PrinterFactory;
import com.codenjoy.dojo.services.PrinterFactoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;

/**
 * User: sanja
 * Date: 13.12.13
 * Time: 10:29
 */
public class TanksEventsTest {

    private Tank enemy;
    private Battlecity game;
    private EventListener events;
    private Player player;
    private Tank hero;
    private BattlecityTest utils = new BattlecityTest();
    private PrinterFactory printerFactory = new PrinterFactoryImpl();

    @Before
    public void setup() {
        enemy = utils.tank(1, 5, Direction.DOWN);

        game = new Battlecity(7, Arrays.asList(new Construction[0]), enemy);

        events = mock(EventListener.class);
        player = utils.player(1, 1, 2, 2, events);
        game.newGame(player);
        hero = player.getTank();
    }

    @Test
    public void shouldKillAiTankEvent() {
        assertD("☼☼☼☼☼☼☼\n" +
                "☼˅    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼˅    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        noEvents(events);

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        onlyEvent(events, BattlecityEvents.KILL_OTHER_TANK);
    }

    @Test
    public void shouldKillMyTankByAIEvent() {
        assertD("☼☼☼☼☼☼☼\n" +
                "☼˅    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        enemy.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼˅    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        noEvents(events);

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼˅    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        onlyEvent(events, BattlecityEvents.KILL_YOUR_TANK);
    }

    @Test
    public void shouldKillOtherPlayerTankEvent() {
        EventListener events2 = mock(EventListener.class);
        Player player2 = utils.player(5, 1, events2);
        game.newGame(player2);
        Tank tank2 = player2.getTank();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼˅    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲   ˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.right();
        hero.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼˅    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ►• ˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        noEvents(events);
        noEvents(events2);

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼˅    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ►  Ѡ☼\n" +
                "☼☼☼☼☼☼☼\n");

        onlyEvent(events, BattlecityEvents.KILL_OTHER_TANK);
        onlyEvent(events2, BattlecityEvents.KILL_YOUR_TANK);
    }

    @Test
    public void shouldKillMyTankByOtherPlayerTankEvent() {
        EventListener events2 = mock(EventListener.class);
        Player player2 = utils.player(5, 1, events2);
        game.newGame(player2);
        Tank tank2 = player2.getTank();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼˅    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲   ˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        tank2.left();
        tank2.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼˅    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲ •˂ ☼\n" +
                "☼☼☼☼☼☼☼\n");

        noEvents(events);
        noEvents(events2);

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼˅    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼Ѡ  ˂ ☼\n" +
                "☼☼☼☼☼☼☼\n");

        onlyEvent(events, BattlecityEvents.KILL_YOUR_TANK);
        onlyEvent(events2, BattlecityEvents.KILL_OTHER_TANK);
    }

    private void noEvents(EventListener ev) {
        Mockito.verifyNoMoreInteractions(ev);
        reset(events);
    }

    @Test
    public void shouldIKillOtherTankWhenKillMeByAi() {
        EventListener events2 = mock(EventListener.class);
        Player player2 = utils.player(5, 1, events2);
        game.newGame(player2);
        Tank tank2 = player2.getTank();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼˅    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲   ˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.turn(Direction.RIGHT);
        enemy.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼˅    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼►   ˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼˅    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼Ѡ • ˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        onlyEvent(events, BattlecityEvents.KILL_YOUR_TANK);
        noEvents(events2);

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼˅    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    ˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        noEvents(events);
        noEvents(events2);
    }

    private void onlyEvent(EventListener ev, BattlecityEvents event) {
        Mockito.verify(ev).event(event);
        noEvents(ev);
        reset(events);
    }

    private void assertD(String field) {
        assertEquals(field, printerFactory.getPrinter(
                game.reader(), player).print());
    }

    @Test
    public void shouldMyBulletsRemovesWhenKillMe() {
        assertD("☼☼☼☼☼☼☼\n" +
                "☼˅    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.turn(Direction.RIGHT);
        enemy.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼˅    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼►    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero.act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼˅    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼Ѡ •  ☼\n" +
                "☼☼☼☼☼☼☼\n");


        assertFalse(player.getTank().isAlive());
        game.newGame(player);
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼˅    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ►   ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

}
