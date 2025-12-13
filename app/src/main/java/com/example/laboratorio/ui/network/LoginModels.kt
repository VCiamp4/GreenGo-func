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

