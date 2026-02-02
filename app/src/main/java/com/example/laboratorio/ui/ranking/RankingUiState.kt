package com.example.laboratorio.ui.ranking

data class RankingUiState(
    val period: RankingPeriod = RankingPeriod.GLOBAL,
    val mode: RankingMode = RankingMode.POINTS,

    val isLoading: Boolean = false,
    val errorMessage: String? = null,

    val items: List<RankingUser> = emptyList()
)


