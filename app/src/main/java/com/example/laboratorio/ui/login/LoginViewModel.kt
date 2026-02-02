package com.example.laboratorio.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.laboratorio.ui.auth.network.LoginRequest
import com.example.laboratorio.ui.auth.network.RetrofitClient
import com.example.laboratorio.ui.auth.network.TokenStore
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

    fun login(onSuccess: (String, String) -> Unit) {
        if (uiState.email.isBlank() || uiState.password.isBlank()) {
            uiState = uiState.copy(errorMessage = "Completa todos los campos")
            return
        }

        uiState = uiState.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            try {
                val response = RetrofitClient.authApi.login(
                    LoginRequest(
                        username = uiState.email,
                        password = uiState.password
                    )
                )


                TokenStore.setTokens(access = response.access, refresh = response.refresh)

                uiState = uiState.copy(isLoading = false)

                onSuccess(uiState.email, uiState.email)

            } catch (e: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = "Usuario o contraseña incorrectos"
                )
            }
        }
    }
}
