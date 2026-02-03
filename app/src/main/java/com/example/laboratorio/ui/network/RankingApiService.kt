package com.example.laboratorio.ui.network

import com.example.laboratorio.ui.ranking.RankingItem
import retrofit2.http.GET
import retrofit2.http.Query

interface RankingApiService {

    @GET("api/ranking/")
    suspend fun getRankingGlobal(
        @Query("tipo_residuo") tipoResiduo: String? = null
    ): List<RankingItem>

    @GET("api/ranking/semanal/")
    suspend fun getRankingSemanal(
        @Query("tipo_residuo") tipoResiduo: String? = null
    ): List<RankingItem>
}
