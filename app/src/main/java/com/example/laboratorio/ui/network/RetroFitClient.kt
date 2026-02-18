package com.example.laboratorio.ui.auth.network

import com.example.laboratorio.ui.network.RankingApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {


    //ip computadora grego para prueba en telefono fisico
    private const val BASE_URL = "http://192.168.0.112:8000/"

    //ip computadora valen para probar en pc
    // const val BASE_URL = "http://10.0.2.2:8000/"
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(AuthHeaderInterceptor())
            .authenticator(TokenRefreshAuthenticator(BASE_URL))
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    val authApi: AuthApiService by lazy {
        retrofit.create(AuthApiService::class.java)
    }

    val rankingApi: RankingApiService by lazy {
        retrofit.create(RankingApiService::class.java)
    }
}
