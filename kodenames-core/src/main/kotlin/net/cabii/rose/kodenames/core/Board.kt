package net.cabii.rose.kodenames.core

enum class CardColor {
    BLUE, RED, BLACK, WHITE;
    val team : Team?
    get() = when(this) {
        BLUE -> Team.BLUE
        RED -> Team.RED
        else -> null
    }
}

data class Position(val x: Int, val y: Int) {
    constructor(index: Int) : this(index % 5, index / 5)

    val listIndex get() = (y * 5) + x

}

data class Card(val color: CardColor, val word: String, val position: Position) {
    var isRevealed: Boolean = false
        private set

    fun reveal(): CardColor {
        isRevealed = true
        return color
    }
}

fun Int.times(action: () -> Unit) {
    for (i in 1..this) {
        action()
    }
}

class Board(words: List<String>) {
    // blue = 9 cards, red = 8 cards, white = 7 cards, Black = 1
    private val remainingCounts: MutableMap<CardColor, Int> = mutableMapOf(
        CardColor.BLUE to 9, CardColor.RED to 8,
        CardColor.WHITE to 7, CardColor.BLACK to 1
    )
    private val cards: List<Card>

    init {
        val listOfCardColors: MutableList<CardColor> = ArrayList(25)

        remainingCounts.forEach { color, count -> count.times { listOfCardColors.add(color) } }

        listOfCardColors.shuffle()

        val listOfCards: MutableList<Card> = ArrayList(25)

        for (i in 0..24) {
            listOfCards.add(Card(listOfCardColors[i], words[i], Position(i)))
        }
        cards = listOfCards

    }

    fun reveal(position: Position): CardColor {

        val card: Card = cards[position.listIndex]
        return if (card.isRevealed) {
            card.color
        } else {
            val color: CardColor = card.reveal()
            remainingCounts.computeIfPresent(color) { _, count ->
                count - 1
            }
            card.reveal()
        }
    }

    // return revealed num of each team
    infix fun remaining(color: CardColor): Int = remainingCounts[color] ?: 0

}
