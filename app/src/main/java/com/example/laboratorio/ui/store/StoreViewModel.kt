package com.example.laboratorio.ui.main.store

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.laboratorio.ui.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

// Asegúrate de que tu data class tenga el campo isOwned
data class StoreItem(
    val id: String,
    val name: String,
    val description: String,
    val cost: Int,
    val isOwned: Boolean = false
)

data class StoreUiState(
    val points: Int = 0,
    val items: List<StoreItem> = emptyList(),
    val successMessage: String? = null,
    val errorMessage: String? = null
)

class StoreViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(StoreUiState())
    val uiState: StateFlow<StoreUiState> = _uiState

    // Definimos los productos "base" aquí
    private val baseItems = listOf(
        // Consumibles (Siempre se pueden comprar, isOwned siempre será false)
        StoreItem("boost_x2", "Booster de Puntos 2x", "Duplica tus puntos por cada reciclaje (1 hora).", 150),
        StoreItem("protector_racha", "Protector de Racha", "Evita que tu racha se reinicie si olvidas reciclar un día.", 350),
        StoreItem("estrella_suerte", "Estrella de la Suerte", "Aumenta la probabilidad de obtener logros raros (7 días).", 400),

        // Logros (Se compran una sola vez)
        StoreItem("shop_novato", "Logro Comprador Novato", "Desbloquea instantáneamente la insignia.", 200),
        StoreItem("shop_compulsivo", "Logro Comprador Compulsivo", "Desbloquea instantáneamente la insignia.", 500),
        StoreItem("shop_coleccionista", "Logro Coleccionista", "Desbloquea instantáneamente la insignia.", 750)
    )

    init {

        viewModelScope.launch {
            combine(
                UserRepository.userPoints,
                UserRepository.unlockedAchievements
            ) { points, unlockedIds ->

                val updatedItems = baseItems.map { item ->
                    // Si el ID del item está en la lista de desbloqueados, isOwned = true
                    item.copy(isOwned = unlockedIds.contains(item.id))
                }

                StoreUiState(points = points, items = updatedItems)
            }.collect { newState ->

                _uiState.value = newState.copy(
                    successMessage = _uiState.value.successMessage,
                    errorMessage = _uiState.value.errorMessage
                )
            }
        }
    }

    fun buyItem(item: StoreItem) {
        // Validación extra: No permitir comprar si ya lo tiene
        if (item.isOwned) return

        val currentPoints = UserRepository.userPoints.value

        if (currentPoints < item.cost) {
            _uiState.value = _uiState.value.copy(errorMessage = "No tenés puntos suficientes", successMessage = null)
            return
        }

        // Proceder con la compra
        UserRepository.deductPoints(item.cost)

        // Si es un logro, desbloquearlo
        if (item.id.startsWith("shop_")) {
            UserRepository.unlockAchievement(item.id)
        }

        _uiState.value = _uiState.value.copy(
            successMessage = "¡Comprado: ${item.name}!",
            errorMessage = null
        )
    }
}