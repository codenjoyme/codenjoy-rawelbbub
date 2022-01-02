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


import com.codenjoy.dojo.rawelbbub.model.items.Bullet;
import org.junit.Test;

import static com.codenjoy.dojo.rawelbbub.TestGameSettings.*;
import static com.codenjoy.dojo.rawelbbub.services.GameSettings.Keys.*;
import static com.codenjoy.dojo.services.PointImpl.pt;
import static org.mockito.Mockito.mock;

public class GameTest extends AbstractGameTest {
    
    @Test
    public void shouldDrawField() {
        // given when
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
 
        // then
        assertF("☼☼☼☼☼☼☼\n" +
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
        // given when
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
 
        // then
        assertEquals(1, field().walls().size());

        // then
        assertF("☼☼☼☼☼☼☼\n" +
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
        // given when
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
 
        // then
        assertEquals(1, field().heroesAndAis().size());
    }

    @Test
    public void shouldResetSlidingTicks_whenLeaveOilLeak() {
        // given
        settings().integer(OIL_SLIPPERINESS, 2);

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

        // when
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        // LEFT -> UP [sliding]
        hero(0).left();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        // LEFT -> UP [sliding]
        hero(0).left();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        // LEFT -> LEFT
        hero(0).left();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        // UP -> LEFT [sliding]
        hero(0).up();
        tick();

        // then
        // sliding state should be reset because the hero left oil leak
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        // RIGHT -> RIGHT
        hero(0).right();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        // DOWN -> RIGHT [sliding]
        hero(0).down();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        // DOWN -> RIGHT [sliding]
        hero(0).down();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        // DOWN -> DOWN [sliding]
        hero(0).down();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        // RIGHT -> DOWN [sliding]
        hero(0).right();
        tick();
        
        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        // RIGHT -> RIGHT
        hero(0).right();
        tick();
 
        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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
        // given
        settings().integer(OIL_SLIPPERINESS, 2);

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

        // when
        hero(0).up();
        hero(0).fire();
        tick();
 
        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        // RIGHT -> UP [sliding]
        hero(0).right();
        hero(0).fire();
        tick();
 
        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        // RIGHT -> UP [sliding]
        hero(0).right();
        hero(0).fire();
        tick();
 
        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        // LEFT -> LEFT
        hero(0).left();
        hero(0).fire();
        tick();
 
        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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
        // given
        settings().integer(OIL_SLIPPERINESS, 2);

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

        // when
        hero(0).left();
        hero(0).fire();
        tick();
 
        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        hero(0).left();
        tick();
 
        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        hero(0).left();
        hero(0).fire();
        tick();
 
        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        hero(0).left();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        // UP -> LEFT [sliding]
        hero(0).up();
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        // UP -> LEFT [sliding]
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        // UP -> UP
        hero(0).up();
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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
    public void shouldHeroCanGoIfOilLeakAtWayWithoutSliding_whenHeroTakePrize() {
        // given
        settings().integer(PRIZE_ON_FIELD, 5)
                .integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_WORKING, 6)
                .integer(OIL_SLIPPERINESS, 1);

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

        // when
        ai(0).kill(mock(Bullet.class));

        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        dice(DICE_NO_SLIDING);
        tick();
        verifyAllEvents("");

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        hero(0).up();
        tick();

        // then
        assertPrize(hero(0), "[PRIZE_NO_SLIDING(0/6)]");

        verifyAllEvents("[CATCH_PRIZE[5]]");

        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // заезжаем на нефтяное пятно
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        // DOWN -> UP но так как игрок взял приз скольжение не происходит, по этому DOWN -> DOWN
        hero(0).down();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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
        // given
        settings().integer(PRIZE_ON_FIELD, 5)
                .integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_WORKING, 2)
                .integer(OIL_SLIPPERINESS, 3);

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

        // when
        ai(0).kill(mock(Bullet.class));

        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        dice(DICE_NO_SLIDING);
        tick();
        verifyAllEvents("");

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        hero(0).up();
        tick();

        // then
        assertPrize(hero(0), "[PRIZE_NO_SLIDING(0/2)]");

        verifyAllEvents("[CATCH_PRIZE[5]]");

        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // заезжаем на нефтяное пятно
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        // так как игрок взял приз скольжение не происходит, по этому UP -> UP
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        // так как игрок взял приз скольжение не происходит, по этому UP -> UP
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        // мы снова на льду, начинаем занос запоминаем команду
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        // RIGHT -> UP занос
        hero(0).right();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        // RIGHT -> RIGHT
        hero(0).right();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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
        // given
        settings().integer(PRIZE_ON_FIELD, 4)
                .integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_WORKING, 6)
                .integer(OIL_SLIPPERINESS, 5);

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

        // when
        ai(0).down();
        hero(0).up();
        ai(0).dontShoot = true;
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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
        
        // when
        ai(0).down();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        ai(0).kill(mock(Bullet.class));

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        dice(DICE_NO_SLIDING);
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        // RIGHT -> UP выполняется занос
        hero(0).right();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        // RIGHT -> UP выполняется занос
        hero(0).right();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        assertPrize(hero(0), "[PRIZE_NO_SLIDING(0/6)]");

        verifyAllEvents("[CATCH_PRIZE[5]]");

        // when
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        hero(0).down();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        hero(0).down();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        hero(0).right();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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
        // given
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).down();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).right();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ►   ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).left();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼◄    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldHeroStayAtPreviousPosition_whenTryingToCrossReefs() {
        // given
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼    ▼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼˄    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).up();
        hero(0).up();
        
        hero(1).down();
        hero(1).down();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼    ▲☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼˅    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).right();
        hero(0).right();
        
        hero(1).left();
        hero(1).left();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼    ►☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼˂    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldBulletHasSameDirectionAsHero() {
        // given
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertEquals(hero(0).getBullets().iterator().next().getDirection(),
                hero(0).getDirection());
    }

    @Test
    public void shouldBulletGoInertiaWhenHeroChangeDirection() {
        // given
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼  ▲  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼  •  ☼\n" +
                "☼     ☼\n" +
                "☼  ▲  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).right();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼  •  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼   ► ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldBulletDisappear_whenHittingTheWallUp() {
        // given
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();
 
        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldBulletDisappear_whenHittingTheWallRight() {
        // given
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼►    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼►   •☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼►    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldBulletDisappear_whenHittingTheWallLeft() {
        // given
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼    ◄☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼•   ◄☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼    ◄☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldBulletDisappear_whenHittingTheWallDown() {
        // given
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼    ▼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼    ▼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    •☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
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
        // given
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼╩    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼╩    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼╨    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼╨    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
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
        // given
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼►   ╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼► • ╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼►   ╠☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼► • ╠☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼►   ╞☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼► • ╞☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
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
        // given
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬   ◄☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬ • ◄☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╣   ◄☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╣ • ◄☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╡   ◄☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╡ • ◄☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
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
        // given
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼╬    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╦    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼╦    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╥    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼╥    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
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
        // given
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼╩    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼╩    ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼╨    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼╨    ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
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
        // given
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ►  ╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ► •╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ►  ╠☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ► •╠☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ►  ╞☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ► •╞☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
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
        // given
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬  ◄ ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬• ◄ ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╣  ◄ ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╣• ◄ ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╡  ◄ ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╡• ◄ ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
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
        // given
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼╬    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╦    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼╦    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╥    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼╥    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldBulletDestroyWall_whenHittingTheWallUp_whenTwoWalls() {
        // given
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╬    ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╩    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╩    ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╨    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╨    ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼╩    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldBulletDestroyWall_whenHittingTheWallRight_whenTwoWalls() {
        // given
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼►  ╬╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼►  ╬╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼► •╬╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼►  ╠╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼► •╠╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼►  ╞╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼► •╞╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼►   ╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼► • ╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼►   ╠☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldBulletDestroyWall_whenHittingTheWallLeft_whenTwoWalls() {
        // given
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬╬  ◄☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬╬  ◄☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬╬• ◄☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬╣  ◄☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬╣• ◄☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬╡  ◄☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬╡• ◄☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬   ◄☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬ • ◄☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╣   ◄☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldBulletDestroyWall_whenHittingTheWallDown_whenTwoWalls() {
        // given
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼    ▼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    ╬☼\n" +
                "☼    ╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼    ▼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    ╬☼\n" +
                "☼    ╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼    ▼☼\n" +
                "☼     ☼\n" +
                "☼    •☼\n" +
                "☼    ╬☼\n" +
                "☼    ╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼    ▼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    ╦☼\n" +
                "☼    ╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼    ▼☼\n" +
                "☼     ☼\n" +
                "☼    •☼\n" +
                "☼    ╦☼\n" +
                "☼    ╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼    ▼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    ╥☼\n" +
                "☼    ╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼    ▼☼\n" +
                "☼     ☼\n" +
                "☼    •☼\n" +
                "☼    ╥☼\n" +
                "☼    ╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼    ▼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    ╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼    ▼☼\n" +
                "☼     ☼\n" +
                "☼    •☼\n" +
                "☼     ☼\n" +
                "☼    ╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼    ▼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    ╦☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // если я иду, а спереди стена, то я не могу двигаться дальше
    @Test
    public void shouldDoNotMove_whenWallTaWay_goDownOrLeft() {
        // given
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ╬  ☼\n" +
                "☼ ╬►╬ ☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).right();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ╬  ☼\n" +
                "☼ ╬►╬ ☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ╬  ☼\n" +
                "☼ ╬▲╬ ☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).left();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ╬  ☼\n" +
                "☼ ╬◄╬ ☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).down();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ╬  ☼\n" +
                "☼ ╬▼╬ ☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        removeAllNear();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ╩  ☼\n" +
                "☼ ╣▼╠ ☼\n" +
                "☼  ╦  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).right();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ╩  ☼\n" +
                "☼ ╣►╠ ☼\n" +
                "☼  ╦  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ╩  ☼\n" +
                "☼ ╣▲╠ ☼\n" +
                "☼  ╦  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).left();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ╩  ☼\n" +
                "☼ ╣◄╠ ☼\n" +
                "☼  ╦  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).down();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ╩  ☼\n" +
                "☼ ╣▼╠ ☼\n" +
                "☼  ╦  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        removeAllNear();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ╨  ☼\n" +
                "☼ ╡▼╞ ☼\n" +
                "☼  ╥  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    private void removeAllNear() {
        // when
        hero(0).up();
        tick();
        hero(0).fire();
        tick();

        hero(0).left();
        tick();
        hero(0).fire();
        tick();

        hero(0).right();
        tick();
        hero(0).fire();
        tick();
        
        hero(0).down();
        tick();
        hero(0).fire();
        tick();
    }

    // если я стреляю дважды, то выпускается два снаряда
    // при этом я проверяю, что они уничтожаются в порядке очереди
    @Test
    public void shouldShotWithSeveralBullets_whenHittingTheWallDown() {
        // given
        givenFl("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ▼☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼      ╬☼\n" +
                "☼      ╬☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ▼☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼      ╬☼\n" +
                "☼      ╬☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ▼☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼      ╬☼\n" +
                "☼      ╬☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ▼☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼      ╦☼\n" +
                "☼      ╬☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ▼☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼      ╥☼\n" +
                "☼      ╬☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ▼☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼      ╬☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ▼☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼      ╬☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ▼☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼      ╦☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ▼☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼      ╥☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ▼☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ▼☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ▼☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ▼☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ▼☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼\n" +
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
        // given
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ▼  ☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ▼  ☼\n" +
                "☼  ╦  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).right();
        tick();

        hero(0).down();
        tick();

        hero(0).down();
        tick();

        hero(0).left();
        tick();

        hero(0).up();
        tick();

        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼  ─  ☼\n" +
                "☼  ▲  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
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
        // given
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼˄    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).down();
        hero(1).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
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
        // given
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

        // when
        hero(0).fire();
        tick();

        // then
        verifyAllEvents(
                "listener(0) => [KILL_OTHER_HERO[1]]\n" +
                "listener(1) => [HERO_DIED]\n");

        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼˄    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertEquals(true, hero(0).isAlive());
        assertEquals(false, hero(1).isAlive());
        assertEquals(true, hero(2).isAlive());


        // when
        newGameFor(player(1), 3, 3);

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▼ ˄  ☼\n" +
                "☼     ☼\n" +
                "☼˄    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        verifyAllEvents(
                "listener(0) => [KILL_OTHER_HERO[2]]\n" +
                "listener(2) => [HERO_DIED]\n");

        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▼ ˄  ☼\n" +
                "☼     ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertEquals(true, hero(0).isAlive());
        assertEquals(true, hero(1).isAlive());
        assertEquals(false, hero(2).isAlive());

        // when
        newGameFor(player(2), 4, 4);

        // then
        assertEquals(true, hero(0).isAlive());
        assertEquals(true, hero(1).isAlive());
        assertEquals(true, hero(2).isAlive());

        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼   ˄ ☼\n" +
                "☼▼ ˄  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
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

        // when
        hero(0).fire();
        hero(0).right();
        hero(1).right();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        hero(0).right();
        hero(1).fire();
        hero(1).left();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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
    public void shouldDieOnce_whenIsDamagedByManyBullets() {
        // given
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

        // when
        hero(1).fire();
        hero(2).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        verifyAllEvents(
                "listener(0) => [HERO_DIED]\n" +
                        "listener(1) => [KILL_OTHER_HERO[1]]\n" +
                        "listener(2) => [KILL_OTHER_HERO[1]]\n");

        // when
        newGameFor(player(0), 1, 1);

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼      ˂  ☼\n" +
                "☼         ☼\n" +
                "☼    ˄    ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    // стоять, если меня убили
    @Test
    public void shouldStopWhenKill() {
        // given
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼˄    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        hero(1).left();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
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
        // given
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼˄    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(1).fire();
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents(
                "listener(0) => [KILL_OTHER_HERO[1], HERO_DIED]\n" +
                        "listener(1) => [HERO_DIED, KILL_OTHER_HERO[1]]\n");

        // when
        newGameFor(player(0), 2, 2);
        newGameFor(player(1), 3, 3);

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼  ˄  ☼\n" +
                "☼ ▲   ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldDestroyBullet() {
        // given
        givenFl("☼☼☼☼☼☼☼☼☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼˄      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        hero(1).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼•      ☼\n" +
                "☼       ☼\n" +
                "☼•      ☼\n" +
                "☼       ☼\n" +
                "☼˄      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼Ѡ      ☼\n" +
                "☼       ☼\n" +
                "☼˄      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼\n" +
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
        // given
        givenFl("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼˄      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        hero(1).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼•      ☼\n" +
                "☼•      ☼\n" +
                "☼       ☼\n" +
                "☼˄      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼Ѡ      ☼\n" +
                "☼       ☼\n" +
                "☼˄      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼\n" +
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
    public void shouldRemoveOtherHero_whenKillIt() {
        // given
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼˄    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲   ˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼˄    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲   ˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼˄    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲   ˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents("");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲   ˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents(
                "listener(0) => [KILL_OTHER_HERO[1]]\n" +
                        "listener(1) => [HERO_DIED]\n");

        // when
        newGameFor(player(1), 3, 3);

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼  ˄  ☼\n" +
                "☼     ☼\n" +
                "☼▲   ˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).right();
        tick();

        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼  ˄  ☼\n" +
                "☼     ☼\n" +
                "☼ ► •˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents("");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼  ˄  ☼\n" +
                "☼     ☼\n" +
                "☼ ►  Ѡ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents(
                "listener(0) => [KILL_OTHER_HERO[2]]\n" +
                "listener(2) => [HERO_DIED]\n");

        // when
        newGameFor(player(2), 4, 4);

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼   ˄ ☼\n" +
                "☼  ˄  ☼\n" +
                "☼     ☼\n" +
                "☼ ►   ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼   ˄ ☼\n" +
                "☼  ˄  ☼\n" +
                "☼     ☼\n" +
                "☼ ►   ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents("");
    }

    private void newGameFor(Player player, int x, int y) {
        // then
        assertEquals(true, player.isDestroyed());

        // when
        // новые координаты для героя
        dice(x, y);
        field().newGame(player); // это сделает сервер в ответ на isAlive = false
    }

    @Test
    public void shouldRegenerateDestroyedWall() {
        // given
        shouldBulletDestroyWall_whenHittingTheWallUp_whenTwoWalls();

        // when
        hero(0).fire();
        tick();
        
        hero(0).fire();
        tick();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        for (int tick = 7; tick <= settings().integer(WALL_REGENERATE_TIME); tick++) {
            tick();
        }

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();
        tick();
        tick();
        tick();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldHeroCantGoIfWallAtWay() {
        // given
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldBulletCantGoIfWallAtWay() {
        // given
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).right();
        hero(0).fire();
        tick();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼    ☼\n" +
                "☼ ►  •☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼    ☼\n" +
                "☼ ►   ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldOnlyOneBulletPerTick() {
        // given
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬    ☼\n" +
                "☼╬    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        hero(0).fire();
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬    ☼\n" +
                "☼╩    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        hero(0).fire();
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬    ☼\n" +
                "☼╨    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬    ☼\n" +
                "☼╨    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldHeroCanFireIfAtWayEnemyBullet() {
        // given
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼˄    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        hero(1).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼˄    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldHeroCanGoIfDestroyWall() {
        // given
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬    ☼\n" +
                "☼╬    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        hero(0).fire();
        tick();

        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬    ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldWallCantRegenerateOnHero() {
        // given
        shouldHeroCanGoIfDestroyWall();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬    ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        for (int tick = 3; tick <= settings().integer(WALL_REGENERATE_TIME); tick++) {
            tick();
        }

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬    ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).right();
        tick();

        for (int tick = 2; tick <= settings().integer(WALL_REGENERATE_TIME); tick++) {
            // then
            assertF("☼☼☼☼☼☼☼\n" +
                    "☼     ☼\n" +
                    "☼     ☼\n" +
                    "☼╬    ☼\n" +
                    "☼ ►   ☼\n" +
                    "☼     ☼\n" +
                    "☼☼☼☼☼☼☼\n");

            // when
            tick();
        }

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬    ☼\n" +
                "☼╬►   ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

    }

    @Test
    public void shouldWallCantRegenerateOnBullet() {
        // given
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        hero(0).fire();
        tick();

        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        for (int tick = 3; tick <= settings().integer(WALL_REGENERATE_TIME); tick++) {
            tick();
        }

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();
 
        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();
 
        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldNTicksPerBullet() {
        // given
        settings().integer(HERO_TICKS_PER_SHOOT, 4);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        String field =
                "☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╩    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n";
        assertF(field);

        for (int tick = 1; tick < settings().integer(HERO_TICKS_PER_SHOOT); tick++) {
            // when
            hero(0).fire();
            tick();

            // then
            assertF(field);
        }

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╨    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldNewAIWhenKillOther() {
        // given
        settings().integer(KILL_HITS_AI_PRIZE, 1);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼¿¿   ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼¿¿   ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼Ѡ¿   ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
        
        verifyAllEvents("[KILL_AI]");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼1¿   ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        dice(5, 5);
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼!¿   ☼\n" +
                "☼    ¿☼\n" +
                "☼    •☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldOnlyRotateIfNoBarrier() {
        // given
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲ ╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).right();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ►╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldOnlyRotateIfBarrier() {
        // given
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ▲╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).right();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ►╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldEnemyCanKillHeroOnWall() {
        // given
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼╬    ☼\n" +
                "☼˄    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(1).fire();
        tick();
        
        hero(1).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼╨    ☼\n" +
                "☼˄    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(1).fire();
        hero(1).up();  // команда поигнорится потому что вначале ходят все танки, а потом летят все снаряды
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼˄    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(1).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼˄    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        verifyAllEvents(
                "listener(0) => [KILL_OTHER_HERO[1]]\n" +
                "listener(1) => [HERO_DIED]\n");

        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldDieWhenMoveOnBullet() {
        // given
        givenFl("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼˄      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼•      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼˄      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        // when
        hero(1).up();
        tick();

        // then
        verifyAllEvents(
                "listener(0) => [KILL_OTHER_HERO[1]]\n" +
                "listener(1) => [HERO_DIED]\n");

        assertF("☼☼☼☼☼☼☼☼☼\n" +
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
        // given
        givenFl("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼˄      ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼•      ☼\n" +
                "☼       ☼\n" +
                "☼˄      ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        // when
        hero(1).up();
        tick();

        // then
        verifyAllEvents(
                "listener(0) => [KILL_OTHER_HERO[1]]\n" +
                "listener(1) => [HERO_DIED]\n");

        assertF("☼☼☼☼☼☼☼☼☼\n" +
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
        // given
        givenFl("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼˄      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼•      ☼\n" +
                "☼˄      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        // when
        hero(1).up();
        tick();

        // then
        verifyAllEvents(
                "listener(0) => [KILL_OTHER_HERO[1]]\n" +
                "listener(1) => [HERO_DIED]\n");

        assertF("☼☼☼☼☼☼☼☼☼\n" +
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
        // given
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

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        hero(0).fire(); // не выйдет
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        tick();
        tick();
        
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        tick();
        tick();
        
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        // допустим за игру он прибил 5 танков
        Player player = player(0);
        player.setKilled(5);

        dice(1, 1);
        field().clearScore();

        // смогу стрельнуть, пушка ресетнется
        hero(0).fire();
        tick();
 
        // then
        // но после рисета это поле чистится
        assertEquals(0, player.score());

        // и стенки тоже ресетнулись
        // и снаряд полетел
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        tick();
        tick();
        
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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
        // given
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

        // when
        dice(1, 1);
        field().clearScore();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        tick(); // внутри там тикает так же gun, но первого выстрела еще не было
        tick();

        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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
        // given when
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼  %  ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertEquals(1, field().trees().size());

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼  %  ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldBeWallTwoTree_whenGameCreated() {
        // given when
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼  %  ☼\n" +
                "☼     ☼\n" +
                "☼▲   %☼\n" +
                "☼☼☼☼☼☼☼\n");

        // then
        assertEquals(2, field().trees().size());

        assertF("☼☼☼☼☼☼☼\n" +
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
        // given
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

        // when
        hero(0).right();
        hero(0).fire();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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
        // given
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼%    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼%    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╬    ☼\n" +
                "☼•    ☼\n" +
                "☼%    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╩    ☼\n" +
                "☼     ☼\n" +
                "☼%    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╩    ☼\n" +
                "☼•    ☼\n" +
                "☼%    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╨    ☼\n" +
                "☼     ☼\n" +
                "☼%    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╨    ☼\n" +
                "☼•    ☼\n" +
                "☼%    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼%    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼%    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼╩    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼%    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
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
        // given
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

        // when
        hero(0).fire();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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
        // given
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼%    ☼\n" +
                "☼%    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼%    ☼\n" +
                "☼%    ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼%    ☼\n" +
                "☼%    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼%    ☼\n" +
                "☼%    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).right();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼%►   ☼\n" +
                "☼%    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).right();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
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
        // given
        settings().bool(SHOW_MY_HERO_UNDER_TREE, true);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼%    ☼\n" +
                "☼%    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼%    ☼\n" +
                "☼%    ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼%    ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼%    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).right();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼%►   ☼\n" +
                "☼%    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).right();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼% ►  ☼\n" +
                "☼%    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldBulletFlyUnderTree_jointly_shouldHeroMoveUnderTree() {
        // given
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

        // when
        hero(0).fire();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        hero(0).left();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        hero(0).left();
        tick();

        hero(0).left();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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
        // given
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

        // when
        hero(0).fire();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        hero(0).left();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        hero(0).left();
        tick();

        hero(0).left();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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
		// when
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

        // when
        hero(0).down();
        tick();

		// then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

		// when
        hero(0).down();
		tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

		// when
        hero(0).down();
		tick();

		// then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

		// when
        hero(0).down();
		tick();

		// then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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
        // given
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

        // when
        hero(0).down();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        hero(0).down();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        hero(0).down();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        hero(0).down();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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
        // given
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

        // when
        hero(1).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        hero(1).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        hero(1).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        hero(1).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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
        // given
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

        // when
        hero(1).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        hero(1).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        hero(1).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        hero(1).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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
        // given
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

        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        ai(0).down();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        ai(0).fire();
        ai(0).down();
        tick();

        ai(0).down();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        ai(0).down();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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
        // given
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

        // when
        hero(1).up();
        tick();// герой запрятался в кустах

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        tick();

        // then
        assertEquals(true, hero(1).isAlive());
        
        // when
        tick();// герой должен погибнуть

        // then
        verifyAllEvents(
                "listener(0) => [KILL_OTHER_HERO[1]]\n" +
                "listener(1) => [HERO_DIED]\n");

        assertEquals(false, hero(1).isAlive());
 
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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
        // given
        settings().bool(SHOW_MY_HERO_UNDER_TREE, true);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼%    ☼\n" +
                "☼%    ☼\n" +
                "☼˄    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).down();
        hero(1).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼%    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).down();
        hero(1).up();
        tick();

        hero(0).down();
        tick();

        hero(1).up();
        
        // then
        // Два танка не могут проехать через друг друга
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼%    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).right();
        hero(1).right();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
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
        // given
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼%    ☼\n" +
                "☼%    ☼\n" +
                "☼˄    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).down();
        hero(1).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼%    ☼\n" +
                "☼%    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).down();
        hero(1).up();
        tick();

        hero(0).down();
        tick();

        hero(1).up();
        
        // then
        // Два танка не могут проехать через друг друга
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼%    ☼\n" +
                "☼%    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).right();
        hero(1).right();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼%►   ☼\n" +
                "☼%˃   ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

	// Лёд
    @Test
    public void shouldBeOilLeak_whenGameCreated() {
        // given when
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼  #  ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // then
        assertEquals(1, field().oil().size());

        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼  #  ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // когда герой двигается по нефтяному пятну, происходит скольжение
    // (он проскальзывает одну команду).
    // Если только заезжаем - то сразу же начинается занос,
    // то есть запоминается команда которой заезжали на пятно
    // Если съезжаем в чистые воды, то любой занос прекращается тут же
    @Test
    public void shouldHeroMoveUp_onOilLeak_afterBeforeWater() {
        // given
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
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        // находимся на льду
        // выполнили команду right(), но танк не реагирует, так как происходит скольжение,Э
        // двигается дальше с предыдущей командой up()
        // RIGHT -> UP (скольжение)
        hero(0).right();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        // двигаемся дальше в направлении up()
        // UP -> UP (выполнилась)
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        // выполнили команду right(), но танк не реагирует, так как происходит скольжение,
        // двигается дальше с предыдущей командой up()
        // RIGHT -> UP (скольжение)
        hero(0).right();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        // выехали со льда
        // двигается дальше в направлении up()
        // UP -> UP (выполнилась)
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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
    public void shouldHeroMoveLeftThenUpThenDown_onOilLeak() {
        // given
        settings().integer(OIL_SLIPPERINESS, 1);

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

        // when
        // заезжаем на лёд
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        // LEFT -> UP (скольжение)
        hero(0).left();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        // DOWN -> DOWN (выполнилась)
        hero(0).down();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        // DOWN -> DOWN (скольжение)
        hero(0).down();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

    // также когда на нем двигается враг, он проскальзывает команду на два тика
    @Test
    public void shouldOtherHeroMoveLeftThenUpThenDown_onOilLeak() {
        // given
        settings().integer(OIL_SLIPPERINESS, 1);

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

        // when
        // враг заезжает на лёд
        hero(0).down();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        // LEFT -> DOWN(скольжение)
        hero(0).left();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        // UP -> UP (выполнилась)
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        // UP -> UP (скольжение)
        // съезд со льда
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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
        // given when
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼  ~  ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // then
        assertEquals(1, field().rivers().size());

        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼  ~  ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

	// Река - через нее герою нельзя пройти, но можно стрелять
	@Test
	public void shouldHeroCanGoIfRiverAtWay() {
        // given
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼~    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).up();
        tick();

        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼~    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

	@Test
	public void shouldBulletCanGoIfRiverAtWay() {
        // given
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼~    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

		// when
        hero(0).up();
		tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼~    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

		// when
        hero(0).fire();
		tick();

		// then
        assertF("☼☼☼☼☼☼☼\n" +
				"☼     ☼\n" +
				"☼     ☼\n" +
				"☼•    ☼\n" +
				"☼~    ☼\n" +
				"☼▲    ☼\n" +
				"☼☼☼☼☼☼☼\n");

		// when
        hero(0).right();
		hero(0).fire();
		tick();

		// then
        assertF("☼☼☼☼☼☼☼\n" +
				"☼•    ☼\n" +
				"☼     ☼\n" +
				"☼     ☼\n" +
				"☼~    ☼\n" +
				"☼ ►•  ☼\n" +
				"☼☼☼☼☼☼☼\n");

		// when
        tick();

		// then
        assertF("☼☼☼☼☼☼☼\n" +
				"☼     ☼\n" +
				"☼     ☼\n" +
				"☼     ☼\n" +
				"☼~    ☼\n" +
				"☼ ►  •☼\n" +
				"☼☼☼☼☼☼☼\n");
	}

    @Test
    public void shouldDoNotMove_whenRiverToWay_goRightOrUpOrLeftOrDown() {
        // given
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ~  ☼\n" +
                "☼ ~▲~ ☼\n" +
                "☼  ~  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).right();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ~  ☼\n" +
                "☼ ~►~ ☼\n" +
                "☼  ~  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ~  ☼\n" +
                "☼ ~▲~ ☼\n" +
                "☼  ~  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).left();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ~  ☼\n" +
                "☼ ~◄~ ☼\n" +
                "☼  ~  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

		// when
        hero(0).down();
		tick();

		// then
        assertF("☼☼☼☼☼☼☼\n" +
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
        // given
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼~ ▲  ☼\n" +
                "☼˄    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(1).up();
        tick();

        hero(1).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼~ ▲  ☼\n" +
                "☼˄    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(1).right();
        hero(1).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼~ ▲  ☼\n" +
                "☼ ˃•  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼~ ▲  ☼\n" +
                "☼ ˃  •☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldOtherHeroDoNotMove_whenRiverToWay_goRightOrUpOrLeftOrDown() {
        // given
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ~  ☼\n" +
                "☼ ~▲~ ☼\n" +
                "☼  ~  ☼\n" +
                "☼    ˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).right();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ~  ☼\n" +
                "☼ ~►~ ☼\n" +
                "☼  ~  ☼\n" +
                "☼    ˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ~  ☼\n" +
                "☼ ~▲~ ☼\n" +
                "☼  ~  ☼\n" +
                "☼    ˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).left();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ~  ☼\n" +
                "☼ ~◄~ ☼\n" +
                "☼  ~  ☼\n" +
                "☼    ˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).down();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ~  ☼\n" +
                "☼ ~▼~ ☼\n" +
                "☼  ~  ☼\n" +
                "☼    ˄☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // Река - через нее боту нельзя пройти. но можно стрелять
    @Test
    public void shouldAIBullet_canGoIfRiverAtWay() {
        // given
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼~ ▲  ☼\n" +
                "☼?    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼~ ▲  ☼\n" +
                "☼◘    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        ai(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼~ ▲  ☼\n" +
                "☼?    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        ai(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼~ ▲  ☼\n" +
                "☼?    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldAIDoNotMove_whenRiverToWay_goRightOrUpOrLeftOrDown() {
        // given
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ~  ☼\n" +
                "☼ ~?~ ☼\n" +
                "☼  ~  ☼\n" +
                "☼    ▲☼\n" +
                "☼☼☼☼☼☼☼\n");

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ~  ☼\n" +
                "☼ ~◘~ ☼\n" +
                "☼  ~  ☼\n" +
                "☼    ▲☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        ai(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ~  ☼\n" +
                "☼ ~?~ ☼\n" +
                "☼  ~  ☼\n" +
                "☼    ▲☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        ai(0).left();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ~  ☼\n" +
                "☼ ~«~ ☼\n" +
                "☼  ~  ☼\n" +
                "☼    ▲☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        ai(0).down();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ~  ☼\n" +
                "☼ ~¿~ ☼\n" +
                "☼  ~  ☼\n" +
                "☼    ▲☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // создаем AI с призами
    @Test
    public void shouldCreatedAiPrize() {
        // given
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

        assertF("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ◘☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        // when
        ai(0).down();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼      ¿☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        assertPrize("1 prizes with 2 heroes");
    }

    // У AI с призами после 4-го хода должен смениться Element
    @Test
    public void shouldSwapElementAfterFourTicks() {
        // given
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

        // then
        assertF("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ◘☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        // when
        ai(0).down();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼      ¿☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        // when
        ai(0).left();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼     « ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        // when
        ai(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼\n" +
                "☼     ? ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        // when
        ai(0).right();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ◘☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        // when
        ai(0).down();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼      ¿☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        assertPrize("1 prizes with 2 heroes");
    }

    // если spawnAiPrize = 3, а спаунится сразу 2 AIа, то 2-й должен быть AIом с призами
    @Test
    public void shouldSpawnAiPrizeWhenTwoAi() {
        // given
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

        // then
        assertF("☼☼☼☼☼☼☼☼☼\n" +
                "☼ ◘    ¿☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertPrize("1 prizes with 3 heroes");
    }

    // если spawnAiPrize = 3 и спаунится сразу 3 AIа, то 2-й должен быть AIом с призами
    @Test
    public void shouldSpawnAiPrizeWhenThreeAi() {
        // given
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
        // then
        assertF("☼☼☼☼☼☼☼☼☼\n" +
                "☼ ◘  ¿ ¿☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertPrize("1 prizes with 4 heroes");
    }

    // если spawnAiPrize = 3, а спаунятся сразу 6 AIов, то должно быть 2 AIа с призами
    @Test
    public void shouldSpawnTwoAiPrizeWhenSixAi() {
        // given
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

        assertF("☼☼☼☼☼☼☼☼☼\n" +
                "☼ ◘¿¿◘¿¿☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertPrize("2 prizes with 7 heroes");
    }

    // если spawnAiPrize = 3, а 3 AIа спаунятся по 1-му за каждый ход,
    // то AI с призами спаунится после 2-го хода
    // так же проверяем что призовой танк меняет свой символ каждые 4 тика
    @Test
    public void shouldSpawnAiPrize_whenAddOneByOneAI() {
        // given
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

        // when
        dropAI(pt(2, 7));

        // then
        assertF("☼☼☼☼☼☼☼☼☼\n" +
                "☼ ◘     ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼\n" +
                "☼ ¿     ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        // when
        dropAI(pt(3, 7));

        // then
        assertF("☼☼☼☼☼☼☼☼☼\n" +
                "☼ ¿¿    ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼\n" +
                "☼ ¿¿    ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        // when
        dropAI(pt(4, 7));

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼\n" +
                "☼ ¿¿¿   ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        assertPrize("1 prizes with 4 heroes");

        // when
        dropAI(pt(5, 7));
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼\n" +
                "☼ ◘¿¿¿  ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        assertPrize("2 prizes with 5 heroes");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼\n" +
                "☼ ¿¿¿¿  ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼\n" +
                "☼ ¿¿¿¿  ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼\n" +
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
        // given
        settings().integer(KILL_HITS_AI_PRIZE, 3);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼?    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼◘    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼¿    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼¿    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼¿    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        verifyAllEvents("[KILL_AI]");

        assertF("☼☼☼☼☼☼☼\n" +
                "☼1    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldMyBulletsRemovesWhenKillMe() {
        // given
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼˃   ˃☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼˃   ˃☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(1).fire();
        tick();

        // then
        verifyAllEvents(
                "listener(0) => [KILL_OTHER_HERO[1]]\n" +
                "listener(1) => [HERO_DIED]\n");

        assertF("☼☼☼☼☼☼☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼Ѡ • ˃☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        newGameFor(player(1), 2, 2);

        // then
        verifyAllEvents("");

        assertF("☼☼☼☼☼☼☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ˄   ☼\n" +
                "☼    ˃☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldDropPrize_onlyInPointKilledAiPrize() {
        // given
        settings().integer(KILL_HITS_AI_PRIZE, 1);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼¿    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertF("☼☼☼☼☼☼☼\n" +
                "☼◘    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        ai(0).kill(mock(Bullet.class));

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼1    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldDropPrize_inPointKilledAiPrize_underTree() {
        // given
        settings().integer(KILL_HITS_AI_PRIZE, 1);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼¿    ☼\n" +
                "☼%    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertF("☼☼☼☼☼☼☼\n" +
                "☼◘    ☼\n" +
                "☼%    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        ai(0).dontShoot = true;
        ai(0).down();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼%    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        ai(0).kill(mock(Bullet.class));

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼1    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldDropPrize_InPointKilledAiPrize_onOilLeak() {
        // given
        settings().integer(KILL_HITS_AI_PRIZE, 1);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼¿    ☼\n" +
                "☼#    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertF("☼☼☼☼☼☼☼\n" +
                "☼◘    ☼\n" +
                "☼#    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        ai(0).dontShoot = true;
        ai(0).down();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        ai(0).kill(mock(Bullet.class));

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
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
        // given
        settings().integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 2);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼¿    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        ai(0).kill(mock(Bullet.class));
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼1    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼!    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
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
        // given
        settings().integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 3);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼¿    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        ai(0).kill(mock(Bullet.class));

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼1    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼!    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼1    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
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
        // given
        settings().integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 4);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼¿    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        ai(0).kill(mock(Bullet.class));
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼1    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼!    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼1    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼!    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
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
        // given
        settings().integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 2);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼¿    ☼\n" +
                "☼%    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertF("☼☼☼☼☼☼☼\n" +
                "☼◘    ☼\n" +
                "☼%    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        ai(0).dontShoot = true;
        ai(0).down();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼%    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        ai(0).kill(mock(Bullet.class));

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼1    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼!    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼%    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // приз на нефти должен исчезнуть через 2 тика, если его не подобрали
    // после исчезновения приза видим нефть
    @Test
    public void shouldExpirePrizeOnField_disappearOnOilLeak() {
        // given
        settings().integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 2);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼¿    ☼\n" +
                "☼#    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertF("☼☼☼☼☼☼☼\n" +
                "☼◘    ☼\n" +
                "☼#    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        ai(0).dontShoot = true;
        ai(0).down();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        ai(0).kill(mock(Bullet.class));

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼1    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼!    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼#    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldHeroTookPrize() {
        // given
        settings().integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 3);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        ai(0).kill(mock(Bullet.class));

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        dice(DICE_IMMORTALITY);
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼1    ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents("");

        // when
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[PRIZE_IMMORTALITY(0/10)]");

        verifyAllEvents("[CATCH_PRIZE[1]]");

        // when
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldOtherTookPrize() {
        // given
        settings().integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 3);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    ¿☼\n" +
                "☼     ☼\n" +
                "☼▲   ˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        ai(0).kill(mock(Bullet.class));

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    Ѡ☼\n" +
                "☼     ☼\n" +
                "☼▲   ˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        dice(DICE_IMMORTALITY);
        hero(1).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    1☼\n" +
                "☼    ˄☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents("");

        // when
        hero(1).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    ˄☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[]");

        assertPrize(hero(1), "[PRIZE_IMMORTALITY(0/10)]");

        verifyAllEvents(
                "listener(1) => [CATCH_PRIZE[1]]\n");

        // when
        hero(1).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
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
        // given
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

        // when
        ai(0).down();
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼%%   ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        ai(0).kill(mock(Bullet.class));
        dice(DICE_IMMORTALITY);
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼1%   ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents("");

        // when
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲%   ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[PRIZE_IMMORTALITY(0/10)]");

        verifyAllEvents("[CATCH_PRIZE[1]]");

        // when
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
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
        // given
        settings().integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 3);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼%%   ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        ai(0).down();
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼%%   ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        ai(0).kill(mock(Bullet.class));
        dice(DICE_IMMORTALITY);
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼1%   ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents("");

        // when
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼%%   ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[PRIZE_IMMORTALITY(0/10)]");

        verifyAllEvents("[CATCH_PRIZE[1]]");

        // when
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼%%   ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldOtherTookPrize_underTree() {
        // given
        settings().integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 3);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼    ¿☼\n" +
                "☼    %☼\n" +
                "☼     ☼\n" +
                "☼▲   ˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        ai(0).down();
        hero(1).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    %☼\n" +
                "☼    ˄☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        ai(0).kill(mock(Bullet.class));
        dice(DICE_IMMORTALITY);
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    1☼\n" +
                "☼    ˄☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents("");

        // when
        hero(1).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    %☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[]");

        assertPrize(hero(1), "[PRIZE_IMMORTALITY(0/10)]");

        verifyAllEvents(
                "listener(1) => [CATCH_PRIZE[1]]\n");

        // when
        hero(1).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼    ˄☼\n" +
                "☼    %☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldHeroTookPrize_onOilLeak() {
        // given
        settings().integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 3);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼#    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        ai(0).down();
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");


        ai(0).kill(mock(Bullet.class));
        dice(DICE_IMMORTALITY);
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼1    ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents("");

        // when
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents("[CATCH_PRIZE[1]]");

        assertPrize(hero(0), "[PRIZE_IMMORTALITY(0/10)]");

        // when
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼#    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldOtherTookPrize_onOilLeak() {
        // given
        settings().integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 3);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼    ¿☼\n" +
                "☼    #☼\n" +
                "☼     ☼\n" +
                "☼▲   ˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        ai(0).down();
        hero(1).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    ¿☼\n" +
                "☼    ˄☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        ai(0).kill(mock(Bullet.class));
        dice(DICE_IMMORTALITY);
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    1☼\n" +
                "☼    ˄☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents("");

        // when
        hero(1).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    ˄☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[]");

        assertPrize(hero(1), "[PRIZE_IMMORTALITY(0/10)]");

        verifyAllEvents(
                "listener(1) => [CATCH_PRIZE[1]]\n");

        // when
        hero(1).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼    ˄☼\n" +
                "☼    #☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldAiDontTookPrize() {
        // given
        settings().integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 3);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼    ▲☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        ai(0).kill(mock(Bullet.class));
        ai(1).dontShoot = true;
        ai(1).up();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼?    ☼\n" +
                "☼    ▲☼\n" +
                "☼☼☼☼☼☼☼\n");

        dice(DICE_IMMORTALITY);
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼1    ☼\n" +
                "☼?    ☼\n" +
                "☼     ☼\n" +
                "☼    ▲☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        ai(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼?    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    ▲☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        ai(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
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
        // given
        settings().integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 3);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        ai(0).kill(mock(Bullet.class));

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents("");

        // when
        dice(DICE_IMMORTALITY);
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[PRIZE_IMMORTALITY(0/10)]");

        verifyAllEvents("[CATCH_PRIZE[1]]");

        // when
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
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
        // given
        settings().integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 6);

        givenFl("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼¿   ˂ ☼\n" +
                "☼      ☼\n" +
                "☼▲     ☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        // when
        ai(0).kill(mock(Bullet.class));

        // then
        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼Ѡ   ˂ ☼\n" +
                "☼      ☼\n" +
                "☼▲     ☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        verifyAllEvents("");

        // when
        dice(DICE_IMMORTALITY,
                6, 6); // новый AI генерим так, чтобы не мешался
        hero(0).up();
        hero(1).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼     ¿☼\n" +
                "☼     •☼\n" +
                "☼1 • ˂ ☼\n" +
                "☼▲     ☼\n" +
                "☼      ☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        verifyAllEvents("");

        // when
        hero(0).up();
        hero(1).left();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼     ¿☼\n" +
                "☼Ѡ  ˂  ☼\n" +
                "☼     •☼\n" +
                "☼      ☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[]");

        verifyAllEvents(
                "listener(0) => [HERO_DIED]\n" +
                        "listener(1) => [KILL_OTHER_HERO[1]]\n");

        // when
        newGameFor(player(0), 5, 1);

        // then
        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼     ¿☼\n" +
                "☼!  ˂  ☼\n" +
                "☼     •☼\n" +
                "☼    ▲ ☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        // when
        hero(1).left();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼1 ˂  ¿☼\n" +
                "☼      ☼\n" +
                "☼    ▲ ☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        // when
        hero(1).left();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼!˂    ☼\n" +
                "☼     ¿☼\n" +
                "☼    ▲ ☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        verifyAllEvents("");

        // when
        hero(1).left();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼˂     ☼\n" +
                "☼      ☼\n" +
                "☼    ▲¿☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        assertPrize(hero(1), "[PRIZE_IMMORTALITY(0/10)]");

        verifyAllEvents(
                "listener(1) => [CATCH_PRIZE[1]]\n");

        // when
        hero(1).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼˄     ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼    ▲»☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        assertPrize(hero(1), "[PRIZE_IMMORTALITY(1/10)]");
    }

    @Test
    public void shouldHeroKillPrize() {
        // given
        settings().integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 3);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        ai(0).kill(mock(Bullet.class));

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        dice(DICE_IMMORTALITY);
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼1    ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
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
        // given
        settings().integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 3);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        ai(0).kill(mock(Bullet.class));

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        dice(DICE_IMMORTALITY);
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼1    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
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
        // given
        settings().integer(KILL_HITS_AI_PRIZE, 2)
                .integer(PRIZE_ON_FIELD, 3);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼¿    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        ai(0).dontShoot = true;

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼◘    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        ai(0).down();
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        ai(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents("");

        // when
        ai(0).down();
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents("");

        // when
        ai(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents("[KILL_AI]");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼1    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
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
        // given
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

        // when
        ai(0).kill(mock(Bullet.class));

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼╬   ╬☼\n" +
                "☼ Ѡ   ☼\n" +
                "☼ ▲╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        dice(DICE_BREAKING_WALLS);
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼╬   ╬☼\n" +
                "☼ 2   ☼\n" +
                "☼ ▲╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents("");

        // when
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼╬   ╬☼\n" +
                "☼ ▲   ☼\n" +
                "☼  ╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[PRIZE_BREAKING_WALLS(0/5)]");

        verifyAllEvents("[CATCH_PRIZE[2]]");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼  ╬  ☼\n" +
                "☼ •   ☼\n" +
                "☼╬   ╬☼\n" +
                "☼ ▲   ☼\n" +
                "☼  ╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼  ╬  ☼\n" +
                "☼ •   ☼\n" +
                "☼╬   ╬☼\n" +
                "☼ ▲   ☼\n" +
                "☼  ╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼  ╬  ☼\n" +
                "☼ •   ☼\n" +
                "☼╬   ╬☼\n" +
                "☼ ▲   ☼\n" +
                "☼  ╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼  ╬  ☼\n" +
                "☼ •   ☼\n" +
                "☼╬   ╬☼\n" +
                "☼ ▲   ☼\n" +
                "☼  ╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼  ╬  ☼\n" +
                "☼ •   ☼\n" +
                "☼╬   ╬☼\n" +
                "☼ ▲   ☼\n" +
                "☼  ╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼  ╬  ☼\n" +
                "☼ •   ☼\n" +
                "☼╬   ╬☼\n" +
                "☼ ▲   ☼\n" +
                "☼  ╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        // The impact of the prize ended. should stop shooting
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼╬   ╬☼\n" +
                "☼ ▲   ☼\n" +
                "☼  ╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");


        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼╬   ╬☼\n" +
                "☼ ▲   ☼\n" +
                "☼  ╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        // should shoot now
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼  ╬  ☼\n" +
                "☼ •   ☼\n" +
                "☼╬   ╬☼\n" +
                "☼ ▲   ☼\n" +
                "☼  ╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        // silence
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼╬   ╬☼\n" +
                "☼ ▲   ☼\n" +
                "☼  ╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼╬   ╬☼\n" +
                "☼ ▲   ☼\n" +
                "☼  ╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        // and shoot again
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼  ╬  ☼\n" +
                "☼ •   ☼\n" +
                "☼╬   ╬☼\n" +
                "☼ ▲   ☼\n" +
                "☼  ╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        // and silence
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼╬   ╬☼\n" +
                "☼ ▲   ☼\n" +
                "☼  ╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldHeroTakePrize_breakingWalls() {
        // given
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

        // when
        ai(0).kill(mock(Bullet.class));

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼╬Ѡ  ╬☼\n" +
                "☼     ☼\n" +
                "☼ ▲╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        dice(DICE_BREAKING_WALLS);
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼╬2  ╬☼\n" +
                "☼ ▲   ☼\n" +
                "☼  ╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents("");

        // when
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼╬▲  ╬☼\n" +
                "☼     ☼\n" +
                "☼  ╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[PRIZE_BREAKING_WALLS(0/10)]");

        verifyAllEvents("[CATCH_PRIZE[2]]");

        // when
        hero(0).right();
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼╬ ►•╬☼\n" +
                "☼     ☼\n" +
                "☼  ╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).up();
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ▲  ☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼  ╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).down();
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬ ▼  ☼\n" +
                "☼  •  ☼\n" +
                "☼  ╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).left();
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ◄   ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldHeroTakePrizeEnemyWithoutPrize_breakingWalls() {
        // given
        settings().integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 5);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼╬╬╬  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼▲ ˄  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        ai(0).kill(mock(Bullet.class));

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼╬╬╬  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼▲ ˄  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        dice(DICE_BREAKING_WALLS);
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼╬╬╬  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼2    ☼\n" +
                "☼▲ ˄  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).up();
        hero(1).up();
        tick();

        assertPrize(hero(0), "[PRIZE_BREAKING_WALLS(0/10)]");

        verifyAllEvents(
                "listener(0) => [CATCH_PRIZE[2]]\n");

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼╬╬╬  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲ ˄  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        hero(1).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼╬╬╬  ☼\n" +
                "☼• •  ☼\n" +
                "☼     ☼\n" +
                "☼▲ ˄  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼ ╬╩  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲ ˄  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldHeroNotBreakingReefs_whenItFiresAtThem() {
        // given
        settings().integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 5);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼╬╬╬  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        ai(0).kill(mock(Bullet.class));

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼╬╬╬  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents("");

        // when
        dice(DICE_BREAKING_WALLS);
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼╬╬╬  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼2    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).up();
        tick();

        assertPrize(hero(0), "[PRIZE_BREAKING_WALLS(0/10)]");

        verifyAllEvents("[CATCH_PRIZE[2]]");

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼╬╬╬  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼╬╬╬  ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼ ╬╬  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼ ╬╬  ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼ ╬╬  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldEndPrizeWorking_breakingWalls() {
        // given
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

        // when
        ai(0).kill(mock(Bullet.class));

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents("");

        // when
        dice(DICE_BREAKING_WALLS);
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼2    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).up();
        tick();

        // then
        assertPrize(hero(0), "[PRIZE_BREAKING_WALLS(0/1)]");

        verifyAllEvents("[CATCH_PRIZE[2]]");

        assertF("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼╩    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldHeroTakePrizeEnemyShootsHero_immortality() {
        // given
        settings().integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 5);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼▲   ˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        ai(0).kill(mock(Bullet.class));

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼▲   ˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        dice(DICE_IMMORTALITY);
        hero(1).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼1   ˄☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents("");

        // when
        hero(0).up();
        hero(1).left();
        tick();

        // then
        assertPrize(hero(0), "[PRIZE_IMMORTALITY(0/10)]");
        verifyAllEvents(
                "listener(0) => [CATCH_PRIZE[1]]\n");

        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲  ˂ ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(1).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲• ˂ ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
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
        // given
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

        // when
        ai(0).kill(mock(Bullet.class));

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼▲   ˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        dice(DICE_IMMORTALITY);
        hero(1).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼1   ˄☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents("");

        // when
        hero(0).up();
        hero(1).left();
        tick();

        // then
        assertPrize(hero(0), "[PRIZE_IMMORTALITY(0/3)]");

        verifyAllEvents(
                "listener(0) => [CATCH_PRIZE[1]]\n");

        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲  ˂ ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(1).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲• ˂ ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        verifyAllEvents("");

        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲  ˂ ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();
        tick();

        // then
        assertPrize(hero(0), "[]");

        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲  ˂ ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(1).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲• ˂ ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
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
        // given
        settings().integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 5);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼¿    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼?    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        ai(0).kill(mock(Bullet.class));

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        dice(DICE_IMMORTALITY);
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼1    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼!    ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents("");

        // when
        hero(0).up();
        tick();

        // then
        assertPrize(hero(0), "[PRIZE_IMMORTALITY(0/10)]");

        verifyAllEvents("[CATCH_PRIZE[1]]");

        assertF("☼☼☼☼☼☼☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        ai(0).up();
        ai(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼?    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
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
        // given
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

        // when
        ai(0).kill(mock(Bullet.class));
        ai(1).dontShoot = true;

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        dice(DICE_IMMORTALITY);
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼1    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).up();
        ai(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼!    ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼?    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents("");

        // when
        hero(0).up();
        ai(0).down();
        tick();

        // then
        assertPrize(hero(0), "[PRIZE_IMMORTALITY(0/2)]");

        verifyAllEvents("[CATCH_PRIZE[1]]");

        assertF("☼☼☼☼☼☼☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        ai(0).up();
        ai(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼?    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        ai(0).down();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents("");

        // when
        ai(0).up();
        ai(0).fire();
        tick();

        // then
        assertPrize(hero(0), "[]");

        assertF("☼☼☼☼☼☼☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼?    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼?    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents("[HERO_DIED]");
    }

    // если герой заехал на нефтяное пятно, а в следующий тик не указал
    // никакой команды, то продолжается движение вперед по старой команде на 1 тик.
    @Test
    public void shouldHeroSlidingOneTicks() {
        // given
        settings().integer(OIL_SLIPPERINESS, 3);

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

        // when
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

    // если герой заехал на нефтяное пятно, а в следующий тик указал
    // какую-то команду, то продолжается движение по старой команде N тиков.
    @Test
    public void shouldHeroSlidingNTicks() {
        // given
        settings().integer(OIL_SLIPPERINESS, 3);

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

        // when
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        // RIGHT -> UP
        hero(0).right();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        // RIGHT -> UP
        hero(0).right();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        // RIGHT -> UP
        hero(0).right();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        // RIGHT -> RIGHT
        hero(0).right();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

    // если герой заехал на нефтяное пятно, то продолжается движение
    // по старой команде N тиков слушается команда N + 1 и опять занос N тиков
    @Test
    public void shouldHeroSlidingNTicks_andAgainSliding() {
        // given
        settings().integer(OIL_SLIPPERINESS, 3);

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

        // when
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        // RIGHT -> UP
        hero(0).right();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        // RIGHT -> UP
        hero(0).right();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        // RIGHT -> UP
        hero(0).right();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        // RIGHT -> RIGHT
        hero(0).right();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        // UP -> RIGHT
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        // UP -> RIGHT
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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

        // when
        // UP -> UP
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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
        // given
        settings().integer(OIL_SLIPPERINESS, 5);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼###  ☼\n" +
                "☼#    ☼\n" +
                "☼#    ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼###  ☼\n" +
                "☼#    ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        // RIGHT -> UP
        hero(0).right();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼###  ☼\n" +
                "☼▲    ☼\n" +
                "☼#    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        // RIGHT -> UP
        hero(0).right();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼▲##  ☼\n" +
                "☼#    ☼\n" +
                "☼#    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        // RIGHT -> UP -> wall -> Canceled sliding
        hero(0).right();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼▲##  ☼\n" +
                "☼#    ☼\n" +
                "☼#    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        // RIGHT -> RIGHT
        hero(0).right();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼#►#  ☼\n" +
                "☼#    ☼\n" +
                "☼#    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldHeroCanGoIfRiverAtWay_whenHeroTakePrize() {
        // given
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

        // when
        ai(0).kill(mock(Bullet.class));

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼~    ☼\n" +
                "☼  ╬  ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        dice(DICE_WALKING_ON_WATER);
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼~    ☼\n" +
                "☼  ╬  ☼\n" +
                "☼3    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents("");

        // when
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼~    ☼\n" +
                "☼  ╬  ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).up();
        tick();

        // then
        verifyAllEvents("[CATCH_PRIZE[3]]");

        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼~    ☼\n" +
                "☼▲ ╬  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼▲    ☼\n" +
                "☼~    ☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldHeroTakePrize_walkOnWater() {
        // given
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

        // when
        ai(0).kill(mock(Bullet.class));

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼~~~  ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼▲ ˄  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        dice(DICE_WALKING_ON_WATER);
        hero(0).up();
        hero(1).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼~~~  ☼\n" +
                "☼3    ☼\n" +
                "☼▲ ˄  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents("");

        // when
        hero(0).up();
        hero(1).up();
        tick();

        // then
        assertPrize(hero(0), "[PRIZE_WALKING_ON_WATER(0/3)]");
        verifyAllEvents(
                "listener(0) => [CATCH_PRIZE[3]]\n");

        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼~~~  ☼\n" +
                "☼▲ ˄  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).up();
        hero(1).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼▲~~  ☼\n" +
                "☼  ˄  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).up();
        hero(1).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
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
        // given
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

        // when
        ai(0).kill(mock(Bullet.class));

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼~    ☼\n" +
                "☼     ☼\n" +
                "☼~ ╬  ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        dice(DICE_WALKING_ON_WATER);
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼~    ☼\n" +
                "☼     ☼\n" +
                "☼~ ╬  ☼\n" +
                "☼3    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents("");

        // when
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼~    ☼\n" +
                "☼     ☼\n" +
                "☼~ ╬  ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).up();
        tick();

        // then
        verifyAllEvents("[CATCH_PRIZE[3]]");

        assertF("☼☼☼☼☼☼☼\n" +
                "☼~    ☼\n" +
                "☼     ☼\n" +
                "☼▲ ╬  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼~    ☼\n" +
                "☼▲    ☼\n" +
                "☼~ ╬  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).up();
        tick();

        hero(0).up();
        tick();

        hero(0).up();
        tick();

        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
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
        // given
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

        // when
        ai(0).kill(mock(Bullet.class));

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~~~☼\n" +
                "☼  Ѡ  ☼\n" +
                "☼  ▲  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        dice(DICE_WALKING_ON_WATER);
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~~~☼\n" +
                "☼  3  ☼\n" +
                "☼  ▲  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents("");

        assertPrize(hero(0), "[]");

        // when
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~~~☼\n" +
                "☼  ▲  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[PRIZE_WALKING_ON_WATER(0/2)]");

        verifyAllEvents("[CATCH_PRIZE[3]]");

        // when
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~~~☼\n" +
                "☼~~▲~~☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[PRIZE_WALKING_ON_WATER(1/2)]");

        // when
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼~~~~~☼\n" +
                "☼~~▲~~☼\n" +
                "☼~~~~~☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[PRIZE_WALKING_ON_WATER(2/2)]");

        // when
        // действие приза закончилось
        // герой получает штраф 4 тика
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼~~~~~☼\n" +
                "☼~~▲~~☼\n" +
                "☼~~~~~☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[]");

        // when
        hero(0).down();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼~~~~~☼\n" +
                "☼~~▼~~☼\n" +
                "☼~~~~~☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[]");

        // when
        hero(0).left();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼~~~~~☼\n" +
                "☼~~◄~~☼\n" +
                "☼~~~~~☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[]");

        // when
        hero(0).right();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼~~~~~☼\n" +
                "☼~~►~~☼\n" +
                "☼~~~~~☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[]");

        // when
        // штраф 4 тика закончился. Возможно перемещение
        hero(0).right();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~►~☼\n" +
                "☼~~~~~☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[]");

        // when
        // штраф еще 4 тика, так как герой снова на воде
        hero(0).left();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~◄~☼\n" +
                "☼~~~~~☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[]");

        // when
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~▲~☼\n" +
                "☼~~~~~☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[]");

        // when
        hero(0).down();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~▼~☼\n" +
                "☼~~~~~☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[]");

        // when
        hero(0).right();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~►~☼\n" +
                "☼~~~~~☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[]");

        // when
        // штраф 4 тика закончился. Возможно перемещение
        hero(0).right();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~~►☼\n" +
                "☼~~~~~☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[]");

        // when
        // штраф еще 4 тика, так как герой снова на воде
        hero(0).down();
        tick();
        tick();
        tick();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~~▼☼\n" +
                "☼~~~~~☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[]");

        // when
        hero(0).down();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~~▼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[]");

        // when
        // мы все так же на воде, а потому не можем двигаться 4 тика
        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~~▲☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[]");

        // when
        hero(0).left();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~~◄☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[]");

        // when
        // но можем выехать на сушу, хоть штраф не закончился
        hero(0).down();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~~~☼\n" +
                "☼    ▼☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[]");

        // when
        // обратно заехать уже не можем как ни старайся
        hero(0).up();
        tick();

        hero(0).up();
        tick();

        hero(0).up();
        tick();

        hero(0).up();
        tick();

        hero(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
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
        // given
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

        // when
        ai(0).kill(mock(Bullet.class));

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼╬╬╬  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼▲ ˄  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        dice(DICE_BREAKING_WALLS);
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼╬╬╬  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼2    ☼\n" +
                "☼▲ ˄  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents("");

        // when
        hero(0).up();
        hero(1).up();
        tick();

        // then
        assertPrize(hero(0), "[PRIZE_BREAKING_WALLS(0/3)]");

        verifyAllEvents(
                "listener(0) => [CATCH_PRIZE[2]]\n");

        assertF("☼☼☼☼☼☼☼\n" +
                "☼╬╬╬  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲ ˄  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        hero(1).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼╬╬╬  ☼\n" +
                "☼• •  ☼\n" +
                "☼     ☼\n" +
                "☼▲ ˄  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        hero(1).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼ ╬╩  ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲ ˄  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        hero(1).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼ ╬╩  ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲ ˄  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        hero(1).fire();
        tick();

        // then
        assertPrize(hero(0), "[]");

        assertF("☼☼☼☼☼☼☼\n" +
                "☼ ╬╩  ☼\n" +
                "☼• •  ☼\n" +
                "☼     ☼\n" +
                "☼▲ ˄  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        hero(1).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
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
        // given
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼~    ☼\n" +
                "☼~   ▲☼\n" +
                "☼?    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        ai(0).up();
        ai(0).dontMove = false;
        ai(0).dontShoot = false;

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼~    ☼\n" +
                "☼~   ▲☼\n" +
                "☼◘    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼~   ▲☼\n" +
                "☼?    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼~    ☼\n" +
                "☼~   ▲☼\n" +
                "☼?    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼~    ☼\n" +
                "☼~   ▲☼\n" +
                "☼?    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼~    ☼\n" +
                "☼~   ▲☼\n" +
                "☼◘    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼~    ☼\n" +
                "☼~   ▲☼\n" +
                "☼?    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        dice(1);
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼~    ☼\n" +
                "☼~   ▲☼\n" +
                "☼ »   ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // если дропнуть танк на нефть, то случался NPE
    // теперь все нормально
    @Test
    public void shouldDropAiOnOilLeak() {
        // given
        settings().integer(OIL_SLIPPERINESS, 2);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼#    ☼\n" +
                "☼#    ☼\n" +
                "☼#    ☼\n" +
                "☼#    ☼\n" +
                "☼    ▲☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        field().getAiGenerator().drop(pt(1, 5));
        ai(0).dontShoot = true;

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼◘    ☼\n" +
                "☼#    ☼\n" +
                "☼#    ☼\n" +
                "☼#    ☼\n" +
                "☼    ▲☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        ai(0).down();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼#    ☼\n" +
                "☼¿    ☼\n" +
                "☼#    ☼\n" +
                "☼#    ☼\n" +
                "☼    ▲☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        ai(0).right();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼#    ☼\n" +
                "☼#    ☼\n" +
                "☼¿    ☼\n" +
                "☼#    ☼\n" +
                "☼    ▲☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        ai(0).right();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
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
        // given
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼~    ☼\n" +
                "☼~    ☼\n" +
                "☼~    ☼\n" +
                "☼~    ☼\n" +
                "☼    ▲☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        dice(2);
        dropAI(pt(1, 5));

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼~◘   ☼\n" +
                "☼~    ☼\n" +
                "☼~    ☼\n" +
                "☼~    ☼\n" +
                "☼    ▲☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        ai(0).down();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼~    ☼\n" +
                "☼~¿   ☼\n" +
                "☼~    ☼\n" +
                "☼~    ☼\n" +
                "☼    ▲☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // если spawnAiPrize = 2, то должно быть 3 AIа с призами
    // если aiPrizeLimit = 2, то будет на поле 2 AIа с призами
    @Test
    public void shouldSpawnTwoAiPrize() {
        // given
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

        assertF("☼☼☼☼☼☼☼☼☼\n" +
                "☼ ◘¿◘¿¿¿☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertPrize("2 prizes with 7 heroes");
    }

    // если spawnAiPrize = 2, то каждый второй AI будет с призами
    // если на поле уже лежит приз и есть один AI с призом, то при aiPrizeLimit = 2,
    // AIов с призами больше появляться не будет
    @Test
    public void shouldNotSpawnAiPrize_ifPrizeOnField() {
        // given
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

        assertF("☼☼☼☼☼☼☼☼☼\n" +
                "☼◘¿◘¿¿¿ ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        assertPrize("2 prizes with 7 heroes");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼\n" +
                "☼¿¿¿¿¿¿ ☼\n" +
                "☼       ☼\n" +
                "☼•      ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼\n" +
                "☼Ѡ¿¿¿¿¿ ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼\n" +
                "☼1¿¿¿¿¿ ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        verifyAllEvents("[KILL_AI]");

        // when
        dropAI(pt(4, 6));
        dropAI(pt(5, 6));
        dropAI(pt(6, 6));
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼\n" +
                "☼!¿◘¿¿¿ ☼\n" +
                "☼   ¿¿¿ ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        assertPrize("1 prizes with 9 heroes");
    }

    @Test
    public void shouldSpawnAiPrize_ifKillPrize() {
        // given
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

        assertF("☼☼☼☼☼☼☼☼☼\n" +
                "☼◘¿◘¿¿¿ ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        assertPrize("2 prizes with 7 heroes");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼\n" +
                "☼¿¿¿¿¿¿ ☼\n" +
                "☼       ☼\n" +
                "☼•      ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼\n" +
                "☼Ѡ¿¿¿¿¿ ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼\n" +
                "☼1¿¿¿¿¿ ☼\n" +
                "☼       ☼\n" +
                "☼•      ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        verifyAllEvents("[KILL_AI]");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼\n" +
                "☼Ѡ¿◘¿¿¿ ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼\n" +
                "☼ ¿¿¿¿¿ ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        // when
        dropAI(pt(4, 6));
        dropAI(pt(5, 6));
        dropAI(pt(6, 6));
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼\n" +
                "☼ ¿¿¿¿¿ ☼\n" +
                "☼   ¿¿¿ ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        assertPrize("2 prizes with 9 heroes");
    }

    @Test
    public void bulletWithTick0ShouldNotAffectHero(){
        // given
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

        // when
        hero(0).fire();
        hero(0).up();
        hero(1).right();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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
        // given
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

        // when
        hero(1).fire();
        hero(1).right();
        hero(0).up();
        hero(0).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼☼☼☼☼\n" +
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
        // given
        settings().integer(PRIZE_ON_FIELD, 5)
                .integer(KILL_HITS_AI_PRIZE, 1);

        catchVisibilityPrizeWithAIOnMap();

        assertPrize(hero(0), "[PRIZE_VISIBILITY(0/10)]");

        assertF("☼☼☼☼☼☼☼\n" +
                "☼  %? ☼\n" +
                "☼  %% ☼\n" +
                "☼▲ %% ☼\n" +
                "☼  %% ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldHeroTakePrizeAndSeeEnemyUnderTree_visibility() {
        // given
        settings().integer(PRIZE_ON_FIELD, 5)
                .integer(KILL_HITS_AI_PRIZE, 1);

        catchVisibilityPrizeWithEnemyOnMap();

        assertPrize(hero(0), "[PRIZE_VISIBILITY(0/10)]");

        assertF("☼☼☼☼☼☼☼\n" +
                "☼  %% ☼\n" +
                "☼  %˄ ☼\n" +
                "☼▲ %% ☼\n" +
                "☼  %% ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    private void catchVisibilityPrizeWithEnemyOnMap() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼  %% ☼\n" +
                "☼  %% ☼\n" +
                "☼¿ %% ☼\n" +
                "☼  %% ☼\n" +
                "☼▲   ˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        ai(0).kill(mock(Bullet.class));

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼  %% ☼\n" +
                "☼  %% ☼\n" +
                "☼Ѡ %% ☼\n" +
                "☼  %% ☼\n" +
                "☼▲   ˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        dice(DICE_VISIBILITY);
        hero(1).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼  %% ☼\n" +
                "☼  %% ☼\n" +
                "☼4 %% ☼\n" +
                "☼  %%˄☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(1).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼  %% ☼\n" +
                "☼  %% ☼\n" +
                "☼! %%˄☼\n" +
                "☼  %% ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).up();
        hero(1).left();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼  %% ☼\n" +
                "☼  %% ☼\n" +
                "☼4 %% ☼\n" +
                "☼▲ %% ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents("");

        // when
        hero(0).up();
        hero(1).up();
        tick();

        // then
        verifyAllEvents(
                "listener(0) => [CATCH_PRIZE[4]]\n");
    }

    @Test
    public void shouldHeroTakePrizeAndSeeBulletsUnderTree_visibility() {
        // given
        shouldHeroTakePrizeAndSeeEnemyUnderTree_visibility();

        assertF("☼☼☼☼☼☼☼\n" +
                "☼  %% ☼\n" +
                "☼  %˄ ☼\n" +
                "☼▲ %% ☼\n" +
                "☼  %% ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).right();
        hero(1).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼  %˄ ☼\n" +
                "☼  %% ☼\n" +
                "☼ ►%% ☼\n" +
                "☼  %% ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).fire();
        hero(1).down();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼  %% ☼\n" +
                "☼  %˅ ☼\n" +
                "☼ ►%• ☼\n" +
                "☼  %% ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(1).fire();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼  %% ☼\n" +
                "☼  %˅ ☼\n" +
                "☼ ►%% ☼\n" +
                "☼  %• ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldEndPrizeWorkingDontSeeBulletsUnderTree_visibility() {
        // given
        shouldHeroTakePrizeAndSeeBulletsUnderTree_visibility();

        assertF("☼☼☼☼☼☼☼\n" +
                "☼  %% ☼\n" +
                "☼  %˅ ☼\n" +
                "☼ ►%% ☼\n" +
                "☼  %• ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[PRIZE_VISIBILITY(3/10)]");

        // when
        for (int count = 0; count < 7; count++) {
            tick();
        }

        // then
        assertPrize(hero(0), "[PRIZE_VISIBILITY(10/10)]");

        // when
        hero(0).fire();
        tick();

        // then
        assertPrize(hero(0), "[]");

        assertF("☼☼☼☼☼☼☼\n" +
                "☼  %% ☼\n" +
                "☼  %% ☼\n" +
                "☼ ►%% ☼\n" +
                "☼  %% ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(1).fire();
        tick();

        // then
        assertPrize(hero(0), "[]");

        assertF("☼☼☼☼☼☼☼\n" +
                "☼  %% ☼\n" +
                "☼  %% ☼\n" +
                "☼ ►%% ☼\n" +
                "☼  %% ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldEndPrizeWorkingDontSeeAiUnderTree_visibility() {
        // given
        settings().integer(PRIZE_ON_FIELD, 5)
                .integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_WORKING, 2);

        catchVisibilityPrizeWithAIOnMap();

        assertPrize(hero(0), "[PRIZE_VISIBILITY(0/2)]");

        assertF("☼☼☼☼☼☼☼\n" +
                "☼  %? ☼\n" +
                "☼  %% ☼\n" +
                "☼▲ %% ☼\n" +
                "☼  %% ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        ai(0).left();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼  «% ☼\n" +
                "☼  %% ☼\n" +
                "☼▲ %% ☼\n" +
                "☼  %% ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        ai(0).down();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼  %% ☼\n" +
                "☼  ¿% ☼\n" +
                "☼▲ %% ☼\n" +
                "☼  %% ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        ai(0).down();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼  %% ☼\n" +
                "☼  %% ☼\n" +
                "☼▲ %% ☼\n" +
                "☼  %% ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[]");
    }

    private void catchVisibilityPrizeWithAIOnMap() {
        // given
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼  %% ☼\n" +
                "☼  %% ☼\n" +
                "☼¿ %%¿☼\n" +
                "☼  %% ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        ai(1).kill(mock(Bullet.class));
        ai(0).left();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼  %% ☼\n" +
                "☼  %% ☼\n" +
                "☼Ѡ %%«☼\n" +
                "☼  %% ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        dice(DICE_VISIBILITY);
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼  %% ☼\n" +
                "☼  %% ☼\n" +
                "☼4 %% ☼\n" +
                "☼  %% ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(0).up();
        ai(0).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼  %% ☼\n" +
                "☼  %% ☼\n" +
                "☼! %% ☼\n" +
                "☼▲ %% ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        verifyAllEvents("");

        // when
        hero(0).up();
        ai(0).up();
        tick();

        // then
        verifyAllEvents("[CATCH_PRIZE[4]]");
    }

    @Test
    public void shouldEndPrizeWorkingDontSeeEnemyUnderTree_visibility() {
        // given
        settings().integer(PRIZE_ON_FIELD, 5)
                .integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_WORKING, 2);

        catchVisibilityPrizeWithEnemyOnMap();

        assertPrize(hero(0), "[PRIZE_VISIBILITY(0/2)]");

        assertF("☼☼☼☼☼☼☼\n" +
                "☼  %% ☼\n" +
                "☼  %˄ ☼\n" +
                "☼▲ %% ☼\n" +
                "☼  %% ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(1).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼  %˄ ☼\n" +
                "☼  %% ☼\n" +
                "☼▲ %% ☼\n" +
                "☼  %% ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(1).left();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼  ˂% ☼\n" +
                "☼  %% ☼\n" +
                "☼▲ %% ☼\n" +
                "☼  %% ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // when
        hero(1).down();
        tick();

        // then
        assertF("☼☼☼☼☼☼☼\n" +
                "☼  %% ☼\n" +
                "☼  %% ☼\n" +
                "☼▲ %% ☼\n" +
                "☼  %% ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }
}