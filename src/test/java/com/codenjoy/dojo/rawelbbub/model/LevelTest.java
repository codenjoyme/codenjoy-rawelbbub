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


import com.codenjoy.dojo.client.local.DiceGenerator;
import com.codenjoy.dojo.rawelbbub.services.GameRunner;
import com.codenjoy.dojo.rawelbbub.services.GameSettings;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.printer.Printer;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import org.junit.Test;

import static com.codenjoy.dojo.services.multiplayer.GamePlayer.DEFAULT_TEAM_ID;
import static com.codenjoy.dojo.services.multiplayer.LevelProgress.levelsStartsFrom1;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class LevelTest {

    private PrinterFactory printerFactory = new PrinterFactoryImpl();

    @Test
    public void test() {
        // given
        DiceGenerator generator = new DiceGenerator();
        GameRunner runner = new GameRunner() {
            @Override
            public Dice getDice() {
                return generator.getDice();
            }
        };
        GameSettings settings = runner.getSettings();
        GamePlayer player = runner.createPlayer(mock(EventListener.class),
                DEFAULT_TEAM_ID, "id", settings);
        GameField game = runner.createGame(levelsStartsFrom1, settings);
        player.newHero(game);

        assertEquals(23, game.reader().size());

        Printer printer = printerFactory.getPrinter(
                game.reader(), player);

        // when then
        assertEquals(
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼%%  ¿        w¿    %%☼\n" +
                "☼%                   %☼\n" +
                "☼    %  ╬╬    ╬╬      ☼\n" +
                "☼╬╬╬ %     %%    ###  ☼\n" +
                "☼  ~     ####   ╬╬╬#  ☼\n" +
                "☼  ~ ╬ %  ╬╬# %  ╬╬#~~☼\n" +
                "☼%        ╬╬# %   ~ ~~☼\n" +
                "☼%%%  ╬~~~~           ☼\n" +
                "☼     ╬~~~   ╬╬   ╬╬╬ ☼\n" +
                "☼  #              ╬   ☼\n" +
                "☼% #╬    #####      % ☼\n" +
                "☼% #╬╬    ╬╬╬   ╬  %% ☼\n" +
                "☼% #~~~        #╬     ☼\n" +
                "☼%   ~  %%  ╬~ #╬   ╬ ☼\n" +
                "☼    ╬ %%% ╬╬~ ##▲    ☼\n" +
                "☼           ~~        ☼\n" +
                "☼  ╬    ######   ╬### ☼\n" +
                "☼% ╬╬   #╬╬╬#  % ╬╬╬# ☼\n" +
                "☼% ~~   #╬╬         # ☼\n" +
                "☼%            ╬╬╬     ☼\n" +
                "☼%%%  ╬╬╬  %%     %%% ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n",
                printer.print());
    }

}
