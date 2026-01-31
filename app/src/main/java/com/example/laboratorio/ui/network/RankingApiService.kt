package com.example.laboratorio.ui.network

import com.example.laboratorio.ui.network.models.RankingEntry
import retrofit2.http.GET
import retrofit2.http.Query

interface RankingApiService {

    @GET("api/ranking/")
    suspend fun getRankingGlobal(
        @Query("tipo_residuo") tipoResiduo: String? = null
    ): List<RankingEntry>

    @GET("api/ranking/semanal")
    suspend fun getRankingSemanal(
        @Query("tipo_residuo") tipoResiduo: String? = null
    ): List<RankingEntry>
}
