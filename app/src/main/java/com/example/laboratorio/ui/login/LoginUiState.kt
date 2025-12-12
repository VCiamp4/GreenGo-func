package com.example.laboratorio.ui.login

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val showPassword: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
