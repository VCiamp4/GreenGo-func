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
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch

data class MainMenuUiState(
    val isLoading: Boolean = false,
    val isClaiming: Boolean = false,
    val username: String? = null,
    val userId: Int? = null,
    val puntosTotales: Int = 0,
    val errorMessage: String? = null,
    val reclamarMessage: String? = null,
    val reclamarError: String? = null,
    val categoria: String? = null,
    val puntos: Int? = null
)

class MainMenuViewModel : ViewModel() {

    var uiState by mutableStateOf(MainMenuUiState())
        private set

    fun onScanResult(result: String?) {
        val rawContent = result?.trim() ?: return
        
        var idToUse = rawContent

        // Intentamos extraer el ID si el QR es un JSON (como muestra el log)
        try {
            val gson = Gson()
            val mapType = object : TypeToken<Map<String, Any>>() {}.type
            val data: Map<String, Any> = gson.fromJson(rawContent, mapType)
            
            if (data.containsKey("ID Residuo")) {
                idToUse = data["ID Residuo"].toString()
            }
        } catch (e: Exception) {
            // Si no es JSON o no tiene el campo, usamos el contenido original
        }

        if (idToUse.isNotBlank()) {
            reclamarResiduo(idToUse)
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
                    loadUserData()
                } else {
                    val errorBody = response.errorBody()?.string()
                    var errorMessage = "Error del servidor (Código ${response.code()})"
                    if (errorBody != null) {
                        try {
                            val errorResponse = Gson().fromJson(errorBody, ApiErrorResponse::class.java)
                            errorMessage = errorResponse.error
                        } catch (e: Exception) {
                            errorMessage = "Respuesta del servidor: $errorBody"
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
                    reclamarError = "Error de conexión: ${e.message}"
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
