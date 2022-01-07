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

import com.codenjoy.dojo.games.rawelbbub.Element;
import org.junit.Test;

import static com.codenjoy.dojo.client.Utils.split;
import static org.junit.Assert.assertEquals;

public class ElementTest {

    @Test
    public void icebergs() {
        assertEquals("[╬, ╠, ╣, ╦, ╩, ╞, ╡, ╥, ╨, │, ─, ┌, ┐, └, ┘]",
                Element.icebergs.toString());
    }

    @Test
    public void destroyIceberg() {
        assertEquals("{╬={LEFT=╠, RIGHT=╣, UP=╦, DOWN=╩}, \n" +
                        "╠={LEFT=╞, RIGHT=│, UP=┌, DOWN=└}, \n" +
                        "╣={LEFT=│, RIGHT=╡, UP=┐, DOWN=┘}, \n" +
                        "╦={LEFT=┌, RIGHT=┐, UP=╥, DOWN=─}, \n" +
                        "╩={LEFT=└, RIGHT=┘, UP=─, DOWN=╨}}",
                split(Element.destroyIceberg, "}, \n"));
    }

    @Test
    public void growIceberg() {
        assertEquals("{╬=╬, ╠=╬, ╣=╬, ╦=╬, ╩=╬, ╞=╠, ╡=╣, ╥=╣, " +
                        "╨=╩, │=╣, ─=╦, ┌=╠, ┐=╣, └=╩, ┘=╣,  =│}",
                Element.growIceberg.toString());
    }
}