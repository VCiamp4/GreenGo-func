package com.example.laboratorio.ui.datastore

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "store_prefs")

class StoreDataStore(private val context: Context) {

    companion object {
        val POINTS_KEY = intPreferencesKey("points")
        val BOOSTERS_KEY = stringSetPreferencesKey("boosters")
    }

    val pointsFlow: Flow<Int> = context.dataStore.data.map { prefs ->
        prefs[POINTS_KEY] ?: 0
    }

    val boostersFlow: Flow<Set<String>> = context.dataStore.data.map { prefs ->
        prefs[BOOSTERS_KEY] ?: emptySet()
    }

    suspend fun savePoints(points: Int) {
        context.dataStore.edit { prefs ->
            prefs[POINTS_KEY] = points
        }
    }

    suspend fun addBooster(boosterId: String) {
        context.dataStore.edit { prefs ->
            val current = prefs[BOOSTERS_KEY] ?: emptySet()
            prefs[BOOSTERS_KEY] = current + boosterId
        }
    }
}
