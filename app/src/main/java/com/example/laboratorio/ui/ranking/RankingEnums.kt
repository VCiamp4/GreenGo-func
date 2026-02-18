package com.example.laboratorio.ui.ranking

import com.google.gson.annotations.SerializedName

enum class RankingPeriod(val label: String) {
    GLOBAL("Global"),
    SEMANAL("Semanal")
}

enum class RankingMode {
    PUNTOS,
    RESIDUO
}

data class PosicionResponse(
    val posicion: Int,
    @SerializedName("total_puntos") val totalPuntos: Int
)