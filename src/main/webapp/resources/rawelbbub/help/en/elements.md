<meta charset="UTF-8">

## Symbol breakdown
| Sprite | Code | Description |
| -------- | -------- | -------- |
|<img src="/codenjoy-contest/resources/rawelbbub/sprite/water.png" style="height:auto;" /> | `WATER(' ')` | An empty space where hero can move. If there was an iceberg in this place before, it can grow again  | 
|<img src="/codenjoy-contest/resources/rawelbbub/sprite/reefs.png" style="height:auto;" /> | `REEFS('☼')` | Underwater reefs. They cannot be destroyed without prize PRIZE_BREAKING_BAD. | 
|<img src="/codenjoy-contest/resources/rawelbbub/sprite/explosion.png" style="height:auto;" /> | `EXPLOSION('Ѡ')` | Explosion site. It disappears in a second. | 
|<img src="/codenjoy-contest/resources/rawelbbub/sprite/oil.png" style="height:auto;" /> | `OIL('#')` | Oil leak, hitting which the hero partially loses control. During the passage, the field of view is limited and the hero will repeat the old commands for several ticks in a row, ignoring the current commands. | 
|<img src="/codenjoy-contest/resources/rawelbbub/sprite/seaweed.png" style="height:auto;" /> | `SEAWEED('%')` | Seaweed hide heroes which can continue to shoot at the same time. The fired shells are also not visible under the weed. Only prizes can be seen from behind seaweed. | 
|<img src="/codenjoy-contest/resources/rawelbbub/sprite/fishnet.png" style="height:auto;" /> | `FISHNET('~')` | Fishnet does not allow to pass through itself without the PRIZE_WALKING_ON_FISHNET prize, but the shells fly freely through the water. Hero stuck in the middle of the fishnet, after canceling the PRIZE_WALKING_ON_FISHNET prize, can move 1 cell in the fishnet only every N ticks. | 
|<img src="/codenjoy-contest/resources/rawelbbub/sprite/iceberg_huge.png" style="height:auto;" /> | `ICEBERG_HUGE('╬')` | An iceberg that hasn't been shot yet. It takes 3 shots to completely destroy. | 
|<img src="/codenjoy-contest/resources/rawelbbub/sprite/iceberg_medium_left.png" style="height:auto;" /> | `ICEBERG_MEDIUM_LEFT('╠')` | Partially destroyed iceberg. For complete destruction, 2 shot is required. | 
|<img src="/codenjoy-contest/resources/rawelbbub/sprite/iceberg_medium_right.png" style="height:auto;" /> | `ICEBERG_MEDIUM_RIGHT('╣')` | Partially destroyed iceberg. For complete destruction, 2 shot is required. | 
|<img src="/codenjoy-contest/resources/rawelbbub/sprite/iceberg_medium_up.png" style="height:auto;" /> | `ICEBERG_MEDIUM_UP('╦')` | Partially destroyed iceberg. For complete destruction, 2 shot is required. | 
|<img src="/codenjoy-contest/resources/rawelbbub/sprite/iceberg_medium_down.png" style="height:auto;" /> | `ICEBERG_MEDIUM_DOWN('╩')` | Partially destroyed iceberg. For complete destruction, 2 shot is required. | 
|<img src="/codenjoy-contest/resources/rawelbbub/sprite/iceberg_small_left_left.png" style="height:auto;" /> | `ICEBERG_SMALL_LEFT_LEFT('╞')` | Almost destroyed iceberg. For complete destruction, 1 shot is required. | 
|<img src="/codenjoy-contest/resources/rawelbbub/sprite/iceberg_small_right_right.png" style="height:auto;" /> | `ICEBERG_SMALL_RIGHT_RIGHT('╡')` | Almost destroyed iceberg. For complete destruction, 1 shot is required. | 
|<img src="/codenjoy-contest/resources/rawelbbub/sprite/iceberg_small_up_up.png" style="height:auto;" /> | `ICEBERG_SMALL_UP_UP('╥')` | Almost destroyed iceberg. For complete destruction, 1 shot is required. | 
|<img src="/codenjoy-contest/resources/rawelbbub/sprite/iceberg_small_down_down.png" style="height:auto;" /> | `ICEBERG_SMALL_DOWN_DOWN('╨')` | Almost destroyed iceberg. For complete destruction, 1 shot is required. | 
|<img src="/codenjoy-contest/resources/rawelbbub/sprite/iceberg_small_left_right.png" style="height:auto;" /> | `ICEBERG_SMALL_LEFT_RIGHT('│')` | Almost destroyed iceberg. For complete destruction, 1 shot is required. | 
|<img src="/codenjoy-contest/resources/rawelbbub/sprite/iceberg_small_up_down.png" style="height:auto;" /> | `ICEBERG_SMALL_UP_DOWN('─')` | Almost destroyed iceberg. For complete destruction, 1 shot is required. | 
|<img src="/codenjoy-contest/resources/rawelbbub/sprite/iceberg_small_up_left.png" style="height:auto;" /> | `ICEBERG_SMALL_UP_LEFT('┌')` | Almost destroyed iceberg. For complete destruction, 1 shot is required. | 
|<img src="/codenjoy-contest/resources/rawelbbub/sprite/iceberg_small_up_right.png" style="height:auto;" /> | `ICEBERG_SMALL_UP_RIGHT('┐')` | Almost destroyed iceberg. For complete destruction, 1 shot is required. | 
|<img src="/codenjoy-contest/resources/rawelbbub/sprite/iceberg_small_down_left.png" style="height:auto;" /> | `ICEBERG_SMALL_DOWN_LEFT('└')` | Almost destroyed iceberg. For complete destruction, 1 shot is required. | 
|<img src="/codenjoy-contest/resources/rawelbbub/sprite/iceberg_small_down_right.png" style="height:auto;" /> | `ICEBERG_SMALL_DOWN_RIGHT('┘')` | Almost destroyed iceberg. For complete destruction, 1 shot is required. | 
|<img src="/codenjoy-contest/resources/rawelbbub/sprite/torpedo_left.png" style="height:auto;" /> | `TORPEDO_LEFT('•')` | Torpedo - is a self-propelled underwater missile designed to be fired from a submarine and to explode on reaching a target. The target can be an iceberg, another submarine and other elements under water. This torpedo moves to the left. | 
|<img src="/codenjoy-contest/resources/rawelbbub/sprite/torpedo_right.png" style="height:auto;" /> | `TORPEDO_RIGHT('¤')` | This torpedo moves to the right. | 
|<img src="/codenjoy-contest/resources/rawelbbub/sprite/torpedo_up.png" style="height:auto;" /> | `TORPEDO_UP('ø')` | This torpedo moves to the up. | 
|<img src="/codenjoy-contest/resources/rawelbbub/sprite/torpedo_down.png" style="height:auto;" /> | `TORPEDO_DOWN('×')` | This torpedo moves to the down. | 
|<img src="/codenjoy-contest/resources/rawelbbub/sprite/hero_left.png" style="height:auto;" /> | `HERO_LEFT('◄')` | Your hero is pointing left. | 
|<img src="/codenjoy-contest/resources/rawelbbub/sprite/hero_right.png" style="height:auto;" /> | `HERO_RIGHT('►')` | Your hero is pointing right. | 
|<img src="/codenjoy-contest/resources/rawelbbub/sprite/hero_up.png" style="height:auto;" /> | `HERO_UP('▲')` | Your hero is pointing up. | 
|<img src="/codenjoy-contest/resources/rawelbbub/sprite/hero_down.png" style="height:auto;" /> | `HERO_DOWN('▼')` | Your hero is pointing down. | 
|<img src="/codenjoy-contest/resources/rawelbbub/sprite/other_hero_left.png" style="height:auto;" /> | `OTHER_HERO_LEFT('˂')` | Other hero is pointing left. | 
|<img src="/codenjoy-contest/resources/rawelbbub/sprite/other_hero_right.png" style="height:auto;" /> | `OTHER_HERO_RIGHT('˃')` | Other hero is pointing right. | 
|<img src="/codenjoy-contest/resources/rawelbbub/sprite/other_hero_up.png" style="height:auto;" /> | `OTHER_HERO_UP('˄')` | Other hero is pointing up. | 
|<img src="/codenjoy-contest/resources/rawelbbub/sprite/other_hero_down.png" style="height:auto;" /> | `OTHER_HERO_DOWN('˅')` | Other hero is pointing down. | 
|<img src="/codenjoy-contest/resources/rawelbbub/sprite/enemy_hero_left.png" style="height:auto;" /> | `ENEMY_HERO_LEFT('Ð')` | Enemy hero is pointing left. | 
|<img src="/codenjoy-contest/resources/rawelbbub/sprite/enemy_hero_right.png" style="height:auto;" /> | `ENEMY_HERO_RIGHT('£')` | Enemy hero is pointing right. | 
|<img src="/codenjoy-contest/resources/rawelbbub/sprite/enemy_hero_up.png" style="height:auto;" /> | `ENEMY_HERO_UP('Ô')` | Enemy hero is pointing up. | 
|<img src="/codenjoy-contest/resources/rawelbbub/sprite/enemy_hero_down.png" style="height:auto;" /> | `ENEMY_HERO_DOWN('Ç')` | Enemy hero is pointing down. | 
|<img src="/codenjoy-contest/resources/rawelbbub/sprite/ai_left.png" style="height:auto;" /> | `AI_LEFT('«')` | AI is pointing left. | 
|<img src="/codenjoy-contest/resources/rawelbbub/sprite/ai_right.png" style="height:auto;" /> | `AI_RIGHT('»')` | AI is pointing right. | 
|<img src="/codenjoy-contest/resources/rawelbbub/sprite/ai_up.png" style="height:auto;" /> | `AI_UP('?')` | AI is pointing up. | 
|<img src="/codenjoy-contest/resources/rawelbbub/sprite/ai_down.png" style="height:auto;" /> | `AI_DOWN('¿')` | AI is pointing down. | 
|<img src="/codenjoy-contest/resources/rawelbbub/sprite/ai_prize_left.png" style="height:auto;" /> | `AI_PRIZE_LEFT('{')` | AI with prize is pointing left. | 
|<img src="/codenjoy-contest/resources/rawelbbub/sprite/ai_prize_right.png" style="height:auto;" /> | `AI_PRIZE_RIGHT('}')` | AI with prize is pointing right. | 
|<img src="/codenjoy-contest/resources/rawelbbub/sprite/ai_prize_up.png" style="height:auto;" /> | `AI_PRIZE_UP('î')` | AI with prize is pointing up. | 
|<img src="/codenjoy-contest/resources/rawelbbub/sprite/ai_prize_down.png" style="height:auto;" /> | `AI_PRIZE_DOWN('w')` | AI with prize is pointing down. | 
|<img src="/codenjoy-contest/resources/rawelbbub/sprite/torpedo_side_left.png" style="height:auto;" /> | `TORPEDO_SIDE_LEFT('t')` | Turn based mode. This torpedo moves to the left. | 
|<img src="/codenjoy-contest/resources/rawelbbub/sprite/torpedo_side_right.png" style="height:auto;" /> | `TORPEDO_SIDE_RIGHT('T')` | Turn based mode. This torpedo moves to the right. | 
|<img src="/codenjoy-contest/resources/rawelbbub/sprite/hero_side_left.png" style="height:auto;" /> | `HERO_SIDE_LEFT('h')` | Turn based mode. Your hero is pointing left. | 
|<img src="/codenjoy-contest/resources/rawelbbub/sprite/hero_side_right.png" style="height:auto;" /> | `HERO_SIDE_RIGHT('H')` | Turn based mode. Your hero is pointing right. | 
|<img src="/codenjoy-contest/resources/rawelbbub/sprite/other_hero_side_left.png" style="height:auto;" /> | `OTHER_HERO_SIDE_LEFT('o')` | Turn based mode. Other hero is pointing left. | 
|<img src="/codenjoy-contest/resources/rawelbbub/sprite/other_hero_side_right.png" style="height:auto;" /> | `OTHER_HERO_SIDE_RIGHT('O')` | Turn based mode. Other hero is pointing right. | 
|<img src="/codenjoy-contest/resources/rawelbbub/sprite/enemy_hero_side_left.png" style="height:auto;" /> | `ENEMY_HERO_SIDE_LEFT('e')` | Turn based mode. Enemy hero is pointing left. | 
|<img src="/codenjoy-contest/resources/rawelbbub/sprite/enemy_hero_side_right.png" style="height:auto;" /> | `ENEMY_HERO_SIDE_RIGHT('E')` | Turn based mode. Enemy hero is pointing right. | 
|<img src="/codenjoy-contest/resources/rawelbbub/sprite/ai_side_left.png" style="height:auto;" /> | `AI_SIDE_LEFT('a')` | Turn based mode. AI is pointing left. | 
|<img src="/codenjoy-contest/resources/rawelbbub/sprite/ai_side_right.png" style="height:auto;" /> | `AI_SIDE_RIGHT('A')` | Turn based mode. AI is pointing right. | 
|<img src="/codenjoy-contest/resources/rawelbbub/sprite/ai_prize_side_left.png" style="height:auto;" /> | `AI_PRIZE_SIDE_LEFT('p')` | Turn based mode. AI with prize is pointing left. | 
|<img src="/codenjoy-contest/resources/rawelbbub/sprite/ai_prize_side_right.png" style="height:auto;" /> | `AI_PRIZE_SIDE_RIGHT('P')` | Turn based mode. AI with prize is pointing right. | 
|<img src="/codenjoy-contest/resources/rawelbbub/sprite/prize.png" style="height:auto;" /> | `PRIZE('!')` | The dropped prize after the destruction of the prize AI flickers on the field every even tick of the game with this sprite. | 
|<img src="/codenjoy-contest/resources/rawelbbub/sprite/prize_immortality.png" style="height:auto;" /> | `PRIZE_IMMORTALITY('1')` | A prize that gives the hero temporary invulnerability. | 
|<img src="/codenjoy-contest/resources/rawelbbub/sprite/prize_breaking_bad.png" style="height:auto;" /> | `PRIZE_BREAKING_BAD('2')` | A prize that allows you to temporarily destroy any icebergs and underwater reefs (but not the border of the field) with 1 shot. | 
|<img src="/codenjoy-contest/resources/rawelbbub/sprite/prize_walking_on_fishnet.png" style="height:auto;" /> | `PRIZE_WALKING_ON_FISHNET('3')` | A prize that allows the hero to temporarily walk on fishnet. | 
|<img src="/codenjoy-contest/resources/rawelbbub/sprite/prize_visibility.png" style="height:auto;" /> | `PRIZE_VISIBILITY('4')` | A prize that allows the hero to temporarily see all enemies and their bullets under the seaweed. | 
|<img src="/codenjoy-contest/resources/rawelbbub/sprite/prize_no_sliding.png" style="height:auto;" /> | `PRIZE_NO_SLIDING('5')` | A prize that allows the hero to temporarily not slide on the ice. | 
