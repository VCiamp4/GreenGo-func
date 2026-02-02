package com.example.laboratorio.ui.network

import retrofit2.http.GET
import retrofit2.http.Query

data class RankingUserDto(
    val user_id: Int,
    val username: String,
    val puntos: Int
)

interface RankingApiService {

    @GET("api/ranking/")
    suspend fun getRanking(
        @Query("tipo_residuo") tipoResiduo: String? = null
    ): List<RankingUserDto>

    @GET("api/ranking/semanal/")
    suspend fun getWeeklyRanking(
        @Query("tipo_residuo") tipoResiduo: String? = null
    ): List<RankingUserDto>
}

