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
      - input from team 1 spy to continue
    - team 1 guesses
      - votes from rest of team 1
      - reveal top vote
      - repeat until wrong guess or out of guesses
    - team 2 spy
      - input from team 2 spy to continue
    - team 2 guesses 
      - votes from rest of team 2
      - reveal top vote
      - repeat until wrong guess or out of guesses
  - announces the winner when black card picked or all cards for a team have been revealed
