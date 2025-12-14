package com.example.laboratorio.ui.auth.network

import java.util.concurrent.atomic.AtomicReference

object TokenStore {
    private val accessRef = AtomicReference<String?>(null)
    private val refreshRef = AtomicReference<String?>(null)

    fun setTokens(access: String, refresh: String) {
        accessRef.set(access)
        refreshRef.set(refresh)
    }

    fun access(): String? = accessRef.get()
    fun refresh(): String? = refreshRef.get()

    fun setAccess(access: String) {
        accessRef.set(access)
    }

    fun clear() {
        accessRef.set(null)
        refreshRef.set(null)
    }

    fun isLoggedIn(): Boolean {
        return !access().isNullOrBlank() && !refresh().isNullOrBlank()
    }
}
