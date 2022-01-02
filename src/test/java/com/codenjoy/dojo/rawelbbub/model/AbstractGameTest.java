package com.codenjoy.dojo.rawelbbub.model;

import com.codenjoy.dojo.rawelbbub.TestGameSettings;
import com.codenjoy.dojo.rawelbbub.model.items.AI;
import com.codenjoy.dojo.rawelbbub.services.Event;
import com.codenjoy.dojo.rawelbbub.services.GameRunner;
import com.codenjoy.dojo.rawelbbub.services.GameSettings;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.multiplayer.LevelProgress;
import com.codenjoy.dojo.services.printer.Printer;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import com.codenjoy.dojo.utils.events.EventsListenersAssert;
import org.junit.After;
import org.junit.Before;
import org.mockito.stubbing.OngoingStubbing;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AbstractGameTest {
    protected Dice dice;
    private Rawelbbub game;
    private List<Player> players;
    private PrinterFactory printerFactory;
    private GameSettings settings;
    private List<Hero> heroes;
    private List<EventListener> listeners;
    private EventsListenersAssert events;
    private Level level;

    public Dice dice(int... values) {
        OngoingStubbing<Integer> when = when(dice.next(anyInt()));
        for (int value : values) {
            when = when.thenReturn(value);
        }
        return dice;
    }

    @Before
    public void setup() {
        listeners = new LinkedList<>();
        players = new LinkedList<>();
        heroes = new LinkedList<>();

        dice = mock(Dice.class);
        settings = new TestGameSettings();
        printerFactory = new PrinterFactoryImpl();
        events = new EventsListenersAssert(() -> listeners, Event.class);
    }

    @After
    public void after() {
        verifyAllEvents("");
    }

    public Hero hero(int index) {
        return heroes.get(index);
    }

    public void givenFl(String map) {
        int levelNumber = LevelProgress.levelsStartsFrom1;
        settings.setLevelMaps(levelNumber, new String[]{map});
        level = settings.level(levelNumber, dice, Level::new);

        GameRunner runner = new GameRunner() {
            @Override
            public Dice getDice() {
                return dice;
            }

            @Override
            public GameSettings getSettings() {
                return settings;
            }
        };
        game = (Rawelbbub) runner.createGame(levelNumber, settings);

        level.heroes()
                .forEach(hero -> game.newGame(initPlayer(hero)));

        game.ais().stream()
                .filter(ai -> ai instanceof AI)
                .map(ai -> (AI) ai)
                .forEach(ai -> {
                    ai.dontShoot = true;
                    ai.dontMove = true;
                });

        heroes = game.heroes();
    }

    public AI dropAI(Point pt) {
        AI ai = game.getAiGenerator().drop(pt);
        ai.dontMove = true;
        ai.dontShoot = true;
        return ai;
    }

    public void verifyAllEvents(String expected) {
        assertEquals(expected, events.getEvents());
    }

    private Player initPlayer(Hero hero) {
        EventListener listener = mock(EventListener.class);
        listeners.add(listener);

        Player player = new Player(listener, settings);
        player.setHero(hero);
        players.add(player);

        return player;
    }

    public String getPrizesCount() {
        List<Hero> all = game.heroesAndAis();
        long prizes = all.stream().filter(Hero::withPrize).count();

        return String.format("%s prizes with %s heroes", prizes, all.size());
    }

    public void assertD(String field) {
        assertEquals(field, getPrinter().print());
    }

    private Printer<String> getPrinter() {
        return printerFactory.getPrinter(
                game.reader(), players.get(0));
    }

    public Rawelbbub game() {
        return game;
    }

    public GameSettings settings() {
        return settings;
    }

    public void assertW(String expected) {
        Printer<String> printer = getPrinter();
        assertEquals(expected, printer.print().replaceAll("[«¿»?•]", " "));
    }

    public Player player(int index) {
        return players.get(index);
    }
}
