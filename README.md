GameSolver
==========

A Java program that takes in an N-player strategic form game in Gambit format and solves the game.

The program finds:

1) All strongly dominant strategies

2) All weakly dominant strategies

3) All very weakly dominant strategies

4) All pure strategy Nash equilibria


Points to note
==============

1) Strongly dominant, weakly dominant and very weakly dominant strategy equilibrium can be printed, but I haven't done that.
   
2) Computation of the pure strategy Nash equilibria will take quite some time, since I've used a brute-force approach. However, the strongly, weakly and very weakly dominant strategies don't take much time, and if you are interested only in them, please comment out the findNashEquilibria() function inside main() and the code that follows to print them.


Comments on the input format
============================

1) My code basically follows the Gambit format. The input file needs to be a .nfg file with the payoff values listed in it.

2) I assume that all the payoff values are written in one line in the file (This is important). So if you plan on using my code, please remember that your input file needs to have exactly 4 lines, as in the sample file 'random.nfg'.
   
3) Please pass the input file 'random.nfg' or your input game file in the same format as a command-line argument to the program.
