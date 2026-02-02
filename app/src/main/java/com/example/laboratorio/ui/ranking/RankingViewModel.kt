package com.example.laboratorio.ui.ranking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.laboratorio.ui.auth.network.RetrofitClient
import kotlinx.coroutines.launch

class RankingViewModel : ViewModel() {

    var uiState = RankingUiState()
        private set

    init {
        loadRanking()
    }

    fun setPeriod(period: RankingPeriod) {
        uiState = uiState.copy(period = period)
        loadRanking()
    }

    fun setMode(mode: RankingMode) {
        uiState = uiState.copy(mode = mode)
        loadRanking()
    }

    private fun loadRanking() {
        uiState = uiState.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            try {
                val response = when (uiState.period) {
                    RankingPeriod.GLOBAL ->
                        RetrofitClient.rankingApi.getRanking(
                            tipoResiduo = if (uiState.mode == RankingMode.RESIDUE) "Plastico" else null
                        )

                    RankingPeriod.WEEKLY ->
                        RetrofitClient.rankingApi.getWeeklyRanking(
                            tipoResiduo = if (uiState.mode == RankingMode.RESIDUE) "Plastico" else null
                        )
                }

                uiState = uiState.copy(
                    isLoading = false,
                    items = response.map {
                        RankingUser(
                            username = it.username,
                            points = it.puntos
                        )
                    }
                )

            } catch (e: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = "No se pudo cargar el ranking"
                )
            }
        }
    }
}
