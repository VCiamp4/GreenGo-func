package com.example.laboratorio.ui.store

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StoreViewModel(
    initialPoints: Int
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        StoreUiState(
            points = initialPoints,
            items = listOf(
                StoreItem(
                    id = 1,
                    name = "Doble puntos",
                    description = "Duplica los puntos del próximo QR",
                    cost = 100,
                    icon = Icons.Filled.Bolt
                ),
                StoreItem(
                    id = 2,
                    name = "Scan rápido",
                    description = "Reduce el tiempo de escaneo",
                    cost = 150,
                    icon = Icons.Filled.Speed
                ),
                StoreItem(
                    id = 3,
                    name = "Bonus diario",
                    description = "Gana +50 pts al iniciar sesión",
                    cost = 200,
                    icon = Icons.Filled.EmojiEvents
                )
            )
        )
    )

    val uiState = _uiState.asStateFlow()

    fun buy(item: StoreItem) {
        val state = _uiState.value

        if (state.points < item.cost) {
            _uiState.update {
                it.copy(errorMessage = "No tenés puntos suficientes")
            }
            return
        }

        _uiState.update { it.copy(isBuying = true) }

        // simulamos compra
        kotlinx.coroutines.GlobalScope.launch {
            delay(800)
            _uiState.update {
                it.copy(
                    points = it.points - item.cost,
                    isBuying = false,
                    successMessage = "Compraste ${item.name} 🎉"
                )
            }
        }
    }

    fun clearMessages() {
        _uiState.update {
            it.copy(successMessage = null, errorMessage = null)
        }
    }
}
