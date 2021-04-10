/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2020 Codenjoy
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
package game

const (
	NONE        = ' '
	BATTLE_WALL = '☼'
	BANG        = 'Ѡ'

	ICE   = '#'
	TREE  = '%'
	RIVER = '~'

	WALL = '╬'

	WALL_DESTROYED_DOWN  = '╩'
	WALL_DESTROYED_UP    = '╦'
	WALL_DESTROYED_LEFT  = '╠'
	WALL_DESTROYED_RIGHT = '╣'

	WALL_DESTROYED_DOWN_TWICE  = '╨'
	WALL_DESTROYED_UP_TWICE    = '╥'
	WALL_DESTROYED_LEFT_TWICE  = '╞'
	WALL_DESTROYED_RIGHT_TWICE = '╡'

	WALL_DESTROYED_LEFT_RIGHT = '│'
	WALL_DESTROYED_UP_DOWN    = '─'

	WALL_DESTROYED_UP_LEFT    = '┌'
	WALL_DESTROYED_RIGHT_UP   = '┐'
	WALL_DESTROYED_DOWN_LEFT  = '└'
	WALL_DESTROYED_DOWN_RIGHT = '┘'

	WALL_DESTROYED = ' '

	BULLET = '•'

	TANK_UP    = '▲'
	TANK_RIGHT = '►'
	TANK_DOWN  = '▼'
	TANK_LEFT  = '◄'

	OTHER_TANK_UP    = '˄'
	OTHER_TANK_RIGHT = '˃'
	OTHER_TANK_DOWN  = '˅'
	OTHER_TANK_LEFT  = '˂'

	AI_TANK_UP    = '?'
	AI_TANK_RIGHT = '»'
	AI_TANK_DOWN  = '¿'
	AI_TANK_LEFT  = '«'

	AI_TANK_PRIZE = '◘'

	PRIZE                  = '!'
	PRIZE_IMMORTALITY      = '1'
	PRIZE_BREAKING_WALLS   = '2'
	PRIZE_WALKING_ON_WATER = '3'
	PRIZE_VISIBILITY       = '4'
	PRIZE_NO_SLIDING       = '5'
)

type Element rune

func (e Element) getLayer() int {
	//switch e {
	//case START, EXIT, HOLE, ZOMBIE_START, GOLD, FLOOR, SPACE,
	//	ANGLE_BACK_LEFT, ANGLE_BACK_RIGHT, ANGLE_IN_LEFT, ANGLE_IN_RIGHT, ANGLE_OUT_LEFT, ANGLE_OUT_RIGHT,
	//	WALL_BACK, WALL_BACK_ANGLE_LEFT, WALL_BACK_ANGLE_RIGHT, WALL_FRONT, WALL_LEFT, WALL_RIGHT,
	//	LASER_MACHINE_CHARGING_DOWN, LASER_MACHINE_CHARGING_LEFT, LASER_MACHINE_CHARGING_RIGHT, LASER_MACHINE_CHARGING_UP,
	//	LASER_MACHINE_READY_DOWN, LASER_MACHINE_READY_LEFT, LASER_MACHINE_READY_RIGHT, LASER_MACHINE_READY_UP,
	//	DEATH_RAY_PERK, UNLIMITED_FIRE_PERK, UNSTOPPABLE_LASER_PERK,
	//	FOG:
	//	return 0
	//case ROBO, ROBO_FALLING, ROBO_LASER, ROBO_OTHER, ROBO_OTHER_FALLING, ROBO_OTHER_LASER,
	//	LASER_DOWN, LASER_LEFT, LASER_RIGHT, LASER_UP,
	//	FEMALE_ZOMBIE, MALE_ZOMBIE, ZOMBIE_DIE,
	//	EMPTY, BOX,
	//	BACKGROUND:
	//	return 1
	//case ROBO_FLYING, ROBO_OTHER_FLYING:
	//	return 2
	//}
	return 0 //prob there is no such element, at least try to find on 1st layer
}
