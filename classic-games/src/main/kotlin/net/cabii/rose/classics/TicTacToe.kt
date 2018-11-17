package net.cabii.rose.classics


enum class Team {
    X, O;
    operator fun not() = if (this == X) O else X
}

class Board {
    object config {
        const val numRows = 3
        const val numCols = 3
    }
    data class Position(val x: Int, val y: Int) {
        constructor(index: Int) : this(index % config.numCols, index / config.numCols)
        val listIndex get() = (y * config.numCols) + x
    }

    private val cells: MutableMap<Int, Team> = mutableMapOf()

    operator fun get(at: Position) = cells[at.listIndex]

    operator fun set(at: Position, to: Team) {
        cells[at.listIndex] = to
    }

    val usedCells: Int
        get() = cells.size
    val blankCount: Int
        get() = (config.numCols * config.numRows) - usedCells
}


interface RequestHandler {
    fun requestMove(forTeam: Team): Board.Position
}

sealed class GameOver {
    data class Winner(val team: Team): GameOver()
    object Draw : GameOver()
}

class Game(val requestHandler: RequestHandler) {
    private val board = Board()
    private var turn: Team = Team.values().random()

    fun play(): GameOver {
        var winner: GameOver?
        do {
            board[requestHandler.requestMove(turn)] = turn
            winner = getWinner()
            turn != turn
        } while ( winner == null)
        return winner
    }

    fun getWinner(): GameOver? {
        return null
    }
}
