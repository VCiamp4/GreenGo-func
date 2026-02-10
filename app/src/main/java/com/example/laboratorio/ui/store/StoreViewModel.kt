package com.example.laboratorio.ui.main.store

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.laboratorio.data.UserRepository
import com.example.laboratorio.ui.store.StoreItem
import com.example.laboratorio.ui.store.StoreUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

// --- MODELOS DE DATOS ---



// Datos para el resultado de la Caja Misteriosa
data class MysteryPrizeResult(
    val type: PrizeType,
    val amount: Int = 0,
    val avatarName: String? = null,
    val message: String
)

enum class PrizeType { COINS, AVATAR, EMPTY }

// Estado de la UI


// --- VIEWMODEL ---

class StoreViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(StoreUiState())
    val uiState: StateFlow<StoreUiState> = _uiState

    // LISTA DE PRODUCTOS (Base)
    private val baseItems = listOf(
        // Consumibles (Siempre se pueden comprar)
        StoreItem("boost_x2", "Booster de Puntos 2x", "Duplica tus puntos por cada reciclaje (1 hora).", 150),
        StoreItem("protector_racha", "Protector de Racha", "Evita que tu racha se reinicie si olvidas reciclar un día.", 350),
        StoreItem("estrella_suerte", "Estrella de la Suerte", "Aumenta la probabilidad de obtener logros raros (7 días).", 400),

        // Objetos Especiales (Gacha y Utilidad)
        StoreItem("mystery_box", "Caja Misteriosa", "¡Prueba tu suerte! Puede contener Avatares exclusivos o monedas.", 100),
        StoreItem("trivia_hint", "Pista de Trivia", "Úsala en el desafío diario para ver la respuesta correcta.", 75),

        // Logros (Se compran una sola vez, se bloquean al tenerlos)
        StoreItem("shop_novato", "Logro Comprador Novato", "Desbloquea instantáneamente la insignia.", 200),
        StoreItem("shop_compulsivo", "Logro Comprador Compulsivo", "Desbloquea instantáneamente la insignia.", 500),
        StoreItem("shop_coleccionista", "Logro Coleccionista", "Desbloquea instantáneamente la insignia.", 750)
    )

    init {
        // Observamos puntos y logros para actualizar la lista en tiempo real
        viewModelScope.launch {
            combine(
                UserRepository.userPoints,
                UserRepository.unlockedAchievements
            ) { points, unlockedIds ->

                val updatedItems = baseItems.map { item ->
                    // Si el ID del item está en los logros desbloqueados, lo marcamos como 'owned'
                    val isOwned = unlockedIds.contains(item.id)
                    item.copy(isOwned = isOwned)
                }

                StoreUiState(
                    points = points,
                    items = updatedItems,
                    // Mantenemos los estados de la caja si ya existían
                    showMysteryBoxDialog = _uiState.value.showMysteryBoxDialog,
                    mysteryPrize = _uiState.value.mysteryPrize
                )
            }.collect { newState ->
                // Mantenemos los mensajes de éxito/error al actualizar
                _uiState.value = newState.copy(
                    successMessage = _uiState.value.successMessage,
                    errorMessage = _uiState.value.errorMessage
                )
            }
        }
    }

    fun buyItem(item: StoreItem) {
        if (item.isOwned) return

        val currentPoints = UserRepository.userPoints.value

        if (currentPoints < item.cost) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "No tenés puntos suficientes",
                successMessage = null
            )
            return
        }

        // --- CASO ESPECIAL: CAJA MISTERIOSA ---
        if (item.id == "mystery_box") {
            UserRepository.deductPoints(item.cost)
            openMysteryBox() // Llamamos a la lógica de sorteo
            return
        }

        // --- CASO NORMAL ---
        // 1. Cobrar
        UserRepository.deductPoints(item.cost)

        // 2. Variable para mensaje
        var customMessage = "¡Comprado: ${item.name}!"

        // 3. Activar efecto según ID
        when (item.id) {
            "boost_x2" -> UserRepository.activateBooster()

            "protector_racha" -> UserRepository.activateStreakShield()

            "trivia_hint" -> {
                UserRepository.addHint()
                customMessage = "¡Pista añadida a tu inventario!"
            }
        }

        // 4. Si es un logro, desbloquearlo
        if (item.id.startsWith("shop_")) {
            UserRepository.unlockAchievement(item.id)
        }

        // 5. Actualizar UI
        _uiState.value = _uiState.value.copy(
            successMessage = customMessage,
            errorMessage = null
        )
    }

    // --- LÓGICA DE LA CAJA MISTERIOSA (ANIMACIÓN) ---
    private fun openMysteryBox() {
        val random = (1..100).random()

        val result = when {
            // 15% Probabilidad de Avatar Nuevo
            random <= 15 -> {
                val allKeys = UserRepository.availableAvatars.keys.toList()
                val owned = UserRepository.unlockedAvatars.value
                val missing = allKeys.filter { !owned.contains(it) }

                if (missing.isNotEmpty()) {
                    val newAvatar = missing.random()
                    UserRepository.unlockAndEquipAvatar(newAvatar)
                    MysteryPrizeResult(PrizeType.AVATAR, avatarName = newAvatar, message = "¡Nuevo Avatar!")
                } else {
                    // Si ya tiene todos, premio consuelo grande
                    UserRepository.addPoints(500)
                    MysteryPrizeResult(PrizeType.COINS, amount = 500, message = "¡Colección completa! 500 monedas.")
                }
            }
            // 15% Premio Grande Monedas
            random <= 30 -> {
                UserRepository.addPoints(300)
                MysteryPrizeResult(PrizeType.COINS, amount = 300, message = "¡Premio Grande!")
            }
            // 30% Premio Medio
            random <= 60 -> {
                UserRepository.addPoints(100)
                MysteryPrizeResult(PrizeType.COINS, amount = 100, message = "¡Ganaste monedas!")
            }
            // 40% Premio Pequeño
            else -> {
                UserRepository.addPoints(20)
                MysteryPrizeResult(PrizeType.COINS, amount = 20, message = "Premio consuelo.")
            }
        }

        // Mostramos el diálogo de animación con el resultado calculado
        _uiState.value = _uiState.value.copy(
            showMysteryBoxDialog = true,
            mysteryPrize = result,
            successMessage = null,
            errorMessage = null
        )
    }

    fun closeMysteryBox() {
        _uiState.value = _uiState.value.copy(
            showMysteryBoxDialog = false,
            mysteryPrize = null
        )
    }

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(
            errorMessage = null,
            successMessage = null
        )
    }
}