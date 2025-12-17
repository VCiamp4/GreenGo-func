package com.example.laboratorio.ui.signup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.laboratorio.ui.auth.network.RetrofitClient
import com.example.laboratorio.ui.auth.network.SignUpRequest
import com.example.laboratorio.ui.auth.network.TokenStore
import kotlinx.coroutines.launch

class SignUpViewModel : ViewModel() {

    var uiState by mutableStateOf(SignUpUiState())
        private set

    fun onUsernameChange(value: String) {
        uiState = uiState.copy(username = value)
    }

    fun onEmailChange(value: String) {
        uiState = uiState.copy(email = value)
    }

    fun onPasswordChange(value: String) {
        uiState = uiState.copy(password = value)
    }

    fun onConfirmPasswordChange(value: String) {
        uiState = uiState.copy(confirmPassword = value)
    }

    fun toggleShowPassword() {
        uiState = uiState.copy(showPassword = !uiState.showPassword)
    }

    fun toggleShowConfirmPassword() {
        uiState = uiState.copy(showConfirmPassword = !uiState.showConfirmPassword)
    }

    fun signup(onSuccess: (email: String) -> Unit) {
        if (uiState.username.isBlank() || uiState.password.isBlank() || uiState.confirmPassword.isBlank()) {
            uiState = uiState.copy(errorMessage = "Completa todos los campos")
            return
        }
        if (uiState.password != uiState.confirmPassword) {
            uiState = uiState.copy(errorMessage = "Las contraseñas no coinciden")
            return
        }

        uiState = uiState.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            try {
                val response = RetrofitClient.authApi.signup(
                    SignUpRequest(
                        username = uiState.username,
                        password = uiState.password
                    )
                )

                TokenStore.setTokens(access = response.access, refresh = response.refresh)

                uiState = uiState.copy(isLoading = false)


                onSuccess(uiState.username)

            } catch (e: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = "No se pudo crear la cuenta (usuario inválido o ya existe)"
                )
            }
        }
    }
}
