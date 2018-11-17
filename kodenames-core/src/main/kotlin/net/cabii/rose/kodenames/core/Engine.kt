package net.cabii.rose.kodenames.core

import java.util.stream.Collectors

class Engine {

    var turn: Turn = Turn.BLUE_SPY

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


}

enum class Turn {
    BLUE_GUESS, RED_GUESS, BLUE_SPY, RED_SPY
}
