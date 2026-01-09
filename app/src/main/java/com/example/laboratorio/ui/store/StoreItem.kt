package com.example.laboratorio.ui.store

import androidx.compose.ui.graphics.vector.ImageVector

data class StoreItem(
    val id: Int,
    val name: String,
    val description: String,
    val cost: Int,
    val icon: ImageVector
)
