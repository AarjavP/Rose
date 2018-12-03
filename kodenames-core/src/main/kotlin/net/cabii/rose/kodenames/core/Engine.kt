package net.cabii.rose.kodenames.core

import java.lang.IllegalStateException
import java.util.stream.Collectors

interface UserInputs {
    fun getSpyInput(team : Team) : SpyInput
    fun getGuessInput(team : Team, clue: String, remainingGuesses: Int ) : GuessInput
}

data class SpyInput(val clue: String, val guesses: Int)
data class GuessInput(val position: Position)

class Engine(val userInputs: UserInputs) {

    val board: Board = Board( initWordListForBoard().toList() )

    private val words: List<String> by lazy {
        Thread.currentThread().contextClassLoader
            .getResourceAsStream("words.txt").bufferedReader()
            .lines().collect(Collectors.toList())
    }

    fun initWordListForBoard(): Set<String> {
        val ret = mutableSetOf<String>()
        while (ret.size < 25) {
            ret += words.random()
        }
        return ret
    }

    fun play() : Team {
        var next : Next = Next.NextTurn{ spyTurn( Team.BLUE ) }
        while( next is Next.NextTurn ) {
            next = next.turn() 
        }
        if( next is Next.GameOver ) return next.winner
        else throw IllegalStateException()
    }

    private fun spyTurn(team : Team) : Next.NextTurn {
        val spyInput : SpyInput = userInputs.getSpyInput(team)
        return Next.NextTurn { guessTurn(team, spyInput.clue, spyInput.guesses + 1)  }
    }

    private fun guessTurn(team : Team, clue: String, remainingGuesses: Int) : Next {
        val guessInput : GuessInput = userInputs.getGuessInput(team, clue, remainingGuesses)
        val revealedCardColor = board.reveal( guessInput.position )
        when ( revealedCardColor ) {
            CardColor.BLACK -> return Next.GameOver( !team )
            CardColor.WHITE -> return Next.NextTurn{ spyTurn( !team ) }
            else -> {
                if( board.remaining( revealedCardColor ) == 0 )
                    return Next.GameOver( revealedCardColor.team?: throw IllegalStateException() )
                if( remainingGuesses > 1 && revealedCardColor.team == team )
                    return   Next.NextTurn{ guessTurn( team, clue, remainingGuesses-1) }
                return Next.NextTurn{ spyTurn( !team ) }
            }
        }
    }
}

//enum class Turn {
//    BLUE_GUESS, RED_GUESS, BLUE_SPY, RED_SPY
//}

sealed class Next {
    data class NextTurn(val turn : () -> Next ) : Next()
    data class GameOver(val winner : Team) : Next()


}

