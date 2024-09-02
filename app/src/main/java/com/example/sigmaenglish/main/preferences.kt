package com.example.sigmaenglish.main

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferencesManager @Inject constructor(private val context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "UserPreferences")

    object UserPreferencesKeys {
        val HIGHEST_STREAK = intPreferencesKey("highest_streak")
        val LOGGING_STREAK = intPreferencesKey("logging_streak")
    }

    // Write Data
    suspend fun updateHighestStreak(context: Context, highestStreak: Int) {
        context.dataStore.edit { preferences ->
            preferences[UserPreferencesKeys.HIGHEST_STREAK] = highestStreak
        }
    }

    suspend fun updateLoggingStreak(context: Context, loggingStreak: Int) {
        context.dataStore.edit { preferences ->
            preferences[UserPreferencesKeys.LOGGING_STREAK] = loggingStreak
        }
    }

    suspend fun checkForUpdateHS(context: Context, newStreak: Int) {
        val currentHighestStreak = highestStreakFlow.first()
        if (currentHighestStreak < newStreak) {
            updateHighestStreak(context, newStreak)
        }
    }
    // Read Data
    val highestStreakFlow: Flow<Int> = context.dataStore.data
        .map { preferences ->
            preferences[UserPreferencesKeys.HIGHEST_STREAK] ?: 0
        }

    val loggingStreakFlow: Flow<Int> = context.dataStore.data
        .map { preferences ->
            preferences[UserPreferencesKeys.LOGGING_STREAK] ?: 0
        }
}
