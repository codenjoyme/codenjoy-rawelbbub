## What is the game about

You have to write your own bot for the hero that beats the other bots
on points. The whole game takes place on the same field. The hero can
move around the free cells in all four directions.
The hero can also shoot a torpedo, which explodes when
hit an obstacle. The torpedo moves twice as fast as the hero.

The player's bot receives points[*] (#ask) for killing enemies. 
Penalty points[(?)](#ask) are awarded for the hero's death. 

The dead hero immediately appears in a random location on the field.

In addition to normal enemy submarines, there are prize submarines. In order to destroy
such a hero must be hit several[(?)](#ask) times. After you have killed 
prize submarine a prize will fall from it, which must be picked up. If 
do not do this, after some time [(?)](#ask) it will disappear. 
The player also receives points[(?)](#ask) for killing the prize submarine. 
Be careful, the prize can be accidentally destroyed by a torpedo. 
if this happens, it will also disappear from the field.

There are several types of prizes. Each of which temporarily gives the hero
a certain advantage for a time:

* Picked up during the game prize `PRIZE_IMMORTALITY` makes the hero 
  invulnerable to enemy torpedoes. 
* And the prize `PRIZE_WALKING_ON_FISHNET` gives the hero the ability to walk through fishing nets. 
* The prize `PRIZE_BREAKING_BAD` will allow you to pierce icebergs and impassable reefs. 
* The prize `PRIZE_VISIBILITY` gives the ability to hide equipment in the seaweed. 
* The prize `PRIZE_NO_SLIDING` gives the ability to prevent skidding while passing
  oil spill. If the action of the prize is over and the hero is 
  among the oil, it will be as if the hero just swam there.

The prize lasts for some time. Every even tick of the game the prize 'flickers' with the `PRIZE' symbol.

The points are added up. The player with the most points (before the agreed 
time).

[(?)](#ask)The exact number of points for any action as well as other 
settings at this point in the game, check with Sensei.