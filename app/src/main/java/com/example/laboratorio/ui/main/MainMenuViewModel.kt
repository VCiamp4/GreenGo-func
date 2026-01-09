package com.example.laboratorio.ui.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.laboratorio.ui.auth.network.ApiErrorResponse
import com.example.laboratorio.ui.auth.network.ReclamarResiduoRequest
import com.example.laboratorio.ui.auth.network.RetrofitClient
import com.google.gson.Gson
import kotlinx.coroutines.launch

data class MainMenuUiState(
    val isLoading: Boolean = false,
    val isClaiming: Boolean = false,

    val username: String? = null,
    val userId: Int? = null,
    val puntosTotales: Int = 0,

    val reclamarMessage: String? = null,
    val reclamarError: String? = null,

    val categoria: String? = null,
    val puntos: Int? = null,

    val errorMessage: String? = null
)

class MainMenuViewModel : ViewModel() {

    var uiState by mutableStateOf(MainMenuUiState())
        private set

    fun onScanResult(qrContent: String?) {
        if (qrContent.isNullOrBlank()) {
            uiState = uiState.copy(
                reclamarError = "QR inválido"
            )
            return
        }

        uiState = uiState.copy(
            isClaiming = true,
            reclamarError = null,
            reclamarMessage = null
        )

        viewModelScope.launch {
            try {
                val response = RetrofitClient.authApi.reclamarResiduo(
                    mapOf("codigo" to qrContent)
                )

                uiState = uiState.copy(
                    isClaiming = false,
                    reclamarMessage = response.mensaje,
                    categoria = response.categoria,
                    puntos = response.puntos,
                    puntosTotales = response.puntos_totales
                )

            } catch (e: Exception) {
                uiState = uiState.copy(
                    isClaiming = false,
                    reclamarError = "No se pudo reclamar el residuo"
                )
            }
        }
    }

    private fun reclamarResiduo(idResiduo: String) {
        uiState = uiState.copy(isClaiming = true, reclamarError = null, reclamarMessage = null)

        viewModelScope.launch {
            try {
                val response = RetrofitClient.authApi.reclamarResiduo(ReclamarResiduoRequest(id_residuo = idResiduo))

                if (response.isSuccessful) {
                    val body = response.body()
                    uiState = uiState.copy(
                        isClaiming = false,
                        reclamarMessage = body?.message,
                        categoria = body?.categoria,
                        puntos = body?.puntos
                    )
                    // Vuelve a cargar los datos del usuario para actualizar los puntos totales
                    loadUserData()
                } else {
                    val errorBody = response.errorBody()?.string()
                    var errorMessage = "Ocurrió un error inesperado."
                    if (errorBody != null) {
                        try {
                            val errorResponse = Gson().fromJson(errorBody, ApiErrorResponse::class.java)
                            errorMessage = errorResponse.error
                        } catch (e: Exception) {
                            errorMessage = when(response.code()) {
                                400 -> "Error en la petición. El residuo puede haber sido reclamado."
                                404 -> "Residuo no encontrado."
                                else -> "Error del servidor."
                            }
                        }
                    }
                    uiState = uiState.copy(
                        isClaiming = false,
                        reclamarError = errorMessage
                    )
                }

            } catch (e: Exception) {
                uiState = uiState.copy(
                    isClaiming = false,
                    reclamarError = "Error de red. No se pudo conectar al servidor."
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
                    userId = response.id,
                    puntosTotales = response.puntos_totales
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
            puntos = null
        )
    }

}
