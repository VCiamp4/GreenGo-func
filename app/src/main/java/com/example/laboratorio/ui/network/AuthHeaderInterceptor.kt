package com.example.laboratorio.ui.auth.network

import okhttp3.Interceptor
import okhttp3.Response

class AuthHeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val path = original.url.encodedPath
        val isAuthEndpoint =
            path.startsWith("/api/login/") ||
                    path.startsWith("/api/signup/") ||
                    path.startsWith("/api/token/refresh/") ||
                    path.startsWith("/api/logout/")

        val access = TokenStore.access()

        val request = if (!isAuthEndpoint && !access.isNullOrBlank()) {
            original.newBuilder()
                .addHeader("Authorization", "Bearer $access")
                .build()
        } else {
            original
        }

        return chain.proceed(request)
    }
}
