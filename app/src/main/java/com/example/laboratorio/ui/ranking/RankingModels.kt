package com.example.laboratorio.ui.ranking

enum class RankingPeriod(val label: String) {
    GLOBAL("Global"),
    WEEKLY("Semanal")
}

enum class RankingMode {
    POINTS,
    RESIDUE
}

data class RankingUser(
    val username: String,
    val points: Int
)
