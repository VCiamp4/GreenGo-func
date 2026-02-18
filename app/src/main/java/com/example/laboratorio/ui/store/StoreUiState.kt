package com.example.laboratorio.ui.store

import com.example.laboratorio.ui.main.store.MysteryPrizeResult

data class StoreUiState(
    val points: Int = 0,
    val items: List<StoreItem> = emptyList(),
    val successMessage: String? = null,
    val errorMessage: String? = null,
    // Estados para la animación de la caja
    val showMysteryBoxDialog: Boolean = false,
    val mysteryPrize: MysteryPrizeResult? = null
)
