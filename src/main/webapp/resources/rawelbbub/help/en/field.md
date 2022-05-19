## Field example

Here is an example of a line from the server:

<pre>board=☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼ ☼☼ ╬ ╬ ╬ ╬ ╬ ╬ ╬ ╬ ╬ ╬ ╬ ╬ ☼☼ ╬ ╬ ╬ ╬ ╬ ╬ ╬ ╬ ╬ ╬˅╬ ☼☼ ╬ ╬˂╬ ╬ ╬ ╬ ╬ _COPY2 ╬ ╬ ╬ ╬ ╬-╬ ☼☼ ╬ ╬ ╬ ╬ ╬ ╬ ☼☼ ╬ ╬ ╠ ╬ ╬ ╬ ☼☼ ╬ ╬ ╬ ╬ ╬ ╬ ╬ ☼☼ ╬ ╬ ╬ ╬ ╬ ╬ ☼☼ ╬ ╬ ☼☼ ╬▲╬ ╬ ╬ ╬ ☼☼ ☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼</pre>

The length of the string is equal to the area of the field `N*N`. If you insert a character
every `N=sqrt(length(string))` characters, then
you get a readable image of the field:


<pre>☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼
☼ ☼
☼ ╬ ╬ ╬ ╬ ╬ ╬ ☼
☼ ╬ ╬ ╬ ╬ ╬ ╬ ☼
☼ ╬ ╬ ╬ ╬ ╬ ╬ ☼
☼ ╬ ╬ ╬ ╬ ╬˅╬ ☼
☼ ╬ ╬˂╬ ╬ ╬ ╬ ☼
☼ ╬ ╬ ╬ ╬ ╬-╬ ☼
☼ ╬ ╬ ╬ ╬ ╬ ╬ ☼
☼ ╬ ╬ ╠ ╬ ╬ ╬ ☼
☼ ╬ ╬ ╬ ╬ ╬ ╬ ☼
☼ ╬ ╬ ╬ ╬ ╬ ╬ ☼
☼ ╬▲╬ ╬ ╬ ╬ ╬ ☼
☼ ☼
☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼</pre>


The first character of the line corresponds to the cell located in the
The coordinate is `[0, 29]`.
The coordinate `[0, 0]` corresponds to the lower left corner.
In this example the hero's position (symbol `▲`) is `[3, 2]`, 
and the torpedo (symbol `-`) is `[11, 7]`.

What this field looks like in real life: