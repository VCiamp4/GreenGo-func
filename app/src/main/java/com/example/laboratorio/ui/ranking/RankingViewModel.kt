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

    // --- ACCIONES (Sin cambios) ---

    fun setMode(mode: RankingMode) {
        if (uiState.mode == mode) return
        // Si cambiamos a PUNTOS, limpiamos el residuo seleccionado
        val newResidue = if (mode == RankingMode.PUNTOS) null else uiState.selectedResidue
        uiState = uiState.copy(mode = mode, selectedResidue = newResidue)
        loadRanking()
    }

    fun setResiduo(residuo: String?) {
        if (uiState.selectedResidue == residuo) return
        uiState = uiState.copy(selectedResidue = residuo)
        loadRanking()
    }

    fun setPeriod(period: RankingPeriod) {
        if (uiState.period == period) return
        uiState = uiState.copy(period = period)
        // Solo recargamos si estamos en modo PUNTOS, el modo RESIDUO no usa periodo
        if (uiState.mode == RankingMode.PUNTOS) {
            loadRanking()
        }
    }

    // --- CARGA DE DATOS (LÓGICA CORREGIDA) ---

    fun loadRanking() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorMessage = null)

            try {
                // REGLA 1: Si estamos en modo RESIDUO pero no hay selección, lista vacía.
                if (uiState.mode == RankingMode.RESIDUO && uiState.selectedResidue == null) {
                    uiState = uiState.copy(isLoading = false, items = emptyList())
                    return@launch
                }

                // REGLA 2: Decidir qué endpoint llamar según el MODO
                val result = if (uiState.mode == RankingMode.RESIDUO) {
                    // --- MODO RESIDUO: Usamos el nuevo endpoint específico con @Path ---
                    // Usamos !! porque la REGLA 1 ya aseguró que no es null aquí.
                    rankingApi.getRankingPorResiduo(tipoResiduo = uiState.selectedResidue!!)

                } else {
                    // --- MODO PUNTOS: Usamos los endpoints globales/semanales ---
                    when (uiState.period) {
                        RankingPeriod.GLOBAL -> rankingApi.getRankingGlobal()
                        RankingPeriod.SEMANAL -> rankingApi.getRankingSemanal()
                    }
                }

                uiState = uiState.copy(
                    isLoading = false,
                    items = result
                )

            } catch (e: Exception) {
                e.printStackTrace()
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = "Error cargando ranking: ${e.localizedMessage}"
                )
            }
        }
    }
}