package com.example.laboratorio.ui.store

data class StoreUiState(
    val points: Int = 0,
    val items: List<StoreItem> = emptyList(),
    val isBuying: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null
)
