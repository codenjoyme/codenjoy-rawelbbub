## В чем суть игры?

Надо написать своего бота для героя, который обыграет других ботов
по очкам. Вся игра при этом проходит на одном поле. Герой может
передвигаться по свободным ячейкам во все четыре стороны.
Так же герой может выстрелить торпедой, которая взорвется при
попадании в препятствие. Торпеда движется быстрее героя в два раза.

За убийство врагов боту игрока начисляются очки[*](#ask). 
За смерть геройа начисляются штрафные очки[*](#ask). 

Мертвый герой тут же появляется в random месте на поле.

Помимо обычных вражеских субмарин есть субмарины с призами. Чтобы уничтожить
такого героя надо попасть несколько[*](#ask) раз. После убийства 
призовую субмарину из него выпадает приз, который нужно подобрать. Если 
этого не сделать, то через некоторое время[*](#ask) он исчезнет. 
За убийство призовой субмарины игроку также начисляются очки[*](#ask). 
Стоит быть внимательным, приз случайно можно уничтожить торпедой - 
если это случится, он так же исчезнет с поля.

Есть несколько видов призов. Каждый из которых на время дает герою
определенное преимущество:

* Подобранный во время игры приз `PRIZE_IMMORTALITY` делает героя 
  неуязвимым к вражеским торпедам. 
* А приз `PRIZE_WALKING_ON_FISHNET` даст возможность ходить через рыболовецкие сети. 
* Приз `PRIZE_BREAKING_BAD` позволит пробивать айсберги и непроходимые рифы. 
* Приз `PRIZE_VISIBILITY` дает возможность прятать технику в водорослях. 
* Приз `PRIZE_NO_SLIDING` дает возможность предотвратить занос во время прохождения
  разлива нефти. Если действие приза окончено, а герой находится 
  среди нефти, то всё будет так как будто герой только что туда заплыл.

Приз действует некоторое время. Каждый четный тик игры приз 'мерцает' символом `PRIZE`.

Очки суммируются. Побеждает игрок с большим числом очков (до условленного 
времени).

[*](#ask)Точное количество очков за любое действие, а так же другие 
настройки на данный момент игры уточни у Сенсея.