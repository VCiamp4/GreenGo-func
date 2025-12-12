package com.example.laboratorio.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    var uiState by mutableStateOf(LoginUiState())
        private set

    fun onEmailChange(newEmail: String) {
        uiState = uiState.copy(email = newEmail)
    }

    fun onPasswordChange(newPassword: String) {
        uiState = uiState.copy(password = newPassword)
    }

    fun toggleShowPassword() {
        uiState = uiState.copy(showPassword = !uiState.showPassword)
    }

    fun login(
        onSuccess: (userName: String, email: String) -> Unit
    ) {
        if (uiState.email.isBlank() || uiState.password.isBlank()) {
            uiState = uiState.copy(errorMessage = "Completa todos los campos")
            return
        }

        uiState = uiState.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            // Simulamos el login, como tu setTimeout de React
            delay(1500)
            val name = uiState.email.substringBefore("@")
            uiState = uiState.copy(isLoading = false)
            onSuccess(name, uiState.email)
        }
    }

    fun clearError() {
        uiState = uiState.copy(errorMessage = null)
    }
}
