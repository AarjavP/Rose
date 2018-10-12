### kodenames design

##### Objects needed

- Card
  - color [blue, red, Black, White]
  - word (Unique Id to identify)
  - isRevealed
  - position on the grid (x,y)

- Board
  - 25 Cards
  - Populates with words and position
  - Counts of Unrevealed cards by color 
    - which Cards marked
  - reveal certain position 
    
- Player 
   - Team [Blue, Red]
   - isSpyMaster
   - UserID

-Engine
  - Initialize board (pass in the words)
  - keeps track of turn
    - team 1 spy
    - team 1 guesses
    - team 2 spy
    - team 2 guesses 
  - gets card to reveal from board 
   
  - announces the winner 
