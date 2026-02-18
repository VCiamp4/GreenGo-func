package com.example.laboratorio.ui.auth.network

import java.util.concurrent.atomic.AtomicReference

object TokenStore {
    private val accessRef = AtomicReference<String?>(null)
    private val refreshRef = AtomicReference<String?>(null)
    private val userIdRef = AtomicReference<Int?>(null)


    fun setTokens(access: String, refresh: String, userId: Int) {
        accessRef.set(access)
        refreshRef.set(refresh)
        userIdRef.set(userId)
    }

    fun access(): String? = accessRef.get()
    fun refresh(): String? = refreshRef.get()

    fun getUserId(): Int = userIdRef.get() ?: 0

    fun setAccess(access: String) {
        accessRef.set(access)
    }

    fun clear() {
        accessRef.set(null)
        refreshRef.set(null)
        userIdRef.set(null)
    }

    fun isLoggedIn(): Boolean {
        return !access().isNullOrBlank() && !refresh().isNullOrBlank()
    }
}
