package com.example.laboratorio.ui.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.laboratorio.ui.auth.network.ReclamarResiduoRequest
import com.example.laboratorio.ui.auth.network.RetrofitClient
import kotlinx.coroutines.launch

data class MainMenuUiState(
    val isLoading: Boolean = false,
    val isClaiming: Boolean = false,
    val username: String? = null,
    val userId: Int? = null,
    val errorMessage: String? = null,
    val qrCodeContent: String? = null,
    val reclamarMessage: String? = null,
    val reclamarError: String? = null,
    val categoria: String? = null,
    val puntos: Int? = null
)

class MainMenuViewModel : ViewModel() {

    var uiState by mutableStateOf(MainMenuUiState())
        private set

    fun onScanResult(result: String?) {
        if (!result.isNullOrBlank()) {
            reclamarResiduo(result)
        }
    }

    private fun reclamarResiduo(idResiduo: String) {
        uiState = uiState.copy(isClaiming = true, reclamarError = null, reclamarMessage = null)

        viewModelScope.launch {
            try {
                val response = RetrofitClient.authApi.reclamarResiduo(ReclamarResiduoRequest(id_residuo = idResiduo))

                when (response.code()) {
                    200 -> {
                        val body = response.body()
                        uiState = uiState.copy(
                            isClaiming = false,
                            reclamarMessage = body?.message,
                            categoria = body?.categoria,
                            puntos = body?.puntos
                        )
                    }
                    400 -> {
                        uiState = uiState.copy(
                            isClaiming = false,
                            reclamarError = "El residuo ya fue reclamado o hubo un error."
                        )
                    }
                    404 -> {
                        uiState = uiState.copy(
                            isClaiming = false,
                            reclamarError = "No se encontró el residuo."
                        )
                    }
                    else -> {
                        uiState = uiState.copy(
                            isClaiming = false,
                            reclamarError = "Error al reclamar el residuo."
                        )
                    }
                }
            } catch (e: Exception) {
                uiState = uiState.copy(
                    isClaiming = false,
                    reclamarError = "Error de red al reclamar el residuo."
                )
            }
        }
    }

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

    fun clearReclamarStatus() {
        uiState = uiState.copy(
            reclamarMessage = null,
            reclamarError = null,
            categoria = null,
            puntos = null,
            qrCodeContent = null
        )
    }

}
