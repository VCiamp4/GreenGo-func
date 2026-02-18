package com.example.laboratorio.ui.ranking

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.laboratorio.ui.auth.network.RetrofitClient
import com.example.laboratorio.ui.auth.network.TokenStore
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
        val id = TokenStore.getUserId()
        uiState = uiState.copy(userId = id)
        loadRanking()
    }

    // --- ACCIONES ---

    fun setMode(mode: RankingMode) {
        if (uiState.mode == mode) return

        // Si cambiamos a RESIDUO, seleccionamos uno por defecto ("PLASTICO") para que no quede vacío.
        val newResidue = if (mode == RankingMode.RESIDUO) "Plastico" else null

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

    fun loadRanking() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorMessage = null)
            try {
                val tipo = if (uiState.mode == RankingMode.RESIDUO) uiState.selectedResidue else null

                val userResponse = RetrofitClient.authApi.datosUsuario()
                val realId = userResponse.id

                // 1. Cargamos la lista (Top 10) según el periodo seleccionado
                val lista = if (uiState.period == RankingPeriod.SEMANAL) {
                    RetrofitClient.rankingApi.getRankingSemanal(tipo)
                } else {
                    RetrofitClient.rankingApi.getRanking(tipo)
                }

                val puntosResponse = RetrofitClient.authApi.obtenerPuntos()
                val misPuntosReales = puntosResponse.puntos

                val posicionData = RetrofitClient.rankingApi.getMiPosicion(idUser = realId, tipoResiduo = tipo)

                uiState = uiState.copy(
                    username = userResponse.username,
                    userId = realId,
                    items = lista,
                    miPosicionReal = posicionData.posicion,
                    misPuntosEnRanking = misPuntosReales,
                    isLoading = false
                )
            } catch (e: Exception) {
                uiState = uiState.copy(isLoading = false, errorMessage = e.localizedMessage)
            }
        }
    }
}