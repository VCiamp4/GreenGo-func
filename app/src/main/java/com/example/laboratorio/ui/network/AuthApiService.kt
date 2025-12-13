package com.example.laboratorio.ui.auth.network

import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("/api/login/")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse

    @POST("/api/signup/")
    suspend fun signup(@Body request: SignUpRequest): LoginResponse
}
