package com.example.laboratorio.ui.auth.network

import com.example.laboratorio.ui.network.model.ReclamarResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

import com.example.laboratorio.ui.network.Estacion
import com.example.laboratorio.ui.network.model.PuntosResponse
import retrofit2.http.Query


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

    @GET("/api/estaciones/")
    suspend fun estaciones(): List<Estacion>

    @GET("/api/estaciones/{id}")
    suspend fun getEstacionDetalle(@Path("id") id: Int): Estacion

    @POST("api/reclamar_residuo/")
    suspend fun reclamarResiduo(
        @Body request: Map<String, String>
    ): ReclamarResponse

    @GET("/api/puntos/")
    suspend fun obtenerPuntos(
        @Query("id_user") idUser: Int? = null
    ): PuntosResponse
}
