package com.example.laboratorio.ui.ranking

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.laboratorio.ui.auth.network.RetrofitClient
import com.example.laboratorio.ui.auth.network.RetrofitClient.rankingApi
import kotlinx.coroutines.launch
import com.example.laboratorio.ui.network.RankingApiService


class RankingViewModel(
    private val rankingApi: RankingApiService = RetrofitClient.rankingApi
) : ViewModel() {

    var uiState by mutableStateOf(RankingUiState())
        private set


    fun setMode(mode: RankingMode) {
        if (uiState.mode == mode) return
        uiState = uiState.copy(mode = mode)
    }

    fun setResiduo(residuo: String?) {
        uiState = uiState.copy(selectedResidue = residuo)
    }


    fun setPeriod(period: RankingPeriod) {
        uiState = uiState.copy(period = period)
    }


    fun loadRanking() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorMessage = null)

            try {
                val result = when (uiState.period) {
                    RankingPeriod.GLOBAL ->
                        rankingApi.getRankingGlobal(
                            tipoResiduo = if (uiState.mode == RankingMode.RESIDUO)
                                uiState.selectedResidue
                            else null
                        )

                    RankingPeriod.SEMANAL ->
                        rankingApi.getRankingSemanal(
                            tipoResiduo = if (uiState.mode == RankingMode.RESIDUO)
                                uiState.selectedResidue
                            else null
                        )
                }

                // 🔥 CLAVE: nueva referencia
                uiState = uiState.copy(
                    isLoading = false,
                    items = result
                )

            } catch (e: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = "Error cargando ranking"
                )
            }
        }
    }



}

