package com.example.laboratorio.ui.signup

data class SignUpUiState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val showPassword: Boolean = false,
    val showConfirmPassword: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
