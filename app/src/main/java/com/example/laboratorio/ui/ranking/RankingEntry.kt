package com.example.laboratorio.ui.ranking

data class RankingEntry(
    val position: Int,
    val username: String,
    val totalPuntos: Int,
    val residueType: String? = null
)
