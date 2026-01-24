package com.example.laboratorio.ui.ranking

enum class RankingPeriod { GLOBAL, WEEKLY }
enum class RankingMode { TOTAL_POINTS, BY_RESIDUE }

data class RankingUiState(
    val isLoading: Boolean = false,
    val period: RankingPeriod = RankingPeriod.GLOBAL,
    val mode: RankingMode = RankingMode.TOTAL_POINTS,
    val selectedResidue: String = "Plástico",
    val ranking: List<RankingEntry> = emptyList(),
    val errorMessage: String? = null
)
