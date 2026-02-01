package com.example.laboratorio.ui.main.ranking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.laboratorio.ui.auth.network.RetrofitClient
import kotlinx.coroutines.launch

class RankingViewModel : ViewModel() {

    var uiState = androidx.compose.runtime.mutableStateOf(RankingUiState())
        private set

    fun loadRanking(
        semanal: Boolean,
        tipoResiduo: String? = null
    ) {
        uiState.value = uiState.value.copy(
            isLoading = true,
            errorMessage = null,
            isSemanal = semanal,
            tipoResiduo = tipoResiduo
        )

        viewModelScope.launch {
            try {
                val result = if (semanal) {
                    RetrofitClient.rankingApi.getRankingSemanal(tipoResiduo)
                } else {
                    RetrofitClient.rankingApi.getRankingGlobal(tipoResiduo)
                }

                uiState.value = uiState.value.copy(
                    isLoading = false,
                    entries = result
                )

            } catch (e: Exception) {
                uiState.value = uiState.value.copy(
                    isLoading = false,
                    errorMessage = "No se pudo cargar el ranking"
                )
            }
        }
    }
}
