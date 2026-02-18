package com.example.laboratorio.ui.network

import com.example.laboratorio.ui.ranking.PosicionResponse
import com.example.laboratorio.ui.ranking.RankingItem
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RankingApiService {

    @GET("/api/ranking/")
    suspend fun getRanking(
        @Query("tipo_residuo") tipoResiduo: String? = null
    ): List<RankingItem>

    // 2. Tu posición (Usa Query ?)
    @GET("/api/ranking/posicion/")
    suspend fun getMiPosicion(
        @Query("id_usuario") idUser: Int,
        @Query("tipo_residuo") tipoResiduo: String? = null
    ): PosicionResponse

    // 3. Semanal (Usa Query ?)
    @GET("/api/ranking/semanal/")
    suspend fun getRankingSemanal(
        @Query("tipo_residuo") tipoResiduo: String? = null
    ): List<RankingItem>

}
