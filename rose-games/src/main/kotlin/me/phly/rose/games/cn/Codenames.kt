package me.phly.rose.games.cn

import java.util.*
import java.util.stream.Collectors

class Codenames

fun main(args: Array<String>) {
    println("Hello world!")
    println(Board())
}

enum class TileType {
    ASSASSIN, BYSTANDER, RED, BLUE
}

private val random = Random()


class Board(rows: Int = 5, cols: Int = 5) {
    var isRedTurn: Boolean
    val totalTypeCounts: Map<TileType, Int>
    var remainingTypeCounts: MutableMap<TileType, Int>
    val allTiles: Map<String, Tile>
    val grid: Grid

    init {
        if (rows < 3 || rows > 7)
            throw IllegalArgumentException("Rows must be between 3 and 7. Given: " + rows)
        if (cols < 3 || cols > 7)
            throw IllegalArgumentException("Cols must be between 3 and 7. Given: " + cols)

        isRedTurn = random.nextBoolean()
        grid = Grid(isRedTurn, rows, cols)


        grid = Array(rows, { row -> Array(cols, { col -> allTiles[col + row * cols] }) })
    }

    override fun toString(): String {
        val sb = StringBuilder()
        sb.appendln("Type Distribution: $totalTypeCounts")
        sb.appendln("Is Red Turn: $isRedTurn")
        sb.appendln("Grid: ")

        //find widest word
        val maxLen = allTiles.map { it.text.length }.max()!!
        val charsPerRow = (maxLen + 3) * cols + 1
        val divider = "-".repeat(charsPerRow)
        sb.appendln(divider)
        for (i in 0 until rows) {
            for (j in 0 until cols) {
                sb.append("| ").append(grid[i][j].text.padEnd(maxLen)).append(' ')
            }
            sb.appendln('|').appendln(divider)
        }
        return sb.toString()
    }

}

class Grid(isRedTurn: Boolean, val rows: Int = 5, val cols: Int = 5) {

    val as2d: Array<Array<Tile>>

    init {
        val totalWords = rows * cols
        val numAssassins = if (totalWords > 25) 2 else 1
        val wordsToSplit = totalWords - numAssassins - 1 //1 starting team
        val bystanderExtra = if (wordsToSplit % 3 == 1) 1 else 0
        val teamExtra = if (wordsToSplit % 3 == 2) 1 else 0

        totalTypeCounts = mapOf(
                TileType.ASSASSIN to numAssassins,
                TileType.BYSTANDER to wordsToSplit / 3 + bystanderExtra,
                TileType.RED to wordsToSplit / 3 + teamExtra + if (isRedTurn) 1 else 0,
                TileType.BLUE to wordsToSplit / 3 + teamExtra + if (isRedTurn) 0 else 1
        )
        assert(totalTypeCounts.values.sum() == totalWords)
        println(totalTypeCounts)

        remainingTypeCounts = EnumMap(totalTypeCounts)

        allTiles = Dictionary.getTiles(totalTypeCounts).associateBy { it.text }

    }

}

private object Dictionary {
    private val words: List<String> by lazy {
        Thread.currentThread().contextClassLoader
                .getResourceAsStream("codenames/words.txt").bufferedReader()
                .lines().collect(Collectors.toList())
    }
    fun getRandomWord() = words[random.nextInt(words.size)]

    fun getTiles(typeCounts: Map<TileType, Int>): List<Tile> {
        val used = mutableSetOf<String>()
        val tiles = mutableListOf<Tile>()
        for ((type, count) in typeCounts) {
            for (i in 1..count) {
                var word: String
                do {
                    word = Dictionary.getRandomWord()
                } while (used.contains(word))
                used.add(word)
                tiles.add(Tile(word, type))
            }
        }
        tiles.shuffle(random)
        return tiles
    }
}

data class Tile(val text: String, val type: TileType) {
    var isRevealed = false
}
