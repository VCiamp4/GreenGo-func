package com.example.laboratorio.ui.ranking

import com.example.laboratorio.ui.ranking.RankingItem

data class RankingUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val items: List<RankingItem> = emptyList(),
    val period: RankingPeriod = RankingPeriod.GLOBAL,
    val mode: RankingMode = RankingMode.PUNTOS,
    val selectedResidue: String? = null,
    val username: String? = null,
    val miPosicionReal: Int = 0,
    val misPuntosEnRanking: Int = 0,
    val userId: Int = 0
)

