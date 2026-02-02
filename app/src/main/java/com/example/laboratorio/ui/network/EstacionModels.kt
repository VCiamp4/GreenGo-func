package com.example.laboratorio.ui.network

data class Estacion(
    val id: Int,
    val nombre: String,
    val latitud: Double,
    val longitud: Double
)


data class EstacionRequest(
    val id_residuo: String
)