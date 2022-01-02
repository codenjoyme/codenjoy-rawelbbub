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


import com.codenjoy.dojo.rawelbbub.model.items.AI;
import com.codenjoy.dojo.rawelbbub.model.items.Bullet;
import com.codenjoy.dojo.rawelbbub.model.items.Wall;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.rawelbbub.TestGameSettings.*;
import static com.codenjoy.dojo.rawelbbub.services.GameSettings.Keys.*;
import static com.codenjoy.dojo.services.PointImpl.pt;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class GameTest extends AbstractGameTest {
    
    @Test
    public void shouldDrawField() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }
    
    // рисуем стенку
    @Test
    public void shouldBeWall_whenGameCreated() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertEquals(1, field().walls().size());

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // рисуем мой танк
    @Test
    public void shouldBeHeroOnFieldWhenGameCreated() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertNotNull(field().heroesAndAis());
    }

    @Test
    public void shouldResetSlidingTicksWhenLeaveIce() {
        settings().integer(SLIPPERINESS, 2);

        givenFl("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ###   ☼\n" +
                "☼    ▲    ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ###   ☼\n" +
                "☼   #▲#   ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // LEFT -> UP [sliding]
        hero(0).left();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ###   ☼\n" +
                "☼   #▲#   ☼\n" +
                "☼   ###   ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // LEFT -> UP [sliding]
        hero(0).left();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ###   ☼\n" +
                "☼   #▲#   ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ###   ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // LEFT -> LEFT
        hero(0).left();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ◄##   ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ###   ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // UP -> LEFT [sliding]
        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ###   ☼\n" +
                "☼  ◄###   ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ###   ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // sliding state should be reset because the hero left ice

        // RIGHT -> RIGHT
        hero(0).right();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ►##   ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ###   ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // DOWN -> RIGHT [sliding]
        hero(0).down();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ###   ☼\n" +
                "☼   #►#   ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ###   ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // DOWN -> RIGHT [sliding]
        hero(0).down();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ##►   ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ###   ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // DOWN -> DOWN [sliding]
        hero(0).down();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ##▼   ☼\n" +
                "☼   ###   ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // RIGHT -> DOWN [sliding]
        hero(0).right();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ##▼   ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // RIGHT -> RIGHT
        hero(0).right();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ###►  ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");
    }
    
    @Test
    public void bulletDirectionShouldBeAffectedBySliding() {
        settings().integer(SLIPPERINESS, 2);

        givenFl("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ###   ☼\n" +
                "☼    ▲    ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).up();
        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ###   ☼\n" +
                "☼   #•#   ☼\n" +
                "☼   #▲#   ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // RIGHT -> UP [sliding]
        hero(0).right();
        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ###   ☼\n" +
                "☼   #•#   ☼\n" +
                "☼   #•#   ☼\n" +
                "☼   #▲#   ☼\n" +
                "☼   ###   ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // RIGHT -> UP [sliding]
        hero(0).right();
        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼   ###   ☼\n" +
                "☼   #•#   ☼\n" +
                "☼   #•#   ☼\n" +
                "☼   #•#   ☼\n" +
                "☼   #▲#   ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ###   ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // LEFT -> LEFT
        hero(0).left();
        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼    •    ☼\n" +
                "☼   #•#   ☼\n" +
                "☼   #•#   ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ###   ☼\n" +
                "☼  •◄##   ☼\n" +
                "☼   ###   ☼\n" +
                "☼   ###   ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    @Test
    public void bulletDirectionShouldBeAffectedBySliding2() {
        settings().integer(SLIPPERINESS, 2);

        givenFl("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼ ####    ☼\n" +
                "☼ ####   ◄☼\n" +
                "☼ ####    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).left();
        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼ ####    ☼\n" +
                "☼ #### •◄ ☼\n" +
                "☼ ####    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).left();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼ ####    ☼\n" +
                "☼ ###• ◄  ☼\n" +
                "☼ ####    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).left();
        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼ ####    ☼\n" +
                "☼ #•#•◄   ☼\n" +
                "☼ ####    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).left();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼ ####    ☼\n" +
                "☼•#•#◄    ☼\n" +
                "☼ ####    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // UP -> LEFT [sliding]
        hero(0).up();
        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼ ####    ☼\n" +
                "☼•#•◄#    ☼\n" +
                "☼ ####    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // UP -> LEFT [sliding]
        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼ ####    ☼\n" +
                "☼•#◄##    ☼\n" +
                "☼ ####    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // UP -> UP
        hero(0).up();
        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼  •      ☼\n" +
                "☼ #▲##    ☼\n" +
                "☼ ####    ☼\n" +
                "☼ ####    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldHeroCanGoIfIceAtWayWithoutSliding_whenHeroTakePrize() {
        settings().integer(PRIZE_ON_FIELD, 5)
                .integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_WORKING, 6)
                .integer(SLIPPERINESS, 1);

        givenFl("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    ?    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    Ѡ    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        dice(DICE_NO_SLIDING);
        field().tick();
        verifyAllEvents("");

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    5    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        verifyAllEvents("");

        hero(0).up();
        field().tick();

        assertPrize(hero(0), "[PRIZE_NO_SLIDING]");
        verifyAllEvents("[CATCH_PRIZE[5]]");

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // заезжаем на лед
        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        //  DOWN -> UP но так как игрок взял приз скольжение не происходит, по этому DOWN -> DOWN
        hero(0).down();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    ▼    ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldHeroCanGoIfAceAtWay_whenPrizeWorkingEnd() {
        settings().integer(PRIZE_ON_FIELD, 5)
                .integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_WORKING, 2)
                .integer(SLIPPERINESS, 3);

        givenFl("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    ?    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    Ѡ    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        dice(DICE_NO_SLIDING);
        field().tick();
        verifyAllEvents("");

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    5    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        verifyAllEvents("");

        hero(0).up();
        field().tick();

        assertPrize(hero(0), "[PRIZE_NO_SLIDING]");
        verifyAllEvents("[CATCH_PRIZE[5]]");

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // заезжаем на лед
        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // так как игрок взял приз скольжение не происходит, по этому UP -> UP
        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼    #    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // так как игрок взял приз скольжение не происходит, по этому UP -> UP
        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // действие приза окончилось
        assertPrize(hero(0), "[]");

        // мы снова на льду, начинаем занос запоминаем команду
        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // RIGHT -> UP занос
        hero(0).right();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // RIGHT -> RIGHT
        hero(0).right();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #►   ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldHeroCanGoIfAceAtWay_whenHeroTackPrizeSlidingEnd() {
        settings().integer(PRIZE_ON_FIELD, 4)
                .integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_WORKING, 6)
                .integer(SLIPPERINESS, 5);

        givenFl("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    ¿    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼         ☼\n" +
                "☼    ▲    ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        ai(0).down();
        hero(0).up();
        ai(0).dontShoot = true;
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    ¿    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");


        ai(0).down();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    #    ☼\n" +
                "☼    ¿    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    #    ☼\n" +
                "☼    Ѡ    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        dice(DICE_NO_SLIDING);
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    #    ☼\n" +
                "☼    5    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    #    ☼\n" +
                "☼    !    ☼\n" +
                "☼    #    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // RIGHT -> UP выполняется занос
        hero(0).right();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    #    ☼\n" +
                "☼    5    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼    #    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        verifyAllEvents("");

        // RIGHT -> UP выполняется занос
        hero(0).right();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    #    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[PRIZE_NO_SLIDING]");
        verifyAllEvents("[CATCH_PRIZE[5]]");

        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    ▲    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).down();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    #    ☼\n" +
                "☼    ▼    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).down();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    ▼    ☼\n" +
                "☼    #    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).right();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #►   ☼\n" +
                "☼    #    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldHeroMove() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).down();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).right();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ►   ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).left();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼◄    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldHeroStayAtPreviousPositionWhenIsNearBorder() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼    ▼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼˄    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).up();
        hero(0).up();
        hero(1).down();
        hero(1).down();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼    ▲☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼˅    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).right();
        hero(0).right();
        hero(1).left();
        hero(1).left();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼    ►☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼˂    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldBulletHasSameDirectionAsHero() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertEquals(hero(0).getBullets().iterator().next().getDirection(),
                hero(0).getDirection());
    }

    @Test
    public void shouldBulletGoInertiaWhenHeroChangeDirection() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼  ▲  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼  •  ☼\n" +
                "☼     ☼\n" +
                "☼  ▲  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).right();
        field().tick();

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
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();
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
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼►    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼►   •☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

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
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼    ◄☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼•   ◄☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

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
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼    ▼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼    ▼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    •☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

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
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╩    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╩    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╨    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╨    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

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
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼►   ╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼► • ╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼►   ╠☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼► • ╠☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼►   ╞☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼► • ╞☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

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
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬   ◄☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬ • ◄☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╣   ◄☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╣ • ◄☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╡   ◄☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╡ • ◄☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

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
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼╬    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╦    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼╦    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╥    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼╥    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

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
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╩    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╩    ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╨    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╨    ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

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
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ►  ╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ► •╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ►  ╠☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ► •╠☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ►  ╞☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ► •╞☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

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
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬  ◄ ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬• ◄ ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╣  ◄ ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╣• ◄ ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╡  ◄ ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╡• ◄ ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

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
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼╬    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╦    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼╦    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╥    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼╥    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

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
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╬    ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╩    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╩    ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╨    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╨    ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

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
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼►  ╬╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼►  ╬╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼► •╬╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼►  ╠╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼► •╠╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼►  ╞╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼► •╞╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼►   ╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼► • ╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

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
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬╬  ◄☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬╬  ◄☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬╬• ◄☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬╣  ◄☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬╣• ◄☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬╡  ◄☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬╡• ◄☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬   ◄☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬ • ◄☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

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
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼    ▼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    ╬☼\n" +
                "☼    ╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼    ▼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    ╬☼\n" +
                "☼    ╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼    ▼☼\n" +
                "☼     ☼\n" +
                "☼    •☼\n" +
                "☼    ╬☼\n" +
                "☼    ╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼    ▼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    ╦☼\n" +
                "☼    ╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼    ▼☼\n" +
                "☼     ☼\n" +
                "☼    •☼\n" +
                "☼    ╦☼\n" +
                "☼    ╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼    ▼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    ╥☼\n" +
                "☼    ╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼    ▼☼\n" +
                "☼     ☼\n" +
                "☼    •☼\n" +
                "☼    ╥☼\n" +
                "☼    ╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼    ▼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    ╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼    ▼☼\n" +
                "☼     ☼\n" +
                "☼    •☼\n" +
                "☼     ☼\n" +
                "☼    ╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

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
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ╬  ☼\n" +
                "☼ ╬►╬ ☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).right();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ╬  ☼\n" +
                "☼ ╬►╬ ☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ╬  ☼\n" +
                "☼ ╬▲╬ ☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).left();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ╬  ☼\n" +
                "☼ ╬◄╬ ☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).down();
        field().tick();

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

        hero(0).right();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ╩  ☼\n" +
                "☼ ╣►╠ ☼\n" +
                "☼  ╦  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ╩  ☼\n" +
                "☼ ╣▲╠ ☼\n" +
                "☼  ╦  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).left();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ╩  ☼\n" +
                "☼ ╣◄╠ ☼\n" +
                "☼  ╦  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).down();
        field().tick();

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
        hero(0).up();
        field().tick();
        hero(0).fire();
        field().tick();

        hero(0).left();
        field().tick();
        hero(0).fire();
        field().tick();

        hero(0).right();
        field().tick();
        hero(0).fire();
        field().tick();

        hero(0).down();
        field().tick();
        hero(0).fire();
        field().tick();
    }

    // если я стреляю дважды, то выпускается два снаряда
    // при этом я проверяю, что они уничтожаются в порядке очереди
    @Test
    public void shouldShotWithSeveralBullets_whenHittingTheWallDown() {
        givenFl("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ▼☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼      ╬☼\n" +
                "☼      ╬☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ▼☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼      ╬☼\n" +
                "☼      ╬☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ▼☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼      ╬☼\n" +
                "☼      ╬☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ▼☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼      ╦☼\n" +
                "☼      ╬☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ▼☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼      ╥☼\n" +
                "☼      ╬☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ▼☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼      ╬☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ▼☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼      ╬☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ▼☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼      ╦☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ▼☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼      ╥☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ▼☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ▼☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ▼☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ▼☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ▼☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        field().tick();

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
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ▼  ☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ▼  ☼\n" +
                "☼  ╦  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).right();
        field().tick();

        hero(0).down();
        field().tick();

        hero(0).down();
        field().tick();

        hero(0).left();
        field().tick();

        hero(0).up();
        field().tick();

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼  ─  ☼\n" +
                "☼  ▲  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

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
    public void shouldStopWhenBeforeOtherHero() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼˄    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).down();
        hero(1).up();
        field().tick();

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
    public void shouldDieWhenOtherHeroKillMe() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼˅    ☼\n" +
                "☼˄    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertEquals(true, hero(0).isAlive());
        assertEquals(true, hero(1).isAlive());
        assertEquals(true, hero(2).isAlive());

        hero(0).fire();
        field().tick();

        verifyAllEvents(
                "listener(0) => [KILL_OTHER_HERO[1]]\n" +
                "listener(1) => [HERO_DIED]\n");

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼˄    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertEquals(true, hero(0).isAlive());
        assertEquals(false, hero(1).isAlive());
        assertEquals(true, hero(2).isAlive());

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼˄    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        verifyAllEvents(
                "listener(0) => [KILL_OTHER_HERO[2]]\n" +
                "listener(2) => [HERO_DIED]\n");

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertEquals(true, hero(0).isAlive());
        assertEquals(false, hero(1).isAlive());
        assertEquals(false, hero(2).isAlive());

        field().tick();

        assertEquals(true, hero(0).isAlive());
        assertEquals(false, hero(1).isAlive());
        assertEquals(false, hero(2).isAlive());
    }

    @Test
    public void killHeroForward(){
        givenFl("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼   ►˃    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).fire();
        hero(0).right();
        hero(1).right();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼   ► Ѡ   ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        verifyAllEvents(
                "listener(0) => [KILL_OTHER_HERO[1]]\n" +
                "listener(1) => [HERO_DIED]\n");
    }

    @Test
    public void killHeroBackward(){
        givenFl("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼   ►˃    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).right();
        hero(1).fire();
        hero(1).left();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼   Ѡ˂    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        verifyAllEvents(
                "listener(0) => [HERO_DIED]\n" +
                "listener(1) => [KILL_OTHER_HERO[1]]\n");
    }


    @Test
    public void shouldDieOnceWhenIsDamagedByManyBullets() {
        givenFl("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    ▲ ˂  ☼\n" +
                "☼         ☼\n" +
                "☼    ˄    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(1).fire();
        hero(2).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    Ѡ ˂  ☼\n" +
                "☼         ☼\n" +
                "☼    ˄    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        field().tick();

        verifyAllEvents(
                "listener(0) => [HERO_DIED]\n" +
                "listener(1) => [KILL_OTHER_HERO[1]]\n" +
                "listener(2) => [KILL_OTHER_HERO[1]]\n");

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼      ˂  ☼\n" +
                "☼         ☼\n" +
                "☼    ˄    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    // стоять, если меня убили
    @Test
    public void shouldStopWhenKill() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼˄    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        hero(1).left();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents(
                "listener(0) => [KILL_OTHER_HERO[1]]\n" +
                "listener(1) => [HERO_DIED]\n");
    }

    @Test
    public void shouldNoConcurrentException() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼˄    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(1).fire();
        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();
        field().tick();

        verifyAllEvents(
                "listener(0) => [KILL_OTHER_HERO[1], HERO_DIED]\n" +
                "listener(1) => [HERO_DIED, KILL_OTHER_HERO[1]]\n");

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
        givenFl("☼☼☼☼☼☼☼☼☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼˄      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        hero(0).fire();
        hero(1).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼•      ☼\n" +
                "☼       ☼\n" +
                "☼•      ☼\n" +
                "☼       ☼\n" +
                "☼˄      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼Ѡ      ☼\n" +
                "☼       ☼\n" +
                "☼˄      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        field().tick();

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
        givenFl("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼˄      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        hero(0).fire();
        hero(1).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼•      ☼\n" +
                "☼•      ☼\n" +
                "☼       ☼\n" +
                "☼˄      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼Ѡ      ☼\n" +
                "☼       ☼\n" +
                "☼˄      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        field().tick();

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

    @Test
    public void shouldRemoveAIWhenKillIt() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼˄    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲   ˄☼\n" +
                "☼☼☼☼☼☼☼\n");


        assertD("☼☼☼☼☼☼☼\n" +
                "☼˄    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲   ˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼˄    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲   ˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents("");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲   ˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents(
                "listener(0) => [KILL_OTHER_HERO[1]]\n" +
                "listener(1) => [HERO_DIED]\n");

        hero(0).right();
        field().tick();

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ► •˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents("");

        field().tick();

        assertW("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ►  Ѡ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents(
                "listener(0) => [KILL_OTHER_HERO[2]]\n" +
                "listener(2) => [HERO_DIED]\n");

        field().tick();

        assertW("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ►   ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents("");
    }

    @Test
    public void shouldRegenerateDestroyedWall() {
        shouldBulletDestroyWall_whenHittingTheWallUp_whenTwoWalls();

        hero(0).fire();
        field().tick();
        hero(0).fire();
        field().tick();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        for (int i = 7; i <= settings().integer(WALL_REGENERATE_TIME); i++) {
            field().tick();
        }

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();
        field().tick();
        field().tick();
        field().tick();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldHeroCantGoIfWallAtWay() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).up();
        field().tick();

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
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).right();
        hero(0).fire();
        field().tick();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼    ☼\n" +
                "☼ ►  •☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

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
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬    ☼\n" +
                "☼╬    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        hero(0).fire();
        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬    ☼\n" +
                "☼╩    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        hero(0).fire();
        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬    ☼\n" +
                "☼╨    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬    ☼\n" +
                "☼╨    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldHeroCanFireIfAtWayEnemyBullet() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼˄    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        hero(1).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼˄    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldHeroCanGoIfDestroyWall() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬    ☼\n" +
                "☼╬    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        hero(0).fire();
        field().tick();

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬    ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldWallCantRegenerateOnHero() {
        shouldHeroCanGoIfDestroyWall();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬    ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        for (int i = 3; i <= settings().integer(WALL_REGENERATE_TIME); i++) {
            field().tick();
        }

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬    ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).right();
        field().tick();

        for (int i = 2; i <= settings().integer(WALL_REGENERATE_TIME); i++) {
            assertD("☼☼☼☼☼☼☼\n" +
                    "☼     ☼\n" +
                    "☼     ☼\n" +
                    "☼╬    ☼\n" +
                    "☼ ►   ☼\n" +
                    "☼     ☼\n" +
                    "☼☼☼☼☼☼☼\n");

            field().tick();
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
    public void shouldWallCantRegenerateOnBullet() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        hero(0).fire();
        field().tick();

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        for (int i = 3; i <= settings().integer(WALL_REGENERATE_TIME); i++) {
            field().tick();
        }

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();
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
        settings().integer(HERO_TICKS_PER_SHOOT, 4);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        String field =
                "☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╩    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n";
        assertD(field);

        for (int i = 1; i < settings().integer(HERO_TICKS_PER_SHOOT); i++) {
            hero(0).fire();
            field().tick();

            assertD(field);
        }

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╨    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldNewAIWhenKillOther() {
        settings().integer(KILL_HITS_AI_PRIZE, 1);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼¿¿   ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼¿¿   ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼Ѡ¿   ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
        verifyAllEvents("[KILL_AI]");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼1¿   ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        dice(5, 5);
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼!¿   ☼\n" +
                "☼    ¿☼\n" +
                "☼    •☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldOnlyRotateIfNoBarrier() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲ ╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).right();
        field().tick();

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
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ▲╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).right();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ►╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldEnemyCanKillHeroOnWall() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼╬    ☼\n" +
                "☼˄    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(1).fire();
        field().tick();
        hero(1).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼╨    ☼\n" +
                "☼˄    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(1).fire();
        hero(1).up();  // команда поигнорится потому что вначале ходят все танки, а потом летят все снаряды
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼˄    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(1).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼˄    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        verifyAllEvents(
                "listener(0) => [KILL_OTHER_HERO[1]]\n" +
                "listener(1) => [HERO_DIED]\n");

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldDieWhenMoveOnBullet() {
        givenFl("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼˄      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼•      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼˄      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        hero(1).up();
        field().tick();

        verifyAllEvents(
                "listener(0) => [KILL_OTHER_HERO[1]]\n" +
                "listener(1) => [HERO_DIED]\n");

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
        givenFl("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼˄      ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼•      ☼\n" +
                "☼       ☼\n" +
                "☼˄      ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        hero(1).up();
        field().tick();

        verifyAllEvents(
                "listener(0) => [KILL_OTHER_HERO[1]]\n" +
                "listener(1) => [HERO_DIED]\n");

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
        givenFl("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼˄      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼•      ☼\n" +
                "☼˄      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        hero(1).up();
        field().tick();

        verifyAllEvents(
                "listener(0) => [KILL_OTHER_HERO[1]]\n" +
                "listener(1) => [HERO_DIED]\n");

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
    public void shouldRemoveBulletsAndResetWalls_whenReset() {
        settings().integer(HERO_TICKS_PER_SHOOT, 3);

        givenFl("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼╬        ☼\n" +
                "☼╬        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

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

        field().tick();

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

        field().tick();

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

        hero(0).fire();
        field().tick();

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

        hero(0).fire(); // не выйдет
        field().tick();

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

        field().tick();

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

        hero(0).fire();
        field().tick();

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

        field().tick();

        field().tick();

        hero(0).fire();
        field().tick();

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

        field().tick();

        field().tick();

        hero(0).fire();
        field().tick();

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
        Player player = player(0);
        player.setKilled(5);

        // when
        field().clearScore();

        // смогу стрельнуть, пушка ресетнется
        hero(0).fire();
        field().tick();

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

        field().tick();

        field().tick();

        hero(0).fire();
        field().tick();

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
        settings().integer(HERO_TICKS_PER_SHOOT, 4);

        givenFl("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        field().clearScore();

        field().tick(); // внутри там тикает так же gun, но первого выстрела еще небыло
        field().tick();

        hero(0).fire();
        field().tick();

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

        hero(0).fire();
        field().tick();

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

        hero(0).fire();
        field().tick();

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

        hero(0).fire();
        field().tick();

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

    // дерево
    @Test
    public void shouldBeWallTree_whenGameCreated() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼  %  ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertEquals(1, field().trees().size());

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼  %  ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldBeWallTwoTree_whenGameCreated() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼  %  ☼\n" +
                "☼     ☼\n" +
                "☼▲   %☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertEquals(2, field().trees().size());

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼  %  ☼\n" +
                "☼     ☼\n" +
                "☼▲   %☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // При выстреле пуля должна пролетать сквозь дерево
    @Test
    public void shouldBulletFlyUnderTree_right() {
        givenFl("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼►    %   ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).right();
        hero(0).fire();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼►    %   ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼ ►•  %   ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼ ►  •%   ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼ ►   %•  ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼ ►   %  •☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼ ►   %   ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldBulletDestroyWallUnderTree_whenHittingTheWallUp_whenTwoWalls() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼%    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼%    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╬    ☼\n" +
                "☼•    ☼\n" +
                "☼%    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╩    ☼\n" +
                "☼     ☼\n" +
                "☼%    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╩    ☼\n" +
                "☼•    ☼\n" +
                "☼%    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╨    ☼\n" +
                "☼     ☼\n" +
                "☼%    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╨    ☼\n" +
                "☼•    ☼\n" +
                "☼%    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼%    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼%    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╩    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼%    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    private List<Wall> walls(Wall... walls) {
        return new LinkedList<>(Arrays.asList(walls));
    }

    // Когда пуля и дерево находятся в одной координате
    // и я не вижу свой танк под деревом, то и пулю все равно не вижу
    @Test
    public void shouldBulletFlyUnderTwoTree_up_caseShowMyHeroUnderTree() {
        // given
        settings().bool(SHOW_MY_HERO_UNDER_TREE, true);

        // when then
        shouldBulletFlyUnderTwoTree_up();
    }

    // Когда пуля и дерево находятся в одной координате
    // я не вижу ее, дерево скрывает
    @Test
    public void shouldBulletFlyUnderTwoTree_up() {
        givenFl("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    %    ☼\n" +
                "☼    %    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    ▲    ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).fire();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    %    ☼\n" +
                "☼    %    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    ▲    ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    %    ☼\n" +
                "☼    %    ☼\n" +
                "☼         ☼\n" +
                "☼    •    ☼\n" +
                "☼         ☼\n" +
                "☼    ▲    ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    %    ☼\n" +
                "☼    %    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    ▲    ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    •    ☼\n" +
                "☼    %    ☼\n" +
                "☼    %    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    ▲    ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼    •    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    %    ☼\n" +
                "☼    %    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    ▲    ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    %    ☼\n" +
                "☼    %    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    ▲    ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    // дерево - когда игрок заходит под него, там видно дерево и больше никакого движения
    @Test
    public void shouldHeroMove_underTree_case2() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼%    ☼\n" +
                "☼%    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼%    ☼\n" +
                "☼%    ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼%    ☼\n" +
                "☼%    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼%    ☼\n" +
                "☼%    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).right();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼%►   ☼\n" +
                "☼%    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).right();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼% ►  ☼\n" +
                "☼%    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // дерево - когда игрок заходит под него,
    // и в настройках сказано, что свой танк виден под деревом
    // я вижу свой танк
    @Test
    public void shouldHeroMove_underTree_caseShowMyHeroUnderTree_case2() {
        settings().bool(SHOW_MY_HERO_UNDER_TREE, true);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼%    ☼\n" +
                "☼%    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼%    ☼\n" +
                "☼%    ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼%    ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼%    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).right();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼%►   ☼\n" +
                "☼%    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).right();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼% ►  ☼\n" +
                "☼%    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldBulletFlyUnderTree_jointly_shouldHeroMoveUnderTree() {
        settings().bool(SHOW_MY_HERO_UNDER_TREE, true);

        givenFl("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼        %☼\n" +
                "☼        %☼\n" +
                "☼         ☼\n" +
                "☼        %☼\n" +
                "☼         ☼\n" +
                "☼        ▲☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).fire();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼        %☼\n" +
                "☼        %☼\n" +
                "☼         ☼\n" +
                "☼        %☼\n" +
                "☼         ☼\n" +
                "☼        ▲☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼        %☼\n" +
                "☼        %☼\n" +
                "☼         ☼\n" +
                "☼        %☼\n" +
                "☼        ▲☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼        %☼\n" +
                "☼        %☼\n" +
                "☼         ☼\n" +
                "☼        ▲☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼        •☼\n" +
                "☼        %☼\n" +
                "☼        %☼\n" +
                "☼        ▲☼\n" +
                "☼        %☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼        •☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼        %☼\n" +
                "☼        ▲☼\n" +
                "☼         ☼\n" +
                "☼        %☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼        ▲☼\n" +
                "☼        %☼\n" +
                "☼         ☼\n" +
                "☼        %☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).left();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼       ◄%☼\n" +
                "☼        %☼\n" +
                "☼         ☼\n" +
                "☼        %☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).left();
        field().tick();

        hero(0).left();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼     ◄  %☼\n" +
                "☼        %☼\n" +
                "☼         ☼\n" +
                "☼        %☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldBulletFlyUnderTree_jointly() {

        givenFl("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼        %☼\n" +
                "☼        %☼\n" +
                "☼         ☼\n" +
                "☼        %☼\n" +
                "☼         ☼\n" +
                "☼        ▲☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).fire();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼        %☼\n" +
                "☼        %☼\n" +
                "☼         ☼\n" +
                "☼        %☼\n" +
                "☼         ☼\n" +
                "☼        ▲☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼        %☼\n" +
                "☼        %☼\n" +
                "☼         ☼\n" +
                "☼        %☼\n" +
                "☼        ▲☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼        %☼\n" +
                "☼        %☼\n" +
                "☼         ☼\n" +
                "☼        %☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼        •☼\n" +
                "☼        %☼\n" +
                "☼        %☼\n" +
                "☼        ▲☼\n" +
                "☼        %☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼        •☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼        %☼\n" +
                "☼        %☼\n" +
                "☼         ☼\n" +
                "☼        %☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼        %☼\n" +
                "☼        %☼\n" +
                "☼         ☼\n" +
                "☼        %☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).left();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼       ◄%☼\n" +
                "☼        %☼\n" +
                "☼         ☼\n" +
                "☼        %☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).left();
        field().tick();

        hero(0).left();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼     ◄  %☼\n" +
                "☼        %☼\n" +
                "☼         ☼\n" +
                "☼        %☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    // так же не видно меня под деревом
    @Test
    public void shouldHeroMove_underTree() {
		givenFl("☼☼☼☼☼☼☼☼☼☼☼\n" +
				"☼▼        ☼\n" +
				"☼         ☼\n" +
				"☼%        ☼\n" +
				"☼%        ☼\n" +
				"☼         ☼\n" +
				"☼         ☼\n" +
				"☼         ☼\n" +
				"☼         ☼\n" +
				"☼˄        ☼\n" +
				"☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).down();
        field().tick();

		assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
				"☼         ☼\n" +
				"☼▼        ☼\n" +
				"☼%        ☼\n" +
				"☼%        ☼\n" +
				"☼         ☼\n" +
				"☼         ☼\n" +
				"☼         ☼\n" +
				"☼         ☼\n" +
				"☼˄        ☼\n" +
				"☼☼☼☼☼☼☼☼☼☼☼\n");

		hero(0).down();
		field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼%        ☼\n" +
                "☼%        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼˄        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

		hero(0).down();
		field().tick();

		assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
				"☼         ☼\n" +
				"☼         ☼\n" +
				"☼%        ☼\n" +
				"☼%        ☼\n" +
				"☼         ☼\n" +
				"☼         ☼\n" +
				"☼         ☼\n" +
				"☼         ☼\n" +
				"☼˄        ☼\n" +
				"☼☼☼☼☼☼☼☼☼☼☼\n");

		hero(0).down();
		field().tick();

		assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
				"☼         ☼\n" +
				"☼         ☼\n" +
				"☼%        ☼\n" +
				"☼%        ☼\n" +
				"☼▼        ☼\n" +
				"☼         ☼\n" +
				"☼         ☼\n" +
				"☼         ☼\n" +
				"☼˄        ☼\n" +
				"☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    // но если в сеттингах сказано что меня видно под деревьями, я - вижу
    @Test
    public void shouldHeroMove_underTree_caseShowMyHeroUnderTree() {
        settings().bool(SHOW_MY_HERO_UNDER_TREE, true);

        givenFl("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼▼        ☼\n" +
                "☼         ☼\n" +
                "☼%        ☼\n" +
                "☼%        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼˄        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).down();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼▼        ☼\n" +
                "☼%        ☼\n" +
                "☼%        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼˄        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).down();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼▼        ☼\n" +
                "☼%        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼˄        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).down();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼%        ☼\n" +
                "☼▼        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼˄        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).down();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼%        ☼\n" +
                "☼%        ☼\n" +
                "☼▼        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼˄        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    // другого игрока не видно под деревом
    @Test
    public void shouldOtherHeroMove_underTree() {
        givenFl("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼▼        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼%        ☼\n" +
                "☼%        ☼\n" +
                "☼         ☼\n" +
                "☼˄        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(1).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼▼        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼%        ☼\n" +
                "☼%        ☼\n" +
                "☼˄        ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(1).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼▼        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼%        ☼\n" +
                "☼%        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(1).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼▼        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼%        ☼\n" +
                "☼%        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(1).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼▼        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼˄        ☼\n" +
                "☼%        ☼\n" +
                "☼%        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    // даже если в сеттингах сказано что меня видно под деревьями,
    // другого танка я не вижу все равно
    @Test
    public void shouldOtherHeroMove_underTree_caseShowMyHeroUnderTree() {
        settings().bool(SHOW_MY_HERO_UNDER_TREE, true);

        givenFl("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼▼        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼%        ☼\n" +
                "☼%        ☼\n" +
                "☼         ☼\n" +
                "☼˄        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(1).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼▼        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼%        ☼\n" +
                "☼%        ☼\n" +
                "☼˄        ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(1).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼▼        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼%        ☼\n" +
                "☼%        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(1).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼▼        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼%        ☼\n" +
                "☼%        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(1).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼▼        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼˄        ☼\n" +
                "☼%        ☼\n" +
                "☼%        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    // под деревом не видно так же и ботов белых
    @Test
    public void shouldAIMove_underTree() {
        givenFl("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼?        ☼\n" +
                "☼         ☼\n" +
                "☼%        ☼\n" +
                "☼%        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼◘        ☼\n" +
                "☼         ☼\n" +
                "☼%        ☼\n" +
                "☼%        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        ai(0).down();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼¿        ☼\n" +
                "☼%        ☼\n" +
                "☼%        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        ai(0).fire();
        ai(0).down();
        field().tick();

        ai(0).down();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼%        ☼\n" +
                "☼%        ☼\n" +
                "☼         ☼\n" +
                "☼•        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        ai(0).down();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼%        ☼\n" +
                "☼%        ☼\n" +
                "☼◘        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼•        ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldEnemyCanKillHeroUnderTree() {
        givenFl("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼▼        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼%        ☼\n" +
                "☼%        ☼\n" +
                "☼%        ☼\n" +
                "☼˄        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(1).up();
        field().tick();// герой запрятался в кустах

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼▼        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼%        ☼\n" +
                "☼%        ☼\n" +
                "☼%        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼▼        ☼\n" +
                "☼         ☼\n" +
                "☼•        ☼\n" +
                "☼%        ☼\n" +
                "☼%        ☼\n" +
                "☼%        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        field().tick();

        assertEquals(true, hero(1).isAlive());
        field().tick();// герой должен погибнуть

        verifyAllEvents(
                "listener(0) => [KILL_OTHER_HERO[1]]\n" +
                "listener(1) => [HERO_DIED]\n");

        assertEquals(false, hero(1).isAlive());
        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼▼        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼%        ☼\n" +
                "☼%        ☼\n" +
                "☼Ѡ        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    // два танка не могут проехать друг через друга под деревьями
    // но мой танк видно - так сказано в настройках
    @Test
    public void shouldTwoHeroCanPassThroughEachOtherUnderTree_caseShowMyHeroUnderTree() {
        settings().bool(SHOW_MY_HERO_UNDER_TREE, true);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼%    ☼\n" +
                "☼%    ☼\n" +
                "☼˄    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).down();
        hero(1).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼%    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).down();
        hero(1).up();
        field().tick();

        hero(0).down();
        field().tick();

        hero(1).up();
        // Два танка не могут проехать через друг друга
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼%    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).right();
        hero(1).right();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼%►   ☼\n" +
                "☼%˃   ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // два танка не могут проехать друг через друга под деревьями
    // но мой танк видно - так сказано в настройках
    @Test
    public void shouldTwoHeroCanPassThroughEachOtherUnderTree() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼%    ☼\n" +
                "☼%    ☼\n" +
                "☼˄    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).down();
        hero(1).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼%    ☼\n" +
                "☼%    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).down();
        hero(1).up();
        field().tick();

        hero(0).down();
        field().tick();

        hero(1).up();
        // Два танка не могут проехать через друг друга
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼%    ☼\n" +
                "☼%    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).right();
        hero(1).right();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼%►   ☼\n" +
                "☼%˃   ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

	// Лёд
    @Test
    public void shouldBeWallIce_whenGameCreated() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼  #  ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertEquals(1, field().ice().size());

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼  #  ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // когда герой двигается по льду, происходит скольжение
    // (он проскальзывает одну команду).
    // Если только заезжаем - то сразу же начинается занос,
    // то есть запоминается команда которой заезжали на лед
    // Если съезжаем на землю, то любой занос прекращается тут же
    @Test
    public void shouldHeroMoveUp_onIce_afterBeforeGround() {
        givenFl("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // заежаем на лёд
        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // находимся на льду
        // выполнили команаду right(), но танк не реагирует, так как происходит скольжение
        // двигается дальше с предедущей командой up()
        // RIGHT -> UP (скольжение)
        hero(0).right();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    #    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼    #    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // двигаемся дальше в направлении up()
        // UP -> UP (выполнилась)
        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    ▲    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // выполнили команаду right(), но танк не реагирует, так как происходит скольжение
        // двигается дальше с предедущей командой up()
        // RIGHT -> UP (скольжение)
        hero(0).right();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    ▲    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // выехали со льда
        // двигается дальше в направлении up()
        // UP -> UP (выполнилась)
        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    ▲    ☼\n" +
                "☼         ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldHeroMoveLeftThenUpThenDown_onIce() {
        settings().integer(SLIPPERINESS, 1);

        givenFl("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // заежаем на лёд
        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // LEFT -> UP (скольжение)
        hero(0).left();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    #    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼    #    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // DOWN -> DOWN (выполнилась)
        hero(0).down();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    ▼    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // DOWN -> DOWN (скольжение)
        hero(0).down();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    ▼    ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    // также когда на нем двигается враг он проскальзывает команду на два тика
    @Test
    public void shouldOtherHeroMoveLeftThenUpThenDown_onIce() {
        settings().integer(SLIPPERINESS, 1);

        givenFl("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    ▼    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼         ☼\n" +
                "☼˄        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // враг заежает на лёд
        hero(0).down();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    ▼    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼         ☼\n" +
                "☼˄        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // LEFT -> DOWN(скольжение)
        hero(0).left();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    #    ☼\n" +
                "☼    ▼    ☼\n" +
                "☼    #    ☼\n" +
                "☼         ☼\n" +
                "☼˄        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // UP -> UP (выполнилась)
        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    ▲    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼         ☼\n" +
                "☼˄        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // UP -> UP (скольжение)
        // сьезд со льда
        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    ▲    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼         ☼\n" +
                "☼˄        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    // Река
    @Test
    public void shouldBeWallWater_whenGameCreated() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼  ~  ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertEquals(1, field().rivers().size());

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼  ~  ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

	// река - через нее герою нельзя пройти. но можно стрелять
	@Test
	public void shouldHeroCanGoIfRiverAtWay() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼~    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).up();
        field().tick();

        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼~    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

	@Test
	public void shouldBulletCanGoIfRiverAtWay() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼~    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

		hero(0).up();
		field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼~    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

		hero(0).fire();
		field().tick();

		assertD("☼☼☼☼☼☼☼\n" +
				"☼     ☼\n" +
				"☼     ☼\n" +
				"☼•    ☼\n" +
				"☼~    ☼\n" +
				"☼▲    ☼\n" +
				"☼☼☼☼☼☼☼\n");

		hero(0).right();
		hero(0).fire();
		field().tick();

		assertD("☼☼☼☼☼☼☼\n" +
				"☼•    ☼\n" +
				"☼     ☼\n" +
				"☼     ☼\n" +
				"☼~    ☼\n" +
				"☼ ►•  ☼\n" +
				"☼☼☼☼☼☼☼\n");

		field().tick();

		assertD("☼☼☼☼☼☼☼\n" +
				"☼     ☼\n" +
				"☼     ☼\n" +
				"☼     ☼\n" +
				"☼~    ☼\n" +
				"☼ ►  •☼\n" +
				"☼☼☼☼☼☼☼\n");
	}

    @Test
    public void shouldDoNotMove_whenRiverToWay_goRightOrUpOrLeftOrDown() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ~  ☼\n" +
                "☼ ~▲~ ☼\n" +
                "☼  ~  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).right();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ~  ☼\n" +
                "☼ ~►~ ☼\n" +
                "☼  ~  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ~  ☼\n" +
                "☼ ~▲~ ☼\n" +
                "☼  ~  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).left();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ~  ☼\n" +
                "☼ ~◄~ ☼\n" +
                "☼  ~  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

		hero(0).down();
		field().tick();

		assertD("☼☼☼☼☼☼☼\n" +
				"☼     ☼\n" +
				"☼  ~  ☼\n" +
				"☼ ~▼~ ☼\n" +
				"☼  ~  ☼\n" +
				"☼     ☼\n" +
				"☼☼☼☼☼☼☼\n");
    }

    // река - через нее врагу нельзя пройти. но можно стрелять
    @Test
    public void shouldOtherHeroBullet_canGoIfRiverAtWay() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼~ ▲  ☼\n" +
                "☼˄    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(1).up();
        field().tick();

        hero(1).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼~ ▲  ☼\n" +
                "☼˄    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(1).right();
        hero(1).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼~ ▲  ☼\n" +
                "☼ ˃•  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼~ ▲  ☼\n" +
                "☼ ˃  •☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldOtherHeroDoNotMove_whenRiverToWay_goRightOrUpOrLeftOrDown() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ~  ☼\n" +
                "☼ ~▲~ ☼\n" +
                "☼  ~  ☼\n" +
                "☼    ˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).right();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ~  ☼\n" +
                "☼ ~►~ ☼\n" +
                "☼  ~  ☼\n" +
                "☼    ˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ~  ☼\n" +
                "☼ ~▲~ ☼\n" +
                "☼  ~  ☼\n" +
                "☼    ˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).left();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ~  ☼\n" +
                "☼ ~◄~ ☼\n" +
                "☼  ~  ☼\n" +
                "☼    ˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).down();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ~  ☼\n" +
                "☼ ~▼~ ☼\n" +
                "☼  ~  ☼\n" +
                "☼    ˄☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // река - через нее боту нельзя пройти. но можно стрелять
    @Test
    public void shouldAIBullet_canGoIfRiverAtWay() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼~ ▲  ☼\n" +
                "☼?    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼~ ▲  ☼\n" +
                "☼◘    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼~ ▲  ☼\n" +
                "☼?    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼~ ▲  ☼\n" +
                "☼?    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    private AI ai(int index) {
        return (AI) field().ais().get(index);
    }

    @Test
    public void shouldAIDoNotMove_whenRiverToWay_goRightOrUpOrLeftOrDown() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ~  ☼\n" +
                "☼ ~?~ ☼\n" +
                "☼  ~  ☼\n" +
                "☼    ▲☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ~  ☼\n" +
                "☼ ~◘~ ☼\n" +
                "☼  ~  ☼\n" +
                "☼    ▲☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ~  ☼\n" +
                "☼ ~?~ ☼\n" +
                "☼  ~  ☼\n" +
                "☼    ▲☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).left();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ~  ☼\n" +
                "☼ ~«~ ☼\n" +
                "☼  ~  ☼\n" +
                "☼    ▲☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).down();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ~  ☼\n" +
                "☼ ~¿~ ☼\n" +
                "☼  ~  ☼\n" +
                "☼    ▲☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // создаем АИтанк с призами
    @Test
    public void shouldCreatedAiPrize() {
        settings().integer(KILL_HITS_AI_PRIZE, 3);

        givenFl("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ?☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ◘☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        ai(0).down();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼      ¿☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        assertEquals("1 prizes with 2 heroes", getPrizesCount());
    }

    // У АИтанка с призами после 4-го хода должен смениться Element
    @Test
    public void shouldSwapElementAfterFourTicks() {
        settings().integer(KILL_HITS_AI_PRIZE, 3);

        givenFl("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ?☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ◘☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        ai(0).down();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼      ¿☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        ai(0).left();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼     « ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        ai(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼     ? ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        ai(0).right();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ◘☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        ai(0).down();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼      ¿☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        assertEquals("1 prizes with 2 heroes", getPrizesCount());
    }

    // если spawnAiPrize = 3, а спаунится сразу 2 АИтанка, то 2-й должен быть АИтанком с призами
    @Test
    public void shouldSpawnAiPrizeWhenTwoAi() {
        settings().integer(SPAWN_AI_PRIZE, 3);

        givenFl("☼☼☼☼☼☼☼☼☼\n" +
                "☼ ¿    ¿☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼ ◘    ¿☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        field().tick();

        assertEquals("1 prizes with 3 heroes", getPrizesCount());
    }

    // если spawnAiPrize = 3 и спаунится сразу 3 АИтанка, то 2-й должен быть АИтанком с призами
    @Test
    public void shouldSpawnAiPrizeWhenThreeAi() {
        settings().integer(SPAWN_AI_PRIZE, 3);

        givenFl("☼☼☼☼☼☼☼☼☼\n" +
                "☼ ¿  ¿ ¿☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼ ◘  ¿ ¿☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        field().tick();

        assertEquals("1 prizes with 4 heroes", getPrizesCount());
    }

    // если spawnAiPrize = 3, а спаунятся сразу 6 АИтанков, то должно быть 2 АИтанка с призами
    @Test
    public void shouldSpawnTwoAiPrizeWhenSixAi() {
        settings().integer(SPAWN_AI_PRIZE, 3);

        givenFl("☼☼☼☼☼☼☼☼☼\n" +
                "☼ ¿¿¿¿¿¿☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼ ◘¿¿◘¿¿☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        field().tick();

        assertEquals("2 prizes with 7 heroes", getPrizesCount());
    }

    // если spawnAiPrize = 3, а 3 АИтанка спаунятся по 1-му за каждый ход,
    // то АИтанк с призами спаунится после 2-го хода
    // так же проверяем что призовой танк меняет свой символ каждые 4 тика
    @Test
    public void shouldSpawnAiPrize_whenAddOneByOneAI() {
        settings().integer(SPAWN_AI_PRIZE, 3);

        givenFl("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        dropAI(pt(2, 7));

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼ ◘     ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼ ¿     ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        dropAI(pt(3, 7));

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼ ¿¿    ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼ ¿¿    ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        dropAI(pt(4, 7));

        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼ ¿¿¿   ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");
        assertEquals("1 prizes with 4 heroes", getPrizesCount());

        dropAI(pt(5, 7));

        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼ ◘¿¿¿  ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");
        assertEquals("2 prizes with 5 heroes", getPrizesCount());

        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼ ¿¿¿¿  ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼ ¿¿¿¿  ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼ ¿¿¿◘  ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");
    }

    // в AI-танк с призами надо попасть 3 раза, чтобы убить
    @Test
    public void shouldKillAiPrizeInThreeHits() {
        settings().integer(KILL_HITS_AI_PRIZE, 3);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼?    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertD("☼☼☼☼☼☼☼\n" +
                "☼◘    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼¿    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼¿    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼¿    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        verifyAllEvents("[KILL_AI]");

        assertD("☼☼☼☼☼☼☼\n" +
                "☼1    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldMyBulletsRemovesWhenKillMe() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼˃   ˃☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼˃   ˃☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(1).fire();
        field().tick();

        verifyAllEvents(
                "listener(0) => [KILL_OTHER_HERO[1]]\n" +
                "listener(1) => [HERO_DIED]\n");

        assertD("☼☼☼☼☼☼☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼Ѡ • ˃☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertEquals(false, hero(1).isAlive());
        field().tick();

        verifyAllEvents("");

        assertD("☼☼☼☼☼☼☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    ˃☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldDropPrize_onlyInPointKilledAiPrize() {
        settings().integer(KILL_HITS_AI_PRIZE, 1);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼¿    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertD("☼☼☼☼☼☼☼\n" +
                "☼◘    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼1    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldDropPrize_inPointKilledAiPrize_underTree() {
        settings().integer(KILL_HITS_AI_PRIZE, 1);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼¿    ☼\n" +
                "☼%    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertD("☼☼☼☼☼☼☼\n" +
                "☼◘    ☼\n" +
                "☼%    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).dontShoot = true;
        ai(0).down();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼%    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼%    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼1    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldDropPrize_InPointKilledAiPrize_onIce() {
        settings().integer(KILL_HITS_AI_PRIZE, 1);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼¿    ☼\n" +
                "☼#    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertD("☼☼☼☼☼☼☼\n" +
                "☼◘    ☼\n" +
                "☼#    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).dontShoot = true;
        ai(0).down();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼1    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // приз должен експайриться и исчезнуть через 2 тика, если его не подобрали
    @Test
    public void shouldExpirePrizeOnField_disappearTwoTicks() {
        settings().integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 2);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼¿    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼1    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼!    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // приз должен експайриться и исчезнуть через 3 тика, если его не подобрали
    @Test
    public void shouldExpirePrizeOnField_disappearThreeTicks() {
        settings().integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 3);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼¿    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼1    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼!    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼1    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // приз должен експайриться и исчезнуть через 4 тика, если его не подобрали
    @Test
    public void shouldExpirePrizeOnField_disappearFourTicks() {
        settings().integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 4);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼¿    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼1    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼!    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼1    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼!    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // приз над деревом должен исчезнуть через 2 тика, если его не подобрали
    // после исчезновения приза видим дерево
    @Test
    public void shouldExpirePrizeOnField_disappearOnTree() {
        settings().integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 2);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼¿    ☼\n" +
                "☼%    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertD("☼☼☼☼☼☼☼\n" +
                "☼◘    ☼\n" +
                "☼%    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).dontShoot = true;
        ai(0).down();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼%    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼%    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼1    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼!    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼%    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // приз на льду должен исчезнуть через 2 тика, если его не подобрали
    // после исчезновения приза видим лед
    @Test
    public void shouldExpirePrizeOnField_disappearOnIce() {
        settings().integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 2);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼¿    ☼\n" +
                "☼#    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertD("☼☼☼☼☼☼☼\n" +
                "☼◘    ☼\n" +
                "☼#    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).dontShoot = true;
        ai(0).down();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼1    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼!    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼#    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldHeroTookPrize() {
        settings().integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 3);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        dice(DICE_IMMORTALITY);
        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼1    ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents("");

        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[PRIZE_IMMORTALITY]");
        verifyAllEvents("[CATCH_PRIZE[1]]");

        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldOtherTookPrize() {
        settings().integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 3);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    ¿☼\n" +
                "☼     ☼\n" +
                "☼▲   ˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    Ѡ☼\n" +
                "☼     ☼\n" +
                "☼▲   ˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        dice(DICE_IMMORTALITY);
        hero(1).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    1☼\n" +
                "☼    ˄☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents("");

        hero(1).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    ˄☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[]");
        assertPrize(hero(1), "[PRIZE_IMMORTALITY]");
        verifyAllEvents(
                "listener(1) => [CATCH_PRIZE[1]]\n");

        hero(1).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼    ˄☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // герой берет приз под деревом - когда его видно под деревьями
    @Test
    public void shouldHeroTookPrize_underTree_caseShowMyHeroUnderTree() {
        settings().integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 3)
                .bool(SHOW_MY_HERO_UNDER_TREE, true);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼%%   ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).down();
        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼%%   ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));
        dice(DICE_IMMORTALITY);
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼1%   ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents("");

        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲%   ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[PRIZE_IMMORTALITY]");
        verifyAllEvents("[CATCH_PRIZE[1]]");

        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼%%   ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // герой берет приз под деревом - когда его не видно под деревьями
    @Test
    public void shouldHeroTookPrize_underTree() {
        settings().integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 3);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼%%   ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).down();
        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼%%   ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));
        dice(DICE_IMMORTALITY);
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼1%   ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents("");

        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼%%   ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[PRIZE_IMMORTALITY]");
        verifyAllEvents("[CATCH_PRIZE[1]]");

        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼%%   ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldOtherTookPrize_underTree() {
        settings().integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 3);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼    ¿☼\n" +
                "☼    %☼\n" +
                "☼     ☼\n" +
                "☼▲   ˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).down();
        hero(1).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    %☼\n" +
                "☼    ˄☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));
        dice(DICE_IMMORTALITY);
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    1☼\n" +
                "☼    ˄☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents("");

        hero(1).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    %☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[]");
        assertPrize(hero(1), "[PRIZE_IMMORTALITY]");
        verifyAllEvents(
                "listener(1) => [CATCH_PRIZE[1]]\n");

        hero(1).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼    ˄☼\n" +
                "☼    %☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldHeroTookPrize_onIce() {
        settings().integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 3);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼#    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).down();
        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");


        ai(0).kill(mock(Bullet.class));
        dice(DICE_IMMORTALITY);
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼1    ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents("");

        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents("[CATCH_PRIZE[1]]");
        assertPrize(hero(0), "[PRIZE_IMMORTALITY]");

        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼#    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldOtherTookPrize_onIce() {
        settings().integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 3);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼    ¿☼\n" +
                "☼    #☼\n" +
                "☼     ☼\n" +
                "☼▲   ˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).down();
        hero(1).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    ¿☼\n" +
                "☼    ˄☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));
        dice(DICE_IMMORTALITY);
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    1☼\n" +
                "☼    ˄☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents("");

        hero(1).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    ˄☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[]");
        assertPrize(hero(1), "[PRIZE_IMMORTALITY]");
        verifyAllEvents(
                "listener(1) => [CATCH_PRIZE[1]]\n");

        hero(1).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼    ˄☼\n" +
                "☼    #☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldAiDontTookPrize() {
        settings().integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 3);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼    ▲☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));
        ai(1).dontShoot = true;
        ai(1).up();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼?    ☼\n" +
                "☼    ▲☼\n" +
                "☼☼☼☼☼☼☼\n");

        dice(DICE_IMMORTALITY);
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼1    ☼\n" +
                "☼?    ☼\n" +
                "☼     ☼\n" +
                "☼    ▲☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼?    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    ▲☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼?    ☼\n" +
                "☼1    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    ▲☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // если я подстрелил танк, а в следующий тик в эту ячейку въезжаю сам,
    // то приз считается подобраным и не отбражается на филде
    @Test
    public void shouldHeroTookPrize_inPointKillAi() {
        settings().integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 3);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents("");

        dice(DICE_IMMORTALITY);
        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[PRIZE_IMMORTALITY]");
        verifyAllEvents("[CATCH_PRIZE[1]]");

        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

    }

    private void assertPrize(Hero hero, String expected) {
        assertEquals(expected, hero.prizes().toString());
    }

    // если в момент подбора приза прилетает снаряд, то умирает танк, а приз остается
    @Test
    public void shouldKillHero_whenHeroTookPrizeAndComesBullet() {
        settings().integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 6);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼¿   ˂☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼Ѡ   ˂☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        dice(DICE_IMMORTALITY);
        hero(0).up();
        hero(1).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼1 • ˂☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents("");

        hero(0).up();
        hero(1).left();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼Ѡ  ˂ ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[]");
        verifyAllEvents(
                "listener(0) => [HERO_DIED]\n" +
                "listener(1) => [KILL_OTHER_HERO[1]]\n");

        hero(1).left();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼1 ˂  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(1).left();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼!˂   ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents("");

        hero(1).left();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼˂    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(1), "[PRIZE_IMMORTALITY]");
        verifyAllEvents(
                "listener(1) => [CATCH_PRIZE[1]]\n");

        hero(1).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼˄    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(1), "[PRIZE_IMMORTALITY]");
    }

    @Test
    public void shouldHeroKillPrize() {
        settings().integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 3);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        dice(DICE_IMMORTALITY);
        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼1    ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[]");
    }

    // если я подстрелил приз, а в следующий тик в эту ячейку въезжаю сам,
    // то приз не подбирается
    @Test
    public void shouldHeroKillPrize_dontTakeNextTick() {
        settings().integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 3);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        dice(DICE_IMMORTALITY);
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼1    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[]");
        verifyAllEvents("");
    }

    @Test
    public void shouldKilAIWithPrize_whenHitKillsIs2() {
        settings().integer(KILL_HITS_AI_PRIZE, 2)
                .integer(PRIZE_ON_FIELD, 3);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼¿    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).dontShoot = true;

        assertD("☼☼☼☼☼☼☼\n" +
                "☼◘    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).down();
        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents("");

        ai(0).down();
        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents("");

        ai(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents(
                "[KILL_AI]");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼1    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents("");
    }

    @Test
    public void shouldHeroTakePrize_shouldShootWithDelayAfterPrizeEnd() {
        settings().integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 5)
                .integer(PRIZE_WORKING, 5)
                .integer(HERO_TICKS_PER_SHOOT,3);


        givenFl("☼☼☼☼☼☼☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼╬   ╬☼\n" +
                "☼ ¿   ☼\n" +
                "☼ ▲╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼╬   ╬☼\n" +
                "☼ Ѡ   ☼\n" +
                "☼ ▲╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        dice(DICE_BREAKING_WALLS);
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼╬   ╬☼\n" +
                "☼ 2   ☼\n" +
                "☼ ▲╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents("");

        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼╬   ╬☼\n" +
                "☼ ▲   ☼\n" +
                "☼  ╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[PRIZE_BREAKING_WALLS]");
        verifyAllEvents("[CATCH_PRIZE[2]]");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  ╬  ☼\n" +
                "☼ •   ☼\n" +
                "☼╬   ╬☼\n" +
                "☼ ▲   ☼\n" +
                "☼  ╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  ╬  ☼\n" +
                "☼ •   ☼\n" +
                "☼╬   ╬☼\n" +
                "☼ ▲   ☼\n" +
                "☼  ╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  ╬  ☼\n" +
                "☼ •   ☼\n" +
                "☼╬   ╬☼\n" +
                "☼ ▲   ☼\n" +
                "☼  ╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  ╬  ☼\n" +
                "☼ •   ☼\n" +
                "☼╬   ╬☼\n" +
                "☼ ▲   ☼\n" +
                "☼  ╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  ╬  ☼\n" +
                "☼ •   ☼\n" +
                "☼╬   ╬☼\n" +
                "☼ ▲   ☼\n" +
                "☼  ╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  ╬  ☼\n" +
                "☼ •   ☼\n" +
                "☼╬   ╬☼\n" +
                "☼ ▲   ☼\n" +
                "☼  ╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        //The impact of the prize ended. should stop shooting
        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼╬   ╬☼\n" +
                "☼ ▲   ☼\n" +
                "☼  ╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");


        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼╬   ╬☼\n" +
                "☼ ▲   ☼\n" +
                "☼  ╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        //should shoot now
        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  ╬  ☼\n" +
                "☼ •   ☼\n" +
                "☼╬   ╬☼\n" +
                "☼ ▲   ☼\n" +
                "☼  ╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        //silence
        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼╬   ╬☼\n" +
                "☼ ▲   ☼\n" +
                "☼  ╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼╬   ╬☼\n" +
                "☼ ▲   ☼\n" +
                "☼  ╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        //and shoot again
        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  ╬  ☼\n" +
                "☼ •   ☼\n" +
                "☼╬   ╬☼\n" +
                "☼ ▲   ☼\n" +
                "☼  ╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        //and silence
        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼╬   ╬☼\n" +
                "☼ ▲   ☼\n" +
                "☼  ╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");

    }


    @Test
    public void shouldHeroTakePrize_breakingWalls() {
        settings().integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 5)
                .integer(PRIZE_WORKING, 10);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼╬¿  ╬☼\n" +
                "☼     ☼\n" +
                "☼ ▲╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼╬Ѡ  ╬☼\n" +
                "☼     ☼\n" +
                "☼ ▲╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        dice(DICE_BREAKING_WALLS);
        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼╬2  ╬☼\n" +
                "☼ ▲   ☼\n" +
                "☼  ╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents("");

        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼╬▲  ╬☼\n" +
                "☼     ☼\n" +
                "☼  ╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[PRIZE_BREAKING_WALLS]");
        verifyAllEvents("[CATCH_PRIZE[2]]");

        hero(0).right();
        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼╬ ►•╬☼\n" +
                "☼     ☼\n" +
                "☼  ╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).up();
        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ▲  ☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼  ╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).down();
        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬ ▼  ☼\n" +
                "☼  •  ☼\n" +
                "☼  ╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).left();
        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ◄   ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldHeroTakePrizeEnemyWithoutPrize_breakingWalls() {
        settings().integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 5);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼╬╬╬  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼▲ ˄  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬╬╬  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼▲ ˄  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        dice(DICE_BREAKING_WALLS);
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬╬╬  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼2    ☼\n" +
                "☼▲ ˄  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).up();
        hero(1).up();
        field().tick();

        assertPrize(hero(0), "[PRIZE_BREAKING_WALLS]");
        verifyAllEvents(
                "listener(0) => [CATCH_PRIZE[2]]\n");

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬╬╬  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲ ˄  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        hero(1).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬╬╬  ☼\n" +
                "☼• •  ☼\n" +
                "☼     ☼\n" +
                "☼▲ ˄  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼ ╬╩  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲ ˄  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldHeroNotBreakingBorder_breakingWalls() {
        settings().integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 5);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼╬╬╬  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬╬╬  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents("");

        dice(DICE_BREAKING_WALLS);
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬╬╬  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼2    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).up();
        field().tick();

        assertPrize(hero(0), "[PRIZE_BREAKING_WALLS]");
        verifyAllEvents("[CATCH_PRIZE[2]]");

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬╬╬  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬╬╬  ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼ ╬╬  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼ ╬╬  ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼ ╬╬  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldEndPrizeWorking_breakingWalls() {
        settings().integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 5)
                .integer(PRIZE_WORKING, 1);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents("");

        dice(DICE_BREAKING_WALLS);
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼2    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).up();
        field().tick();

        assertPrize(hero(0), "[PRIZE_BREAKING_WALLS]");
        verifyAllEvents("[CATCH_PRIZE[2]]");

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╩    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldHeroTakePrizeEnemyShootsHero_immortality() {
        settings().integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 5);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼▲   ˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼▲   ˄☼\n" +
                "☼☼☼☼☼☼☼\n");


        dice(DICE_IMMORTALITY);
        hero(1).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼1   ˄☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents("");

        hero(0).up();
        hero(1).left();
        field().tick();

        assertPrize(hero(0), "[PRIZE_IMMORTALITY]");
        verifyAllEvents(
                "listener(0) => [CATCH_PRIZE[1]]\n");

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲  ˂ ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(1).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲• ˂ ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲  ˂ ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents("");
    }

    @Test
    public void shouldEndPrizeWorkingEnemyShootsHero_immortality() {
        settings().integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 5)
                .integer(PRIZE_WORKING, 3);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼▲   ˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼▲   ˄☼\n" +
                "☼☼☼☼☼☼☼\n");


        dice(DICE_IMMORTALITY);
        hero(1).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼1   ˄☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents("");

        hero(0).up();
        hero(1).left();
        field().tick();

        assertPrize(hero(0), "[PRIZE_IMMORTALITY]");
        verifyAllEvents(
                "listener(0) => [CATCH_PRIZE[1]]\n");

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲  ˂ ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(1).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲• ˂ ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        verifyAllEvents("");

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲  ˂ ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();
        field().tick();

        assertPrize(hero(0), "[]");

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲  ˂ ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(1).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲• ˂ ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼Ѡ  ˂ ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents(
                "listener(0) => [HERO_DIED]\n" +
                "listener(1) => [KILL_OTHER_HERO[1]]\n");
    }

    @Test
    public void shouldHeroTakePrizeAiShootsHero_immortality() {
        settings().integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 5);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼¿    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼?    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        dice(DICE_IMMORTALITY);
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼1    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼!    ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents("");

        hero(0).up();
        field().tick();

        assertPrize(hero(0), "[PRIZE_IMMORTALITY]");
        verifyAllEvents("[CATCH_PRIZE[1]]");

        assertD("☼☼☼☼☼☼☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).up();
        ai(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼?    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼?    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents("");
    }

    @Test
    public void shouldEndPrizeWorkingAiShootsHero_immortality() {
        settings().integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 5)
                .integer(PRIZE_WORKING, 2);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼¿    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));
        ai(1).dontShoot = true;

        assertD("☼☼☼☼☼☼☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        dice(DICE_IMMORTALITY);
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼1    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).up();
        ai(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼!    ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼?    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents("");

        hero(0).up();
        ai(0).down();
        field().tick();

        assertPrize(hero(0), "[PRIZE_IMMORTALITY]");
        verifyAllEvents("[CATCH_PRIZE[1]]");

        assertD("☼☼☼☼☼☼☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).up();
        ai(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼?    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).down();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents("");

        ai(0).up();
        ai(0).fire();
        field().tick();

        assertPrize(hero(0), "[]");

        assertD("☼☼☼☼☼☼☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼?    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼?    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents("[HERO_DIED]");
    }

    // если герой заехал на лед, а в следующий тик не указал никакой команды,
    // то продолжается движение вперед по старой команде на 1 тик.
    @Test
    public void shouldHeroSlidingOneTicks() {
        settings().integer(SLIPPERINESS, 3);

        givenFl("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    #    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼    #    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    // если герой заехал на лед, а в следующий тик указал какую-то команду,
    // то продолжается движение по старой команде N тиков.
    @Test
    public void shouldHeroSlidingNTicks() {
        settings().integer(SLIPPERINESS, 3);

        givenFl("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // RIGHT -> UP
        hero(0).right();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼    #    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // RIGHT -> UP
        hero(0).right();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    #    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // RIGHT -> UP
        hero(0).right();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    ▲    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // RIGHT -> RIGHT
        hero(0).right();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    #►   ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    // если герой заехал на лед, то продолжается движение по старой команде N тиков
    // слушается команда N + 1 и опять занос N тиков
    @Test
    public void shouldHeroSlidingNTicks_andAgainSliding() {
        settings().integer(SLIPPERINESS, 3);

        givenFl("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼#######  ☼\n" +
                "☼#        ☼\n" +
                "☼#        ☼\n" +
                "☼#        ☼\n" +
                "☼▲        ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼#######  ☼\n" +
                "☼#        ☼\n" +
                "☼#        ☼\n" +
                "☼▲        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // RIGHT -> UP
        hero(0).right();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼#######  ☼\n" +
                "☼#        ☼\n" +
                "☼▲        ☼\n" +
                "☼#        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // RIGHT -> UP
        hero(0).right();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼#######  ☼\n" +
                "☼▲        ☼\n" +
                "☼#        ☼\n" +
                "☼#        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // RIGHT -> UP
        hero(0).right();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼▲######  ☼\n" +
                "☼#        ☼\n" +
                "☼#        ☼\n" +
                "☼#        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // RIGHT -> RIGHT
        hero(0).right();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼#►#####  ☼\n" +
                "☼#        ☼\n" +
                "☼#        ☼\n" +
                "☼#        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // UP -> RIGHT
        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼##►####  ☼\n" +
                "☼#        ☼\n" +
                "☼#        ☼\n" +
                "☼#        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // UP -> RIGHT
        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼###►###  ☼\n" +
                "☼#        ☼\n" +
                "☼#        ☼\n" +
                "☼#        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // UP -> UP
        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼   ▲     ☼\n" +
                "☼#######  ☼\n" +
                "☼#        ☼\n" +
                "☼#        ☼\n" +
                "☼#        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    // если герой в ходе заноса уперся в стену, то занос прекращается
    @Test
    public void shouldHeroAndSliding_ifBumpedWall() {
        settings().integer(SLIPPERINESS, 5);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼###  ☼\n" +
                "☼#    ☼\n" +
                "☼#    ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼###  ☼\n" +
                "☼#    ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // RIGHT -> UP
        hero(0).right();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼###  ☼\n" +
                "☼▲    ☼\n" +
                "☼#    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // RIGHT -> UP
        hero(0).right();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼▲##  ☼\n" +
                "☼#    ☼\n" +
                "☼#    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // RIGHT -> UP -> wall -> Canceled sliding
        hero(0).right();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼▲##  ☼\n" +
                "☼#    ☼\n" +
                "☼#    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // RIGHT -> RIGHT
        hero(0).right();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼#►#  ☼\n" +
                "☼#    ☼\n" +
                "☼#    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldHeroCanGoIfRiverAtWay_whenHeroTakePrize() {
        settings().integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 5)
                .integer(PRIZE_WORKING, 3);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼~    ☼\n" +
                "☼  ╬  ☼\n" +
                "☼?    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼~    ☼\n" +
                "☼  ╬  ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        dice(DICE_WALKING_ON_WATER);
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼~    ☼\n" +
                "☼  ╬  ☼\n" +
                "☼3    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents("");

        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼~    ☼\n" +
                "☼  ╬  ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).up();
        field().tick();
        verifyAllEvents("[CATCH_PRIZE[3]]");

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼~    ☼\n" +
                "☼▲ ╬  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼▲    ☼\n" +
                "☼~    ☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldHeroTakePrize_walkOnWater() {
        settings().integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 5)
                .integer(PRIZE_WORKING, 3);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼~~~  ☼\n" +
                "☼¿    ☼\n" +
                "☼     ☼\n" +
                "☼▲ ˄  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼~~~  ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼▲ ˄  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        dice(DICE_WALKING_ON_WATER);
        hero(0).up();
        hero(1).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼~~~  ☼\n" +
                "☼3    ☼\n" +
                "☼▲ ˄  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents("");

        hero(0).up();
        hero(1).up();
        field().tick();

        assertPrize(hero(0), "[PRIZE_WALKING_ON_WATER]");
        verifyAllEvents(
                "listener(0) => [CATCH_PRIZE[3]]\n");

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼~~~  ☼\n" +
                "☼▲ ˄  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).up();
        hero(1).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼▲~~  ☼\n" +
                "☼  ˄  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).up();
        hero(1).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼▲    ☼\n" +
                "☼~~~  ☼\n" +
                "☼  ˄  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // когда заканчивается действие приза движение по воде отключается
    @Test
    public void shouldHeroCanGoIfRiverAtWay_whenPrizeIsOver() {
        settings().integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 5)
                .integer(PRIZE_WORKING, 2);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼~    ☼\n" +
                "☼     ☼\n" +
                "☼~ ╬  ☼\n" +
                "☼?    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼~    ☼\n" +
                "☼     ☼\n" +
                "☼~ ╬  ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        dice(DICE_WALKING_ON_WATER);
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼~    ☼\n" +
                "☼     ☼\n" +
                "☼~ ╬  ☼\n" +
                "☼3    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents("");

        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼~    ☼\n" +
                "☼     ☼\n" +
                "☼~ ╬  ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).up();
        field().tick();
        verifyAllEvents("[CATCH_PRIZE[3]]");

        assertD("☼☼☼☼☼☼☼\n" +
                "☼~    ☼\n" +
                "☼     ☼\n" +
                "☼▲ ╬  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼~    ☼\n" +
                "☼▲    ☼\n" +
                "☼~ ╬  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).up();
        field().tick();

        hero(0).up();
        field().tick();

        hero(0).up();
        field().tick();

        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼~    ☼\n" +
                "☼▲    ☼\n" +
                "☼~ ╬  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // если во время окончание приза танк оказался на воде, он получает штраф.
    // N тиков он не может ходить по клеточкам но может менять направление движение и стрелять,
    // на N+1 тик он может сместится на позицию указанной команды и продолжать движение.
    // За исключением - если после смещения он оказался снова на воде, то процедура повторяется до тех пор,
    // пока танк не выйдет полностью из воды.
    @Test
    public void shouldHeroCanGoIfRiverAtWay_whenPrizeIsOver_butHeroOnWater() {
        settings().integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 5)
                .integer(PENALTY_WALKING_ON_WATER, 4)
                .integer(PRIZE_WORKING, 2);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~~~☼\n" +
                "☼  ?  ☼\n" +
                "☼  ▲  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~~~☼\n" +
                "☼  Ѡ  ☼\n" +
                "☼  ▲  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        dice(DICE_WALKING_ON_WATER);
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~~~☼\n" +
                "☼  3  ☼\n" +
                "☼  ▲  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents("");

        assertPrize(hero(0), "[]");

        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~~~☼\n" +
                "☼  ▲  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[PRIZE_WALKING_ON_WATER]");
        verifyAllEvents("[CATCH_PRIZE[3]]");

        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~~~☼\n" +
                "☼~~▲~~☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[PRIZE_WALKING_ON_WATER]");

        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼~~~~~☼\n" +
                "☼~~▲~~☼\n" +
                "☼~~~~~☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[PRIZE_WALKING_ON_WATER]");

        // действие приза закончилось
        // герой получает штраф 4 тика
        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼~~~~~☼\n" +
                "☼~~▲~~☼\n" +
                "☼~~~~~☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[]");

        hero(0).down();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼~~~~~☼\n" +
                "☼~~▼~~☼\n" +
                "☼~~~~~☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[]");

        hero(0).left();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼~~~~~☼\n" +
                "☼~~◄~~☼\n" +
                "☼~~~~~☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[]");

        hero(0).right();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼~~~~~☼\n" +
                "☼~~►~~☼\n" +
                "☼~~~~~☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[]");

        // штраф 4 тика закончился. Возможно перемещение
        hero(0).right();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~►~☼\n" +
                "☼~~~~~☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[]");

        // штраф еще 4 тика, так как герой снова на воде
        hero(0).left();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~◄~☼\n" +
                "☼~~~~~☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[]");

        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~▲~☼\n" +
                "☼~~~~~☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[]");

        hero(0).down();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~▼~☼\n" +
                "☼~~~~~☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[]");

        hero(0).right();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~►~☼\n" +
                "☼~~~~~☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[]");

        // штраф 4 тика закончился. Возможно перемещение
        hero(0).right();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~~►☼\n" +
                "☼~~~~~☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[]");

        // штраф еще 4 тика, так как герой снова на воде
        hero(0).down();
        field().tick();
        field().tick();
        field().tick();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~~▼☼\n" +
                "☼~~~~~☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[]");

        hero(0).down();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~~▼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[]");

        // мы все так же на воде, а потому не можем двигаться 4 тика
        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~~▲☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[]");

        hero(0).left();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~~◄☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[]");

        // но можем выехать на сушу, хоть штраф не закончился
        hero(0).down();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~~~☼\n" +
                "☼    ▼☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[]");

        // обратно заехать уже не можем как ни старайся
        hero(0).up();
        field().tick();

        hero(0).up();
        field().tick();

        hero(0).up();
        field().tick();

        hero(0).up();
        field().tick();

        hero(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~~~☼\n" +
                "☼    ▲☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[]");
    }

    @Test
    public void shouldHeroTakePrizeAndShootsEveryTick_breakingWalls() {
        settings().integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 5)
                .integer(PRIZE_WORKING, 3)
                .integer(HERO_TICKS_PER_SHOOT, 3);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼╬╬╬  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼▲ ˄  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬╬╬  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼▲ ˄  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        dice(DICE_BREAKING_WALLS);
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬╬╬  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼2    ☼\n" +
                "☼▲ ˄  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents("");

        hero(0).up();
        hero(1).up();
        field().tick();

        assertPrize(hero(0), "[PRIZE_BREAKING_WALLS]");
        verifyAllEvents(
                "listener(0) => [CATCH_PRIZE[2]]\n");

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬╬╬  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲ ˄  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        hero(1).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬╬╬  ☼\n" +
                "☼• •  ☼\n" +
                "☼     ☼\n" +
                "☼▲ ˄  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        hero(1).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼ ╬╩  ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲ ˄  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        hero(1).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼ ╬╩  ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲ ˄  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        hero(1).fire();
        field().tick();

        assertPrize(hero(0), "[]");

        assertD("☼☼☼☼☼☼☼\n" +
                "☼ ╬╩  ☼\n" +
                "☼• •  ☼\n" +
                "☼     ☼\n" +
                "☼▲ ˄  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).fire();
        hero(1).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼ ╬╨  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲ ˄  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // когда бот упирается в водоем -> он останавливается на 5 тиков и отстреливается
    // после этого меняет направление и уезжает
    @Test
    public void shouldAiMoveAfterFiveTicks() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼~    ☼\n" +
                "☼~   ▲☼\n" +
                "☼?    ☼\n" +
                "☼☼☼☼☼☼☼\n");
        ai(0).up();
        ai(0).dontMove = false;
        ai(0).dontShoot = false;

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼~    ☼\n" +
                "☼~   ▲☼\n" +
                "☼◘    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼~   ▲☼\n" +
                "☼?    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼~    ☼\n" +
                "☼~   ▲☼\n" +
                "☼?    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼~    ☼\n" +
                "☼~   ▲☼\n" +
                "☼?    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼~    ☼\n" +
                "☼~   ▲☼\n" +
                "☼◘    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼~    ☼\n" +
                "☼~   ▲☼\n" +
                "☼?    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        dice(1);
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼~    ☼\n" +
                "☼~   ▲☼\n" +
                "☼ »   ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // если дропнуть танк на лед, то случался NPE
    // теперь все нормально
    @Test
    public void shouldDropAiOnIce() {
        settings().integer(SLIPPERINESS, 2);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼#    ☼\n" +
                "☼#    ☼\n" +
                "☼#    ☼\n" +
                "☼#    ☼\n" +
                "☼    ▲☼\n" +
                "☼☼☼☼☼☼☼\n");

        field().getAiGenerator().drop(pt(1, 5));
        ai(0).dontShoot = true;

        assertD("☼☼☼☼☼☼☼\n" +
                "☼◘    ☼\n" +
                "☼#    ☼\n" +
                "☼#    ☼\n" +
                "☼#    ☼\n" +
                "☼    ▲☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).down();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼#    ☼\n" +
                "☼¿    ☼\n" +
                "☼#    ☼\n" +
                "☼#    ☼\n" +
                "☼    ▲☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).right();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼#    ☼\n" +
                "☼#    ☼\n" +
                "☼¿    ☼\n" +
                "☼#    ☼\n" +
                "☼    ▲☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).right();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼#    ☼\n" +
                "☼#    ☼\n" +
                "☼#»   ☼\n" +
                "☼#    ☼\n" +
                "☼    ▲☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // мы не можем дропнуть танк на воду
    @Test
    public void shouldCantDropAiInRiver() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼~    ☼\n" +
                "☼~    ☼\n" +
                "☼~    ☼\n" +
                "☼~    ☼\n" +
                "☼    ▲☼\n" +
                "☼☼☼☼☼☼☼\n");

        dice(2);
        dropAI(pt(1, 5));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼~◘   ☼\n" +
                "☼~    ☼\n" +
                "☼~    ☼\n" +
                "☼~    ☼\n" +
                "☼    ▲☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).down();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼~    ☼\n" +
                "☼~¿   ☼\n" +
                "☼~    ☼\n" +
                "☼~    ☼\n" +
                "☼    ▲☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // если spawnAiPrize = 2, то должно быть 3 АИтанка с призами
    // если aiPrizeLimit = 2, то будет на поле 2 АИтанка с призами
    @Test
    public void shouldSpawnTwoAiPrize() {
        settings().integer(SPAWN_AI_PRIZE, 2)
                .integer(AI_PRIZE_LIMIT, 2);

        givenFl("☼☼☼☼☼☼☼☼☼\n" +
                "☼ ¿¿¿¿¿¿☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼ ◘¿◘¿¿¿☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        field().tick();

        assertEquals("2 prizes with 7 heroes", getPrizesCount());
    }

    // если spawnAiPrize = 2, то каждый второй АИтанк будет с призами
    // если на поле уже лежит приз и есть один АИтанк с призом, то при aiPrizeLimit = 2,
    // АИтанков с призами больше появляться не будет
    @Test
    public void shouldNotSpawnAiPrize_ifPrizeOnField() {
        settings().integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 5)
                .integer(SPAWN_AI_PRIZE, 2)
                .integer(AI_PRIZE_LIMIT, 2);

        givenFl("☼☼☼☼☼☼☼☼☼\n" +
                "☼¿¿¿¿¿¿ ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼◘¿◘¿¿¿ ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        assertEquals("2 prizes with 7 heroes", getPrizesCount());

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼¿¿¿¿¿¿ ☼\n" +
                "☼       ☼\n" +
                "☼•      ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼Ѡ¿¿¿¿¿ ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼1¿¿¿¿¿ ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");
        verifyAllEvents("[KILL_AI]");

        dropAI(pt(4, 6));
        dropAI(pt(5, 6));
        dropAI(pt(6, 6));

        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼!¿◘¿¿¿ ☼\n" +
                "☼   ¿¿¿ ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        assertEquals("1 prizes with 9 heroes", getPrizesCount());
    }

    @Test
    public void shouldSpawnAiPrize_ifKillPrize() {
        settings().integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 5)
                .integer(SPAWN_AI_PRIZE, 2)
                .integer(AI_PRIZE_LIMIT, 2);

        givenFl("☼☼☼☼☼☼☼☼☼\n" +
                "☼¿¿¿¿¿¿ ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼◘¿◘¿¿¿ ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        assertEquals("2 prizes with 7 heroes", getPrizesCount());

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼¿¿¿¿¿¿ ☼\n" +
                "☼       ☼\n" +
                "☼•      ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼Ѡ¿¿¿¿¿ ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        hero(0).fire();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼1¿¿¿¿¿ ☼\n" +
                "☼       ☼\n" +
                "☼•      ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");
        verifyAllEvents("[KILL_AI]");

        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼Ѡ¿◘¿¿¿ ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼ ¿¿¿¿¿ ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        dropAI(pt(4, 6));
        dropAI(pt(5, 6));
        dropAI(pt(6, 6));

        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼ ¿¿¿¿¿ ☼\n" +
                "☼   ¿¿¿ ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        assertEquals("2 prizes with 9 heroes", getPrizesCount());
    }

    @Test
    public void bulletWithTick0ShouldNotAffectHero(){
        givenFl("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼     ˃▲  ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).fire();
        hero(0).up();
        hero(1).right();
        field().tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼      •  ☼\n" +
                "☼      ▲  ☼\n" +
                "☼      ˃  ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    @Test
    public void bulletWithTick0ShouldNotAffectOtherBullet(){
        givenFl("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼      ˃  ☼\n" +
                "☼         ☼\n" +
                "☼      ▲  ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(1).fire();
        hero(1).right();
        hero(0).up();
        hero(0).fire();
        field().tick();


        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼      •˃•☼\n" +
                "☼      ▲  ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldHeroTakePrizeAndSeeAiUnderTree_visibility() {
        settings().integer(PRIZE_ON_FIELD, 5)
                .integer(KILL_HITS_AI_PRIZE, 1);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼  %% ☼\n" +
                "☼  %% ☼\n" +
                "☼¿ %%¿☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));
        ai(1).left();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  %% ☼\n" +
                "☼  %% ☼\n" +
                "☼Ѡ %%«☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        dice(DICE_VISIBILITY);
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  %% ☼\n" +
                "☼  %% ☼\n" +
                "☼4 %% ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).up();
        ai(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  %% ☼\n" +
                "☼  %% ☼\n" +
                "☼! %% ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents("");

        hero(0).up();
        ai(0).up();
        field().tick();

        assertPrize(hero(0), "[PRIZE_VISIBILITY]");
        verifyAllEvents("[CATCH_PRIZE[4]]");

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  %? ☼\n" +
                "☼  %% ☼\n" +
                "☼▲ %% ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldHeroTakePrizeAndSeeEnemyUnderTree_visibility() {
        settings().integer(PRIZE_ON_FIELD, 5)
                .integer(KILL_HITS_AI_PRIZE, 1);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼  %% ☼\n" +
                "☼  %% ☼\n" +
                "☼¿ %% ☼\n" +
                "☼     ☼\n" +
                "☼▲   ˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  %% ☼\n" +
                "☼  %% ☼\n" +
                "☼Ѡ %% ☼\n" +
                "☼     ☼\n" +
                "☼▲   ˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        dice(DICE_VISIBILITY);
        hero(1).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  %% ☼\n" +
                "☼  %% ☼\n" +
                "☼4 %% ☼\n" +
                "☼    ˄☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(1).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  %% ☼\n" +
                "☼  %% ☼\n" +
                "☼! %%˄☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).up();
        hero(1).left();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  %% ☼\n" +
                "☼  %% ☼\n" +
                "☼4 %% ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents("");

        hero(0).up();
        hero(1).up();
        field().tick();

        assertPrize(hero(0), "[PRIZE_VISIBILITY]");
        verifyAllEvents(
                "listener(0) => [CATCH_PRIZE[4]]\n");

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  %% ☼\n" +
                "☼  %˄ ☼\n" +
                "☼▲ %% ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldEndPrizeWorkingDontSeeAiUnderTree_visibility() {
        settings().integer(PRIZE_ON_FIELD, 5)
                .integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_WORKING, 2);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼  %% ☼\n" +
                "☼  %% ☼\n" +
                "☼¿ %%¿☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));
        ai(1).left();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  %% ☼\n" +
                "☼  %% ☼\n" +
                "☼Ѡ %%«☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        dice(DICE_VISIBILITY);
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  %% ☼\n" +
                "☼  %% ☼\n" +
                "☼4 %% ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).up();
        ai(0).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  %% ☼\n" +
                "☼  %% ☼\n" +
                "☼! %% ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents("");

        hero(0).up();
        ai(0).up();
        field().tick();

        assertPrize(hero(0), "[PRIZE_VISIBILITY]");
        verifyAllEvents("[CATCH_PRIZE[4]]");

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  %? ☼\n" +
                "☼  %% ☼\n" +
                "☼▲ %% ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).left();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  «% ☼\n" +
                "☼  %% ☼\n" +
                "☼▲ %% ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).down();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  %% ☼\n" +
                "☼  ¿% ☼\n" +
                "☼▲ %% ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).down();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  %% ☼\n" +
                "☼  %% ☼\n" +
                "☼▲ %% ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[]");
    }

    @Test
    public void shouldEndPrizeWorkingDontSeeEnemyUnderTree_visibility() {
        settings().integer(PRIZE_ON_FIELD, 5)
                .integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_WORKING, 2);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼  %% ☼\n" +
                "☼  %% ☼\n" +
                "☼¿ %% ☼\n" +
                "☼     ☼\n" +
                "☼▲   ˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  %% ☼\n" +
                "☼  %% ☼\n" +
                "☼Ѡ %% ☼\n" +
                "☼     ☼\n" +
                "☼▲   ˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        dice(DICE_VISIBILITY);
        hero(1).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  %% ☼\n" +
                "☼  %% ☼\n" +
                "☼4 %% ☼\n" +
                "☼    ˄☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(1).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  %% ☼\n" +
                "☼  %% ☼\n" +
                "☼! %%˄☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).up();
        hero(1).left();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  %% ☼\n" +
                "☼  %% ☼\n" +
                "☼4 %% ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents("");

        hero(0).up();
        hero(1).up();
        field().tick();

        assertPrize(hero(0), "[PRIZE_VISIBILITY]");
        verifyAllEvents(
                "listener(0) => [CATCH_PRIZE[4]]\n");

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  %% ☼\n" +
                "☼  %˄ ☼\n" +
                "☼▲ %% ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(1).up();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  %˄ ☼\n" +
                "☼  %% ☼\n" +
                "☼▲ %% ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(1).left();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  ˂% ☼\n" +
                "☼  %% ☼\n" +
                "☼▲ %% ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(1).down();
        field().tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  %% ☼\n" +
                "☼  %% ☼\n" +
                "☼▲ %% ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }
}
