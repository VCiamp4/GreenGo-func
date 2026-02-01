package com.example.laboratorio.ui.auth.network

/**
 * Representa la petición para reclamar un residuo escaneado.
 */
data class ReclamarResiduoRequest(
    val id_residuo: String
)

/**
 * Representa la respuesta exitosa del servidor tras reclamar un residuo.
 */
data class ReclamarResiduoResponse(
    val message: String,
    val categoria: String,
    val puntos: Int
)

/**
 * Modelo para capturar los mensajes de error específicos de la API de residuos.
 */
data class ApiErrorResponse(
    val error: String
)
