package com.example.laboratorio.ui.store

data class StoreUiState(
    val points: Int = 0,
    val items: List<StoreItem> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)
