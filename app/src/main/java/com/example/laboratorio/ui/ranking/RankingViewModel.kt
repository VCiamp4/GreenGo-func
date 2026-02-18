package com.example.laboratorio.ui.ranking

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.laboratorio.ui.auth.network.RetrofitClient
import com.example.laboratorio.ui.network.RankingApiService
import kotlinx.coroutines.launch

// NOTA: Borramos las definiciones de data class y enums de aquí
// porque ya las tienes en RankingUiState.kt, RankingEnums.kt, etc.

class RankingViewModel(
    private val rankingApi: RankingApiService = RetrofitClient.rankingApi
) : ViewModel() {

    var uiState by mutableStateOf(RankingUiState())
        private set

    // --- BLOQUE INIT: CARGA AUTOMÁTICA AL INICIAR ---
    init {
        loadRanking()
    }

    // --- ACCIONES ---

    fun setMode(mode: RankingMode) {
        if (uiState.mode == mode) return

        // Si cambiamos a RESIDUO, seleccionamos uno por defecto ("PLASTICO") para que no quede vacío.
        val newResidue = if (mode == RankingMode.RESIDUO) "PLASTICO" else null

        uiState = uiState.copy(
            mode = mode,
            selectedResidue = newResidue,
            errorMessage = null // Limpiamos errores previos
        )

        loadRanking()
    }

    fun setResiduo(residuo: String) {
        if (uiState.selectedResidue == residuo) return
        uiState = uiState.copy(selectedResidue = residuo)
        loadRanking()
    }

    fun setPeriod(period: RankingPeriod) {
        if (uiState.period == period) return
        uiState = uiState.copy(period = period)

        // Solo recargamos si estamos en modo PUNTOS
        if (uiState.mode == RankingMode.PUNTOS) {
            loadRanking()
        }
    }

    // --- CARGA DE DATOS ---

    private fun loadRanking() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorMessage = null)

            try {
                // REGLA 1: Validación de seguridad
                if (uiState.mode == RankingMode.RESIDUO && uiState.selectedResidue == null) {
                    uiState = uiState.copy(isLoading = false, items = emptyList())
                    return@launch
                }

                // REGLA 2: Llamada a la API según modo
                val result = if (uiState.mode == RankingMode.RESIDUO) {
                    // MODO RESIDUO
                    rankingApi.getRankingPorResiduo(tipoResiduo = uiState.selectedResidue!!)
                } else {
                    // MODO PUNTOS
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