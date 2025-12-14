package com.example.laboratorio.ui.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.laboratorio.ui.auth.network.DatosUsuarioResponse
import com.example.laboratorio.ui.auth.network.RetrofitClient
import kotlinx.coroutines.launch

data class MainMenuUiState(
    val isLoading: Boolean = false,
    val username: String? = null,
    val userId: Int? = null,
    val errorMessage: String? = null
)

class MainMenuViewModel : ViewModel() {

    var uiState by mutableStateOf(MainMenuUiState())
        private set

    fun loadUserData() {
        uiState = uiState.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            try {
                val response = RetrofitClient.authApi.datosUsuario()

                uiState = uiState.copy(
                    isLoading = false,
                    username = response.username,
                    userId = response.id
                )

            } catch (e: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = "No se pudieron obtener los datos del usuario"
                )
            }
        }
    }

}
