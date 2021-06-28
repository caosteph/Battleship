# battleship
Two-player Battleship game written in Java that can restore state of game and keeps track of top three highest scores. Submitted for final project in CIS 120 (Programming I) in December 2020. This game features implementation of four core concepts taught in the class: 2d-arrays, collections, I/O, and testing. Further explanation of how these concepts are implemented into the game, as well as the design, can be found in CoreConceptsAndDesign.txt. 

Demo available at: https://youtu.be/LUl73yfx5vk

GAME OBJECTIVE:
The object of Battleship is to try and sink all of the other player's ships before they sink all of your ships. All of the other player's ships are somewhere on his/her board. You try and hit them by calling out the coordinates of one of the squares on the board. The other player also tries to hit your ships by calling out coordinates. Neither you nor the other player can see the other's board so you must try to guess where they are. Each board in the physical game has two grids: the lower (horizontal) section for the player's shipsand the upper part (vertical during play) for recording the player's guesses.\n"

STARTING A NEW GAME:
- Each player places the 5 ships somewhere on their board. 
- The ships can only be placed vertically or horizontally. 
- Diagonal placement is not allowed. 
- No part of a ship may hang off the edge of the board. 
- Ships may not overlap each other. 
- No ships may be placed on another ship.
- No ships can be placed within one tile of each other.
- Once the guessing begins, the players may not move the ships.
- The 5 ships are:  Carrier (occupies 5 spaces), Battleship (4 spaces), Cruiser (3), Submarine (3), and Destroyer (2).

PLAYING THE GAME:
- Player's take turns guessing by selecting coordinates to fire on.
- The board will be marked with dots:  red for hit, grey for miss.
- When all of the squares that one your ships occupies have been hit, the ship will be sunk.
- You can keep track of which ships have been sunk at the side. 
- As soon as all of one player's ships have been sunk, the game ends.
