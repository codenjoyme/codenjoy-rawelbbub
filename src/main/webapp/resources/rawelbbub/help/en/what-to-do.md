## How to play?

The game is turn-based, every second the server sends your client
the state of the updated field for the current moment and waits for a response
command to the hero. In the next second, the player has to give
command to the hero. If he doesn't, the hero stands still.

Your goal is to make the hero move according to your algorithm.
The hero on the field must be able to score as many points as he can.
The main goal of the game is to beat all opponents by points.

## Control Commands

There are several commands and they depend on the display/control format 
of the game, as chosen by Sensei at the time of the game:

* In the classic overhead view mode, the move command does 
  exactly what it says:  

  + `UP`, `DOWN`, `LEFT`, `RIGHT` - lead to a simultaneous turn and
    move the hero in a given direction by 1 cell.
  + `ACT` - torpedo shot. 
  + Movement commands can be combined with the shot commands, 
    separated by a comma - which means that in one tick of the game 
    there will be a shot and then movement `LEFT,ACT` or vice versa `ACT,LEFT`.
  
* In the game mode "TURN/FORWARD" the view on the field is the same from above, but the control of the hero
  is slightly different and is divided into turn commands and movement commands: 

  + `UP` - "full forward" causes the hero to move one cell 
    forward in the direction of movement.
  + `DOWN` - "reverse" causes the hero to move one cell 
    to the opposite side of the direction of movement.
  + `LEFT` - "turn left" causes the hero to turn clockwise 
    clockwise by 90 degrees.
  + `RIGHT` - "turn to the right" causes the hero to turn counter 
    clockwise by 90 degrees.

* In the "Side View" game mode, the view on the field, oddly enough, is from the side, and controlling the
  the hero is possible in the format of dive / surfacing and movement to the left / right:  

  + `UP` - "surfacing" causes the hero to move one cell up
    without changing his original direction.
  + `DOWN` - "dive" moves the hero one cell down
    without changing his original direction.
  + `LEFT` - causes the hero to turn and move 1 cell to the left.
  + `RIGHT` - causes the hero to turn and move 1 cell to the right.
  
## Settings

The settings will change[*](#ask) as the game progresses.

## Cases

## Hints

The first task is to write a websocket client that will connect
to the server. Then get the hero to obey commands. In this way,
the player will be prepared for tomorrow's game. The second task is to play
a meaningful game and win.

If you don't know where to start, try implementing the following algorithms:

* Move to a random empty adjacent cell.
* Move forward into an empty cell in the direction of the nearest prize.
* Try to hide from torpedoes.
* Try to avoid enemy submarines and other heroes.
* Try to shoot at other heroes.