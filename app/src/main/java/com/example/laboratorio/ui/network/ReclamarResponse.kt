package com.example.laboratorio.ui.network.model

data class ReclamarResponse(
    val mensaje: String,
    val categoria: String,
    val puntos: Int,
    val puntos_totales: Int
)

data class PuntosResponse(
    val puntos: Int
)