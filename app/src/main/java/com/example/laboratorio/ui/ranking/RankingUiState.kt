package com.example.laboratorio.ui.main.ranking

import com.example.laboratorio.ui.network.models.RankingEntry

data class RankingUiState(
    val isLoading: Boolean = false,
    val entries: List<RankingEntry> = emptyList(),
    val errorMessage: String? = null,
    val tipoResiduo: String? = null,
    val isSemanal: Boolean = false
)
