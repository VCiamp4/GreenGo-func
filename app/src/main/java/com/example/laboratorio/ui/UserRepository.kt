package com.example.laboratorio.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object UserRepository {

    // --- DATOS EXISTENTES ---
    private val _userPoints = MutableStateFlow(1500)
    val userPoints: StateFlow<Int> = _userPoints.asStateFlow()

    private val _unlockedAchievements = MutableStateFlow(setOf<String>())
    val unlockedAchievements: StateFlow<Set<String>> = _unlockedAchievements.asStateFlow()

    private val _pointMultiplier = MutableStateFlow(1)
    val pointMultiplier: StateFlow<Int> = _pointMultiplier.asStateFlow()

    private val _hasStreakProtection = MutableStateFlow(false)
    val hasStreakProtection: StateFlow<Boolean> = _hasStreakProtection.asStateFlow()

    private val _triviaHints = MutableStateFlow(0)
    val triviaHints: StateFlow<Int> = _triviaHints.asStateFlow()

    // --- NUEVO: SISTEMA DE AVATARES ---

    // Lista de avatares disponibles en el juego (Nombre -> Icono)
    val availableAvatars = mapOf(
        "default" to Icons.Default.Person,
        "happy" to Icons.Default.EmojiEmotions,
        "robot" to Icons.Default.SmartToy,
        "pet" to Icons.Default.Pets,
        "cool" to Icons.Default.Face
    )

    // Avatar actual del usuario (Guardamos la key, ej: "robot")
    private val _currentAvatar = MutableStateFlow("default")
    val currentAvatar: StateFlow<String> = _currentAvatar.asStateFlow()

    // Avatares que el usuario ya tiene (Set de keys)
    private val _unlockedAvatars = MutableStateFlow(setOf("default"))
    val unlockedAvatars: StateFlow<Set<String>> = _unlockedAvatars.asStateFlow()

    // --- FUNCIONES ---

    fun deductPoints(amount: Int) {
        _userPoints.update { it - amount }
    }

    fun addPoints(amount: Int) {
        val multiplier = _pointMultiplier.value
        _userPoints.update { it + (amount * multiplier) }
    }

    fun unlockAchievement(achievementId: String) {
        _unlockedAchievements.update { it + achievementId }
    }

    fun activateBooster() { _pointMultiplier.value = 2 }
    fun activateStreakShield() { _hasStreakProtection.value = true }
    fun addHint() { _triviaHints.update { it + 1 } }
    fun consumeHint() { if (_triviaHints.value > 0) _triviaHints.update { it - 1 } }

    // NUEVO: Desbloquear y Equipar Avatar
    fun unlockAndEquipAvatar(avatarKey: String) {
        _unlockedAvatars.update { it + avatarKey }
        _currentAvatar.value = avatarKey // Lo equipamos automáticamente al ganarlo
    }

    // Helper para saber si ya lo tiene
    fun hasAvatar(avatarKey: String): Boolean {
        return _unlockedAvatars.value.contains(avatarKey)
    }

    fun equipAvatar(avatarKey: String) {
        if (_unlockedAvatars.value.contains(avatarKey)) {
            _currentAvatar.value = avatarKey
        }
    }
}