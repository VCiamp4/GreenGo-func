package com.example.laboratorio.ui.main.store

import androidx.lifecycle.ViewModel
import com.example.laboratorio.ui.store.StoreItem
import com.example.laboratorio.ui.store.StoreUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class StoreViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        StoreUiState(
            points = 120, // 🔹 por ahora HARDCODEADO
            items = listOf(
                StoreItem(
                    id = "boost_x2",
                    name = "Booster x2",
                    description = "Duplica los puntos del próximo QR",
                    cost = 100
                ),
                StoreItem(
                    id = "retry_qr",
                    name = "Reintento QR",
                    description = "Permite volver a escanear un QR fallido",
                    cost = 50
                )
            )
        )
    )

    val uiState: StateFlow<StoreUiState> = _uiState

    fun buyItem(item: StoreItem) {
        val state = _uiState.value

        if (state.points < item.cost) {
            _uiState.value = state.copy(
                errorMessage = "No tenés puntos suficientes",
                successMessage = null
            )
            return
        }

        // Compra válida
        _uiState.value = state.copy(
            points = state.points - item.cost,
            successMessage = "Compraste ${item.name}",
            errorMessage = null
        )
    }

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(
            errorMessage = null,
            successMessage = null
        )
    }
}
