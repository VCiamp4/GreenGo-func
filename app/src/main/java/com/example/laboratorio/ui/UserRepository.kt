package com.example.laboratorio.ui

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// Objeto Singleton para compartir datos entre pantallas
object UserRepository {

    // 1. PUNTOS DEL USUARIO (Inicializamos en 1500 para pruebas)
    private val _userPoints = MutableStateFlow(1500)
    val userPoints: StateFlow<Int> = _userPoints.asStateFlow()

    // 2. LOGROS DESBLOQUEADOS (Un Set de IDs de logros. Empieza vacío)
    private val _unlockedAchievements = MutableStateFlow(setOf<String>())
    val unlockedAchievements: StateFlow<Set<String>> = _unlockedAchievements.asStateFlow()

    // Funciones para modificar los datos
    fun deductPoints(amount: Int) {
        _userPoints.update { it - amount }
    }

    fun addPoints(amount: Int) {
        _userPoints.update { it + amount }
    }

    fun unlockAchievement(achievementId: String) {
        _unlockedAchievements.update { currentSet ->
            currentSet + achievementId // Agrega el ID al set
        }
    }

    fun isUnlocked(achievementId: String): Boolean {
        return _unlockedAchievements.value.contains(achievementId)
    }
}