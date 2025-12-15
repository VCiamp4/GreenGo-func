package com.example.laboratorio.ui.auth.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApiService {

    @POST("/api/login/")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse

    @POST("/api/signup/")
    suspend fun signup(@Body request: SignUpRequest): LoginResponse

    @POST("/api/token/refresh/")
    suspend fun refresh(@Body request: RefreshRequest): RefreshResponse

    @POST("/api/logout/")
    suspend fun logout(@Body request: LogoutRequest)

    @GET("/api/datos_usuario/")
    suspend fun datosUsuario(): DatosUsuarioResponse

    @POST("/api/residuo/reclamar/")
    suspend fun reclamarResiduo(@Body request: ReclamarResiduoRequest): Response<ReclamarResiduoResponse>
}
