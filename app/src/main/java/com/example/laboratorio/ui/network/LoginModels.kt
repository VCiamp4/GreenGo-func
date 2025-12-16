package com.example.laboratorio.ui.auth.network

data class LoginRequest(
    val username: String,
    val password: String
)

data class LoginResponse(
    val access: String,
    val refresh: String
)

data class SignUpRequest(
    val username: String,
    val password: String
)

data class TokenPairResponse(
    val refresh: String,
    val access: String
)

data class RefreshRequest(
    val refresh: String
)

data class RefreshResponse(
    val access: String
)

data class LogoutRequest(
    val refresh: String
)

data class DatosUsuarioResponse(
    val id: Int,
    val username: String,
    val puntos_totales: Int
)

data class ReclamarResiduoRequest(
    val id_residuo: String
)

data class ReclamarResiduoResponse(
    val message: String,
    val categoria: String,
    val puntos: Int
)

data class ApiErrorResponse(
    val error: String
)
