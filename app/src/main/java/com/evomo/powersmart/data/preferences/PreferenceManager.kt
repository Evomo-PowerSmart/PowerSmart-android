package com.evomo.powersmart.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferenceManager @Inject constructor(
    private val context: Context,
) {
    suspend fun setIsTokenSent(isTokenSent: Boolean) {
        context.dataStore.edit { settings ->
            settings[PreferenceKeys.IS_TOKEN_SENT] = isTokenSent
        }
    }

    fun getIsTokenSent(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[PreferenceKeys.IS_TOKEN_SENT] ?: false
        }
    }

    suspend fun setTheme(theme: Theme) {
        context.dataStore.edit { settings ->
            settings[PreferenceKeys.THEME] = theme.value
        }
    }

    fun getTheme(): Flow<Theme> {
        return context.dataStore.data.map { preferences ->
            preferences[PreferenceKeys.THEME]?.let { themeValue ->
                Theme.entries.find { it.value == themeValue }
            } ?: Theme.SYSTEM_DEFAULT
        }
    }
}

private val readOnlyProperty = preferencesDataStore(name = "preference")

val Context.dataStore: DataStore<Preferences> by readOnlyProperty

private object PreferenceKeys {
    val IS_TOKEN_SENT = booleanPreferencesKey("is_token_sent")
    val THEME = stringPreferencesKey("theme")
}