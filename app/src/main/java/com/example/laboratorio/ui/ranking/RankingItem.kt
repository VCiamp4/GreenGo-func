package com.example.laboratorio.ui.ranking

import com.google.gson.annotations.SerializedName


data class RankingItem(
    val username: String,
    @SerializedName("total_puntos")
    val totalPuntos: Int,
)
