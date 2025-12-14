package com.example.laboratorio.ui.auth.network

import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TokenRefreshAuthenticator(
    private val baseUrl: String
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {

        // Evitar loop infinito
        if (responseCount(response) >= 2) return null

        val refreshToken = TokenStore.refresh() ?: return null

        return try {
            // Retrofit SIN interceptors para evitar recursión
            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val api = retrofit.create(AuthApiService::class.java)

            val newAccessToken = runBlocking {
                api.refresh(RefreshRequest(refreshToken)).access
            }

            // Guardamos el nuevo access token
            TokenStore.setAccess(newAccessToken)

            // Reintentamos la request original con el nuevo token
            response.request.newBuilder()
                .header("Authorization", "Bearer $newAccessToken")
                .build()

        } catch (e: Exception) {
            // Si falla el refresh, forzamos logout
            TokenStore.clear()
            null
        }
    }

    private fun responseCount(response: Response): Int {
        var result = 1
        var prior = response.priorResponse
        while (prior != null) {
            result++
            prior = prior.priorResponse
        }
        return result
    }
}
