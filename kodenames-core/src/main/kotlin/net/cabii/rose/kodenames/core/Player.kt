package net.cabii.rose.kodenames.core

enum class Team{
    BLUE, RED;
    operator fun not() = if (this == BLUE) RED else BLUE
}

// data -> makes toString (values with variable), and toEquals
data class Player(val team: Team, val isSpyMaster: Boolean, val userId: String)
